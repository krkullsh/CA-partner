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
import com.example.data.local.CaLabDatabase
import com.example.data.model.CatchLabScenario
import com.example.data.model.ChatMessage
import com.example.data.model.ConceptDifference
import com.example.data.model.ExplanationDepth
import com.example.data.model.LabMode
import com.example.data.model.LabVerdict
import com.example.data.model.MessageSender
import com.example.data.model.PaperCode
import com.example.data.model.SyllabusTopic
import com.example.data.repository.StudyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val scoreFeedback: String? = null
)

data class AskLabState(
    val selectedPaper: PaperCode = PaperCode.PAPER_1,
    val selectedTopic: SyllabusTopic? = CaSyllabusData.topics.firstOrNull(),
    val selectedDepth: ExplanationDepth = ExplanationDepth.DEPTH_1,
    val customQuery: String = "",
    val displayedContent: String = "",
    val isLoading: Boolean = false
)

data class SpotDifferenceState(
    val selectedPaper: PaperCode? = null, // null = all papers
    val searchQuery: String = "",
    val selectedDifference: ConceptDifference? = CuratedDifferences.differences.firstOrNull(),
    val isSelfQuizMode: Boolean = false,
    val revealedColumns: Set<Int> = emptySet()
)

data class VerdictsDashboardState(
    val verdicts: List<LabVerdict> = emptyList(),
    val averageScore: Double = 0.0,
    val totalSessions: Int = 0,
    val isLoading: Boolean = false
)

class CaLabViewModel(application: Application) : AndroidViewModel(application) {

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

    private val _askState = MutableStateFlow(AskLabState())
    val askState: StateFlow<AskLabState> = _askState.asStateFlow()

    private val _diffState = MutableStateFlow(SpotDifferenceState())
    val diffState: StateFlow<SpotDifferenceState> = _diffState.asStateFlow()

    // On-demand Verdicts Dashboard state (no continuous background Room polling)
    private val _verdictsDashboard = MutableStateFlow(VerdictsDashboardState())
    val verdictsDashboard: StateFlow<VerdictsDashboardState> = _verdictsDashboard.asStateFlow()

    init {
        // Initialize Teach Lab with welcome greeting
        resetTeachLabSession(_teachState.value.selectedTopic)
        updateAskLabContent()
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
        val topics = repository.getTopicsForPaper(paper)
        val firstTopic = topics.firstOrNull()
        _teachState.value = _teachState.value.copy(
            selectedPaper = paper,
            selectedTopic = firstTopic
        )
        resetTeachLabSession(firstTopic)
    }

    fun selectTeachTopic(topic: SyllabusTopic) {
        _teachState.value = _teachState.value.copy(
            selectedPaper = topic.paper,
            selectedTopic = topic
        )
        resetTeachLabSession(topic)
    }

    fun resetTeachLabSession(topic: SyllabusTopic?) {
        val greeting = OfflineLabEngine.generateLabGreeting(topic)
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
                    OfflineLabEngine.generateFollowUpDoubt(currentTopic, userText, newTurn)
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
                val allUserWords = _teachState.value.messages
                    .filter { it.sender == MessageSender.USER }
                    .joinToString("\n") { it.text }

                val verdict = OfflineLabEngine.generateVerdict(topic, allUserWords)

                // Save to Room DB
                repository.saveVerdict(verdict)
                loadVerdictsDashboard()

                val currentMessages = _teachState.value.messages.toMutableList()
                currentMessages.add(
                    ChatMessage(
                        sender = MessageSender.LAB,
                        text = "📊 **Lab's Verdict has been prepared!** You scored ${verdict.masteryScore}/100 on ${verdict.topicName}. Check the detailed breakdown below.",
                        isVerdict = true,
                        verdict = verdict
                    )
                )

                val bounded = if (currentMessages.size > 50) currentMessages.takeLast(50) else currentMessages
                _teachState.value = _teachState.value.copy(
                    messages = bounded,
                    isAiThinking = false,
                    latestVerdict = verdict,
                    isVerdictModalVisible = true
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
    // CATCH LAB OUT MODE (Trick Mode)
    // -------------------------------------------------------------
    fun selectCatchPaper(paper: PaperCode) {
        val scenarios = repository.getScenariosForPaper(paper)
        _catchState.value = _catchState.value.copy(
            selectedPaper = paper,
            scenarios = if (scenarios.isNotEmpty()) scenarios else repository.getAllScenarios(),
            activeScenarioIndex = 0,
            userSpottingInput = "",
            isRevealed = false,
            scoreFeedback = null
        )
    }

    fun setCatchScenarioIndex(index: Int) {
        _catchState.value = _catchState.value.copy(
            activeScenarioIndex = index,
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
        val topics = repository.getTopicsForPaper(paper)
        _askState.value = _askState.value.copy(
            selectedPaper = paper,
            selectedTopic = topics.firstOrNull()
        )
        updateAskLabContent()
    }

    fun selectAskTopic(topic: SyllabusTopic) {
        _askState.value = _askState.value.copy(
            selectedPaper = topic.paper,
            selectedTopic = topic
        )
        updateAskLabContent()
    }

    fun setAskDepth(depth: ExplanationDepth) {
        _askState.value = _askState.value.copy(selectedDepth = depth)
        updateAskLabContent()
    }

    fun updateAskCustomQuery(query: String) {
        _askState.value = _askState.value.copy(customQuery = query)
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
            _askState.value = _askState.value.copy(
                selectedPaper = matchedTopic.paper,
                selectedTopic = matchedTopic
            )
            updateAskLabContent()
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
}
