package com.example.data.curriculum

import com.example.data.model.ComparisonRow
import com.example.data.model.ConceptDifference
import com.example.data.model.PaperCode
import com.example.data.model.PriorityTier
import com.example.data.model.StatutoryCheatMatrix
import com.example.data.model.StatutoryMatrixRow

object CuratedDifferences {

    val differences: List<ConceptDifference> by lazy {
        listOf(
            // =========================================================================
            // PAPER 1: Advanced Accounting (5 High-Yield Comparisons)
            // =========================================================================
            ConceptDifference(
                id = "diff_p1_as14_merger_purchase",
                paper = PaperCode.PAPER_1,
                title = "AS 14 Amalgamation: Pooling of Interests (Merger) vs Purchase Method",
                conceptA = "Amalgamation in Nature of Merger (Pooling of Interests)",
                conceptB = "Amalgamation in Nature of Purchase",
                comparisonPoints = listOf(
                    ComparisonRow("Conditions Required", "All 5 statutory conditions of AS 14 must be satisfied simultaneously.", "If even ONE of the 5 conditions of AS 14 is NOT satisfied."),
                    ComparisonRow("Asset & Liability Valuation", "Recorded at existing book values (except for alignment of accounting policies).", "Recorded at agreed fair values or existing book values as per scheme."),
                    ComparisonRow("Treatment of Reserves", "All reserves (General, Capital, P&L, Statutory) of transferor entity are retained.", "ONLY Statutory Reserves (e.g. Tonnage Tax, Investment Allowance) are retained."),
                    ComparisonRow("Difference in PC & Equity", "Difference between PC and Share Capital of transferor adjusted in General Reserve.", "Difference between PC and Net Assets taken over credited to Capital Reserve or debited to Goodwill."),
                    ComparisonRow("Statutory Reserve Reserve Account", "No Statutory Reserve Amalgamation Account is created.", "Statutory Reserve created by debiting 'Amalgamation Adjustment Reserve Account'.")
                ),
                memoryHook = "Merger = Book Values & All Reserves. Purchase = Fair Values & Goodwill/Capital Reserve.",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "6/6 Attempts Compulsory",
                examinerTrapNote = "ICAI Trap: Adding debenture payment to Purchase Consideration. PC includes ONLY payments made to equity & preference shareholders!"
            ),
            ConceptDifference(
                id = "diff_p1_as19_leases",
                paper = PaperCode.PAPER_1,
                title = "AS 19 Leases: Finance Lease vs Operating Lease",
                conceptA = "Finance Lease (AS 19)",
                conceptB = "Operating Lease (AS 19)",
                comparisonPoints = listOf(
                    ComparisonRow("Risk & Rewards Transfer", "Transfers substantially ALL risks and rewards incidental to ownership to lessee.", "Does NOT transfer substantially all risks and rewards incidental to ownership."),
                    ComparisonRow("Balance Sheet Recognition", "Asset and equivalent liability recognized in Lessee's Balance Sheet at Present Value of MLP.", "No asset or liability in Lessee's B/S; lease payments expensed straight-line in P&L."),
                    ComparisonRow("Depreciation Claim", "Claimed by Lessee on capitalized asset in Lessee's income statement.", "Claimed by Lessor in Lessor's income statement."),
                    ComparisonRow("Implicit Interest Rate", "Lease payment split into Finance Charge (P&L debit) and principal reduction.", "Entire lease rent charged to P&L as operating expense."),
                    ComparisonRow("Substance over Form", "Legal title may/may not pass, but economic substance makes it an asset purchase.", "Simple rental hire arrangement where legal title and risks stay with lessor.")
                ),
                memoryHook = "Finance Lease = Asset on Lessee's B/S + Lessee claims depreciation. Operating Lease = Rent in P&L.",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "5/6 Attempts",
                examinerTrapNote = "ICAI Trap: Present Value of Minimum Lease Payments >= 90% of Fair Value automatically qualifies lease as Finance Lease."
            ),
            ConceptDifference(
                id = "diff_p1_as5_prior_period_policy",
                paper = PaperCode.PAPER_1,
                title = "AS 5: Prior Period Items vs Change in Accounting Policy",
                conceptA = "Prior Period Items (AS 5)",
                conceptB = "Change in Accounting Policy (AS 5)",
                comparisonPoints = listOf(
                    ComparisonRow("Nature / Origin", "Arises from errors, mathematical mistakes, or omissions in earlier financial statements.", "Deliberate change in accounting principles/bases adopted for financial statements."),
                    ComparisonRow("Permissibility", "Unintended error discovered subsequently; not a matter of choice or statutory mandate.", "Permitted ONLY IF required by statute, AS compliance, or better presentation."),
                    ComparisonRow("P&L Disclosure", "Shown separately in current P&L after determining profit/loss from ordinary activities.", "Impact disclosed in notes/P&L in period of change and future periods if material."),
                    ComparisonRow("Quantification", "Exact error amount identified from prior years is stated separately.", "Financial impact quantified. If unascertainable, the fact must be disclosed."),
                    ComparisonRow("ICAI Exam Example", "Omitting invoice of ₹ 5 Lakhs in FY 2024-25 discovered during audit of FY 2025-26.", "Switching inventory valuation method from Weighted Average to FIFO.")
                ),
                memoryHook = "Error in past = Prior Period. Choice for better presentation = Accounting Policy.",
                priorityTier = PriorityTier.TIER_2,
                pastExamFrequency = "4/6 Attempts",
                examinerTrapNote = "ICAI Trap: Revision of estimate (e.g. useful life of machinery) is NEITHER prior period nor policy change—it is prospective!"
            ),
            ConceptDifference(
                id = "diff_p1_buyback_reconstruction",
                paper = PaperCode.PAPER_1,
                title = "Buyback of Securities (Sec 68) vs Internal Reconstruction (Sec 66)",
                conceptA = "Buyback of Securities (Section 68)",
                conceptB = "Internal Reconstruction (Section 66)",
                comparisonPoints = listOf(
                    ComparisonRow("Liquidity Impact", "Outflow of cash/funds to buy back shares from existing shareholders.", "No outflow of cash; re-alignment of existing capital and sacrifices by stakeholders."),
                    ComparisonRow("Court/NCLT Approval", "NCLT approval NOT required; Board / Special Resolution sufficient.", "Mandatory approval and sanction order required from NCLT."),
                    ComparisonRow("Capital Redemption Reserve", "Mandatory creation of CRR equal to nominal value of shares bought back out of free reserves.", "Sacrifices credited to 'Capital Reduction Account' to write off accumulated losses."),
                    ComparisonRow("Max Ceiling / Limits", "Max 25% of paid-up capital + free reserves in any financial year.", "No statutory percentage cap; subject to agreement and NCLT approval."),
                    ComparisonRow("Words 'And Reduced'", "Not required to append 'And Reduced' to company name.", "Must append words 'And Reduced' to Balance Sheet name if ordered by NCLT.")
                ),
                memoryHook = "Buyback = Pay cash to shareholders + Create CRR. Internal Reconstruction = Reorganize losses + NCLT sanction.",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "6/6 Attempts",
                examinerTrapNote = "ICAI Trap: CRR can ONLY be utilized for issuing fully paid bonus shares—never for paying cash dividends!"
            ),
            ConceptDifference(
                id = "diff_p1_as21_as23_as27",
                paper = PaperCode.PAPER_1,
                title = "Group Accounting: Subsidiary (AS 21) vs Associate (AS 23) vs Joint Venture (AS 27)",
                conceptA = "Subsidiary Entity (AS 21)",
                conceptB = "Associate (AS 23) / Joint Venture (AS 27)",
                comparisonPoints = listOf(
                    ComparisonRow("Control Threshold", "Control > 50% voting power or composition of Board of Directors.", "Associate: Significant Influence (20% to 50%). JV: Joint Control under contract."),
                    ComparisonRow("Consolidation Method", "Full Line-by-Line consolidation of Assets, Liabilities, Income, and Expenses.", "Associate: Equity Method (One-line investment adjustment). JV: Proportionate Consolidation."),
                    ComparisonRow("Minority Interest", "Recognized separately in Consolidated Balance Sheet for outside share.", "No Minority Interest recognized in Equity Method for Associates."),
                    ComparisonRow("Cost of Control / Goodwill", "Goodwill/Capital Reserve calculated on date of control acquisition.", "Goodwill/Capital Reserve included within carrying amount of investment."),
                    ComparisonRow("Inter-Group Profits", "100% downstream unrealized profit eliminated (or pro-rata upstream).", "Unrealized profit eliminated ONLY to the extent of investor's interest.")
                ),
                memoryHook = "AS 21 > 50% (Line-by-Line). AS 23 = 20-50% (Equity Method). AS 27 = Contractual Joint Control.",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "6/6 Attempts",
                examinerTrapNote = "ICAI Trap: Holding 18% voting power WITH Board representation creates Significant Influence (AS 23 applies)."
            ),

            // =========================================================================
            // PAPER 2: Corporate and Other Laws (5 High-Yield Comparisons)
            // =========================================================================
            ConceptDifference(
                id = "diff_p2_sec185_186",
                paper = PaperCode.PAPER_2,
                title = "Companies Act: Section 185 (Loans to Directors) vs Section 186 (Loans & Investments)",
                conceptA = "Section 185 (Loans to Directors)",
                conceptB = "Section 186 (Loans & Investments by Company)",
                comparisonPoints = listOf(
                    ComparisonRow("Target Beneficiary", "Prohibits/regulates loans, guarantees, or securities to DIRECTORS or interested entities.", "Regulates loans, guarantees, securities, and share investments in ANY PERSON/body corporate."),
                    ComparisonRow("General Rule", "General Rule is PROHIBITION on direct loans to directors (with narrow statutory exceptions).", "General Rule is PERMISSION up to statutory ceiling, requiring Special Resolution beyond limits."),
                    ComparisonRow("Ceiling / Approval", "Special Resolution + explanatory statement showing loan utilization for principal business.", "Board Resolution (Unanimous) up to 60% paid-up capital + free reserves + SPR OR 100% free reserves + SPR."),
                    ComparisonRow("Public Financial Inst. Approval", "PFI approval NOT mandated specifically under 185.", "Mandatory PFI approval required if any loan/term default is subsisting."),
                    ComparisonRow("Penalty for Default", "Fine ₹ 5 L to ₹ 25 L on company; Director punishable with imprisonment/fine.", "Fine ₹ 25,000 to ₹ 5 Lakhs on company and officer in default.")
                ),
                memoryHook = "Sec 185 = NO Loans to Directors (Strict prohibition). Sec 186 = 60%/100% Limits on Investments/Loans.",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "6/6 Attempts",
                examinerTrapNote = "ICAI Trap: Loan to Wholly Owned Subsidiary (WOS) is EXEMPT u/s 185 provided loan is used for principal business activities."
            ),
            ConceptDifference(
                id = "diff_p2_moa_aoa",
                paper = PaperCode.PAPER_2,
                title = "Corporate Charter: Memorandum of Association (MOA) vs Articles of Association (AOA)",
                conceptA = "Memorandum of Association (MOA)",
                conceptB = "Articles of Association (AOA)",
                comparisonPoints = listOf(
                    ComparisonRow("Definition & Scope", "Constitution and charter of company defining external boundaries and scope of powers.", "Internal regulations and bye-laws governing management and operational conduct."),
                    ComparisonRow("Relationship Regulated", "Regulates relationship between company and OUTSIDE WORLD (creditors, public, buyers).", "Regulates relationship between company and its MEMBERS, and among members inter se."),
                    ComparisonRow("Subordination", "Supreme document; subordinate ONLY to Companies Act 2013.", "Subordinate to BOTH Companies Act 2013 and Memorandum of Association."),
                    ComparisonRow("Ultra Vires Acts", "Acts beyond MOA are NULL & VOID and CANNOT be ratified even by 100% shareholders.", "Acts beyond AOA (but within MOA) can be RATIFIED by shareholders by Special Resolution."),
                    ComparisonRow("Mandatory Clauses", "Contains 6 mandatory clauses (Name, Registered Office, Object, Liability, Capital, Association).", "Contains internal rules (Share transfer, Board meetings, Voting, Dividends, Winding up).")
                ),
                memoryHook = "MOA = External Charter (Ultra Vires = Void). AOA = Internal Bye-laws (Can be ratified).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "12/12 Attempts",
                examinerTrapNote = "ICAI Trap: Any provision in AOA contrary to MOA or Companies Act is void ab initio."
            ),
            ConceptDifference(
                id = "diff_p2_fixed_floating_charge",
                paper = PaperCode.PAPER_2,
                title = "Company Charges: Fixed Charge vs Floating Charge",
                conceptA = "Fixed Charge",
                conceptB = "Floating Charge",
                comparisonPoints = listOf(
                    ComparisonRow("Subject Asset", "Attaches to specific, identifiable, permanent assets (e.g. Land, Building, Heavy Machinery).", "Hovers over a shifting, fluctuating class of present & future assets (e.g. Stock-in-trade, Debtors)."),
                    ComparisonRow("Asset Disposal by Company", "Company CANNOT sell/transfer asset without explicit consent of charge-holder.", "Company can freely deal with, sell, or dispose of assets in ordinary course of business."),
                    ComparisonRow("Crystallization", "Fixed from day one; no event needed to fix charge on the asset.", "Floating until 'Crystallization' event (default, winding up, receiver appointment), becoming fixed."),
                    ComparisonRow("Registration u/s 77", "Mandatory registration with ROC in Form CHG-1 within 30 days.", "Mandatory registration with ROC in Form CHG-1 within 30 days."),
                    ComparisonRow("Priority in Winding Up", "Top priority payout from proceeds of specific charged asset.", "Subordinate to preferential creditors (workmen dues, taxes) u/s 326.")
                ),
                memoryHook = "Fixed Charge = Specific Asset (Cannot sell). Floating Charge = Fluctuating Stock (Sells until Crystallizes).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "5/6 Attempts",
                examinerTrapNote = "ICAI Trap: Delay in ROC charge registration can be condoned by ROC up to 60 days (with additional fees)."
            ),
            ConceptDifference(
                id = "diff_p2_void_illegal_agreement",
                paper = PaperCode.PAPER_2,
                title = "Contract Law: Void Agreement vs Illegal Agreement",
                conceptA = "Void Agreement (Sec 2(g))",
                conceptB = "Illegal Agreement",
                comparisonPoints = listOf(
                    ComparisonRow("Enforceability & Legality", "Not enforceable by law, but not necessarily forbidden or punishable under penal laws.", "Forbidden by law, opposed to public policy, or involves commission of a crime/offense."),
                    ComparisonRow("Collateral Transactions", "Collateral agreements to a void agreement remain VALID and enforceable.", "Collateral agreements to an illegal agreement are AUTOMATICALLY VOID."),
                    ComparisonRow("Punishment / Fine", "Parties are NOT liable to criminal prosecution or fine under Penal Code.", "Parties are subject to prosecution, fine, or imprisonment under criminal statute."),
                    ComparisonRow("Restitution (Sec 65)", "Restitution / benefit recovery allowed if agreement discovered void.", "No restitution allowed; court leaves parties as they are ('In pari delicto')."),
                    ComparisonRow("ICAI Exam Example", "Agreement in restraint of trade (Sec 27) or agreement with a minor.", "Agreement to pay bribe or traffic public offices.")
                ),
                memoryHook = "All Illegal agreements are Void, but NOT all Void agreements are Illegal!",
                priorityTier = PriorityTier.TIER_2,
                pastExamFrequency = "12/12 Attempts",
                examinerTrapNote = "ICAI Trap: Money lent to perform a void agreement is recoverable, but money lent for an illegal agreement is unrecoverable."
            ),
            ConceptDifference(
                id = "diff_p2_fema_current_capital",
                paper = PaperCode.PAPER_2,
                title = "FEMA 1999: Current Account Transactions vs Capital Account Transactions",
                conceptA = "Current Account Transactions (Sec 2(j))",
                conceptB = "Capital Account Transactions (Sec 2(e))",
                comparisonPoints = listOf(
                    ComparisonRow("Impact on Balance Sheet", "Does NOT alter assets or liabilities outside India (or contingent liabilities).", "ALTERS assets/liabilities outside India of Indian resident or in India of foreign resident."),
                    ComparisonRow("General Freedom", "Generally FREE unless explicitly restricted under Schedule I, II, or III rules.", "Generally RESTRICTED unless explicitly permitted by RBI/FEMA regulations."),
                    ComparisonRow("Examples", "Foreign trade payments, interest on loans, family maintenance, medical expenses, overseas travel.", "Acquiring foreign real estate, FDI, ODI, ECB loans, foreign portfolio investment."),
                    ComparisonRow("LRS Limit", "USD 250,000 per FY under LRS for individuals for permitted current transactions.", "USD 250,000 per FY under LRS for buying foreign equity/property."),
                    ComparisonRow("Governing Authority", "Ministry of Finance / Central Govt defines prohibited Schedule lists.", "Reserve Bank of India (RBI) frames Capital Account Regulations.")
                ),
                memoryHook = "Current = Income/Expense (No B/S change). Capital = Alters Foreign Asset/Liability on Balance Sheet.",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "6/6 Attempts",
                examinerTrapNote = "ICAI Trap: Remittance for lottery, racing, or purchase of banned sweepstakes is PROHIBITED u/s 5 Schedule I."
            ),

            // =========================================================================
            // PAPER 3: Taxation (5 High-Yield Comparisons)
            // =========================================================================
            ConceptDifference(
                id = "diff_p3_default_old_tax_regime",
                paper = PaperCode.PAPER_3,
                title = "Income Tax: Sec 115BAC Default Tax Regime vs Old Tax Regime",
                conceptA = "Default Tax Regime u/s 115BAC",
                conceptB = "Old Tax Regime (Optional)",
                comparisonPoints = listOf(
                    ComparisonRow("Basic Exemption Limit", "₹ 300,000 for all individuals (regardless of age/senior citizen status).", "₹ 250,000 (Individual <60 yrs), ₹ 300,000 (Senior 60-80 yrs), ₹ 500,000 (Super Senior >80 yrs)."),
                    ComparisonRow("Standard Deduction (Salaries)", "₹ 75,000 allowed for Salaried employees in FY 2025-26.", "₹ 50,000 allowed for Salaried employees."),
                    ComparisonRow("Section 87A Rebate Ceiling", "Rebate up to ₹ 25,000 if taxable income <= ₹ 700,000 (Zero tax up to ₹ 7 Lakhs).", "Rebate up to ₹ 12,500 if taxable income <= ₹ 500,000."),
                    ComparisonRow("Deductions Allowed", "Chapter VI-A (80C, 80D, 80TTA) DISALLOWED except 80CCD(2) employer NPS contribution.", "All Chapter VI-A deductions (80C, 80D, 80G, 80TTA) fully allowed."),
                    ComparisonRow("House Property Loss Set-off", "Self-occupied house interest u/s 24(b) DISALLOWED; HP loss CANNOT be set off against Salary.", "Self-occupied interest up to ₹ 200,000 allowed; HP loss set off up to ₹ 2 Lakhs against other heads.")
                ),
                memoryHook = "Default 115BAC = Lower Slab Rates + ₹ 75k Std Deduction, NO 80C/80D. Old Regime = Higher Slabs + All Deductions.",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "12/12 Attempts Compulsory Q1",
                examinerTrapNote = "ICAI Trap: Sec 115BAC is DEFAULT. Salaried individuals can opt out every year at ITR filing; business individuals can opt out ONCE."
            ),
            ConceptDifference(
                id = "diff_p3_tds_194c_194j",
                paper = PaperCode.PAPER_3,
                title = "Income Tax TDS: Section 194C (Contracts) vs Section 194J (Professionals/Technical)",
                conceptA = "Section 194C (Work Contracts)",
                conceptB = "Section 194J (Professional & Technical Fees)",
                comparisonPoints = listOf(
                    ComparisonRow("Scope of Work", "Carrying out any 'work' including advertising, broadcasting, catering, transport of goods/passengers.", "Fees for professional services, technical services (FTS), royalty, non-compete fees, director fees."),
                    ComparisonRow("TDS Rate", "1% if payee is Individual/HUF; 2% for company/firm/others.", "10% for professional/royalty; 2% for Fees for Technical Services (FTS) & call centers."),
                    ComparisonRow("Threshold Exemption", "Single contract <= ₹ 30,000 AND aggregate contracts in FY <= ₹ 100,000.", "₹ 30,000 separate threshold for each of the 4 categories."),
                    ComparisonRow("Transport Operators", "NO TDS if goods transport operator owns <= 10 trucks and furnishes PAN declaration.", "No transport exemption exists under Section 194J."),
                    ComparisonRow("Director Remuneration", "Does not cover director sitting fees.", "100% TDS u/s 194J(1)(ba) without ANY threshold exemption.")
                ),
                memoryHook = "194C = Physical Work (1%/2%, 30k/1L). 194J = Professional Brains (10%/2%, 30k).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "12/12 Attempts",
                examinerTrapNote = "ICAI Trap: Maintenance contract involving routine servicing is 194C (2%), but specialized technical audit is 194J (2%/10%)."
            ),
            ConceptDifference(
                id = "diff_p3_gst_composite_mixed",
                paper = PaperCode.PAPER_3,
                title = "GST Section 8: Composite Supply vs Mixed Supply",
                conceptA = "Composite Supply (Sec 2(30) & Sec 8(a))",
                conceptB = "Mixed Supply (Sec 2(74) & Sec 8(b))",
                comparisonPoints = listOf(
                    ComparisonRow("Bundling Nature", "Consists of two or more supplies NATURALLY BUNDLED in ordinary course of business.", "Two or more individual supplies bundled together for a single price, NOT naturally bundled."),
                    ComparisonRow("Principal Supply", "One of the supplies is clearly identifiable as the 'Principal Supply'.", "No principal supply; items can be supplied independently in market."),
                    ComparisonRow("Applicable Tax Rate", "Entire bundle taxed at the GST rate applicable to the PRINCIPAL SUPPLY.", "Entire bundle taxed at the HIGHEST GST rate among all supplies in the combination."),
                    ComparisonRow("ICAI Exam Example", "Laptop sold with charger & carry bag -> Principal supply is Laptop (18%).", "Gift box with chocolates (18%), canned juice (12%), and dry fruits (28%) -> Taxed at 28%."),
                    ComparisonRow("Single Price Mandate", "Can be single price or itemized, provided naturally bundled.", "MUST be sold for a SINGLE combined price.")
                ),
                memoryHook = "Composite = Naturally bundled -> Tax at Principal Rate. Mixed = Artificially bundled for 1 price -> Tax at HIGHEST Rate.",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "6/6 Attempts",
                examinerTrapNote = "ICAI Trap: Hotel stay with complimentary breakfast is Composite Supply (taxed at hotel room rate)."
            ),
            ConceptDifference(
                id = "diff_p3_gst_fcm_rcm",
                paper = PaperCode.PAPER_3,
                title = "GST Payment Mechanism: Forward Charge (FCM) vs Reverse Charge (RCM)",
                conceptA = "Forward Charge Mechanism (FCM)",
                conceptB = "Reverse Charge Mechanism (RCM u/s 9(3)/9(4))",
                comparisonPoints = listOf(
                    ComparisonRow("Tax Payment Liability", "SUPPLIER of goods/services collects tax from recipient and deposits with Govt.", "RECIPIENT of goods/services is directly liable to pay GST to Govt."),
                    ComparisonRow("E-Credit Ledger Utilization", "Supplier can pay tax using Input Tax Credit (ITC) from E-Credit Ledger.", "Recipient MUST pay RCM tax in CASH ONLY (E-Cash Ledger); ITC CANNOT be used to pay RCM."),
                    ComparisonRow("ITC Eligibility on RCM", "Supplier claims ITC on inputs used.", "Recipient can claim 100% ITC of cash RCM tax paid in the same month."),
                    ComparisonRow("Registration Threshold", "Supplier registers if turnover exceeds ₹ 20 L / ₹ 40 L.", "Recipient paying RCM requires MANDATORY GST registration u/s 24 (Zero threshold)."),
                    ComparisonRow("Specified RCM Services", "Standard commercial supplies.", "GTA services, Legal fees by Advocates, Director fees, Renting motor vehicle, Security services.")
                ),
                memoryHook = "FCM = Supplier pays (Uses ITC). RCM = Recipient pays in CASH ONLY (Mandatory Registration).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "12/12 Attempts",
                examinerTrapNote = "ICAI Trap: Legal services supplied by Senior Advocate to a business entity with turnover > ₹ 20 L is under 100% RCM."
            ),
            ConceptDifference(
                id = "diff_p3_gst_zerorated_exempt",
                paper = PaperCode.PAPER_3,
                title = "GST Supply Classification: Zero-Rated Supply vs Exempt Supply",
                conceptA = "Zero-Rated Supply (Sec 16 IGST Act)",
                conceptB = "Exempt Supply (Sec 2(47) CGST Act)",
                comparisonPoints = listOf(
                    ComparisonRow("Scope & Cover", "Applies ONLY to Exports of goods/services and Supplies to SEZ Developer/Unit.", "Supplies attracting Nil rate, wholly exempt by notification, or non-taxable (petrol/alcohol)."),
                    ComparisonRow("Input Tax Credit (ITC)", "100% ITC allowed on inputs/input services used in zero-rated supplies.", "ITC on inputs/input services used for exempt supplies is BLOCKED u/s 17(2) & Rule 42/43."),
                    ComparisonRow("Refund Eligibility", "Exporter can claim 100% refund of unutilized ITC OR pay IGST and claim refund.", "No refund of input tax credit allowed for exempt supplies."),
                    ComparisonRow("Turnover Computation", "Included in Aggregate Turnover for threshold registration limit.", "Included in Aggregate Turnover, but requires ITC reversal."),
                    ComparisonRow("Bond / LUT Option", "Export can be made under Letter of Undertaking (LUT) without paying tax.", "No LUT needed as tax rate itself is Nil or notification exempt.")
                ),
                memoryHook = "Zero-Rated = Exports/SEZ (Get 100% ITC & Refund). Exempt = Nil Rate (ITC Blocked u/s 17(2)).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "6/6 Attempts",
                examinerTrapNote = "ICAI Trap: Non-taxable supplies (e.g. alcoholic liquor for human consumption) are part of Exempt Supply u/s 2(47)."
            ),

            // =========================================================================
            // PAPER 4: Cost and Management Accounting (5 High-Yield Comparisons)
            // =========================================================================
            ConceptDifference(
                id = "diff_p4_cost_control_reduction",
                paper = PaperCode.PAPER_4,
                title = "Cost Management: Cost Control vs Cost Reduction",
                conceptA = "Cost Control",
                conceptB = "Cost Reduction",
                comparisonPoints = listOf(
                    ComparisonRow("Objective", "Maintains costs within predetermined standard limits; prevents cost overruns.", "Achieves REAL and PERMANENT reduction in unit cost of product without impairing quality."),
                    ComparisonRow("Nature / Continuity", "Temporary preventive measure; ends when target cost is reached.", "Continuous, permanent dynamic process with no ultimate limit."),
                    ComparisonRow("Emphasis", "Focuses on past and present performance using Variance Analysis.", "Focuses on future improvements through research, redesign, and value engineering."),
                    ComparisonRow("Quality Impact", "Assumes current target standards without questioning product design.", "Challenges standards and aims to reduce cost while maintaining/improving quality."),
                    ComparisonRow("Techniques Used", "Standard Costing, Budgetary Control, Inventory Control.", "Value Analysis, Work Study, Re-engineering, Activity Based Management.")
                ),
                memoryHook = "Cost Control = Stick to standards. Cost Reduction = Permanently lower the standards via innovation.",
                priorityTier = PriorityTier.TIER_2,
                pastExamFrequency = "9/9 Attempts",
                examinerTrapNote = "ICAI Trap: Cost reduction should NEVER be achieved at the expense of product quality or essential features."
            ),
            ConceptDifference(
                id = "diff_p4_marginal_absorption",
                paper = PaperCode.PAPER_4,
                title = "Costing Systems: Marginal Costing vs Absorption Costing",
                conceptA = "Marginal Costing",
                conceptB = "Absorption Costing",
                comparisonPoints = listOf(
                    ComparisonRow("Fixed Overhead Treatment", "Fixed Production Overheads treated as PERIOD COSTS and charged 100% to current P&L.", "Fixed Production Overheads treated as PRODUCT COSTS and absorbed into cost per unit."),
                    ComparisonRow("Inventory Valuation", "Work-in-Progress and Finished Goods valued at VARIABLE COST ONLY.", "WIP and Finished Goods valued at TOTAL COST (Variable + Absorbed Fixed Overhead)."),
                    ComparisonRow("Under/Over Absorption", "No under or over-absorption of fixed overheads as they are not product-allocated.", "Under/Over-absorption arises due to difference between actual and budgeted overhead rates."),
                    ComparisonRow("Profit Impact (Production > Sales)", "Absorption Costing profit is HIGHER because fixed overhead is carried forward in closing stock.", "Marginal Costing profit is LOWER because all fixed overheads are expensed in current year."),
                    ComparisonRow("Decision Making", "Ideal for short-term decision making (CVP, Make or Buy, Limiting Factor).", "Mandatory for external financial reporting under AS 2 / Schedule III.")
                ),
                memoryHook = "Marginal = Fixed cost expensed 100% (Stock = Variable only). Absorption = Fixed cost in stock (Product cost).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "9/9 Attempts",
                examinerTrapNote = "ICAI Trap: Profit Difference = (Closing Stock Units - Opening Stock Units) × Fixed Overhead Rate per unit."
            ),
            ConceptDifference(
                id = "diff_p4_traditional_abc",
                paper = PaperCode.PAPER_4,
                title = "Overhead Allocation: Traditional Absorption vs Activity-Based Costing (ABC)",
                conceptA = "Traditional Absorption Costing",
                conceptB = "Activity-Based Costing (ABC)",
                comparisonPoints = listOf(
                    ComparisonRow("Cost Pools", "Overheads pooled by departments (Production & Service departments).", "Overheads pooled by ACTIVITIES (e.g. Setups, Material Handling, Inspections)."),
                    ComparisonRow("Allocation Bases", "Uses volume-based drivers (Direct Labour Hours or Machine Hours).", "Uses cause-and-effect Cost Drivers (e.g. Number of Setups, Purchase Orders, Inspections)."),
                    ComparisonRow("Product Distortion", "Over-costs high-volume simple products; under-costs low-volume complex products.", "Accurately attributes overheads to products based on actual resource consumption."),
                    ComparisonRow("Suitable Environment", "Single product or labor-intensive environment with low overhead percentage.", "Multi-product, automated environment where overheads constitute a major cost share."),
                    ComparisonRow("Decision Utility", "Poor guide for product pricing, mix, and outsourcing decisions.", "Superior insights for Activity-Based Management (ABM) and target costing.")
                ),
                memoryHook = "Traditional = Volume hours. ABC = Cause & Effect Activity Drivers (Eliminates product cross-subsidization).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "9/9 Attempts",
                examinerTrapNote = "ICAI Trap: In ABC, machine setup cost is driven by 'Number of Production Runs', NOT total machine hours!"
            ),
            ConceptDifference(
                id = "diff_p4_job_contract",
                paper = PaperCode.PAPER_4,
                title = "Method of Costing: Job Costing vs Contract Costing",
                conceptA = "Job Costing",
                conceptB = "Contract Costing",
                comparisonPoints = listOf(
                    ComparisonRow("Scale & Duration", "Applied to small, short-duration customer orders executed inside factory premises.", "Applied to large-scale, long-duration construction contracts executed at customer site."),
                    ComparisonRow("Cost Location", "Performed within factory/workshop using job cost sheets.", "Executed at external contract site; each site treated as an independent cost center."),
                    ComparisonRow("Work-in-Progress & Retention", "WIP consists of cost of uncompleted jobs inside factory.", "WIP includes Value of Work Certified + Cost of Work Uncertified less Retention Money."),
                    ComparisonRow("Notional Profit Recognition", "Profit recognized ONLY upon completion of job and delivery.", "Profit recognized progressively based on percentage of completion & Work Certified."),
                    ComparisonRow("Sub-contracts & Plant", "Sub-contracts rare; factory machines used across multiple jobs.", "Sub-contracts common; dedicated plant charged to site and credited at residual value.")
                ),
                memoryHook = "Job Costing = Small factory job (Profit at end). Contract Costing = Large site project (Progressive Profit).",
                priorityTier = PriorityTier.TIER_2,
                pastExamFrequency = "5/9 Attempts",
                examinerTrapNote = "ICAI Trap: Contractee Account is debited with Value of Work Certified, NOT total contract price!"
            ),
            ConceptDifference(
                id = "diff_p4_bincard_storesledger",
                paper = PaperCode.PAPER_4,
                title = "Material Accounting: Bin Card vs Stores Ledger",
                conceptA = "Bin Card",
                conceptB = "Stores Ledger",
                comparisonPoints = listOf(
                    ComparisonRow("Maintained By", "Maintained by the STOREKEEPER inside the warehouse bin/rack.", "Maintained by the COST ACCOUNTING DEPARTMENT in general office."),
                    ComparisonRow("Information Recorded", "Records QUANTITY ONLY (Receipts, Issues, and Balance in physical units).", "Records BOTH QUANTITY and VALUE (Rates, Amounts, and Financial Balance)."),
                    ComparisonRow("Timing of Entry", "Posted IMMEDIATELY upon physical receipt or issue of materials at store counter.", "Posted subsequently upon receipt of Goods Received Note (GRN) and Requisitions."),
                    ComparisonRow("Inter-Department Verification", "Provides physical check for perpetual inventory control.", "Provides financial reconciliation check with General Ledger."),
                    ComparisonRow("Document Nature", "Physical card attached to container or rack inside warehouse.", "Loose-leaf ledger or computerized cost accounting record.")
                ),
                memoryHook = "Bin Card = Storekeeper (Quantity Only). Stores Ledger = Cost Accountant (Quantity + Value).",
                priorityTier = PriorityTier.TIER_2,
                pastExamFrequency = "9/9 Attempts",
                examinerTrapNote = "ICAI Trap: Discrepancy between Bin Card and Stores Ledger indicates unrecorded stores requisition or theft."
            ),

            // =========================================================================
            // PAPER 5: Auditing and Ethics (5 High-Yield Comparisons)
            // =========================================================================
            ConceptDifference(
                id = "diff_p5_internal_statutory_audit",
                paper = PaperCode.PAPER_5,
                title = "Audit Types: Internal Audit (Sec 138) vs Statutory Audit (Sec 139)",
                conceptA = "Internal Audit (Section 138)",
                conceptB = "Statutory Audit (Section 139)",
                comparisonPoints = listOf(
                    ComparisonRow("Governing Provision", "Governed by Section 138 of Companies Act 2013 & SIA standards.", "Governed by Section 139 to 147 of Companies Act 2013 & SAs."),
                    ComparisonRow("Appointor & Reporting", "Appointed by Board of Directors; reports to Management / Audit Committee.", "Appointed by Shareholders at AGM; reports to SHAREHOLDERS of company."),
                    ComparisonRow("Mandatory Scope", "Mandatory for listed companies and unlisted public/private companies exceeding turnover/loan limits.", "MANDATORY for ALL companies incorporated under Companies Act 2013."),
                    ComparisonRow("Qualifications", "Can be CA, Cost Accountant, or other professional decided by Board; can be employee.", "MUST be a practicing Chartered Accountant / CA Firm; employee CANNOT be appointed."),
                    ComparisonRow("Primary Objective", "Reviews operational efficiency, internal control robustness, and policy compliance.", "Expresses an independent opinion on 'True and Fair View' of financial statements.")
                ),
                memoryHook = "Internal Audit = Management tool (Sec 138). Statutory Audit = Shareholder's Independent Opinion (Sec 139).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "7/7 Attempts",
                examinerTrapNote = "ICAI Trap: Statutory auditor MAY rely on internal auditor's work u/s SA 610, but retains SOLE responsibility for audit opinion."
            ),
            ConceptDifference(
                id = "diff_p5_sa705_opinions",
                paper = PaperCode.PAPER_5,
                title = "SA 705 Audit Opinions: Qualified vs Adverse vs Disclaimer of Opinion",
                conceptA = "Qualified Opinion (SA 705)",
                conceptB = "Adverse Opinion vs Disclaimer of Opinion (SA 705)",
                comparisonPoints = listOf(
                    ComparisonRow("Misstatement Impact", "Misstatements are MATERIAL BUT NOT PERVASIVE to financial statements.", "Adverse: Misstatements are BOTH MATERIAL AND PERVASIVE."),
                    ComparisonRow("Scope Limitation Impact", "Unable to obtain audit evidence; possible effects are Material BUT NOT Pervasive.", "Disclaimer: Unable to obtain evidence; possible effects are BOTH MATERIAL AND PERVASIVE."),
                    ComparisonRow("Wording Key Phrase", "Expresses 'Except for the effects of matter described... true and fair view'.", "Adverse: 'Do NOT give true & fair view'. Disclaimer: 'Do NOT express an opinion'."),
                    ComparisonRow("Basis Section", "'Basis for Qualified Opinion' paragraph placed immediately before Opinion paragraph.", "'Basis for Adverse Opinion' or 'Basis for Disclaimer of Opinion' paragraph mandatory."),
                    ComparisonRow("ICAI Exam Scenario", "Inventory overvalued by ₹ 10 Lakhs in a company having ₹ 50 Crores net profit.", "Financial statements prepared on Going Concern basis when company is in liquidation.")
                ),
                memoryHook = "Material NOT Pervasive = Qualified ('Except for'). Material AND Pervasive = Adverse (Misstated) or Disclaimer (No Evidence).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "7/7 Attempts Compulsory",
                examinerTrapNote = "ICAI Trap: If management imposes scope limitation resulting in pervasive inability, auditor MUST issue Disclaimer or Withdraw."
            ),
            ConceptDifference(
                id = "diff_p5_sa706_eom_om",
                paper = PaperCode.PAPER_5,
                title = "SA 706 Audit Report: Emphasis of Matter (EOM) vs Other Matter (OM) Paragraph",
                conceptA = "Emphasis of Matter (EOM) Paragraph",
                conceptB = "Other Matter (OM) Paragraph",
                comparisonPoints = listOf(
                    ComparisonRow("Content Source", "Refers to a matter APPROPRIATELY PRESENTED OR DISCLOSED in financial statements.", "Refers to a matter OTHER THAN those presented or disclosed in financial statements."),
                    ComparisonRow("Auditor's Purpose", "Draws users' attention to a fundamental matter fundamental to understanding financial statements.", "Draws users' attention to matters relevant to understanding the AUDIT, auditor responsibilities, or report."),
                    ComparisonRow("Modification Status", "EOM paragraph DOES NOT modify or qualify the auditor's opinion.", "OM paragraph DOES NOT modify or qualify the auditor's opinion."),
                    ComparisonRow("Placement in Report", "Placed immediately after Opinion paragraph under distinct heading 'Emphasis of Matter'.", "Placed in separate section titled 'Other Matter' after KAM section."),
                    ComparisonRow("ICAI Examples", "Major catastrophe uncertain litigation, early adoption of new AS.", "Prior period audited by another auditor, reporting on branch audit.")
                ),
                memoryHook = "EOM = Highlight key B/S Note disclosure. OM = Highlight Audit process / legal responsibility outside B/S Notes.",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "7/7 Attempts",
                examinerTrapNote = "ICAI Trap: EOM is NOT a substitute for expressing a modified opinion under SA 705 when misstatements exist!"
            ),
            ConceptDifference(
                id = "diff_p5_sa240_fraud_error",
                paper = PaperCode.PAPER_5,
                title = "SA 240 Risk Assessment: Fraud vs Error",
                conceptA = "Fraud (SA 240)",
                conceptB = "Error (Unintentional)",
                comparisonPoints = listOf(
                    ComparisonRow("Intentionality", "INTENTIONAL act involving deceit, trickery, or misrepresentation to obtain illegal advantage.", "UNINTENTIONAL mistake or oversight in financial statement preparation."),
                    ComparisonRow("Concealment", "Accompanied by active concealment, forgery, false documentation, or collusion.", "No concealment; easily detected through routine vouching and analytical procedures."),
                    ComparisonRow("Reporting u/s 143(12)", "Mandatory reporting to Central Govt in Form ADT-4 if fraud amount >= ₹ 1 Crore.", "No reporting required to Central Government u/s 143(12)."),
                    ComparisonRow("Categories", "Fraudulent Financial Reporting (e.g. fake sales) OR Misappropriation of Assets (theft).", "Mathematical error, wrong application of AS, or misinterpretation of facts."),
                    ComparisonRow("Responsibility", "Primary responsibility to prevent/detect lies with Management & Governance (TCWG).", "Management maintains internal controls to minimize clerical error risks.")
                ),
                memoryHook = "Fraud = Intentional deceit (Report ADT-4 if >= 1Cr). Error = Unintentional mistake.",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "7/7 Attempts",
                examinerTrapNote = "ICAI Trap: Fraud >= ₹ 1 Crore must be reported to Central Govt within 60 days of knowledge via Form ADT-4."
            ),
            ConceptDifference(
                id = "diff_p5_sa330_toc_substantive",
                paper = PaperCode.PAPER_5,
                title = "SA 330 Audit Procedures: Test of Controls (TOC) vs Substantive Procedures",
                conceptA = "Test of Controls (TOC)",
                conceptB = "Substantive Procedures",
                comparisonPoints = listOf(
                    ComparisonRow("Audit Objective", "Evaluates OPERATING EFFECTIVENESS of internal controls in preventing or detecting misstatements.", "Detects MATERIAL MISSTATEMENTS at assertion level in transactions, balances, and disclosures."),
                    ComparisonRow("Timing / Sequence", "Performed during interim audit to evaluate control environment.", "Performed during year-end financial statement audit."),
                    ComparisonRow("Components", "Inquiry, Observation, Inspection of approval logs, Re-performance of controls.", "Tests of Details (Vouching/Verification) AND Substantive Analytical Procedures."),
                    ComparisonRow("Dependence", "If TOC shows controls are effective, auditor REDUCES substantive testing extent.", "Mandatory for ALL material class of transactions regardless of control strength."),
                    ComparisonRow("ICAI Example", "Checking dual authorization signature on purchase invoices above ₹ 50,000.", "Verifying bank reconciliation statement and confirming balance with bank directly.")
                ),
                memoryHook = "TOC = Test if controls work. Substantive = Test if numbers on B/S are correct.",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "7/7 Attempts",
                examinerTrapNote = "ICAI Trap: Irrespective of assessed RMM, auditor MUST perform substantive procedures for material account balances."
            ),

            // =========================================================================
            // PAPER 6: Financial & Strategic Management (5 High-Yield Comparisons)
            // =========================================================================
            ConceptDifference(
                id = "diff_p6_dol_dfl",
                paper = PaperCode.PAPER_6,
                title = "FM Leverages: Degree of Operating Leverage (DOL) vs Degree of Financial Leverage (DFL)",
                conceptA = "Degree of Operating Leverage (DOL)",
                conceptB = "Degree of Financial Leverage (DFL)",
                comparisonPoints = listOf(
                    ComparisonRow("Risk Measured", "Measures Business Risk associated with fixed operating costs.", "Measures Financial Risk associated with fixed interest charges."),
                    ComparisonRow("Formula", "DOL = Contribution / EBIT (or % Change in EBIT / % Change in Sales).", "DFL = EBIT / EBT (or % Change in EPS / % Change in EBIT)."),
                    ComparisonRow("Cost Cause", "Arises due to presence of Fixed Operating Costs (e.g. Rent, Depreciation, Salaries).", "Arises due to presence of Fixed Financial Charges (e.g. Debenture Interest, Pref Dividend)."),
                    ComparisonRow("Break-Even Connection", "Directly linked to Operating Break-Even Point (Fixed Costs / Contribution Margin).", "Directly linked to Financial Break-Even Point (Interest + Pref Dividend / 1-t)."),
                    ComparisonRow("Combined Leverage (DCL)", "DOL measures Operating Risk alone.", "DFL measures Financial Risk; Combined DCL = DOL × DFL = Contribution / EBT.")
                ),
                memoryHook = "DOL = Fixed Operating Cost (Contribution/EBIT). DFL = Fixed Financial Interest (EBIT/EBT). DCL = Contribution/EBT.",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "12/12 Attempts",
                examinerTrapNote = "ICAI Trap: High DOL + High DFL creates an extremely volatile risky combination; ideal is Moderate DOL + Low DFL."
            ),
            ConceptDifference(
                id = "diff_p6_npv_irr",
                paper = PaperCode.PAPER_6,
                title = "FM Capital Budgeting: Net Present Value (NPV) vs Internal Rate of Return (IRR)",
                conceptA = "Net Present Value (NPV) Method",
                conceptB = "Internal Rate of Return (IRR) Method",
                comparisonPoints = listOf(
                    ComparisonRow("Measurement Unit", "Expressed in absolute MONETARY AMOUNT (Rupees profit added to shareholder wealth).", "Expressed as a PERCENTAGE yield discount rate."),
                    ComparisonRow("Discount Rate Used", "Uses pre-determined Cost of Capital / Hurdle Rate (WACC) as discount factor.", "Calculates the exact discount rate at which PV of Cash Inflows EQUALS Initial Outflow (NPV = 0)."),
                    ComparisonRow("Re-investment Rate Assumption", "Assumes intermediate cash inflows re-invested at Cost of Capital (Realistic).", "Assumes intermediate cash inflows re-invested at internal IRR rate (Unrealistic)."),
                    ComparisonRow("Mutually Exclusive Projects", "Superior and reliable; always select project with HIGHEST positive NPV.", "May give conflicting ranking due to scale or cash flow timing differences."),
                    ComparisonRow("Acceptance Criterion", "Accept if NPV > 0.", "Accept if IRR > Cost of Capital (K).")
                ),
                memoryHook = "NPV = Absolute Rupees added (Re-invests at WACC). IRR = % Rate where NPV = 0 (NPV is king in conflict!).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "12/12 Attempts",
                examinerTrapNote = "ICAI Trap: In case of ranking conflict between NPV and IRR for mutually exclusive projects, ALWAYS follow NPV recommendation!"
            ),
            ConceptDifference(
                id = "diff_p6_ni_noi_capital_structure",
                paper = PaperCode.PAPER_6,
                title = "FM Capital Structure: Net Income (NI) vs Net Operating Income (NOI) Approach",
                conceptA = "Net Income (NI) Approach (Durand)",
                conceptB = "Net Operating Income (NOI) Approach (Durand)",
                comparisonPoints = listOf(
                    ComparisonRow("Capital Structure Relevance", "RELEVANT theory; debt ratio change DIRECTLY affects overall cost of capital (WACC) and firm value.", "IRRELEVANT theory; leverage change DOES NOT affect overall cost of capital (WACC) or firm value."),
                    ComparisonRow("Overall Cost of Capital (Ko)", "Ko DECREASES as debt proportion increases because Kd is cheaper than Ke.", "Ko remains CONSTANT at all leverage levels regardless of debt-equity ratio."),
                    ComparisonRow("Cost of Equity (Ke)", "Ke remains CONSTANT as debt increases (assumes equity risk perception unchanged).", "Ke INCREASES linearly with debt leverage due to rising financial risk to shareholders."),
                    ComparisonRow("Optimal Capital Structure", "Optimal capital structure exists at 100% MAXIMUM DEBT financing.", "No optimal capital structure; every capital structure is equally good."),
                    ComparisonRow("Market Value of Firm (V)", "Value of firm V = EBIT / Ko increases with debt.", "Value of firm V = EBIT / Ko remains constant.")
                ),
                memoryHook = "NI = Debt matters (Higher debt = Lower WACC). NOI = Debt irrelevant (WACC stays constant as Ke rises).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "6/6 Attempts",
                examinerTrapNote = "ICAI Trap: MM Hypothesis WITHOUT taxes mirrors NOI Approach (WACC constant), but MM WITH taxes supports NI Approach."
            ),
            ConceptDifference(
                id = "diff_p6_sm_red_blue_ocean",
                paper = PaperCode.PAPER_6,
                title = "SM Strategy Types: Red Ocean Strategy vs Blue Ocean Strategy",
                conceptA = "Red Ocean Strategy",
                conceptB = "Blue Ocean Strategy",
                comparisonPoints = listOf(
                    ComparisonRow("Market Space", "Compete in existing market space with established boundary rules.", "Create uncontested new market space where competition does not exist."),
                    ComparisonRow("Competition Focus", "Focus on beating existing competitors through price wars or incremental features.", "Make competition irrelevant by creating a new category."),
                    ComparisonRow("Demand Creation", "Exploit existing market demand among existing customer segments.", "Create and capture new demand among non-customers."),
                    ComparisonRow("Value-Cost Trade-off", "Choose between Differentiation OR Low Cost (Value-Cost trade-off).", "Pursue BOTH Differentiation AND Low Cost simultaneously (Value Innovation)."),
                    ComparisonRow("ICAI Exam Example", "Traditional smartphone war (Samsung vs Xiaomi in budget segment).", "Apple iPhone launch in 2007 or Cirque du Soleil reinventing circus entertainment.")
                ),
                memoryHook = "Red Ocean = Bloody crowded market (Price wars). Blue Ocean = Uncontested clear waters (Value Innovation).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "12/12 Attempts",
                examinerTrapNote = "ICAI Trap: Value Innovation in Blue Ocean Strategy simultaneously drives down costs while elevating buyer value."
            ),
            ConceptDifference(
                id = "diff_p6_sm_porter_cost_differentiation",
                paper = PaperCode.PAPER_6,
                title = "SM Porter's Generic Strategies: Cost Leadership vs Differentiation Strategy",
                conceptA = "Cost Leadership Strategy (Porter)",
                conceptB = "Differentiation Strategy (Porter)",
                comparisonPoints = listOf(
                    ComparisonRow("Strategic Objective", "Become the lowest-cost producer in the industry across broad customer market.", "Provide unique, premium product features valued by customers willing to pay higher prices."),
                    ComparisonRow("Competitive Advantage Basis", "Economies of scale, lean overheads, cheap raw material access, standard design.", "Brand reputation, superior technology, exceptional service, custom design."),
                    ComparisonRow("Price & Margin Focus", "Sells at industry average prices for higher margins OR undercuts price to gain volume.", "Charges PREMIUM PRICE to achieve higher profit margins per unit."),
                    ComparisonRow("Vulnerability", "Technological breakthroughs by rivals or inflation eroding low-cost advantage.", "Imitation by competitors or customer price sensitivity shifting demand to cheap substitutes."),
                    ComparisonRow("ICAI Exam Example", "Reliance Jio entering telecom market with lowest tariff packages.", "Apple iPhones or Rolex watches enjoying brand loyalty and premium pricing.")
                ),
                memoryHook = "Cost Leadership = Lowest cost producer (Volume profit). Differentiation = Unique premium product (Premium price margin).",
                priorityTier = PriorityTier.TIER_1,
                pastExamFrequency = "12/12 Attempts",
                examinerTrapNote = "ICAI Trap: Being 'Stuck in the Middle' occurs when a firm fails to achieve EITHER Cost Leadership OR Differentiation!"
            )
        )
    }

    fun getDifferencesForPaper(paper: PaperCode): List<ConceptDifference> {
        return differences.filter { it.paper == paper }
    }

    // =========================================================================
    // STATUTORY CHEAT MATRICES (1 Per Paper Across All 6 Papers)
    // =========================================================================
    val statutoryMatrices: List<StatutoryCheatMatrix> by lazy {
        listOf(
            // Paper 1: AS Applicability & Valuation Basis Matrix
            StatutoryCheatMatrix(
                id = "matrix_p1_as",
                paper = PaperCode.PAPER_1,
                title = "Paper 1: Accounting Standards (AS) Applicability & Valuation Matrix",
                columnHeaders = listOf("AS Standard", "Mandatory Scope & Trigger", "Measurement / Valuation Basis", "Required P&L/BS Treatment", "Key Penalty / Exception"),
                rows = listOf(
                    StatutoryMatrixRow("AS 2", "Valuation of Inventories", "Lower of Cost OR Net Realizable Value (NRV)", "FIFO or Weighted Average; write-down credited to P&L", "Excludes WIP of service providers & construction contracts"),
                    StatutoryMatrixRow("AS 10", "Property, Plant & Equipment", "Historical Cost less Accumulated Depreciation & Impairment", "Revaluation gain to Revaluation Reserve; loss to P&L", "Component accounting mandatory for significant parts"),
                    StatutoryMatrixRow("AS 13", "Accounting for Investments", "Current: Lower of Cost/Fair Value; Long-Term: At Cost", "Long-term decline provisioned ONLY IF permanent", "Reclassification between Current & Long-term at lower of Cost/FV"),
                    StatutoryMatrixRow("AS 16", "Borrowing Costs", "Capitalize interest directly attributable to Qualifying Asset", "Capitalization ceases when asset is substantially complete", "Suspended during extended periods of active development interruption"),
                    StatutoryMatrixRow("AS 28", "Impairment of Assets", "Assess if Carrying Amount > Recoverable Amount (higher of Value in Use/Net Selling Price)", "Impairment loss debited to P&L (or Revaluation Reserve)", "Reversal allowed except for Goodwill impairment")
                )
            ),

            // Paper 2: Companies Act Resolutions & Time Limits Matrix
            StatutoryCheatMatrix(
                id = "matrix_p2_law",
                paper = PaperCode.PAPER_2,
                title = "Paper 2: Companies Act 2013 Key Limits, Resolutions & Time Lines Matrix",
                columnHeaders = listOf("Section / Rule", "Subject Matter / Provision", "Statutory Ceiling / Threshold Limit", "Required Approval Level", "Filing & Penalty Limit"),
                rows = listOf(
                    StatutoryMatrixRow("Sec 96", "Annual General Meeting (AGM)", "Within 6 months from FY end (15 months max gap)", "Ordinary Business / Ordinary Resolution", "MGT-15 report filed within 30 days of AGM"),
                    StatutoryMatrixRow("Sec 180", "Borrowing Powers of Board", "Aggregate borrowings > Paid up capital + Free Reserves + SPR", "Special Resolution at General Meeting", "Ultra vires borrowing void unless ratified"),
                    StatutoryMatrixRow("Sec 185", "Loans to Directors", "Prohibited unless WOS or principal business financing", "Special Resolution + Explanatory Statement", "Fine ₹ 5 L to ₹ 25 L on company & officer"),
                    StatutoryMatrixRow("Sec 186", "Loans & Investments by Company", "60% of Paid-up Capital+FR+SPR OR 100% FR+SPR", "Unanimous Board Approval (+ SR if limit exceeded)", "PFI approval mandatory if any default subsisting"),
                    StatutoryMatrixRow("Sec 77", "Registration of Charges", "Registration within 30 days of creation", "Form CHG-1 or CHG-9 to ROC", "ROC extension allowed up to 60 days on extra fee")
                )
            ),

            // Paper 3: TDS Rates & Threshold Limits Matrix
            StatutoryCheatMatrix(
                id = "matrix_p3_tax",
                paper = PaperCode.PAPER_3,
                title = "Paper 3: Income Tax TDS/TCS & GST Threshold Limits Matrix",
                columnHeaders = listOf("Section / Rule", "Nature of Payment / Supply", "Threshold Exemption Limit", "Applicable TDS / GST Rate", "Mandatory Conditions / Notes"),
                rows = listOf(
                    StatutoryMatrixRow("Sec 194C", "Payment to Contractors", "Single contract <= ₹ 30,000 / Aggregate FY <= ₹ 100,000", "1% (Individual/HUF), 2% (Others)", "No TDS for transport operator owning <= 10 trucks with PAN"),
                    StatutoryMatrixRow("Sec 194J", "Professional & Technical Fees", "₹ 30,000 per annum for each category", "10% (Professional/Royalty), 2% (Technical/Call center)", "Director fees taxed at 10% u/s 194J(1)(ba) with ZERO threshold"),
                    StatutoryMatrixRow("Sec 194I", "Rent Payment (Land/Building/Plant)", "₹ 240,000 per annum per lessor", "10% (Land/Building/Furniture), 2% (Plant & Machinery)", "Form 26QC for individuals paying house rent > ₹ 50,000/month u/s 194IB"),
                    StatutoryMatrixRow("Sec 43B(h)", "Payments to MSME Suppliers", "Within 15 days (or max 45 days written contract)", "Deduction allowed ONLY in year of actual payment", "Proviso for payment up to ITR due date DOES NOT APPLY"),
                    StatutoryMatrixRow("GST RCM 9(3)", "Inward Reverse Charge Supplies", "Mandatory registration for recipient (Zero threshold)", "100% tax deposited by Recipient in CASH ONLY", "Legal fees, Director fees, GTA, Renting motor vehicle")
                )
            ),

            // Paper 4: All Costing Formulas Matrix
            StatutoryCheatMatrix(
                id = "matrix_p4_costing",
                paper = PaperCode.PAPER_4,
                title = "Paper 4: Cost & Management Accounting Formula & Variance Matrix",
                columnHeaders = listOf("Costing Topic", "Key Formula / Concept", "Standard Input Variables", "Target Output Unit", "Exam Application Note"),
                rows = listOf(
                    StatutoryMatrixRow("Material Variance", "MCV = (SQ × SR) - (AQ × AR)", "SQ = Std Qty for Actual Output, SR = Std Rate", "Rupees (Favourable or Adverse)", "MCV = MPV + MUV; always calculate SQ for actual output!"),
                    StatutoryMatrixRow("Labour Variance", "LCV = (SH × SR) - (AH × AR)", "SH = Std Hours for Actual Output, SR = Std Rate", "Rupees (Favourable or Adverse)", "Idle Time Variance = Idle Hours × SR (Always Adverse!)"),
                    StatutoryMatrixRow("EOQ Analysis", "EOQ = √(2 × A × O / C)", "A = Annual Demand, O = Order Cost, C = Carrying Cost/unit", "Physical Units per order", "Total Inventory Cost at EOQ = Ordering Cost + Carrying Cost"),
                    StatutoryMatrixRow("Marginal CVP", "P/V Ratio = (Contribution / Sales) × 100", "Contribution = Sales - Variable Cost", "Percentage (%)", "BEP (Units) = Fixed Cost / Contribution per unit"),
                    StatutoryMatrixRow("Margin of Safety", "MoS = Actual Sales - BEP Sales", "Profit = MoS × P/V Ratio", "Rupees or Percentage (%)", "MoS % = Profit / Contribution = MoS Sales / Total Sales")
                )
            ),

            // Paper 5: CARO 2020 Clauses & Reporting Matrix
            StatutoryCheatMatrix(
                id = "matrix_p5_audit",
                paper = PaperCode.PAPER_5,
                title = "Paper 5: CARO 2020 Key Reporting Clauses & Limits Matrix",
                columnHeaders = listOf("CARO Clause", "Reporting Subject", "Trigger Limit / Threshold", "Specific Audit Requirement", "Key Misstatement Danger"),
                rows = listOf(
                    StatutoryMatrixRow("Clause 3(i)", "PPE & Intangible Assets", "Property records, physical verification every 3 yrs", "Report if title deeds of immovable property NOT in company name", "Lessee lease agreements executed excluded"),
                    StatutoryMatrixRow("Clause 3(ii)", "Inventory & Working Capital Limits", "Sanctioned working capital limits > ₹ 5 Crores from banks", "Quarterly statements filed with banks agree with books", "Discrepancy > 10% in physical inventory verification"),
                    StatutoryMatrixRow("Clause 3(iii)", "Loans & Investments u/s 189", "Loans/advances granted to related parties", "State if terms non-prejudicial & overdue > 90 days", "Total amount overdue and steps taken for recovery"),
                    StatutoryMatrixRow("Clause 3(vii)", "Statutory Dues Payment", "Undisputed statutory dues outstanding > 6 months", "Report exact amount outstanding at balance sheet date", "PF, ESI, Income Tax, GST, Customs Duty"),
                    StatutoryMatrixRow("Clause 3(xi)", "Fraud Reporting", "Any fraud by or on company noticed/reported", "State if ADT-4 filed with Central Govt u/s 143(12)", "Whistle-blower complaints considered by auditor")
                )
            ),

            // Paper 6: FM Formulas & SM Cheat Sheet Matrix
            StatutoryCheatMatrix(
                id = "matrix_p6_fm_sm",
                paper = PaperCode.PAPER_6,
                title = "Paper 6: FM Formulas & SM Strategic Matrices Cheat Sheet",
                columnHeaders = listOf("Domain / Topic", "Model / Formula", "Key Component Variables", "Strategic / Financial Decision", "Exam Memory Hook"),
                rows = listOf(
                    StatutoryMatrixRow("FM Leverages", "DOL = Contrib / EBIT; DFL = EBIT / EBT", "Contrib = Sales - VC; EBT = EBIT - Interest", "DCL = DOL × DFL = Contrib / EBT", "Combined Leverage measures total risk"),
                    StatutoryMatrixRow("FM Cost of Capital", "WACC = (Ke × We) + (Kd × Wd) + (Kp × Wp)", "Kd = I(1-t)/NP; Ke = (D1/P0) + g", "Minimum required rate of return for project", "Weights can be Book Value or Market Value"),
                    StatutoryMatrixRow("SM BCG Matrix", "Market Growth Rate vs Relative Market Share", "Stars (High/High), Cash Cows (Low/High)", "Question Marks (High/Low), Dogs (Low/Low)", "Cash Cows fund Question Marks into Stars"),
                    StatutoryMatrixRow("SM Ansoff Matrix", "Products (Existing/New) vs Markets (Existing/New)", "Penetration (Ex/Ex), Product Dev (New/Ex)", "Market Dev (Ex/New), Diversification (New/New)", "Diversification carries highest strategic risk"),
                    StatutoryMatrixRow("SM Change Mgmt", "Kurt Lewin 3-Stage Change Model", "1. Unfreezing -> 2. Changing -> 3. Refreezing", "Overcoming employee resistance to change", "Refreezing locks in new corporate culture")
                )
            )
        )
    }

    fun getMatrixForPaper(paper: PaperCode): StatutoryCheatMatrix? {
        return statutoryMatrices.firstOrNull { it.paper == paper }
    }
}
