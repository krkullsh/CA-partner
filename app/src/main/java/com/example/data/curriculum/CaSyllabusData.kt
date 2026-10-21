package com.example.data.curriculum

import com.example.data.model.PaperCode
import com.example.data.model.SyllabusTopic

object CaSyllabusData {
    val topics: List<SyllabusTopic> = listOf(
        // PAPER 1: Advanced Accounting
        SyllabusTopic(
            id = "p1_as4",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 4: Contingencies and Events Occurring After Balance Sheet Date",
            keyProvisions = listOf(
                "Adjusting events: conditions existing on or before balance sheet date",
                "Non-adjusting events: conditions arising after balance sheet date (disclosure in report of approving authority)",
                "Going concern impairment is ALWAYS an adjusting event",
                "Proposed dividend after balance sheet date is a non-adjusting event (AS 4 revised)"
            ),
            commonExaminerPitfalls = "Students often treat proposed dividend or post-year-end fire loss as adjusting events. Fire loss is non-adjusting unless it destroys going concern!"
        ),
        SyllabusTopic(
            id = "p1_as2",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 2: Valuation of Inventories",
            keyProvisions = listOf(
                "Lower of cost and Net Realizable Value (NRV)",
                "Costs excluded: abnormal waste, storage cost unless necessary in production, admin overheads, selling & distribution",
                "Materials held for use in production of finished goods not written down below cost if finished goods are sold at or above cost"
            ),
            commonExaminerPitfalls = "Adding selling costs or general admin overheads into inventory cost; not checking raw material NRV rule against finished product selling price."
        ),
        SyllabusTopic(
            id = "p1_as10",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 10: Property, Plant and Equipment",
            keyProvisions = listOf(
                "Cost includes initial estimate of dismantling and restoring site (decommissioning liability)",
                "Component accounting: separate depreciation for significant parts with different useful lives",
                "Subsequent expenditure capitalized only if it increases future economic benefits"
            ),
            commonExaminerPitfalls = "Ignoring decommissioning obligations in initial cost capitalization; failing to separate major inspection costs."
        ),
        SyllabusTopic(
            id = "p1_buyback",
            paper = PaperCode.PAPER_1,
            chapter = "Company Accounts",
            topicName = "Buyback of Securities (Section 68)",
            keyProvisions = listOf(
                "Max 25% of aggregate paid-up capital and free reserves",
                "Max 25% of total paid-up equity capital in a financial year",
                "Post-buyback Debt-to-Equity ratio cannot exceed 2:1",
                "Transfer nominal value to Capital Redemption Reserve (CRR) if bought out of free reserves/securities premium"
            ),
            commonExaminerPitfalls = "Applying the 25% test on equity shares count instead of aggregate reserves test, or forgetting to create CRR."
        ),
        SyllabusTopic(
            id = "p1_cfs",
            paper = PaperCode.PAPER_1,
            chapter = "Consolidation",
            topicName = "Consolidated Financial Statements (AS 21)",
            keyProvisions = listOf(
                "Cost of control / Goodwill or Capital Reserve computation on acquisition date",
                "Minority interest calculation (Equity + share of post/pre acquisition reserves)",
                "Elimination of intra-group balances and unrealized profits on downstream/upstream inventory"
            ),
            commonExaminerPitfalls = "Failing to divide unrealized profit between holding and minority interest on upstream sales."
        ),

        // PAPER 2: Corporate and Other Laws
        SyllabusTopic(
            id = "p2_sec185",
            paper = PaperCode.PAPER_2,
            chapter = "Companies Act 2013",
            topicName = "Section 185: Loans to Directors",
            keyProvisions = listOf(
                "General prohibition on giving loans, guarantees, or securities to directors or interested persons",
                "Exemptions: Managing/Whole-time Director as per service condition, loan in ordinary course with interest >= RBI bank rate",
                "Special resolution allowed for entities where director is interested provided borrower uses loan for principal business"
            ),
            commonExaminerPitfalls = "Confusing Section 185 (Loans to Directors - strict prohibition) with Section 186 (Loans & Investments by Company - 60%/100% threshold)."
        ),
        SyllabusTopic(
            id = "p2_sec186",
            paper = PaperCode.PAPER_2,
            chapter = "Companies Act 2013",
            topicName = "Section 186: Loans and Investments by Company",
            keyProvisions = listOf(
                "Threshold: 60% of paid-up share capital + free reserves + securities premium OR 100% of free reserves + securities premium, whichever is higher",
                "Prior approval by Special Resolution if limit is exceeded",
                "Unanimous Board Resolution required at Board meeting with consent of all directors present"
            ),
            commonExaminerPitfalls = "Taking simple majority instead of unanimous board approval at board meeting, or missing securities premium from the 60% base."
        ),
        SyllabusTopic(
            id = "p2_sec135",
            paper = PaperCode.PAPER_2,
            chapter = "Companies Act 2013",
            topicName = "Section 135: Corporate Social Responsibility (CSR)",
            keyProvisions = listOf(
                "Applicability: Net worth >= ₹500 Cr OR Turnover >= ₹1000 Cr OR Net Profit >= ₹5 Cr during immediately preceding financial year",
                "Spend: At least 2% of average net profits made during the 3 immediately preceding financial years",
                "Unspent amount relating to ongoing project transfer to Unspent CSR Account within 30 days of FY end"
            ),
            commonExaminerPitfalls = "Looking at 3 preceding years for applicability instead of immediately preceding FY; confusing ongoing project transfer deadlines."
        ),
        SyllabusTopic(
            id = "p2_fema",
            paper = PaperCode.PAPER_2,
            chapter = "Other Laws",
            topicName = "FEMA 1999: Current Account vs Capital Account Transactions",
            keyProvisions = listOf(
                "Person Resident in India (PRI): resided in India > 182 days in preceding FY (subject to employment/business intent)",
                "Current Account transactions freely permitted unless restricted under Schedule I, II, or III",
                "Capital Account transactions alter assets or liabilities outside India of a resident or in India of a non-resident"
            ),
            commonExaminerPitfalls = "Determining residential status based on current year days instead of preceding FY; assuming all foreign remittances need RBI approval."
        ),

        // PAPER 3: Taxation (Direct Tax & GST)
        SyllabusTopic(
            id = "p3_tds_194c_194j",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax - TDS",
            topicName = "TDS Provisions: Sec 194C (Contractors) vs Sec 194J (Professionals)",
            keyProvisions = listOf(
                "Sec 194C: Rate 1% (Ind/HUF), 2% (others). Threshold: Single contract > ₹30,000 or aggregate > ₹1,00,000 per FY",
                "Sec 194J: Rate 10% (professional / royalty) and 2% (technical services / call centre). Threshold: ₹30,000 per category",
                "Sec 206AA: If PAN not furnished, deduct TDS at higher of 20% or rate in force"
            ),
            commonExaminerPitfalls = "Applying 10% on FTS (fees for technical services) instead of amended 2%; missing the ₹1,00,000 aggregate limit under 194C."
        ),
        SyllabusTopic(
            id = "p3_gst_itc_17_5",
            paper = PaperCode.PAPER_3,
            chapter = "GST - Input Tax Credit",
            topicName = "Section 17(5): Blocked Credits under GST",
            keyProvisions = listOf(
                "Motor vehicles for transport of persons with seating capacity <= 13 persons (including driver) are BLOCKED unless used for taxable supply of vehicles, passenger transport, or driving school",
                "Food, beverages, outdoor catering, health services, club membership BLOCKED unless statutory obligation or used as inward supply of same category",
                "Goods lost, stolen, destroyed, written off, or given as gift/free sample BLOCKED"
            ),
            commonExaminerPitfalls = "Allowing ITC on passenger cars used for sales executives or factory visits; forgetting that goods given as free samples require ITC reversal."
        ),
        SyllabusTopic(
            id = "p3_gst_rcm",
            paper = PaperCode.PAPER_3,
            chapter = "GST - Charge of Tax",
            topicName = "Section 9(3) / 9(4): Reverse Charge Mechanism (RCM)",
            keyProvisions = listOf(
                "Recipient is liable to pay tax directly to the government",
                "Payment of RCM must be made in CASH only (electronic credit ledger CANNOT be used)",
                "Recipient can claim ITC on RCM tax paid in the same/subsequent month subject to section 16"
            ),
            commonExaminerPitfalls = "Attempting to offset RCM liability using input tax credit balance instead of paying in electronic cash ledger."
        ),

        // PAPER 4: Cost and Management Accounting
        SyllabusTopic(
            id = "p4_material_eoq",
            paper = PaperCode.PAPER_4,
            chapter = "Material Cost",
            topicName = "Economic Order Quantity (EOQ) and Stock Levels",
            keyProvisions = listOf(
                "EOQ = sqrt((2 * Annual Demand * Order Cost) / Carrying Cost per unit per year)",
                "Reorder Level = Maximum Consumption * Maximum Lead Time",
                "Safety Stock = (Max Usage - Avg Usage) * Lead Time",
                "At EOQ, Total Ordering Cost = Total Carrying Cost"
            ),
            commonExaminerPitfalls = "Carrying cost percentage calculated on order cost instead of purchase price per unit; confusing units vs financial amounts."
        ),
        SyllabusTopic(
            id = "p4_standard_costing",
            paper = PaperCode.PAPER_4,
            chapter = "Standard Costing",
            topicName = "Material & Labour Variance Analysis",
            keyProvisions = listOf(
                "Material Cost Variance = (Standard Quantity * Standard Price) - (Actual Quantity * Actual Price)",
                "Material Price Variance = Actual Quantity * (Standard Price - Actual Price)",
                "Material Usage Variance = Standard Price * (Standard Quantity - Actual Quantity)",
                "Labour Efficiency Variance = Standard Rate * (Standard Hours - Actual Hours Worked)"
            ),
            commonExaminerPitfalls = "Multiplying Material Usage Variance by Actual Price instead of Standard Price! This is the #1 student error."
        ),
        SyllabusTopic(
            id = "p4_marginal_costing",
            paper = PaperCode.PAPER_4,
            chapter = "Marginal Costing",
            topicName = "Breakeven Analysis & Margin of Safety",
            keyProvisions = listOf(
                "P/V Ratio = (Contribution / Sales) * 100 = (Change in Profit / Change in Sales) * 100",
                "Breakeven Point (Value) = Fixed Cost / P/V Ratio",
                "Margin of Safety = Actual Sales - Breakeven Sales = Profit / P/V Ratio",
                "Total Sales = Breakeven Sales + Margin of Safety"
            ),
            commonExaminerPitfalls = "Treating fixed cost as variable per unit when volume changes; mixing up units with rupee values."
        ),

        // PAPER 5: Auditing and Ethics
        SyllabusTopic(
            id = "p5_sa500_505",
            paper = PaperCode.PAPER_5,
            chapter = "Audit Evidence",
            topicName = "SA 500 (Audit Evidence) & SA 505 (External Confirmations)",
            keyProvisions = listOf(
                "Sufficient Appropriate Audit Evidence (SAAE): Quantity (sufficiency) + Quality (appropriateness - relevance & reliability)",
                "External evidence obtained directly by auditor is more reliable than internal evidence",
                "If management refuses to allow confirmation: auditor must inquire reasons, evaluate implications on risk assessment & fraud, perform alternative procedures",
                "If refusal unreasonable: communicate to TCWG and consider impact on Auditor's Opinion (SA 705)"
            ),
            commonExaminerPitfalls = "Students state 'auditor should simply accept management request' or forget to specify performing alternative audit procedures."
        ),
        SyllabusTopic(
            id = "p5_caro2020",
            paper = PaperCode.PAPER_5,
            chapter = "CARO 2020",
            topicName = "Companies (Auditor's Report) Order, 2020: Key Clauses",
            keyProvisions = listOf(
                "Clause (i): PPE and Intangible assets, physical verification by management, title deeds held in company name, benami proceedings",
                "Clause (ii): Inventory physical verification (discrepancy >= 10% in aggregate requires reporting), quarterly returns to banks for working capital > ₹5 Cr",
                "Clause (vii): Statutory dues (undisputed dues outstanding > 6 months from date they became payable)",
                "Clause (xi): Fraud by or on the company noticed/reported during the year, Form ADT-4 filing"
            ),
            commonExaminerPitfalls = "Writing CARO 2016 clause numbers; forgetting the 10% inventory discrepancy threshold or ₹5 Cr bank limit."
        ),
        SyllabusTopic(
            id = "p5_sa705",
            paper = PaperCode.PAPER_5,
            chapter = "Audit Report",
            topicName = "SA 705: Modifications to the Opinion in Independent Auditor's Report",
            keyProvisions = listOf(
                "Material but NOT Pervasive: Qualified Opinion ('Except for...')",
                "Material AND Pervasive + Misstatement identified: Adverse Opinion",
                "Material AND Pervasive + Inability to obtain SAAE: Disclaimer of Opinion",
                "Basis for Opinion paragraph must immediately follow Opinion paragraph"
            ),
            commonExaminerPitfalls = "Issuing an Adverse Opinion when evidence is missing (should be Disclaimer), or vice versa."
        ),

        // PAPER 6: Financial Management & Strategic Management
        SyllabusTopic(
            id = "p6_leverage",
            paper = PaperCode.PAPER_6,
            chapter = "Financial Management",
            topicName = "Leverage Analysis: DOL, DFL, and DCL",
            keyProvisions = listOf(
                "Degree of Operating Leverage (DOL) = Contribution / EBIT (measures operating risk)",
                "Degree of Financial Leverage (DFL) = EBIT / (EBT - (Pref Dividend / (1 - t))) (measures financial risk)",
                "Degree of Combined Leverage (DCL) = DOL * DFL = Contribution / EBT",
                "High operating fixed costs increase DOL; high interest debt increases DFL"
            ),
            commonExaminerPitfalls = "Dividing EBIT by Contribution instead of Contribution / EBIT; forgetting grossing up preference dividend by (1 - t)."
        ),
        SyllabusTopic(
            id = "p6_wacc",
            paper = PaperCode.PAPER_6,
            chapter = "Financial Management",
            topicName = "Cost of Capital: Ke, Kd, and WACC",
            keyProvisions = listOf(
                "Cost of Debt (Kd) = I(1 - t) / Net Proceeds (for irredeemable debt)",
                "Cost of Equity (Ke) under CAPM = Rf + Beta * (Rm - Rf)",
                "Market value weights preferred over Book value weights for WACC",
                "Retained earnings cost (Kr) = Ke (ignoring brokerage/personal tax)"
            ),
            commonExaminerPitfalls = "Forgetting tax shield (1 - t) on debenture interest; using Book Value weights when market values are available."
        ),
        SyllabusTopic(
            id = "p6_sm_matrices",
            paper = PaperCode.PAPER_6,
            chapter = "Strategic Management",
            topicName = "Strategic Analysis: BCG Matrix vs Ansoff Matrix",
            keyProvisions = listOf(
                "BCG Matrix: Market Growth Rate (vertical) vs Relative Market Share (horizontal) -> Stars, Cash Cows, Question Marks, Dogs",
                "Ansoff Product-Market Growth Matrix: Market Penetration (Existing/Existing), Market Development (New/Existing), Product Development (Existing/New), Diversification (New/New)",
                "Cash generated by Cash Cows funds Stars and Question Marks"
            ),
            commonExaminerPitfalls = "Inverting the axes of BCG Matrix or confusing Market Penetration with Market Development."
        )
    )

    fun getTopicsForPaper(paper: PaperCode): List<SyllabusTopic> {
        return topics.filter { it.paper == paper }
    }
}
