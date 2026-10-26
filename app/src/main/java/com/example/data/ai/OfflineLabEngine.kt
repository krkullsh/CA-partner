package com.example.data.ai

import com.example.data.curriculum.CaSyllabusData
import com.example.data.model.CatchLabScenario
import com.example.data.model.ComparisonRow
import com.example.data.model.ConceptDifference
import com.example.data.model.ExperienceLevel
import com.example.data.model.LabVerdict
import com.example.data.model.PaperCode
import com.example.data.model.SubtleError
import com.example.data.model.SyllabusTopic

object OfflineLabEngine {

    fun generateLabGreeting(
        topic: SyllabusTopic?,
        experienceLevel: ExperienceLevel = ExperienceLevel.KNOWLEDGED
    ): String {
        return if (topic != null) {
            when (experienceLevel) {
                ExperienceLevel.NOVICE -> {
                    "👋 Welcome! I'm your supportive Senior at the firm. We're going to master **${topic.topicName}** (${topic.paper.title}) together! 🌟\n\nDon't worry about memorizing every section number right away—explain the concept to me in simple terms. I'll listen, share gentle hints, and help you build strong fundamentals. Whenever you're done, tap 'Give Verdict'!"
                }
                ExperienceLevel.KNOWLEDGED -> {
                    "👋 Hey Boss! I'm your junior article assistant at the firm. I'm assigned to work on **${topic.topicName}** (${topic.paper.title}), but honestly, these ICAI provisions and limits make my head spin! 😅\n\nExplain it to me in your own words—how does this provision work in practice? I'll listen and let you know what doubts I have. Whenever you're done, tap 'Give Verdict'!"
                }
                ExperienceLevel.EXAM_READY -> {
                    "📋 Welcome, Candidate. I am your strict ICAI Examiner conducting a technical viva on **${topic.topicName}** (${topic.paper.title}).\n\nState your complete statutory exposition. You are required to cite exact Section/AS paragraphs, monetary and numerical thresholds, statutory provisos, and reporting consequences under Schedule III/CARO. Begin your answer."
                }
            }
        } else {
            "👋 Welcome to Teach Lab! Pick any Paper and Topic, and explain it to me in your own words. Tap 'Give Verdict' whenever you're ready!"
        }
    }

    fun generateFollowUpDoubt(
        topic: SyllabusTopic,
        userExplanation: String,
        turnCount: Int,
        experienceLevel: ExperienceLevel = ExperienceLevel.KNOWLEDGED
    ): String {
        val lower = userExplanation.lowercase()

        // Find which key provisions the student has not mentioned yet
        val unmentionedProvisions = topic.keyProvisions.filter { prov ->
            val keywords = prov.split(" ", ":", "-", ",", "(", ")")
                .map { it.trim().lowercase() }
                .filter { it.length > 3 && it !in listOf("under", "shall", "where", "which", "their", "these", "other", "after", "before", "every", "first", "total", "cases") }
            keywords.none { lower.contains(it) }
        }

        return when (experienceLevel) {
            ExperienceLevel.NOVICE -> {
                // Supportive Senior with Gentle Hints
                when {
                    turnCount <= 1 -> {
                        if (unmentionedProvisions.isNotEmpty()) {
                            val targetProvision = unmentionedProvisions.first()
                            "Great start! 🌟 You've laid out the basics of **${topic.topicName}**. But let me give you a gentle hint: think about how *\"$targetProvision\"* applies. (💡 Senior Hint: Notice if there's a specific threshold, percentage, or condition here!) How would you explain that part in simple terms?"
                        } else {
                            "Superb effort! 👏 You've explained the core idea of **${topic.topicName}** nicely. (💡 Senior Hint: Are there any special exceptions or conditions where this general rule does not apply?)"
                        }
                    }
                    turnCount == 2 -> {
                        "You're making great progress! 💡 Here's a tip on an exam trap: *\"${topic.commonExaminerPitfalls}\"*. (Hint: Don't worry about memorizing every word—just tell me how you'd avoid this mistake in practice!)"
                    }
                    turnCount == 3 -> {
                        if (unmentionedProvisions.isNotEmpty()) {
                            val nextTarget = unmentionedProvisions.first()
                            "Almost there! 🎯 Keep this gentle hint in mind: *\"$nextTarget\"*. (💡 Senior Hint: Think about what working note or disclosure is needed). How would you summarize it?"
                        } else {
                            "You've grasped the main concepts nicely! 👍 (💡 Senior Hint: Whenever you feel confident, tap **'Give Verdict'** to see your score with hints applied!)"
                        }
                    }
                    else -> {
                        "Well done! You've walked through the key aspects of **${topic.topicName}**. Tap **'Give Verdict'** to see your score and strengths!"
                    }
                }
            }

            ExperienceLevel.KNOWLEDGED -> {
                // Probing Article Assistant (Provisos & Thresholds)
                when {
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

            ExperienceLevel.EXAM_READY -> {
                // Strict ICAI Examiner (Exact Section/AS & Keywords Required)
                when {
                    turnCount <= 1 -> {
                        if (unmentionedProvisions.isNotEmpty()) {
                            val targetProvision = unmentionedProvisions.first()
                            "⚠️ Candidate: Your initial submission on **${topic.topicName}** lacks statutory precision. Specifically address: *\"$targetProvision\"*. Cite the exact Section/AS paragraph, monetary limit, or statutory formula required by ICAI."
                        } else {
                            "⚠️ Candidate: You have provided general observations on **${topic.topicName}**, but ICAI evaluators demand statutory rigor. Specify the exact numerical thresholds, statutory provisos (\"Provided that...\"), and prescribed timeframes."
                        }
                    }
                    turnCount == 2 -> {
                        "🚨 Examiner Challenge: A company scenario triggers this exact trap: *\"${topic.commonExaminerPitfalls}\"*. What is the strict statutory consequence, journal entry adjustment, or qualification required in the audit report?"
                    }
                    turnCount == 3 -> {
                        if (unmentionedProvisions.isNotEmpty()) {
                            val nextTarget = unmentionedProvisions.first()
                            "⚠️ Missing Technical Coverage: You have omitted *\"$nextTarget\"*. State the mandatory disclosure format under Schedule III / AS notes to accounts without generalizations."
                        } else {
                            "⚖️ Final Examination Cross-Check: What are the penal provisions for non-compliance under the Act/Standard, and how must this be reported under CARO / Audit Standards?"
                        }
                    }
                    else -> {
                        "Review concluded. Tap **'Give Verdict'** for strict ICAI marking and point-by-point deduction analysis."
                    }
                }
            }
        }
    }

    fun evaluateExplanation(
        topic: SyllabusTopic,
        conversationText: String,
        experienceLevel: ExperienceLevel = ExperienceLevel.KNOWLEDGED
    ): LabVerdict = generateVerdict(topic, conversationText, experienceLevel)

    fun generateVerdict(
        topic: SyllabusTopic,
        conversationText: String,
        experienceLevel: ExperienceLevel = ExperienceLevel.KNOWLEDGED
    ): LabVerdict {
        val lower = conversationText.lowercase()
        val nailed = mutableListOf<String>()
        val slipped = mutableListOf<String>()

        // 1. Set baseline score based on ExperienceLevel
        var score = when (experienceLevel) {
            ExperienceLevel.NOVICE -> 75 // +10 bonus baseline forgiving rubric
            ExperienceLevel.KNOWLEDGED -> 65 // Standard baseline
            ExperienceLevel.EXAM_READY -> 55 // Strict examiner starting rubric
        }

        // 2. Evaluate Key Provisions
        topic.keyProvisions.forEach { prov ->
            val keywords = prov.split(" ", ":", "-", ",", "(", ")")
                .map { it.trim().lowercase() }
                .filter { it.length > 3 && it !in listOf("under", "shall", "where", "which", "their", "these", "other", "after", "before", "every", "first", "total") }

            val matchThreshold = when (experienceLevel) {
                ExperienceLevel.NOVICE -> 1
                ExperienceLevel.KNOWLEDGED -> 1
                ExperienceLevel.EXAM_READY -> 2.coerceAtMost(keywords.size)
            }

            val matchedCount = keywords.count { lower.contains(it) }
            if (matchedCount >= matchThreshold) {
                nailed.add(prov)
                score += if (experienceLevel == ExperienceLevel.NOVICE) 10 else 8
            } else {
                when (experienceLevel) {
                    ExperienceLevel.NOVICE -> {
                        slipped.add("Concept to brush up: $prov")
                    }
                    ExperienceLevel.KNOWLEDGED -> {
                        slipped.add("Missed specifying: $prov")
                    }
                    ExperienceLevel.EXAM_READY -> {
                        slipped.add("Omitted statutory provision: $prov")
                        score -= 5
                    }
                }
            }

            // 3. In EXAM_READY mode: Specifically check exact numerical limits / percentages / ratios
            if (experienceLevel == ExperienceLevel.EXAM_READY) {
                val numberTokens = Regex("""\b(\d+[%a-zA-Z\:\/]*|\d+)\b""").findAll(prov)
                    .map { it.value.trim().lowercase() }
                    .filter { it.length >= 2 && (it.any { c -> c.isDigit() } || it.contains("%") || it.contains(":")) }
                    .toList()

                if (numberTokens.isNotEmpty()) {
                    val missingNumbers = numberTokens.filter { !lower.contains(it) }
                    if (missingNumbers.isNotEmpty()) {
                        slipped.add("Missing Statutory Limit: Omitted exact threshold/ratio (${missingNumbers.joinToString(", ")}) in '$prov'")
                        score -= 4
                    }
                }
            }
        }

        // 4. In EXAM_READY mode: Flag missed examiner pitfalls
        if (experienceLevel == ExperienceLevel.EXAM_READY && topic.commonExaminerPitfalls.isNotBlank()) {
            val pitfallWords = topic.commonExaminerPitfalls.split(" ", ":", "-", ",", ".")
                .map { it.trim().lowercase() }
                .filter { it.length > 4 && it !in listOf("their", "these", "other", "after", "before", "common", "examiner", "pitfall", "often", "students") }

            val pitfallMatched = pitfallWords.any { lower.contains(it) }
            if (!pitfallMatched) {
                slipped.add("Critical Examiner Trap Missed: Failed to address pitfall — ${topic.commonExaminerPitfalls}")
                score -= 8
            } else {
                nailed.add("Successfully navigated examiner trap: ${topic.commonExaminerPitfalls.take(60)}...")
                score += 5
            }
        } else if (topic.commonExaminerPitfalls.isNotBlank()) {
            val pitfallWords = topic.commonExaminerPitfalls.split(" ", ":", "-", ",", ".")
                .map { it.trim().lowercase() }
                .filter { it.length > 4 }
            if (pitfallWords.any { lower.contains(it) }) {
                nailed.add("Identified common exam trap: ${topic.commonExaminerPitfalls.take(50)}...")
                score += 4
            }
        }

        // 5. Check for provisos / conditions / exceptions
        val hasProvisos = lower.contains("provided that") || lower.contains("exception") ||
            lower.contains("subject to") || lower.contains("threshold") || lower.contains("proviso")

        if (hasProvisos) {
            nailed.add("Accurately identified statutory provisos & exception conditions")
            score += 5
        } else {
            when (experienceLevel) {
                ExperienceLevel.NOVICE -> {
                    slipped.add("Opportunity to learn: Explore any statutory exceptions and provisos")
                }
                ExperienceLevel.KNOWLEDGED -> {
                    slipped.add("Did not explicitly cite the statutory 'Provided that...' proviso or exceptions")
                }
                ExperienceLevel.EXAM_READY -> {
                    slipped.add("Strict Examiner Deduction: Omitted statutory 'Provided that...' proviso or exceptions")
                    score -= 5
                }
            }
        }

        // Encouragement boost for NOVICE mode
        if (experienceLevel == ExperienceLevel.NOVICE) {
            score = (score + 10).coerceAtMost(100)
        }

        // 6. Clamp score based on ExperienceLevel
        val finalScore = when (experienceLevel) {
            ExperienceLevel.NOVICE -> score.coerceIn(65, 100)
            ExperienceLevel.KNOWLEDGED -> score.coerceIn(55, 96)
            ExperienceLevel.EXAM_READY -> score.coerceIn(40, 94)
        }

        val nextAction = when (experienceLevel) {
            ExperienceLevel.NOVICE -> {
                if (slipped.isNotEmpty()) {
                    "Great foundational start! Review ${topic.topicName} in ICAI Study Material, focusing on: ${slipped.first().removePrefix("Concept to brush up: ")}."
                } else {
                    "Well done! You've built solid conceptual clarity on ${topic.topicName}. Try testing yourself in 'Knowledged' mode next!"
                }
            }
            ExperienceLevel.KNOWLEDGED -> {
                if (slipped.isNotEmpty()) {
                    "Revise ${topic.topicName} in ICAI Study Material, especially: ${slipped.first().removePrefix("Missed specifying: ")}."
                } else {
                    "Solve 2 past exam / RTP case studies on ${topic.topicName} to polish answer structure."
                }
            }
            ExperienceLevel.EXAM_READY -> {
                if (slipped.isNotEmpty()) {
                    "Strict ICAI Exam Drill: Practice a written 10-mark answer under timed exam conditions (18 minutes) fixing: ${slipped.first()}."
                } else {
                    "Exam Ready! Proceed to complete a full 100-mark mock paper or drill the next Tier 1 topic."
                }
            }
        }

        return LabVerdict(
            masteryScore = finalScore,
            paperName = topic.paper.title,
            topicName = topic.topicName,
            whatYouNailed = if (nailed.isNotEmpty()) nailed else listOf("Good general overview of the concept", "Clear practical communication"),
            whatSlipped = if (slipped.isNotEmpty()) slipped else listOf("Minor technical terminology refinements required"),
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

        subtleErrors.add(
            SubtleError(
                wrongStatement = "Article treated the statutory threshold as an optional recommendation.",
                errorExplanation = pitfall.ifBlank { "Failure to distinguish mandatory compliance from discretionary exemptions under ICAI standards." },
                correctRuleOrLimit = provisions.firstOrNull() ?: "Strict adherence to ICAI statutory provisions."
            )
        )

        val secondProv = provisions.getOrNull(1) ?: provisions.firstOrNull() ?: "Compliance with statutory conditions"
        subtleErrors.add(
            SubtleError(
                wrongStatement = "Article calculated compliance without applying the standard proviso conditions.",
                errorExplanation = "Under ${paper.title}, exceptions and provisos modify the primary rule.",
                correctRuleOrLimit = secondProv
            )
        )

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
