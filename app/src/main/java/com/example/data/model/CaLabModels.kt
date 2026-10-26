package com.example.data.model

enum class SubjectArchetype {
    STANDARDS_AND_ACCOUNTS,
    STATUTE_AND_CASE_LAW,
    TAX_AMENDMENTS_AND_COMPUTATION,
    FORMULAS_AND_PROFORMA,
    AUDIT_PROCEDURES_AND_CLAUSES,
    STRATEGIC_FRAMEWORKS
}

enum class PaperCode(
    val code: String,
    val title: String,
    val shortName: String,
    val colorHex: Long,
    val archetype: SubjectArchetype = SubjectArchetype.STANDARDS_AND_ACCOUNTS
) {
    PAPER_1("Paper 1", "Advanced Accounting", "Adv. Accounts", 0xFF0284C7, SubjectArchetype.STANDARDS_AND_ACCOUNTS),
    PAPER_2("Paper 2", "Corporate and Other Laws", "Law", 0xFF8B5CF6, SubjectArchetype.STATUTE_AND_CASE_LAW),
    PAPER_3("Paper 3", "Taxation (Direct Tax & GST)", "Taxation", 0xFFD97706, SubjectArchetype.TAX_AMENDMENTS_AND_COMPUTATION),
    PAPER_4("Paper 4", "Cost and Management Accounting", "Costing (CMA)", 0xFF10B981, SubjectArchetype.FORMULAS_AND_PROFORMA),
    PAPER_5("Paper 5", "Auditing and Ethics", "Audit & Ethics", 0xFFEF4444, SubjectArchetype.AUDIT_PROCEDURES_AND_CLAUSES),
    PAPER_6("Paper 6", "Financial & Strategic Management", "FM & SM", 0xFF06B6D4, SubjectArchetype.STRATEGIC_FRAMEWORKS);

    companion object {
        fun fromCode(code: String): PaperCode {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) || it.name.equals(code, ignoreCase = true) }
                ?: PAPER_1
        }
    }
}

enum class ExperienceLevel(val label: String, val defaultDepth: ExplanationDepth) {
    NOVICE("Novice (First Read)", ExplanationDepth.DEPTH_1),
    KNOWLEDGED("Knowledged (Revision)", ExplanationDepth.DEPTH_2),
    EXAM_READY("Exam Ready (Final Polish)", ExplanationDepth.DEPTH_3)
}

enum class CatalogSortOrder(val label: String) {
    OFFICIAL_ICAI("Official ICAI Module & Chapter Order"),
    PRIORITY_TIER("Exam Priority Tier (Tier 1–4)")
}

enum class LabMode(val displayName: String, val badge: String, val iconName: String) {
    TEACH_LAB("Teach Lab", "Feynman Mode", "School"),
    CATCH_LAB_OUT("Catch Lab Out", "Trick Mode 🎭", "BugReport"),
    ASK_LAB("Ask Lab", "3-Tier Depth", "Psychology"),
    SPOT_DIFFERENCE("Spot Difference", "Side-by-Side", "CompareArrows"),
    VERDICTS("Lab's Verdict", "Gap Analysis", "Assessment")
}

enum class ExplanationDepth(val level: Int, val label: String, val subtitle: String) {
    DEPTH_1(1, "Depth 1 (Simple)", "First-day commerce student analogy"),
    DEPTH_2(2, "Depth 2 (Standard)", "Structured notes & flowcharts"),
    DEPTH_3(3, "Depth 3 (ICAI Exam)", "Provision + Facts + Conclusion with bold keywords")
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isVerdict: Boolean = false,
    val verdict: LabVerdict? = null
)

enum class MessageSender {
    USER,
    LAB
}

data class LabVerdict(
    val id: Long = 0,
    val masteryScore: Int,
    val paperName: String,
    val topicName: String,
    val whatYouNailed: List<String>,
    val whatSlipped: List<String>,
    val icaiExaminerTip: String,
    val nextAction: String,
    val createdAt: Long = System.currentTimeMillis()
)

enum class PriorityTier(val label: String, val badgeColorHex: Long, val weightMultiplier: Double) {
    TIER_1("Tier 1 • MUST / Fortress", 0xFFEF4444, 3.0),
    TIER_2("Tier 2 • HIGH Yield", 0xFFF59E0B, 2.2),
    TIER_3("Tier 3 • MODERATE / Steady", 0xFF0EA5E9, 1.5),
    TIER_4("Tier 4 • LOW / Booster", 0xFF64748B, 1.0)
}

data class StandardBlueprint(
    val scope: String,
    val recognitionAndMeasurement: String,
    val disclosure: String
)

data class TeacherSection(
    val id: String,
    val heading: String,
    val bodyMarkdown: String,
    val linkedTrapScenarioId: String? = null
)

data class SyllabusTopic(
    val id: String,
    val paper: PaperCode,
    val chapter: String,
    val topicName: String,
    val moduleName: String = "Module 1",
    val blueprint: StandardBlueprint? = null,
    val priorityTier: PriorityTier = PriorityTier.TIER_2,
    val pastPaperFrequency: String = "6/6 Attempts",
    val averageMarks: Double = 8.0,
    val trendDirection: String = "Consistent",
    val writtenTarget: Int = 6,
    val mcqTarget: Int = 25,
    val oralRecallTarget: Int = 3,
    val oralRhythmNote: String = "Recall key provisions every 3rd day",
    val fusedWithTopics: List<String> = emptyList(),
    val keyProvisions: List<String>,
    val commonExaminerPitfalls: String,
    val icaiChapterNumber: Int = 1,
    val hasRecentAmendment: Boolean = false,
    val amendmentSummary: String = "",
    val teacherSections: List<TeacherSection> = emptyList()
)

data class CatchLabScenario(
    val id: String,
    val paper: PaperCode,
    val topic: String,
    val title: String,
    val caseSnippet: String,
    val errorCount: Int,
    val subtleErrors: List<SubtleError>,
    val correctIcaiProvision: String
)

data class SubtleError(
    val wrongStatement: String,
    val errorExplanation: String,
    val correctRuleOrLimit: String
)

data class ConceptDifference(
    val id: String,
    val paper: PaperCode,
    val title: String,
    val conceptA: String,
    val conceptB: String,
    val comparisonPoints: List<ComparisonRow>,
    val memoryHook: String,
    val priorityTier: PriorityTier = PriorityTier.TIER_1,
    val pastExamFrequency: String = "High Exam Frequency",
    val examinerTrapNote: String = ""
)

data class ComparisonRow(
    val basisOfDifference: String,
    val conceptAValue: String,
    val conceptBValue: String
)

data class StatutoryMatrixRow(
    val sectionOrClause: String,
    val provisionName: String,
    val thresholdOrLimit: String,
    val approvalOrRate: String,
    val penaltyOrException: String
)

data class StatutoryCheatMatrix(
    val id: String,
    val paper: PaperCode,
    val title: String,
    val columnHeaders: List<String>,
    val rows: List<StatutoryMatrixRow>
)

data class DepthExplanation(
    val topicId: String,
    val topicName: String,
    val paper: PaperCode,
    val depth1: String,
    val depth2: String,
    val depth3: String
)

data class McqQuestion(
    val id: String,
    val paper: PaperCode,
    val topicId: String,
    val topicName: String,
    val priorityTier: PriorityTier,
    val caseScenarioText: String? = null,
    val questionStem: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String,
    val icaiProvisionRef: String
)
