package com.example.data.ai

import com.example.data.curriculum.CaSyllabusData
import com.example.data.model.CatchLabScenario
import com.example.data.model.ComparisonRow
import com.example.data.model.ConceptDifference
import com.example.data.model.LabVerdict
import com.example.data.model.PaperCode
import com.example.data.model.SubtleError
import com.example.data.model.SyllabusTopic

object OfflineLabEngine {

    fun generateLabGreeting(topic: SyllabusTopic?): String {
        return if (topic != null) {
            "👋 Hey Boss! I'm your junior article assistant at the firm. I'm assigned to work on **${topic.topicName}** (${topic.paper.title}), but honestly, these ICAI provisions and limits make my head spin! 😅\n\nExplain it to me in your own words—how does this provision work in practice? I'll listen and let you know what doubts I have."
        } else {
            "👋 Hey Boss! Ready for our study session. Pick any Paper and Topic, and teach it to me like I'm your junior article assistant. Remember our motto: *'Knowing isn't understanding—you understand a provision or standard best when you can teach it.'* Whenever you're done, tap 'Give Verdict' for my evaluation!"
        }
    }

    fun generateFollowUpDoubt(
        topic: SyllabusTopic,
        userExplanation: String,
        turnCount: Int
    ): String {
        val lower = userExplanation.lowercase()

        // Find which key provisions the student has not mentioned yet
        val unmentionedProvisions = topic.keyProvisions.filter { prov ->
            val keywords = prov.split(" ", ":", "-", ",", "(", ")")
                .map { it.trim().lowercase() }
                .filter { it.length > 3 && it !in listOf("under", "shall", "where", "which", "their", "these", "other", "after", "before", "every", "first", "total", "cases") }
            keywords.none { lower.contains(it) }
        }

        return when {
            turnCount <= 1 -> {
                if (unmentionedProvisions.isNotEmpty()) {
                    val targetProvision = unmentionedProvisions.first()
                    "Wait Boss! You gave an interesting start on **${topic.topicName}**, but what about: *\"$targetProvision\"*? How does that specific statutory rule or threshold work in practice?"
                } else {
                    "Hold on Boss! You've explained the basics of **${topic.topicName}**, but are there any crucial exceptions, threshold limits, or provisos (\"Provided that...\") that change this treatment?"
                }
            }
            turnCount == 2 -> {
                "Hold on, what about this common exam trap: *\"${topic.commonExaminerPitfalls}\"*? If the ICAI examiner gives a tricky practical scenario testing this exact point, how should we solve or report it?"
            }
            turnCount == 3 -> {
                if (unmentionedProvisions.isNotEmpty()) {
                    val nextTarget = unmentionedProvisions.first()
                    "Wait Boss, I'm still a bit fuzzy on: *\"$nextTarget\"*! What working note, compliance disclosure, or computation step does ICAI expect here?"
                } else {
                    "Understood Boss! But what happens if management or the company fails to follow this requirement? Are there penal consequences, audit report modifications, or tax disallowances?"
                }
            }
            else -> {
                "Understood Boss! I think that completely ties together **${topic.topicName}**. Whenever you're ready, tap **'Give Verdict'** so I can evaluate your explanation against the ICAI standards!"
            }
        }
    }

    fun generateVerdict(
        topic: SyllabusTopic,
        conversationText: String
    ): LabVerdict {
        val lower = conversationText.lowercase()
        val nailed = mutableListOf<String>()
        val slipped = mutableListOf<String>()

        var score = 65

        // Check key provisions
        topic.keyProvisions.forEach { prov ->
            val keywords = prov.split(" ").filter { it.length > 4 }
            val matchedCount = keywords.count { lower.contains(it.lowercase()) }
            if (matchedCount >= 1) {
                nailed.add(prov)
                score += 8
            } else {
                slipped.add("Missed specifying: $prov")
            }
        }

        // Check for provisos / conditions
        if (lower.contains("provided that") || lower.contains("exception") || lower.contains("subject to") || lower.contains("threshold")) {
            nailed.add("Accurately identified statutory provisos & exception conditions")
            score += 5
        } else {
            slipped.add("Did not explicitly cite the statutory 'Provided that...' proviso or exceptions")
        }

        // Clamp score between 55 and 98
        val finalScore = score.coerceIn(55, 96)

        val nextAction = when {
            slipped.isNotEmpty() -> "Revise ${topic.topicName} in ICAI Study Material, especially: ${slipped.first().removePrefix("Missed specifying: ")}."
            else -> "Solve 2 past exam / RTP case studies on ${topic.topicName} to polish answer structure."
        }

        return LabVerdict(
            masteryScore = finalScore,
            paperName = topic.paper.title,
            topicName = topic.topicName,
            whatYouNailed = if (nailed.isNotEmpty()) nailed else listOf("Good general overview of the concept", "Clear practical communication"),
            whatSlipped = if (slipped.isNotEmpty()) slipped else listOf("Minor wording differences from ICAI technical terminology"),
            icaiExaminerTip = topic.commonExaminerPitfalls,
            nextAction = nextAction
        )
    }

    fun generateOfflineTrickScenario(topic: SyllabusTopic): CatchLabScenario {
        val topicName = topic.topicName
        val paper = topic.paper
        val provisions = topic.keyProvisions
        val pitfall = topic.commonExaminerPitfalls

        val subtleErrors = mutableListOf<SubtleError>()

        // Error 1: Common examiner pitfall
        subtleErrors.add(
            SubtleError(
                wrongStatement = "Article treated the statutory threshold as an optional recommendation.",
                errorExplanation = pitfall.ifBlank { "Failure to distinguish mandatory compliance from discretionary exemptions under ICAI standards." },
                correctRuleOrLimit = provisions.firstOrNull() ?: "Strict adherence to ICAI statutory provisions."
            )
        )

        // Error 2: Second key provision misapplication
        val secondProv = provisions.getOrNull(1) ?: provisions.firstOrNull() ?: "Compliance with statutory conditions"
        subtleErrors.add(
            SubtleError(
                wrongStatement = "Article calculated compliance without applying the standard proviso conditions.",
                errorExplanation = "Under ${paper.title}, exceptions and provisos modify the primary rule.",
                correctRuleOrLimit = secondProv
            )
        )

        // Error 3: Presentation or disclosure failure
        val thirdProv = provisions.getOrNull(2) ?: "Mandatory statutory note / disclosure in financial statements"
        subtleErrors.add(
            SubtleError(
                wrongStatement = "Article omitted the mandatory disclosure in the final audit documentation.",
                errorExplanation = "Non-disclosure violates ICAI presentation standards for $topicName.",
                correctRuleOrLimit = thirdProv
            )
        )

        return CatchLabScenario(
            id = "offline_scenario_" + System.currentTimeMillis(),
            paper = paper,
            topic = topicName,
            title = "Case Audit Review: $topicName",
            caseSnippet = "During a review of $topicName (${paper.title}), the junior article made 3 critical oversights: 1. Applied an unverified exemption without checking mandatory preconditions; 2. Misapplied the statutory limit without reviewing amended provisos; and 3. Omitted the required disclosure from the final compliance notes.",
            errorCount = 3,
            subtleErrors = subtleErrors,
            correctIcaiProvision = "Under $topicName, compliance requires fulfilling all preconditions specified in ${provisions.firstOrNull() ?: "the ICAI Study Material"} and avoiding the trap regarding $pitfall."
        )
    }

    fun generateOfflineConceptDifference(query: String, paper: PaperCode?): ConceptDifference {
        val clean = query.trim()
        val parts = if (clean.contains(" vs ", ignoreCase = true)) {
            clean.split(Regex("(?i)\\s+vs\\s+"))
        } else if (clean.contains(" and ", ignoreCase = true)) {
            clean.split(Regex("(?i)\\s+and\\s+"))
        } else {
            listOf(clean, "Standard Alternate")
        }

        val conceptA = parts.getOrNull(0)?.trim()?.takeIf { it.isNotEmpty() } ?: "Concept A"
        val conceptB = parts.getOrNull(1)?.trim()?.takeIf { it.isNotEmpty() } ?: "Concept B"

        val rows = listOf(
            ComparisonRow(
                basisOfDifference = "Governing Scope & Meaning",
                conceptAValue = "$conceptA represents statutory provisions or procedures under CA Inter syllabus.",
                conceptBValue = "$conceptB represents contrasting statutory requirements or treatment."
            ),
            ComparisonRow(
                basisOfDifference = "Applicability & Conditions",
                conceptAValue = "Applies when specific statutory criteria or standards are triggered.",
                conceptBValue = "Applies under distinct conditions or general default provisions."
            ),
            ComparisonRow(
                basisOfDifference = "Statutory Treatment & Computation",
                conceptAValue = "Requires specific computation, recognition, or compliance workflow.",
                conceptBValue = "Requires separate accounting, disclosure, or audit procedure."
            ),
            ComparisonRow(
                basisOfDifference = "ICAI Exam Focus",
                conceptAValue = "Examiners test precise keywords and section/standard numbers for $conceptA.",
                conceptBValue = "Examiners test exceptions, proviso conditions, and disclosures for $conceptB."
            )
        )

        return ConceptDifference(
            id = "offline_diff_" + System.currentTimeMillis(),
            paper = paper ?: PaperCode.PAPER_1,
            title = "$conceptA vs $conceptB",
            conceptA = conceptA,
            conceptB = conceptB,
            comparisonPoints = rows,
            memoryHook = "Anchor $conceptA to specific statutory conditions and contrast with $conceptB defaults."
        )
    }
}
