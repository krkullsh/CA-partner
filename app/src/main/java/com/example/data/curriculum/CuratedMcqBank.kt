package com.example.data.curriculum

import com.example.data.model.McqQuestion
import com.example.data.model.PaperCode
import com.example.data.model.PriorityTier

object CuratedMcqBank {

    val mcqs: List<McqQuestion> = listOf(
        // =========================================================================
        // PAPER 1: Advanced Accounting (4 MCQs)
        // =========================================================================
        McqQuestion(
            id = "mcq_p1_01",
            paper = PaperCode.PAPER_1,
            topicId = "p1_sch3_fin_stmt",
            topicName = "Preparation of Financial Statements of Companies (Schedule III Division I)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = """
                Apex Ltd. is preparing its financial statements for the financial year ended March 31, 2026 as per Division I of Schedule III to the Companies Act, 2013. The company has total trade payables of ₹ 45 Lakhs, out of which ₹ 15 Lakhs are due to Micro and Small Enterprises (MSMEs). An amount of ₹ 3 Lakhs owed to an MSME vendor has been outstanding for 60 days, whereas the agreed credit period as per the contract is 45 days. The company's normal operating cycle is 10 months.
            """.trimIndent(),
            questionStem = "As per Schedule III Division I disclosures and Section 16 of the MSMED Act 2006, how should interest on delayed payments to MSME vendors be treated in Apex Ltd.'s financial statements?",
            options = listOf(
                "A. Interest must be accrued and debited to Statement of Profit and Loss, and is deductible under Section 37 of Income Tax Act",
                "B. Interest must be accrued and debited to Statement of Profit and Loss, but is expressly dis-allowed as a deduction under Section 23 of MSMED Act",
                "C. Interest is disclosed only as a contingent liability in the Notes to Accounts without P&L accrual",
                "D. Interest is ignored if the payment is delayed by less than 90 days"
            ),
            correctOptionIndex = 1,
            explanation = "Under Section 16 of MSMED Act 2006, compound interest at 3 times bank rate is payable on delayed payments exceeding 45 days. Schedule III mandates accrual in P&L, and Section 23 of MSMED Act explicitly disallows this interest as a tax deductible expense.",
            icaiProvisionRef = "Schedule III Division I & Section 23 of MSMED Act 2006"
        ),
        McqQuestion(
            id = "mcq_p1_02",
            paper = PaperCode.PAPER_1,
            topicId = "p1_amalgamation_companies",
            topicName = "Amalgamation of Companies (AS 14 & Corporate Schemes)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "In an amalgamation in the nature of purchase under AS 14, Zenith Ltd. acquires Horizon Ltd. The agreed values of assets and liabilities taken over are ₹ 80 Lakhs and ₹ 30 Lakhs respectively. The agreed Purchase Consideration is ₹ 58 Lakhs paid to shareholders of Horizon Ltd. Additionally, Zenith Ltd. pays ₹ 2 Lakhs directly to debenture holders of Horizon Ltd. What is the amount of Goodwill or Capital Reserve arising on amalgamation?",
            options = listOf(
                "A. Capital Reserve of ₹ 2 Lakhs",
                "B. Goodwill of ₹ 8 Lakhs",
                "C. Goodwill of ₹ 10 Lakhs",
                "D. Capital Reserve of ₹ 10 Lakhs"
            ),
            correctOptionIndex = 1,
            explanation = "Net Assets taken over = ₹ 80 L - ₹ 30 L = ₹ 50 Lakhs. Purchase Consideration paid to shareholders = ₹ 58 Lakhs. (Note: Payments to debenture holders are part of liability discharge, NOT Purchase Consideration). Goodwill = Purchase Consideration (₹ 58 L) - Net Assets (₹ 50 L) = ₹ 8 Lakhs.",
            icaiProvisionRef = "AS 14 Paragraph 13 & 36"
        ),
        McqQuestion(
            id = "mcq_p1_03",
            paper = PaperCode.PAPER_1,
            topicId = "p1_consolidation_cfs",
            topicName = "Consolidated Financial Statements of Group Entities (AS 21, AS 23, AS 27)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = """
                Holding Co. holds 75% equity in Subsidiary Co. acquired on April 1, 2025. On September 30, 2025, Subsidiary Co. sold goods costing ₹ 12 Lakhs to Holding Co. for ₹ 16 Lakhs (at 25% profit margin on selling price). As on March 31, 2026, 50% of these goods remain unsold in Holding Co.'s inventory.
            """.trimIndent(),
            questionStem = "What is the amount of unrealized profit to be eliminated in consolidation and how is it apportioned between Consolidated Profit & Loss and Minority Interest?",
            options = listOf(
                "A. ₹ 4 Lakhs total eliminated; ₹ 3 Lakhs from Consolidated P&L and ₹ 1 Lakh from Minority Interest",
                "B. ₹ 2 Lakhs total eliminated; ₹ 1.5 Lakhs deducted from Consolidated P&L and ₹ 0.5 Lakh from Minority Interest",
                "C. ₹ 2 Lakhs total eliminated; 100% deducted from Consolidated P&L only",
                "D. ₹ 1.5 Lakhs total eliminated; 100% deducted from Minority Interest"
            ),
            correctOptionIndex = 1,
            explanation = "Unsold goods = 50% of ₹ 16 L = ₹ 8 Lakhs. Profit element = 25% on selling price = ₹ 2 Lakhs total unrealized profit. Since this is an UPSTREAM sale (Subsidiary to Holding), the ₹ 2 Lakh unrealized profit is eliminated pro-rata: 75% (₹ 1.5 L) from Holding P&L and 25% (₹ 0.5 L) from Minority Interest.",
            icaiProvisionRef = "AS 21 Paragraph 17 & Guidance Note on CFS"
        ),
        McqQuestion(
            id = "mcq_p1_04",
            paper = PaperCode.PAPER_1,
            topicId = "p1_internal_reconstruction",
            topicName = "Internal Reconstruction of Companies",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "Under a scheme of Internal Reconstruction sanctioned by NCLT, equity shares of ₹ 100 each are reduced to ₹ 20 each fully paid up. The company has 1,00,000 equity shares. The total sacrifice made by equity shareholders credited to Capital Reduction Account is:",
            options = listOf(
                "A. ₹ 20,000,000",
                "B. ₹ 80,000,000",
                "C. ₹ 100,000,000",
                "D. ₹ 60,000,000"
            ),
            correctOptionIndex = 1,
            explanation = "Sacrifice per share = ₹ 100 - ₹ 20 = ₹ 80 per share. Total sacrifice credited to Capital Reduction Account = 1,00,000 shares × ₹ 80 = ₹ 80,000,000 (₹ 80 Lakhs).",
            icaiProvisionRef = "Companies Act Section 66 & Accountancy Practice"
        ),

        // =========================================================================
        // PAPER 2: Corporate and Other Laws (4 MCQs)
        // =========================================================================
        McqQuestion(
            id = "mcq_p2_01",
            paper = PaperCode.PAPER_2,
            topicId = "p2_fema",
            topicName = "Foreign Exchange Management Act, 1999 (FEMA)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = """
                Mr. Raman, an Indian resident individual, intends to remit funds abroad during Financial Year 2025-26 under the Liberalised Remittance Scheme (LRS). He proposes:
                1. USD 120,000 for his daughter's university tuition fees in USA.
                2. USD 90,000 for medical treatment of his spouse in UK.
                3. USD 60,000 for purchasing equity shares of an unlisted tech startup in Singapore.
            """.trimIndent(),
            questionStem = "Does Mr. Raman require prior RBI approval for these transactions under LRS during FY 2025-26?",
            options = listOf(
                "A. No, because education and medical expenses are exempt from LRS limits",
                "B. Yes, because the total remittance of USD 270,000 exceeds the LRS limit of USD 250,000 per financial year",
                "C. No, provided he gets approval from Authorized Dealer Bank for USD 20,000 excess",
                "D. Yes, purchasing unlisted foreign shares is prohibited under current account transactions"
            ),
            correctOptionIndex = 1,
            explanation = "Under FEMA LRS, resident individuals can remit up to USD 250,000 in total per Financial Year for permissible current and capital account transactions combined. Since total proposed remittance is USD 270,000 (120k + 90k + 60k), the excess USD 20,000 requires prior RBI approval.",
            icaiProvisionRef = "FEMA 1999 LRS Rules & RBI Master Direction"
        ),
        McqQuestion(
            id = "mcq_p2_02",
            paper = PaperCode.PAPER_2,
            topicId = "p2_llp_act",
            topicName = "Limited Liability Partnership Act, 2008",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "Vanguard LLP has 5 partners. Due to a partner's retirement, the number of partners drops to 1 on August 1, 2025. The sole partner continues carrying on business for 8 months until March 31, 2026 without admitting a second partner. What is the personal liability status of the sole partner for obligations incurred during this period?",
            options = listOf(
                "A. The partner remains protected by limited liability for the entire 8 months",
                "B. The partner becomes personally liable for all obligations incurred after the first 6 months (i.e. from Feb 1, 2026 onwards)",
                "C. The LLP is automatically dissolved on August 1, 2025",
                "D. The partner becomes personally liable for all obligations incurred from August 1, 2025"
            ),
            correctOptionIndex = 1,
            explanation = "Under Section 6 of LLP Act 2008, if the number of partners drops below 2 and the business is carried on for more than 6 months, the remaining partner who knows this fact becomes personally liable for obligations incurred AFTER the expiry of 6 months.",
            icaiProvisionRef = "LLP Act 2008 Section 6"
        ),
        McqQuestion(
            id = "mcq_p2_03",
            paper = PaperCode.PAPER_2,
            topicId = "p2_ch7_mgmt_admin",
            topicName = "Management & Administration (Sec 88 - 122)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "A listed company holds its Annual General Meeting (AGM) on September 25, 2025. Under Section 121 of Companies Act 2013, by what date must the company file the Report on AGM in Form MGT-15 with the Registrar of Companies (ROC)?",
            options = listOf(
                "A. Within 30 days of AGM (by October 25, 2025)",
                "B. Within 60 days of AGM (by November 24, 2025)",
                "C. Within 15 days of AGM (by October 10, 2025)",
                "D. Within 90 days from closure of financial year"
            ),
            correctOptionIndex = 0,
            explanation = "Section 121(1) requires every listed public company to prepare a report on AGM in Form MGT-15 and file it with the ROC within 30 days of the conclusion of the AGM.",
            icaiProvisionRef = "Companies Act 2013 Section 121 & Rule 25"
        ),
        McqQuestion(
            id = "mcq_p2_04",
            paper = PaperCode.PAPER_2,
            topicId = "p2_general_clauses",
            topicName = "The General Clauses Act, 1897",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "Where an Act of Parliament does not explicitly specify the date on which it comes into operation, when does it come into force as per Section 5 of the General Clauses Act, 1897?",
            options = listOf(
                "A. On the 1st day of April following the enactment",
                "B. On the date it receives the assent of the President of India",
                "C. 30 days after publication in the Official Gazette",
                "D. On the date it is passed by both Houses of Parliament"
            ),
            correctOptionIndex = 1,
            explanation = "Under Section 5(1) of General Clauses Act 1897, where a Central Act does not specify the date of commencement, it comes into operation on the day on which it receives the assent of the President.",
            icaiProvisionRef = "General Clauses Act 1897 Section 5"
        ),

        // =========================================================================
        // PAPER 3: Taxation (4 MCQs)
        // =========================================================================
        McqQuestion(
            id = "mcq_p3_01",
            paper = PaperCode.PAPER_3,
            topicId = "p3_it_pgbp",
            topicName = "Profits and Gains of Business or Profession (PGBP)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = """
                M/s Delta Traders, a registered partnership firm, incurred an expenditure of ₹ 450,000 towards purchase of raw materials from an MSME micro-enterprise on February 10, 2026. The agreed credit period as per written contract is 30 days (due March 12, 2026). The firm actually paid the MSME supplier on May 15, 2026 (prior to due date of filing ITR u/s 139(1)).
            """.trimIndent(),
            questionStem = "In which Assessment Year is the deduction of ₹ 450,000 allowable to M/s Delta Traders under Section 43B(h) of the Income Tax Act?",
            options = listOf(
                "A. AY 2026-27 (FY 2025-26), because payment was made before the ITR filing due date u/s 139(1)",
                "B. AY 2027-28 (FY 2026-27), because actual payment was made after March 31, 2026 and beyond the written credit period",
                "C. AY 2026-27 for 50% and AY 2027-28 for 50%",
                "D. Disallowed permanently under Section 40(a)(ia)"
            ),
            correctOptionIndex = 1,
            explanation = "Section 43B(h) mandates that payments to Micro & Small Enterprises beyond the time limit allowed under Section 15 of MSMED Act (max 45 days / agreed limit) are allowable ONLY in the year of actual payment. Proviso to Sec 43B allowing payment up to ITR due date DOES NOT APPLY to clause (h). Hence deductible in FY 2026-27 (AY 2027-28).",
            icaiProvisionRef = "Income Tax Act u/s 43B(h) & Section 15 MSMED Act"
        ),
        McqQuestion(
            id = "mcq_p3_02",
            paper = PaperCode.PAPER_3,
            topicId = "p3_gst_itc_17_5",
            topicName = "Input Tax Credit (ITC) & Blocked Credits u/s 17(5)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "ABC Logistics Pvt. Ltd., a taxable freight supplier, purchased a motor vehicle with seating capacity of 8 persons for ₹ 15 Lakhs (GST paid ₹ 2.7 Lakhs) for transporting its executive managers. It also purchased a truck with seating capacity of 3 persons for goods transport (GST paid ₹ 1.8 Lakhs). What is the total eligible Input Tax Credit?",
            options = listOf(
                "A. ₹ 4.5 Lakhs (both vehicles eligible)",
                "B. ₹ 1.8 Lakhs (only truck eligible; passenger motor vehicle <= 13 seats blocked u/s 17(5)(a))",
                "C. ₹ 2.7 Lakhs (only manager vehicle eligible)",
                "D. Nil (all motor vehicles blocked)"
            ),
            correctOptionIndex = 1,
            explanation = "Under Section 17(5)(a) of CGST Act, ITC on motor vehicles for transportation of persons having approved seating capacity <= 13 persons is blocked (unless used for specified exceptions like further supply, passenger transport, or driving school). Goods vehicles (truck) are NOT blocked under 17(5)(a). Eligible ITC = ₹ 1.8 Lakhs.",
            icaiProvisionRef = "CGST Act 2017 Section 17(5)(a)"
        ),
        McqQuestion(
            id = "mcq_p3_03",
            paper = PaperCode.PAPER_3,
            topicId = "p3_it_capital_gains",
            topicName = "Capital Gains (Sec 45 to 55A)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "Mr. Suresh sold a residential house on June 1, 2025 for ₹ 90 Lakhs (Stamp Duty Value ₹ 110 Lakhs). The assessable Stamp Duty Value was not questioned before any authority. For computing Long-Term Capital Gains under Section 50C, the full value of consideration shall be taken as:",
            options = listOf(
                "A. ₹ 90 Lakhs, because variance is within 110% safe harbour",
                "B. ₹ 110 Lakhs, because SDV (₹ 110 L) exceeds 110% of actual consideration (110% of ₹ 90 L = ₹ 99 L)",
                "C. ₹ 99 Lakhs",
                "D. Average of ₹ 90 L and ₹ 110 L = ₹ 100 Lakhs"
            ),
            correctOptionIndex = 1,
            explanation = "Under Section 50C, if Stamp Duty Value exceeds 110% of the actual agreement consideration, the SDV shall be adopted as full value of consideration. Here 110% of ₹ 90 L = ₹ 99 L. Since SDV is ₹ 110 L (> ₹ 99 L), full value of consideration = ₹ 110 Lakhs.",
            icaiProvisionRef = "Income Tax Act Section 50C"
        ),
        McqQuestion(
            id = "mcq_p3_04",
            paper = PaperCode.PAPER_3,
            topicId = "p3_gst_time_of_supply",
            topicName = "Time of Supply under CGST Act",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "X Ltd. supplies goods to Y Ltd. Invoice issued on Oct 10, 2025. Goods dispatched on Oct 12, 2025. Payment received in bank on Nov 5, 2025. Under Notification No. 66/2017-CT, what is the Time of Supply for payment of GST on supply of goods?",
            options = listOf(
                "A. Nov 5, 2025 (date of payment)",
                "B. Oct 10, 2025 (date of issue of invoice / last date required u/s 31)",
                "C. Oct 12, 2025 (date of dispatch)",
                "D. Oct 31, 2025"
            ),
            correctOptionIndex = 1,
            explanation = "As per Notification No. 66/2017-Central Tax, all registered suppliers of goods (other than composition scheme) are exempt from paying GST on advance payment. The time of supply for goods is the date of issue of invoice or last date on which invoice is required to be issued u/s 31 (i.e. Oct 10, 2025).",
            icaiProvisionRef = "CGST Act Section 12(2) & Notification 66/2017-CT"
        ),

        // =========================================================================
        // PAPER 4: Cost and Management Accounting (4 MCQs)
        // =========================================================================
        McqQuestion(
            id = "mcq_p4_01",
            paper = PaperCode.PAPER_4,
            topicId = "p4_marginal_costing",
            topicName = "Marginal Costing & CVP Analysis",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = """
                Precision Tools Ltd. manufactures a single product selling at ₹ 100 per unit. Variable cost is ₹ 60 per unit. Total fixed costs are ₹ 800,000 per annum. The company currently sells 25,000 units per year.
            """.trimIndent(),
            questionStem = "Calculate the P/V Ratio, Break-Even Point in units, and Margin of Safety percentage.",
            options = listOf(
                "A. P/V Ratio = 40%, BEP = 20,000 units, Margin of Safety = 20%",
                "B. P/V Ratio = 60%, BEP = 13,333 units, Margin of Safety = 46.7%",
                "C. P/V Ratio = 40%, BEP = 20,000 units, Margin of Safety = 25%",
                "D. P/V Ratio = 50%, BEP = 16,000 units, Margin of Safety = 36%"
            ),
            correctOptionIndex = 0,
            explanation = "Contribution per unit = ₹ 100 - ₹ 60 = ₹ 40. P/V Ratio = 40/100 = 40%. Break-Even Units = Fixed Cost / Contribution = ₹ 800,000 / ₹ 40 = 20,000 units. Margin of Safety Units = Actual Sales - BEP = 25,000 - 20,000 = 5,000 units. MoS % = 5,000 / 25,000 = 20%.",
            icaiProvisionRef = "CMA Marginal Costing Formulas"
        ),
        McqQuestion(
            id = "mcq_p4_02",
            paper = PaperCode.PAPER_4,
            topicId = "p4_standard_costing",
            topicName = "Standard Costing & Variance Analysis",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "Standard Material required for producing 1 unit of finished product is 5 kg at ₹ 10/kg. Actual production was 1,000 units requiring 5,200 kg of material purchased and consumed at ₹ 11/kg. The Material Usage Variance is:",
            options = listOf(
                "A. ₹ 2,000 Adverse",
                "B. ₹ 5,200 Adverse",
                "C. ₹ 7,200 Adverse",
                "D. ₹ 2,200 Adverse"
            ),
            correctOptionIndex = 0,
            explanation = "Standard Quantity for Actual Production (SQ) = 1,000 units × 5 kg = 5,000 kg. Actual Quantity (AQ) = 5,200 kg. Standard Rate (SR) = ₹ 10/kg. Material Usage Variance = (SQ - AQ) × SR = (5,000 - 5,200) × ₹ 10 = -₹ 2,000 (₹ 2,000 Adverse).",
            icaiProvisionRef = "CMA Standard Costing Variance Analysis"
        ),
        McqQuestion(
            id = "mcq_p4_03",
            paper = PaperCode.PAPER_4,
            topicId = "p4_material_eoq",
            topicName = "Material Cost & EOQ Analysis",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "Annual demand for Raw Material X is 10,000 units. Ordering cost per order is ₹ 200. Carrying cost per unit per annum is ₹ 4. Calculate the Economic Order Quantity (EOQ) and total annual inventory management cost at EOQ.",
            options = listOf(
                "A. EOQ = 1,000 units; Total Cost = ₹ 4,000",
                "B. EOQ = 1,000 units; Total Cost = ₹ 2,000",
                "C. EOQ = 500 units; Total Cost = ₹ 8,000",
                "D. EOQ = 2,000 units; Total Cost = ₹ 2,000"
            ),
            correctOptionIndex = 0,
            explanation = "EOQ = √(2 × A × O / C) = √(2 × 10,000 × 200 / 4) = √1,000,000 = 1,000 units. Total Inventory Cost at EOQ = Annual Ordering Cost + Annual Carrying Cost = (10,000/1,000 × 200) + (1,000/2 × 4) = ₹ 2,000 + ₹ 2,000 = ₹ 4,000.",
            icaiProvisionRef = "CMA Material Cost EOQ Formula"
        ),
        McqQuestion(
            id = "mcq_p4_04",
            paper = PaperCode.PAPER_4,
            topicId = "p4_ch5_abc",
            topicName = "Activity Based Costing (ABC)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "In Activity-Based Costing, which of the following is the most appropriate Cost Driver for the cost pool 'Machine Setup Costs'?",
            options = listOf(
                "A. Machine Hours",
                "B. Direct Labour Hours",
                "C. Number of Setups / Production Runs",
                "D. Units Produced"
            ),
            correctOptionIndex = 2,
            explanation = "In ABC, Machine Setup Cost varies directly with the number of times machines are configured for a batch, making 'Number of Setups / Production Runs' the most cause-and-effect direct cost driver.",
            icaiProvisionRef = "CMA Activity Based Costing Principles"
        ),

        // =========================================================================
        // PAPER 5: Auditing and Ethics (4 MCQs)
        // =========================================================================
        McqQuestion(
            id = "mcq_p5_01",
            paper = PaperCode.PAPER_5,
            topicId = "p5_ch9_items_fin_stmt",
            topicName = "Audit of Items of Financial Statements",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = """
                During the statutory audit of Omega Ltd. for FY 2025-26, the auditor observes that the company revalued its freehold land from ₹ 50 Lakhs to ₹ 120 Lakhs based on an independent valuation report. The valuation surplus of ₹ 70 Lakhs was credited directly to Statement of Profit and Loss as an exceptional income item.
            """.trimIndent(),
            questionStem = "Is the accounting and disclosure treatment followed by Omega Ltd. correct under AS 10 / Schedule III, and what impact does it have on the audit report?",
            options = listOf(
                "A. Treatment is correct as revaluation gain is a realized business income",
                "B. Treatment is incorrect; revaluation surplus must be credited to Revaluation Reserve. If uncorrected, auditor should issue a Qualified or Adverse opinion u/s SA 705",
                "C. Treatment is incorrect, but auditor can accept if disclosed in Notes without report qualification",
                "D. Treatment is correct provided tax is deducted at source"
            ),
            correctOptionIndex = 1,
            explanation = "Under AS 10 (Property, Plant and Equipment), an increase in carrying amount on revaluation must be credited directly to Revaluation Reserve in Other Comprehensive Income / Reserve, NOT P&L. Crediting ₹ 70 L to P&L materially overstates profit, necessitating a Qualified or Adverse opinion under SA 705.",
            icaiProvisionRef = "AS 10 (Revised) & SA 705 Modifications"
        ),
        McqQuestion(
            id = "mcq_p5_02",
            paper = PaperCode.PAPER_5,
            topicId = "p5_sa500_505",
            topicName = "Audit Evidence (SA 500 & SA 505 External Confirmations)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "The auditor sends a negative external confirmation request to a trade receivable under SA 505. When is a negative confirmation request considered sufficient and appropriate audit evidence?",
            options = listOf(
                "A. When the risk of material misstatement is assessed as high",
                "B. When a large number of small, homogenous account balances are involved and the exception rate is expected to be very low",
                "C. For all compulsory Q1 balance sheet verification items",
                "D. Negative confirmation requests provide stronger evidence than positive requests in all scenarios"
            ),
            correctOptionIndex = 1,
            explanation = "SA 505 states negative confirmations provide less persuasive evidence than positive requests. They are used ONLY when RMM is low, large number of small balances exist, low exception rate is expected, and auditor has no reason to believe respondents will disregard requests.",
            icaiProvisionRef = "SA 505 External Confirmations Paragraph 15"
        ),
        McqQuestion(
            id = "mcq_p5_03",
            paper = PaperCode.PAPER_5,
            topicId = "p5_ch10_banks_entities_caro",
            topicName = "CARO 2020 & Bank Audit",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "Under Clause 3(i)(c) of CARO 2020, what specific reporting is required from the statutory auditor regarding immovable properties?",
            options = listOf(
                "A. Whether physical verification was done every 6 months",
                "B. Whether title deeds of all immovable properties (other than properties where company is lessee and lease agreements are executed) disclosed in financial statements are held in the name of the company",
                "C. Whether revaluation was conducted by a registered valuer",
                "D. Whether mortgage charges are registered with ROC within 15 days"
            ),
            correctOptionIndex = 1,
            explanation = "CARO 2020 Clause 3(i)(c) requires the auditor to report whether title deeds of all immovable properties (other than lessee properties with valid lease deeds) are held in the company's name, detailing any exceptions.",
            icaiProvisionRef = "CARO 2020 Order Clause 3(i)(c)"
        ),
        McqQuestion(
            id = "mcq_p5_04",
            paper = PaperCode.PAPER_5,
            topicId = "p5_ch7_sa700_701_705_706_710",
            topicName = "Audit Report (SA 700, 705, 706, 701 KAM)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "Under SA 701, Key Audit Matters (KAM) are required to be communicated in the auditor's report for which category of entities?",
            options = listOf(
                "A. All private limited companies",
                "B. All listed entities and entities where communicating KAM is required by law or regulation",
                "C. Only banking and insurance companies",
                "D. All companies having paid up share capital > ₹ 10 Crores"
            ),
            correctOptionIndex = 1,
            explanation = "SA 701 applies mandatory reporting of Key Audit Matters (KAM) to audits of complete sets of general purpose financial statements of LISTED entities, or when otherwise required by statute.",
            icaiProvisionRef = "SA 701 Paragraph 5"
        ),

        // =========================================================================
        // PAPER 6: Financial & Strategic Management (4 MCQs)
        // =========================================================================
        McqQuestion(
            id = "mcq_p6_01",
            paper = PaperCode.PAPER_6,
            topicId = "p6_fm_working_capital",
            topicName = "Working Capital Management & Operating Cycle",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = """
                A manufacturing firm provides the following operational data for FY 2025-26:
                - Raw Material Holding Period = 45 days
                - Work-in-Progress (WIP) Conversion Period = 20 days
                - Finished Goods Storage Period = 30 days
                - Debtors Collection Period = 40 days
                - Creditors Payment Period = 50 days
            """.trimIndent(),
            questionStem = "Calculate the Net Operating Cycle (in days) and the number of operating cycles in a year of 360 days.",
            options = listOf(
                "A. Net Operating Cycle = 85 days; Number of Cycles = 4.24",
                "B. Net Operating Cycle = 135 days; Number of Cycles = 2.67",
                "C. Net Operating Cycle = 85 days; Number of Cycles = 4.24",
                "D. Net Operating Cycle = 85 days; Number of Cycles = 4.24"
            ),
            correctOptionIndex = 0,
            explanation = "Gross Operating Cycle = RM (45) + WIP (20) + FG (30) + Debtors (40) = 135 days. Net Operating Cycle = Gross Operating Cycle - Creditors Payment Period = 135 - 50 = 85 days. Operating cycles in a year = 360 / 85 = 4.24 cycles.",
            icaiProvisionRef = "FM Working Capital Operating Cycle Formula"
        ),
        McqQuestion(
            id = "mcq_p6_02",
            paper = PaperCode.PAPER_6,
            topicId = "p6_wacc",
            topicName = "Cost of Capital & WACC Computation",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "Titan Ltd. has 10% Debentures of face value ₹ 100 each issued at par, redeemable after 5 years at a premium of 5%. The corporate tax rate is 30%. Using the approximation method, the effective after-tax cost of debt (Kd) is approximately:",
            options = listOf(
                "A. 7.00%",
                "B. 7.80%",
                "C. 10.00%",
                "D. 8.50%"
            ),
            correctOptionIndex = 1,
            explanation = "Interest (I) = ₹ 10. Tax rate (t) = 0.3. Redemption Value (RV) = ₹ 105. Net Proceeds (NP) = ₹ 100. Kd = [I(1 - t) + (RV - NP)/N] / [(RV + NP)/2] = [10(1 - 0.3) + (105 - 100)/5] / [(105 + 100)/2] = [7 + 1] / [102.5] = 8 / 102.5 ≈ 7.80%.",
            icaiProvisionRef = "FM Cost of Debt Formula"
        ),
        McqQuestion(
            id = "mcq_p6_03",
            paper = PaperCode.PAPER_6,
            topicId = "p6_sm_strategic_choices",
            topicName = "Strategic Choices (BCG, Ansoff, Porter)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "In Ansoff's Product-Market Growth Matrix, when a firm introduces a completely NEW product into a completely NEW market segment, the strategic growth posture is categorized as:",
            options = listOf(
                "A. Market Penetration",
                "B. Market Development",
                "C. Product Development",
                "D. Diversification"
            ),
            correctOptionIndex = 3,
            explanation = "Ansoff Matrix defines 4 strategies: Existing Product + Existing Market = Market Penetration; New Product + Existing Market = Product Development; Existing Product + New Market = Market Development; New Product + New Market = Diversification.",
            icaiProvisionRef = "SM Chapter 4 Ansoff Growth Matrix"
        ),
        McqQuestion(
            id = "mcq_p6_04",
            paper = PaperCode.PAPER_6,
            topicId = "p6_sm_implementation",
            topicName = "Strategy Implementation & Change Management (Lewin)",
            priorityTier = PriorityTier.TIER_1,
            caseScenarioText = null,
            questionStem = "In Kurt Lewin's 3-Stage Change Model, which stage involves establishing new habits, cementing new behavioral norms, and aligning organizational culture so employees do not regress to old practices?",
            options = listOf(
                "A. Unfreezing",
                "B. Changing / Moving",
                "C. Refreezing",
                "D. Restructuring"
            ),
            correctOptionIndex = 2,
            explanation = "Kurt Lewin's 3 stages are: 1. Unfreezing (breaking old habits), 2. Changing (implementing new processes), 3. Refreezing (locking in changes as organizational culture).",
            icaiProvisionRef = "SM Chapter 5 Change Management u/s Kurt Lewin"
        )
    )

    fun getMcqsForPaper(paper: PaperCode): List<McqQuestion> {
        return mcqs.filter { it.paper == paper }
    }

    fun getMcqsForTopic(topicId: String): List<McqQuestion> {
        val matched = mcqs.filter { it.topicId.equals(topicId, ignoreCase = true) }
        return if (matched.isNotEmpty()) matched else mcqs.take(4)
    }

    fun getAllMcqs(): List<McqQuestion> = mcqs
}
