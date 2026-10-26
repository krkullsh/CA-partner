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
import com.example.data.curriculum.StrategyBlueprintReader
import com.example.data.model.SubjectArchetype
import com.example.data.model.TeacherSection
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
                val diff = aiResult.getOrThrow()
                saveCustomDifference(diff)
                return aiResult
            }
        }
        // Seamless fallback ensures student gets an instant structured comparison
        val offlineDiff = OfflineLabEngine.generateOfflineConceptDifference(query, paper)
        saveCustomDifference(offlineDiff)
        return Result.success(offlineDiff)
    }

    // Database: Custom Differences Persistence
    suspend fun saveCustomDifference(diff: ConceptDifference) {
        database.studyDao().insertCustomDifference(
            com.example.data.local.CustomDifferenceEntity.fromDomain(diff)
        )
    }

    fun getCustomDifferences(): Flow<List<ConceptDifference>> {
        return database.studyDao().getAllCustomDifferences().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun markTeacherReadStatus(topicId: String, completed: Boolean) {
        val existing = database.studyDao().getProgressForTopic(topicId)
        if (existing == null) {
            database.studyDao().insertOrUpdateProgress(
                TopicProgressEntity(
                    topicId = topicId,
                    teacherReadCompleted = if (completed) 1 else 0,
                    lastRevisedAt = System.currentTimeMillis()
                )
            )
        } else {
            database.studyDao().updateTeacherReadStatus(topicId, if (completed) 1 else 0)
        }
    }

    suspend fun markSwipeDrillStatus(topicId: String, status: Int) {
        val existing = database.studyDao().getProgressForTopic(topicId)
        if (existing == null) {
            database.studyDao().insertOrUpdateProgress(
                TopicProgressEntity(
                    topicId = topicId,
                    swipeDrillStatus = status,
                    lastRevisedAt = System.currentTimeMillis()
                )
            )
        } else {
            database.studyDao().updateSwipeDrillStatus(topicId, status)
        }
    }

    fun getOfflineTeacherArticle(topic: SyllabusTopic, archetype: SubjectArchetype): List<TeacherSection> {
        if (topic.teacherSections.isNotEmpty()) {
            return topic.teacherSections
        }

        val explanation = getExplanation(topic.id)
        val blueprintExcerpt = StrategyBlueprintReader.getBlueprintExcerptForTopic(context, topic)
        val matchingScenario = CuratedCatchLabScenarios.getScenariosForPaper(topic.paper).firstOrNull { sc ->
            sc.topic.contains(topic.topicName, ignoreCase = true) ||
            topic.topicName.contains(sc.topic, ignoreCase = true) ||
            sc.id.contains(topic.id, ignoreCase = true)
        } ?: CuratedCatchLabScenarios.getScenariosForPaper(topic.paper).firstOrNull()

        // Section 1: "1. Core Logic & Everyday Analogy"
        val depth1Text = explanation?.depth1?.trim()
        val section1Body = if (!depth1Text.isNullOrBlank()) {
            depth1Text
        } else {
            """
            ☕ **The Everyday Commerce Analogy:**
            Imagine running a local enterprise or professional partnership in India. When dealing with **${topic.topicName}**, think of this provision as a statutory safeguard ensuring proper financial reporting, capital integrity, and operational accountability.

            💡 **Why This Rule Exists in Practice:**
            ${topic.blueprint?.scope ?: (topic.keyProvisions.firstOrNull() ?: "Compliance with mandatory ICAI statutory and accounting requirements.")}

            Without this guideline, businesses could misstate assets, underreport liabilities, or fail to recognize obligations to stakeholders.
            """.trimIndent()
        }

        // Section 2: "2. Official Blueprint Breakdown"
        val section2Body = when (archetype) {
            SubjectArchetype.STANDARDS_AND_ACCOUNTS -> """
                ### 🏛️ Standards & Company Accounts Blueprint

                **1. Scope & Applicability:**
                ${topic.blueprint?.scope ?: "Governed by ICAI Accounting Standards and Companies Act, 2013."}

                **2. Recognition, Measurement & Statutory Conditions:**
                ${topic.blueprint?.recognitionAndMeasurement ?: topic.keyProvisions.joinToString("\n") { "• $it" }}

                **3. Accounting Treatment, Proforma & Schedule III Disclosures:**
                ${topic.blueprint?.disclosure ?: "Disclose significant accounting policies, notes to accounts, and appropriate classifications."}
                ${if (!blueprintExcerpt.isNullOrBlank()) "\n\n**Blueprint Faculty Guidance:**\n$blueprintExcerpt" else ""}
            """.trimIndent()

            SubjectArchetype.STATUTE_AND_CASE_LAW, SubjectArchetype.TAX_AMENDMENTS_AND_COMPUTATION -> """
                ### ⚖️ Statutory & Regulatory Blueprint

                **1. Bare Statutory Provisions & Core Rules:**
                ${topic.keyProvisions.take(2).joinToString("\n") { "• $it" }.ifBlank { topic.blueprint?.scope ?: "Applicable sections and statutory rules." }}

                **2. Provisos, Sub-Clauses & Exceptions:**
                ${if (topic.keyProvisions.size > 2) topic.keyProvisions.drop(2).joinToString("\n") { "• $it" } else "No blanket exemptions unless explicitly notified by the Central Government or statutory authority."}

                **3. Threshold Limits, Time Frames & Consequences of Non-Compliance:**
                ${topic.blueprint?.recognitionAndMeasurement ?: "Strict adherence to statutory filing dates, approval majorities, and prescribed penalty provisions."}
                ${if (!blueprintExcerpt.isNullOrBlank()) "\n\n**Blueprint Faculty Guidance:**\n$blueprintExcerpt" else ""}
            """.trimIndent()

            SubjectArchetype.FORMULAS_AND_PROFORMA, SubjectArchetype.STRATEGIC_FRAMEWORKS -> """
                ### 📊 Formulas, Proforma & Framework Blueprint

                **1. Core Principles, Key Formulas & Strategic Matrix:**
                ${topic.keyProvisions.take(2).joinToString("\n") { "• $it" }.ifBlank { topic.blueprint?.scope ?: "Core operational formulas and cost behaviors." }}

                **2. Working Table Proforma & Methodological Steps:**
                ${topic.blueprint?.recognitionAndMeasurement ?: (if (topic.keyProvisions.size > 2) topic.keyProvisions.drop(2).joinToString("\n") { "• $it" } else "Standard cost sheet / working proforma format.")}

                **3. Decision Rule, Managerial Implication & Optimization:**
                ${topic.blueprint?.disclosure ?: "Analyze break-even point, sensitivity, margin of safety, and resource optimization."}
                ${if (!blueprintExcerpt.isNullOrBlank()) "\n\n**Blueprint Faculty Guidance:**\n$blueprintExcerpt" else ""}
            """.trimIndent()

            SubjectArchetype.AUDIT_PROCEDURES_AND_CLAUSES -> """
                ### 🔍 Audit Procedures, Standards & Reporting Blueprint

                **1. Standard on Auditing (SA) Scope & Auditor's Objective:**
                ${topic.blueprint?.scope ?: "Engagement execution in accordance with Standards on Auditing (SAs) issued by ICAI."}

                **2. Substantive Procedures, Inquiries & Test of Details:**
                ${topic.blueprint?.recognitionAndMeasurement ?: topic.keyProvisions.joinToString("\n") { "• $it" }}

                **3. Documentation, CARO 2020 Reporting & Modified Opinions:**
                ${topic.blueprint?.disclosure ?: "Gather sufficient appropriate audit evidence (SA 500) and draft CARO clause remarks or audit report modifications (SA 705)."}
                ${if (!blueprintExcerpt.isNullOrBlank()) "\n\n**Blueprint Faculty Guidance:**\n$blueprintExcerpt" else ""}
            """.trimIndent()
        }

        // Section 3: "3. Examiner Traps & Amendments"
        val section3Body = buildString {
            appendLine("⚠️ **Primary Examiner Pitfall & Student Misconceptions:**")
            appendLine(topic.commonExaminerPitfalls)
            appendLine()
            if (topic.hasRecentAmendment) {
                appendLine("📢 **Recent ICAI Study Material Amendment / Finance Act Update:**")
                appendLine(topic.amendmentSummary)
                appendLine()
            }
            appendLine("🔍 **Recurring ICAI Question Patterns:**")
            appendLine("• Exam Frequency: ${topic.pastPaperFrequency} (Average: ${topic.averageMarks.toInt()} Marks)")
            appendLine("• Historical Trend: ${topic.trendDirection}")
            appendLine("• Target Practice Quotas: Written: ${topic.writtenTarget} | MCQs: ${topic.mcqTarget} | Recall: ${topic.oralRecallTarget}")
            if (matchingScenario != null) {
                appendLine()
                appendLine("📌 **Linked Trap Case Scenario:**")
                appendLine("*${matchingScenario.title}*")
                appendLine("Case test snippet: \"${matchingScenario.caseSnippet.take(150)}...\"")
            }
        }.trim()

        // Section 4: "4. Exam Answer Presentation"
        val depth3Text = explanation?.depth3?.trim()
        val section4Body = if (!depth3Text.isNullOrBlank()) {
            depth3Text
        } else {
            """
            🏛️ **ICAI Suggested Answer 3-Step Structure:**

            **Step 1: Statutory Authority & Relevant Provision:**
            Identify the exact standard or section (**${topic.topicName}**). State the statutory criteria with mandatory ICAI keywords.
            *(Reference: ${topic.blueprint?.scope ?: topic.keyProvisions.firstOrNull() ?: "ICAI New Scheme Core Framework"})*

            **Step 2: Working Notes & Fact Analysis:**
            Step-by-step arithmetic computation or legal evaluation:
            ${topic.keyProvisions.joinToString("\n") { "- **$it**" }}

            **Step 3: Conclusion & Examiner Advice:**
            Conclude with a clear, definitive statement.
            Avoid generic answers—examiners look for specific thresholds, precise section numbers, and journal/disclosure formatting.
            *Examiner Warning: ${topic.commonExaminerPitfalls}*
            """.trimIndent()
        }

        return listOf(
            TeacherSection(
                id = "sec_1_logic",
                heading = "1. Core Logic & Everyday Analogy",
                bodyMarkdown = section1Body
            ),
            TeacherSection(
                id = "sec_2_blueprint",
                heading = "2. Official Blueprint Breakdown",
                bodyMarkdown = section2Body
            ),
            TeacherSection(
                id = "sec_3_traps",
                heading = "3. Examiner Traps & Amendments",
                bodyMarkdown = section3Body,
                linkedTrapScenarioId = matchingScenario?.id
            ),
            TeacherSection(
                id = "sec_4_exam_format",
                heading = "4. Exam Answer Presentation",
                bodyMarkdown = section4Body
            )
        )
    }

    fun getHintForTopic(topic: SyllabusTopic): String {
        val firstProv = topic.keyProvisions.firstOrNull()?.trim()
        if (!firstProv.isNullOrBlank()) {
            return "💡 Provision Hint: $firstProv"
        }
        val bp = topic.blueprint
        if (bp != null && bp.recognitionAndMeasurement.isNotBlank()) {
            return "💡 Provision Hint: ${bp.recognitionAndMeasurement.take(120)}..."
        }
        if (topic.commonExaminerPitfalls.isNotBlank()) {
            return "💡 Exam Trap Hint: ${topic.commonExaminerPitfalls.take(120)}..."
        }
        return "💡 Hint: Ensure compliance with statutory criteria for ${topic.topicName}."
    }
}
