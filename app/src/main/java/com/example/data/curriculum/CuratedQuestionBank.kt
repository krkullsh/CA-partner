package com.example.data.curriculum

import android.content.Context
import com.example.data.model.PaperCode
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.regex.Pattern

data class PracticeQuestion(
    val id: String,
    val paper: PaperCode,
    val chapterName: String,
    val questionNumber: String,
    val questionText: String,
    val marks: Int,
    val minutesAllowed: Int,
    val tags: List<String>,
    val officialAnswer: String,
    val approachTips: String,
    val examinerObservations: String
)

object CuratedQuestionBank {

    val allQuestions: List<PracticeQuestion> = listOf(
        // ==========================================
        // PAPER 1: ADVANCED ACCOUNTING
        // ==========================================
        PracticeQuestion(
            id = "p1_q1_fused_as15_as10",
            paper = PaperCode.PAPER_1,
            chapterName = "Compulsory Q1 • Fused AS 15 + AS 10",
            questionNumber = "Q1 (Compulsory)",
            questionText = "Beta Ltd. is constructing a major manufacturing facility (a qualifying asset under AS 16). During the year ended 31st March 2026, the company incurred Rs. 45 Lakhs on direct site labour. In addition:\n" +
                "(i) Actuarial valuation of gratuity for the on-site dedicated construction workforce revealed a net liability increase of Rs. 6.5 Lakhs (comprising Current Service Cost Rs. 5 Lakhs, Net Interest Rs. 1.5 Lakhs, and Actuarial Gain of Rs. 0.8 Lakhs on plan assets).\n" +
                "(ii) The company's general office staff gratuity expense of Rs. 12 Lakhs was also capitalised by the accountant into PPE.\n" +
                "(iii) Testing of equipment produced trial run output sold for Rs. 2 Lakhs.\n\n" +
                "Advise Beta Ltd. on the correct accounting treatment under AS 10 (Property, Plant and Equipment) and AS 15 (Employee Benefits).",
            marks = 10,
            minutesAllowed = 14,
            tags = listOf("10 Marks", "14 Minutes", "ICAI FAV", "High Chance", "Compulsory Q1", "Fused: AS 15 + AS 10"),
            officialAnswer = "**1. Applicable Accounting Standards:**\n" +
                "• **AS 10 (Revised) - Property, Plant and Equipment:** Directly attributable costs of bringing the asset to the location and condition necessary for it to be capable of operating in the manner intended by management must be included in the cost of PPE. Costs of employee benefits (as defined in AS 15) arising directly from the construction or acquisition of the item of PPE are directly attributable costs.\n" +
                "• **AS 15 (Revised) - Employee Benefits:** Employee benefit costs directly attributable to construction can be capitalized. However, administrative overheads and general corporate employee benefits cannot be capitalized.\n\n" +
                "**2. Analysis of Items:**\n" +
                "• **On-site dedicated workforce gratuity (Rs. 5.7 Lakhs net):** Current service cost Rs. 5L + Net Interest Rs. 1.5L - Actuarial Gain Rs. 0.8L = Rs. 5.7 Lakhs is directly attributable to the construction of the facility. It **must be capitalized** into Capital Work-in-Progress (CWIP) under AS 10.\n" +
                "• **General office staff gratuity (Rs. 12 Lakhs):** General administrative employee costs are not directly attributable to qualifying asset construction. The accountant's treatment is **incorrect**. This Rs. 12 Lakhs must be charged to the Profit and Loss Account.\n" +
                "• **Trial run proceeds (Rs. 2 Lakhs):** Under revised AS 10, income from producing items while bringing PPE to site (trial run) is credited to Statement of P&L and not deducted from the cost of PPE.\n\n" +
                "**3. Conclusion:** Capitalize Rs. 45 Lakhs labour + Rs. 5.7 Lakhs site gratuity = Rs. 50.7 Lakhs to CWIP. Charge Rs. 12 Lakhs gratuity and credit Rs. 2 Lakhs trial run to P&L.",
            approachTips = "1. Quote relevant clauses of AS 10 & AS 15 in Opening Paragraph.\n2. Dissect each transaction separately with a clear Capitalize vs P&L verdict.\n3. Remember revised AS 10 amendment: Trial run proceeds go to P&L, not deducted from asset cost!",
            examinerObservations = "Common Student Mistake: Students deduct trial run sale proceeds from PPE cost (following the obsolete pre-revised AS 10 rule). Always show the updated MCA/ICAI standard!"
        ),
        PracticeQuestion(
            id = "p1_q2_schedule_iii",
            paper = PaperCode.PAPER_1,
            chapterName = "Preparation of Financial Statements (Schedule III)",
            questionNumber = "Q2 (Compulsory Anchor)",
            questionText = "State the disclosure requirements under Division I of Schedule III to the Companies Act, 2013 for the following balance sheet items:\n" +
                "(a) Capital Work-in-Progress (CWIP) Ageing Schedule.\n" +
                "(b) Current maturities of Long-Term Borrowings.\n" +
                "(c) Share application money pending allotment.\n" +
                "(d) Trade Payables Ageing Schedule (MSME vs Others).",
            marks = 14,
            minutesAllowed = 22,
            tags = listOf("14 Marks", "22 Minutes", "ICAI FAV", "High Chance", "Schedule III Division I", "Compulsory Anchor"),
            officialAnswer = "**(a) CWIP Ageing Schedule:** Must be presented in the tabular format:\n" +
                "1. Projects in progress vs Projects temporarily suspended.\n" +
                "2. Age buckets: Less than 1 year, 1-2 years, 2-3 years, and More than 3 years.\n" +
                "3. For CWIP whose completion is overdue or has exceeded cost compared to original plan, CWIP completion schedule must be given.\n\n" +
                "**(b) Current Maturities of Long-Term Borrowings:** Disclosed under 'Current Liabilities' under the sub-head 'Other Financial Liabilities' (or Short-Term Borrowings/Other Current Liabilities as per ICAI Guidance Note).\n\n" +
                "**(c) Share Application Money Pending Allotment:** Shown as a separate line item between 'Shareholders' Funds' and 'Non-Current Liabilities'. If terms require refund or minimum subscription not met, disclosed under Other Current Liabilities.\n\n" +
                "**(d) Trade Payables Ageing Schedule:** Must separate (i) MSME, (ii) Others, (iii) Disputed dues - MSME, (iv) Disputed dues - Others across aging buckets (< 1 yr, 1-2 yrs, 2-3 yrs, > 3 yrs).",
            approachTips = "Draw the exact 4-bucket ageing table format (<1 yr, 1-2 yrs, 2-3 yrs, >3 yrs) for both CWIP and Trade Payables.",
            examinerObservations = "Students lose marks for omitting the 'Disputed dues' columns in the trade payables schedule and forgetting CWIP overrun schedules."
        ),
        PracticeQuestion(
            id = "p1_q3_amalgamation_as14",
            paper = PaperCode.PAPER_1,
            chapterName = "Amalgamation of Companies (AS 14)",
            questionNumber = "Q3",
            questionText = "Explain the treatment of the following items under the Pooling of Interests Method vs Purchase Method of Accounting for Amalgamation under AS 14:\n" +
                "(i) Difference between Purchase Consideration and Net Identifiable Assets.\n" +
                "(ii) Statutory Reserves (e.g., Export Profit Reserve, Amalgamation Adjustment Account).\n" +
                "(iii) Expenses of Amalgamation incurred by the Transferee Company.",
            marks = 10,
            minutesAllowed = 15,
            tags = listOf("10 Marks", "15 Minutes", "ICAI FAV", "High Chance", "AS 14", "Corporate Restructuring"),
            officialAnswer = "**(i) Difference between PC and Net Assets:**\n" +
                "• *Pooling of Interests:* Transferred to General Reserve / Capital Reserve.\n" +
                "• *Purchase Method:* Excess of PC over Net Assets is debited to **Goodwill Account** (amortized over max 5 years); excess of Net Assets over PC is credited to **Capital Reserve**.\n\n" +
                "**(ii) Statutory Reserves:**\n" +
                "• *Pooling of Interests:* All reserves of the transferor company are recorded in the transferee books in the same form.\n" +
                "• *Purchase Method:* Only statutory reserves required to be preserved under tax statutes (e.g. Sec 80HHC) are recorded by debiting **Amalgamation Adjustment Deficit/Account** and crediting Statutory Reserve.\n\n" +
                "**(iii) Liquidation/Amalgamation Expenses:** Under Purchase Method, if borne by Transferee, debited to Goodwill / Capital Reserve.",
            approachTips = "Present as a side-by-side comparative table. Use clear ledger accounting terminology.",
            examinerObservations = "Many students mistakenly record general reserves in Purchase Method. Only statutory reserves are carried forward via Amalgamation Adjustment Account!"
        ),

        // ==========================================
        // PAPER 2: CORPORATE AND OTHER LAWS
        // ==========================================
        PracticeQuestion(
            id = "p2_q1_fused_law",
            paper = PaperCode.PAPER_2,
            chapterName = "Compulsory Q1 • Fused Law (Sec 186 + LLP + FEMA)",
            questionNumber = "Q1 (Compulsory)",
            questionText = "Apex Ltd. has Paid-Up Capital of Rs. 40 Crores and Free Reserves of Rs. 20 Crores. The Board of Directors proposes:\n" +
                "(a) To lend Rs. 42 Crores to Sigma LLP (in which a director of Apex Ltd. is a designated partner).\n" +
                "(b) To remit USD 180,000 to a US university towards education fee for the daughter of the Managing Director.\n\n" +
                "Examine with reference to Section 186 & Section 185 of the Companies Act 2013, LLP Act 2008, and FEMA 1999:\n" +
                "(i) Whether the loan to Sigma LLP requires a Special Resolution?\n" +
                "(ii) Whether the foreign exchange remittance requires prior approval of the Reserve Bank of India?",
            marks = 10,
            minutesAllowed = 14,
            tags = listOf("10 Marks", "14 Minutes", "ICAI FAV", "High Chance", "Compulsory Q1", "Sec 186 & 185", "FEMA LRS"),
            officialAnswer = "**(i) Loan to Sigma LLP under Section 186 & 185:**\n" +
                "• **Threshold Limit under Sec 186(2):** A company shall not give loans exceeding higher of: (a) 60% of (Paid-up Capital + Free Reserves + Securities Premium) = 60% of (40 + 20) = Rs. 36 Crores; OR (b) 100% of Free Reserves = Rs. 20 Crores. Thus, ceiling without Special Resolution is Rs. 36 Crores.\n" +
                "• The proposed loan is Rs. 42 Crores, which exceeds Rs. 36 Crores. Hence, **prior approval by Special Resolution in general meeting is mandatory**.\n" +
                "• **Section 185 Check:** Under Section 185(2), loan to an entity in which director is interested (Sigma LLP) can be granted provided a Special Resolution is passed and the borrowing LLP utilizes the loan for its principal business activities.\n\n" +
                "**(ii) Foreign Remittance under FEMA 1999 (Current Account Transaction):**\n" +
                "• Remittance for studies abroad is a Current Account Transaction governed by Schedule III of FEM (Current Account Transactions) Rules, 2000 under the **Liberalised Remittance Scheme (LRS)**.\n" +
                "• Under LRS, individuals are permitted to remit up to **USD 250,000 per financial year** for permitted current account purposes without any prior RBI approval.\n" +
                "• Since USD 180,000 is within the USD 250,000 limit, **no prior RBI approval is required**. Remittance can be made through an Authorized Dealer (AD) Category-I bank.",
            approachTips = "1. Always quote Section numbers first: Sec 186(2) and Schedule III of FEMA.\n2. Calculate the 60% / 100% threshold explicitly in rupees.\n3. State the exact USD 250,000 LRS limit clearly.",
            examinerObservations = "Students frequently forget to check Sec 185 along with Sec 186, and confuse LRS USD 250,000 limit with the old $100,000 limit."
        ),
        PracticeQuestion(
            id = "p2_q2_fema_current_vs_capital",
            paper = PaperCode.PAPER_2,
            chapterName = "Foreign Exchange Management Act, 1999",
            questionNumber = "Q2",
            questionText = "State with reasons whether the following transactions are 'Current Account' or 'Capital Account' transactions under FEMA, 1999:\n" +
                "(a) Remittance of dividend by an Indian company to its non-resident shareholder.\n" +
                "(b) Acquisition of immovable property in London by an Indian resident company for setting up a liaison office.\n" +
                "(c) Payment of charter hire charges for a vessel chartered for oil exploration.\n" +
                "(d) Raising of External Commercial Borrowings (ECB) of USD 5 Million by an Indian textile mill.",
            marks = 8,
            minutesAllowed = 12,
            tags = listOf("8 Marks", "12 Minutes", "ICAI FAV", "High Chance", "FEMA 1999", "Current vs Capital"),
            officialAnswer = "**Legal Provision:** Section 2(e) defines 'Capital Account Transaction' as a transaction which alters the assets or liabilities, including contingent liabilities, outside India of persons resident in India or assets/liabilities in India of persons resident outside India.\n" +
                "Section 2(j) defines 'Current Account Transaction' as a transaction other than a capital account transaction, including payments in connection with foreign trade, interest on loans, and net income from investments.\n\n" +
                "**(a) Remittance of Dividend:** Current Account Transaction [Sec 2(j)(i) - Net income from investments].\n" +
                "**(b) Immovable Property in London:** Capital Account Transaction [Sec 2(e) - Alters assets outside India of a person resident in India].\n" +
                "**(c) Charter Hire Charges:** Current Account Transaction [Sec 2(j)(i) - Payment due in connection with foreign trade and business].\n" +
                "**(d) External Commercial Borrowings (ECB):** Capital Account Transaction [Sec 2(e) - Alters liabilities outside India of Indian resident].",
            approachTips = "Classify using a crisp 2-column format: Transaction | Classification | Statutory Reason.",
            examinerObservations = "Students confuse interest/dividend remittance with the underlying loan/shares. The capital is Capital Account, but the yield (dividend/interest) is strictly Current Account."
        ),

        // ==========================================
        // PAPER 3: TAXATION (Direct Tax & GST)
        // ==========================================
        PracticeQuestion(
            id = "p3_q1_total_income_pgbp",
            paper = PaperCode.PAPER_3,
            chapterName = "Computation of Total Income & Tax Payable (PGBP)",
            questionNumber = "Q1 (Compulsory)",
            questionText = "Mr. Rajesh (aged 48, resident) furnishes the following details for P.Y. 2025-26:\n" +
                "1. Net Profit as per P&L Account: Rs. 18,50,000.\n" +
                "2. Debited to P&L: Rs. 1,80,000 paid to an MSME supplier on 28th May 2026 for goods supplied on 15th January 2026 (agreed credit period 15 days).\n" +
                "3. Depreciation debited to P&L: Rs. 2,40,000 (Allowable depreciation under Section 32 is Rs. 3,10,000).\n" +
                "4. Cash payment to a transporter for freight: Rs. 32,000 on a single day.\n" +
                "Compute his Business Income and advise whether default regime u/s 115BAC is beneficial.",
            marks = 14,
            minutesAllowed = 22,
            tags = listOf("14 Marks", "22 Minutes", "ICAI FAV", "High Chance", "Compulsory Q1", "Sec 115BAC", "PGBP Sec 43B(h)"),
            officialAnswer = "**1. Business Income Computation:**\n" +
                "• Net Profit as per P&L: Rs. 18,50,000\n" +
                "• **Add: Disallowance u/s 43B(h) (MSME Overdue):** Under Section 43B(h), any sum payable to a micro or small enterprise beyond the time specified in Sec 15 of MSMED Act (15 days / agreed 45 days) shall be allowed only in the previous year in which it is actually paid. Since payment was made on 28-05-2026 (next FY), Rs. 1,80,000 is disallowed in PY 2025-26: **+ Rs. 1,80,000**\n" +
                "• **Add: Transporter Cash Payment u/s 40A(3):** For plying, hiring or leasing goods carriages, cash payment limit is **Rs. 35,000 per day** (higher limit). Since Rs. 32,000 does not exceed Rs. 35,000, **no disallowance** is attracted.\n" +
                "• **Add: Depreciation debited:** + Rs. 2,40,000\n" +
                "• **Less: Allowable Depreciation u/s 32:** - Rs. 3,10,000\n" +
                "• **Total Business Income:** Rs. 19,60,000.\n\n" +
                "**2. Tax Regime 115BAC Evaluation:** Default new regime offers lower slab rates (up to Rs. 3L Nil, 3-7L 5%, 7-10L 10%, 10-12L 15%, 12-15L 20%, >15L 30%) with Sec 87A rebate if taxable income ≤ Rs. 7 Lakhs.",
            approachTips = "1. Highlight Section 43B(h) amendment explicitly.\n2. Note transporter cash limit is Rs. 35,000 (not Rs. 10,000).\n3. Keep Working Notes numbered sequentially.",
            examinerObservations = "Most students incorrectly disallow transporter cash payment thinking the limit is Rs. 10,000. It is Rs. 35,000 specifically for goods carriages!"
        ),
        PracticeQuestion(
            id = "p3_q2_gst_itc_blocked_credit",
            paper = PaperCode.PAPER_3,
            chapterName = "Goods and Services Tax (GST) • Input Tax Credit (ITC)",
            questionNumber = "Q2",
            questionText = "Vanguard Synthetics Ltd., a registered supplier in Maharashtra, received the following inward supplies during November 2025:\n" +
                "(a) 5 motor vehicles (seating capacity 5 persons each) purchased for marketing executives: GST paid Rs. 4,50,000.\n" +
                "(b) 1 truck (seating capacity 3 persons) purchased for transporting raw materials from warehouse to factory: GST paid Rs. 2,80,000.\n" +
                "(c) Group health insurance taken for factory workers pursuant to statutory requirement under the Factories Act, 1948: GST paid Rs. 75,000.\n" +
                "(d) Inputs costing Rs. 2,00,000 (GST Rs. 36,000) destroyed in an accidental fire in the shop floor.\n" +
                "(e) An invoice dated 10th October 2025 for Rs. 1,00,000 (GST Rs. 18,000) where the supplier has not paid the tax into the government treasury by 30th November (Rule 37A).\n\n" +
                "Determine the eligible Input Tax Credit (ITC) available to Vanguard Synthetics Ltd. for the tax period November 2025 with statutory reasons under Section 16 & Section 17(5) of the CGST Act, 2017.",
            marks = 10,
            minutesAllowed = 15,
            tags = listOf("10 Marks", "15 Minutes", "ICAI FAV", "High Chance", "GST ITC", "Sec 17(5) Blocked Credits", "Rule 37A"),
            officialAnswer = "**Legal Provisions & ITC Admissibility:**\n\n" +
                "**(a) Motor vehicles for executives (GST Rs. 4,50,000):**\n" +
                "• Under Section 17(5)(a), ITC on motor vehicles for transportation of persons having approved seating capacity of not more than 13 persons (including driver) is **blocked**, unless used for taxable supply of further supply of vehicles, passenger transportation, or driving training.\n" +
                "• Eligible ITC = **NIL** (Blocked Credit).\n\n" +
                "**(b) Truck for raw material transport (GST Rs. 2,80,000):**\n" +
                "• Motor vehicles used for transportation of goods are not covered by Section 17(5)(a). ITC is fully allowable under Section 16(1).\n" +
                "• Eligible ITC = **Rs. 2,80,000**.\n\n" +
                "**(c) Group Health Insurance under Factories Act (GST Rs. 75,000):**\n" +
                "• Under the proviso to Section 17(5)(b), ITC in respect of life insurance and health insurance is **allowable** where it is obligatory for an employer to provide the same to its employees under any law for the time being in force.\n" +
                "• Eligible ITC = **Rs. 75,000**.\n\n" +
                "**(d) Inputs destroyed in fire (GST Rs. 36,000):**\n" +
                "• Under Section 17(5)(h), ITC shall not be available in respect of goods lost, stolen, destroyed, written off or disposed of by way of gift or free samples.\n" +
                "• Eligible ITC = **NIL** (Blocked Credit).\n\n" +
                "**(e) Supplier tax unpaid (Rule 37A - GST Rs. 18,000):**\n" +
                "• Under Section 16(2)(c) read with Rule 37A, tax must actually be paid to the government. If the supplier does not pay the tax by 30th November following the end of financial year, the recipient must reverse ITC. Here for current period, provisional credit can be taken if reflected in GSTR-2B, but once confirmed unpaid under Rule 37A, reversal with interest applies. Eligible ITC = **NIL**.\n\n" +
                "**Total Eligible ITC = Rs. 2,80,000 + Rs. 75,000 = Rs. 3,55,000.**",
            approachTips = "1. Tabulate each transaction: Item | Eligible ITC (Rs.) | Ineligible ITC (Rs.) | Section Reference.\n2. Always emphasize the proviso to Sec 17(5)(b) where statutory employer obligation makes health insurance credit eligible!",
            examinerObservations = "Students often forget the proviso to Sec 17(5)(b) and wrongly block health insurance even when mandated by the Factories Act. Remember statutory employer mandate unlocks ITC!"
        ),

        // ==========================================
        // PAPER 4: COST AND MANAGEMENT ACCOUNTING
        // ==========================================
        PracticeQuestion(
            id = "p4_q1_standard_marginal",
            paper = PaperCode.PAPER_4,
            chapterName = "Standard Costing & Marginal Costing",
            questionNumber = "Q1 (Compulsory)",
            questionText = "XYZ Ltd. manufactures product 'Z'. Standard raw material per unit is 4 kg @ Rs. 25/kg. During March 2026, 2,000 units of 'Z' were produced using 8,400 kg of material purchased at a total cost of Rs. 2,01,600.\n" +
                "(a) Calculate Material Cost Variance, Material Price Variance, and Material Usage Variance.\n" +
                "(b) If selling price is Rs. 200/unit and contribution is 40%, calculate Break-Even Point in units and Margin of Safety if fixed costs are Rs. 1,20,000.",
            marks = 10,
            minutesAllowed = 14,
            tags = listOf("10 Marks", "14 Minutes", "ICAI FAV", "High Chance", "Compulsory Q1", "Standard Costing", "Marginal Costing"),
            officialAnswer = "**(a) Material Variances:**\n" +
                "• Standard Quantity (SQ) for actual output = 2,000 units × 4 kg = 8,000 kg.\n" +
                "• Standard Price (SP) = Rs. 25/kg.\n" +
                "• Actual Quantity (AQ) = 8,400 kg.\n" +
                "• Actual Price (AP) = Rs. 2,01,600 / 8,400 kg = Rs. 24/kg.\n\n" +
                "1. **Material Cost Variance (MCV)** = (SQ × SP) - (AQ × AP) = (8,000 × 25) - 2,01,600 = 2,00,000 - 2,01,600 = **Rs. 1,600 (Adverse)**\n" +
                "2. **Material Price Variance (MPV)** = AQ × (SP - AP) = 8,400 × (25 - 24) = **Rs. 8,400 (Favourable)**\n" +
                "3. **Material Usage Variance (MUV)** = SP × (SQ - AQ) = 25 × (8,000 - 8,400) = **Rs. 10,000 (Adverse)**\n" +
                "• *Reconciliation:* MCV = MPV + MUV -> 1,600 (A) = 8,400 (F) - 10,000 (A). Verified.\n\n" +
                "**(b) Marginal Costing Metrics:**\n" +
                "• Contribution per unit = 40% of Rs. 200 = Rs. 80/unit.\n" +
                "• **Break-Even Point (units)** = Fixed Cost / Contribution per unit = Rs. 1,20,000 / Rs. 80 = **1,500 units**.\n" +
                "• **Margin of Safety (units)** = Actual Sales - BEP Sales = 2,000 units - 1,500 units = **500 units** (Rs. 1,00,000).",
            approachTips = "Always present variance reconciliation (MCV = MPV + MUV) at the end to prove computational accuracy to the examiner.",
            examinerObservations = "Students often compute SQ based on budgeted output instead of actual output achieved. SQ is ALWAYS standard quantity for ACTUAL production."
        ),
        PracticeQuestion(
            id = "p4_q2_activity_based_costing",
            paper = PaperCode.PAPER_4,
            chapterName = "Activity-Based Costing (ABC) vs Absorption Costing",
            questionNumber = "Q2",
            questionText = "PQR Ltd. manufactures two products: Standard (10,000 units) and Deluxe (2,000 units). Under traditional absorption costing, overheads are absorbed at Rs. 40 per direct labour hour (DLH). Each Standard unit takes 2 DLH and Deluxe takes 4 DLH.\n" +
                "Total overheads of Rs. 11,20,000 are split into activity cost pools:\n" +
                "1. Machine Setup: Rs. 4,80,000 (Total setups: 120; Standard = 40, Deluxe = 80)\n" +
                "2. Material Quality Inspections: Rs. 3,60,000 (Total inspections: 90; Standard = 30, Deluxe = 60)\n" +
                "3. Power & Maintenance: Rs. 2,80,000 (Machine hours: 28,000; Standard = 20,000, Deluxe = 8,000)\n\n" +
                "(a) Calculate overhead cost per unit for both products under Traditional Absorption Costing.\n" +
                "(b) Calculate cost driver rates and overhead cost per unit under Activity-Based Costing (ABC).\n" +
                "(c) Explain why Deluxe product was undercosted under traditional costing.",
            marks = 10,
            minutesAllowed = 15,
            tags = listOf("10 Marks", "15 Minutes", "ICAI FAV", "High Chance", "Activity Based Costing", "Overhead Cost Distortion"),
            officialAnswer = "**(a) Traditional Absorption Costing:**\n" +
                "• Standard product (2 DLH @ Rs. 40/DLH) = **Rs. 80 per unit**.\n" +
                "• Deluxe product (4 DLH @ Rs. 40/DLH) = **Rs. 160 per unit**.\n\n" +
                "**(b) Activity-Based Costing (ABC):**\n" +
                "1. **Cost Driver Rates:**\n" +
                "   • Setup Cost Driver Rate = Rs. 4,80,000 / 120 setups = **Rs. 4,000 per setup**\n" +
                "   • Inspection Cost Driver Rate = Rs. 3,60,000 / 90 inspections = **Rs. 4,000 per inspection**\n" +
                "   • Power & Maint Rate = Rs. 2,80,000 / 28,000 hours = **Rs. 10 per machine hour**\n\n" +
                "2. **Overhead Allocation to Standard (10,000 units):**\n" +
                "   • Setups: 40 × Rs. 4,000 = Rs. 1,60,000\n" +
                "   • Inspections: 30 × Rs. 4,000 = Rs. 1,20,000\n" +
                "   • Power: 20,000 × Rs. 10 = Rs. 2,00,000\n" +
                "   • Total Standard Overheads = Rs. 4,80,000 -> **Overhead per unit = Rs. 48.00**\n\n" +
                "3. **Overhead Allocation to Deluxe (2,000 units):**\n" +
                "   • Setups: 80 × Rs. 4,000 = Rs. 3,20,000\n" +
                "   • Inspections: 60 × Rs. 4,000 = Rs. 2,40,000\n" +
                "   • Power: 8,000 × Rs. 10 = Rs. 80,000\n" +
                "   • Total Deluxe Overheads = Rs. 6,40,000 -> **Overhead per unit = Rs. 320.00**\n\n" +
                "**(c) Explanation of Cost Distortion:**\n" +
                "• Traditional costing used direct labour hours as a single volume proxy, assuming overhead consumption is proportional to volume. Under traditional costing, Standard was assigned Rs. 80 and Deluxe Rs. 160.\n" +
                "• ABC reveals Deluxe actually consumes 66.7% of setups and inspections despite only representing 16.7% of production volume. Deluxe was heavily undercosted (Rs. 160 vs Rs. 320), resulting in Standard product cross-subsidizing Deluxe.",
            approachTips = "1. Tabulate Cost Driver Rates first.\n2. Create a clean matrix: Activity | Cost Driver Rate | Standard Units/Cost | Deluxe Units/Cost.\n3. Mention the term 'Product Cross-Subsidization' explicitly in part (c).",
            examinerObservations = "Students frequently calculate total activity costs correctly but forget to divide by output units (10,000 and 2,000) to find per-unit overhead."
        ),

        // ==========================================
        // PAPER 5: AUDITING AND ETHICS
        // ==========================================
        PracticeQuestion(
            id = "p5_q1_caro_sa505",
            paper = PaperCode.PAPER_5,
            chapterName = "CARO 2020 & SA 505 External Confirmations",
            questionNumber = "Q1 (Compulsory)",
            questionText = "State the reporting duties of the statutory auditor under CARO, 2020 and Standards on Auditing in the following independent situations:\n" +
                "(a) The company has sanctioned working capital limits in excess of Rs. 5 Crores from a consortium of banks on the basis of security of current assets. The quarterly returns/statements filed by the company with banks differ materially from books of account.\n" +
                "(b) Management refuses to allow the auditor to send external confirmation requests under SA 505 to major trade receivables, claiming commercial confidentiality.",
            marks = 10,
            minutesAllowed = 14,
            tags = listOf("10 Marks", "14 Minutes", "ICAI FAV", "High Chance", "Compulsory Q1", "CARO 2020 Clause (ii)(b)", "SA 505"),
            officialAnswer = "**(a) Reporting under CARO 2020 - Clause 3(ii)(b):**\n" +
                "• Under Clause 3(ii)(b) of CARO 2020, where the company has been sanctioned working capital limits in excess of **Rs. 5 Crores** (at any point during the year) from banks or financial institutions on the basis of security of current assets, the auditor is required to report:\n" +
                "  1. Whether quarterly returns or statements filed with banks agree with books of account.\n" +
                "  2. If not, the auditor shall provide **full details of discrepancies**.\n" +
                "• Hence, the auditor must quantify and tabulate the material differences and report this non-reconciliation in the CARO report.\n\n" +
                "**(b) Management Refusal under SA 505 (External Confirmations):**\n" +
                "• As per SA 505, if management refuses to allow the auditor to send a confirmation request, the auditor shall:\n" +
                "  1. Inquire into management's reasons for refusal and seek audit evidence as to their validity and reasonableness.\n" +
                "  2. Evaluate the implications of management's refusal on the auditor's assessment of relevant risks of material misstatement, including fraud risk, and on the nature, timing, and extent of other audit procedures.\n" +
                "  3. Perform **alternative audit procedures** (e.g., verifying subsequent cash collections, shipping documents, sales orders).\n" +
                "• If the auditor concludes that refusal is unreasonable or is unable to obtain sufficient appropriate evidence through alternative procedures, the auditor shall communicate with TCWG and **modify the opinion in accordance with SA 705** (Qualified or Disclaimer of Opinion).",
            approachTips = "Structure answering SA 505 into 4 clear steps: Reason -> Assess Fraud Risk -> Alternative Procedures -> Reporting under SA 705.",
            examinerObservations = "Students fail to cite Clause 3(ii)(b) specifically and miss the requirement to perform alternative procedures before jumping straight to disclaimer of opinion."
        ),
        PracticeQuestion(
            id = "p5_q2_sa705_sa701_modifications",
            paper = PaperCode.PAPER_5,
            chapterName = "Audit Reports (SA 700, 705, 706 & SA 701 KAM)",
            questionNumber = "Q2",
            questionText = "State with reasons the type of audit opinion to be expressed by the auditor in the following cases:\n" +
                "(a) Inventory valuing Rs. 35 Crores (representing 40% of Total Assets) of an engineering company could not be physically verified due to access restrictions imposed by management, and no alternative procedures were feasible.\n" +
                "(b) The company has not provided for gratuity liability of Rs. 45 Lakhs in violation of AS 15. Total Profit before tax is Rs. 9.5 Crores, and the misstatement is material but confined to employee benefits.\n" +
                "(c) Can a matter communicated as a Key Audit Matter (KAM) under SA 701 serve as a substitute for modifying the opinion under SA 705?",
            marks = 10,
            minutesAllowed = 15,
            tags = listOf("10 Marks", "15 Minutes", "ICAI FAV", "High Chance", "SA 705 Modifications", "SA 701 KAM", "Audit Report"),
            officialAnswer = "**(a) Access Restriction on 40% of Total Assets (Inventory):**\n" +
                "• **Standard:** SA 705 (Modifications to the Opinion in the Independent Auditor's Report).\n" +
                "• **Analysis:** The auditor is unable to obtain sufficient appropriate audit evidence due to a management-imposed limitation on scope. Inventory constitutes 40% of total assets, which is both **material and pervasive** to the financial statements.\n" +
                "• **Conclusion:** The auditor shall issue a **Disclaimer of Opinion**.\n\n" +
                "**(b) Non-provision of Gratuity of Rs. 45 Lakhs under AS 15:**\n" +
                "• **Standard:** SA 705.\n" +
                "• **Analysis:** The financial statements are materially misstated due to non-compliance with AS 15. However, the misstatement is confined to employee benefit provisions and does not represent a substantial proportion of the financial statements; thus it is **material but NOT pervasive**.\n" +
                "• **Conclusion:** The auditor shall issue a **Qualified Opinion** ('except for the effects of the matter described in the Basis for Qualified Opinion section').\n\n" +
                "**(c) KAM under SA 701 as Substitute for Modification under SA 705:**\n" +
                "• **Direct Answer:** **NO.** SA 701 explicitly provides that communicating Key Audit Matters is **not a substitute** for:\n" +
                "  1. Disclosures in financial statements required by applicable financial reporting framework.\n" +
                "  2. The auditor expressing a modified opinion under SA 705.\n" +
                "  3. Reporting in accordance with SA 570 (Revised) when a material uncertainty relates to Going Concern.\n" +
                "• When a modified opinion is issued, the matter is reported in the 'Basis for Qualified/Adverse/Disclaimer of Opinion' section and excluded from the KAM section.",
            approachTips = "1. Build the decision matrix: Materiality vs Pervasiveness (Material + Pervasive = Adverse / Disclaimer; Material but Not Pervasive = Qualified).\n2. Highlight that KAM cannot substitute modified opinion under SA 705.",
            examinerObservations = "Students confuse 'Disclaimer of Opinion' with 'Adverse Opinion'. Adverse opinion is for misstatements that are material and pervasive; Disclaimer is for inability to obtain evidence (scope limitation) that is material and pervasive."
        ),

        // ==========================================
        // PAPER 6: FINANCIAL MANAGEMENT & STRATEGIC MANAGEMENT
        // ==========================================
        PracticeQuestion(
            id = "p6_q1_wacc_leverage",
            paper = PaperCode.PAPER_6,
            chapterName = "WACC & Leverages (Compulsory Q1 Fused)",
            questionNumber = "Q1 (Compulsory)",
            questionText = "Zenith Ltd. has an existing capital structure of:\n" +
                "• Equity Shares (Face value Rs. 10): Rs. 50 Lakhs\n" +
                "• 12% Debt: Rs. 30 Lakhs\n" +
                "Current EBIT is Rs. 16 Lakhs. Corporate Tax rate is 30%. Current market price per equity share is Rs. 40. Expected next year dividend is Rs. 4 per share with 5% constant growth.\n" +
                "(a) Calculate Cost of Equity (Ke) using Dividend Growth Model, and after-tax Cost of Debt (Kd).\n" +
                "(b) Calculate Degree of Operating Leverage (DOL) and Degree of Financial Leverage (DFL) if variable cost is 50% of Sales (Sales = Rs. 40 Lakhs).",
            marks = 10,
            minutesAllowed = 14,
            tags = listOf("10 Marks", "14 Minutes", "ICAI FAV", "High Chance", "Compulsory Q1", "Cost of Capital", "Leverages"),
            officialAnswer = "**(a) Cost of Capital:**\n" +
                "1. **Cost of Equity (Ke):**\n" +
                "   Ke = (D1 / P0) + g = (Rs. 4 / Rs. 40) + 0.05 = 0.10 + 0.05 = **15.0%**\n" +
                "2. **After-tax Cost of Debt (Kd):**\n" +
                "   Kd = Interest rate × (1 - Tax) = 12% × (1 - 0.30) = **8.4%**\n\n" +
                "**(b) Degree of Leverages:**\n" +
                "• Sales = Rs. 40,00,000\n" +
                "• Variable Cost (50%) = Rs. 20,00,000\n" +
                "• Contribution = Sales - VC = Rs. 20,00,000\n" +
                "• EBIT = Rs. 16,00,000 (Fixed Cost = Contribution - EBIT = Rs. 4,00,000)\n" +
                "• Interest = 12% on Rs. 30 Lakhs = Rs. 3,60,000\n" +
                "• EBT = EBIT - Interest = 16,00,000 - 3,60,000 = Rs. 12,40,000\n\n" +
                "1. **Degree of Operating Leverage (DOL):**\n" +
                "   DOL = Contribution / EBIT = 20,00,000 / 16,00,000 = **1.25**\n" +
                "2. **Degree of Financial Leverage (DFL):**\n" +
                "   DFL = EBIT / EBT = 16,00,000 / 12,40,000 = **1.29**\n" +
                "3. **Degree of Combined Leverage (DCL):**\n" +
                "   DCL = DOL × DFL = 1.25 × 1.29 = **1.61**",
            approachTips = "Always show the step-by-step Income Statement flow: Sales -> Contribution -> EBIT -> EBT -> EAT.",
            examinerObservations = "Students sometimes divide EBIT by EAT instead of EBT for DFL. Remember DFL = EBIT / EBT when there are no preference shares!"
        ),
        PracticeQuestion(
            id = "p6_q2_sm_porter_five_forces",
            paper = PaperCode.PAPER_6,
            chapterName = "Strategic Management • Industry & Competitive Analysis",
            questionNumber = "Q2",
            questionText = "Explain Michael Porter's Five Forces Model for analyzing industry attractiveness. How do the 'Bargaining Power of Buyers' and 'Threat of Substitutes' influence profitability in the Indian Airline / E-Commerce industry? Also, explain the danger of being 'Stuck in the Middle' in Porter's Generic Competitive Strategies.",
            marks = 10,
            minutesAllowed = 15,
            tags = listOf("10 Marks", "15 Minutes", "ICAI FAV", "High Chance", "Porter's 5 Forces", "Generic Strategies", "Strategic Management"),
            officialAnswer = "**1. Michael Porter's Five Forces Model:**\n" +
                "Porter postulates that industry profitability is determined by five basic competitive forces:\n" +
                "• **Threat of New Entrants:** Depends on barriers to entry (capital requirements, economies of scale, access to distribution).\n" +
                "• **Bargaining Power of Suppliers:** High when input is unique, switching costs are high, or suppliers are concentrated.\n" +
                "• **Bargaining Power of Buyers:** High when buyers purchase in large volumes, products are standardized, or switching costs are low.\n" +
                "• **Threat of Substitute Products:** Puts a ceiling on prices firms can charge (e.g., High-speed trains or video conferencing vs airlines).\n" +
                "• **Rivalry among Existing Competitors:** Intense when competitors are numerous, industry growth is slow, or exit barriers are high.\n\n" +
                "**2. Analysis in Indian Context:**\n" +
                "• **Bargaining Power of Buyers:** Very high in Indian Aviation and E-commerce due to zero switching cost for consumers, transparent aggregator price comparison engines (OTAs, shopping portals), and high price sensitivity.\n" +
                "• **Threat of Substitutes:** High (Vande Bharat trains and road expressway networks act as strong physical substitutes for short-haul flights).\n\n" +
                "**3. Concept of 'Stuck in the Middle':**\n" +
                "• Under Porter's Generic Strategies, a firm must clearly choose either **Cost Leadership**, **Differentiation**, or **Focus**.\n" +
                "• A firm that fails to achieve a distinct cost advantage and fails to achieve meaningful product differentiation becomes 'Stuck in the Middle'.\n" +
                "• It suffers from low profitability, confused brand identity, and loses high-volume customers to cost leaders and premium customers to differentiated rivals.",
            approachTips = "1. Draw the Porter Five Forces diagram schematic.\n2. Provide concrete contemporary Indian industry examples (Aviation/Telecom/E-Commerce).\n3. Explicitly define 'Stuck in the Middle' with its economic consequences.",
            examinerObservations = "Students often describe the five forces generically without explaining how buyer power or substitutes exert downward pressure on industry margins."
        )
    )

    fun getQuestionsForPaper(paper: PaperCode): List<PracticeQuestion> {
        return allQuestions.filter { it.paper == paper }
    }

    fun getQuestionById(id: String): PracticeQuestion? {
        return allQuestions.firstOrNull { it.id.equals(id, ignoreCase = true) }
    }

    fun getCompulsoryQuestions(): List<PracticeQuestion> {
        return allQuestions.filter { it.questionNumber.contains("Compulsory", ignoreCase = true) }
    }
}

/**
 * Safely reads `.md` question bank files from `context.assets` if present,
 * and falls back to `CuratedQuestionBank` if the asset file is not found or fails to load.
 */
fun parseQuestionsFromAssets(
    context: Context,
    fileName: String,
    paper: PaperCode
): List<PracticeQuestion> {
    return try {
        val assetManager = context.assets
        val availableFiles = assetManager.list("") ?: emptyArray()
        val resolvedFileName = availableFiles.firstOrNull { it.equals(fileName, ignoreCase = true) }

        if (resolvedFileName == null) {
            return CuratedQuestionBank.getQuestionsForPaper(paper)
        }

        val inputStream = try {
            assetManager.open(resolvedFileName)
        } catch (e: Exception) {
            return CuratedQuestionBank.getQuestionsForPaper(paper)
        }

        val parsedList = mutableListOf<PracticeQuestion>()

        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var currentChapter = "Chapter 1"
            var currentQNum = ""
            val currentQText = StringBuilder()
            var currentMetaRaw = ""
            val currentAnswer = StringBuilder()
            val currentApproach = StringBuilder()
            val currentObservations = StringBuilder()

            val questionPattern = Pattern.compile("^(?:PART\\s+[A-Z]\\s+)?(?:Question|Q)\\.?\\s*(\\d+[a-zA-Z]?)[:.]?\\s*(.*)", Pattern.CASE_INSENSITIVE)
            val answerPattern = Pattern.compile("^(?:Answer|Ans|Solution)\\.?\\s*(\\d+[a-zA-Z]?)?[:.]?\\s*(.*)", Pattern.CASE_INSENSITIVE)
            val approachPattern = Pattern.compile("^(?:Approach\\s*(?:&|and)?\\s*Time-?Saving\\s*Tips?|Approach\\s*:)(.*)", Pattern.CASE_INSENSITIVE)
            val observationPattern = Pattern.compile("^(?:Examiner\\s*Observations?|Examiner\\s*Comments?)(.*)", Pattern.CASE_INSENSITIVE)
            val chapterPattern = Pattern.compile("^(?:Chapter|Unit|#+)\\s*[-:]?\\s*(\\d+.*)", Pattern.CASE_INSENSITIVE)

            var currentSection = 0 // 0: None, 1: Question, 2: Answer, 3: Approach, 4: Observations

            fun flushQuestion() {
                if (currentQNum.isNotBlank() && (currentQText.isNotBlank() || currentAnswer.isNotBlank())) {
                    val (marks, minutes, tags) = parseQuestionMetadata(currentMetaRaw)
                    val q = PracticeQuestion(
                        id = "${paper.code}_${currentChapter.take(15).trim().replace(" ", "_")}_q$currentQNum".lowercase(),
                        paper = paper,
                        chapterName = currentChapter,
                        questionNumber = currentQNum,
                        questionText = currentQText.toString().trim(),
                        marks = marks,
                        minutesAllowed = minutes,
                        tags = tags,
                        officialAnswer = if (currentAnswer.isNotBlank()) currentAnswer.toString().trim() else "Refer to standard ICAI suggested answer.",
                        approachTips = if (currentApproach.isNotBlank()) currentApproach.toString().trim() else "Structure answer with Legal Provision, Analysis, and clear Conclusion.",
                        examinerObservations = if (currentObservations.isNotBlank()) currentObservations.toString().trim() else "Quote specific Section / AS and avoid vague generalizations."
                    )
                    parsedList.add(q)
                }
                currentQNum = ""
                currentQText.clear()
                currentMetaRaw = ""
                currentAnswer.clear()
                currentApproach.clear()
                currentObservations.clear()
            }

            var line = reader.readLine()
            var count = 0
            while (line != null && count < 200) {
                val trimmed = line.trim()

                if (trimmed.contains("Catestseries.org", ignoreCase = true) || trimmed.startsWith("©")) {
                    line = reader.readLine()
                    continue
                }

                val chapterMatcher = chapterPattern.matcher(trimmed)
                if (chapterMatcher.find() && trimmed.length < 90) {
                    currentChapter = trimmed.replace("#", "").trim()
                    line = reader.readLine()
                    continue
                }

                val qMatcher = questionPattern.matcher(trimmed)
                if (qMatcher.find()) {
                    flushQuestion()
                    count++
                    currentQNum = qMatcher.group(1) ?: count.toString()
                    val remainder = qMatcher.group(2) ?: ""
                    if (remainder.isNotBlank()) {
                        currentQText.append(remainder).append("\n")
                    }
                    currentSection = 1
                    line = reader.readLine()
                    continue
                }

                val ansMatcher = answerPattern.matcher(trimmed)
                if (ansMatcher.find()) {
                    currentSection = 2
                    val remainder = ansMatcher.group(2) ?: ""
                    if (remainder.isNotBlank()) {
                        currentAnswer.append(remainder).append("\n")
                    }
                    line = reader.readLine()
                    continue
                }

                val appMatcher = approachPattern.matcher(trimmed)
                if (appMatcher.find()) {
                    currentSection = 3
                    val remainder = appMatcher.group(1) ?: ""
                    if (remainder.isNotBlank()) {
                        currentApproach.append(remainder).append("\n")
                    }
                    line = reader.readLine()
                    continue
                }

                val obsMatcher = observationPattern.matcher(trimmed)
                if (obsMatcher.find()) {
                    currentSection = 4
                    val remainder = obsMatcher.group(1) ?: ""
                    if (remainder.isNotBlank()) {
                        currentObservations.append(remainder).append("\n")
                    }
                    line = reader.readLine()
                    continue
                }

                if (currentSection == 1 && currentMetaRaw.isBlank() &&
                    (trimmed.startsWith("[") || (trimmed.startsWith("(") && (trimmed.contains("mark", ignoreCase = true) || trimmed.contains("min", ignoreCase = true))))
                ) {
                    currentMetaRaw = trimmed
                    line = reader.readLine()
                    continue
                }

                when (currentSection) {
                    1 -> currentQText.append(trimmed).append("\n")
                    2 -> currentAnswer.append(trimmed).append("\n")
                    3 -> currentApproach.append(trimmed).append("\n")
                    4 -> currentObservations.append(trimmed).append("\n")
                }

                line = reader.readLine()
            }
            flushQuestion()
        }

        if (parsedList.isNotEmpty()) {
            parsedList
        } else {
            CuratedQuestionBank.getQuestionsForPaper(paper)
        }
    } catch (e: Exception) {
        CuratedQuestionBank.getQuestionsForPaper(paper)
    }
}

private fun parseQuestionMetadata(metaRaw: String): Triple<Int, Int, List<String>> {
    var marks = 5
    var minutes = 8
    val tags = mutableListOf<String>()

    val clean = metaRaw.replace("[", "").replace("]", "").replace("(", "").replace(")", "")
    val parts = clean.split("|", ",", ";")

    for (part in parts) {
        val t = part.trim()
        val lower = t.lowercase()
        if (lower.contains("mark")) {
            val num = lower.filter { it.isDigit() }.toIntOrNull()
            if (num != null && num in 1..30) {
                marks = num
                tags.add("$marks Marks")
            }
        } else if (lower.contains("min")) {
            val num = lower.filter { it.isDigit() }.toIntOrNull()
            if (num != null && num in 1..60) {
                minutes = num
                tags.add("$minutes Minutes")
            }
        } else if (t.isNotBlank()) {
            tags.add(t)
        }
    }

    if (!tags.any { it.contains("Mark", ignoreCase = true) }) {
        tags.add(0, "$marks Marks")
    }
    if (!tags.any { it.contains("Minute", ignoreCase = true) }) {
        tags.add(1, "$minutes Minutes")
    }
    if (!tags.contains("ICAI FAV")) {
        tags.add("ICAI FAV")
    }
    if (!tags.contains("High Chance")) {
        tags.add("High Chance")
    }

    return Triple(marks, minutes, tags.distinct())
}
