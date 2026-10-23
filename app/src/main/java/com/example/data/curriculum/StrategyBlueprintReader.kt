package com.example.data.curriculum

import android.content.Context
import com.example.data.model.PaperCode
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap

object StrategyBlueprintReader {

    private val blueprintCache = ConcurrentHashMap<PaperCode, String>()

    val paperToBlueprintFile = mapOf(
        PaperCode.PAPER_1 to "CA_Inter_AdvAcc_Strategy_Blueprint.md",
        PaperCode.PAPER_2 to "CA_Inter_Corporate_Other_Laws_Strategy.md",
        PaperCode.PAPER_3 to "CA_Inter_Taxation_Complete_Strategy.md",
        PaperCode.PAPER_4 to "CA_Inter_CMA_Topic_Wise_Strategy.md",
        PaperCode.PAPER_5 to "CA_Inter_Audit_Ethics_Strategy.md",
        PaperCode.PAPER_6 to "CA_Inter_FM_SM_Master_Strategy_Blueprint.md"
    )

    fun getBlueprintMarkdown(context: Context, paper: PaperCode): String {
        blueprintCache[paper]?.let { return it }

        val filename = paperToBlueprintFile[paper]
        val text = if (filename != null) {
            try {
                kotlinx.coroutines.runBlocking(kotlinx.coroutines.Dispatchers.IO) {
                    val assetManager = context.assets
                    val filesInAssets = assetManager.list("") ?: emptyArray()
                    val targetFile = filesInAssets.firstOrNull { it.equals(filename, ignoreCase = true) } ?: filename

                    assetManager.open(targetFile).use { inputStream ->
                        BufferedReader(InputStreamReader(inputStream)).use { reader ->
                            val readText = reader.readText()
                            if (readText.isNotBlank()) readText else getFallbackSummary(paper)
                        }
                    }
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
