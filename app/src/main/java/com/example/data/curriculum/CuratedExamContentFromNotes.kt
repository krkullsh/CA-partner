// File: app/src/main/java/com/example/data/curriculum/CuratedExamContentFromNotes.kt
package com.example.data.curriculum

import com.example.data.model.PaperCode

/**
 * 1. EXAM-BASED CONCEPT VS. CONCEPT (WITH PYQ PROBABILITY & EXAM QUESTIONS)
 * Sourced directly from the uploaded CA Inter Revision & Audit Notes.
 */
data class NoteExamComparison(
    val id: String,
    val paper: PaperCode,
    val chapterShortTag: String,
    val conceptA: String,
    val conceptB: String,
    val examProbabilityBadge: String,
    val pastPaperTrend: String,
    val basisRows: List<Triple<String, String, String>>, // (Basis, Concept A, Concept B)
    val examBasedQuestion: String,
    val icaiExamSolution: String,
    val examinerObservation: String
)

/**
 * 2. OFFLINE TEACHER (TOPIC RADAR & WHAT CAME IN PAST PAPERS)
 * Strictly distinct from Chapter Notes — tells the student WHAT topics exist,
 * WHAT came in past papers, and HOW students lose marks.
 */
data class OfflineTeacherRadarNote(
    val topicId: String,
    val paper: PaperCode,
    val chapterTitle: String,
    val examProbability: String,
    val subTopicsInChapter: List<String>,
    val whatCameInPastPapers: List<String>,
    val howStudentsLoseMarks: List<String>
)

/**
 * 3. LITERAL CHAPTER NOTES (NO TIER SYSTEM — DIRECT CHAPTER SUBSTANCE)
 * Shows exact rules, Formula Logic, Accounting Treatments, and Common Confusions.
 */
data class LiteralChapterNoteSection(
    val topicId: String,
    val sectionHeading: String,
    val coreRuleOrProvision: String,
    val formulaOrAccountingLogic: String,
    val commonStudentConfusion: String
)

object IntegratedNotesRepository {

    // =========================================================================
    // A. CONCEPT VS. CONCEPT (ALL 6 PAPERS — WITH PYQ QUESTIONS & SOLUTIONS)
    // =========================================================================
    val examComparisons: List<NoteExamComparison> = listOf(

        // ---------------------------------------------------------------------
        // PAPER 1: ADVANCED ACCOUNTING
        // ---------------------------------------------------------------------
        NoteExamComparison(
            id = "adv_acc_as13_cum_vs_ex",
            paper = PaperCode.PAPER_1,
            chapterShortTag = "Adv Acc • AS 13 (Investments)",
            conceptA = "Cum-Interest Price",
            conceptB = "Ex-Interest Price",
            examProbabilityBadge = "95% EXAM PROBABILITY • Tested in almost every AS 13 numerical",
            pastPaperTrend = "Tested in 5–10 mark Investment Account ledger questions (Debentures / Bonds).",
            basisRows = listOf(
                Triple("Meaning", "Quoted price INCLUDES accrued interest from last interest date to transaction date.", "Quoted price EXCLUDES accrued interest; buyer pays interest separately over & above price."),
                Triple("Cost of Investment (Capital Column)", "Cost = Total Paid − Accrued Interest + Brokerage/Stamp Duty.", "Cost = Ex-Interest Price Paid + Brokerage/Stamp Duty."),
                Triple("Total Cash Outflow / Inflow", "Equals the quoted Cum-Interest price (± brokerage).", "Equals Ex-Interest price + Accrued Interest (± brokerage)."),
                Triple("Disposal Adjustment", "Accrued interest up to date of sale is deducted from Gross Sale Proceeds to find Capital Receipt.", "Ex-interest sale price (net of brokerage) goes directly to Capital Column.")
            ),
            examBasedQuestion = "EXAM QUESTION (AS 13): On 1.7.20X1, a company purchased 100, 8% Debentures of ₹100 each at ₹98 Ex-Interest with brokerage @ 1% on purchase price. Interest is payable on 31st March and 30th September. Compute the Interest Element and Investment Cost Element.",
            icaiExamSolution = "1. Interest Element (1st April to 30th June = 3 months):\n" +
                "   = 100 × ₹100 × 8% × (3/12) = ₹200 (Debited to Interest Column).\n" +
                "2. Investment Cost Element (Capital Column):\n" +
                "   = (100 × ₹98) + [1% of (100 × ₹98)] = ₹9,800 + ₹98 = ₹9,898.",
            examinerObservation = "Students frequently calculate accrued interest on the purchase price (₹98) instead of Nominal/Face Value (₹100), or forget to add brokerage to the Capital Cost column."
        ),

        NoteExamComparison(
            id = "adv_acc_as1_going_concern",
            paper = PaperCode.PAPER_1,
            chapterShortTag = "Adv Acc • AS 1 (Policies & Framework)",
            conceptA = "Going Concern Assumption Valid",
            conceptB = "Going Concern Assumption NOT Valid",
            examProbabilityBadge = "85% EXAM PROBABILITY • Classic 5-Mark Comparative P&L/B/S Question",
            pastPaperTrend = "Asked as a comparative Balance Sheet & P&L reconstruction question where an entity faces liquidation.",
            basisRows = listOf(
                Triple("Fixed Assets / PPE", "Carried at Historical Cost less accumulated depreciation.", "Stated at Net Realisable Value (NRV) / Market Realisable Value."),
                Triple("Inventory Valuation", "Valued at LOWER of Cost or Net Realisable Value (AS 2).", "Valued directly at Net Realisable Value (even if NRV > Cost)."),
                Triple("Deferred Expenditure", "Amortised over useful benefit period as per policy.", "Written off 100% immediately to Profit & Loss Account (Balance Sheet value = Nil)."),
                Triple("Liabilities & Loan Penalties", "Recorded at contracted book obligation.", "Recorded at actual settlement value (including loan penalties and creditor discounts/waivers).")
            ),
            examBasedQuestion = "EXAM QUESTION (AS 1 / Framework): A trader's books show Deferred Expenditure of ₹15,000, Closing Stock (Cost ₹32,000, NRV ₹38,000), Trade Payables ₹50,000 (creditors agreed to ₹500 discount on early settlement), and an unrecorded Loan Penalty of ₹2,000. State the P&L impact if Going Concern is NOT valid.",
            icaiExamSolution = "When Going Concern is NOT valid:\n" +
                "1. Closing Stock is valued at NRV = ₹38,000 (not lower of cost or NRV).\n" +
                "2. Entire Deferred Expenditure of ₹15,000 is debited to P&L A/c.\n" +
                "3. Loan Penalty of ₹2,000 is debited to P&L A/c.\n" +
                "4. Discount on Trade Payables of ₹500 is credited to P&L A/c.",
            examinerObservation = "Students generally forget discount on creditors and loan penalty adjustments, and fail to write off 100% of deferred expenditure when Going Concern is invalid."
        ),

        NoteExamComparison(
            id = "adv_acc_as20_basic_vs_diluted_eps",
            paper = PaperCode.PAPER_1,
            chapterShortTag = "Adv Acc • AS 20 (Earnings Per Share)",
            conceptA = "Basic EPS",
            conceptB = "Diluted EPS",
            examProbabilityBadge = "92% EXAM PROBABILITY • High-Frequency 5-Mark Numerical",
            pastPaperTrend = "Tested on Rights Issue TERP adjustment factor, Bonus restatement, and Convertible Debentures net-of-tax interest add-back.",
            basisRows = listOf(
                Triple("Numerator (Earnings)", "Net Profit/Loss attributable to Equity Shareholders (after deducting preference dividend: cumulative for full year regardless of declaration; non-cumulative only if provided).", "Basic EPS Earnings + After-tax interest saved on convertible debt + Dividends on dilutive potential equity shares."),
                Triple("Denominator (Shares)", "Weighted Average Number of Equity Shares (WANES) actually outstanding during the period.", "WANES + Incremental dilutive potential equity shares assumed converted from beginning of period (or issue date if later)."),
                Triple("Partly Paid Shares", "Included as a fraction ONLY if entitled to participate in dividends.", "Included in Diluted EPS if NOT entitled to participate in dividends until fully paid."),
                Triple("Share Options / Warrants", "Excluded until exercised.", "Numerator unchanged; only free/incremental shares added to denominator."),
                Triple("Anti-Dilutive Instruments", "Not applicable.", "COMPLETELY IGNORED (tested against Profit from Continuing Ordinary Activities as control factor).")
            ),
            examBasedQuestion = "EXAM QUESTION (AS 20): For 20X1-X2, Net Profit after tax is ₹55,00,000. The company has 10,000, 10% Cumulative Preference Shares of ₹100 each, WANES of 9,58,334 shares, and 50,000, 12% Convertible Debentures of ₹100 each (each convertible into 8 equity shares). Tax rate is 30%. Compute Basic and Diluted EPS.",
            icaiExamSolution = "1. Basic EPS:\n" +
                "   • Numerator (EATESH) = ₹55,00,000 − ₹1,00,000 (Cumulative Pref. Dividend) = ₹54,00,000.\n" +
                "   • Basic EPS = ₹54,00,000 ÷ 9,58,334 shares = ₹5.63.\n" +
                "2. Diluted EPS:\n" +
                "   • Adjusted Numerator = ₹54,00,000 + [₹6,00,000 × (1 − 0.30)] = ₹58,20,000.\n" +
                "   • Adjusted Denominator = 9,58,334 + (50,000 × 8) = 13,58,334 shares.\n" +
                "   • Diluted EPS = ₹58,20,000 ÷ 13,58,334 shares = ₹4.28.",
            examinerObservation = "Students often forget to deduct cumulative preference dividend when undeclared, or forget to adjust debenture interest NET OF TAX (1 - t) when computing Diluted EPS."
        ),

        NoteExamComparison(
            id = "adv_acc_as11_ifo_vs_nfo",
            paper = PaperCode.PAPER_1,
            chapterShortTag = "Adv Acc • AS 11 & Foreign Branches",
            conceptA = "Integral Foreign Operation (IFO)",
            conceptB = "Non-Integral Foreign Operation (NFO)",
            examProbabilityBadge = "88% EXAM PROBABILITY • Tested in AS 11 & Foreign Branch Trial Balance",
            pastPaperTrend = "Tested on translation of Foreign Branch Trial Balance and Para 46A exchange difference capitalisation.",
            basisRows = listOf(
                Triple("Nature of Operation", "Carries on business as if it were an extension of the reporting enterprise's operations.", "Accumulates cash and other monetary items, incurs expenses, generates income in its local currency autonomously."),
                Triple("Non-Monetary Assets & Depr.", "Translated at Historical Rate on date of purchase (or valuation date).", "Translated at Closing Rate on Balance Sheet date."),
                Triple("Treatment of Exchange Difference", "Recognised immediately in Profit & Loss Account (impacts current year profit).", "Accumulated in Foreign Currency Translation Reserve (FCTR) under Equity (not P&L) until disposal."),
                Triple("Reclassification Rule", "Change IFO → NFO: Exchange difference on non-monetary items on date of change goes to FCTR.", "Change NFO → IFO: Translated amounts on date of change become historical cost; FCTR held till disposal.")
            ),
            examBasedQuestion = "EXAM QUESTION (AS 11 Para 46A): A Ltd. (Indian company) borrowed $1,000 on 1.4.20X1 (repayable in 5 years) to finance a PPE (useful life 10 years). Exchange rate on 1.4.20X1 = ₹80/$; on 31.3.20X2 = ₹82/$. State the treatment of the ₹2,000 exchange loss on 31.3.20X2 if (i) Para 46A is NOT adopted, (ii) Para 46A is adopted for PPE, and (iii) Para 46A is adopted for non-depreciable asset.",
            icaiExamSolution = "Exchange Loss on Loan = $1,000 × (₹82 − ₹80) = ₹2,000:\n" +
                "• Case (i) Para 46A NOT adopted: Charge full ₹2,000 to P&L A/c; Depreciation on PPE = ₹80,000 ÷ 10 = ₹8,000.\n" +
                "• Case (ii) Para 46A adopted (for PPE): Add ₹2,000 to cost of PPE (Revised PPE = ₹82,000); Revised Depreciation = ₹82,000 ÷ 10 = ₹8,200.\n" +
                "• Case (iii) Para 46A adopted (not for PPE): Transfer ₹2,000 to FCMITDA A/c and amortise over loan tenure = ₹2,000 ÷ 5 years = ₹400 to P&L.",
            examinerObservation = "When Para 46A is adopted for a depreciable asset (PPE), students often transfer the exchange loss to FCMITDA instead of adding it to the carrying cost of PPE and revising depreciation."
        ),

        NoteExamComparison(
            id = "adv_acc_as17_segment_10_vs_75",
            paper = PaperCode.PAPER_1,
            chapterShortTag = "Adv Acc • AS 17 (Segment Reporting)",
            conceptA = "10% Materiality Threshold Test",
            conceptB = "75% Overall Reporting Test",
            examProbabilityBadge = "86% EXAM PROBABILITY • Favourite 5-Mark Case & MCQ",
            pastPaperTrend = "Frequently tested trap on whether inter-segment revenue is included or excluded.",
            basisRows = listOf(
                Triple("Revenue Base Used", "Uses TOTAL Segment Revenue (External Sales + Inter-Segment Transfers).", "Uses EXTERNAL Revenue ONLY (excludes inter-segment transfers)."),
                Triple("Test Criteria", "Segment is reportable if Revenue ≥ 10%, OR Result ≥ 10% of combined profit/loss (whichever is greater in absolute terms), OR Assets ≥ 10%.", "Total External Revenue of all reportable segments must be ≥ 75% of Total Enterprise Revenue."),
                Triple("Consequence if Not Met", "Segment is not reportable unless picked up under the 75% rule or management discretion.", "Additional segments (even < 10%) MUST be identified as reportable until the 75% external revenue mark is reached.")
            ),
            examBasedQuestion = "EXAM QUESTION (AS 17): While identifying reportable segments under AS 17, an accountant included inter-segment transfers for both the 10% materiality test and the 75% overall revenue test. Comment on the validity of this approach.",
            icaiExamSolution = "The accountant's approach is partly incorrect:\n" +
                "1. For the 10% Materiality Test: Including both External Revenue and Inter-Segment Revenue is CORRECT.\n" +
                "2. For the 75% Overall Test: Inter-segment revenue MUST BE EXCLUDED. Total External Revenue attributable to reportable segments must constitute at least 75% of Total Enterprise External Revenue.",
            examinerObservation = "Examiner Point: 75% test uses EXTERNAL revenue only (not inter-segment revenue), whereas the 10% test uses BOTH internal and external revenue. Mixing these two bases is the #1 reason students lose marks in AS 17."
        ),

        // ---------------------------------------------------------------------
        // PAPER 2: CORPORATE & OTHER LAWS
        // ---------------------------------------------------------------------
        NoteExamComparison(
            id = "law_ios_subject_to_vs_notwithstanding",
            paper = PaperCode.PAPER_2,
            chapterShortTag = "Law • Interpretation of Statutes",
            conceptA = "'Subject to...' vs 'Notwithstanding...'",
            conceptB = "'Means' vs 'Includes' & Proviso vs Explanation",
            examProbabilityBadge = "90% EXAM PROBABILITY • Guaranteed 3–4 Mark Statutory Aid Question",
            pastPaperTrend = "Tested in almost every paper under Internal Aids to Interpretation and Rules of Construction of Deeds.",
            basisRows = listOf(
                Triple("Subject to vs Notwithstanding", "'Subject to' makes the provision SUBSERVIENT (the other referenced provision prevails). 'Notwithstanding' (Non-obstante clause) is OVERRIDING (this clause prevails over conflicting provisions).", "'Without prejudice to' means the particular provisions operate IN ADDITION TO and do not restrict the general provision."),
                Triple("'Means' vs 'Includes' in Definitions", "'Means' makes a definition RESTRICTIVE and exhaustive (no other meaning can be assigned).", "'Includes' makes a definition EXTENSIVE and inclusive (enlarges ordinary meaning to cover items not normally included)."),
                Triple("Proviso vs Explanation", "Proviso ('Provided that...') carves out an EXCEPTION or qualification to the main enactment.", "Explanation clarifies ambiguity to harmonise with the main section; it CANNOT widen the ambit of the main section."),
                Triple("Conflict in Deeds vs Statutes", "In a Statute: Harmonious construction applies; specific overrides general (generalia specialibus non derogant).", "In a Deed/Contract: First harmonise all clauses; if irreconcilable conflict remains, the EARLIER clause overrides the latter clause.")
            ),
            examBasedQuestion = "EXAM QUESTION (Interpretation of Statutes): Gaurav Textile Company Ltd. entered into a commercial contract where Clause 4 and Clause 11 are in direct conflict and cannot be reconciled. Separately, explain the difference between a 'Proviso' and an 'Explanation' in a statute.",
            icaiExamSolution = "1. Conflict in Deed/Contract: When interpreting a commercial contract, an effort must first be made to resolve the conflict by interpreting all clauses harmoniously so every clause is given effect. If reconciliation is impossible, in a deed/contract the EARLIER clause (Clause 4) overrides the latter clause (Clause 11).\n" +
                "2. Proviso vs Explanation: A Proviso carves out an exception or qualification to the main provision, whereas an Explanation is added to clear up ambiguity and harmonise the main section without widening its ambit.",
            examinerObservation = "Students often reverse the rule for deeds vs. statutes: in a Deed/Contract, the EARLIER clause prevails in an irreconcilable conflict, whereas in a Statute/Will, the later provision prevails."
        ),

        // ---------------------------------------------------------------------
        // PAPER 3: TAXATION (DT & GST)
        // ---------------------------------------------------------------------
        NoteExamComparison(
            id = "tax_gst_tds_vs_tcs",
            paper = PaperCode.PAPER_3,
            chapterShortTag = "Taxation (GST) • TDS & TCS",
            conceptA = "GST TDS (Section 51)",
            conceptB = "GST TCS (Section 52)",
            examProbabilityBadge = "90% EXAM PROBABILITY • High-Yield MCQ & 4-Mark Case Question",
            pastPaperTrend = "Tested on threshold contract value (₹2.5 Lakhs exclusive of GST) and location of supplier vs place of supply proviso.",
            basisRows = listOf(
                Triple("Who Deducts / Collects", "Specified Government departments, Local Authorities, PSUs, and notified entities.", "Every Electronic Commerce Operator (ECO) not being an agent."),
                Triple("Threshold Trigger", "Total value of taxable supply under a contract EXCEEDS ₹2,50,000 (excluding CGST, SGST, IGST, Cess).", "No monetary contract threshold for ECOs (collected on Net Value of Taxable Supplies)."),
                Triple("Effective Rate", "2% (1% CGST + 1% SGST for intra-state; 2% IGST for inter-state).", "0.5% (0.25% CGST + 0.25% SGST for intra-state; 0.5% IGST for inter-state)."),
                Triple("No-Deduction / Exception Trap", "NO TDS if Location of Supplier AND Place of Supply are in a State/UT DIFFERENT from Recipient's registration State.", "Sales returns of taxable supplies made through ECO are deducted to arrive at Net Value.")
            ),
            examBasedQuestion = "EXAM QUESTION (GST Sec 51): Supplier 'A' (registered in Delhi) makes a taxable supply of goods worth ₹3,36,000 (inclusive of 12% GST) to a PSU registered in Haryana, where the Place of Supply is also Delhi. Is GST TDS under Section 51 applicable?",
            icaiExamSolution = "1. Contract Value excluding GST = ₹3,36,000 × (100 / 112) = ₹3,00,000 (Exceeds ₹2,50,000 threshold).\n" +
                "2. However, apply the Proviso to Sec 51(1): Location of Supplier (Delhi) AND Place of Supply (Delhi) are in a State DIFFERENT from the State of registration of the Recipient PSU (Haryana).\n" +
                "3. Conclusion: NO TDS shall be deducted.",
            examinerObservation = "Students check only the ₹2,50,000 limit (often forgetting to exclude GST first) and miss the proviso where Supplier Location + Place of Supply differ from Recipient's registration State."
        ),

        NoteExamComparison(
            id = "tax_gst_sec73_74_vs_74a",
            paper = PaperCode.PAPER_3,
            chapterShortTag = "Taxation (GST) • Payment of Tax & Sec 74A",
            conceptA = "Sections 73 & 74 (Up to FY 2023-24)",
            conceptB = "Section 74A (FY 2024-25 Onward)",
            examProbabilityBadge = "92% EXAM PROBABILITY • Latest Statutory Amendment",
            pastPaperTrend = "Tested in Electronic Credit/Cash Ledger set-off order and Rule 88B interest computation.",
            basisRows = listOf(
                Triple("Applicability Period", "Applies to tax periods up to Financial Year 2023-24.", "Applies to Financial Year 2024-25 onwards (unified determination section)."),
                Triple("Statutory Reference in Set-Off Order", "Self-assessed tax of previous returns -> Self-assessed tax of current return -> Dues including demand u/s 73 or 74.", "Dues for FY 2024-25 onward are referenced under Section 74A alongside Sec 73/74 for earlier years."),
                Triple("Electronic Cash Ledger & Interest", "Form GST PMT-09 enables transfer across major/minor heads.", "Interest u/s 50(1) @ 18% p.a. is levied on Net Cash Liability unless proceedings u/s 73, 74, or 74A have commenced (Rule 88B).")
            ),
            examBasedQuestion = "EXAM QUESTION (GST Sec 49 / Rule 88B): M/s Apex delayed filing GSTR-3B for Nov 2025 by 30 days. Total tax liability is ₹5,00,000. Balance available in Electronic Credit Ledger is ₹3,50,000 and ₹1,50,000 is paid via Electronic Cash Ledger. Compute interest u/s 50(1).",
            icaiExamSolution = "As per Proviso to Section 50(1) read with Rule 88B, when return is filed belatedly (and not pursuant to proceedings u/s 73, 74, or 74A), interest @ 18% p.a. is payable ONLY on the portion of tax paid by debiting the Electronic Cash Ledger:\n" +
                "Interest = ₹1,50,000 × 18% × (30 / 365) = ₹2,219.",
            examinerObservation = "Students lose marks by charging 18% interest on the gross liability (₹5,00,000) instead of net cash liability (₹1,50,000), or by forgetting to cite Rule 88B and Section 74A for FY 2024-25+ periods."
        ),

        // ---------------------------------------------------------------------
        // PAPER 4: COST & MANAGEMENT ACCOUNTING
        // ---------------------------------------------------------------------
        NoteExamComparison(
            id = "cma_joint_splitoff_vs_further_processing",
            paper = PaperCode.PAPER_4,
            chapterShortTag = "Costing • Joint Products & Service Costing",
            conceptA = "Sell at Split-Off Point",
            conceptB = "Process Further Beyond Split-Off",
            examProbabilityBadge = "90% EXAM PROBABILITY • Mandatory 5–10 Mark Decision Table",
            pastPaperTrend = "Tested in almost every Joint & By-Product numerical alongside Physical Unit / NRV / Contribution Margin apportionment.",
            basisRows = listOf(
                Triple("Decision Rule", "Choose if Incremental Profit from further processing is ≤ 0.", "Choose if Incremental Revenue > Incremental Further Processing Cost (Incremental Profit > 0)."),
                Triple("Treatment of Joint Costs", "Pre-separation Joint Costs are SUNK / IRRELEVANT for the further processing decision.", "Only post-split-off incremental processing costs and specific fixed costs are deducted."),
                Triple("Service Costing Equivalent Units", "Method 1: Convert all room grades (Deluxe 2.5x, Luxurious 5x) to Base Standard Grade (1x).", "Method 2: Convert all room grades to Luxurious Grade (1/5x, 1/2x)—both methods give the EXACT same final room tariff!")
            ),
            examBasedQuestion = "EXAM QUESTION (Costing — Joint Products & Service Costing): (a) Product X can be sold at split-off for ₹4,00,000 or processed further at an additional cost of ₹1,20,000 to be sold for ₹5,00,000. Apportioned joint cost is ₹2,50,000. Should X be processed further? (b) In a Holiday Home, Peak Season occupancy is 100% and Off-Season tariff is 50% of Peak Season. How are Equivalent Full Room-Days calculated?",
            icaiExamSolution = "Part (a) Further Processing Decision:\n" +
                "• Incremental Revenue = ₹5,00,000 − ₹4,00,000 = ₹1,00,000.\n" +
                "• Less: Incremental Processing Cost = ₹1,20,000 → Incremental Loss = (₹20,000).\n" +
                "• Decision: Do NOT process further; sell at split-off. (Joint cost of ₹2,50,000 is sunk and irrelevant).\n" +
                "Part (b) Service Costing Formula:\n" +
                "• Equivalent Full Room-Days = (Season Room-Days × 100%) + (Off-Season Room-Days × 50%).\n" +
                "• Off-Season Rent = 50% of Peak Season Rent.",
            examinerObservation = "Students mistakenly deduct apportioned pre-separation joint costs when evaluating whether to process a joint product further, leading to wrong accept/reject decisions."
        ),

        // ---------------------------------------------------------------------
        // PAPER 5: AUDITING & ETHICS
        // ---------------------------------------------------------------------
        NoteExamComparison(
            id = "audit_sa510_opening_balances_opinions",
            paper = PaperCode.PAPER_5,
            chapterShortTag = "Audit • SA 510 & SA 500 Evidence",
            conceptA = "Unable to Obtain SAAE on Opening Balances",
            conceptB = "Material Misstatement in Opening Balances (APD Rule)",
            examProbabilityBadge = "88% EXAM PROBABILITY • High-Yield Case Study & Audit Report Modification",
            pastPaperTrend = "Tested as a 4-mark case scenario combining SA 510 (Initial Audit Engagements) and SA 705.",
            basisRows = listOf(
                Triple("Trigger Condition", "Auditor is unable to obtain Sufficient Appropriate Audit Evidence (SAAE) regarding opening balances.", "Opening balances contain a material misstatement affecting Current Period Financial Statements (CPFS), OR accounting policies are not consistently applied."),
                Triple("APD Rule Check", "Scope limitation prevents verification of opening figures.", "Failure to properly ACCOUNT + PRESENT + DISCLOSE (APD Rule) the misstatement or change in policy."),
                Triple("Resulting Audit Opinion", "QUALIFIED Opinion or DISCLAIMER of Opinion (never Adverse for scope limitation).", "QUALIFIED Opinion or ADVERSE Opinion (never Disclaimer for known misstatement)."),
                Triple("Management's Expert (SA 500)", "Primary Evidence = Accounting Records (tested via analysis, review, re-performance, reconciliation).", "When using Management's Expert, evaluate Competence, Capabilities, Objectivity & understand expert's work (Mnemonic: CRAN2ES).")
            ),
            examBasedQuestion = "EXAM QUESTION (SA 510): CA Riya is appointed as the first statutory auditor of Zenith Ltd. for FY 2025-26. She notices a material misstatement in the opening inventory balance that materially affects current year profit, and management refuses to account for or disclose it. What audit opinion should she issue?",
            icaiExamSolution = "As per SA 510 (Initial Audit Engagements — Opening Balances), apply the APD Rule:\n" +
                "1. Any misstatement in opening balances that materially affects the current period's financial statements must be properly Accounted for, Presented, and Disclosed.\n" +
                "2. Since management refuses to account for and disclose the material misstatement, CA Riya shall express a QUALIFIED Opinion or an ADVERSE Opinion (depending on pervasiveness) in accordance with SA 705.",
            examinerObservation = "Students often write 'Disclaimer of Opinion' when a material misstatement is found in opening balances. Remember: Inability to obtain SAAE → Qualified/Disclaimer; Material Misstatement not Accounted/Presented/Disclosed → Qualified/Adverse."
        ),

        // ---------------------------------------------------------------------
        // PAPER 6: FINANCIAL MANAGEMENT & STRATEGIC MANAGEMENT (FM & SM)
        // ---------------------------------------------------------------------
        NoteExamComparison(
            id = "fm_capital_rationing_divisible_vs_nondivisible",
            paper = PaperCode.PAPER_6,
            chapterShortTag = "FM • Capital Budgeting & Receivables",
            conceptA = "Divisible Projects & Replacement Chain",
            conceptB = "Non-Divisible Projects & Equivalent Annual Criterion (EAC)",
            examProbabilityBadge = "94% EXAM PROBABILITY • Core 8–10 Mark Capital Budgeting Problem",
            pastPaperTrend = "Tested on Capital Rationing project selection under budget constraint and Mutually Exclusive Projects with Unequal Lives.",
            basisRows = listOf(
                Triple("Capital Rationing Rule", "Projects can be accepted in fractions → Rank by Profitability Index (PI) and allocate budget top-down, taking a fractional part of the marginal project.", "Projects are indivisible (all-or-nothing) → PI ranking alone is NOT sufficient! Enumerate feasible combinations within budget and pick the combination with highest total NPV."),
                Triple("Unequal Lives Resolution", "Replacement Chain Method: Extend shorter-life project by repetition to a Common Time Horizon and compare combined NPV.", "Equivalent Annual Criterion (EAC = NPV ÷ PVIFA): Convert each project's NPV into an annual annuity over its life and select higher EAC."),
                Triple("Receivables Policy Evaluation", "Total Cost Approach: Computes total contribution and total costs separately under Present and Proposed policies.", "Incremental Approach: Isolates ONLY marginal changes (Incremental Sales, Incremental Variable Cost, Bad Debts, and Opportunity Cost on Incremental Investment in Receivables).")
            ),
            examBasedQuestion = "EXAM QUESTION (FM Capital Rationing): With a capital budget of ₹1,20,000, a firm evaluates Projects E (Outlay ₹35k, NPV ₹19.3k, PI 1.55), B (Outlay ₹40k, NPV ₹18.7k, PI 1.47), C (Outlay ₹25k, NPV ₹10.1k, PI 1.40), and D (Outlay ₹30k, NPV ₹11.2k, PI 1.37). Determine the optimal selection if projects are (i) Divisible and (ii) Non-Divisible.",
            icaiExamSolution = "1. If Projects are DIVISIBLE (Rank by PI: E → B → C → D):\n" +
                "   • Accept E (₹35k) + B (₹40k) + C (₹25k) + 2/3 of D (₹20k) = ₹1,20,000.\n" +
                "   • Total NPV = 19,300 + 18,700 + 10,100 + (2/3 × 11,200) = ₹55,567.\n" +
                "2. If Projects are NON-DIVISIBLE (Compare Feasible Combinations ≤ ₹1,20,000):\n" +
                "   • Combination 1 (E + B + C): Outlay = ₹1,00,000; Total NPV = ₹48,100.\n" +
                "   • Combination 2 (E + B + D): Outlay = ₹1,05,000; Total NPV = 19,300 + 18,700 + 11,200 = ₹49,200.\n" +
                "   • Decision: Select E + B + D (Higher Total NPV of ₹49,200).",
            examinerObservation = "Critical Rule: For non-divisible projects, stopping at PI ranking (E + B + C = ₹48,100) loses marks because Combination E + B + D yields a higher NPV (₹49,200) within the ₹1,20,000 budget."
        ),

        NoteExamComparison(
            id = "fm_dol_vs_dfl_and_sm_growth",
            paper = PaperCode.PAPER_6,
            chapterShortTag = "FM & SM • Leverage & Growth Strategies",
            conceptA = "Operating Leverage (DOL) & Internal Growth",
            conceptB = "Financial Leverage (DFL) & External Growth",
            examProbabilityBadge = "92% EXAM PROBABILITY • Tested in Every FM Leverage & SM Strategy Paper",
            pastPaperTrend = "Tested as Reverse Leverage numerical (finding Sales/EBIT from DOL & DFL) and SM Case Study on Merger vs Strategic Alliance.",
            basisRows = listOf(
                Triple("Core Formula (FM)", "DOL = Contribution ÷ EBIT = % Change in EBIT ÷ % Change in Sales. (Also: DOL = 1 ÷ Margin of Safety).", "DFL = EBIT ÷ [EBT − (Preference Dividend ÷ (1 − t))]. Measures sensitivity of EPS to changes in EBIT."),
                Triple("Break-Even & Risk Anchor", "Operating Break-Even = Fixed Cost ÷ Contribution per unit. Measures Business/Operating Risk.", "Financial Break-Even = Interest + [Preference Dividend ÷ (1 − t)]. Measures Financial Risk."),
                Triple("SM Growth Classification", "Internal Growth: Expansion through Intensification (Ansoff's Market Penetration, Market Development, Product Development) or Internal Diversification.", "External Growth: Mergers & Acquisitions (Horizontal, Vertical, Conglomerate—mirroring Diversification types) and Strategic Alliances (partners remain independent entities).")
            ),
            examBasedQuestion = "EXAM QUESTION (FM Reverse Leverage): Company A has Variable Cost Ratio = 60%, Interest = ₹20,000, Operating Leverage (DOL) = 5, and Financial Leverage (DFL) = 3. Compute EBIT, Contribution, and Sales.",
            icaiExamSolution = "Step 1 — Find EBIT using DFL:\n" +
                "  DFL = EBIT ÷ (EBIT − Interest) → 3 = EBIT ÷ (EBIT − 20,000) → 3 EBIT − 60,000 = EBIT → 2 EBIT = 60,000 → EBIT = ₹30,000.\n" +
                "Step 2 — Find Contribution using DOL:\n" +
                "  DOL = Contribution ÷ EBIT → 5 = Contribution ÷ 30,000 → Contribution = ₹1,50,000.\n" +
                "Step 3 — Find Sales using P/V Ratio:\n" +
                "  P/V Ratio = 100% − 60% = 40% → Sales = ₹1,50,000 ÷ 40% = ₹3,75,000.",
            examinerObservation = "How Students Lose Marks: Using Sales/EBIT instead of Contribution/EBIT for DOL, or forgetting to gross up Preference Dividend by Dp/(1 − t) in the DFL denominator."
        )
    )

    // =========================================================================
    // B. OFFLINE TEACHER RADAR (ALL 6 PAPERS — TOPICS & PAST PAPER TRENDS)
    // =========================================================================
    val teacherRadarByPaper: Map<PaperCode, List<OfflineTeacherRadarNote>> = mapOf(
        PaperCode.PAPER_1 to listOf(
            OfflineTeacherRadarNote(
                topicId = "adv_acc_as13",
                paper = PaperCode.PAPER_1,
                chapterTitle = "AS 13 (Revised): Accounting for Investments",
                examProbability = "95% Probability • 5 to 10 Marks",
                subTopicsInChapter = listOf(
                    "1. Current Investments (Lower of Cost & Fair Value) vs. Long-Term Investments (At Cost less non-temporary decline)",
                    "2. Cost of Investment: Brokerage, Duties, Right Shares (Subscribed vs. Renounced + Cum-Right Exception), Bonus Shares",
                    "3. Cum-Interest vs. Ex-Interest Fixed Income Securities Ledger",
                    "4. Equity Shares Ledger (Pre-acquisition Dividend deducted from Cost vs. Post-acquisition credited to P&L)",
                    "5. Reclassification (Long-Term ↔ Current)"
                ),
                whatCameInPastPapers = listOf(
                    "Composite Equity Share Investment Ledger with Bonus Issue (1:6), Rights Issue (partial renunciation + partial subscription), Pre-acquisition dividend, and sale at average cost (8–10 Marks) [Illustration 9].",
                    "Debenture Investment Account with multiple Cum/Ex-Interest purchases, sale of subset of debentures, and year-end valuation at lower of cost or market value (5–8 Marks).",
                    "Reclassification practical scenarios across all directions with worked amounts (4–5 Marks)."
                ),
                howStudentsLoseMarks = listOf(
                    "Crediting sale of rights to P&L without checking the Cum-Right market decline exception.",
                    "Calculating dividend or interest on Market/Purchase price instead of Face/Nominal Value.",
                    "Forgetting to compute Weighted Average Cost when selling a portion of shares or debentures."
                )
            ),
            OfflineTeacherRadarNote(
                topicId = "adv_acc_as20_as11_as21",
                paper = PaperCode.PAPER_1,
                chapterTitle = "AS 20 (EPS), AS 11 (Foreign Exchange) & Company Accounts (Buyback / Amalgamation / CFS)",
                examProbability = "95% Probability • 25 to 35 Marks Combined across Paper 1",
                subTopicsInChapter = listOf(
                    "1. AS 20: Basic EPS, Diluted EPS, Bonus Restatement, Rights Issue TERP & Adjustment Factor, Incremental EPS Sequencing",
                    "2. AS 11: Initial & Closing Recognition, Para 46A (PPE vs. FCMITDA), Integral (IFO) vs. Non-Integral (NFO) Branch Translation",
                    "3. Buyback (Sec 68): 3-Test Limit + Simultaneous Equation when CRR transfer reduces Free Reserves below statutory floor",
                    "4. Amalgamation (AS 14) & Internal Reconstruction: Merger (Pooling) vs. Purchase Method, Inter-company owings & Unrealised Profit on stock",
                    "5. Consolidated Financial Statements (AS 21): 9-Step Procedure (DOA vs. DOC, Pre/Post Reserves, Cost of Control, Minority Interest)"
                ),
                whatCameInPastPapers = listOf(
                    "AS 20 Rights Issue (TERP & Adjustment Factor) + Restated Prior Year EPS + Convertible Debentures Diluted EPS (5–8 Marks) [Questions 12, 15].",
                    "Buyback Simultaneous Equation numerical where Free Reserves are insufficient to cover both maximum buyback and CRR transfer (`x = CRR`, `y = Buyback`) (5–8 Marks).",
                    "AS 14 Amalgamation Balance Sheet & Realization Ledger with inter-company loan/debtors and unrealised profit on unsold stock (10–14 Marks)."
                ),
                howStudentsLoseMarks = listOf(
                    "AS 20: Applying Rights Issue adjustment factor to post-rights shares, or including anti-dilutive shares in Diluted EPS.",
                    "AS 21: Passing journal entries in group books (there are NO separate group books—consolidation is a presentation worksheet) or placing Minority Interest under Current Liabilities.",
                    "AS 1 (Pg. 43–45): Failing to quantify the exact ₹ crore impact on profit when drafting Notes to Accounts for a change in inventory valuation policy."
                )
            )
        ),

        PaperCode.PAPER_2 to listOf(
            OfflineTeacherRadarNote(
                topicId = "law_ios_deeds",
                paper = PaperCode.PAPER_2,
                chapterTitle = "Interpretation of Statutes, Deeds and Documents",
                examProbability = "90% Probability • 6 to 8 Marks in every exam",
                subTopicsInChapter = listOf(
                    "1. Primary Rules: Literal/Grammatical Rule, Golden Rule (ut res magis valeat quam pereat), Mischief Rule (Heydon's 4 Questions), Harmonious Construction, Ejusdem Generis, Noscitur a Sociis",
                    "2. Internal Aids: Preamble, Heading, Marginal Notes (not part of enactment except Constitution), Proviso, Explanation, Schedules, 'Means' vs. 'Includes'",
                    "3. Statutory Clauses: 'Subject to' (Subservient) vs. 'Notwithstanding' (Overriding) vs. 'Without Prejudice'",
                    "4. 4 Elements of a Document (Matter, Record, Substance, Means) & Conflict between Clauses in a Deed"
                ),
                whatCameInPastPapers = listOf(
                    "Case question on Gaurav Textile Co. Ltd. asking how to resolve a conflict between two clauses of a commercial contract (Earlier clause prevails if harmony is impossible) (4 Marks).",
                    "Distinction between 'Proviso' and 'Explanation' and whether an Explanation can widen the scope of the main section (3–4 Marks).",
                    "Application of Ejusdem Generis and Heydon's Mischief Rule when statutory language is ambiguous (4 Marks)."
                ),
                howStudentsLoseMarks = listOf(
                    "Writing that the 'later clause' prevails in a contract/deed (in a Deed, the EARLIER clause overrides if irreconcilable).",
                    "Applying Mischief Rule or Preamble to override plain, unambiguous statutory words.",
                    "Confusing 'Subject to' (yields to other section) with 'Notwithstanding' (overrides other section)."
                )
            )
        ),

        PaperCode.PAPER_3 to listOf(
            OfflineTeacherRadarNote(
                topicId = "tax_gst_payment_tds_tcs",
                paper = PaperCode.PAPER_3,
                chapterTitle = "GST: Payment of Tax (Sec 49–50, Sec 74A) & TDS/TCS (Sec 51–52)",
                examProbability = "90% Probability • 4 to 8 Marks + 2-Mark Case MCQ",
                subTopicsInChapter = listOf(
                    "1. Electronic Cash Ledger, Electronic Credit Ledger & Electronic Liability Register (Sec 49)",
                    "2. Mandatory ITC Utilization & Set-Off Order (Sec 49, 49A, 49B & Rule 88A)",
                    "3. Interest on Delayed Payment of Tax (Sec 50 & Rule 88B + Circular 192/04/2023)",
                    "4. Order of Discharge of Tax & Other Dues (Sec 49(8) — Sec 73/74 vs. Sec 74A for FY 2024-25+)",
                    "5. GST TDS (Sec 51) & GST TCS by E-Commerce Operators (Sec 52)"
                ),
                whatCameInPastPapers = listOf(
                    "Practical computation of Minimum Cash Payment after applying Rule 88A (IGST credit exhaustion before CGST/SGST) (4–6 Marks).",
                    "Interest u/s 50(1) & 50(3) when IGST credit is wrongly availed and utilized vs. only availed.",
                    "Applicability of TDS u/s 51 across inter-state vs. intra-state PSU contracts."
                ),
                howStudentsLoseMarks = listOf(
                    "Utilizing CGST or SGST credit before completely exhausting IGST credit (violating Sec 49A / Rule 88A).",
                    "Cross-utilizing CGST credit against SGST liability (strictly prohibited).",
                    "Forgetting to cite Section 74A for FY 2024-25 onward when stating the statutory order of liability discharge."
                )
            )
        ),

        PaperCode.PAPER_4 to listOf(
            OfflineTeacherRadarNote(
                topicId = "cma_joint_service_costing",
                paper = PaperCode.PAPER_4,
                chapterTitle = "Joint & By-Products and Service Costing (Hotels / Hospitals / Transport)",
                examProbability = "92% Probability • 10 to 15 Marks Combined",
                subTopicsInChapter = listOf(
                    "1. Joint Cost Apportionment: Physical Units, Net Realisable Value (NRV) at Split-Off, Constant Gross Margin %, Contribution Margin",
                    "2. 4-Step Further Processing Decision Framework (Incremental Revenue vs. Incremental Processing Cost)",
                    "3. Service Costing: 7-Step Equivalent Room-Days Method (Standard / Deluxe / Luxurious Grades)",
                    "4. Peak Season vs. Off-Season Equivalent Full Room-Days & Room Rent Formula (incl. % of Total Takings rent)"
                ),
                whatCameInPastPapers = listOf(
                    "Holiday Home / Hotel numerical with 3 suite types (Single, Double, Triple), seasonal vs. off-season occupancy, and building rent expressed as fixed ₹10,000/month + 5% of total takings (8–10 Marks) [Illustration 6].",
                    "Joint Product apportionment + Incremental decision on whether to sell at split-off or process further (5–8 Marks)."
                ),
                howStudentsLoseMarks = listOf(
                    "Allocating joint costs to decide whether to process further (joint costs are sunk at split-off).",
                    "Forgetting to convert both room count × days × occupancy % AND price ratio when computing Total Equivalent Room-Days.",
                    "Failing to express '5% of total takings' algebraically in terms of Total Takings `X` when computing target room rent."
                )
            )
        ),

        PaperCode.PAPER_5 to listOf(
            OfflineTeacherRadarNote(
                topicId = "audit_sa500_sa510",
                paper = PaperCode.PAPER_5,
                chapterTitle = "Audit Evidence (SA 500) & Initial Audit Engagements — Opening Balances (SA 510)",
                examProbability = "88% Probability • 4 to 8 Marks",
                subTopicsInChapter = listOf(
                    "1. Sources of Audit Evidence: Accounting Records (Primary) vs. Other Sources (Management's Expert, Third Parties, Prior Audits)",
                    "2. Evaluating Management's Expert — Mnemonic `CRAN2ES`",
                    "3. SA 510 Opening Balances: APD Rule (Accounted + Presented + Disclosed)",
                    "4. Audit Opinion Matrix on Opening Balances (Qualified vs. Disclaimer vs. Adverse)"
                ),
                whatCameInPastPapers = listOf(
                    "Case study where a newly appointed auditor finds either (a) inability to verify opening inventory or (b) unadjusted misstatement in opening balances affecting current period (4 Marks).",
                    "Factors affecting the nature, timing, and extent of audit procedures when relying on a Management's Expert under SA 500 (3–4 Marks)."
                ),
                howStudentsLoseMarks = listOf(
                    "Violating the APD Rule—omitting one of the three mandatory words: 'Accounted, Presented, and Disclosed'.",
                    "Mixing up Disclaimer of Opinion (used when unable to obtain SAAE) with Adverse Opinion (used when misstatement is material and pervasive)."
                )
            )
        ),

        PaperCode.PAPER_6 to listOf(
            OfflineTeacherRadarNote(
                topicId = "fm_sm_master_radar",
                paper = PaperCode.PAPER_6,
                chapterTitle = "FM (Capital Budgeting, Leverage, Cost of Capital, Receivables) & SM (Growth Strategies)",
                examProbability = "95% Probability • 25 to 35 Marks across Paper 6",
                subTopicsInChapter = listOf(
                    "1. Capital Budgeting Special Cases: Capital Rationing (Divisible PI vs. Non-Divisible Combinations) & Unequal Lives (Replacement Chain vs. EAC)",
                    "2. Receivables Management: Incremental Approach Format (6 Steps) & Nominal vs. Effective Compound Cost of Not Taking Discount",
                    "3. Leverage & Capital Structure: DOL, DFL (with Preference Dividend gross-up), Reverse Leverage Problems, and NOI Approach",
                    "4. Cost of Debt & Bonds: YTM / IRR Trial-and-Error when RV and NP differ significantly vs. Annual Principal Amortisation",
                    "5. Strategic Management: Internal Growth (Intensification/Diversification) vs. External Growth (M&A & Strategic Alliances)"
                ),
                whatCameInPastPapers = listOf(
                    "Incremental Credit Policy evaluation (Mosaic Ltd. / MNO Ltd. variant) where Variable Cost is 80% of Cost of Sales, bad debts rise from 1% to 4%, and credit period extends from 30 to 60 days (8 Marks) [Illustration 16].",
                    "Reverse Leverage calculation (Company A & B) finding EBIT, Contribution, and Sales from given DOL, DFL, Interest, and VC% (6 Marks) [Illustration 5].",
                    "Capital Rationing under Divisible vs. Non-Divisible assumption (5–6 Marks)."
                ),
                howStudentsLoseMarks = listOf(
                    "Using the Kd approximation formula when Redemption Value (RV) and Net Proceeds (NP) differ hugely (e.g., Deep Discount Bonds where RV = ₹1,00,000 and NP = ₹2,500—must use IRR Trial & Error!).",
                    "Including Fixed Costs in Incremental Variable Cost when Fixed Costs do not change under a proposed credit policy.",
                    "Using Sales/EBIT instead of Contribution/EBIT for DOL, or forgetting `DOL × MOS = 1`."
                )
            )
        )
    )

    // =========================================================================
    // C. LITERAL CHAPTER NOTES (ALL 6 PAPERS — DIRECT CHAPTER SUBSTANCE)
    // =========================================================================
    val literalChapterNotesByPaper: Map<PaperCode, List<LiteralChapterNoteSection>> = mapOf(
        PaperCode.PAPER_1 to listOf(
            LiteralChapterNoteSection(
                topicId = "adv_acc_as13",
                sectionHeading = "1. AS 13 (Revised): Classification, Valuation & Reclassification",
                coreRuleOrProvision = "• Current Investment: Held for ≤ 1 year from date of investment → Valued at LOWER of Cost and Fair Value (individual or category-wise, never global).\n" +
                    "• Long-Term Investment: Held for > 1 year → Carried at COST, reduced only for non-temporary decline (assessed individually).",
                formulaOrAccountingLogic = "RECLASSIFICATION FORMULA LOGIC (Para 24):\n" +
                    "• Long-Term → Current: Transfer at LOWER of Cost and Carrying Amount at date of transfer.\n" +
                    "• Current → Long-Term: Transfer at LOWER of Cost and Fair Value at date of transfer.\n" +
                    "• Disposal Cost: (Total Capital Column Cost ÷ Total Nominal Units Held) × Units Sold.",
                commonStudentConfusion = "Remember 'Cost' is the anchor in both reclassification formulas: pair Cost with Carrying Amount when leaving Long-Term, and pair Cost with Fair Value when leaving Current."
            ),
            LiteralChapterNoteSection(
                topicId = "adv_acc_as20",
                sectionHeading = "2. AS 20: Basic & Diluted EPS, Rights Issue TERP & Sequencing",
                coreRuleOrProvision = "• Cumulative Preference Dividend: Deduct for the FULL period whether or not declared (exclude prior arrears).\n" +
                    "• Bonus Issue / Share Split: Restate BOTH current year and prior comparative period EPS as if issued at the start of the earliest period presented.\n" +
                    "• Diluted EPS Sequencing: Rank multiple potential equity instruments by 'Earnings per Incremental Share' from lowest (most dilutive) to highest; test against Profit from Continuing Ordinary Activities.",
                formulaOrAccountingLogic = "RIGHTS ISSUE TERP & ADJUSTMENT FACTOR:\n" +
                    "• TERP = [(Pre-Rights Shares × Fair Value Before) + (Rights Shares × Rights Issue Price)] ÷ Total Post-Rights Shares\n" +
                    "• Adjustment Factor = Fair Value Immediately Prior to Exercise ÷ TERP\n" +
                    "• Restated Prior EPS = Original Prior EPS ÷ Adjustment Factor\n" +
                    "• Current WANES = (Pre-Rights Shares × Adj. Factor × Pre-Fraction) + (Total Post-Rights Shares × Post-Fraction)",
                commonStudentConfusion = "Apply the Rights Adjustment Factor ONLY to the pre-rights period shares; post-rights shares already include the bonus element and are time-weighted at 1.0x."
            ),
            LiteralChapterNoteSection(
                topicId = "adv_acc_buyback_cfs",
                sectionHeading = "3. Buyback Simultaneous Equation & AS 21 Consolidation Steps",
                coreRuleOrProvision = "• Buyback Simultaneous Equation: When Free Reserves are insufficient to cover both maximum buyback ('y') and CRR transfer ('x') while maintaining the 2:1 post-buyback debt-equity ratio:\n" +
                    "  Equation (1): (Existing Shareholders' Funds − x) − Minimum Post-Buyback Own Funds (50% of Debt) = y\n" +
                    "  Equation (2): 2x = y (where nominal value bought back = x at 50% of buyout price).\n" +
                    "• AS 21 Consolidation: 9-Step Worksheet (Shareholding % → DOA → DOC → Pre vs Post Reserves → Net Assets on DOA → Goodwill/Capital Reserve → Minority Interest on DOC → Group Reserves → Consolidated B/S).",
                formulaOrAccountingLogic = "FRAMEWORK CAPITAL MAINTENANCE & EQUATION:\n" +
                    "• Assets (A) − Liabilities (L) = Equity (E)\n" +
                    "• Profit (P) = (Closing Equity) − (Opening Equity) − Capital Introduced + Drawings\n" +
                    "• Financial Capital (CPP): Opening Equity restated by (Closing Price Index ÷ Opening Price Index)\n" +
                    "• Physical Capital Maintenance: Opening Units × Current Replacement Cost per unit",
                commonStudentConfusion = "Under AS 5 & AS 10 (Revised), changing depreciation from SLM to WDV or introducing a formal gratuity scheme in place of ad-hoc ex-gratia payments is a Change in Accounting Estimate / New Policy for different substance—NOT a change in accounting policy!"
            )
        ),

        PaperCode.PAPER_2 to listOf(
            LiteralChapterNoteSection(
                topicId = "law_ios_deeds",
                sectionHeading = "1. Primary Rules & Internal Aids to Interpretation of Statutes",
                coreRuleOrProvision = "• Literal Rule: Plain words need no explanation (absoluta sententia expositore non indiget).\n" +
                    "• Golden Rule: Modify grammatical sense only to avoid absurdity/repugnancy (ut res magis valeat quam pereat).\n" +
                    "• Mischief Rule (Heydon's Case): Apply 4 questions (1. What was the old law? 2. What was the mischief/defect? 3. What remedy did Parliament adopt? 4. True reason of the remedy) ONLY when words are ambiguous.\n" +
                    "• Ejusdem Generis: General words following a distinct genus/category of specific words take their meaning from that same class/species.",
                formulaOrAccountingLogic = "4 ELEMENTS OF A DOCUMENT & CLAUSE HIERARCHY:\n" +
                    "• 4 Elements: 1. Matter (information) → 2. Record (writing/device) → 3. Substance (paper/stone/digital) → 4. Means (letters/figures/marks).\n" +
                    "• Statutory Hierarchy: 'Notwithstanding' (Overrides) > Main Enactment > 'Subject to' (Subservient).\n" +
                    "• Deed Conflict Rule: Harmonise all clauses first; if impossible, EARLIER clause overrides latter clause.",
                commonStudentConfusion = "Marginal Notes appended to sections of a statute are NOT part of the enactment and cannot be used to construe plain sections (Exception: Marginal notes to Articles of the Constitution of India passed by the Constituent Assembly)."
            )
        ),

        PaperCode.PAPER_3 to listOf(
            LiteralChapterNoteSection(
                topicId = "tax_gst_payment_tds_tcs",
                sectionHeading = "1. ITC Utilization Order (Sec 49, 49A, 49B & Rule 88A)",
                coreRuleOrProvision = "• Step 1 (IGST Credit): Must FIRST be utilized to pay IGST liability. Remaining IGST credit can be utilized to pay CGST and/or SGST/UTGST in ANY order and in ANY proportion.\n" +
                    "• Step 2 (CGST Credit): Can be utilized ONLY after IGST credit is 100% exhausted. Used first for CGST, then for IGST. (NEVER for SGST).\n" +
                    "• Step 3 (SGST Credit): Can be utilized ONLY after IGST credit is 100% exhausted. Used first for SGST, then for IGST. (NEVER for CGST).",
                formulaOrAccountingLogic = "STATUTORY ORDER OF DISCHARGE (Sec 49(8)):\n" +
                    "1. Self-assessed tax & dues of PREVIOUS tax periods\n" +
                    "2. Self-assessed tax & dues of CURRENT tax period\n" +
                    "3. Any other amount payable including demand u/s 73 / 74 (up to FY 23-24) or Sec 74A (FY 24-25 onward).",
                commonStudentConfusion = "Rule 88A gives you freedom to split remaining IGST credit between CGST and SGST in whatever ratio minimizes your final cash outlay—always allocate remaining IGST credit to the head where native credit (CGST or SGST) is lower."
            ),
            LiteralChapterNoteSection(
                topicId = "tax_gst_payment_tds_tcs",
                sectionHeading = "2. Interest on Delayed Payment & Wrong ITC (Sec 50 & Rule 88B)",
                coreRuleOrProvision = "• Delayed Return Filing (Sec 50(1)): Interest @ 18% p.a. is levied ONLY on the net tax paid through the Electronic Cash Ledger (unless proceedings u/s 73/74/74A have commenced).\n" +
                    "• Wrongly Availed AND Utilized ITC (Sec 50(3)): Interest @ 18% p.a. from date of utilization till date of reversal/payment. No interest if ITC was wrongly availed in Electronic Credit Ledger but NEVER utilized.",
                formulaOrAccountingLogic = "WRONG UTILIZATION FORMULA (Rule 88B(3)):\n" +
                    "Taxable Wrong Utilization = Max(0, Wrongly Availed ITC − Closing Balance in Credit Ledger)",
                commonStudentConfusion = "Merely taking wrong ITC in the Electronic Credit Ledger does NOT trigger Section 50(3) interest; interest triggers only when the balance in the Credit Ledger falls below the wrongly availed amount."
            )
        ),

        PaperCode.PAPER_4 to listOf(
            LiteralChapterNoteSection(
                topicId = "cma_joint_service_costing",
                sectionHeading = "1. Further Processing Incremental Framework & Equivalent Room-Days",
                coreRuleOrProvision = "• Further Processing Framework (Joint Products):\n" +
                    "  Step 1: Incremental Revenue = Final Sale Value after processing − Sale Value at Split-Off.\n" +
                    "  Step 2: Incremental Cost = Further processing costs incurred AFTER split-off.\n" +
                    "  Step 3: Incremental Profit/(Loss) = Step 1 − Step 2 (Process further if > 0; sell at split-off if ≤ 0).\n" +
                    "• Service Costing (7-Step Equivalent Room-Days):\n" +
                    "  1. Identify grades → 2. Set price ratios (e.g., 1 : 2.5 : 5) → 3. Room-Days per grade (Rooms × Days × Occupancy %) → 4. Multiply by price ratio → 5. Sum Total Equivalent Units → 6. Cost per Equivalent Unit → 7. Multiply by grade ratio.",
                formulaOrAccountingLogic = "SEASONAL VS OFF-SEASON HOTEL TARIFF FORMULA:\n" +
                    "• Equivalent Full Room-Days = (Peak Season Room-Days × 100%) + (Off-Season Room-Days × 50%)\n" +
                    "• Peak Season Rent per Room-Day = Total Target Revenue ÷ Equivalent Full Room-Days\n" +
                    "• Off-Season Rent per Room-Day = Peak Season Rent × 50%",
                commonStudentConfusion = "Whether you convert all room grades into Base Standard Units (1x, 2.5x, 5x) or into Luxurious Units (1/5x, 1/2x, 1x), the final room rent for each category comes out 100% identical!"
            )
        ),

        PaperCode.PAPER_5 to listOf(
            LiteralChapterNoteSection(
                topicId = "audit_sa500_sa510",
                sectionHeading = "1. SA 500 Audit Evidence & SA 510 Opening Balances (APD Rule)",
                coreRuleOrProvision = "• SA 500 Sources of Evidence: Accounting Records are the primary source (tested via analysis, review, re-performance, and reconciliation); corroborated by other sources (management's expert, third parties, analytical procedures). Assurance increases when consistent evidence is obtained from different sources of different nature.\n" +
                    "• SA 510 APD Rule: Any material misstatement in opening balances or change in accounting policy must be properly ACCOUNTED + PRESENTED + DISCLOSED.",
                formulaOrAccountingLogic = "SA 510 OPINION DECISION MATRIX:\n" +
                    "1. Unable to obtain SAAE on Opening Balances → QUALIFIED or DISCLAIMER of Opinion.\n" +
                    "2. Material Misstatement in Opening Balances not Accounted, Presented & Disclosed → QUALIFIED or ADVERSE Opinion.\n" +
                    "3. Accounting Policies not consistently applied / change not Accounted, Presented & Disclosed → QUALIFIED or ADVERSE Opinion.",
                commonStudentConfusion = "Remember the Mnemonic 'CRAN2ES' for factors affecting audit procedures when using a Management's Expert under SA 500, and never issue an Adverse Opinion for a scope limitation (inability to obtain SAAE)."
            )
        ),

        PaperCode.PAPER_6 to listOf(
            LiteralChapterNoteSection(
                topicId = "fm_capital_receivables",
                sectionHeading = "1. Receivables Incremental Format & Cost of Foregoing Discount",
                coreRuleOrProvision = "• Incremental Credit Policy Approach (6 Steps):\n" +
                    "  (A) Incremental Expected Profit = Incremental Sales − Incremental Variable Cost − Incremental Fixed Cost (if any) − Incremental Bad Debts − Incremental Cash Discount (less Tax).\n" +
                    "  (B) Required Return on Incremental Investment = [(New Cost of Credit Sales × New Collection Period / 360) − (Old Cost of Credit Sales × Old Collection Period / 360)] × Required RoR %.\n" +
                    "  Decision: Accept policy with highest Net Incremental Benefit (A − B).",
                formulaOrAccountingLogic = "COST OF NOT TAKING SUPPLIER CASH DISCOUNT:\n" +
                    "• Nominal Annual Cost = [d ÷ (100 − d)] × [365 ÷ t]\n" +
                    "• Effective (Compound) Annual Cost = [(100 ÷ (100 − d)) ^ (365 / t)] − 1\n" +
                    "  (where d = discount %, and t = Total Credit Period − Discount Period).",
                commonStudentConfusion = "Why is the Effective Compound Cost higher than the Nominal Cost? Because the nominal formula ignores the compounding effect of reinvesting the saved discount across each credit cycle 't'."
            ),
            LiteralChapterNoteSection(
                topicId = "fm_leverage_cost_of_capital",
                sectionHeading = "2. Leverage (DOL / DFL), NOI Approach & Deep Discount Bond YTM",
                coreRuleOrProvision = "• NOI Approach (Capital Structure): Overall Cost of Capital (Ko) and Total Firm Value (V = EBIT ÷ Ko) remain CONSTANT regardless of debt-equity mix; cheaper debt is exactly offset by rising Cost of Equity: Ke = Ko + (Ko − Kd)(D / S).\n" +
                    "• Deep Discount / Large RV-NP Gap Bonds: Do NOT use the Kd approximation formula when Redemption Value (RV) and Net Proceeds (NP) differ significantly (or in gradual annual principal amortisation)—always use YTM / IRR interpolation: FV = PV × (1 + r)^n.",
                formulaOrAccountingLogic = "LEVERAGE & BREAK-EVEN MASTER FORMULAS:\n" +
                    "• DOL = Contribution ÷ EBIT = 1 ÷ Margin of Safety (MOS)\n" +
                    "• % Change in EBIT = DOL × % Change in Sales\n" +
                    "• DFL = EBIT ÷ [EBT − (Dp ÷ (1 − t))]\n" +
                    "• Financial Break-Even Point (FBEP) = Interest + [Dp ÷ (1 − t)]",
                commonStudentConfusion = "Always check that DOL × MOS = 1 for quick cross-verification, and remember that DFL has EBIT in the numerator (the larger figure when profitable) and EBT less grossed-up preference dividend in the denominator."
            )
        )
    )
}
