package com.example.data.repository

import android.content.Context
import com.example.data.ai.GeminiApiClient
import com.example.data.ai.OfflineLabEngine
import com.example.data.curriculum.CaSyllabusData
import com.example.data.curriculum.CuratedCatchLabScenarios
import com.example.data.curriculum.CuratedDifferences
import com.example.data.curriculum.CuratedExplanations
import com.example.data.local.BookmarkEntity
import com.example.data.local.CaLabDatabase
import com.example.data.local.TopicProgressEntity
import com.example.data.local.VerdictEntity
import com.example.data.model.CatchLabScenario
import com.example.data.model.ConceptDifference
import com.example.data.model.DepthExplanation
import com.example.data.model.LabVerdict
import com.example.data.model.PaperCode
import com.example.data.model.SyllabusTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudyRepository(
    private val database: CaLabDatabase,
    private val context: Context
) {
    private val prefs = context.getSharedPreferences("ca_lab_prefs", Context.MODE_PRIVATE)

    // Online / Offline settings
    var isOfflineMode: Boolean
        get() = prefs.getBoolean("pref_offline_mode", false)
        set(value) {
            prefs.edit().putBoolean("pref_offline_mode", value).apply()
        }

    var customApiKey: String
        get() = prefs.getString("pref_custom_api_key", "") ?: ""
        set(value) {
            prefs.edit().putString("pref_custom_api_key", value).apply()
        }

    fun hasValidGeminiKey(): Boolean {
        return GeminiApiClient.getApiKey(customApiKey).isNotBlank()
    }

    // Syllabus
    fun getTopicsForPaper(paper: PaperCode): List<SyllabusTopic> =
        CaSyllabusData.getTopicsForPaper(paper)

    fun getAllTopics(): List<SyllabusTopic> = CaSyllabusData.topics

    fun getTopicById(id: String): SyllabusTopic? =
        CaSyllabusData.topics.firstOrNull { it.id == id }

    // Catch Lab Out
    fun getScenariosForPaper(paper: PaperCode): List<CatchLabScenario> =
        CuratedCatchLabScenarios.getScenariosForPaper(paper)

    fun getAllScenarios(): List<CatchLabScenario> =
        CuratedCatchLabScenarios.scenarios

    // Differences
    fun getDifferencesForPaper(paper: PaperCode): List<ConceptDifference> =
        CuratedDifferences.getDifferencesForPaper(paper)

    fun getAllDifferences(): List<ConceptDifference> =
        CuratedDifferences.differences

    // Explanations
    fun getExplanation(topicId: String): DepthExplanation? =
        CuratedExplanations.getExplanation(topicId)

    // Database: Verdicts
    suspend fun saveVerdict(verdict: LabVerdict): Long {
        return database.studyDao().insertVerdict(VerdictEntity.fromDomain(verdict))
    }

    suspend fun getVerdictsOnce(): List<LabVerdict> {
        return database.studyDao().getVerdictsList().map { it.toDomain() }
    }

    suspend fun getAverageMasteryScoreOnce(): Double {
        return database.studyDao().getAverageScoreValue() ?: 0.0
    }

    suspend fun getTotalSessionsCountOnce(): Int {
        return database.studyDao().getTotalSessionsCountValue()
    }

    fun getVerdicts(): Flow<List<LabVerdict>> {
        return database.studyDao().getAllVerdicts().map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getAverageMasteryScore(): Flow<Double> {
        return database.studyDao().getAverageScore().map { it ?: 0.0 }
    }

    fun getTotalSessionsCount(): Flow<Int> {
        return database.studyDao().getTotalSessionsCount()
    }

    // Database: Bookmarks
    suspend fun toggleBookmark(id: String, type: String, title: String, paperName: String) {
        if (database.studyDao().isBookmarked(id)) {
            database.studyDao().removeBookmark(id)
        } else {
            database.studyDao().insertBookmark(BookmarkEntity(id, type, title, paperName))
        }
    }

    suspend fun isBookmarked(id: String): Boolean = database.studyDao().isBookmarked(id)

    fun getBookmarks(): Flow<List<BookmarkEntity>> = database.studyDao().getAllBookmarks()

    // Database: Topic Progress
    fun getAllTopicProgress(): Flow<List<TopicProgressEntity>> = database.studyDao().getAllProgress()

    suspend fun getTopicProgress(topicId: String): TopicProgressEntity? =
        database.studyDao().getProgressForTopic(topicId)

    suspend fun recordWrittenPractice(topicId: String) {
        val existing = database.studyDao().getProgressForTopic(topicId)
        if (existing == null) {
            database.studyDao().insertOrUpdateProgress(
                TopicProgressEntity(topicId = topicId, writtenSolved = 1, lastRevisedAt = System.currentTimeMillis())
            )
        } else {
            database.studyDao().incrementWrittenCount(topicId)
        }
    }

    suspend fun recordOralRecall(topicId: String) {
        val existing = database.studyDao().getProgressForTopic(topicId)
        if (existing == null) {
            database.studyDao().insertOrUpdateProgress(
                TopicProgressEntity(topicId = topicId, oralRecallsCompleted = 1, lastRevisedAt = System.currentTimeMillis())
            )
        } else {
            database.studyDao().incrementOralRecallCount(topicId)
        }
    }

    suspend fun recordMcqPractice(topicId: String, attempted: Int, correct: Int) {
        val existing = database.studyDao().getProgressForTopic(topicId)
        if (existing == null) {
            database.studyDao().insertOrUpdateProgress(
                TopicProgressEntity(
                    topicId = topicId,
                    mcqAttempted = attempted,
                    mcqCorrect = correct,
                    lastRevisedAt = System.currentTimeMillis()
                )
            )
        } else {
            database.studyDao().recordMcqResult(topicId, attempted, correct)
        }
    }

    // Database: Error Log
    suspend fun logError(
        paperName: String,
        topicId: String,
        topicName: String,
        priorityTierName: String,
        questionOrConcept: String,
        userMistake: String,
        correctRule: String
    ): Long {
        return database.studyDao().insertErrorLog(
            com.example.data.local.ErrorLogEntity(
                paperName = paperName,
                topicId = topicId,
                topicName = topicName,
                priorityTierName = priorityTierName,
                questionOrConcept = questionOrConcept,
                userMistake = userMistake,
                correctRule = correctRule
            )
        )
    }

    fun getErrorLogs(): Flow<List<com.example.data.local.ErrorLogEntity>> = database.studyDao().getAllErrorLogs()

    suspend fun resolveErrorLog(id: Long) {
        database.studyDao().deleteErrorLog(id)
    }

    // Strategy Blueprint Markdown Loader
    fun getStrategyBlueprintMarkdown(paper: PaperCode): String {
        return com.example.data.curriculum.StrategyBlueprintReader.getBlueprintMarkdown(context, paper)
    }

    // AI Generation (Online with offline fallback)
    suspend fun generateAiResponse(
        prompt: String,
        history: List<Pair<String, String>> = emptyList()
    ): Result<String> {
        if (isOfflineMode) {
            return Result.failure(IllegalStateException("Offline mode active"))
        }

        val key = GeminiApiClient.getApiKey(customApiKey)
        if (key.isBlank()) {
            return Result.failure(IllegalStateException("No Gemini API key available"))
        }

        return GeminiApiClient.generateContent(prompt, key, history)
    }

    suspend fun generateAiVerdict(
        topic: SyllabusTopic,
        conversationText: String
    ): Result<LabVerdict> {
        if (isOfflineMode) {
            return Result.failure(IllegalStateException("Offline mode active"))
        }

        val key = GeminiApiClient.getApiKey(customApiKey)
        if (key.isBlank()) {
            return Result.failure(IllegalStateException("No Gemini API key available"))
        }

        return GeminiApiClient.generateJsonVerdict(topic, conversationText, key)
    }

    suspend fun generateTrickScenario(paper: PaperCode, topicName: String): Result<CatchLabScenario> {
        val topic = CaSyllabusData.topics.firstOrNull { it.topicName.equals(topicName, ignoreCase = true) }
            ?: CaSyllabusData.getTopicsForPaper(paper).firstOrNull()
            ?: CaSyllabusData.topics.first()

        if (!isOfflineMode && hasValidGeminiKey()) {
            val key = GeminiApiClient.getApiKey(customApiKey)
            val aiResult = GeminiApiClient.generateAiTrickScenario(paper, topicName, key)
            if (aiResult.isSuccess) {
                return aiResult
            }
        }
        // Seamless fallback ensures student is never blocked by network timeouts
        return Result.success(OfflineLabEngine.generateOfflineTrickScenario(topic))
    }

    suspend fun generateConceptDifference(query: String, paper: PaperCode?): Result<ConceptDifference> {
        if (!isOfflineMode && hasValidGeminiKey()) {
            val key = GeminiApiClient.getApiKey(customApiKey)
            val aiResult = GeminiApiClient.generateAiConceptDifference(query, paper, key)
            if (aiResult.isSuccess) {
                return aiResult
            }
        }
        // Seamless fallback ensures student gets an instant structured comparison
        return Result.success(OfflineLabEngine.generateOfflineConceptDifference(query, paper))
    }
}
