package com.example.data.curriculum

import com.example.data.model.PaperCode
import com.example.data.model.PriorityTier
import com.example.data.model.StandardBlueprint
import com.example.data.model.SyllabusTopic

object CaSyllabusData {
    val topics: List<SyllabusTopic> = listOf(
        // =========================================================================
        // PAPER 1: Advanced Accounting (ICAI CA Intermediate New Scheme)
        // 6-Attempt Trend May 2024 to May 2026 Strategy Blueprint
        // =========================================================================

        // --- Module 1 & 2: Company Accounts & Heavyweight Compulsory Problems (Tier 1 MUST) ---
        SyllabusTopic(
            id = "p1_sch3_fin_stmt",
            paper = PaperCode.PAPER_1,
            chapter = "Company Accounts",
            topicName = "Preparation of Financial Statements of Companies (Schedule III Division I)",
            moduleName = "Module 1",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 14.0,
            trendDirection = "Rising Trend • Compulsory Q2 Anchor",
            writtenTarget = 10,
            mcqTarget = 35,
            oralRecallTarget = 5,
            oralRhythmNote = "Daily Schedule III format, 11 analytical ratios & non-current line items recall",
            fusedWithTopics = listOf("Accounting Standards", "Cash Flow Statements"),
            blueprint = StandardBlueprint(
                scope = "Applies to all Indian companies preparing financial statements under Companies (Accounting Standards) Rules (Non-Ind AS / Division I of Schedule III).",
                recognitionAndMeasurement = "Vertical format for Balance Sheet and Statement of Profit and Loss. Classification of assets and liabilities into Current and Non-Current based on Operating Cycle (presumed 12 months if unidentifiable). Strict adherence to Schedule III disclosure norms and round-off rules.",
                disclosure = "Share Capital details (reconciliation of shares, rights/preferences, promoter shareholding, shares held by holding company). Aging schedules for Trade Receivables, Trade Payables, CWIP, and Intangible assets under development. 11 analytical accounting ratios."
            ),
            keyProvisions = listOf(
                "Current vs Non-Current classification based on enterprise Operating Cycle and 12-month realization test",
                "Promoter shareholding table and changes during the year mandatory in Notes to Accounts",
                "Aging schedule for Trade Receivables and Trade Payables with MSME segregation and undisputed/disputed categorization",
                "Mandatory disclosure of 11 analytical accounting ratios (Current, Debt-Equity, Debt Service, ROE, Inventory Turnover, etc.) with explanations if variance > 25%"
            ),
            commonExaminerPitfalls = "Treating proposed dividend as a current liability instead of a contingent liability / disclosure in notes; missing MSME disclosures in trade payables."
        ),
        SyllabusTopic(
            id = "p1_amalgamation_companies",
            paper = PaperCode.PAPER_1,
            chapter = "Corporate Restructuring",
            topicName = "Amalgamation of Companies (AS 14 & Corporate Schemes)",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 14.0,
            trendDirection = "Compulsory 14M Long Problem",
            writtenTarget = 10,
            mcqTarget = 35,
            oralRecallTarget = 5,
            oralRhythmNote = "Purchase Consideration & Ledger closing entries every 2nd day",
            fusedWithTopics = listOf("AS 14", "Corporate Restructuring"),
            blueprint = StandardBlueprint(
                scope = "Accounting for combinations, mergers, and takeovers involving liquidation of transferor entity and acquisition by transferee company.",
                recognitionAndMeasurement = "Purchase Consideration (PC) = Total payments to shareholders (equity and preference) in shares, cash, or securities. Net Asset Method (Total agreed value of assets taken over less agreed value of liabilities) or Net Payment Method. Merger method: Pooling of interests (book values, general reserve adjustments). Purchase method: Goodwill/Capital reserve and Statutory Reserve maintenance.",
                disclosure = "Scheme particulars, effective date, accounting method applied, computation of PC, treatment of statutory reserves and inter-company holdings."
            ),
            keyProvisions = listOf(
                "Purchase Consideration includes ONLY payments to shareholders (payments to debenture holders or liquidation costs are NOT part of PC)",
                "Net Payment Method vs Net Asset Method for calculating Purchase Consideration",
                "Inter-company debtors/creditors and bills receivable/payable cancelled against each other",
                "Unrealized profit on inter-company stock eliminated by debiting Goodwill/Capital Reserve (or P&L if merger)"
            ),
            commonExaminerPitfalls = "Adding payments made to debenture holders or trade creditors into Purchase Consideration; failing to eliminate inter-company inventory markup."
        ),
        SyllabusTopic(
            id = "p1_consolidation_cfs",
            paper = PaperCode.PAPER_1,
            chapter = "Consolidation",
            topicName = "Consolidated Financial Statements of Group Entities (AS 21, AS 23, AS 27)",
            moduleName = "Module 3",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 14.0,
            trendDirection = "Compulsory 14M Long Problem",
            writtenTarget = 10,
            mcqTarget = 35,
            oralRecallTarget = 5,
            oralRhythmNote = "Pre vs Post acquisition reserves & Minority Interest step-by-step recall",
            fusedWithTopics = listOf("AS 21", "AS 23"),
            blueprint = StandardBlueprint(
                scope = "Consolidation of Balance Sheet and Statement of Profit and Loss of parent holding company with one or more domestic/foreign subsidiaries.",
                recognitionAndMeasurement = "1. Determination of parent's share and Non-Controlling Interest (Minority Interest). 2. Pre-acquisition vs Post-acquisition analysis of subsidiary reserves. 3. Cost of Control (Goodwill or Capital Reserve). 4. Minority Interest at date of Balance Sheet. 5. Elimination of mutual debts, bills, and unrealized intra-group profits.",
                disclosure = "Details of group structure, proportion of voting power, nature of relationship, changes in ownership interest, and significant restrictions on funds transfer."
            ),
            keyProvisions = listOf(
                "Date of acquisition determines segregation of pre-acquisition profits (capital nature -> Cost of Control) and post-acquisition profits (revenue nature -> Consolidated P&L)",
                "Cost of control: Excess of cost of investment over parent's equity in subsidiary on acquisition date = Goodwill; excess of equity over cost = Capital Reserve",
                "Minority interest = Face value of shares held by outsiders + Outside share in pre-acquisition reserves + Outside share in post-acquisition reserves",
                "Elimination of intra-group unrealized profit: Downstream sale -> deduct 100% from Holding P&L; Upstream sale -> deduct pro-rata from Holding P&L and Minority Interest"
            ),
            commonExaminerPitfalls = "Treating post-acquisition bonus shares as income; failing to apportion upstream unrealized inventory profit between Holding and Minority Interest."
        ),
        SyllabusTopic(
            id = "p1_internal_reconstruction",
            paper = PaperCode.PAPER_1,
            chapter = "Corporate Restructuring",
            topicName = "Internal Reconstruction of Companies",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 12.0,
            trendDirection = "Alternating 10-14M Problem • High Scoring",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Capital Reduction Account ledger entries & sacrifices every 3rd day",
            fusedWithTopics = listOf("Buyback of Securities", "AS 3"),
            blueprint = StandardBlueprint(
                scope = "Reorganization of company's capital structure without liquidation, governed by Sections 66 (Reduction of Share Capital) of Companies Act 2013.",
                recognitionAndMeasurement = "All sacrifices made by equity shareholders, preference shareholders, debenture holders, and creditors credited to 'Capital Reduction / Reconstruction Account'. Accumulated losses (P&L debit), unamortized expenses, and overvalued fictitious assets written off. Any balance remaining in Capital Reduction Account transferred to Capital Reserve.",
                disclosure = "Balance sheet prepared post-reconstruction must append words 'And Reduced' if ordered by NCLT, and disclose reconstructed capital structure."
            ),
            keyProvisions = listOf(
                "Creation of Capital Reduction Account (nominal account crediting all sacrifices by stakeholders)",
                "Writing off accumulated losses, fictitious assets (goodwill, preliminary expenses, P&L debit balance)",
                "Surplus remaining in Capital Reduction Account transferred to Capital Reserve (never credited to General Reserve or P&L)",
                "Court / NCLT confirmation required under Section 66 for reduction of share capital"
            ),
            commonExaminerPitfalls = "Crediting profit on reduction to General Reserve instead of Capital Reserve; writing off assets without first debiting accumulated P&L losses."
        ),
        SyllabusTopic(
            id = "p1_buyback_sec68",
            paper = PaperCode.PAPER_1,
            chapter = "Company Accounts",
            topicName = "Buyback of Securities (Section 68) & Equity with Differential Rights",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 10.0,
            trendDirection = "Fused with Internal Reconstruction",
            writtenTarget = 8,
            mcqTarget = 25,
            oralRecallTarget = 4,
            oralRhythmNote = "3 Statutory test formulas (Resource, Share, Debt-Equity) every 3rd day",
            fusedWithTopics = listOf("Internal Reconstruction", "CRR Creation"),
            blueprint = StandardBlueprint(
                scope = "Governed by Sections 68, 69, and 70 of Companies Act 2013 and Companies (Share Capital and Debentures) Rules 2014.",
                recognitionAndMeasurement = "Sources of buyback: Free reserves, Securities Premium, or proceeds of fresh issue of different kind of securities. Nominal value of shares bought back out of free reserves/premium must be transferred to Capital Redemption Reserve (CRR). Three statutory tests: Resource Test (25% paid-up capital + free reserves), Share Outstanding Test (25% total paid-up equity shares), Debt-Equity Ratio Test (Post-buyback debt <= 2 * Equity).",
                disclosure = "Disclose in explanatory statement to notice: full material facts, necessity, class of shares, amount invested, and time limit for completion (within 1 year from special resolution)."
            ),
            keyProvisions = listOf(
                "Resource Test: Max 25% of aggregate paid-up share capital and free reserves",
                "Share Outstanding Test: Max 25% of total paid-up equity capital in that financial year",
                "Debt-to-Equity Test: Post-buyback Debt / Equity ratio cannot exceed 2:1 (Debt includes secured + unsecured; Equity = Paid-up capital + Free reserves)",
                "Transfer nominal value of shares bought back to Capital Redemption Reserve (CRR) if paid out of free reserves or securities premium"
            ),
            commonExaminerPitfalls = "Applying the 25% test on equity share count only and forgetting the monetary aggregate reserves test; failing to transfer nominal value to CRR."
        ),
        SyllabusTopic(
            id = "p1_cfs_company",
            paper = PaperCode.PAPER_1,
            chapter = "Company Accounts",
            topicName = "Cash Flow Statements (Section 2(40) & AS 3)",
            moduleName = "Module 1",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 10.0,
            trendDirection = "Consistent 8-10M Problem",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Operating cash flow adjustments & T-accounts daily",
            fusedWithTopics = listOf("Schedule III", "AS 3"),
            blueprint = StandardBlueprint(
                scope = "Mandatory component of financial statements under Section 2(40) of Companies Act 2013 (exempts One Person Company, Small Company, and Dormant Company).",
                recognitionAndMeasurement = "Operating cash flows derived using Indirect Method (starting with Net Profit before Tax & Extraordinary Items) adjusting for non-cash and non-operating items and working capital changes. Investing and Financing flows calculated using T-accounts.",
                disclosure = "Reconciliation of Opening and Closing Cash and Cash Equivalents, non-cash investing transactions, and restrictions on cash balances held by the entity."
            ),
            keyProvisions = listOf(
                "Exemption from CFS under Companies Act 2013: One Person Company (OPC), Small Company, and Dormant Company",
                "Indirect method format: Net Profit before tax + Non-cash/non-operating charges - Working capital adjustments = Cash from operations - Tax paid",
                "T-account ledger preparation for PPE, Provision for Tax, Proposed Dividend, and Investments to find missing cash purchases/sales",
                "Treatment of interim dividend: Financed out of current profit, outflow under Financing activity"
            ),
            commonExaminerPitfalls = "Adding back non-operating income or deducting non-operating expenses in operating cash flow; forgetting tax refund or dividend tax impacts."
        ),
        SyllabusTopic(
            id = "p1_branch_accounting",
            paper = PaperCode.PAPER_1,
            chapter = "Special Transactions",
            topicName = "Accounting for Branches (including Foreign Branches)",
            moduleName = "Module 3",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "5/6 Attempts",
            averageMarks = 9.0,
            trendDirection = "Rising Trend • High Scoring",
            writtenTarget = 8,
            mcqTarget = 25,
            oralRecallTarget = 4,
            oralRhythmNote = "Stock & Debtors loading entries & Integral/Non-integral rules every 3rd day",
            fusedWithTopics = listOf("AS 11: Foreign Exchange"),
            blueprint = StandardBlueprint(
                scope = "Accounting for Inland Dependent Branches (Debtors method, Stock and Debtors method), Independent Branches, and Foreign Branches under AS 11.",
                recognitionAndMeasurement = "Dependent branches: Goods invoiced at Cost or Invoice Price (load eliminated via Stock Reserve). Independent branches: Branch maintains full set of books, reconciles Head Office Account and Branch Account (cash-in-transit, goods-in-transit). Foreign branches: Classified as Integral (monetary closing rate, non-monetary historical rate, difference to P&L) or Non-Integral (closing rate for BS, average for P&L, difference to FCTR).",
                disclosure = "Incorporation of branch trial balance into Head Office financial statements, reconciliation adjustments, and foreign currency translation disclosures."
            ),
            keyProvisions = listOf(
                "Stock and Debtors method: Branch Stock Account, Branch Debtors Account, Branch Adjustment Account (for invoice price load and gross profit)",
                "Loading on opening stock, goods sent to branch, and closing stock eliminated through Branch Adjustment Account",
                "Inter-branch transactions recorded via Head Office Account (branches do NOT debit/credit each other directly)",
                "Foreign Branch classification under AS 11: Integral (exchange differences to P&L) vs Non-Integral (exchange differences to FCTR in Balance Sheet)"
            ),
            commonExaminerPitfalls = "Recording direct inter-branch entries without routing through Head Office account; forgetting to eliminate loading on branch closing inventory."
        ),

        // --- Core Accounting Standards (Tier 1 & Tier 2) ---
        SyllabusTopic(
            id = "p1_as10",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 10: Property, Plant and Equipment (PPE)",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 10.0,
            trendDirection = "Compulsory Q1 Pairing with AS 15",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Decommissioning liability & Component depreciation rules recall",
            fusedWithTopics = listOf("AS 15: Employee Benefits", "AS 16: Borrowing Costs"),
            blueprint = StandardBlueprint(
                scope = "Applies to tangible items held for use in production, supply of goods/services, rental to others, or administrative purposes, expected to be used for > 12 months.",
                recognitionAndMeasurement = "Initial: Cost includes purchase price, import duties, directly attributable costs, and initial estimate of dismantling/restoration (decommissioning). Subsequent: Cost model or Revaluation model. Component accounting mandatory for significant parts with different useful lives. Depreciation over useful life to residual value.",
                disclosure = "Measurement bases, depreciation methods, useful lives or depreciation rates, gross carrying amount and accumulated depreciation at beginning/end, reconciliation of movements."
            ),
            keyProvisions = listOf(
                "Cost includes initial estimate of dismantling and restoring site (decommissioning liability)",
                "Component accounting: separate depreciation for significant parts with different useful lives",
                "Subsequent expenditure capitalized only if it increases future economic benefits beyond previously assessed standard of performance",
                "Revaluation model: entire class must be revalued; revaluation surplus credited to Revaluation Reserve (OCI)"
            ),
            commonExaminerPitfalls = "Ignoring decommissioning obligations in initial cost capitalization; failing to separate major inspection costs or significant components for depreciation."
        ),
        SyllabusTopic(
            id = "p1_as15",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 15: Employee Benefits",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 10.0,
            trendDirection = "Compulsory Q1 Pairing with AS 10",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Projected Unit Credit (PUC) method & Actuarial gains/losses P&L rule",
            fusedWithTopics = listOf("AS 10: PPE"),
            blueprint = StandardBlueprint(
                scope = "All employee benefits except share-based payments (Guidance Note). Covers Short-term, Post-employment, Other Long-term, and Termination benefits.",
                recognitionAndMeasurement = "Short-term: Undiscounted amount recognized as expense when service is rendered. Post-employment: Defined Contribution (recognize contribution payable) vs Defined Benefit (Projected Unit Credit method, actuarial valuation, actuarial gains/losses recognized immediately in P&L). Termination benefits: Recognized immediately when enterprise is demonstrably committed.",
                disclosure = "Defined benefit plans: Reconciliation of opening/closing balances of present value of obligation and fair value of plan assets, expense recognized in P&L, principal actuarial assumptions."
            ),
            keyProvisions = listOf(
                "Short-term benefits (wages, leave encashment within 12 months): Undiscounted expense as service is rendered",
                "Defined Contribution Plans (Provident Fund): Accrued contribution charged to P&L",
                "Defined Benefit Plans (Gratuity, Pension): Projected Unit Credit (PUC) method used for actuarial valuation",
                "Actuarial gains and losses on defined benefit obligations recognized IMMEDIATELY in P&L (no deferral / corridor method)"
            ),
            commonExaminerPitfalls = "Deferring actuarial gains/losses over employee service lives; confusing defined contribution with defined benefit accounting."
        ),
        SyllabusTopic(
            id = "p1_as2",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 2: Valuation of Inventories",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 6.0,
            trendDirection = "High MCQ Case Scenario Frequency",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Raw materials write-down exception rule check",
            fusedWithTopics = listOf("Costing CMA Material", "Schedule III"),
            blueprint = StandardBlueprint(
                scope = "Applies to all inventories except WIP in construction (AS 7), WIP of service providers, financial instruments, and livestock/mineral oils.",
                recognitionAndMeasurement = "Valued at Lower of Cost and Net Realizable Value (NRV). Cost = Purchase + Conversion + Other costs to bring to present location/condition. Excludes abnormal loss, storage (unless necessary), admin & selling expenses. Formula: FIFO or Weighted Average.",
                disclosure = "Accounting policies adopted in measuring inventories including cost formula used. Total carrying amount and appropriate classification (Raw Materials, WIP, Finished Goods, Stores/Spares)."
            ),
            keyProvisions = listOf(
                "Valuation rule: Lower of Cost and Net Realizable Value (NRV) on an item-by-item basis",
                "Costs excluded from inventory: abnormal waste, storage cost unless necessary in production, general administrative overheads, selling & distribution expenses",
                "Materials held for use in production not written down below cost if finished goods in which they will be incorporated are expected to be sold at or above cost"
            ),
            commonExaminerPitfalls = "Adding selling expenses or administrative overheads into inventory cost; writing down raw materials when finished goods are still profitable."
        ),
        SyllabusTopic(
            id = "p1_as13",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 13: Accounting for Investments",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "5/6 Attempts",
            averageMarks = 6.5,
            trendDirection = "High MCQ Case Scenario Frequency",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Pre-acquisition dividend cost reduction & Rights valuation",
            fusedWithTopics = listOf("Consolidation AS 21", "AS 23"),
            blueprint = StandardBlueprint(
                scope = "Accounting for investments in financial statements of enterprises. Excludes operating/finance leases, retirement benefit funds, and mutual funds.",
                recognitionAndMeasurement = "Classification: Current Investments (readily realizable, intended to hold <= 1 year) vs Long-term Investments. Current investments: Lower of Cost and Fair Value (NAV/Market price) on individual or category basis. Long-term investments: Carried at Cost. Provision for diminution made only if decline is other than temporary.",
                disclosure = "Accounting policies; classification into current and long-term; aggregate amount of quoted investments and market value; aggregate amount of unquoted investments."
            ),
            keyProvisions = listOf(
                "Current investments: Lower of Cost and Fair Value (individual or category basis); write-down charged to P&L",
                "Long-term investments: Carried at Cost; provision made only for 'other than temporary' diminution",
                "Pre-acquisition dividend received: Deducted from acquisition cost of investment (reduces cost), NOT credited to P&L",
                "Right shares: Cost of rights exercised added to investment; sale proceeds of rights renunciations credited to P&L (unless bought on cum-right basis)"
            ),
            commonExaminerPitfalls = "Crediting pre-acquisition dividend to P&L instead of reducing carrying cost of investment; writing down long-term investment for temporary market fluctuations."
        ),
        SyllabusTopic(
            id = "p1_as16",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 16: Borrowing Costs",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 6.0,
            trendDirection = "High Yield Practical Problem",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Qualifying asset criteria & suspension of capitalization",
            fusedWithTopics = listOf("AS 10: PPE"),
            blueprint = StandardBlueprint(
                scope = "Accounting for borrowing costs (interest, commitment charges, finance charges on leases, exchange differences to extent regarded as interest).",
                recognitionAndMeasurement = "Borrowing costs directly attributable to acquisition, construction, or production of a Qualifying Asset MUST be capitalized as part of cost of asset. Qualifying asset: asset that necessarily takes substantial period of time to get ready for its intended use or sale (usually >= 12 months). Suspension of capitalization during extended periods of interruption. Cessation when substantially all activities complete.",
                disclosure = "Accounting policy adopted for borrowing costs; amount of borrowing costs capitalized during the period."
            ),
            keyProvisions = listOf(
                "Qualifying asset: Asset that necessarily takes a substantial period of time to get ready for its intended use or sale (presumed >= 12 months)",
                "Specific borrowings: Actual borrowing costs incurred less any investment income on temporary investment of those borrowings",
                "General borrowings: Capitalization rate = weighted average of borrowing costs applicable to outstanding borrowings",
                "Capitalization suspended during extended periods in which active development is interrupted"
            ),
            commonExaminerPitfalls = "Capitalizing borrowing costs for routinely manufactured inventories or ready-to-use PPE; failing to deduct temporary investment income from specific borrowing costs."
        ),
        SyllabusTopic(
            id = "p1_as19",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 19: Leases",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "5/6 Attempts",
            averageMarks = 6.5,
            trendDirection = "High Yield Practical Problem",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Finance vs Operating lease tests & Sale and leaseback",
            fusedWithTopics = listOf("Financial Management Leasing"),
            blueprint = StandardBlueprint(
                scope = "Applies in accounting for all leases other than mineral/natural resources, licensing agreements, and investment property.",
                recognitionAndMeasurement = "Finance Lease: Transfers substantially all risks and rewards incident to ownership (e.g., PV of MLP >= substantially all fair value, lease term covers major part of economic life). Lessee records asset and liability at lower of Fair Value and PV of Minimum Lease Payments (MLP). Lessor records receivable at Net Investment in Lease. Operating Lease: Lease payments recognized as expense/income on straight-line basis over lease term.",
                disclosure = "Finance leases: Reconciliation between total gross investment and PV of MLP at BS date; unearned finance income. Operating leases: Future minimum lease payments for < 1 yr, 1-5 yrs, > 5 yrs."
            ),
            keyProvisions = listOf(
                "Finance Lease: Substantially all risks and rewards transferred to lessee; recorded at Lower of Fair Value and Present Value of Minimum Lease Payments (MLP)",
                "Discount rate: Interest rate implicit in the lease (if impracticable, lessee's incremental borrowing rate)",
                "Operating Lease: Lease rentals charged to Profit & Loss on straight-line basis over lease term unless another systematic basis is more representative",
                "Sale and Leaseback: If results in finance lease, any excess of sales proceeds over carrying amount deferred and amortized over lease term"
            ),
            commonExaminerPitfalls = "Recognizing gain on sale and leaseback immediately in P&L when it results in a Finance Lease (it must be deferred and amortized!)."
        ),
        SyllabusTopic(
            id = "p1_as20",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 20: Earnings Per Share",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "5/6 Attempts",
            averageMarks = 6.0,
            trendDirection = "High Yield Computational Problem",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Bonus element in rights issue & Diluted EPS formulas",
            fusedWithTopics = listOf("Schedule III Ratios", "Financial Management"),
            blueprint = StandardBlueprint(
                scope = "Mandatory for all enterprises whose equity/potential equity shares are listed or about to be listed, and commercial/industrial enterprises exceeding thresholds.",
                recognitionAndMeasurement = "Basic EPS = (Net Profit or Loss attributable to Equity Shareholders) / (Weighted Average Number of Equity Shares outstanding during the period). Diluted EPS = Adjusted Net Profit for dilutive potential shares / Adjusted Weighted Average Number of Shares. Potential equity shares are dilutive only when their conversion decreases EPS from continuing operations.",
                disclosure = "Basic and Diluted EPS on face of Statement of Profit and Loss with equal prominence. Reconciliations of numerators and denominators used in calculation."
            ),
            keyProvisions = listOf(
                "Basic EPS = Profit attributable to equity shareholders / Weighted average number of equity shares outstanding",
                "Bonus issue / Share split: Weighted average shares adjusted retrospectively as if bonus was issued at beginning of earliest period reported",
                "Right issue: Theoretical ex-rights price calculated to determine bonus element in right shares",
                "Diluted EPS considers all potential equity shares (convertible debentures, ESOPs) unless they are anti-dilutive"
            ),
            commonExaminerPitfalls = "Time-weighting bonus shares from the date of allotment instead of treating them as outstanding from the beginning of the reporting period."
        ),
        SyllabusTopic(
            id = "p1_as22",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 22: Accounting for Taxes on Income",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "5/6 Attempts",
            averageMarks = 6.0,
            trendDirection = "High Yield Problem • DTA/DTL Rules",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Virtual certainty rule for unabsorbed depreciation DTA",
            fusedWithTopics = listOf("Direct Tax", "Schedule III"),
            blueprint = StandardBlueprint(
                scope = "Accounting for taxes on income (current tax and deferred tax) for all enterprises.",
                recognitionAndMeasurement = "Current tax: Amount of tax payable in respect of taxable income for the period. Deferred tax: Recognized for all timing differences (differences between taxable income and accounting income that originate in one period and reverse in one or more subsequent periods). Deferred Tax Asset (DTA) recognized ONLY when reasonable certainty of realization exists. For unabsorbed depreciation/tax losses: VIRTUAL CERTAINTY supported by convincing evidence.",
                disclosure = "Breakup of deferred tax assets and deferred tax liabilities into major components; nature of evidence supporting recognition of DTA where enterprise has unabsorbed depreciation or carry-forward losses."
            ),
            keyProvisions = listOf(
                "Timing differences: Originating and reversing differences between accounting income and taxable income (e.g., depreciation differences, 43B disallowance)",
                "Permanent differences: Never reverse in subsequent periods (e.g., agricultural income, weighted deduction under 35AD) -> NO deferred tax created",
                "Deferred Tax Asset (DTA) recognition rule: Reasonable certainty normally; VIRTUAL CERTAINTY supported by convincing evidence if there are unabsorbed tax losses",
                "DTA/DTL measured using tax rates and tax laws that have been enacted or substantively enacted by the balance sheet date"
            ),
            commonExaminerPitfalls = "Creating DTA on carry-forward business losses without virtual certainty and convincing evidence; calculating deferred tax on permanent differences."
        ),
        SyllabusTopic(
            id = "p1_as28",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 28: Impairment of Assets",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "5/6 Attempts",
            averageMarks = 5.5,
            trendDirection = "High Yield Numerical",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Recoverable amount = HIGHER of Net Selling Price & Value in Use",
            fusedWithTopics = listOf("AS 10: PPE", "AS 26: Intangibles"),
            blueprint = StandardBlueprint(
                scope = "Prescribes procedures that an enterprise applies to ensure its assets are carried at no more than their recoverable amount.",
                recognitionAndMeasurement = "Impairment test: Assess at each balance sheet date whether there is any indication that asset may be impaired. If indication exists: Estimate Recoverable Amount. Recoverable Amount = Higher of Net Selling Price (Fair Value less cost to sell) and Value in Use. Value in Use = Present value of estimated future cash flows from continuing use and ultimate disposal. Impairment loss = Carrying amount - Recoverable amount (charged to P&L).",
                disclosure = "For each class of assets: Amount of impairment losses recognized in P&L, amount of reversals of impairment loss recognized in P&L, events and circumstances leading to impairment."
            ),
            keyProvisions = listOf(
                "Recoverable amount = HIGHER of Net Selling Price and Value in Use (PV of estimated future cash flows discounted at pre-tax rate)",
                "Impairment loss recognized when Carrying Amount > Recoverable Amount; charged immediately to Profit & Loss",
                "Cash Generating Unit (CGU): Smallest identifiable group of assets that generates cash inflows largely independent of other assets",
                "Impairment loss allocated first to reduce Goodwill of CGU, then to other assets pro-rata on basis of carrying amount"
            ),
            commonExaminerPitfalls = "Taking Lower of Net Selling Price and Value in Use instead of Higher; allocating impairment loss to assets below their individual net selling price."
        ),

        // --- Tier 3/4 Theory & Booster Standards ---
        SyllabusTopic(
            id = "p1_as1",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 1: Disclosure of Accounting Policies",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "3/6 Attempts",
            averageMarks = 4.0,
            trendDirection = "Theory / Short Case",
            writtenTarget = 4,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "Fundamental assumptions only disclosed if NOT followed",
            blueprint = StandardBlueprint(
                scope = "Applies to all enterprise financial statements. Covers selection and application of significant accounting policies.",
                recognitionAndMeasurement = "Fundamental Accounting Assumptions: Going Concern, Consistency, Accrual. If followed, no specific disclosure needed. If NOT followed, fact must be specifically disclosed. Policies chosen based on Prudence, Substance over Form, and Materiality.",
                disclosure = "Disclose all significant accounting policies at one place as part of financial statements. Any change in policy having material effect in current or subsequent periods must be disclosed with financial impact."
            ),
            keyProvisions = listOf(
                "Fundamental assumptions: Going Concern, Consistency, Accrual (assumed to be followed; disclosure mandatory ONLY if not followed)",
                "Major considerations governing policy selection: Prudence, Substance over Form, Materiality",
                "Changes in accounting policies: Disclose reasons and quantifiable financial effect, or state that effect is not ascertainable"
            ),
            commonExaminerPitfalls = "Stating that disclosure of Going Concern is required when it is followed. It is only disclosed if NOT followed! Forgetting to state financial impact of change in policy."
        ),
        SyllabusTopic(
            id = "p1_as3",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 3: Cash Flow Statements",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 8.0,
            trendDirection = "Core Standard Problem",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Operating, Investing, Financing classification checks",
            blueprint = StandardBlueprint(
                scope = "Mandatory for all listed and corporate entities (except eligible small enterprises under Rule 2021). Classifies cash flows into Operating, Investing, and Financing.",
                recognitionAndMeasurement = "Operating cash flows: Direct or Indirect method. Investing: acquisition and disposal of long-term assets. Financing: changes in equity size/composition and borrowings. Cash equivalents: highly liquid investments (maturity <= 3 months, insignificant risk of value change).",
                disclosure = "Components of cash and cash equivalents with reconciliation to Balance Sheet. Non-cash investing/financing transactions excluded from cash flow statement but disclosed in notes."
            ),
            keyProvisions = listOf(
                "Classification into Operating, Investing, and Financing activities",
                "Cash equivalents: short-term, highly liquid investments convertible into known cash within 3 months with insignificant risk of change in value",
                "Interest & dividend: For non-financial enterprises, interest paid & dividend paid are Financing; interest received & dividend received are Investing",
                "Taxes on income: Classified under Operating activities unless specifically identifiable with Investing/Financing"
            ),
            commonExaminerPitfalls = "Treating bank overdraft as financing activity when it forms an integral part of cash management (it is a negative cash equivalent); misclassifying dividend paid as operating."
        ),
        SyllabusTopic(
            id = "p1_as4",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 4: Contingencies and Events Occurring After Balance Sheet Date",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "4/6 Attempts",
            averageMarks = 5.0,
            trendDirection = "Short Case Scenario",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Adjusting vs Non-adjusting events & Proposed dividend treatment",
            blueprint = StandardBlueprint(
                scope = "Events between Balance Sheet date and the date on which financial statements are approved by Board of Directors. Note: Contingencies aspect superseded by AS 29.",
                recognitionAndMeasurement = "Adjusting Events: Provide additional evidence of conditions existing at balance sheet date. Adjust assets/liabilities. Non-Adjusting Events: Relate to conditions arising after balance sheet date. No adjustment to assets/liabilities. Exception: Going concern failure is always an adjusting event.",
                disclosure = "Non-adjusting events disclosed in report of approving authority (Board's Report) if material: nature of event and estimate of financial effect (or statement that estimate cannot be made)."
            ),
            keyProvisions = listOf(
                "Adjusting events: conditions existing on or before balance sheet date (e.g., insolvency of debtor shortly after balance sheet date)",
                "Non-adjusting events: conditions arising subsequent to balance sheet date (e.g., major fire destroying inventory in April)",
                "Going concern impairment is ALWAYS an adjusting event even if caused by post-balance-sheet disaster",
                "Proposed dividend after balance sheet date is a non-adjusting event (recognized in subsequent year upon approval)"
            ),
            commonExaminerPitfalls = "Treating proposed dividend or post-balance-sheet fire as adjusting events. Fire loss is non-adjusting unless it destroys enterprise going concern!"
        ),
        SyllabusTopic(
            id = "p1_as5",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 5: Net Profit/Loss, Prior Period Items & Accounting Policy Changes",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "4/6 Attempts",
            averageMarks = 5.0,
            trendDirection = "Short Case Scenario",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Change in accounting estimate is prospective; prior period items are errors",
            blueprint = StandardBlueprint(
                scope = "Applies in presenting profit or loss from ordinary activities, extraordinary items, prior period items, and accounting policy / estimate changes.",
                recognitionAndMeasurement = "Ordinary activities: distinct from extraordinary. Extraordinary items: clearly distinct from ordinary activities and not expected to recur frequently/regularly. Prior period items: income/expenses arising in current period as result of errors or omissions in prior period(s).",
                disclosure = "Extraordinary items disclosed separately in P&L to show effect on current profit. Prior period items disclosed separately in P&L with nature and amount. Changes in accounting estimates disclosed in current period."
            ),
            keyProvisions = listOf(
                "Extraordinary items: distinct from ordinary activities, non-recurring (e.g., loss from earthquake/attachment of property)",
                "Prior period items: errors or omissions in preparation of financial statements of one or more prior periods (routine revisions are NOT prior period)",
                "Change in accounting estimate: handled prospectively in current and future periods (e.g., change in useful life or bad debt percentage)",
                "Change in accounting policy: handled retrospectively unless impracticable"
            ),
            commonExaminerPitfalls = "Treating revision of an estimate (like bad debt provision or warranty estimate) as a prior period item. Revision of estimate is prospective!"
        ),
        SyllabusTopic(
            id = "p1_as7",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 7: Construction Contracts",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "4/6 Attempts",
            averageMarks = 5.0,
            trendDirection = "Percentage of Completion Method Numerical",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Expected contract loss provided IMMEDIATELY in full",
            blueprint = StandardBlueprint(
                scope = "Applies to accounting for construction contracts in financial statements of contractors.",
                recognitionAndMeasurement = "Recognize contract revenue and costs by Percentage of Completion Method (POCM) when contract outcome can be reliably estimated. Stage of completion = (Contract costs incurred to date / Total estimated contract costs). Expected losses recognized immediately in full regardless of stage.",
                disclosure = "Contract revenue recognized in period; methods used to determine contract revenue and stage of completion; aggregate costs incurred + recognized profits less recognized losses; advance received and retentions."
            ),
            keyProvisions = listOf(
                "Percentage of Completion Method (POCM) mandatory for recognizing revenue and costs",
                "Stage of completion = (Costs incurred to date / Total estimated contract costs) * 100",
                "Expected total contract loss must be provided for IMMEDIATELY and fully, irrespective of percentage of completion",
                "Contract revenue includes initial agreed amount plus variations, claims, and incentive payments to the extent probable"
            ),
            commonExaminerPitfalls = "Recognizing contract losses proportionately over the contract period instead of writing off 100% of the total estimated loss immediately."
        ),
        SyllabusTopic(
            id = "p1_as9",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 9: Revenue Recognition",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "4/6 Attempts",
            averageMarks = 5.0,
            trendDirection = "Case Law Problem",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Reasonable certainty of collection rule",
            blueprint = StandardBlueprint(
                scope = "Recognition of revenue from sale of goods, rendering of services, and use by others of enterprise resources yielding interest, royalties, and dividends.",
                recognitionAndMeasurement = "Sale of goods: Transfer of significant risks and rewards of ownership to buyer, no effective managerial control retained, measurable, collection reasonably certain. Services: Completed service or proportionate completion. Interest: Time proportion. Royalties: On accrual as per agreement. Dividends: When right to receive payment is established.",
                disclosure = "Circumstances where revenue recognition has been postponed pending the resolution of significant uncertainties."
            ),
            keyProvisions = listOf(
                "Sale of Goods: Significant risks and rewards of ownership transferred and no effective managerial control retained",
                "Reasonable certainty of collection is an absolute prerequisite for revenue recognition; if uncertainty arises after recognition, create provision for bad debts",
                "Rendering of Services: Proportionate completion method or completed service contract method",
                "Interest recognized on time proportion basis, Royalties on accrual as per agreement, Dividend when shareholder right to receive is established"
            ),
            commonExaminerPitfalls = "Reversing previously recognized revenue when collection becomes uncertain instead of making a bad debt provision; recognizing dividend before AGM declaration date."
        ),
        SyllabusTopic(
            id = "p1_as11",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 11: The Effects of Changes in Foreign Exchange Rates",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "4/6 Attempts",
            averageMarks = 5.0,
            trendDirection = "Monetary vs Non-monetary Items",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Integral branch to P&L; Non-integral branch to FCTR",
            blueprint = StandardBlueprint(
                scope = "Accounting for transactions in foreign currencies and translating financial statements of foreign branches/operations (integral vs non-integral).",
                recognitionAndMeasurement = "Initial: Spot rate on transaction date. Balance Sheet Date: Monetary items (cash, receivables, payables) translated at closing rate; exchange differences recognized in P&L. Non-monetary items (PPE, inventory) carried at historical cost translated at rate on transaction date. Integral branch: translate as if domestic. Non-integral: assets/liabilities at closing rate, income/expense at actual/average, difference to Foreign Currency Translation Reserve (FCTR).",
                disclosure = "Amount of exchange differences included in net profit or loss for the period; net exchange differences accumulated in separate foreign currency translation reserve."
            ),
            keyProvisions = listOf(
                "Monetary items translated at closing rate at each balance sheet date; exchange differences taken to Profit & Loss",
                "Non-monetary items carried at historical cost translated at historical exchange rate (no exchange gain/loss on balance sheet date)",
                "Integral foreign operation: Translated using transaction/average rate (P&L) and closing/historical rate (BS), exchange difference to P&L",
                "Non-integral foreign operation: All assets and liabilities at closing rate, P&L items at average rate, difference to FCTR in Balance Sheet"
            ),
            commonExaminerPitfalls = "Translating inventory or fixed assets at closing rate when valued at cost; confusing integral vs non-integral foreign branch translation rules."
        ),
        SyllabusTopic(
            id = "p1_as12",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 12: Accounting for Government Grants",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "4/6 Attempts",
            averageMarks = 5.0,
            trendDirection = "Depreciable vs Capital Grant Accounting",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Refund of government grant treatment",
            blueprint = StandardBlueprint(
                scope = "Accounting for government grants, assistance, and subsidies. Excludes participation in ownership or infrastructure in development areas.",
                recognitionAndMeasurement = "Recognize when reasonable assurance that entity will comply with conditions and grant will be received. Capital approach vs Income approach. Specific depreciable PPE: deduct from gross book value OR treat as deferred income amortized over useful life in proportion to depreciation. Revenue grants: credit to P&L or deduct from related expense. Promoter contribution: credit to Capital Reserve.",
                disclosure = "Accounting policy adopted, nature and extent of government grants recognized in financial statements."
            ),
            keyProvisions = listOf(
                "Grants related to specific fixed assets: Method 1 (deduct from asset cost), Method 2 (treat as Deferred Government Grant amortized over useful life)",
                "Grants in the nature of promoters' contribution: Credit directly to Capital Reserve",
                "Revenue grants: Credited to Profit & Loss as other income or deducted from related expenses",
                "Refund of government grant: If asset was reduced, increase asset book value; if deferred income, debit deferred balance then P&L"
            ),
            commonExaminerPitfalls = "Crediting grants related to depreciable assets entirely to P&L in year of receipt; missing prospective depreciation adjustment upon grant refund."
        ),
        SyllabusTopic(
            id = "p1_as14",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 14: Accounting for Amalgamations",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 8.0,
            trendDirection = "Pooling of Interests vs Purchase Method",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "5 Merger conditions & Amalgamation Adjustment Deficit Account",
            blueprint = StandardBlueprint(
                scope = "Accounting for amalgamations and treatment of resulting goodwill or reserves.",
                recognitionAndMeasurement = "Amalgamation in the nature of Merger (Pooling of Interests method): 5 conditions (all assets/liabilities transferred, 90% equity shareholders agree, consideration in equity shares, business intended to continue, no book value adjustments except policy harmonization). Difference between purchase consideration and share capital adjusted in General Reserve. Amalgamation in the nature of Purchase: Assets/liabilities recorded at agreed/book values; excess PC over net assets = Goodwill, shortfall = Capital Reserve.",
                disclosure = "Names and general nature of business of amalgamating companies, effective date, method of accounting, details of consideration and reserves created."
            ),
            keyProvisions = listOf(
                "Amalgamation in the nature of Merger: 5 strict conditions satisfied -> Pooling of Interests Method",
                "Amalgamation in the nature of Purchase -> Purchase Method (assets & liabilities recorded at agreed values)",
                "Statutory Reserves of transferor company must be maintained under Purchase method by creating Amalgamation Adjustment Deficit Account",
                "Consideration includes payments to shareholders (equity and preference), excludes liabilities taken over or direct payments to debenture holders"
            ),
            commonExaminerPitfalls = "Including payments to debenture holders or creditors in Purchase Consideration; forgetting Amalgamation Adjustment Deficit Account for statutory reserves."
        ),
        SyllabusTopic(
            id = "p1_as17",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 17: Segment Reporting",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "4/6 Attempts",
            averageMarks = 5.0,
            trendDirection = "10% & 75% Threshold Calculations",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "10% Segment revenue/result/assets and 75% external revenue rule",
            blueprint = StandardBlueprint(
                scope = "Reporting financial information by business and geographical segments for enterprises whose equity/debt is listed or turnover/borrowings exceed limits.",
                recognitionAndMeasurement = "Identify Business and Geographical segments. Reportable segments: Any segment meeting ANY of 10% thresholds (Segment Revenue >= 10% of total revenue; Segment Result >= 10% of greater in absolute amount of total operating profit/loss; Segment Assets >= 10% of total assets). Overall 75% test: External revenue of reportable segments must be >= 75% of total enterprise revenue.",
                disclosure = "Segment revenue, segment result, segment assets, segment liabilities, capital expenditure, depreciation, non-cash expenses, reconciliation to financial statements."
            ),
            keyProvisions = listOf(
                "Primary vs Secondary segment format based on dominant source of enterprise risks and returns",
                "10% Threshold test: Segment is reportable if it meets >= 10% of total revenue, OR >= 10% of segment profit/loss (absolute), OR >= 10% of total segment assets",
                "75% External Revenue Test: If total external revenue of reportable segments < 75% of total enterprise revenue, additional segments must be identified",
                "Unallocated corporate expenses and interest expenses cannot be arbitrarily allocated to segments"
            ),
            commonExaminerPitfalls = "Applying the 10% result threshold on net profit instead of absolute operating profit/loss comparison; ignoring the 75% overall external revenue requirement."
        ),
        SyllabusTopic(
            id = "p1_as18",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 18: Related Party Disclosures",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "4/6 Attempts",
            averageMarks = 5.0,
            trendDirection = "KMP & Control Identification",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Disclosure mandatory if control exists even without transactions",
            blueprint = StandardBlueprint(
                scope = "Applies in reporting related party relationships and transactions between reporting enterprise and its related parties.",
                recognitionAndMeasurement = "Related parties: Holding/subsidiaries/fellow subsidiaries, associates, joint ventures, KMP (Key Management Personnel) and relatives, individuals with significant influence and relatives, enterprises over which KMP/relatives exercise significant influence. Control = > 50% voting power or composition of governing body. Significant influence = >= 20% voting power.",
                disclosure = "Disclose name of related party and nature of relationship (even without transactions if control exists). Where transactions took place: nature of transactions, volume (amounts), outstanding balances, pricing policies."
            ),
            keyProvisions = listOf(
                "Related parties include: Holding, subsidiary, fellow subsidiary, associates, KMP (Managing/Whole-time Director) and their relatives",
                "Non-executive independent directors are NOT considered KMP for related party disclosures",
                "If control exists, disclosure of relationship is mandatory EVEN IF there are NO transactions between parties during the year",
                "Providers of finance, trade unions, public utilities, and government departments are NOT related parties simply by virtue of their normal dealings"
            ),
            commonExaminerPitfalls = "Assuming independent directors are related parties; omitting related party disclosure when there were zero transactions despite existence of control."
        ),
        SyllabusTopic(
            id = "p1_as21",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 21: Consolidated Financial Statements",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 8.0,
            trendDirection = "Line by Line Consolidation Rules",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Cost of control and upstream unrealized profit allocation",
            blueprint = StandardBlueprint(
                scope = "Preparation and presentation of consolidated financial statements for a group of enterprises under the control of a parent.",
                recognitionAndMeasurement = "Control: Ownership of > 50% voting power or control of composition of Board of Directors. Combine financial statements line by line by adding like items of assets, liabilities, income, and expenses. Eliminate parent investment against equity in subsidiary -> Goodwill (excess) or Capital Reserve (deficit). Minority interest calculated and shown separately.",
                disclosure = "List of all subsidiaries, proportion of ownership interest/voting power, nature of relationship if parent does not own > 50% voting power, effect of acquisition/disposal."
            ),
            keyProvisions = listOf(
                "Line-by-line consolidation of assets, liabilities, income, and expenses",
                "Cost of control: Investment cost compared with parent's share of net assets on acquisition date -> Goodwill or Capital Reserve",
                "Minority interest: Outside shareholders' share in net assets (paid-up capital + share in pre & post acquisition reserves)",
                "Elimination of intra-group transactions, balances, and unrealized profits on downstream (100% P&L) and upstream (allocated between parent & minority) inventory"
            ),
            commonExaminerPitfalls = "Deducting 100% of upstream unrealized inventory profit from holding company reserves instead of allocating between holding and minority interest."
        ),
        SyllabusTopic(
            id = "p1_as23",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 23: Accounting for Investments in Associates",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "4/6 Attempts",
            averageMarks = 5.0,
            trendDirection = "Equity Method Application",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Significant influence >= 20% and equity method adjustments",
            blueprint = StandardBlueprint(
                scope = "Accounting by an investor for investments in associates in Consolidated Financial Statements.",
                recognitionAndMeasurement = "Associate: Enterprise in which investor has significant influence (>= 20% voting power) and which is neither subsidiary nor joint venture. Equity Method of accounting: Investment initially recorded at cost; carrying amount increased or decreased to recognize investor's share of profits or losses of associate after date of acquisition. Goodwill/Capital Reserve computed at date of acquisition.",
                disclosure = "Appropriate listing and description of associates, proportion of ownership interest, investor share of profits/losses of associates."
            ),
            keyProvisions = listOf(
                "Equity method used for associates in Consolidated Financial Statements (CFS)",
                "Significant influence presumed if investor holds directly or indirectly >= 20% of voting power",
                "Carrying amount = Cost of acquisition + Investor's share of post-acquisition profits/reserves - Dividends received",
                "Investor ceases equity method when investment ceases to be an associate or investor's share of losses equals/exceeds carrying amount"
            ),
            commonExaminerPitfalls = "Applying line-by-line consolidation to associates instead of Equity Method; continuing to recognize associate losses after carrying value reaches zero."
        ),
        SyllabusTopic(
            id = "p1_as24",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 24: Discontinuing Operations",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "2/6 Attempts",
            averageMarks = 4.0,
            trendDirection = "Booster Theory",
            writtenTarget = 3,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "Initial disclosure event triggers",
            blueprint = StandardBlueprint(
                scope = "Establishing principles for reporting information about discontinuing operations to enhance ability of users to project enterprise cash flows and earnings.",
                recognitionAndMeasurement = "Discontinuing operation: Component of enterprise that represents a separate major line of business or geographical area of operations and is being disposed of pursuant to a single plan. Initial Disclosure Event: Earlier of (a) entering into binding sale agreement, and (b) Board approving detailed formal plan and announcing it publicly. Follows existing AS recognition/measurement principles.",
                disclosure = "Description of discontinuing operation, segment in which reported, date and nature of initial disclosure event, carrying amounts of total assets/liabilities, amounts of revenue/expenses/pre-tax profit/tax expense and net cash flows attributable to ordinary operating, investing, and financing."
            ),
            keyProvisions = listOf(
                "Discontinuing operation: Separate major line of business or geographical area pursuant to a single formal plan",
                "Initial disclosure event: Earlier of binding sale agreement OR Board approval and public announcement of formal plan",
                "P&L and Cash Flow disclosures segregated between continuing and discontinuing operations",
                "Standard does NOT establish new recognition/measurement rules; it mandates segregation and comprehensive disclosure"
            ),
            commonExaminerPitfalls = "Treating gradual phasing out of a single product line as a discontinuing operation when it is not a separate major line of business."
        ),
        SyllabusTopic(
            id = "p1_as25",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 25: Interim Financial Reporting",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "2/6 Attempts",
            averageMarks = 4.0,
            trendDirection = "Booster Theory & Principles",
            writtenTarget = 3,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "Weighted average annual effective tax rate in interim periods",
            blueprint = StandardBlueprint(
                scope = "Applies if an enterprise is required or elects to publish an interim financial report. Prescribes minimum content and principles for recognition/measurement.",
                recognitionAndMeasurement = "Discrete view vs Integral view: AS 25 adopts combination (integral for certain costs, discrete for revenues). Same accounting policies applied as in annual financial statements. Revenues received seasonally or cyclically cannot be anticipated or deferred. Costs incurred unevenly anticipated/deferred only if appropriate at year end. Income tax expense recognized based on estimated weighted average annual effective tax rate.",
                disclosure = "Condensed Balance Sheet, Condensed Statement of P&L, Condensed Cash Flow Statement, Selected explanatory notes (seasonality, unusual items, changes in estimates/policies, dividends)."
            ),
            keyProvisions = listOf(
                "Form and content: Complete financial statements OR Condensed financial statements (Condensed BS, P&L, CFS, and explanatory notes)",
                "Revenues received seasonally/cyclically CANNOT be anticipated or deferred if not appropriate at year-end",
                "Income tax expense recognized in each interim period based on best estimate of weighted average annual effective tax rate",
                "Accounting policies applied in interim reports must be identical to annual financial statements"
            ),
            commonExaminerPitfalls = "Deferring seasonal advertising or maintenance costs to later quarters when there is no contractual right or asset created."
        ),
        SyllabusTopic(
            id = "p1_as26",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 26: Intangible Assets",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "4/6 Attempts",
            averageMarks = 5.0,
            trendDirection = "Research vs Development Phase Rules",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Internally generated goodwill never capitalized; research expensed immediately",
            blueprint = StandardBlueprint(
                scope = "Accounting for intangible assets (identifiable non-monetary asset without physical substance). Excludes financial assets, mineral rights, and insurance contracts.",
                recognitionAndMeasurement = "Recognition criteria: Probable future economic benefits and cost can be measured reliably. Internally generated goodwill / brands / mastheads CANNOT be recognized as assets! Research phase: All expenditure expensed to P&L immediately. Development phase: Capitalized ONLY if all 6 feasibility criteria are met. Amortization over useful life (rebuttable presumption <= 10 years).",
                disclosure = "Useful lives or amortization rates, amortization methods used, gross carrying amount and accumulated amortization at beginning/end, reconciliation of additions/disposals."
            ),
            keyProvisions = listOf(
                "Internally generated goodwill, brands, publishing titles, and customer lists must NEVER be capitalized",
                "Research phase expenditure charged to P&L as incurred (cannot be capitalized even retrospectively)",
                "Development expenditure capitalized ONLY when all 6 technical and financial feasibility criteria are met",
                "Amortization period: Rebuttable presumption that useful life will not exceed 10 years from the date asset is available for use"
            ),
            commonExaminerPitfalls = "Capitalizing research expenditure or internally generated goodwill; failing to amortize software from the date it is ready for intended use."
        ),
        SyllabusTopic(
            id = "p1_as29",
            paper = PaperCode.PAPER_1,
            chapter = "Accounting Standards",
            topicName = "AS 29: Provisions, Contingent Liabilities and Contingent Assets",
            moduleName = "Accounting Standards",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "4/6 Attempts",
            averageMarks = 5.0,
            trendDirection = "Decision Tree Case Problem",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Probable (>50%) provision vs Possible (10-50%) contingent liability",
            blueprint = StandardBlueprint(
                scope = "Applies in accounting for provisions, contingent liabilities, and contingent assets, except those resulting from financial instruments or executory contracts.",
                recognitionAndMeasurement = "Provision: Present obligation from past event, outflow of resources probable (> 50%), and reliable estimate can be made -> RECOGNIZE in BS. Contingent Liability: Possible obligation OR present obligation where outflow is not probable or amount cannot be measured reliably -> DISCLOSE in notes (unless remote). Contingent Asset: Possible asset from past event -> DO NOT RECOGNIZE; disclose only in approving authority report if inflow probable.",
                disclosure = "For each class of provision: Opening balance, additions, utilized, unused reversed, closing balance, brief description of obligation and expected timing of outflows. Contingent liabilities disclosed in notes."
            ),
            keyProvisions = listOf(
                "Provision recognized when: (a) Present obligation from past event, (b) Probable outflow of resources (> 50% chance), and (c) Reliable estimate can be made",
                "Contingent liability disclosed in Notes to Accounts unless possibility of outflow is remote (< 10%)",
                "Contingent assets NEVER recognized in financial statements (prudence); disclosed in Board's Report if inflow of economic benefit is probable",
                "Provisions reviewed at each balance sheet date and adjusted to reflect current best estimate"
            ),
            commonExaminerPitfalls = "Creating a provision for future operating losses or restructuring without a detailed formal binding plan; recognizing contingent asset in Balance Sheet."
        ),

        // =========================================================================
        // PAPER 2: Corporate and Other Laws (ICAI CA Intermediate New Scheme)
        // 12-Attempt Trend Dec 2021 to May 2026 Strategy Blueprint
        // =========================================================================

        // --- Other Laws: The Core Four (Tier 1 MUST - 6/6 Attempts Presence) ---
        SyllabusTopic(
            id = "p2_fema",
            paper = PaperCode.PAPER_2,
            chapter = "Other Laws",
            topicName = "Foreign Exchange Management Act (FEMA), 1999",
            moduleName = "Other Laws",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 13.7,
            trendDirection = "Compulsory Q1 Anchor • Heavyweight",
            writtenTarget = 8,
            mcqTarget = 35,
            oralRecallTarget = 5,
            oralRhythmNote = "PRI 182-day rule & Current Account Schedule I, II, III every 2nd day",
            fusedWithTopics = listOf("Companies Act", "LLP Act"),
            keyProvisions = listOf(
                "Person Resident in India (PRI Sec 2(v)): Resided in India > 182 days in preceding FY; excludes persons leaving for employment/business outside India",
                "Current Account transactions (Sec 5): Prohibited under Schedule I (lottery, football pools), Central Govt approval under Schedule II (cultural tours, transshipment), RBI approval under Schedule III (LRS > $250,000 per FY)",
                "Capital Account transactions (Sec 6): Alter assets/liabilities outside India of resident or inside India of non-resident; prohibited for lottery, chit funds, agricultural/plantation property",
                "Realization and Repatriation of foreign exchange (Sec 8): Mandatory for residents earning foreign exchange within prescribed period"
            ),
            commonExaminerPitfalls = "Determining PRI status based on days in the current year instead of immediately preceding FY; assuming all remittances require RBI approval under FEMA."
        ),
        SyllabusTopic(
            id = "p2_llp_act",
            paper = PaperCode.PAPER_2,
            chapter = "Other Laws",
            topicName = "The Limited Liability Partnership Act, 2008",
            moduleName = "Other Laws",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 12.3,
            trendDirection = "Consistent 12M+ Presence",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 5,
            oralRhythmNote = "Designated Partners residency (120 days) & Form 8 / Form 11 deadlines every 3rd day",
            fusedWithTopics = listOf("Partnership", "Small LLP"),
            keyProvisions = listOf(
                "Designated Partners (Sec 7): At least 2 individuals, at least 1 resident in India (stayed in India >= 120 days in preceding 1 year); DPIN required",
                "Filing Statement of Account & Solvency (Sec 34): Form 8 within 30 days from the end of 6 months of FY (i.e., by 30th October); signed by designated partners",
                "Filing Annual Return (Sec 35): Form 11 within 60 days of closure of financial year (i.e., on or before 30th May)",
                "Small LLP: Contribution <= ₹25 Lakhs (or prescribed up to 5 Cr) AND Turnover in preceding FY <= ₹40 Lakhs (or prescribed up to 50 Cr)"
            ),
            commonExaminerPitfalls = "Applying 182 days residency requirement for LLP designated partners instead of the amended 120-day threshold."
        ),
        SyllabusTopic(
            id = "p2_general_clauses",
            paper = PaperCode.PAPER_2,
            chapter = "Other Laws",
            topicName = "The General Clauses Act, 1897",
            moduleName = "Other Laws",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 8.0,
            trendDirection = "Never Skipped in 12 Exams",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 4,
            oralRhythmNote = "Sec 6 effect of repeal & Sec 9/10 time computation every 3rd day",
            fusedWithTopics = listOf("Interpretation of Statutes"),
            keyProvisions = listOf(
                "Computation of time (Sec 9 & 10): Use 'from' to exclude the first day, 'to' to include the last day; if court/office is closed on last day, act done on next open day is valid",
                "Effect of repeal (Sec 6): Does not revive anything not in force, affect previous operation, affect right/privilege/obligation acquired, or affect penalty/investigation",
                "Duty to be done on alternative days & Power to appoint includes power to suspend or dismiss (Sec 16)",
                "Key definitions: 'Document' (matter expressed/described upon substance by letters/figures/marks), 'Good Faith' (done honestly, whether done negligently or not)"
            ),
            commonExaminerPitfalls = "Confusing General Clauses Act definition of 'Good Faith' (honesty alone, negligence immaterial) with IPC definition (requires due care and attention)."
        ),
        SyllabusTopic(
            id = "p2_interpretation_statutes",
            paper = PaperCode.PAPER_2,
            chapter = "Other Laws",
            topicName = "Interpretation of Statutes (Rules & Aids to Construction)",
            moduleName = "Other Laws",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 8.0,
            trendDirection = "Never Skipped in 12 Exams",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 4,
            oralRhythmNote = "Primary vs Secondary rules (Literal, Mischief, Ejusdem Generis) every 3rd day",
            fusedWithTopics = listOf("General Clauses Act"),
            keyProvisions = listOf(
                "Primary Rules: Rule of Literal Construction (natural plain grammatical meaning), Mischief Rule / Heydon's Case (suppress mischief and advance remedy), Golden Rule (modify words to avoid absurdity), Harmonious Construction (avoid repugnancy)",
                "Secondary Rules: Noscitur a Sociis (words known by companion words), Ejusdem Generis (general words following specific words take meaning from same genus)",
                "Internal aids: Long title, Preamble, Headings, Marginal notes, Proviso (qualifies main provision), Explanation (clarifies meaning), Schedules",
                "External aids: Historical setting, Parliamentary committee reports, dictionaries, prior judicial pronouncements, foreign decisions"
            ),
            commonExaminerPitfalls = "Applying Ejusdem Generis when the specific preceding words do not form a distinct category or genus."
        ),

        // --- Company Law: Core Pillars (Tier 2 - 6/6 Attempts Presence) ---
        SyllabusTopic(
            id = "p2_ch7_mgmt_admin",
            paper = PaperCode.PAPER_2,
            chapter = "Company Law",
            topicName = "Chapter 7: Management & Administration (Sec 88-122)",
            moduleName = "Company Law",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 10.5,
            trendDirection = "Core 10M+ Anchor Chapter",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "AGM gaps (15m), 21 clear days notice, Quorum (5/15/30), Proxies 48 hrs every 3rd day",
            fusedWithTopics = listOf("Accounts of Companies"),
            keyProvisions = listOf(
                "Annual General Meeting (Sec 96): Held every calendar year; max 15 months between two AGMs; within 6 months of FY closing (first AGM within 9 months of first FY)",
                "Notice of meeting (Sec 101): At least 21 clear days written notice (excludes date of serving and date of meeting); shorter notice requires 95% consent of members entitled to vote",
                "Quorum for public company (Sec 103): 5 members (if <= 1000 members), 15 members (if 1001 to 5000), 30 members (if > 5000 members) personally present",
                "Proxies (Sec 105): Cannot vote on show of hands; cannot speak; proxy form filed at least 48 hours before meeting; max 50 members and 10% of total voting power per proxy"
            ),
            commonExaminerPitfalls = "Failing to exclude 2 days (dispatch and meeting day) plus 48 hours for post when computing 21 clear days; counting proxies toward meeting quorum."
        ),
        SyllabusTopic(
            id = "p2_sec185",
            paper = PaperCode.PAPER_2,
            chapter = "Company Law",
            topicName = "Chapter 10: Audit & Auditors & Directors' Loans (Sec 139-148, Sec 185)",
            moduleName = "Company Law",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 10.0,
            trendDirection = "Core 10M Chapter • Overlaps Paper 5",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Sec 141(3) disqualifications & Sec 143(12) ₹1 Cr fraud reporting every 3rd day",
            fusedWithTopics = listOf("Paper 5 Auditing", "Sec 185 Loans"),
            keyProvisions = listOf(
                "Appointment of auditor (Sec 139): 5-year tenure; rotation mandatory for listed and specified unlisted companies (individual: one 5-year term; firm: two 5-year terms with 5-year cooling period)",
                "Disqualifications of auditor (Sec 141(3)): Body corporate, officer/employee, partner of officer, relative holding > ₹1 Lakh face value, indebted > ₹5 Lakh, guarantee > ₹1 Lakh, audit of > 20 companies",
                "Section 185 Loans to Directors: Strict prohibition on loans/guarantees to directors; exception for MD/WTD as part of service conditions, loans in ordinary course at bank rate, and special resolution for entities with director interest used for principal business",
                "Fraud reporting under Sec 143(12): Frauds involving amount >= ₹1 Crore reported to Central Government in Form ADT-4 within 60 days"
            ),
            commonExaminerPitfalls = "Allowing an auditor whose relative holds ₹2 Lakh shares (limit is ₹1 Lakh face value!); confusing Section 185 (Loans to Directors - prohibition) with Section 186 (Loans by Company - threshold)."
        ),
        SyllabusTopic(
            id = "p2_sec135",
            paper = PaperCode.PAPER_2,
            chapter = "Company Law",
            topicName = "Chapter 9: Accounts of Companies & CSR (Sec 128-138)",
            moduleName = "Company Law",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "6/6 Attempts",
            averageMarks = 10.0,
            trendDirection = "Core 10M Chapter • CSR Focus",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Sec 135 CSR thresholds (500/1000/5) & 2% average net profits every 3rd day",
            fusedWithTopics = listOf("Schedule III", "Auditing"),
            keyProvisions = listOf(
                "Maintenance of books of account (Sec 128): Kept at registered office on accrual basis & double entry; preserved for at least 8 preceding financial years",
                "Section 135 CSR Applicability: Net Worth >= ₹500 Cr OR Turnover >= ₹1000 Cr OR Net Profit >= ₹5 Cr during immediately preceding FY; spend at least 2% of average net profits of 3 preceding FYs",
                "Unspent CSR funds: Ongoing projects transferred to Unspent CSR Account within 30 days of FY end; non-ongoing transferred to Fund specified in Schedule VII within 6 months",
                "Internal Audit applicability (Sec 138): Listed companies always; unlisted public if turnover >= 200 Cr, deposits >= 25 Cr, capital >= 50 Cr, or borrowings >= 100 Cr"
            ),
            commonExaminerPitfalls = "Looking at 3 preceding years for CSR threshold applicability instead of immediately preceding FY; confusing ongoing project unspent transfer deadlines."
        ),
        SyllabusTopic(
            id = "p2_ch4_share_capital",
            paper = PaperCode.PAPER_2,
            chapter = "Company Law",
            topicName = "Chapter 4: Share Capital and Debentures (Sec 43-72)",
            moduleName = "Company Law",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "11/12 Exams",
            averageMarks = 8.8,
            trendDirection = "High Yield Consistent Question",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "DVR 74% limit, Sweat Equity 15%/25%, Bonus sources every 3rd day",
            fusedWithTopics = listOf("Buyback Section 68"),
            keyProvisions = listOf(
                "Equity with Differential Voting Rights (DVR): Max 74% of total post-issue paid-up equity share capital; company must have no subsisting defaults in dividend or financial statements",
                "Sweat Equity Shares (Sec 54): Authorized by Special Resolution; locked in for 3 years; max 15% of paid-up equity in a year or ₹5 Crore, whichever is higher (lifetime limit 25%)",
                "Bonus Shares (Sec 63): Issued out of Free Reserves, Securities Premium, or CRR; CANNOT be issued in lieu of dividend or by capitalizing revaluation reserves",
                "Reduction of Share Capital (Sec 66): Requires Special Resolution and NCLT confirmation; creditors given notice to object"
            ),
            commonExaminerPitfalls = "Issuing bonus shares out of revaluation reserve; recommending bonus shares as a substitute for cash dividend."
        ),
        SyllabusTopic(
            id = "p2_sec186",
            paper = PaperCode.PAPER_2,
            chapter = "Company Law",
            topicName = "Section 186: Loans and Investments by Company",
            moduleName = "Company Law",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "8/12 Exams",
            averageMarks = 6.0,
            trendDirection = "High Application Frequency",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "60% Paid-up + Reserves vs 100% Reserves check",
            fusedWithTopics = listOf("Section 185"),
            keyProvisions = listOf(
                "Threshold: 60% of paid-up share capital + free reserves + securities premium OR 100% of free reserves + securities premium, whichever is higher",
                "Prior approval by Special Resolution if limits exceeded; unanimous Board approval at board meeting required for all transactions regardless of limit",
                "No loan can be given at an interest rate lower than prevailing yield of 1/3/5/10 year Govt Security closest to loan tenor",
                "Maintenance of Register of loans, guarantees, and securities in Form MBP-2"
            ),
            commonExaminerPitfalls = "Passing resolution by simple majority instead of unanimous board approval at board meeting; missing securities premium from the 60% base."
        ),

        // --- Tier 3: Medium Yield (Alternating & Specific Topics) ---
        SyllabusTopic(
            id = "p2_ch6_charges",
            paper = PaperCode.PAPER_2,
            chapter = "Company Law",
            topicName = "Chapter 6: Registration of Charges (Sec 77-87)",
            moduleName = "Company Law",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "3/6 Attempts",
            averageMarks = 8.3,
            trendDirection = "Alternating 8M Problem",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "CHG-1 (30 days + 30 days) & CHG-4 satisfaction deadlines",
            keyProvisions = listOf(
                "Creation / Modification of charge: Registration with ROC in Form CHG-1 (other than debentures) or CHG-9 within 30 days of creation",
                "Extension of time: ROC may allow filing within additional 30 days (total 60 days) on payment of additional fees, and further 60 days on ad valorem fees",
                "Satisfaction of charge (Sec 82): Company must intimate ROC in Form CHG-4 within 30 days of payment; ROC issues certificate in Form CHG-5",
                "Effect of non-registration under Sec 77: Void against liquidator and any creditor; however, validity of contract/obligation to repay money is NOT affected"
            ),
            commonExaminerPitfalls = "Assuming that an unregistered charge makes the loan void (the loan remains valid as an unsecured debt, but security is void against liquidator/creditors)."
        ),
        SyllabusTopic(
            id = "p2_ch11_foreign_companies",
            paper = PaperCode.PAPER_2,
            chapter = "Company Law",
            topicName = "Chapter 11: Companies Incorporated Outside India (Sec 379-393)",
            moduleName = "Company Law",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "5/6 Attempts",
            averageMarks = 6.0,
            trendDirection = "Steady 6M Question",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "FC-1 30 days filing & FC-3 accounts delivery within 6 months",
            keyProvisions = listOf(
                "Foreign Company definition under Sec 2(42): Incorporated outside India having place of business in India (physically or electronically) and conducts business activity in India",
                "Filing with ROC under Sec 380: Submit certified copy of charter, address of registered office, list of directors, and authorized person in India in Form FC-1 within 30 days",
                "Accounts of foreign company (Sec 381): Deliver balance sheet and P&L for Indian business operations along with list of places of business in Form FC-3 within 6 months of FY end",
                "Applicability of Indian Company Law provisions (Sec 379): If >= 50% paid-up capital held by Indian citizens/companies, Chapter XXII and other specified provisions apply as if domestic"
            ),
            commonExaminerPitfalls = "Assuming an overseas company without any physical or electronic place of business in India is a 'Foreign Company' under the Companies Act."
        ),
        SyllabusTopic(
            id = "p2_ch5_deposits",
            paper = PaperCode.PAPER_2,
            chapter = "Company Law",
            topicName = "Chapter 5: Acceptance of Deposits by Companies (Sec 73-76)",
            moduleName = "Company Law",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "5/6 Attempts",
            averageMarks = 5.4,
            trendDirection = "Steady 5-6M Problem",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "20% Deposit Repayment Reserve by 30th April & DPT-3 return by 30th June",
            keyProvisions = listOf(
                "Eligible Company under Sec 76 (accepting deposits from public): Public company with Net Worth >= ₹100 Crore OR Turnover >= ₹500 Crore with prior Special Resolution",
                "Deposit Repayment Reserve: Deposit at least 20% of deposits maturing in the following financial year in a scheduled bank by 30th April",
                "Ceiling limits: Member deposits max 35% of paid-up capital + free reserves + securities premium (eligible public company: 10% members + 25% public)",
                "Filing annual return of deposits in Form DPT-3 with ROC on or before 30th June every year"
            ),
            commonExaminerPitfalls = "Depositing the 20% reserve after 30th April; confusing the deposit limits for eligible companies (25% public + 10% members) with ordinary public companies."
        ),
        SyllabusTopic(
            id = "p2_ch8_dividend",
            paper = PaperCode.PAPER_2,
            chapter = "Company Law",
            topicName = "Chapter 8: Declaration and Payment of Dividend (Sec 123-127)",
            moduleName = "Company Law",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "5/6 Attempts",
            averageMarks = 5.0,
            trendDirection = "Steady 5M Question",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Rule 3 conditions for past reserve dividends & 30-day payment timeline",
            keyProvisions = listOf(
                "Sources of dividend: Current year profit after depreciation, past reserves, or money provided by Central/State Govt; transfer to reserves is voluntary",
                "Declaration from past reserves (Rule 3): Rate <= avg rate of last 3 years; total withdrawal <= 10% of paid-up capital + free reserves; balance reserves >= 15% of paid-up capital",
                "Deposit in separate bank account within 5 days of declaration; payment within 30 days; unpaid dividend transferred to Unpaid Dividend Account within 7 days thereafter",
                "Transfer to Investor Education and Protection Fund (IEPF): Any amount unpaid/unclaimed for 7 years transferred to IEPF along with corresponding shares"
            ),
            commonExaminerPitfalls = "Violating Rule 3 conditions when paying dividends out of accumulated reserves during loss years; failing to deposit dividend in scheduled bank within 5 days."
        ),

        // --- Tier 4: Low / Booster Chapters ---
        SyllabusTopic(
            id = "p2_ch3_prospectus",
            paper = PaperCode.PAPER_2,
            chapter = "Company Law",
            topicName = "Chapter 3: Prospectus and Allotment of Securities (Sec 23-42)",
            moduleName = "Company Law",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "2/6 Attempts",
            averageMarks = 10.0,
            trendDirection = "Sporadic High Mark when asked",
            writtenTarget = 4,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "Sec 42 private placement 200 person limit & PAS-3 return",
            keyProvisions = listOf(
                "Private Placement under Section 42: Offer made to max 200 persons in a FY (excluding QIBs & employees under ESOP); subscription money in separate bank account; return of allotment PAS-3 in 15 days",
                "Shelf Prospectus (Sec 31): Valid for up to 1 year; requires filing Information Memorandum in Form PAS-2 before subsequent offers",
                "Red Herring Prospectus (Sec 32): Omits complete particulars of quantum or price of securities; filed with ROC at least 3 days prior to opening of subscription",
                "Civil liability for misstatement in prospectus under Section 35 and criminal liability under Section 447 (fraud)"
            ),
            commonExaminerPitfalls = "Counting Qualified Institutional Buyers (QIBs) in the 200-person limit under Section 42; releasing subscription funds before filing return of allotment."
        ),
        SyllabusTopic(
            id = "p2_ch2_incorporation",
            paper = PaperCode.PAPER_2,
            chapter = "Company Law",
            topicName = "Chapter 2: Incorporation of Company & Matters Incidental (Sec 3-22)",
            moduleName = "Company Law",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "3/6 Attempts",
            averageMarks = 5.7,
            trendDirection = "Occasional Question",
            writtenTarget = 4,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "Ultra Vires void ab initio & Indoor Management exceptions",
            keyProvisions = listOf(
                "Memorandum of Association (MoA) clauses: Name, Registered Office (State), Object, Liability, Capital, and Association clauses (Sec 4)",
                "Doctrine of Ultra Vires: Acts beyond powers of company in MoA are wholly void and cannot be ratified even by unanimous consent of all shareholders",
                "Doctrine of Indoor Management (Turquand's Rule): Outsiders dealing in good faith entitled to assume internal regulations complied with; Exceptions: Knowledge of irregularity, Suspicion/Negligence, Forgery",
                "Registered Office verification: File Form INC-22 within 30 days of incorporation; alteration of registered office across state lines requires Central Govt (RD) approval (Sec 12 & 13)"
            ),
            commonExaminerPitfalls = "Claiming that an act ultra vires the Memorandum can be ratified by a Special Resolution (it cannot!); applying Indoor Management protection to forged documents."
        ),
        SyllabusTopic(
            id = "p2_ch1_preliminary",
            paper = PaperCode.PAPER_2,
            chapter = "Company Law",
            topicName = "Chapter 1: Preliminary (Definitions & Classifications, Sec 1-2)",
            moduleName = "Company Law",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "2/6 Attempts",
            averageMarks = 4.0,
            trendDirection = "Booster Definitions",
            writtenTarget = 3,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "Small Company ₹4 Cr capital & ₹40 Cr turnover test",
            keyProvisions = listOf(
                "Small Company threshold under Sec 2(85): Paid-up capital <= ₹4 Crore AND Turnover in immediately preceding FY <= ₹40 Crore",
                "Holding, Subsidiary & Associate Company under Sec 2(6) & 2(46): Control of > 50% voting power or composition of Board; Associate requires significant influence (>= 20% voting power/business decisions)",
                "Private Company under Sec 2(68): Restriction on transfer of shares, limits members to 200 (excluding current & former employees), prohibits public invitation",
                "Section 8 Company: Formed for charitable objects, applies profits in promoting objects, prohibits dividend payment; Central Govt license required"
            ),
            commonExaminerPitfalls = "Applying the Small Company test on current year turnover instead of preceding FY; counting employee-shareholders towards the 200-member limit for private companies."
        ),

        // =========================================================================
        // PAPER 3: Taxation (Direct Tax & GST) (ICAI CA Intermediate New Scheme)
        // 12-Attempt Trend Dec 2021 to May 2026 Strategy Blueprint
        // =========================================================================

        // --- Section A: Income Tax Law (Tier 1 MUST) ---
        SyllabusTopic(
            id = "p3_it_pgbp",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 5: Profits & Gains of Business or Profession (PGBP, Sec 28-44DB)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 14.0,
            trendDirection = "Compulsory Q1 Problem Anchor",
            writtenTarget = 12,
            mcqTarget = 40,
            oralRecallTarget = 5,
            oralRhythmNote = "Daily Sec 43B, Sec 40A(3), Sec 40(a)(ia) disallowances checklist",
            fusedWithTopics = listOf("Depreciation Sec 32", "TDS Sec 194C/J"),
            keyProvisions = listOf(
                "Depreciation (Sec 32): Block of assets method; if asset acquired and put to use for < 180 days in the year, claim 50% of normal depreciation rate; additional depreciation 20% on new plant & machinery",
                "Disallowances: Sec 40(a)(ia) 30% disallowance for non-deduction of TDS on resident payments; Sec 40A(3) 100% disallowance for cash payment > ₹10,000 in a day",
                "Section 43B statutory dues: Deductible only if paid on or before the due date of filing return under Sec 139(1); includes employer PF/ESI and interest to banks/NBFCs",
                "Presumptive Taxation: Sec 44AD (8% / 6% digital for turnover <= ₹2 Cr / ₹3 Cr); Sec 44ADA (50% for professionals receipts <= ₹50 Lakh / ₹75 Lakh); Sec 44AE (heavy goods carriage ₹1,000/ton/month)"
            ),
            commonExaminerPitfalls = "Allowing 100% depreciation when asset was put to use for less than 180 days; claiming employee PF contribution under Sec 43B (it is strictly governed by Sec 36(1)(va) due date!)."
        ),
        SyllabusTopic(
            id = "p3_it_capital_gains",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 6: Capital Gains (Sec 45-55)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 10.0,
            trendDirection = "Consistent 10M Section",
            writtenTarget = 10,
            mcqTarget = 35,
            oralRecallTarget = 5,
            oralRhythmNote = "Sec 50C 110% safe harbour & Sec 54/54EC/54F reinvestment rules every 2nd day",
            fusedWithTopics = listOf("Income from Other Sources", "PGBP"),
            keyProvisions = listOf(
                "Holding Period for Long-Term Asset: Listed equity/equity mutual funds > 12 months; Unlisted equity shares & Immovable property (land/building) > 24 months; Other assets > 36 months",
                "Full Value of Consideration for Real Estate (Sec 50C): If Stamp Duty Value exceeds 110% of declared consideration, Stamp Duty Value adopted as consideration",
                "Exemptions: Sec 54 (investment in residential house up to ₹10 Cr), Sec 54EC (NHAI/REC/PFC bonds max ₹50 Lakhs within 6 months), Sec 54F (long-term asset into residential house)",
                "Tax rates on Capital Gains: Sec 111A (STCG on listed equity subject to STT @ 15%); Sec 112A (LTCG on listed equity > ₹1 Lakh @ 10% without indexation); Sec 112 (other LTCG @ 20%)"
            ),
            commonExaminerPitfalls = "Applying cost indexation to long-term capital gains on listed equity shares under Sec 112A (indexation is prohibited!); forgetting the 110% safe harbour in Sec 50C."
        ),
        SyllabusTopic(
            id = "p3_it_total_income",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Computation of Total Income & Tax Payable (Comprehensive)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 14.0,
            trendDirection = "Compulsory 14M Comprehensive Problem",
            writtenTarget = 12,
            mcqTarget = 40,
            oralRecallTarget = 5,
            oralRhythmNote = "Full tax computation with Sec 115BAC vs Old Regime daily",
            fusedWithTopics = listOf("PGBP", "Salaries", "Chapter VI-A Deductions"),
            keyProvisions = listOf(
                "Aggregation of all 5 heads of income after inter-source and inter-head set-off of losses",
                "Gross Total Income minus Chapter VI-A deductions permissible under default Sec 115BAC (80CCD(2) & 80JJAA)",
                "Application of Sec 115BAC tax slabs, 87A rebate (up to ₹25,000 if TI <= ₹7,00,000), and marginal relief",
                "Addition of Surcharge (max 25% under 115BAC) and mandatory 4% Health & Education Cess"
            ),
            commonExaminerPitfalls = "Allowing 80C or 80D deductions under default Sec 115BAC; omitting 87A rebate when taxable income is <= ₹7,00,000."
        ),

        // --- Section A: Income Tax Law (Tier 2 High Yield) ---
        SyllabusTopic(
            id = "p3_it_residential_status",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 2: Residence and Scope of Total Income (Sec 6 & Sec 5)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "10/12 Exams",
            averageMarks = 6.5,
            trendDirection = "61M/12 Exams • High Yield",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 4,
            oralRhythmNote = "Oral rhythm every 3rd day on ROR/RNOR tests & deemed resident 6(1A)",
            fusedWithTopics = listOf("Scope of Total Income"),
            keyProvisions = listOf(
                "Basic conditions for Individual (Sec 6(1)): (a) Stay >= 182 days in current FY, OR (b) Stay >= 60 days in current FY and >= 365 days in 4 preceding FYs",
                "Exceptions to 60-day rule: Indian citizen leaving for employment or crew member, or Indian citizen/PIO visiting India with income <= 15L -> 182 days applies (120 days if income > 15L)",
                "Deemed Resident (Sec 6(1A)): Indian citizen with total income > ₹15 Lakhs (other than foreign sources) not liable to tax in any other country -> Deemed RNOR",
                "Scope of Total Income (Sec 5): ROR taxed on global income; RNOR taxed on Indian income + foreign business controlled from India; NR taxed on Indian income only"
            ),
            commonExaminerPitfalls = "Taxing foreign income of an RNOR when the foreign business is not controlled from India; miscounting departure and arrival days (both are counted in India!)."
        ),
        SyllabusTopic(
            id = "p3_it_salaries",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 3: Income from Salaries (Sec 15-17)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "9/12 Exams",
            averageMarks = 7.0,
            trendDirection = "51M/12 Exams • High Yield",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Rule 3 perquisite checklist every 3rd day (Car, Accommodation, Gratuity)",
            fusedWithTopics = listOf("House Property", "115BAC"),
            keyProvisions = listOf(
                "Standard deduction under Sec 16(ia): ₹50,000 (or ₹75,000 under amended 115BAC) or actual salary, whichever is less",
                "House Rent Allowance (Sec 10(13A)): Exempt least of: (a) Actual HRA received, (b) Rent paid minus 10% of salary, (c) 50% (metro) / 40% (non-metro) of salary (NOT available under 115BAC)",
                "Gratuity (Sec 10(10)): For employees covered under Payment of Gratuity Act: exempt least of (a) Actual gratuity, (b) ₹20,00,000, (c) 15/26 * Last drawn salary * Completed years of service",
                "Leave salary encashment (Sec 10(10AA)): Exemption limit for non-government employees increased to ₹25,00,000"
            ),
            commonExaminerPitfalls = "Allowing HRA or professional tax deductions under default Sec 115BAC (they are disallowed!); using 15/30 instead of 15/26 for Gratuity Act covered employees."
        ),
        SyllabusTopic(
            id = "p3_tds_194c_194j",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 11: TDS & TCS Provisions (Sec 192-206C)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "10/12 Exams",
            averageMarks = 8.0,
            trendDirection = "High Yield MCQ & Practical Problem",
            writtenTarget = 6,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Rates & thresholds table (194C, 194J, 194Q, 206C) every 2nd day",
            fusedWithTopics = listOf("PGBP Section 40(a)(ia)"),
            keyProvisions = listOf(
                "Sec 194C (Contractors): Rate 1% (Ind/HUF), 2% (others); Threshold: single contract > ₹30,000 or aggregate > ₹1,00,000 per FY",
                "Sec 194J (Professionals/Royalty): Rate 10% (professional/royalty) and 2% (technical services / call centre); Threshold: ₹30,000 per category per FY",
                "Sec 194Q (Purchase of goods): TDS @ 0.1% if buyer turnover > ₹10 Cr and purchases from seller exceed ₹50 Lakhs in FY; Sec 206C(1H) TCS applies if 194Q not applicable",
                "Sec 206AA: If payee does not furnish PAN, deduct TDS at higher of rate in force or 20% (5% under 194Q/206C(1H))"
            ),
            commonExaminerPitfalls = "Deducting 10% on technical services instead of amended 2% under Sec 194J; applying TCS under 206C(1H) when TDS under 194Q already applies."
        ),
        SyllabusTopic(
            id = "p3_it_deductions_via",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 10: Deductions from Gross Total Income (Chapter VI-A)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "10/12 Exams",
            averageMarks = 8.0,
            trendDirection = "Core Deduction Component",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "80CCD(2) & 80JJAA allowed in 115BAC checklist",
            fusedWithTopics = listOf("Total Income Computation"),
            keyProvisions = listOf(
                "Sec 80C: Max ₹1,50,000 for PPF, EPF, ELSS, Life Insurance premium, tuition fees, principal repayment of housing loan (disallowed under 115BAC)",
                "Sec 80CCD(2): Employer contribution to NPS up to 14% (Central/State Govt) or 10% (others) of salary — ALLOWED even under default Sec 115BAC",
                "Sec 80D: Health insurance premium max ₹25,00,000 (self/family) + ₹25,000 / ₹50,000 (senior citizen parents); preventive checkup max ₹5,000",
                "Sec 80JJAA: 30% of additional employee cost allowed for 3 assessment years for new regular employees — ALLOWED under Sec 115BAC"
            ),
            commonExaminerPitfalls = "Claiming 80C, 80D, or 80TTA under the default Sec 115BAC regime (only 80CCD(2) and 80JJAA are allowed under 115BAC!)."
        ),

        // --- Section A: Income Tax Law (Tier 3 Steady) ---
        SyllabusTopic(
            id = "p3_it_house_property",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 4: Income from House Property (Sec 22-27)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "8/12 Exams",
            averageMarks = 5.0,
            trendDirection = "36M/12 Exams • Often Fused with Clubbing",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "GAV computation & SOP interest disallowance in 115BAC",
            fusedWithTopics = listOf("Clubbing of Income"),
            keyProvisions = listOf(
                "Gross Annual Value (GAV): Expected Rent = Higher of Municipal Value and Fair Rent (restricted to Standard Rent); GAV = Higher of Expected Rent and Actual Rent received/receivable",
                "Deductions from NAV (Sec 24): (a) Standard deduction 30% of NAV (Sec 24(a)), (b) Interest on borrowed capital (Sec 24(b))",
                "Interest on borrowed capital limits for Self-Occupied Property: Max ₹2,00,000 for acquisition/construction within 5 years; ₹30,000 for repair (NOTE: Disallowed for SOP under Sec 115BAC)",
                "Unrealized rent and Arrears of rent received: Taxed in the year of receipt under Sec 25A after granting flat 30% statutory deduction"
            ),
            commonExaminerPitfalls = "Deducting municipal taxes unpaid by owner; allowing interest deduction on self-occupied property under default regime Sec 115BAC."
        ),
        SyllabusTopic(
            id = "p3_it_other_sources",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 7: Income from Other Sources (Sec 56-59)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "7/12 Exams",
            averageMarks = 5.0,
            trendDirection = "Gift Taxation & Casual Winnings",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Sec 56(2)(x) ₹50,000 threshold & relative definition",
            fusedWithTopics = listOf("Capital Gains"),
            keyProvisions = listOf(
                "Taxation of Gifts (Sec 56(2)(x)): Sum of money > ₹50,000 aggregate per FY; immovable property without consideration where stamp value > 50K or inadequate if diff > 50K and 10% of consideration",
                "Exempt gifts: Received from relatives (spouse, brother/sister, lineal ascendant/descendant), on marriage, under will/inheritance, or from local authority/registered trust",
                "Casual Winnings (Sec 115BB): Lottery, crossword puzzle, gambling, betting taxed at flat 30% without basic exemption or deductions under Chapter VI-A",
                "Family Pension deduction under Sec 57: Lower of 1/3rd of pension or ₹15,000 (increased to ₹25,000 under amended 115BAC)"
            ),
            commonExaminerPitfalls = "Setting off losses against lottery winnings; assuming gifts from cousins are exempt (cousins are NOT 'relatives' under Section 56(2)(x)!)."
        ),
        SyllabusTopic(
            id = "p3_it_clubbing",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 8: Clubbing of Income (Sec 60-65)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "8/12 Exams",
            averageMarks = 5.0,
            trendDirection = "Spouse & Minor Child Clubbing",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Minor child income clubbed with higher earning parent",
            fusedWithTopics = listOf("House Property"),
            keyProvisions = listOf(
                "Transfer of income without transfer of asset (Sec 60): Income clubbed in hands of transferor whether revocable or irrevocable",
                "Remuneration to spouse (Sec 64(1)(ii)): From concern where individual has substantial interest clubbed with individual, unless remuneration is solely attributable to technical/professional qualification",
                "Income from asset transferred to spouse / son's wife without adequate consideration clubbed with transferor (Sec 64(1)(iv) & (vi))",
                "Minor child income (Sec 64(1A)): Clubbed in hands of parent whose total income is higher; exemption ₹1,500 per minor child under Sec 10(32) (old regime only)"
            ),
            commonExaminerPitfalls = "Clubbing minor child income earned through specialized talent or manual work; clubbing income from accretion of transferred assets."
        ),
        SyllabusTopic(
            id = "p3_it_setoff_losses",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 9: Aggregation of Income & Set-Off / Carry Forward of Losses",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "8/12 Exams",
            averageMarks = 6.0,
            trendDirection = "Set-off & Carry Forward Restrictions",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Business loss CANNOT set off against salary; 8-year limit",
            fusedWithTopics = listOf("Total Income Computation"),
            keyProvisions = listOf(
                "Inter-source set-off (Sec 70): Loss from one source set off against income from another source under same head; Exceptions: Speculative loss, Specified business (35AD), Long-term capital loss",
                "Inter-head set-off (Sec 71): House property loss set off against any other head max ₹2,00,000; Business loss CANNOT be set off against Salary income; Capital losses cannot be set off against other heads",
                "Carry forward rules: House property loss (8 years), Business loss (8 years, mandatory return under 139(3)), Speculative loss (4 years), Capital loss (8 years)",
                "Unabsorbed depreciation (Sec 32(2)): Can be carried forward indefinitely and set off against ANY income (except salary) in subsequent years"
            ),
            commonExaminerPitfalls = "Setting off business losses against Salary income; carrying forward business loss when income tax return was filed after the due date under Sec 139(1)."
        ),
        SyllabusTopic(
            id = "p3_it_advance_tax",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 12: Advance Tax & Interest (Sec 207-211, Sec 234A/B/C)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "7/12 Exams",
            averageMarks = 5.0,
            trendDirection = "Practical Computation of Interest",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "15-45-75-100% instalment dates & 234B/C interest calculation",
            keyProvisions = listOf(
                "Advance Tax liability under Sec 208: Arises if estimated tax liability (after TDS/TCS) is >= ₹10,000; Senior citizen resident with no business income exempt",
                "Instalments: 15% by 15th June, 45% by 15th September, 75% by 15th December, 100% by 15th March (Presumptive 44AD/44ADA pay 100% by 15th March)",
                "Sec 234B Interest: 1% per month for default/shortfall if advance tax paid is < 90% of assessed tax",
                "Sec 234C Interest: 1% per month for deferment of individual instalments (e.g., shortfall from 12% in June, 36% in Sept, 75% in Dec, 100% in March)"
            ),
            commonExaminerPitfalls = "Charging 234C interest on capital gains or casual winnings when advance tax paid in remaining instalments after occurrence of income."
        ),
        SyllabusTopic(
            id = "p3_it_returns",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 13: Filing Return of Income & Self-Assessment (Sec 139-140A)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "7/12 Exams",
            averageMarks = 4.0,
            trendDirection = "Due Dates & Penalties",
            writtenTarget = 4,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "Due dates: 31 July (Non-audit), 31 Oct (Audit), 31 Dec (Belated/Revised)",
            keyProvisions = listOf(
                "Due dates under Sec 139(1): 31st July (non-audit individuals/HUFs), 31st October (companies and assessees subject to tax audit), 30th November (transfer pricing)",
                "Belated return (Sec 139(4)) & Revised return (Sec 139(5)): Can be filed up to 31st December of the relevant Assessment Year",
                "Late filing fee under Sec 234F: ₹5,000 (reduced to ₹1,000 if total income does not exceed ₹5,00,000)",
                "Updated Return under Sec 139(8A): Can be filed within 24 months from end of relevant AY on payment of 25% (first 12m) or 50% (next 12m) additional tax"
            ),
            commonExaminerPitfalls = "Attempting to file a revised return or claim loss carry-forward after 31st December of the assessment year."
        ),
        SyllabusTopic(
            id = "p3_it_basic_rates",
            paper = PaperCode.PAPER_3,
            chapter = "Direct Tax",
            topicName = "Chapter 1: Basic Concepts & Tax Rates (Default Sec 115BAC vs Old Regime)",
            moduleName = "Direct Tax",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "4/12 Exams",
            averageMarks = 3.5,
            trendDirection = "13M/12 Exams • Rates & Rebate Booster",
            writtenTarget = 3,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "115BAC slab rates (0-3-6-9-12-15L) & 4% cess",
            keyProvisions = listOf(
                "Default New Tax Regime (Sec 115BAC): Slabs: 0-3L Nil, 3-6L 5%, 6-9L 10%, 9-12L 15%, 12-15L 20%, > 15L 30%",
                "Rebate under Sec 87A: Under Sec 115BAC, rebate up to ₹25,00,00 if Total Income <= ₹7,00,000 (tax liability is NIL up to 7 Lakhs); marginal relief applies if TI slightly exceeds 7L",
                "Health & Education Cess @ 4% on aggregate of income tax and surcharge across all assessees",
                "Surcharge under Sec 115BAC capped at max 25% (for TI > 2 Crore); old regime surcharge goes up to 37% for TI > 5 Crore"
            ),
            commonExaminerPitfalls = "Calculating tax under old slab rates without noting that Sec 115BAC is the statutory default regime; omitting 87A rebate for income up to ₹7 Lakhs."
        ),

        // --- Section B: Goods and Services Tax (Tier 1 MUST) ---
        SyllabusTopic(
            id = "p3_gst_itc_17_5",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 8: Input Tax Credit & Blocked Credits (Sec 16-18, Sec 17(5))",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 12.0,
            trendDirection = "Compulsory Q1 Core Pillar",
            writtenTarget = 10,
            mcqTarget = 40,
            oralRecallTarget = 5,
            oralRhythmNote = "Daily Section 17(5) blocked credit clauses checklist",
            fusedWithTopics = listOf("Payment of Tax", "Computation of GST"),
            keyProvisions = listOf(
                "Sec 16 Eligibility: 4 core conditions (possession of tax invoice, receipt of goods/services, tax paid to govt by supplier, return filed under Sec 39); 180-day rule to pay supplier else ITC reversed with interest",
                "Sec 17(5) Motor Vehicles: Blocked for passenger vehicles with seating capacity <= 13 persons unless used for further supply of vehicles, passenger transportation, or driving school",
                "Sec 17(5) Other Blocked Credits: Food, beverages, outdoor catering, health club memberships (unless statutory obligation), goods lost/stolen/destroyed/written off/given as gifts or free samples",
                "Sec 17(5) Works contract: For construction of immovable property capitalized to asset account is BLOCKED (allowed if for plant and machinery)"
            ),
            commonExaminerPitfalls = "Claiming ITC on goods distributed as free samples or gifts; claiming ITC on factory civil construction works capitalized to building."
        ),
        SyllabusTopic(
            id = "p3_gst_value_of_supply",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 7: Value of Supply & GST Computation (Section 15)",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 14.0,
            trendDirection = "Compulsory 10-14M Computation Problem",
            writtenTarget = 10,
            mcqTarget = 40,
            oralRecallTarget = 5,
            oralRhythmNote = "Sec 15(2) mandatory inclusions & pre/post discounts exclusions daily",
            fusedWithTopics = listOf("ITC Sec 16", "Time of Supply"),
            keyProvisions = listOf(
                "Transaction value under Sec 15(1): Price actually paid or payable when supplier and recipient are not related and price is the sole consideration",
                "Mandatory inclusions under Sec 15(2): Any taxes other than GST, incidental expenses (packing, inspection, freight) charged by supplier, interest or late fee for delayed payment, non-govt subsidies",
                "Exclusion of discounts under Sec 15(3): Pre-supply discount recorded on invoice; Post-supply discount excluded only if agreed beforehand, linked to invoices, and recipient reverses ITC",
                "Subsidies provided by Central Government or State Government are EXCLUDED from transaction value"
            ),
            commonExaminerPitfalls = "Excluding post-supply turnover discounts when the recipient has failed to reverse proportionate input tax credit."
        ),

        // --- Section B: Goods and Services Tax (Tier 2 High Yield) ---
        SyllabusTopic(
            id = "p3_gst_rcm",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 3: Charge of GST & Reverse Charge (Sec 9 & 10 Composition Levy)",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "10/12 Exams",
            averageMarks = 8.0,
            trendDirection = "High Yield Practical Problem",
            writtenTarget = 6,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Sec 9(3) RCM notifications & Sec 10 Composition limits every 3rd day",
            fusedWithTopics = listOf("Payment of Tax"),
            keyProvisions = listOf(
                "Sec 9(3) Reverse Charge Mechanism (RCM): GTA services (5% without ITC or 12% forward charge with ITC), Legal services by advocates, Sponsorship to body corporate/firm, Director services",
                "Payment of RCM liability: MUST be paid in CASH only via Electronic Cash Ledger (Electronic Credit Ledger CANNOT be used for RCM)",
                "Sec 10 Composition Scheme: Threshold ₹1.5 Crore (₹75 Lakhs in special states); Rates: 1% for manufacturers/traders, 5% for restaurants; cannot issue tax invoice, cannot collect tax, cannot claim ITC",
                "Sec 10(2A) Composition for Service Providers: Threshold turnover up to ₹50 Lakhs in preceding FY; pays tax @ 6% (3% CGST + 3% SGST)"
            ),
            commonExaminerPitfalls = "Using ITC to pay GST under Reverse Charge Mechanism; allowing a composition dealer to issue a Tax Invoice or claim ITC."
        ),
        SyllabusTopic(
            id = "p3_gst_time_of_supply",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 6: Time of Supply (Sec 12 & 13)",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "10/12 Exams",
            averageMarks = 8.0,
            trendDirection = "High Yield Computational Problem",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 4,
            oralRhythmNote = "Goods advance exemption vs Services advance taxation every 3rd day",
            fusedWithTopics = listOf("Invoicing Sec 31"),
            keyProvisions = listOf(
                "Time of supply of Goods (Sec 12): Earlier of date of issue of invoice (or last date invoice required to be issued under Sec 31) or date of payment (Notification 66/2017 exempts advances on goods)",
                "Time of supply of Services (Sec 13): If invoice issued within 30 days -> earlier of date of invoice or date of payment (advances on services ARE taxable upon receipt!)",
                "If invoice NOT issued within 30 days for services: Earlier of date of completion of service or date of payment",
                "Time of supply under RCM: Goods -> earlier of date of receipt of goods, 30 days from invoice, date of payment; Services -> earlier of 60 days from invoice or date of payment"
            ),
            commonExaminerPitfalls = "Applying advance exemption to services (advances for services are always subject to GST upon receipt!)."
        ),
        SyllabusTopic(
            id = "p3_gst_place_of_supply",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 4: Place of Supply (IGST Act Sec 10-14)",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "10/12 Exams",
            averageMarks = 8.0,
            trendDirection = "Inter vs Intra State Determination",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 4,
            oralRhythmNote = "Bill-to-Ship-to & immovable property place of supply rules",
            fusedWithTopics = listOf("Charge of GST"),
            keyProvisions = listOf(
                "Determination of Inter-State vs Intra-State: Location of Supplier and Place of Supply in same State -> Intra-State; different States -> Inter-State (IGST)",
                "Place of supply of Goods (Sec 10): Where supply involves movement -> location of goods at time delivery terminates; Bill-to-Ship-to -> principal place of business of third party",
                "Place of supply of Services (Sec 12): Immovable property related services -> location of property; Restaurant/catering/fitness -> location where service actually performed",
                "General residual rule for services (Sec 12(2)): Made to registered person -> location of recipient; made to unregistered person -> location of recipient where address on record exists"
            ),
            commonExaminerPitfalls = "Charging CGST + SGST on Bill-to Ship-to transactions where billing party is in a different state from shipping party."
        ),
        SyllabusTopic(
            id = "p3_gst_exemptions",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 5: Exemptions from GST (Sec 11 CGST / Sec 6 IGST)",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "9/12 Exams",
            averageMarks = 6.0,
            trendDirection = "Crucial for Value calculation",
            writtenTarget = 5,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Health, education & agricultural service exemptions checklist",
            fusedWithTopics = listOf("Value of Supply"),
            keyProvisions = listOf(
                "Health care services: Clinical establishments, authorized medical practitioners, and paramedics are EXEMPT (except hair transplant, cosmetic surgery, hospital room rent > ₹5,000/day)",
                "Educational services: Services by educational institution to students/staff are EXEMPT; services provided TO institution exempt only for transport, catering, security/cleaning up to higher secondary",
                "Agricultural services: Cultivation, harvesting, threshing, supply of farm labour, warehousing of agricultural produce are EXEMPT",
                "Passenger transport exemptions: Metro, monorail, tramway, non-AC stage carriage, auto-rickshaws (except when booked via e-commerce operator)"
            ),
            commonExaminerPitfalls = "Treating coaching institutions preparing students for competitive exams as exempt educational institutions (they are fully taxable!)."
        ),

        // --- Section B: Goods and Services Tax (Tier 3 Steady & Tier 4) ---
        SyllabusTopic(
            id = "p3_gst_supply",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 2: Supply under GST (Section 7 & 8)",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "8/12 Exams",
            averageMarks = 6.0,
            trendDirection = "Composite vs Mixed Supply Problems",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Schedule I without consideration & Schedule III negative list",
            fusedWithTopics = listOf("Value of Supply"),
            keyProvisions = listOf(
                "Section 7(1)(a): All forms of supply of goods/services made or agreed to be made for consideration by a person in course or furtherance of business",
                "Schedule I (Activities without consideration): Permanent transfer of business assets with ITC availed, supplies between related persons/distinct persons, principal-agent transactions",
                "Schedule III (Negative List): Employee to employer in relation to employment, court/tribunal services, funeral services, sale of land and completed building, actionable claims other than lottery/gambling",
                "Section 8: Composite Supply (two or more bundled supplies where one is principal supply -> taxed at principal supply rate) vs Mixed Supply (independent items for single price -> taxed at highest rate)"
            ),
            commonExaminerPitfalls = "Treating a canned gift hamper (mixed supply) as a composite supply; treating transfer between branches with same PAN in different states as not a supply."
        ),
        SyllabusTopic(
            id = "p3_gst_registration",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 9: Registration under GST (Sec 22-30)",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "7/12 Exams",
            averageMarks = 5.0,
            trendDirection = "Threshold Limits & Section 24",
            writtenTarget = 4,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Thresholds: Goods ₹40L / Services ₹20L & Section 24 compulsory cases",
            keyProvisions = listOf(
                "Threshold under Sec 22: Exclusive supply of goods ₹40 Lakhs (normal states) / ₹20 Lakhs (special states); Supply of services ₹20 Lakhs (normal states) / ₹10 Lakhs (special states)",
                "Compulsory Registration under Sec 24: Persons making inter-state taxable supply of goods, Casual Taxable Persons (CTP), persons liable to pay RCM, non-resident taxable persons, e-commerce operators",
                "Effective date of registration: If application submitted within 30 days of becoming liable -> date on which liability arose; if submitted after 30 days -> date of grant of registration",
                "Aadhaar authentication: If authenticated, registration approved within 7 working days; if biometric required or not authenticated, site verification and approval within 30 days"
            ),
            commonExaminerPitfalls = "Claiming ₹40 Lakh threshold for service providers (threshold for services is strictly ₹20 Lakhs!); missing compulsory registration for inter-state supplies of goods."
        ),
        SyllabusTopic(
            id = "p3_gst_invoicing_eway",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 10: Tax Invoice, Credit & Debit Notes & E-Way Bill (Sec 31-34, Rule 138)",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "8/12 Exams",
            averageMarks = 6.0,
            trendDirection = "E-Invoicing & E-Way Bill Rules",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "E-Invoicing ₹5 Cr threshold & E-Way Bill 200 km/day validity",
            keyProvisions = listOf(
                "Time limit for invoice (Sec 31): Goods -> on or before removal/delivery; Services -> within 30 days from provision of service (45 days for banking/financial institutions)",
                "E-Invoicing: Mandatory for registered persons whose aggregate turnover exceeds ₹5 Crore in any preceding financial year (generates IRN & QR code)",
                "Credit Note (Sec 34): Issued by supplier to reduce tax liability up to 30th November following the end of financial year or date of furnishing annual return, whichever is earlier",
                "E-Way Bill (Rule 138): Mandatory for movement of goods of consignment value > ₹50,000; Validity: 1 day per 200 km for regular cargo (1 day per 20 km for over-dimensional cargo)"
            ),
            commonExaminerPitfalls = "Issuing credit notes after 30th November of subsequent FY to reduce GST liability; calculating E-way bill validity on outdated 100 km/day rule (it is 200 km!)."
        ),
        SyllabusTopic(
            id = "p3_gst_payment_of_tax",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 12: Payment of Tax & Electronic Ledgers (Sec 49-50)",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "8/12 Exams",
            averageMarks = 5.0,
            trendDirection = "Order of ITC Utilization",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "IGST exhausted first; CGST-SGST cross-utilization strictly forbidden",
            fusedWithTopics = listOf("ITC Sec 16"),
            keyProvisions = listOf(
                "Three Electronic Ledgers: Electronic Cash Ledger (cash deposits via challan CPIN valid 15 days), Electronic Credit Ledger (ITC availed), Electronic Liability Register",
                "Order of ITC utilization: IGST credit exhausted FIRST against IGST, then CGST and SGST/UTGST in any proportion; CGST credit against CGST then IGST; SGST against SGST then IGST",
                "Strict rule: Cross-utilization of CGST credit against SGST liability and SGST credit against CGST liability is IMPOSSIBLE and strictly prohibited",
                "Interest under Sec 50: 18% per annum on net tax liability paid belatedly; 24% per annum where undue or excess ITC was availed and utilized"
            ),
            commonExaminerPitfalls = "Attempting to offset CGST credit against SGST; charging 18% interest on gross tax when ITC was available and delay was only in filing return."
        ),
        SyllabusTopic(
            id = "p3_gst_accounts_records",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 11: Accounts & Records under GST (Sec 35 & 36)",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "4/12 Exams",
            averageMarks = 4.0,
            trendDirection = "Retention Period Rules",
            writtenTarget = 3,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "72 months retention from due date of Annual Return",
            keyProvisions = listOf(
                "Mandatory records under Sec 35: Production/manufacture of goods, inward and outward supply, stock of goods, input tax credit availed, output tax payable and paid",
                "Maintenance of electronic records: Back-up must be maintained, passwords and access shared with tax authorities on demand",
                "Retention Period under Sec 36: At least 72 months (6 years) from the due date of furnishing annual return for the year pertaining to such accounts",
                "Pending appeals/revisions: Retain records for 1 year after final disposal of appeal or for 72 months from annual return due date, whichever is later"
            ),
            commonExaminerPitfalls = "Calculating 72 months from the end of the financial year instead of from the due date of the annual return (31st December)."
        ),
        SyllabusTopic(
            id = "p3_gst_tds_tcs",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 13: TDS & TCS under GST (Sec 51 & 52)",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "4/12 Exams",
            averageMarks = 3.5,
            trendDirection = "Government Contracts & E-Commerce",
            writtenTarget = 3,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "2% GST TDS on contracts > ₹2.5 Lakhs & 1% TCS by ECO",
            keyProvisions = listOf(
                "Sec 51 TDS: Deductors include Central/State Govt departments, local authorities, governmental agencies; deduct TDS @ 2% (1% CGST + 1% SGST or 2% IGST)",
                "TDS Threshold: Total value of supply under a single contract exceeds ₹2,50,000 (excluding taxes); no deduction if location of supplier and place of supply are in State A and recipient in State B",
                "Sec 52 TCS: Electronic Commerce Operators collect TCS @ 1% (0.5% CGST + 0.5% SGST or 1% IGST) on net value of taxable supplies made through the portal",
                "Remittance & Returns: TDS/TCS deposited to government within 10 days after the end of month in Form GSTR-7 / GSTR-8"
            ),
            commonExaminerPitfalls = "Deducting GST TDS when individual invoices are under ₹2.5 Lakhs but aggregate contract value exceeds ₹2,50,000 (the test is on contract value, not per invoice!)."
        ),
        SyllabusTopic(
            id = "p3_gst_returns",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 14: Returns under GST (Sec 37-48, QRMP Scheme)",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "6/12 Exams",
            averageMarks = 4.0,
            trendDirection = "QRMP & GSTR-9 / 9C Rules",
            writtenTarget = 4,
            mcqTarget = 20,
            oralRecallTarget = 2,
            oralRhythmNote = "GSTR-1, GSTR-3B due dates & GSTR-9 ₹2 Cr threshold",
            keyProvisions = listOf(
                "GSTR-1 (Outward Supplies): Monthly by 11th of succeeding month; quarterly under QRMP scheme by 13th (with optional Invoice Furnishing Facility IFF for first 2 months)",
                "GSTR-3B (Summary Return): Monthly by 20th; QRMP quarterly filers by 22nd / 24th; taxes paid via Fixed Sum Method (35% challan) or Self-Assessment Method",
                "GSTR-9 Annual Return: Due by 31st December following FY end; mandatory for taxpayers with aggregate turnover > ₹2 Crore (exempt for <= 2 Cr)",
                "GSTR-9C Reconciliation Statement: Self-certified reconciliation statement mandatory for taxpayers with aggregate turnover exceeding ₹5 Crore"
            ),
            commonExaminerPitfalls = "Filing GSTR-3B before GSTR-1; mandating GSTR-9C reconciliation audit by a Chartered Accountant (it is now self-certified by management!)."
        ),
        SyllabusTopic(
            id = "p3_gst_intro",
            paper = PaperCode.PAPER_3,
            chapter = "GST",
            topicName = "Chapter 1: GST in India - An Introduction & Constitutional Framework",
            moduleName = "GST",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "3/12 Exams",
            averageMarks = 3.0,
            trendDirection = "Constitutional Basics Booster",
            writtenTarget = 3,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "Article 246A, 269A, 279A & taxes subsumed",
            keyProvisions = listOf(
                "Dual GST structure: CGST + SGST/UTGST for intra-state supplies; IGST for inter-state supplies (administered under IGST Act 2017)",
                "Constitutional provisions: Article 246A (simultaneous power to Parliament and State Legislatures), Article 269A (levy & collection of IGST), Article 279A (GST Council)",
                "Items outside GST: Alcoholic liquor for human consumption (State Excise + VAT); 5 petroleum products (crude, motor spirit/petrol, HSD, ATF, natural gas) temporarily outside",
                "Taxes subsumed: Central Excise, Service Tax, State VAT, Entry Tax, Luxury Tax, Entertainment Tax (except local body taxes)"
            ),
            commonExaminerPitfalls = "Stating that petroleum products are permanently exempt (they are taxable from a date to be notified by GST Council!)."
        ),

        // =========================================================================
        // PAPER 4: Cost and Management Accounting (ICAI CA Intermediate New Scheme)
        // 9-Attempt Trend May 2023 to May 2026 Strategy Blueprint
        // =========================================================================

        // --- Tier 1 CORE (9/9 Attempts, 60%+ Marks, 4-5x Oral Recall) ---
        SyllabusTopic(
            id = "p4_marginal_costing",
            paper = PaperCode.PAPER_4,
            chapter = "Marginal Costing",
            topicName = "Chapter 14: Marginal Costing & Decision Making",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "9/9 Attempts",
            averageMarks = 12.0,
            trendDirection = "Compulsory Q1 Anchor • 9/9 Attempts",
            writtenTarget = 10,
            mcqTarget = 40,
            oralRecallTarget = 5,
            oralRhythmNote = "P/V ratio, BEP, MOS & Contribution per Key Factor daily",
            fusedWithTopics = listOf("Standard Costing", "Budgets"),
            keyProvisions = listOf(
                "Marginal Cost Equation: Sales - Variable Cost = Contribution = Fixed Cost + Profit",
                "P/V Ratio = (Contribution / Sales) * 100 = (Change in Profit / Change in Sales) * 100",
                "Break-Even Point (BEP): In Units = Fixed Cost / Contribution per unit; In Value = Fixed Cost / P/V Ratio",
                "Margin of Safety (MOS) = Actual Sales - Break-Even Sales = Profit / P/V Ratio; Total Sales = BEP Sales + Margin of Safety",
                "Key / Limiting Factor Decision: Rank products based on Contribution per unit of Limiting Factor (e.g., Contribution / Machine Hour)"
            ),
            commonExaminerPitfalls = "Treating fixed cost as variable per unit when volume changes; failing to rank products by contribution per key factor unit."
        ),
        SyllabusTopic(
            id = "p4_standard_costing",
            paper = PaperCode.PAPER_4,
            chapter = "Standard Costing",
            topicName = "Chapter 13: Standard Costing & Variance Analysis",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "9/9 Attempts",
            averageMarks = 10.0,
            trendDirection = "Core 10M Variance Problem • 9/9 Attempts",
            writtenTarget = 10,
            mcqTarget = 35,
            oralRecallTarget = 5,
            oralRhythmNote = "Material & Labour variance tree daily (MCV, MPV, MUV, LCV, LRateV, LEffV)",
            fusedWithTopics = listOf("Overhead Variances"),
            keyProvisions = listOf(
                "Material Variances: MCV = (SQ * SP) - (AQ * AP); MPV = AQ * (SP - AP); MUV = SP * (SQ - AQ); MMV = SP * (RSQ - AQ); MYV = Standard Yield Cost * (AY - SY)",
                "Labour Variances: LCV = (SH * SR) - (AH * AR); LRateV = AH * (SR - AR); LEffV = SR * (SH - AH); Idle Time Variance = Idle Hours * SR (Always Adverse!)",
                "Variable Overhead Variances: VOH Cost = (SH * SR) - (AH * AR); VOH Exp = AH * (SR - AR); VOH Eff = SR * (SH - AH)",
                "Fixed Overhead Variances: FOH Cost = Absorbed FOH - Actual FOH; FOH Expenditure = Budgeted FOH - Actual FOH; FOH Volume = (SH - BH) * Standard Rate"
            ),
            commonExaminerPitfalls = "Multiplying Material Usage Variance by Actual Price instead of Standard Price! Treating Idle Time Variance as Favourable."
        ),
        SyllabusTopic(
            id = "p4_material_eoq",
            paper = PaperCode.PAPER_4,
            chapter = "Material Cost",
            topicName = "Chapter 2: Material Cost (EOQ, Stock Levels, Inventory Control)",
            moduleName = "Module 1",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "9/9 Attempts",
            averageMarks = 10.0,
            trendDirection = "Core 10M Problem • 9/9 Attempts",
            writtenTarget = 8,
            mcqTarget = 35,
            oralRecallTarget = 5,
            oralRhythmNote = "EOQ & stock level formulas (ROL, Min, Max, Danger) daily",
            fusedWithTopics = listOf("Overheads"),
            keyProvisions = listOf(
                "Economic Order Quantity (EOQ) = sqrt((2 * Annual Demand * Ordering Cost) / Carrying Cost per unit per year); at EOQ, Total Ordering Cost = Total Carrying Cost",
                "Reorder Level (ROL) = Maximum Consumption * Maximum Lead Time; Safety Stock = (Max Usage - Normal Usage) * Lead Time",
                "Minimum Level = ROL - (Normal Consumption * Normal Lead Time); Maximum Level = ROL + ROQ - (Minimum Consumption * Minimum Lead Time)",
                "Inventory Control Techniques: ABC Analysis (value-based: A high value 70%, B moderate 20%, C low 10%), VED (Vital, Essential, Desirable for spares), FSN (Fast, Slow, Non-moving)"
            ),
            commonExaminerPitfalls = "Calculating carrying cost percentage on ordering cost instead of purchase price per unit; confusing units with rupee values in stock level formulas."
        ),
        SyllabusTopic(
            id = "p4_ch5_abc",
            paper = PaperCode.PAPER_4,
            chapter = "Activity Based Costing",
            topicName = "Chapter 5: Activity Based Costing (ABC)",
            moduleName = "Module 1",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "9/9 Attempts",
            averageMarks = 10.0,
            trendDirection = "Core 10M Question • 9/9 Attempts",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Cost driver rates & activity hierarchy (Unit, Batch, Product, Facility) every 2nd day",
            fusedWithTopics = listOf("Overheads Absorption Costing"),
            keyProvisions = listOf(
                "ABC assigns costs to activities based on resource consumption and then to products based on activity consumption (eliminates volume-based distortions)",
                "Cost Hierarchy: Unit-level activities (machine running), Batch-level (setups, material handling), Product-sustaining (product design), Facility-sustaining (plant security, lighting)",
                "Cost Driver Rate = Total Cost of Cost Pool / Total Activity Driver Volume",
                "Types of Cost Drivers: Transaction Drivers (count of setups/inspections), Duration Drivers (hours of inspection/setup), Intensity Drivers (direct charging of resources)"
            ),
            commonExaminerPitfalls = "Allocating facility-sustaining overheads using batch drivers; failing to recognize that high-volume products subsidize low-volume products under traditional costing."
        ),
        SyllabusTopic(
            id = "p4_ch3_employee_cost",
            paper = PaperCode.PAPER_4,
            chapter = "Employee Cost",
            topicName = "Chapter 3: Employee Cost & Direct Expenses",
            moduleName = "Module 1",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "9/9 Attempts",
            averageMarks = 8.0,
            trendDirection = "Core Incentive Problem • 9/9 Attempts",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Halsey (50%) & Rowan incentive bonus plans every 2nd day",
            fusedWithTopics = listOf("Overheads Absorption"),
            keyProvisions = listOf(
                "Idle Time: Normal idle time is uncontrollable and absorbed into production overheads/wage rates; Abnormal idle time (power failure, strike) charged to Costing P&L",
                "Labour Turnover measurement: Separation Method (Separations / Avg Workers * 100), Replacement Method (Replacements / Avg Workers * 100), Flux Method ((Sep + Accessions) / Avg Workers * 100)",
                "Halsey Premium Plan: Total Wages = (Actual Time * Time Rate) + (50% * Time Saved * Time Rate)",
                "Rowan Premium Plan: Total Wages = (Actual Time * Time Rate) + ((Time Saved / Standard Time) * Actual Time * Time Rate); bonus increases up to 50% time saved then declines"
            ),
            commonExaminerPitfalls = "Including new workers recruited for expansion in the Replacement method (replacement means filling existing vacancies only!)."
        ),
        SyllabusTopic(
            id = "p4_ch15_budgetary_control",
            paper = PaperCode.PAPER_4,
            chapter = "Budgets & Budgetary Control",
            topicName = "Chapter 15: Budgets and Budgetary Control",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "9/9 Attempts",
            averageMarks = 10.0,
            trendDirection = "Core 10M Question • 9/9 Attempts",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Flexible budget & cash budget receipts/payments format every 2nd day",
            fusedWithTopics = listOf("Standard Costing"),
            keyProvisions = listOf(
                "Functional Budgets: Sales Budget, Production Budget (Sales + Closing FG - Opening FG), Material Consumption & Purchase Budget, Cash Budget",
                "Flexible Budget: Re-computes costs for different capacity levels (60%, 80%, 100%) by separating fixed, variable, and semi-variable elements",
                "Cash Budget: Receipts & Payments Method (projects cash inflows and outflows month-by-month to detect deficit/surplus)",
                "Zero-Based Budgeting (ZBB): Past budget figures are NOT taken as base; every activity justified from scratch as if starting new; evaluates Decision Packages"
            ),
            commonExaminerPitfalls = "Failing to adjust lag in payment of wages or delay in collection of debtors in Cash Budget; scaling fixed costs with volume in flexible budgets."
        ),

        // --- Tier 2 HIGH (7-8/9 Attempts, 3x Oral Recall) ---
        SyllabusTopic(
            id = "p4_ch6_cost_sheet",
            paper = PaperCode.PAPER_4,
            chapter = "Cost Sheet",
            topicName = "Chapter 6: Cost Sheet (CAS-1 to CAS-24 Formats)",
            moduleName = "Module 1",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "8/9 Attempts",
            averageMarks = 10.0,
            trendDirection = "High Yield 10M Question",
            writtenTarget = 8,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Prime Cost -> Works Cost -> Cost of Production checklist every 3rd day",
            fusedWithTopics = listOf("Overheads", "Cost Accounting Systems"),
            keyProvisions = listOf(
                "Prime Cost = Direct Material Consumed + Direct Employee Cost + Direct Expenses",
                "Works / Factory Cost = Prime Cost + Factory Overheads + Opening WIP - Closing WIP",
                "Cost of Production = Works Cost + Quality Control Cost + R&D Cost + Administration Overheads (Production related) - Credit for Recoveries + Primary Packing Cost",
                "Cost of Sales = Cost of Production + Opening FG - Closing FG + Admin Overheads (General) + Selling & Distribution Overheads; Cost of Sales + Profit = Sales"
            ),
            commonExaminerPitfalls = "Including General Administrative expenses or Selling & Distribution expenses in Cost of Production (they appear only after Cost of Goods Sold!)."
        ),
        SyllabusTopic(
            id = "p4_ch4_overheads",
            paper = PaperCode.PAPER_4,
            chapter = "Overheads",
            topicName = "Chapter 4: Overheads - Absorption Costing Method",
            moduleName = "Module 1",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "8/9 Attempts",
            averageMarks = 8.0,
            trendDirection = "High Yield Primary/Secondary Distribution",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Machine hour rate & repeated distribution method every 3rd day",
            fusedWithTopics = listOf("Cost Sheet", "ABC"),
            keyProvisions = listOf(
                "Primary Distribution: Direct allocation of specific overheads and apportionment of common overheads (Rent -> floor area, Depreciation -> asset value, Power -> HP * hours)",
                "Secondary Distribution: Re-apportionment of service department costs to production departments via Step Ladder, Simultaneous Equations, or Repeated Distribution method",
                "Absorption Rates: Machine Hour Rate (Overheads / Machine Hours) and Labour Hour Rate (Overheads / Direct Labour Hours)",
                "Under/Over-absorption of Overheads: Actual Overheads vs Absorbed Overheads (Absorbed = Actual Hours * Predetermined Rate); treated via Supplementary rate or Costing P&L"
            ),
            commonExaminerPitfalls = "Computing absorbed overheads by multiplying predetermined rate by budgeted hours instead of ACTUAL hours worked."
        ),
        SyllabusTopic(
            id = "p4_ch10_process_costing",
            paper = PaperCode.PAPER_4,
            chapter = "Process Costing",
            topicName = "Chapter 10: Process & Operation Costing (Equivalent Production)",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "7/9 Attempts",
            averageMarks = 10.0,
            trendDirection = "High Yield 10M Problem",
            writtenTarget = 8,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Equivalent production statement (FIFO vs Weighted Average) every 3rd day",
            fusedWithTopics = listOf("Joint Products"),
            keyProvisions = listOf(
                "Normal Loss: Unavoidable loss inherent in production; scrap value of normal loss credited to Process Account (reduces cost of good units)",
                "Abnormal Loss: Avoidable loss exceeding standard expectation; valued at full cost of good production units and debited to Costing Profit & Loss",
                "Abnormal Gain: Production exceeding standard expectation; credited to Abnormal Gain Account and transferred to Costing P&L after adjusting shortfall in normal scrap",
                "Equivalent Units of Production: Restating partially completed WIP into equivalent finished units using FIFO or Weighted Average method"
            ),
            commonExaminerPitfalls = "Valuing Abnormal Loss at scrap value instead of cost per unit; allocating normal loss costs to abnormal loss units."
        ),
        SyllabusTopic(
            id = "p4_ch12_service_costing",
            paper = PaperCode.PAPER_4,
            chapter = "Service Costing",
            topicName = "Chapter 12: Service Costing (Operating Costing)",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "7/9 Attempts",
            averageMarks = 8.0,
            trendDirection = "High Yield Operating Costing",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Passenger-Kilometre & Room-Day calculations every 3rd day",
            keyProvisions = listOf(
                "Composite Cost Units: Transport (Passenger-Kilometre or Ton-Kilometre), Hospital (Patient-Day or Room-Day), Hotel (Room-Day with seasonal weights), Electricity (Kilowatt-Hour)",
                "Absolute Ton-Km (sum of individual trips: Weight * Distance) vs Commercial Ton-Km (Average Weight * Total Distance)",
                "Cost Classification: Standing Charges (fixed: license fee, insurance, garage rent, driver salary), Maintenance Charges (repairs, tyres), Running Charges (petrol/diesel, grease)",
                "Fare determination: Operating Cost per passenger-km + Desired profit margin = Fare per passenger-km"
            ),
            commonExaminerPitfalls = "Confusing Absolute Ton-Km with Commercial Ton-Km; forgetting to factor in occupancy percentage in passenger transport calculations."
        ),

        // --- Tier 3 MEDIUM (5-7M avg) & Tier 4 BASE ---
        SyllabusTopic(
            id = "p4_ch9_job_contract_costing",
            paper = PaperCode.PAPER_4,
            chapter = "Job & Contract Costing",
            topicName = "Chapter 9: Job Costing and Contract Costing",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "5/9 Attempts",
            averageMarks = 6.0,
            trendDirection = "Medium Yield Problem",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Work certified, retention money & escalation clause",
            keyProvisions = listOf(
                "Job Costing: Costs accumulated for each individual customer job/work order having unique specifications; uses Job Cost Cards",
                "Contract Costing: Large-scale, long-duration jobs carried out at site; follows AS 7 (Percentage of Completion Method)",
                "Architect Certificate & Retention Money: Cash Received = Work Certified minus Retention Money; Work Uncertified valued at actual cost",
                "Escalation Clause: Protects contractor against unexpected increases in prices of material, labour, and petroleum products during contract execution"
            ),
            commonExaminerPitfalls = "Valuing Work Uncertified at contract sale price instead of cost; recognizing 100% estimated profit before contract reaches reasonable completion stage."
        ),
        SyllabusTopic(
            id = "p4_ch11_joint_by_products",
            paper = PaperCode.PAPER_4,
            chapter = "Joint Products & By-Products",
            topicName = "Chapter 11: Joint Products and By-Products",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "6/9 Attempts",
            averageMarks = 6.0,
            trendDirection = "Medium Yield Apportionment",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "NRV at split-off point apportionment method",
            fusedWithTopics = listOf("Process Costing"),
            keyProvisions = listOf(
                "Joint Products: Two or more products of significant economic importance produced simultaneously from a common raw material up to Split-off Point",
                "By-Products: Incidental or secondary outputs of minor economic value obtained during manufacturing of main product",
                "Methods of Apportioning Joint Costs: Physical Units Method, Sales Value at Split-off Method, Net Realizable Value (NRV) Method, Constant Gross Margin % Method",
                "Treatment of By-Product: Realizable value of by-product credited to Main Process Account (or deducted from joint cost of main products)"
            ),
            commonExaminerPitfalls = "Apportioning joint costs based on sales value after further processing without deducting further processing costs (must use NRV at split-off!)."
        ),
        SyllabusTopic(
            id = "p4_ch8_unit_batch_costing",
            paper = PaperCode.PAPER_4,
            chapter = "Unit & Batch Costing",
            topicName = "Chapter 8: Unit and Batch Costing",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "5/9 Attempts",
            averageMarks = 5.0,
            trendDirection = "Medium Yield Practical",
            writtenTarget = 4,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "Economic Batch Quantity (EBQ) formula",
            keyProvisions = listOf(
                "Unit Costing applied where single homogeneous product is produced on a continuous basis (mining, cement, brick making, breweries)",
                "Batch Costing applied where production is carried out in identifiable batches of similar items (readymade garments, pharmaceuticals, component manufacturing)",
                "Economic Batch Quantity (EBQ) = sqrt((2 * Annual Demand * Setting up Cost) / Carrying Cost per unit per year)",
                "At EBQ, Total Set-up Costs equal Total Carrying Costs of the batch inventory"
            ),
            commonExaminerPitfalls = "Applying EOQ order cost formula to setup costs without verifying if annual demand is uniform throughout the period."
        ),
        SyllabusTopic(
            id = "p4_ch7_cost_accounting_systems",
            paper = PaperCode.PAPER_4,
            chapter = "Cost Accounting Systems",
            topicName = "Chapter 7: Cost Accounting Systems (Integrated & Non-Integrated)",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "5/9 Attempts",
            averageMarks = 5.0,
            trendDirection = "Cost vs Financial Reconciliation",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Reconciliation statement format & purely financial items",
            fusedWithTopics = listOf("Cost Sheet"),
            keyProvisions = listOf(
                "Non-Integrated Accounting: Cost records separate from financial records; uses General Ledger Adjustment (Cost Ledger Control - CLC) account to complete double entry",
                "Key Control Accounts: Stores Ledger Control, Work-in-Progress Control, Finished Goods Control, Cost of Sales, Costing Profit & Loss",
                "Reconciliation of Cost and Financial Accounts: Reconciles profit differences due to under/over-absorption, purely financial items (income tax, dividend, interest), and inventory valuation bases",
                "Integrated Accounting: Single unified ledger system eliminating the need for Cost Ledger Control account and separate reconciliation"
            ),
            commonExaminerPitfalls = "Crediting opening stock overvaluation in cost accounts to financial profit instead of deducting it in the reconciliation statement."
        ),
        SyllabusTopic(
            id = "p4_ch1_intro_costing",
            paper = PaperCode.PAPER_4,
            chapter = "Cost & Management Accounting",
            topicName = "Chapter 1: Introduction to Cost and Management Accounting",
            moduleName = "Module 1",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "3/9 Attempts",
            averageMarks = 4.0,
            trendDirection = "Base Theory",
            writtenTarget = 3,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "Cost control vs Cost reduction definitions",
            keyProvisions = listOf(
                "Cost Object, Cost Centre (location, person, or equipment for which cost is ascertained) and Cost Unit (unit of product/service in relation to which costs are ascertained)",
                "Cost Classification by Behaviour: Fixed Cost (remains constant in total, varies per unit), Variable Cost (varies in total, constant per unit), Semi-Variable Cost",
                "Costing vs Financial Accounting: Costing assists internal management with cost ascertainment, planning, and control; financial accounting reports historic performance to external stakeholders",
                "Cost Control (setting targets and maintaining performance) vs Cost Reduction (permanent real reduction in unit cost without impairing quality or suitability)"
            ),
            commonExaminerPitfalls = "Treating Cost Control and Cost Reduction as synonymous; confusing cost units with cost centres."
        ),

        // =========================================================================
        // PAPER 5: Auditing and Ethics (ICAI CA Intermediate New Scheme)
        // 7-Attempt Trend May 2024 to May 2026 Strategy Blueprint
        // =========================================================================

        // --- Tier 1 Fortress (>= 14M avg, 50+ MCQs, 10+ Written) ---
        SyllabusTopic(
            id = "p5_ch9_items_fin_stmt",
            paper = PaperCode.PAPER_5,
            chapter = "Audit of Items of Financial Statements",
            topicName = "Chapter 9: Substantive Audit of Balance Sheet & P&L Items",
            moduleName = "Module 3",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "7/7 Attempts",
            averageMarks = 16.0,
            trendDirection = "Fortress 16M Compulsory Anchor",
            writtenTarget = 12,
            mcqTarget = 50,
            oralRecallTarget = 5,
            oralRhythmNote = "Daily substantive verification assertions (Existence, Rights, Valuation, Completeness)",
            fusedWithTopics = listOf("Schedule III", "CARO 2020"),
            blueprint = StandardBlueprint(
                scope = "Detailed substantive verification procedures and assertions for each line item of financial statements under Schedule III.",
                recognitionAndMeasurement = "Audit assertions: Balance Sheet (Existence, Rights & Obligations, Completeness, Valuation & Allocation), P&L (Occurrence, Completeness, Accuracy, Cut-off, Classification). Verification of Share Capital (allotment returns, calls-in-arrear), Reserves, Long-term Borrowings (covenants, charges registered with ROC), PPE (physical verification, title deeds), Trade Receivables (circularization, aging, bad debt provisioning), Inventories (stock count, cut-off testing, NRV testing), Trade Payables (search for unrecorded liabilities), Revenue (cut-off, dispatch proofs).",
                disclosure = "Verification of Schedule III presentation, capital commitments, contingent liabilities, and statutory ratio disclosures."
            ),
            keyProvisions = listOf(
                "Testing Management Assertions: Existence (assets exist), Completeness (all transactions recorded), Valuation (recorded at correct amount), Rights and Obligations (entity holds legal ownership)",
                "Cut-off testing: Ensuring goods dispatched/received near balance sheet date are recorded in correct accounting period",
                "Search for unrecorded liabilities: Examining post-balance-sheet cash disbursements, invoices, and Goods Received Notes (GRNs)",
                "Verification of charges with Registrar of Companies (ROC) within 30 days under Section 77 for all secured borrowings"
            ),
            commonExaminerPitfalls = "Vouching expenses backward from ledger to voucher only (tests occurrence, but fails to test completeness/omissions); skipping search for unrecorded liabilities."
        ),
        SyllabusTopic(
            id = "p5_sa500_505",
            paper = PaperCode.PAPER_5,
            chapter = "Audit Evidence",
            topicName = "Chapter 4: SA 500, SA 501, SA 505, SA 510 & SA 550 (Audit Evidence & Special Areas)",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "7/7 Attempts",
            averageMarks = 18.0,
            trendDirection = "Fortress SA Cluster Anchor • 7/7 Attempts",
            writtenTarget = 12,
            mcqTarget = 50,
            oralRecallTarget = 5,
            oralRhythmNote = "Daily SAAE reliability hierarchy & External Confirmation controls",
            fusedWithTopics = listOf("Audit Report", "Risk Assessment"),
            blueprint = StandardBlueprint(
                scope = "Designing and performing audit procedures to obtain sufficient appropriate audit evidence (SAAE) to draw reasonable conclusions.",
                recognitionAndMeasurement = "SA 500: Sufficiency (quantity) + Appropriateness (quality - relevance and reliability). External > Internal; Written > Oral; Original > Photocopy. Audit procedures: Inspection, Observation, External Confirmation, Recalculation, Reperformance, Analytical Procedures, Inquiry. SA 501: Physical inventory count attendance, litigation inquiry with external legal counsel, segment reporting. SA 505: External confirmations (positive vs negative). SA 510: Opening balances verification. SA 550: Related party identification and transactions outside normal course of business.",
                disclosure = "Document audit evidence obtained, management representation letters received, confirmation logs, and explanations for non-responses."
            ),
            keyProvisions = listOf(
                "Sufficient Appropriate Audit Evidence (SAAE): Sufficiency = quantity affected by RoMM; Appropriateness = quality (relevance & reliability)",
                "Reliability hierarchy: External source > Internal source; Directly obtained by auditor > Obtained indirectly; Written documentary > Oral; Originals > Photocopies",
                "SA 501: Auditor attendance at physical inventory counting is mandatory unless impracticable; if attendance impracticable, perform alternative procedures",
                "SA 505: External confirmation control: Auditor must maintain direct control over preparing, sending, and receiving responses; negative confirmations provide less persuasive evidence than positive",
                "SA 550: Significant related party transactions outside entity's normal course of business indicate high risk of fraud"
            ),
            commonExaminerPitfalls = "Allowing client management to dispatch confirmation letters or receive responses; treating oral management explanations as sufficient appropriate evidence."
        ),
        SyllabusTopic(
            id = "p5_ch7_sa700_701_705_706_710",
            paper = PaperCode.PAPER_5,
            chapter = "Audit Report",
            topicName = "Chapter 7: SA 700, 701, 705, 706 & 710 (Forming Opinions & Modifications)",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "7/7 Attempts",
            averageMarks = 14.0,
            trendDirection = "Fortress Reporting Cluster • 7/7 Attempts",
            writtenTarget = 10,
            mcqTarget = 50,
            oralRecallTarget = 5,
            oralRhythmNote = "Daily SA 705 Opinion matrix (Qualified vs Adverse vs Disclaimer) & KAM",
            fusedWithTopics = listOf("Going Concern SA 570", "CARO 2020"),
            blueprint = StandardBlueprint(
                scope = "Forming an opinion and reporting on financial statements, communicating key audit matters, modifying opinions, and adding Emphasis of Matter paragraphs.",
                recognitionAndMeasurement = "SA 700: Basic elements of report (Title, Addressee, Opinion, Basis for Opinion, Going Concern, Key Audit Matters, Responsibilities of Management, Auditor's Responsibilities, Other Reporting Responsibilities). SA 701: Key Audit Matters (KAM) mandatory for listed entities (matters of most significance). SA 705: Qualified ('Except for...'), Adverse (Material & Pervasive misstatement), Disclaimer (Material & Pervasive inability to obtain SAAE). SA 706: Emphasis of Matter (matter appropriately presented in FS that is fundamental to users) vs Other Matter.",
                disclosure = "Wording and structure of Opinion, Basis for Opinion, Key Audit Matters section, and Emphasis of Matter / Other Matter paragraphs."
            ),
            keyProvisions = listOf(
                "SA 700 standard structure: Title -> Addressee -> Opinion Section -> Basis for Opinion -> Going Concern -> KAM -> Responsibilities",
                "SA 701: Key Audit Matters (KAM) mandatory for audits of complete sets of general purpose financial statements of listed entities",
                "SA 705 Opinion Matrix: Material but NOT Pervasive -> Qualified ('Except for'); Material AND Pervasive with misstatement -> Adverse; Material AND Pervasive with limitation of scope -> Disclaimer",
                "SA 706: Emphasis of Matter paragraph refers to a matter appropriately presented/disclosed in FS; does NOT modify auditor's opinion"
            ),
            commonExaminerPitfalls = "Using an Adverse opinion when audit evidence could not be obtained (should be Disclaimer); placing Basis for Opinion before Opinion section."
        ),

        // --- Tier 2 High Yield (10-13.9M avg) ---
        SyllabusTopic(
            id = "p5_ch3_sa315_320_330",
            paper = PaperCode.PAPER_5,
            chapter = "Risk Assessment & Internal Control",
            topicName = "Chapter 3: SA 315, SA 320 & SA 330 (Risk of Material Misstatement & Materiality)",
            moduleName = "Module 1",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "7/7 Attempts",
            averageMarks = 12.0,
            trendDirection = "High Yield Core Audit Process",
            writtenTarget = 8,
            mcqTarget = 35,
            oralRecallTarget = 4,
            oralRhythmNote = "Audit Risk model & Materiality benchmarks every 2nd day",
            fusedWithTopics = listOf("Audit Strategy SA 300"),
            blueprint = StandardBlueprint(
                scope = "Identifying and assessing Risks of Material Misstatement (RoMM) at financial statement and assertion levels, determining materiality, and responding to assessed risks.",
                recognitionAndMeasurement = "SA 315: Understand entity and its environment, including internal control (COSO 5 components: Control Environment, Risk Assessment, Info System, Control Activities, Monitoring). Assess RoMM (Inherent Risk x Control Risk). SA 320: Materiality determined based on benchmark (PBT, revenue, net assets); calculate Performance Materiality to reduce aggregation risk. SA 330: Test of Controls (operating effectiveness) and Substantive Procedures (Tests of Details & Substantive Analytical Procedures).",
                disclosure = "Document identified and assessed RoMM, overall responses to address financial statement level risks, and results of audit procedures."
            ),
            keyProvisions = listOf(
                "Audit Risk = Risk of Material Misstatement (Inherent Risk * Control Risk) * Detection Risk",
                "Components of Internal Control: Control Environment, Entity's Risk Assessment Process, Information System, Control Activities, Monitoring of Controls",
                "SA 320: Materiality depends on size and nature; Performance Materiality set at lower amount to reduce risk that uncorrected misstatements exceed materiality",
                "SA 330: Tests of Controls evaluate operating effectiveness; if controls are ineffective or absent, rely purely on extensive Substantive Procedures"
            ),
            commonExaminerPitfalls = "Believing that strong internal controls eliminate the need for substantive audit procedures entirely. Substantive procedures are MANDATORY for all material classes!"
        ),
        SyllabusTopic(
            id = "p5_ch10_banks_entities_caro",
            paper = PaperCode.PAPER_5,
            chapter = "Special Entities & Bank Audit",
            topicName = "Chapter 10: Audit of Banks, Special Entities & CARO 2020",
            moduleName = "Module 3",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "7/7 Attempts",
            averageMarks = 12.0,
            trendDirection = "High Yield Special Entities & CARO",
            writtenTarget = 8,
            mcqTarget = 35,
            oralRecallTarget = 4,
            oralRhythmNote = "IRACP provisioning percentages & CARO 21 clauses every 2nd day",
            fusedWithTopics = listOf("Audit Report", "Items of FS"),
            blueprint = StandardBlueprint(
                scope = "Audit of Banking Companies (Banking Regulation Act 1949), Non-corporate entities (Hotels, Hospitals, Educational Institutions, Clubs), and reporting under CARO 2020.",
                recognitionAndMeasurement = "Bank Audit: Verification of Advances (funded vs non-funded). Prudential norms on Income Recognition, Asset Classification and Provisioning (IRACP): Standard, Sub-standard (NPA <= 12 months), Doubtful (D1 up to 1 yr, D2 1-3 yrs, D3 > 3 yrs), Loss assets. Long Form Audit Report (LFAR). CARO 2020: 21 clauses (PPE title deeds, inventory 10% discrepancy, quarterly bank returns > ₹5 Cr, statutory dues > 6 months, fraud reporting, internal audit applicability).",
                disclosure = "CARO 2020 clause-by-clause reporting in auditor's report; LFAR submission to bank management and RBI."
            ),
            keyProvisions = listOf(
                "Bank Audit IRACP Norms: NPA definition (interest/principal overdue > 90 days for term loans, out of order > 90 days for CC/OD)",
                "Provisioning requirements: Sub-standard (15% secured, 25% unsecured); Doubtful (25% D1, 40% D2, 100% D3 for secured portion + 100% unsecured); Loss (100%)",
                "CARO 2020 Clause (i): PPE physical verification by management, title deeds not held in company name, benami proceedings",
                "CARO 2020 Clause (ii): Physical verification of inventory (discrepancy >= 10% in aggregate); reconciliation of quarterly stock statements submitted to banks for sanctioned working capital limits > ₹5 Crore",
                "Audit of Educational Institutions: Verification of fee concession, grants, caution money, hostel receipts, and trust deed stipulations"
            ),
            commonExaminerPitfalls = "Applying CARO 2016 clauses; confusing provisioning percentages for Sub-standard (15%) vs Doubtful D1 (25%); misstating ₹5 Cr bank working capital threshold."
        ),
        SyllabusTopic(
            id = "p5_ch1_sa200_210_220",
            paper = PaperCode.PAPER_5,
            chapter = "Nature, Objective & Scope of Audit",
            topicName = "Chapter 1: SQC 1, SA 200, SA 210 & SA 220 (Quality Control & Objectives)",
            moduleName = "Module 1",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "6/7 Attempts",
            averageMarks = 10.0,
            trendDirection = "High Yield Foundation Standards",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Professional Skepticism & SA 210 preconditions every 3rd day",
            fusedWithTopics = listOf("Professional Ethics"),
            blueprint = StandardBlueprint(
                scope = "Applies to overall responsibilities of auditor in conducting an audit in accordance with SAs, agreeing terms of engagement, and quality control at firm and engagement levels.",
                recognitionAndMeasurement = "SA 200: Obtain reasonable assurance that financial statements are free from material misstatement whether due to fraud or error, and report thereon. Maintain Professional Skepticism and Professional Judgment. Recognize Inherent Limitations of Audit. SA 210: Preconditions for audit (acceptable financial reporting framework, management acknowledgement of responsibilities). Agree audit engagement letter. SA 220 & SQC 1: Leadership responsibilities, ethical requirements, client acceptance, engagement performance, and monitoring.",
                disclosure = "Engagement letter documenting objective, scope, responsibilities of auditor and management, identification of applicable financial reporting framework, and expected form of reports."
            ),
            keyProvisions = listOf(
                "SA 200: Overall objective is to obtain Reasonable Assurance (high, but not absolute assurance) and express an independent opinion",
                "Professional Skepticism: Attitude that includes a questioning mind, being alert to conditions indicating possible misstatement, and critical assessment of audit evidence",
                "Inherent limitations of audit: Nature of financial reporting, nature of audit procedures, need for audit to be conducted within reasonable period and cost",
                "SA 210: Preconditions for audit (acceptable FRF, management acknowledgment of responsibility for internal control and unrestricted access)",
                "SQC 1 & SA 220: Elements of Quality Control (Leadership, Ethical requirements, Acceptance/Continuance, HR, Engagement performance, Monitoring)"
            ),
            commonExaminerPitfalls = "Claiming an audit provides 'absolute assurance' against fraud; failing to list the two statutory preconditions before accepting an audit under SA 210."
        ),

        // --- Tier 3 Steady (6-9.9M avg) & Tier 4 Booster ---
        SyllabusTopic(
            id = "p5_ch5_sa230_520_530",
            paper = PaperCode.PAPER_5,
            chapter = "Audit Documentation & Techniques",
            topicName = "Chapter 5: SA 230, SA 520 & SA 530 (Documentation, Analytics & Sampling)",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "6/7 Attempts",
            averageMarks = 8.0,
            trendDirection = "Steady Practice Standards",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "SA 230 60-day assembly & 7-year retention rules",
            blueprint = StandardBlueprint(
                scope = "Auditor's responsibility to prepare audit documentation, use substantive analytical procedures, and apply audit sampling in performing tests.",
                recognitionAndMeasurement = "SA 230: Working papers must enable an experienced auditor with no prior connection to understand NTE of procedures, results/evidence, and significant matters. Assembly of final audit file within 60 days from auditor's report date; retention period minimum 7 years. SA 520: Analytical procedures (trend, ratio, regression) at planning, substantive, and final review stages. SA 530: Statistical vs Non-statistical sampling; Sampling Risk (Alpha/Beta risk) and Tolerable Error/Misstatement.",
                disclosure = "Document working papers, cross-referencing, sampling method chosen, sample size justification, evaluation of sample errors, and analytical expectations."
            ),
            keyProvisions = listOf(
                "SA 230: Working papers are property of the auditor; assembly of final audit file completed within 60 days of report date; retention for at least 7 years",
                "Experienced Auditor test: Documentation must enable an experienced auditor having no previous connection to understand procedures, results, and conclusions",
                "SA 520: Substantive Analytical Procedures: Plausibility and predictability of relationships; develop expectation and compare with recorded amounts",
                "SA 530: Sampling Risk (Risk of under-reliance/incorrect rejection and risk of over-reliance/incorrect acceptance); project sample errors to population"
            ),
            commonExaminerPitfalls = "Confusing working paper retention period (minimum 7 years from report date) with assembly period (60 days); confusing sampling risk with non-sampling risk."
        ),
        SyllabusTopic(
            id = "p5_ch2_sa300_planning",
            paper = PaperCode.PAPER_5,
            chapter = "Audit Strategy & Planning",
            topicName = "Chapter 2: SA 300 (Audit Strategy, Audit Planning and Audit Programme)",
            moduleName = "Module 1",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "5/7 Attempts",
            averageMarks = 7.0,
            trendDirection = "Steady Audit Planning Chapter",
            writtenTarget = 5,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Audit strategy vs Audit plan distinction",
            blueprint = StandardBlueprint(
                scope = "Auditor's responsibility to plan an audit of financial statements. Planning is a continuous and iterative process.",
                recognitionAndMeasurement = "Establish overall Audit Strategy that sets scope, timing, and direction of audit and guides development of Audit Plan. Audit Plan includes nature, timing, and extent of planned risk assessment procedures (SA 315) and further audit procedures (SA 330). Audit Programme details specific verification procedures for each audit area.",
                disclosure = "Document the overall audit strategy, the audit plan, and any significant changes made during the engagement and reasons for such changes."
            ),
            keyProvisions = listOf(
                "Overall Audit Strategy sets the scope, timing, and direction of the audit and guides the development of the more detailed Audit Plan",
                "Audit Plan is more detailed than strategy; includes Nature, Timing, and Extent (NTE) of risk assessment and further audit procedures",
                "Audit planning is NOT a discrete phase; it is a continual and iterative process throughout the entire audit engagement",
                "Audit Programme: Written set of instructions to audit assistants outlining verification steps, sample sizes, and timing of procedures"
            ),
            commonExaminerPitfalls = "Treating audit strategy and audit plan as identical terms; claiming that once an audit plan is approved, it cannot be modified during the audit."
        ),
        SyllabusTopic(
            id = "p5_ch11_ethics_independence",
            paper = PaperCode.PAPER_5,
            chapter = "Ethics & Professional Conduct",
            topicName = "Chapter 11: Professional Ethics, Integrity & Auditor Independence",
            moduleName = "Module 3",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "6/7 Attempts",
            averageMarks = 8.0,
            trendDirection = "Steady Ethics Chapter",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "5 Fundamental Principles & 5 Threats to Independence",
            fusedWithTopics = listOf("Companies Act Sec 141"),
            blueprint = StandardBlueprint(
                scope = "ICAI Code of Ethics principles, threats to independence, safeguards, and statutory disqualifications of auditors under Section 141 of Companies Act 2013.",
                recognitionAndMeasurement = "5 Fundamental Principles of Professional Ethics: Integrity, Objectivity, Professional Competence & Due Care, Confidentiality, Professional Behaviour. 5 Threats to Independence: Self-interest threat, Self-review threat, Advocacy threat, Familiarity threat, Intimidation threat. Independence of mind vs Independence in appearance. Section 141(3) Disqualifications: Body corporate, officer/employee, partner/employee of officer, person holding security/interest in company/subsidiary (relative holding > ₹1 Lakh face value), indebtedness > ₹5 Lakh, guarantee > ₹1 Lakh, business relationship, audit of > 20 companies.",
                disclosure = "Disclosure of non-audit services prohibited under Section 144; declaration of independence to Those Charged With Governance."
            ),
            keyProvisions = listOf(
                "5 Fundamental Principles: Integrity, Objectivity, Professional Competence and Due Care, Confidentiality, Professional Behaviour",
                "5 Threats to Independence: Self-interest threat, Self-review threat, Advocacy threat, Familiarity threat, Intimidation threat",
                "Section 141(3)(d) Security limits: Relative may hold securities in company of face value not exceeding ₹1,00,000 (grace period of 60 days to take corrective action)",
                "Section 141(3)(g) Ceiling on audits: Maximum 20 company audits per partner (excluding OPC, dormant, small companies, and private companies with paid-up capital < ₹100 Crore)",
                "Section 144: Auditor CANNOT provide prohibited non-audit services (accounting/book keeping, internal audit, investment banking, actuarial, outsourced financial services)"
            ),
            commonExaminerPitfalls = "Thinking an auditor can hold ₹1,00,000 face value (the exception applies ONLY to relatives, NOT to the auditor or partner!); counting OPC or Small companies in the 20-company audit limit."
        ),
        SyllabusTopic(
            id = "p5_ch8_sa240_250_260_299_610",
            paper = PaperCode.PAPER_5,
            chapter = "Special Aspects of Auditing",
            topicName = "Chapter 8: SA 240, SA 250, SA 260, SA 299 & SA 610 (Fraud, Laws & Joint Audit)",
            moduleName = "Module 3",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "6/7 Attempts",
            averageMarks = 8.0,
            trendDirection = "Fraud & Joint Audit Special Standards",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Section 143(12) ₹1 Crore fraud reporting & SA 299 joint liability",
            blueprint = StandardBlueprint(
                scope = "Responsibilities relating to fraud, compliance with laws/regulations, communicating with Those Charged With Governance (TCWG), joint audits, and using work of internal auditors.",
                recognitionAndMeasurement = "SA 240: Fraud triangle (Incentive/Pressure, Opportunity, Rationalization). Fraudulent Financial Reporting vs Misappropriation of Assets. Presumption of revenue recognition fraud risk and management override of controls. Section 143(12) reporting to Central Govt in Form ADT-4 (fraud >= ₹1 Cr). SA 250: Direct effect vs indirect effect laws. SA 299: Joint auditors jointly and severally responsible for undivided work, decisions, and common disclosures; severally responsible for assigned divisions. SA 610: Evaluating internal audit function competence and objectivity before using direct assistance.",
                disclosure = "Reporting to Central Government under Section 143(12) within 60 days, communication with TCWG, and joint audit work allocation agreements."
            ),
            keyProvisions = listOf(
                "SA 240: Primary responsibility for prevention and detection of fraud rests with Management and TCWG; auditor responsible for reasonable assurance regarding material misstatement",
                "Fraud reporting under Section 143(12): If fraud involves amount >= ₹1 Crore, report to Board/Audit Committee within 2 days, seek reply in 45 days, and forward to Central Govt (MCA) in Form ADT-4 within 15 days",
                "SA 299 Joint Audit: Joint auditors are jointly and severally responsible for unallocated work, decisions on presentation/disclosure, and compliance with statutory provisions",
                "SA 610: Auditor cannot use internal auditor for work involving significant judgment, high risk of material misstatement, or where internal auditor lacks objectivity"
            ),
            commonExaminerPitfalls = "Failing to report fraud >= ₹1 Cr to Central Government; holding all joint auditors jointly liable for work individually allocated to one auditor in signed work allocation document."
        ),
        SyllabusTopic(
            id = "p5_ch6_sa450_560_570_580",
            paper = PaperCode.PAPER_5,
            chapter = "Completion & Review",
            topicName = "Chapter 6: SA 450, SA 560, SA 570 & SA 580 (Subsequent Events & Going Concern)",
            moduleName = "Module 2",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "4/7 Attempts",
            averageMarks = 5.0,
            trendDirection = "Booster Review Standards",
            writtenTarget = 4,
            mcqTarget = 20,
            oralRecallTarget = 2,
            oralRhythmNote = "SA 570 Going Concern disclosure & SA 580 written representations",
            blueprint = StandardBlueprint(
                scope = "Procedures during final review stage: evaluating misstatements, subsequent events up to report date, appropriateness of going concern assumption, and written representations.",
                recognitionAndMeasurement = "SA 450: Accumulate misstatements identified during audit (other than clearly trivial); request management to correct; evaluate uncorrected misstatements against materiality. SA 560: Subsequent events between BS date and report date; inquire events affecting conditions. SA 570: Going concern indicators (financial, operating, other). If material uncertainty exists but adequate disclosure made: Unmodified opinion + 'Material Uncertainty Related to Going Concern' paragraph. If disclosure inadequate: Qualified or Adverse opinion. SA 580: Written representations support other evidence; CANNOT substitute for other evidence.",
                disclosure = "Material Uncertainty Related to Going Concern paragraph in Auditor's Report if appropriate disclosure made; unmodified opinion not affected."
            ),
            keyProvisions = listOf(
                "SA 450: Distinguish factual, judgmental, and projected misstatements; communicate all uncorrected misstatements to TCWG",
                "SA 560: Subsequent events: Perform procedures to identify events occurring between balance sheet date and date of auditor's report",
                "SA 570: If going concern assumption is valid but material uncertainty exists: If disclosed adequately in notes -> Unmodified Opinion with separate 'Material Uncertainty Related to Going Concern' section; If NOT disclosed -> Qualified or Adverse Opinion",
                "SA 580: Written representations are necessary audit evidence but DO NOT provide sufficient appropriate evidence on their own for matters where other evidence is expected"
            ),
            commonExaminerPitfalls = "Issuing a disclaimer of opinion simply because a going concern material uncertainty exists; relying exclusively on written management representation without corroborative evidence."
        ),

        // =========================================================================
        // PAPER 6: Financial Management & Strategic Management (ICAI CA Intermediate)
        // 12-Exam Trend Dec 2021 to May 2026 Strategy Blueprint
        // =========================================================================

        // --- Section A: Financial Management (Tier 1 MUST) ---
        SyllabusTopic(
            id = "p6_fm_working_capital",
            paper = PaperCode.PAPER_6,
            chapter = "Financial Management",
            topicName = "Chapter 9: Management of Working Capital (Treasury, Cash & Receivables)",
            moduleName = "Financial Management",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 11.0,
            trendDirection = "Compulsory Practical Anchor • 9-12M",
            writtenTarget = 10,
            mcqTarget = 35,
            oralRecallTarget = 5,
            oralRhythmNote = "Operating Cycle (R + W + F + D - C) & Cash cost working capital daily",
            fusedWithTopics = listOf("Ratio Analysis"),
            keyProvisions = listOf(
                "Operating Cycle (OC) = Raw Material Storage Period (R) + WIP Period (W) + Finished Goods Storage (F) + Debtors Collection (D) - Creditors Payment Period (C)",
                "Estimation of Working Capital: Cash Cost Approach (excludes profit margin and depreciation from debtors and inventory) vs Total Approach",
                "Cash Management Models: Baumol Model (Optimum Cash = sqrt((2 * Annual Cash * Transaction Cost) / Opportunity Cost)); Miller-Orr Model (Return Point Z = Lower Limit + (3 * Variance * Trans Cost / 4 * Interest)^(1/3))",
                "Receivables Management: Evaluating credit policies by comparing incremental contribution with incremental carrying cost of receivables (at variable/cash cost) and bad debts"
            ),
            commonExaminerPitfalls = "Valuing debtors at selling price including profit when calculating working capital on cash cost basis; omitting lead time in cash management."
        ),
        SyllabusTopic(
            id = "p6_wacc",
            paper = PaperCode.PAPER_6,
            chapter = "Financial Management",
            topicName = "Chapter 4: Cost of Capital (Ke, Kd, Kp, and WACC)",
            moduleName = "Financial Management",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 8.5,
            trendDirection = "Core Valuation Pillar • 7-10M",
            writtenTarget = 8,
            mcqTarget = 35,
            oralRecallTarget = 4,
            oralRhythmNote = "WACC market value weights formula & CAPM Ke = Rf + Beta*(Rm - Rf) daily",
            fusedWithTopics = listOf("Capital Structure", "Investment Decisions"),
            keyProvisions = listOf(
                "Cost of Redeemable Debt (Kd) = (Interest * (1 - t) + (RV - NP) / n) / ((RV + NP) / 2)",
                "Cost of Redeemable Preference (Kp) = (Pref Dividend + (RV - NP) / n) / ((RV + NP) / 2)",
                "Cost of Equity (Ke): CAPM = Rf + Beta * (Rm - Rf); Dividend Growth Model Ke = (D1 / P0) + g; Earnings Yield Ke = EPS / P0",
                "Weighted Average Cost of Capital (WACC): WACC = (Ke * We) + (Kd * Wd) + (Kp * Wp); Market Value weights preferred over Book Value weights"
            ),
            commonExaminerPitfalls = "Forgetting the tax shield (1 - t) on debenture interest; using Book Value weights when market values are available."
        ),
        SyllabusTopic(
            id = "p6_fm_capital_budgeting",
            paper = PaperCode.PAPER_6,
            chapter = "Financial Management",
            topicName = "Chapter 7: Investment Decisions (Capital Budgeting Techniques)",
            moduleName = "Financial Management",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "11/12 Exams",
            averageMarks = 8.0,
            trendDirection = "Consistent 6-9M Core Problem",
            writtenTarget = 8,
            mcqTarget = 35,
            oralRecallTarget = 4,
            oralRhythmNote = "NPV & IRR interpolation formula every 2nd day",
            fusedWithTopics = listOf("Cost of Capital"),
            keyProvisions = listOf(
                "Net Present Value (NPV): PV of Cash Inflows minus Initial Cash Outflow; accept if NPV > 0; theoretically the soundest criteria for shareholder wealth",
                "Internal Rate of Return (IRR): Discount rate at which NPV = 0; interpolation: IRR = Lower Rate + (NPV at LR / (NPV at LR - NPV at HR)) * (HR - LR)",
                "Profitability Index (PI) = Present Value of Cash Inflows / Initial Outlay; ideal for ranking mutually exclusive projects under Capital Rationing",
                "Modified IRR (MIRR): Resolves multiple IRR and reinvestment rate flaws by assuming cash inflows are reinvested at WACC / cost of capital"
            ),
            commonExaminerPitfalls = "Using Accounting Profit instead of Cash Flow after Tax (CFAT = PAT + Depreciation) in NPV/IRR calculations; confusing IRR interpolation signs."
        ),

        // --- Section A: Financial Management (Tier 2 High Yield) ---
        SyllabusTopic(
            id = "p6_fm_capital_structure",
            paper = PaperCode.PAPER_6,
            chapter = "Financial Management",
            topicName = "Chapter 5: Financing Decisions - Capital Structure Theories & EBIT-EPS",
            moduleName = "Financial Management",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "10/12 Exams",
            averageMarks = 9.0,
            trendDirection = "High Yield 8-12M Problem",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "EBIT-EPS Indifference point formula & MM arbitrage every 2nd day",
            fusedWithTopics = listOf("Cost of Capital", "Leverages"),
            keyProvisions = listOf(
                "Net Income (NI) Theory (Durand): Higher leverage increases total firm value and lowers overall WACC because Kd < Ke",
                "Net Operating Income (NOI) Theory: WACC and total value remain CONSTANT regardless of leverage; Ke increases linearly with financial risk",
                "Modigliani-Miller (MM) Hypothesis: Without taxes -> Value is independent of leverage (Arbitrage Proof); With corporate taxes -> Value of Levered Firm = Value of Unlevered Firm + (Tax Rate * Debt)",
                "EBIT-EPS Indifference Point: Level of EBIT where EPS is identical under two alternative financing plans: ((EBIT - I1)(1 - t) - PD1) / N1 = ((EBIT - I2)(1 - t) - PD2) / N2"
            ),
            commonExaminerPitfalls = "Neglecting to gross up preference dividend by (1 - t) when calculating EBIT-EPS indifference point; assuming MM without tax favors high debt."
        ),
        SyllabusTopic(
            id = "p6_fm_dividend_decisions",
            paper = PaperCode.PAPER_6,
            chapter = "Financial Management",
            topicName = "Chapter 8: Dividend Decisions (Walter, Gordon & MM Models)",
            moduleName = "Financial Management",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "9/12 Exams",
            averageMarks = 7.0,
            trendDirection = "High Yield 6-8M Problem",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 3,
            oralRhythmNote = "Walter Model P = (D + (r/Ke)*(E-D)) / Ke every 3rd day",
            fusedWithTopics = listOf("Cost of Capital"),
            keyProvisions = listOf(
                "Walter's Model: P = (D + (r / Ke) * (E - D)) / Ke; If r > Ke (growth firm) -> Optimum payout 0%; If r < Ke (declining firm) -> Optimum payout 100%; If r = Ke -> Dividend is indifferent",
                "Gordon's Model: P0 = (E1 * (1 - b)) / (Ke - (b * r)) where b = retention ratio and b*r = growth rate g; assumes Ke > b*r",
                "Modigliani-Miller (MM) Dividend Irrelevance: Market value of firm depends on earning power of assets, not dividend policy: P0 = (P1 + D1) / (1 + Ke)",
                "Traditional & Lintner's Model: Dt = c * (Target EPS * Target Payout) + (1 - c) * D(t-1) reflecting dividend smoothing"
            ),
            commonExaminerPitfalls = "Recommending 100% dividend payout for a growth company (r > Ke) under Walter's model (the optimum payout is 0%!)."
        ),
        SyllabusTopic(
            id = "p6_fm_ratio_analysis",
            paper = PaperCode.PAPER_6,
            chapter = "Financial Management",
            topicName = "Chapter 3: Financial Analysis & Planning - Ratio Analysis",
            moduleName = "Financial Management",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 5.0,
            trendDirection = "Fixed 5M in Compulsory Q1",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 4,
            oralRhythmNote = "DuPont 3-step formula & Coverage ratios every 3rd day",
            fusedWithTopics = listOf("Working Capital"),
            keyProvisions = listOf(
                "Liquidity Ratios: Current Ratio (CA / CL, benchmark 2:1), Quick / Acid-Test Ratio ((CA - Inventory - Prepaid) / CL, benchmark 1:1)",
                "Coverage & Solvency Ratios: Debt-Equity Ratio (Debt / Equity), Interest Coverage Ratio (EBIT / Interest), Debt Service Coverage Ratio (DSCR = (PAT + Dep + Int) / (Int + Principal Installment))",
                "Turnover Ratios: Inventory Turnover (COGS / Avg Inventory), Debtors Turnover (Credit Sales / Avg Debtors), Working Capital Turnover (Sales / Net Working Capital)",
                "DuPont Analysis: ROE = Net Profit Margin * Asset Turnover * Equity Multiplier = (PAT / Sales) * (Sales / Assets) * (Assets / Equity)"
            ),
            commonExaminerPitfalls = "Calculating Inventory Turnover using Sales instead of Cost of Goods Sold (COGS); omitting preference dividend from equity calculations."
        ),
        SyllabusTopic(
            id = "p6_leverage",
            paper = PaperCode.PAPER_6,
            chapter = "Financial Management",
            topicName = "Chapter 6: Financing Decisions - Leverages (DOL, DFL, DCL)",
            moduleName = "Financial Management",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 5.0,
            trendDirection = "Fixed 5M in Compulsory Q1",
            writtenTarget = 6,
            mcqTarget = 25,
            oralRecallTarget = 4,
            oralRhythmNote = "DOL = Contribution/EBIT & DFL = EBIT/(EBT - PD/(1-t)) every 3rd day",
            fusedWithTopics = listOf("Capital Structure"),
            keyProvisions = listOf(
                "Degree of Operating Leverage (DOL) = Contribution / EBIT = % Change in EBIT / % Change in Sales (measures business/operating risk)",
                "Degree of Financial Leverage (DFL) = EBIT / (EBT - (Preference Dividend / (1 - t))) = % Change in EPS / % Change in EBIT (measures financial risk)",
                "Degree of Combined Leverage (DCL) = DOL * DFL = Contribution / (EBT - (Preference Dividend / (1 - t))) = % Change in EPS / % Change in Sales",
                "Financial Break-Even Point = Interest + (Preference Dividend / (1 - t)) (EBIT level where EPS equals ZERO)"
            ),
            commonExaminerPitfalls = "Dividing EBIT by Contribution instead of Contribution / EBIT; forgetting to gross up preference dividend by (1 - t) in DFL."
        ),

        // --- Section A: Financial Management (Tier 3 & Tier 4) ---
        SyllabusTopic(
            id = "p6_fm_types_financing",
            paper = PaperCode.PAPER_6,
            chapter = "Financial Management",
            topicName = "Chapter 2: Types of Financing (Sources of Capital)",
            moduleName = "Financial Management",
            priorityTier = PriorityTier.TIER_3,
            pastPaperFrequency = "7/12 Exams",
            averageMarks = 5.0,
            trendDirection = "Moderate 4-6M Theory & Problem",
            writtenTarget = 4,
            mcqTarget = 20,
            oralRecallTarget = 3,
            oralRhythmNote = "Commercial Paper & Factoring with/without recourse",
            keyProvisions = listOf(
                "Long-term Sources: Equity shares (permanent capital, dilution risk), Preference shares (fixed dividend, cumulative/participating), Debentures (secured, tax deductible interest)",
                "Medium & Short-term Sources: Lease financing (Operating vs Finance lease), Commercial Paper (unsecured promissory note issued by highly rated corporate, maturity 7 days to 1 year)",
                "Factoring vs Forfaiting: Factoring (short-term receivables financing, with or without recourse), Forfaiting (medium-to-long term export receivables financing without recourse)",
                "International Sources: American Depository Receipts (ADR - US markets), Global Depository Receipts (GDR - European markets), Foreign Currency Convertible Bonds (FCCB)"
            ),
            commonExaminerPitfalls = "Treating Commercial Paper as a long-term source; confusing Factoring with recourse (factor does not bear bad debt) with Non-recourse factoring."
        ),
        SyllabusTopic(
            id = "p6_fm_scope_obj",
            paper = PaperCode.PAPER_6,
            chapter = "Financial Management",
            topicName = "Chapter 1: Scope and Objectives of Financial Management",
            moduleName = "Financial Management",
            priorityTier = PriorityTier.TIER_4,
            pastPaperFrequency = "5/12 Exams",
            averageMarks = 3.0,
            trendDirection = "Booster Theory 2-4M",
            writtenTarget = 3,
            mcqTarget = 15,
            oralRecallTarget = 2,
            oralRhythmNote = "Profit vs Wealth maximization & agency problem",
            keyProvisions = listOf(
                "Profit Maximization vs Wealth Maximization: Wealth maximization (maximizing Net Present Value / market value of equity shares) is superior as it considers time value of money, risk, and cash flows",
                "Core Financial Decisions: Investment decisions (Capital Budgeting), Financing decisions (Capital Structure & Cost of Capital), Dividend decisions",
                "Agency Problem: Conflict of interest between shareholders (principals) and management (agents); addressed through executive stock options, performance-linked compensation, and governance",
                "Role of Chief Financial Officer (CFO): Transition from traditional scorekeeper to strategic partner involved in resource allocation, risk management, and value creation"
            ),
            commonExaminerPitfalls = "Advocating Profit Maximization as the primary corporate objective over Wealth Maximization; ignoring risk and time value in financial objectives."
        ),

        // --- Section B: Strategic Management (Tier 1 MUST) ---
        SyllabusTopic(
            id = "p6_sm_strategic_choices",
            paper = PaperCode.PAPER_6,
            chapter = "Strategic Management",
            topicName = "Chapter 4: Strategic Choices (Generic Strategies & Corporate Options)",
            moduleName = "Strategic Management",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 12.0,
            trendDirection = "Compulsory Heavy Weight • 10-14M",
            writtenTarget = 10,
            mcqTarget = 35,
            oralRecallTarget = 5,
            oralRhythmNote = "Porter's Generic Strategies & Growth/Integration directions daily",
            fusedWithTopics = listOf("Internal Environment"),
            keyProvisions = listOf(
                "Michael Porter's Generic Strategies: Cost Leadership (broad low cost), Differentiation (broad unique features), Focus Cost (narrow niche low cost), Focus Differentiation (narrow niche unique)",
                "Grand Corporate Strategies: Stability Strategy, Expansion / Growth Strategy, Retrenchment / Turnaround Strategy, Combination Strategy",
                "Growth Directions: Intensification (market penetration, product development), Integration (Forward integration closer to customer; Backward integration closer to raw material), Diversification (Concentric vs Conglomerate)",
                "Strategic Alliances & Joint Ventures: Collaborative partnerships sharing risks and resources without complete legal merger"
            ),
            commonExaminerPitfalls = "Confusing Forward Integration (manufacturer opening retail stores) with Backward Integration (manufacturer buying raw material mine)."
        ),
        SyllabusTopic(
            id = "p6_sm_implementation",
            paper = PaperCode.PAPER_6,
            chapter = "Strategic Management",
            topicName = "Chapter 5: Strategy Implementation and Control (Structure, Culture, Balanced Scorecard)",
            moduleName = "Strategic Management",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 12.5,
            trendDirection = "Compulsory Heavy Weight • 10-15M",
            writtenTarget = 10,
            mcqTarget = 35,
            oralRecallTarget = 5,
            oralRhythmNote = "Balanced Scorecard (4 perspectives) & Lewin's Change model daily",
            fusedWithTopics = listOf("Strategic Choices"),
            keyProvisions = listOf(
                "Strategy-Structure Alignment: Structure follows strategy (Chandler); Organizational structures: Functional, Divisional, Strategic Business Unit (SBU), Matrix, Network structure",
                "Strategic Leadership: Transformational Leadership (inspires vision, motivates change) vs Transactional Leadership (clarifies roles, uses reward/penalty systems)",
                "Strategic Change Process (Kurt Lewin): Unfreezing the existing situation -> Changing to the new state -> Refreezing the new behavior and culture",
                "Strategic Control Types: Premise Control, Implementation Control, Strategic Surveillance, Special Alert Control",
                "Balanced Scorecard (Kaplan & Norton): 4 Perspectives: Financial Perspective, Customer Perspective, Internal Business Processes, Learning and Growth"
            ),
            commonExaminerPitfalls = "Focusing solely on financial metrics in the Balanced Scorecard; assuming Strategy follows Structure (Structure must follow Strategy!)."
        ),
        SyllabusTopic(
            id = "p6_sm_intro",
            paper = PaperCode.PAPER_6,
            chapter = "Strategic Management",
            topicName = "Chapter 1: Introduction to Strategic Management (Intent, Vision, Levels)",
            moduleName = "Strategic Management",
            priorityTier = PriorityTier.TIER_1,
            pastPaperFrequency = "12/12 Exams",
            averageMarks = 10.0,
            trendDirection = "High Yield Foundation • 9-12M",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Vision, Mission, Objectives & 3 Strategy levels daily",
            fusedWithTopics = listOf("Strategic Choices"),
            keyProvisions = listOf(
                "Strategic Intent Hierarchy: Vision (where we want to be), Mission (who we are and what we do), Business Definition, Objectives & Goals (concrete quantifiable milestones)",
                "Strategic Management Process: Strategic Analysis -> Strategy Formulation -> Strategy Implementation -> Strategy Evaluation & Control",
                "Three Levels of Strategy: Corporate Level (growth, diversification, M&A - CEO/Board), Business Level (competitive advantage in SBU - Division heads), Functional Level (marketing, operations, HR, finance)",
                "Strategic Management in Not-for-Profit organizations (educational, medical, governmental): Focuses on public service, resource stewardship, and social impact rather than financial profit"
            ),
            commonExaminerPitfalls = "Confusing Vision (future aspiration) with Mission (current operational purpose); mixing corporate level decisions with functional level actions."
        ),

        // --- Section B: Strategic Management (Tier 2 High Yield) ---
        SyllabusTopic(
            id = "p6_sm_matrices",
            paper = PaperCode.PAPER_6,
            chapter = "Strategic Management",
            topicName = "Chapter 3: Strategic Analysis - Internal Environment (SWOT, BCG, Ansoff, Value Chain)",
            moduleName = "Strategic Management",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "11/12 Exams",
            averageMarks = 12.0,
            trendDirection = "High Yield Matrix Questions • 10-14M",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "BCG, Ansoff & Value Chain primary/support activities every 2nd day",
            fusedWithTopics = listOf("Strategic Choices"),
            keyProvisions = listOf(
                "SWOT & TOWS Matrix: SO strategies (use strengths to capture opportunities), WO strategies (overcome weaknesses by taking opportunities), ST strategies (use strengths to avoid threats), WT strategies (defensive)",
                "BCG Growth-Share Matrix: Market Growth Rate (vertical) vs Relative Market Share (horizontal) -> Stars (high growth/high share), Cash Cows (low growth/high share), Question Marks (high growth/low share), Dogs (low/low)",
                "Ansoff's Product-Market Matrix: Market Penetration (Existing/Existing), Market Development (New/Existing), Product Development (Existing/New), Diversification (New/New)",
                "Porter's Value Chain Analysis: Primary Activities (Inbound Logistics, Operations, Outbound Logistics, Marketing & Sales, Service) + Support Activities (Infrastructure, HR, Tech Development, Procurement)",
                "Core Competence (Prahalad & Hamel): Provides customer value, difficult to imitate, and provides potential access to a wide variety of markets (VRIO Framework)"
            ),
            commonExaminerPitfalls = "Inverting the axes of BCG Matrix (Market Share is horizontal, Market Growth is vertical!); classifying Market Penetration as Diversification."
        ),
        SyllabusTopic(
            id = "p6_sm_external_analysis",
            paper = PaperCode.PAPER_6,
            chapter = "Strategic Management",
            topicName = "Chapter 2: Strategic Analysis - External Environment (PESTLE & Porter's 5 Forces)",
            moduleName = "Strategic Management",
            priorityTier = PriorityTier.TIER_2,
            pastPaperFrequency = "10/12 Exams",
            averageMarks = 10.0,
            trendDirection = "High Yield Competitive Analysis • 8-12M",
            writtenTarget = 8,
            mcqTarget = 30,
            oralRecallTarget = 4,
            oralRhythmNote = "Porter's 5 Forces & Strategic group mapping every 2nd day",
            fusedWithTopics = listOf("Strategic Choices"),
            keyProvisions = listOf(
                "PESTLE Framework: Evaluates Macro environment through Political, Economic, Socio-cultural, Technological, Legal, and Environmental dimensions",
                "Porter's Five Forces of Industry Competitiveness: (1) Threat of New Entrants, (2) Bargaining Power of Buyers, (3) Bargaining Power of Suppliers, (4) Threat of Substitutes, (5) Rivalry Among Existing Competitors",
                "Barriers to Entry: Economies of scale, product differentiation, capital requirements, switching costs, access to distribution channels, government policy",
                "Strategic Group Mapping: Graphical technique for plotting competitors on two non-correlated strategic dimensions (e.g., price vs quality) to identify competitive clusters"
            ),
            commonExaminerPitfalls = "Confusing Substitutes (alternative product performing same function, e.g., tea vs coffee) with direct competitors (Tata Tea vs Red Label)."
        )
    )

    fun getTopicsForPaper(paper: PaperCode): List<SyllabusTopic> {
        return topics.filter { it.paper == paper }
    }
}
