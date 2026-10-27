package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.ai.OfflineLabEngine
import com.example.data.curriculum.CaSyllabusData
import com.example.data.curriculum.CuratedCatchLabScenarios
import com.example.data.curriculum.CuratedDifferences
import com.example.data.curriculum.CuratedExplanations
import com.example.data.curriculum.CuratedQuestionBank
import com.example.data.curriculum.PracticeQuestion
import com.example.data.local.CaLabDatabase
import com.example.data.model.CatchLabScenario
import com.example.data.model.ChatMessage
import com.example.data.model.ConceptDifference
import com.example.data.model.CatalogSortOrder
import com.example.data.model.ExperienceLevel
import com.example.data.model.ExplanationDepth
import com.example.data.model.LabMode
import com.example.data.model.LabVerdict
import com.example.data.model.MessageSender
import com.example.data.model.PaperCode
import com.example.data.model.PriorityTier
import com.example.data.model.SyllabusTopic
import com.example.data.repository.StudyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TeachLabState(
    val selectedPaper: PaperCode = PaperCode.PAPER_1,
    val selectedTopic: SyllabusTopic? = CaSyllabusData.topics.firstOrNull(),
    val messages: List<ChatMessage> = emptyList(),
    val isAiThinking: Boolean = false,
    val latestVerdict: LabVerdict? = null,
    val isVerdictModalVisible: Boolean = false,
    val turnCount: Int = 0
)

data class CatchLabState(
    val selectedPaper: PaperCode = PaperCode.PAPER_1,
    val scenarios: List<CatchLabScenario> = emptyList(),
    val activeScenarioIndex: Int = 0,
    val userSpottingInput: String = "",
    val isRevealed: Boolean = false,
    val scoreFeedback: String? = null,
    val isGeneratingAiScenario: Boolean = false,
    val aiErrorMessage: String? = null
)

data class AskLabState(
    val selectedPaper: PaperCode = PaperCode.PAPER_1,
    val selectedTopic: SyllabusTopic? = CaSyllabusData.topics.firstOrNull(),
    val selectedDepth: ExplanationDepth = ExplanationDepth.DEPTH_2,
    val customQuery: String = "",
    val displayedContent: String = "",
    val isLoading: Boolean = false,
    val showBookmarkedOnly: Boolean = false,
    val isPracticeQuestionsExpanded: Boolean = true
)

data class SpotDifferenceState(
    val selectedPaper: PaperCode? = null, // null = all papers
    val searchQuery: String = "",
    val selectedDifference: ConceptDifference? = CuratedDifferences.differences.firstOrNull(),
    val isSelfQuizMode: Boolean = false,
    val revealedColumns: Set<Int> = emptySet(),
    val showBookmarkedOnly: Boolean = false,
    val isGeneratingAiDifference: Boolean = false,
    val aiErrorMessage: String? = null
)

data class VerdictsDashboardState(
    val verdicts: List<LabVerdict> = emptyList(),
    val averageScore: Double = 0.0,
    val totalSessions: Int = 0,
    val isLoading: Boolean = false
)

data class McqTestState(
    val selectedPaper: PaperCode = PaperCode.PAPER_1,
    val questions: List<com.example.data.model.McqQuestion> = emptyList(),
    val userAnswers: Map<String, Int> = emptyMap(),
    val isSubmitted: Boolean = false,
    val scoreCorrect: Int = 0,
    val totalQuestions: Int = 0,
    val isGeneratingAi: Boolean = false,
    val errorMessage: String? = null
)

class CaLabViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("ca_lab_settings", android.content.Context.MODE_PRIVATE)

    // Experience Level StateFlow backed by SharedPreferences
    private val _experienceLevel = MutableStateFlow(
        runCatching {
            ExperienceLevel.valueOf(prefs.getString("experience_level", ExperienceLevel.KNOWLEDGED.name) ?: ExperienceLevel.KNOWLEDGED.name)
        }.getOrDefault(ExperienceLevel.KNOWLEDGED)
    )
    val experienceLevel: StateFlow<ExperienceLevel> = _experienceLevel.asStateFlow()

    // Catalogue Sort Order StateFlow backed by SharedPreferences
    private val _catalogSortOrder = MutableStateFlow(
        runCatching {
            CatalogSortOrder.valueOf(prefs.getString("catalog_sort_order", CatalogSortOrder.OFFICIAL_ICAI.name) ?: CatalogSortOrder.OFFICIAL_ICAI.name)
        }.getOrDefault(CatalogSortOrder.OFFICIAL_ICAI)
    )
    val catalogSortOrder: StateFlow<CatalogSortOrder> = _catalogSortOrder.asStateFlow()

    // Unified Global Context for Sticky Study Context Pill
    private val _activePaper = MutableStateFlow(PaperCode.PAPER_1)
    val activePaper: StateFlow<PaperCode> = _activePaper.asStateFlow()

    private val _activeTopic = MutableStateFlow(CaSyllabusData.topics.first())
    val activeTopic: StateFlow<SyllabusTopic> = _activeTopic.asStateFlow()

    private val _groqApiKey = MutableStateFlow(prefs.getString("groq_api_key", "") ?: "")
    val groqApiKey: StateFlow<String> = _groqApiKey.asStateFlow()

    private val _selectedAiProvider = MutableStateFlow(prefs.getString("ai_provider", "GEMINI") ?: "GEMINI")
    val selectedAiProvider: StateFlow<String> = _selectedAiProvider.asStateFlow()

    fun setGroqApiKey(key: String) {
        _groqApiKey.value = key
        prefs.edit().putString("groq_api_key", key).apply()
    }

    fun setAiProvider(provider: String) {
        _selectedAiProvider.value = provider
        prefs.edit().putString("ai_provider", provider).apply()
    }

    fun setExperienceLevel(level: ExperienceLevel) {
        _experienceLevel.value = level
        prefs.edit().putString("experience_level", level.name).apply()
        // Automatically sync default depth in AskLab
        setAskDepth(level.defaultDepth)
    }

    fun setCatalogSortOrder(order: CatalogSortOrder) {
        _catalogSortOrder.value = order
        prefs.edit().putString("catalog_sort_order", order.name).apply()
    }

    val repository = StudyRepository(
        CaLabDatabase.getInstance(application.applicationContext),
        application.applicationContext
    )

    // Navigation & Global Mode
    private val _currentMode = MutableStateFlow(LabMode.TEACH_LAB)
    val currentMode: StateFlow<LabMode> = _currentMode.asStateFlow()

    private val _isOffline = MutableStateFlow(repository.isOfflineMode)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    private val _showSettingsDialog = MutableStateFlow(false)
    val showSettingsDialog: StateFlow<Boolean> = _showSettingsDialog.asStateFlow()

    // Mode States
    private val _teachState = MutableStateFlow(TeachLabState())
    val teachState: StateFlow<TeachLabState> = _teachState.asStateFlow()

    private val _catchState = MutableStateFlow(
        CatchLabState(
            scenarios = CuratedCatchLabScenarios.scenarios
        )
    )
    val catchState: StateFlow<CatchLabState> = _catchState.asStateFlow()

    private val _askState = MutableStateFlow(AskLabState(selectedDepth = _experienceLevel.value.defaultDepth))
    val askState: StateFlow<AskLabState> = _askState.asStateFlow()

    private val _diffState = MutableStateFlow(SpotDifferenceState())
    val diffState: StateFlow<SpotDifferenceState> = _diffState.asStateFlow()

    // On-demand Verdicts Dashboard state
    private val _verdictsDashboard = MutableStateFlow(VerdictsDashboardState())
    val verdictsDashboard: StateFlow<VerdictsDashboardState> = _verdictsDashboard.asStateFlow()

    // Diagnostic MCQ Test State
    private val _mcqTestState = MutableStateFlow(McqTestState())
    val mcqTestState: StateFlow<McqTestState> = _mcqTestState.asStateFlow()

    val bookmarkedIds: StateFlow<Set<String>> = repository.getBookmarks()
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val topicProgressMap: StateFlow<Map<String, com.example.data.local.TopicProgressEntity>> = repository.getAllTopicProgress()
        .map { list -> list.associateBy { it.topicId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val errorLogs: StateFlow<List<com.example.data.local.ErrorLogEntity>> = repository.getErrorLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customDifferences: StateFlow<List<ConceptDifference>> = repository.getCustomDifferences()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun incrementWrittenSolved(topicId: String) {
        viewModelScope.launch {
            repository.recordWrittenPractice(topicId)
        }
    }

    fun incrementOralRecall(topicId: String) {
        viewModelScope.launch {
            repository.recordOralRecall(topicId)
        }
    }

    fun markTeacherRead(topicId: String, completed: Boolean = true) {
        viewModelScope.launch {
            repository.markTeacherReadStatus(topicId, completed)
        }
    }

    fun recordSwipeDrillStatus(topicId: String, status: Int) {
        viewModelScope.launch {
            repository.markSwipeDrillStatus(topicId, status)
        }
    }

    fun toggleBookmark(id: String, type: String, title: String, paperName: String) {
        viewModelScope.launch {
            repository.toggleBookmark(id, type, title, paperName)
        }
    }

    init {
        _askState.value = _askState.value.copy(selectedDepth = _experienceLevel.value.defaultDepth)
        resetTeachLabSession(_teachState.value.selectedTopic)
        updateAskLabContent()
        selectMcqTestPaper(PaperCode.PAPER_1)
    }

    // -------------------------------------------------------------
    // SORTED TOPICS & STICKY STUDY CONTEXT SYNC
    // -------------------------------------------------------------
    fun getSortedTopicsForPaper(paper: PaperCode): List<SyllabusTopic> {
        val topics = repository.getTopicsForPaper(paper)
        return when (_catalogSortOrder.value) {
            CatalogSortOrder.OFFICIAL_ICAI -> topics.sortedWith(
                compareBy<SyllabusTopic> { it.icaiChapterNumber }.thenBy { it.topicName }
            )
            CatalogSortOrder.PRIORITY_TIER -> topics.sortedWith(
                compareBy<SyllabusTopic> { it.priorityTier.ordinal }.thenBy { it.icaiChapterNumber }
            )
        }
    }

    fun setActivePaper(paper: PaperCode) {
        _activePaper.value = paper
        val sortedTopics = getSortedTopicsForPaper(paper)
        val selectedTopic = sortedTopics.firstOrNull() ?: CaSyllabusData.topics.first()
        _activeTopic.value = selectedTopic

        // 1. Sync Teach Lab
        _teachState.value = _teachState.value.copy(
            selectedPaper = paper,
            selectedTopic = selectedTopic
        )
        resetTeachLabSession(selectedTopic)

        // 2. Sync Catch Lab
        val paperScenarios = CuratedCatchLabScenarios.scenarios.filter { it.paper == paper }
        _catchState.value = _catchState.value.copy(
            selectedPaper = paper,
            scenarios = if (paperScenarios.isNotEmpty()) paperScenarios else CuratedCatchLabScenarios.scenarios,
            activeScenarioIndex = 0,
            userSpottingInput = "",
            isRevealed = false,
            scoreFeedback = null
        )

        // 3. Sync Ask Lab
        _askState.value = _askState.value.copy(
            selectedPaper = paper,
            selectedTopic = selectedTopic
        )
        updateAskLabContent()

        // 4. Sync Spot Difference
        val diffs = repository.getDifferencesForPaper(paper)
        _diffState.value = _diffState.value.copy(
            selectedPaper = paper,
            selectedDifference = diffs.firstOrNull()
        )

        // 5. Sync MCQ Test Paper
        selectMcqTestPaper(paper)
    }

    fun setActiveTopic(topic: SyllabusTopic) {
        _activeTopic.value = topic
        _activePaper.value = topic.paper

        // 1. Sync Teach Lab
        _teachState.value = _teachState.value.copy(
            selectedPaper = topic.paper,
            selectedTopic = topic
        )
        resetTeachLabSession(topic)

        // 2. Sync Catch Lab
        val paperScenarios = CuratedCatchLabScenarios.scenarios.filter { it.paper == topic.paper }
        val scenarioList = if (paperScenarios.isNotEmpty()) paperScenarios else CuratedCatchLabScenarios.scenarios
        val matchIndex = scenarioList.indexOfFirst {
            it.topic.contains(topic.topicName, ignoreCase = true) || topic.topicName.contains(it.topic, ignoreCase = true)
        }
        _catchState.value = _catchState.value.copy(
            selectedPaper = topic.paper,
            scenarios = scenarioList,
            activeScenarioIndex = if (matchIndex >= 0) matchIndex else 0,
            userSpottingInput = "",
            isRevealed = false,
            scoreFeedback = null
        )

        // 3. Sync Ask Lab
        _askState.value = _askState.value.copy(
            selectedPaper = topic.paper,
            selectedTopic = topic
        )
        updateAskLabContent()

        // 4. Sync Spot Difference
        val diffs = repository.getDifferencesForPaper(topic.paper)
        _diffState.value = _diffState.value.copy(
            selectedPaper = topic.paper,
            selectedDifference = diffs.firstOrNull()
        )
    }

    // -------------------------------------------------------------
    // DIAGNOSTIC MCQ TEST MODE
    // -------------------------------------------------------------
    fun selectMcqTestPaper(paper: PaperCode) {
        val curated = com.example.data.curriculum.CuratedMcqBank.getMcqsForPaper(paper)
        _mcqTestState.value = _mcqTestState.value.copy(
            selectedPaper = paper,
            questions = curated,
            userAnswers = emptyMap(),
            isSubmitted = false,
            scoreCorrect = 0,
            totalQuestions = curated.size,
            isGeneratingAi = false,
            errorMessage = null
        )
    }

    fun selectMcqOption(questionId: String, optionIndex: Int) {
        if (_mcqTestState.value.isSubmitted) return
        val current = _mcqTestState.value.userAnswers.toMutableMap()
        current[questionId] = optionIndex
        _mcqTestState.value = _mcqTestState.value.copy(userAnswers = current)
    }

    fun submitMcqTest() {
        val state = _mcqTestState.value
        if (state.isSubmitted || state.questions.isEmpty()) return

        var correctCount = 0
        val questions = state.questions

        viewModelScope.launch {
            questions.forEach { q ->
                val userChoice = state.userAnswers[q.id]
                val isCorrect = userChoice != null && userChoice == q.correctOptionIndex
                if (isCorrect) {
                    correctCount++
                }

                // Update topic progress in Room DB
                repository.recordMcqPractice(q.topicId, 1, if (isCorrect) 1 else 0)

                // Log error if wrong
                if (!isCorrect) {
                    val userMistakeText = if (userChoice != null && userChoice in q.options.indices) {
                        q.options[userChoice]
                    } else {
                        "Unattempted"
                    }
                    val correctRuleText = "${q.options.getOrNull(q.correctOptionIndex) ?: ""} (Ref: ${q.icaiProvisionRef})"

                    repository.logError(
                        paperName = q.paper.shortName,
                        topicId = q.topicId,
                        topicName = q.topicName,
                        priorityTierName = q.priorityTier.label,
                        questionOrConcept = q.questionStem,
                        userMistake = userMistakeText,
                        correctRule = correctRuleText
                    )
                }
            }

            _mcqTestState.value = state.copy(
                isSubmitted = true,
                scoreCorrect = correctCount,
                totalQuestions = questions.size
            )
        }
    }

    fun resetMcqTest() {
        selectMcqTestPaper(_mcqTestState.value.selectedPaper)
    }

    fun generateAiMcqTest() {
        val paper = _mcqTestState.value.selectedPaper
        val topics = repository.getTopicsForPaper(paper)
        val randomTopicName = topics.randomOrNull()?.topicName ?: paper.title

        _mcqTestState.value = _mcqTestState.value.copy(isGeneratingAi = true, errorMessage = null)

        viewModelScope.launch {
            val key = com.example.data.ai.GeminiApiClient.getApiKey(repository.customApiKey)
            if (key.isNotBlank() && !_isOffline.value) {
                val result = com.example.data.ai.GeminiApiClient.generateAiMcqs(paper, randomTopicName, key)
                result.onSuccess { aiMcqs ->
                    val combined = (aiMcqs + com.example.data.curriculum.CuratedMcqBank.getMcqsForPaper(paper)).distinctBy { it.id }
                    _mcqTestState.value = _mcqTestState.value.copy(
                        questions = combined.take(6),
                        userAnswers = emptyMap(),
                        isSubmitted = false,
                        scoreCorrect = 0,
                        totalQuestions = combined.take(6).size,
                        isGeneratingAi = false
                    )
                }.onFailure { err ->
                    _mcqTestState.value = _mcqTestState.value.copy(
                        isGeneratingAi = false,
                        errorMessage = err.message ?: "Could not fetch AI MCQs. Showing Curated Bank."
                    )
                }
            } else {
                val fallback = com.example.data.curriculum.CuratedMcqBank.getMcqsForPaper(paper)
                _mcqTestState.value = _mcqTestState.value.copy(
                    questions = fallback,
                    userAnswers = emptyMap(),
                    isSubmitted = false,
                    isGeneratingAi = false,
                    errorMessage = "Offline Mode: Loaded Curated ICAI 2-Mark MCQs."
                )
            }
        }
    }

    fun resolveErrorLog(id: Long) {
        viewModelScope.launch {
            repository.resolveErrorLog(id)
        }
    }

    fun setMode(mode: LabMode) {
        _currentMode.value = mode
        if (mode == LabMode.VERDICTS) {
            loadVerdictsDashboard()
        }
    }

    fun loadVerdictsDashboard() {
        viewModelScope.launch {
            _verdictsDashboard.value = _verdictsDashboard.value.copy(isLoading = true)
            val list = repository.getVerdictsOnce()
            val avg = if (list.isNotEmpty()) list.map { it.masteryScore }.average() else 0.0
            _verdictsDashboard.value = VerdictsDashboardState(
                verdicts = list,
                averageScore = avg,
                totalSessions = list.size,
                isLoading = false
            )
        }
    }

    fun toggleOfflineMode() {
        val next = !_isOffline.value
        _isOffline.value = next
        repository.isOfflineMode = next
    }

    fun setCustomApiKey(key: String) {
        repository.customApiKey = key
    }

    fun toggleSettingsDialog(show: Boolean) {
        _showSettingsDialog.value = show
    }

    // -------------------------------------------------------------
    // TEACH LAB MODE
    // -------------------------------------------------------------
    fun selectTeachPaper(paper: PaperCode) {
        setActivePaper(paper)
    }

    fun selectTeachTopic(topic: SyllabusTopic) {
        setActiveTopic(topic)
    }

    fun resetTeachLabSession(topic: SyllabusTopic?) {
        val greeting = OfflineLabEngine.generateLabGreeting(topic, _experienceLevel.value)
        _teachState.value = _teachState.value.copy(
            messages = listOf(
                ChatMessage(
                    sender = MessageSender.LAB,
                    text = greeting
                )
            ),
            isAiThinking = false,
            latestVerdict = null,
            isVerdictModalVisible = false,
            turnCount = 0
        )
    }

    fun sendTeachMessage(userText: String) {
        if (userText.isBlank()) return

        val currentMessages = _teachState.value.messages.toMutableList()
        currentMessages.add(ChatMessage(sender = MessageSender.USER, text = userText))
        val currentTopic = _teachState.value.selectedTopic
        val newTurn = _teachState.value.turnCount + 1

        _teachState.value = _teachState.value.copy(
            messages = currentMessages,
            isAiThinking = true,
            turnCount = newTurn
        )

        viewModelScope.launch {
            try {
                // Check if user requested verdict
                val lower = userText.trim().lowercase()
                if (lower.contains("give verdict") || lower == "verdict" || lower.contains("give lab's verdict")) {
                    generateAndShowVerdict()
                    return@launch
                }

                // If Online and has key, call Gemini API
                if (!_isOffline.value && repository.hasValidGeminiKey()) {
                    val history = currentMessages.takeLast(10).map {
                        (if (it.sender == MessageSender.USER) "user" else "model") to it.text
                    }
                    val topicPrompt = "I am teaching you the CA Inter topic: ${currentTopic?.topicName ?: "CA Inter provision"}. Here is my explanation: $userText. Probe me for what I left out like a curious junior article assistant."
                    val result = repository.generateAiResponse(topicPrompt, history)
                    if (result.isSuccess) {
                        val aiReply = result.getOrNull().orEmpty()
                        currentMessages.add(ChatMessage(sender = MessageSender.LAB, text = aiReply))
                        val bounded = if (currentMessages.size > 50) currentMessages.takeLast(50) else currentMessages
                        _teachState.value = _teachState.value.copy(
                            messages = bounded,
                            isAiThinking = false
                        )
                        return@launch
                    }
                }

                // Offline Fallback
                kotlinx.coroutines.delay(400) // responsive typing delay
                val followUp = if (currentTopic != null) {
                    OfflineLabEngine.generateFollowUpDoubt(currentTopic, userText, newTurn, _experienceLevel.value)
                } else {
                    "Got it Boss! But what specific threshold limits or exceptions apply under this provision?"
                }
                currentMessages.add(ChatMessage(sender = MessageSender.LAB, text = followUp))
                val bounded = if (currentMessages.size > 50) currentMessages.takeLast(50) else currentMessages
                _teachState.value = _teachState.value.copy(
                    messages = bounded,
                    isAiThinking = false
                )
            } catch (e: Exception) {
                _teachState.value = _teachState.value.copy(isAiThinking = false)
            }
        }
    }

    fun generateAndShowVerdict() {
        _teachState.value = _teachState.value.copy(isAiThinking = true)

        viewModelScope.launch {
            try {
                val topic = _teachState.value.selectedTopic ?: CaSyllabusData.topics.first()
                val messages = _teachState.value.messages
                val conversationText = messages.joinToString("\n") { "${it.sender.name}: ${it.text}" }

                val verdict = if (!_isOffline.value && repository.hasValidGeminiKey()) {
                    val result = repository.generateAiVerdict(topic, conversationText)
                    if (result.isSuccess) result.getOrThrow() else OfflineLabEngine.generateVerdict(topic, conversationText, _experienceLevel.value)
                } else {
                    OfflineLabEngine.generateVerdict(topic, conversationText, _experienceLevel.value)
                }

                // Save to Room DB
                val savedId = repository.saveVerdict(verdict)
                val finalVerdict = verdict.copy(id = savedId)

                // Add to Chat as special verdict item
                val currentMessages = _teachState.value.messages.toMutableList()
                currentMessages.add(
                    ChatMessage(
                        sender = MessageSender.LAB,
                        text = "🏆 Mastery Verdict: ${finalVerdict.masteryScore}/100. Check detailed analysis card!",
                        isVerdict = true,
                        verdict = finalVerdict
                    )
                )

                _teachState.value = _teachState.value.copy(
                    messages = currentMessages,
                    latestVerdict = finalVerdict,
                    isVerdictModalVisible = true,
                    isAiThinking = false
                )
            } catch (e: Exception) {
                _teachState.value = _teachState.value.copy(isAiThinking = false)
            }
        }
    }

    fun dismissVerdictModal() {
        _teachState.value = _teachState.value.copy(isVerdictModalVisible = false)
    }

    // -------------------------------------------------------------
    // CATCH LAB OUT MODE (Trap Spotting)
    // -------------------------------------------------------------
    fun selectCatchPaper(paper: PaperCode) {
        setActivePaper(paper)
    }

    fun setCatchScenarioIndex(index: Int) {
        val total = _catchState.value.scenarios.size
        if (index in 0 until total) {
            _catchState.value = _catchState.value.copy(
                activeScenarioIndex = index,
                userSpottingInput = "",
                isRevealed = false,
                scoreFeedback = null
            )
        }
    }

    fun nextCatchScenario() {
        val total = _catchState.value.scenarios.size
        if (total == 0) return
        val next = (_catchState.value.activeScenarioIndex + 1) % total
        _catchState.value = _catchState.value.copy(
            activeScenarioIndex = next,
            userSpottingInput = "",
            isRevealed = false,
            scoreFeedback = null
        )
    }

    fun previousCatchScenario() {
        val total = _catchState.value.scenarios.size
        if (total == 0) return
        val prev = if (_catchState.value.activeScenarioIndex - 1 < 0) total - 1 else _catchState.value.activeScenarioIndex - 1
        _catchState.value = _catchState.value.copy(
            activeScenarioIndex = prev,
            userSpottingInput = "",
            isRevealed = false,
            scoreFeedback = null
        )
    }

    fun updateCatchSpottingInput(input: String) {
        _catchState.value = _catchState.value.copy(userSpottingInput = input)
    }

    fun revealCatchAnswer() {
        val currentScenario = _catchState.value.scenarios.getOrNull(_catchState.value.activeScenarioIndex)
        val userNotes = _catchState.value.userSpottingInput.lowercase()

        var spottedCount = 0
        currentScenario?.subtleErrors?.forEach { err ->
            val words = err.wrongStatement.split(" ").filter { it.length > 4 }
            if (words.any { userNotes.contains(it.lowercase()) }) {
                spottedCount++
            }
        }

        val feedback = if (userNotes.isNotBlank()) {
            if (spottedCount >= 2) {
                "🎯 Sharp Eye! You caught $spottedCount out of ${currentScenario?.errorCount ?: 3} subtle traps! Excellent exam awareness."
            } else {
                "🧐 Good try! You spotted $spottedCount trap. Compare your notes with Lab's detailed corrections below."
            }
        } else {
            "🔍 Lab's subtle errors and correct provisions are revealed below!"
        }

        _catchState.value = _catchState.value.copy(
            isRevealed = true,
            scoreFeedback = feedback
        )
    }

    // -------------------------------------------------------------
    // ASK LAB MODE (3-Tier Depth)
    // -------------------------------------------------------------
    fun selectAskPaper(paper: PaperCode) {
        setActivePaper(paper)
    }

    fun selectAskTopic(topic: SyllabusTopic) {
        setActiveTopic(topic)
    }

    fun setAskDepth(depth: ExplanationDepth) {
        _askState.value = _askState.value.copy(selectedDepth = depth)
        updateAskLabContent()
    }

    fun updateAskCustomQuery(query: String) {
        _askState.value = _askState.value.copy(customQuery = query)
    }

    fun togglePracticeQuestionsExpanded() {
        _askState.value = _askState.value.copy(
            isPracticeQuestionsExpanded = !_askState.value.isPracticeQuestionsExpanded
        )
    }

    fun getPracticeQuestionsForTopic(topic: SyllabusTopic?): List<PracticeQuestion> {
        if (topic == null) return emptyList()
        val allForPaper = CuratedQuestionBank.getQuestionsForPaper(topic.paper)
        val matched = allForPaper.filter { q ->
            val chapLower = topic.chapter.lowercase()
            val topicLower = topic.topicName.lowercase()
            q.chapterName.contains(chapLower, ignoreCase = true) ||
            q.questionText.contains(topicLower, ignoreCase = true) ||
            topic.fusedWithTopics.any { fused -> q.chapterName.contains(fused, ignoreCase = true) || q.questionText.contains(fused, ignoreCase = true) }
        }
        return if (matched.isNotEmpty()) matched else allForPaper
    }

    fun searchOrGenerateExplanation() {
        val query = _askState.value.customQuery.trim()
        if (query.isBlank()) {
            updateAskLabContent()
            return
        }

        // Check if query matches a known topic
        val matchedTopic = CaSyllabusData.topics.firstOrNull {
            it.topicName.contains(query, ignoreCase = true) || it.chapter.contains(query, ignoreCase = true)
        }

        if (matchedTopic != null) {
            setActiveTopic(matchedTopic)
            return
        }

        // If online, call Gemini for custom topic depth explanation
        if (!_isOffline.value && repository.hasValidGeminiKey()) {
            _askState.value = _askState.value.copy(isLoading = true)
            viewModelScope.launch {
                val depthPrompt = when (_askState.value.selectedDepth) {
                    ExplanationDepth.DEPTH_1 -> "Explain '$query' at Depth 1 (Simple): Explain like to a first-day commerce student using a relatable Indian business analogy (e.g. Kirana store, Chai stall, Dosa corner)."
                    ExplanationDepth.DEPTH_2 -> "Explain '$query' at Depth 2 (Standard): Structured notes, flowcharts, and formula breakdowns for CA Inter."
                    ExplanationDepth.DEPTH_3 -> "Explain '$query' at Depth 3 (ICAI Exam Level): Exact format rewarded by ICAI examiners (1. Provision of Law/Standard, 2. Facts & Analysis of Case, 3. Conclusion; highlight mandatory ICAI keywords in bold; show structured Working Notes first if practical)."
                }
                val result = repository.generateAiResponse(depthPrompt)
                _askState.value = _askState.value.copy(
                    displayedContent = result.getOrDefault("Could not fetch online explanation. Showing offline guide."),
                    isLoading = false
                )
            }
        } else {
            // Offline fallback message with helpful guidance
            _askState.value = _askState.value.copy(
                displayedContent = "Offline Mode: Showing closest syllabus match for '${_askState.value.selectedTopic?.topicName}' at ${_askState.value.selectedDepth.label}.\n\nSelect a topic from the dropdown or switch to Cloud AI in settings for free-form queries."
            )
        }
    }

    private fun updateAskLabContent() {
        val topic = _askState.value.selectedTopic ?: CaSyllabusData.topics.first()
        val explanation = repository.getExplanation(topic.id)

        val text = if (explanation != null) {
            when (_askState.value.selectedDepth) {
                ExplanationDepth.DEPTH_1 -> explanation.depth1
                ExplanationDepth.DEPTH_2 -> explanation.depth2
                ExplanationDepth.DEPTH_3 -> explanation.depth3
            }
        } else {
            // General structured fallback for topics without dedicated long text
            when (_askState.value.selectedDepth) {
                ExplanationDepth.DEPTH_1 -> "☕ **Commerce Newcomer Analogy for ${topic.topicName}:**\nThink of this provision as an everyday business check. In practice, accountants must track statutory conditions before claiming benefits.\n\nKey Concept: ${topic.keyProvisions.firstOrNull()}"
                ExplanationDepth.DEPTH_2 -> "📊 **Standard Revision Notes:**\n• Topic: ${topic.topicName}\n• Paper: ${topic.paper.title}\n\n**Core Statutory Rules:**\n${topic.keyProvisions.joinToString("\n") { "• $it" }}\n\n**Common Pitfall:** ${topic.commonExaminerPitfalls}"
                ExplanationDepth.DEPTH_3 -> "🏛️ **ICAI Exam Answer Structure:**\n\n**1. Relevant Provision:**\nUnder the provisions of **${topic.topicName}**, compliance with all mandatory statutory conditions is essential.\n\n**2. Key Conditions:**\n${topic.keyProvisions.joinToString("\n") { "- **$it**" }}\n\n**3. Examiner Guidance:**\n*${topic.commonExaminerPitfalls}*"
            }
        }

        _askState.value = _askState.value.copy(displayedContent = text)
    }

    // -------------------------------------------------------------
    // SPOT THE DIFFERENCE MODE
    // -------------------------------------------------------------
    fun selectDiffPaper(paper: PaperCode?) {
        val list = if (paper == null) repository.getAllDifferences() else repository.getDifferencesForPaper(paper)
        _diffState.value = _diffState.value.copy(
            selectedPaper = paper,
            selectedDifference = list.firstOrNull()
        )
    }

    fun selectDifference(diff: ConceptDifference) {
        _diffState.value = _diffState.value.copy(
            selectedDifference = diff,
            revealedColumns = emptySet()
        )
    }

    fun setDiffSearch(query: String) {
        _diffState.value = _diffState.value.copy(searchQuery = query)
    }

    fun toggleSelfQuizMode() {
        _diffState.value = _diffState.value.copy(
            isSelfQuizMode = !_diffState.value.isSelfQuizMode,
            revealedColumns = emptySet()
        )
    }

    fun revealQuizRow(index: Int) {
        val current = _diffState.value.revealedColumns.toMutableSet()
        current.add(index)
        _diffState.value = _diffState.value.copy(revealedColumns = current)
    }

    // Catch Lab AI generation
    fun generateNewAiTrickScenario() {
        val paper = _catchState.value.selectedPaper
        val topics = repository.getTopicsForPaper(paper)
        val targetTopic = topics.randomOrNull()?.topicName ?: paper.title
        _catchState.value = _catchState.value.copy(
            isGeneratingAiScenario = true,
            aiErrorMessage = null
        )

        viewModelScope.launch {
            val result = repository.generateTrickScenario(paper, targetTopic)
            result.onSuccess { newScenario ->
                val updated = listOf(newScenario) + _catchState.value.scenarios
                _catchState.value = _catchState.value.copy(
                    scenarios = updated,
                    activeScenarioIndex = 0,
                    userSpottingInput = "",
                    isRevealed = false,
                    scoreFeedback = null,
                    isGeneratingAiScenario = false
                )
            }.onFailure { err ->
                _catchState.value = _catchState.value.copy(
                    isGeneratingAiScenario = false,
                    aiErrorMessage = err.message ?: "Failed to generate AI scenario. Ensure Cloud AI is enabled."
                )
            }
        }
    }

    fun dismissCatchAiError() {
        _catchState.value = _catchState.value.copy(aiErrorMessage = null)
    }

    // Ask Lab Bookmark Filter
    fun toggleAskBookmarkedOnly() {
        _askState.value = _askState.value.copy(showBookmarkedOnly = !_askState.value.showBookmarkedOnly)
    }

    // Spot Difference Bookmark & AI Generator
    fun toggleDiffBookmarkedOnly() {
        _diffState.value = _diffState.value.copy(showBookmarkedOnly = !_diffState.value.showBookmarkedOnly)
    }

    fun generateAiDifference(query: String) {
        val cleanQuery = query.trim()
        if (cleanQuery.isBlank()) return

        _diffState.value = _diffState.value.copy(
            isGeneratingAiDifference = true,
            aiErrorMessage = null
        )

        viewModelScope.launch {
            val result = repository.generateConceptDifference(cleanQuery, _diffState.value.selectedPaper)
            result.onSuccess { newDiff ->
                _diffState.value = _diffState.value.copy(
                    selectedDifference = newDiff,
                    revealedColumns = emptySet(),
                    isGeneratingAiDifference = false
                )
            }.onFailure { err ->
                _diffState.value = _diffState.value.copy(
                    isGeneratingAiDifference = false,
                    aiErrorMessage = err.message ?: "Could not compare concepts for '$cleanQuery'."
                )
            }
        }
    }

    fun generateAiTopicComparison(topicA: SyllabusTopic, topicB: SyllabusTopic) {
        val paperToUse = topicA.paper
        _diffState.value = _diffState.value.copy(
            selectedPaper = paperToUse,
            isGeneratingAiDifference = true,
            aiErrorMessage = null
        )
        viewModelScope.launch {
            val result = repository.generateConceptDifference("${topicA.topicName} vs ${topicB.topicName}", paperToUse)
            result.onSuccess { newDiff ->
                _diffState.value = _diffState.value.copy(
                    selectedDifference = newDiff,
                    revealedColumns = emptySet(),
                    isGeneratingAiDifference = false
                )
            }.onFailure { err ->
                _diffState.value = _diffState.value.copy(
                    isGeneratingAiDifference = false,
                    aiErrorMessage = err.message ?: "Could not compare '${topicA.topicName}' vs '${topicB.topicName}'."
                )
            }
        }
    }

    fun dismissDiffAiError() {
        _diffState.value = _diffState.value.copy(aiErrorMessage = null)
    }

    // -------------------------------------------------------------
    // V6 DATA SAFETY & BACKUP EXPORT / IMPORT
    // -------------------------------------------------------------
    fun exportProgress(onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val json = repository.exportUserProgressJson()
                onResult(json)
            } catch (e: Exception) {
                onResult("")
            }
        }
    }

    fun importProgress(json: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.importUserProgressJson(json)
            result.onSuccess { count ->
                onResult("Successfully restored $count study records!")
            }.onFailure { err ->
                onResult("Import failed: ${err.message ?: "Invalid JSON backup"}")
            }
        }
    }
}
