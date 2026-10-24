package com.example.data.curriculum

import android.content.Context
import com.example.data.model.PaperCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap

object StrategyBlueprintReader {

    private val blueprintCache = ConcurrentHashMap<PaperCode, String>()
    private val fullExcelMatrixCache = ConcurrentHashMap<PaperCode, com.example.data.model.StatutoryCheatMatrix>()

    val paperToBlueprintFile = mapOf(
        PaperCode.PAPER_1 to "CA_Inter_AdvAcc_Strategy_Blueprint.md",
        PaperCode.PAPER_2 to "CA_Inter_Corporate_Other_Laws_Strategy.md",
        PaperCode.PAPER_3 to "CA_Inter_Taxation_Complete_Strategy.md",
        PaperCode.PAPER_4 to "CA_Inter_CMA_Topic_Wise_Strategy.md",
        PaperCode.PAPER_5 to "CA_Inter_Audit_Ethics_Strategy.md",
        PaperCode.PAPER_6 to "CA_Inter_FM_SM_Master_Strategy_Blueprint.md"
    )

    fun getFullExcelMatrixForPaper(context: Context, paper: PaperCode): com.example.data.model.StatutoryCheatMatrix {
        fullExcelMatrixCache[paper]?.let { return it }

        val baseMatrix = CuratedDifferences.getMatrixForPaper(paper)
        val curatedRows = baseMatrix?.rows ?: emptyList()

        // 1. Sourced from CaSyllabusData topics for this paper
        val syllabusTopics = CaSyllabusData.getTopicsForPaper(paper)
        val syllabusRows = syllabusTopics.map { topic ->
            val tierLabel = topic.priorityTier.label.split("•").firstOrNull()?.trim() ?: topic.priorityTier.name
            val moduleClean = topic.moduleName.replace("Module ", "Mod ")
            val keyRule = if (topic.keyProvisions.isNotEmpty()) {
                topic.keyProvisions.take(2).joinToString("; ")
            } else {
                topic.blueprint?.scope?.take(70) ?: "Core ICAI Standard Requirements"
            }
            com.example.data.model.StatutoryMatrixRow(
                sectionOrClause = "$tierLabel • $moduleClean",
                provisionName = "${topic.chapter}: ${topic.topicName}",
                thresholdOrLimit = "Exam Freq: ${topic.pastPaperFrequency} (Avg: ${topic.averageMarks.toInt()}M)",
                approvalOrRate = keyRule,
                penaltyOrException = "Quotas — W:${topic.writtenTarget} M:${topic.mcqTarget} O:${topic.oralRecallTarget} | Pitfall: ${topic.commonExaminerPitfalls.take(70)}"
            )
        }

        // 2. Parse Strategy Blueprint Markdown for this paper
        val markdownText = getBlueprintMarkdown(context, paper)
        val blueprintRows = parseBlueprintMarkdownToMatrixRows(markdownText, paper)

        // 3. Combine: Curated + Blueprint Tables + Syllabus Quotas
        val combined = ArrayList<com.example.data.model.StatutoryMatrixRow>()
        combined.addAll(curatedRows)
        combined.addAll(blueprintRows)
        combined.addAll(syllabusRows)

        val distinctRows = combined.distinctBy {
            "${it.sectionOrClause.trim().lowercase()}_${it.provisionName.trim().lowercase()}"
        }

        val columnHeaders = listOf(
            "Standard / Section / Tier",
            "Chapter / Provision",
            "Exam Weight / Limit",
            "Core Formula / Approval / Key Rule",
            "Mastery Quotas / Pitfalls / Traps"
        )

        val fullMatrix = com.example.data.model.StatutoryCheatMatrix(
            id = "master_excel_${paper.name.lowercase()}",
            paper = paper,
            title = "${paper.title} — Master Excel Matrix & Quotas (${distinctRows.size} Entries)",
            columnHeaders = columnHeaders,
            rows = distinctRows
        )

        fullExcelMatrixCache[paper] = fullMatrix
        return fullMatrix
    }

    private fun parseBlueprintMarkdownToMatrixRows(
        markdownText: String,
        paper: PaperCode
    ): List<com.example.data.model.StatutoryMatrixRow> {
        val rows = mutableListOf<com.example.data.model.StatutoryMatrixRow>()
        val lines = markdownText.lines()

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isBlank()) continue

            // Skip footers / headers
            val lower = trimmed.lowercase()
            if (lower.contains("catestseries") || lower.contains("page ") || lower.contains("©") || lower.contains("india's best")) {
                continue
            }

            // A) Markdown Table rows with '|'
            if (trimmed.contains("|")) {
                // Skip separator rows like |---|---|
                val withoutSpaces = trimmed.replace(" ", "")
                if (withoutSpaces.all { it in "|-:" }) continue

                val cells = trimmed.split("|")
                    .map { it.replace("**", "").replace("*", "").replace("`", "").trim() }
                    .filter { it.isNotBlank() }

                if (cells.size >= 2) {
                    val col0 = cells[0]
                    val col1 = cells[1]

                    // Skip header row indicator
                    val col0Lower = col0.lowercase()
                    if (col0Lower == "standard" || col0Lower == "section" || col0Lower == "chapter" ||
                        col0Lower == "as standard" || col0Lower == "clause" || col0Lower == "section / rule" ||
                        col0Lower == "costing topic" || col0Lower == "caro clause" || col0Lower == "domain / topic"
                    ) {
                        continue
                    }

                    val col2 = cells.getOrElse(2) { "As per Blueprint" }.ifBlank { "As per Blueprint" }
                    val col3 = cells.getOrElse(3) { "Core ICAI Standard Rule" }.ifBlank { "Core ICAI Standard Rule" }
                    val col4 = cells.getOrElse(4) { "High-Yield ICAI Focus" }.ifBlank { "High-Yield ICAI Focus" }

                    rows.add(
                        com.example.data.model.StatutoryMatrixRow(
                            sectionOrClause = col0,
                            provisionName = col1,
                            thresholdOrLimit = col2,
                            approvalOrRate = col3,
                            penaltyOrException = col4
                        )
                    )
                    continue
                }
            }

            // B) High-yield bullet points: e.g. • **Schedule III Financial Statements:** 14M Compulsory Q2 in 6/6 attempts...
            val bulletMatch = Regex("""^[•\-\*]\s*\*\*(.*?)\*\*:?\s*(.*)$""").find(trimmed)
            if (bulletMatch != null) {
                val header = bulletMatch.groupValues[1].replace("*", "").trim()
                val body = bulletMatch.groupValues[2].replace("**", "").replace("*", "").trim()

                if (header.isNotBlank() && body.isNotBlank() && !header.equals("section", ignoreCase = true)) {
                    val sentences = body.split(Regex("""(?<=[.!?])\s+""")).filter { it.isNotBlank() }
                    val firstSentence = sentences.firstOrNull() ?: body
                    val remaining = if (sentences.size > 1) sentences.drop(1).joinToString(" ") else "Essential exam topic"

                    val sectionPrefix = when {
                        header.startsWith("AS ", ignoreCase = true) -> header.take(7)
                        header.startsWith("SA ", ignoreCase = true) -> header.take(7)
                        header.startsWith("Sec", ignoreCase = true) -> header.take(8)
                        else -> "Blueprint • ${paper.shortName}"
                    }

                    rows.add(
                        com.example.data.model.StatutoryMatrixRow(
                            sectionOrClause = sectionPrefix,
                            provisionName = header,
                            thresholdOrLimit = firstSentence.take(90),
                            approvalOrRate = remaining.take(90),
                            penaltyOrException = "Exam Trap & Blueprint Rule: ${body.take(80)}"
                        )
                    )
                }
            }
        }
        return rows
    }

    fun readBlueprintForPaper(context: Context, paper: PaperCode): String {
        return getBlueprintMarkdown(context, paper)
    }

    fun getBlueprintMarkdown(context: Context, paper: PaperCode): String {
        blueprintCache[paper]?.let { return it }

        val filename = paperToBlueprintFile[paper]
        val text = if (filename != null) {
            try {
                runBlocking(Dispatchers.IO) {
                    readAssetSafely(context, filename, paper)
                }
            } catch (e: Exception) {
                getFallbackSummary(paper)
            }
        } else {
            getFallbackSummary(paper)
        }

        blueprintCache[paper] = text
        return text
    }

    private suspend fun readAssetSafely(context: Context, filename: String, paper: PaperCode): String {
        return withContext(Dispatchers.IO) {
            try {
                val assetManager = context.assets
                val filesInAssets = assetManager.list("") ?: emptyArray()
                val targetFile = filesInAssets.firstOrNull { it.equals(filename, ignoreCase = true) } ?: filename

                assetManager.open(targetFile).use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        val readText = reader.readText()
                        if (readText.isNotBlank()) readText else getFallbackSummary(paper)
                    }
                }
            } catch (e: Exception) {
                getFallbackSummary(paper)
            }
        }
    }

    fun getFallbackSummary(paper: PaperCode): String {
        return when (paper) {
            PaperCode.PAPER_1 -> """
                # CA Intermediate — Advanced Accounting Strategy Blueprint
                ### 6-Attempt Exam Trend Analysis (May 2024 – May 2026)

                **1. Core Fortress Chapters (Tier 1 - Non-Negotiable):**
                • **Schedule III Financial Statements:** 14M Compulsory Q2 in 6/6 attempts. Always test Land Revaluation and Machinery Sale adjustments.
                • **Compulsory Q1 Pairing:** AS 15 (Employee Benefits) + AS 10 (PPE & AS 28 Impairment) for 10 guaranteed marks.
                • **Amalgamation (AS 14):** 14-16 marks in 6/6 attempts. Master Purchase vs Pooling of Interests.
                • **Consolidation (AS 21/23/27):** Flat 14 marks in 6/6 attempts. Step-acquisition is key.
                • **Cash Flow Statement (AS 3):** 7-8 marks in 6/6 attempts. Watch for non-cash financing traps.
                • **Internal Reconstruction + Buyback:** 14-22 marks combined. CRR computation is essential.

                **2. Exam Execution Strategy:**
                • Start with Q1 (AS 15 + AS 10) to secure early confidence.
                • Move to Q2 (Schedule III) with clean formats.
                • Apply the 10-Minute Move-On rule if stuck.
            """.trimIndent()
            PaperCode.PAPER_2 -> """
                # CA Intermediate — Corporate and Other Laws Strategy Blueprint
                ### 12-Attempt Exam Trend Analysis (Dec 2021 – May 2026)

                **1. The Core Four (Tier 1 - 35 to 44 Marks Guaranteed):**
                • **FEMA 1999:** 13.7M avg across 6/6 attempts. LRS USD 250,000 and Current vs Capital transactions.
                • **LLP Act 2008:** 12.3M avg across 6/6 attempts. Sec 58 conversion and partner liabilities.
                • **General Clauses Act 1897:** 8.0M in 12/12 attempts (never once skipped!).
                • **Interpretation of Statutes:** 8.0M in 12/12 attempts. Master all 8 rules.

                **2. Compulsory Q1 Pattern:**
                • Always tests 3 different laws at once (Companies Act + LLP + FEMA).
                • Use the 4-part legal answer format: Definition, Section, Provision, Exception.
            """.trimIndent()
            PaperCode.PAPER_3 -> """
                # CA Intermediate — Taxation Strategy Blueprint
                ### 12-Attempt Exam Trend Analysis (Dec 2021 – May 2026)

                **1. Must-Do Core Chapters (Tier 1):**
                • **Computation of Total Income & Tax Payable:** 15M Compulsory Q1 in 100% of exams under both regimes.
                • **PGBP (Profits & Gains):** 15M avg. Sec 43B(h) MSME rule and depreciation blocks.
                • **Input Tax Credit (ITC):** 10M in 12/12 exams. Sec 17(5) blocked credits and Rule 86B 99% cap.
                • **Advance Tax & TDS/TCS:** 12/12 exams. Master rate and threshold tables.
                • **Calculation of GST Payable:** Locked at 10M in last 3 exams (Supply + ITC + Payment).
            """.trimIndent()
            PaperCode.PAPER_4 -> """
                # CA Intermediate — Cost and Management Accounting Strategy
                ### 9-Attempt Exam Trend Analysis (May 2023 – May 2026)

                **1. Tier 1 Core Chapters (60%+ of the Paper):**
                • **Marginal Costing:** 11.4M avg (up to 18M). CVP, Break-Even, and Limiting Factor decisions.
                • **Standard Costing:** 9.1M avg. Material and Labour variances reconciliation.
                • **Material Cost:** 11.2M avg. Forward and reverse EOQ, inventory valuation.
                • **Employee Cost:** 9.7M avg. Halsey/Rowan incentives and labour turnover rates.
                • **Activity Based Costing (ABC):** 9.7M avg. ABC vs Absorption comparison.
                • **Budgetary Control:** 9.3M avg. Flexible budget cost behaviour.
            """.trimIndent()
            PaperCode.PAPER_5 -> """
                # CA Intermediate — Auditing and Ethics Strategy Blueprint
                ### 7-Attempt Exam Trend Analysis (May 2024 – May 2026)

                **1. Tier 1 Fortress Chapters:**
                • **Audit of Items of Financial Statements (Ch 5):** 17.0M avg (up to 23M). Balance Sheet and P&L procedures.
                • **Audit Evidence (Ch 4 - SA 500 series):** 14.6M avg. SA 501, 505, 530, and 550.

                **2. Tier 2 High Yield:**
                • **Audit Strategy & Planning (SA 300):** 11.0M avg. 7/7 attempts.
                • **Risk Assessment & Internal Control (SA 315/320):** 11.6M avg. Automated environments.
                • **Audit Report & CARO 2020:** 10.7M avg. Rotates clauses every attempt.
            """.trimIndent()
            PaperCode.PAPER_6 -> """
                # CA Intermediate — FM & SM Master Strategy Blueprint
                ### 12-Attempt Exam Trend Analysis (Dec 2021 – May 2026)

                **1. Tier 1 MUST Chapters (FM & SM):**
                • **SM Ch 4 Strategic Choices:** 10-14M. BCG, Ansoff, GE Matrix, Porter Generic.
                • **SM Ch 5 Implementation & Evaluation:** 10-15M. Lewin's 3-Step Model (8/12 exams).
                • **FM Ch 9 Working Capital Management:** 9-12M. Operating cycle and Cash budgeting.
                • **SM Ch 1 Introduction to SM:** 9-12M. Vision/Mission/Goals and Strategic levels.
                • **FM Ch 4 Cost of Capital:** 7-10M. WACC and marginal cost formulas.
                • **FM Ch 7 Investment Decisions:** 6-9M. NPV, IRR, and capital rationing.
            """.trimIndent()
        }
    }
}
