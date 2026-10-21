package com.example.data.ai

import com.example.data.curriculum.CaSyllabusData
import com.example.data.model.LabVerdict
import com.example.data.model.PaperCode
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

        // Targeted follow-up doubts based on Paper & Topic
        return when (topic.paper) {
            PaperCode.PAPER_1 -> {
                if (turnCount == 1) {
                    "Wait Boss, what about the financial year-end adjustments? What happens if an event occurs AFTER the balance sheet date but BEFORE the board approves the accounts? Does it change the balance sheet numbers or just go into the board report notes?"
                } else if (!lower.contains("going concern") && !lower.contains("dividend")) {
                    "Hold on! What if the company incurs a massive loss that destroys the going concern assumption? Does that follow the same rule, or is it an exception?"
                } else if (!lower.contains("disclosure") && !lower.contains("proviso")) {
                    "Got it! But what specific disclosure is required in the Notes to Accounts if we classify it as a non-adjusting event? Does ICAI expect the financial quantification?"
                } else {
                    "Understood Boss! I think I've got the core concept. Do you want to test me more or should I prepare my **Lab's Verdict** on your explanation?"
                }
            }
            PaperCode.PAPER_2 -> {
                if (turnCount == 1) {
                    "Hold on Boss! You mentioned the approval, but is it a **Simple Board Resolution**, a **Unanimous Board Resolution**, or a **Special Resolution** passed by shareholders? And does it require an 'AND' or an 'OR' condition between the thresholds?"
                } else if (!lower.contains("penalty") && !lower.contains("punishment") && !lower.contains("fine")) {
                    "Wait, what if the director blatantly takes the loan anyway? What are the penal consequences under the Companies Act for both the company and the defaulting director?"
                } else if (!lower.contains("wholly owned") && !lower.contains("holding")) {
                    "Does this restriction also apply to loans given by a Holding company to its Wholly Owned Subsidiary (WOS), or is there an express statutory exemption?"
                } else {
                    "That clears up the confusion! Whenever you're ready, tap 'Give Verdict' so I can assess how well you covered this provision."
                }
            }
            PaperCode.PAPER_3 -> {
                if (turnCount == 1) {
                    "Wait Boss! What is the exact monetary threshold limit? If the invoice is ₹29,000, do we deduct TDS? And what happens if there are multiple smaller contracts throughout the year exceeding ₹1,00,000?"
                } else if (!lower.contains("pan") && !lower.contains("206aa")) {
                    "What if the payee refuses to give their PAN card? Do we deduct at normal rates or does Section 206AA trigger a 20% higher rate?"
                } else if (topic.id.contains("gst") && !lower.contains("cash")) {
                    "Under GST RCM, can our client pay this tax using their existing Electronic Credit Ledger balance, or does the government strictly mandate payment in CASH?"
                } else {
                    "Crystal clear on the tax provision! Ready for the verdict whenever you say 'Give Verdict'."
                }
            }
            PaperCode.PAPER_4 -> {
                if (turnCount == 1) {
                    "Wait Boss! In this variance calculation, do we multiply the difference by the **Standard Price** or the **Actual Price**? I always get confused between the purchasing department's fault and the factory floor's fault!"
                } else if (!lower.contains("abnormal") && !lower.contains("normal")) {
                    "How do we treat abnormal losses in the cost sheet? Does it get loaded onto the good units, or is it directly transferred to the Costing P&L account?"
                } else {
                    "Understood the costing mechanism! Would you like me to evaluate your explanation with **Lab's Verdict** now?"
                }
            }
            PaperCode.PAPER_5 -> {
                if (turnCount == 1) {
                    "Boss, what if the client's management firmly refuses our request to send external confirmations? Can we just accept an oral explanation or management representation letter?"
                } else if (!lower.contains("alternative") && !lower.contains("tcwg")) {
                    "What specific alternative audit procedures must the audit team perform to gather Sufficient Appropriate Audit Evidence (SAAE)?"
                } else if (!lower.contains("opinion") && !lower.contains("sa 705")) {
                    "If the refusal is completely unjustified and amounts are material and pervasive, which opinion does SA 705 require: Qualified, Adverse, or Disclaimer?"
                } else {
                    "Great explanation of the audit standard! Tap 'Give Verdict' to see your mastery score and gap analysis."
                }
            }
            PaperCode.PAPER_6 -> {
                if (turnCount == 1) {
                    "Wait Boss! When calculating Degree of Financial Leverage (DFL), how do we handle Preference Share Dividends? Do we just subtract it directly from EBIT, or do we have to gross it up for taxes like `Dp / (1 - t)`?"
                } else if (!lower.contains("market value") && !lower.contains("book value")) {
                    "When calculating WACC, which weights are preferred by ICAI: Book Value weights or Market Value weights? And why?"
                } else {
                    "Awesome, that connects the financial theory to exam practice! Let me know when you want 'Lab's Verdict'."
                }
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

        val nextAction = when (topic.paper) {
            PaperCode.PAPER_1 -> "Revise AS 4 Para 8 vs Para 13 disclosure guidelines in Study Material Module 1."
            PaperCode.PAPER_2 -> "Practice drafting board resolution clauses and Sec 186 calculation tables."
            PaperCode.PAPER_3 -> "Review Section 194C / 194J threshold limits and Sec 17(5) blocked credits."
            PaperCode.PAPER_4 -> "Solve 2 standard costing comprehensive variance reconciliation problems."
            PaperCode.PAPER_5 -> "Read SA 505 Para 8 on management refusal and SA 705 audit report modifications."
            PaperCode.PAPER_6 -> "Solve leverage calculation with preference dividends and tax grossing up."
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
}
