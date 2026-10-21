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
}
