package com.example.data.curriculum

import com.example.data.model.CatchLabScenario
import com.example.data.model.PaperCode
import com.example.data.model.SubtleError

object CuratedCatchLabScenarios {
    val scenarios: List<CatchLabScenario> by lazy {
        listOf(
        // Paper 1: AS 4 Event after Balance Sheet Date
        CatchLabScenario(
            id = "cl_as4_case",
            paper = PaperCode.PAPER_1,
            topic = "AS 4: Events Occurring After Balance Sheet Date",
            title = "The Warehouse Fire & Debtor Insolvency Mystery",
            caseSnippet = """
During the audit of Zenith Ltd for FY 2024-25 (B/S date 31st March 2025, Board approval date 15th July 2025), Lab (Junior Article) made these working note conclusions:
1. "A major customer who owed ₹15 Lakhs on 31st March was declared insolvent on 28th April 2025 due to a prolonged decline in his financial position. Since the insolvency declaration took place after the Balance Sheet date, I classified this as a Non-Adjusting Event and did not adjust the debtors."
2. "A fire occurred on 10th May 2025 destroying ₹40 Lakhs of inventory, which did not threaten going concern. I created a provision in the Balance Sheet as of 31st March 2025 as an Adjusting Event."
3. "The Board proposed a dividend of ₹10 Lakhs on 15th July 2025, so I recognized it as a liability on the Balance Sheet as of 31st March 2025 in accordance with AS 4."
            """.trimIndent(),
            errorCount = 3,
            subtleErrors = listOf(
                SubtleError(
                    wrongStatement = "Insolvency of debtor after year-end classified as Non-Adjusting Event",
                    errorExplanation = "Under AS 4, insolvency of a customer after balance sheet date confirms that condition of insolvency existed on the balance sheet date. Hence, it is an ADJUSTING EVENT requiring adjustment to trade receivables.",
                    correctRuleOrLimit = "AS 4 Para 8.2: Adjusting event providing additional evidence of conditions existing at the balance sheet date."
                ),
                SubtleError(
                    wrongStatement = "Fire destroying inventory after year-end treated as Adjusting Event",
                    errorExplanation = "The fire occurred on 10th May, which is an event after the balance sheet date not relating to conditions existing at 31st March. Unless going concern is threatened, it is a NON-ADJUSTING EVENT disclosed in the Board's Report.",
                    correctRuleOrLimit = "AS 4 Para 13: Non-adjusting event disclosed in approving authority's report, no adjustment in accounts."
                ),
                SubtleError(
                    wrongStatement = "Proposed dividend recognized as a liability on the Balance sheet date",
                    errorExplanation = "As per amended AS 4, proposed dividend after balance sheet date but before approval is NOT recognized as a liability on the balance sheet date because no obligation exists on that date. It is disclosed in Notes to Accounts.",
                    correctRuleOrLimit = "AS 4 (Revised) Para 14: Proposed dividend after balance sheet date is disclosed only in notes."
                )
            ),
            correctIcaiProvision = "AS 4 requires distinguishing whether events provide additional evidence of conditions existing at the balance sheet date (Adjusting) or represent subsequent conditions (Non-adjusting with disclosure). Proposed dividends never create present obligations on reporting date."
        ),

        // Paper 2: Companies Act Sec 185 vs 186
        CatchLabScenario(
            id = "cl_law_185_186",
            paper = PaperCode.PAPER_2,
            topic = "Companies Act 2013: Sec 185 & 186",
            title = "Lab's Corporate Loan Audit Memorandum",
            caseSnippet = """
Lab prepared a compliance memo for Alpha Ltd (Public Company):
1. "The company gave an unsecured loan of ₹50 Lakhs to Mr. Rohit, a Non-Executive Director. Lab confirmed this is valid because the loan was passed by a Special Resolution in an Extraordinary General Meeting."
2. "Under Section 186, Alpha Ltd made investments exceeding limits. Lab noted that the threshold under Sec 186 is 50% of paid-up capital + free reserves OR 100% of securities premium, whichever is lower."
3. "The Board meeting approving the Sec 186 loan had 5 directors present. The resolution was passed with 3 directors voting in favor and 2 abstaining. Lab reported this as a validly passed Board Resolution."
            """.trimIndent(),
            errorCount = 3,
            subtleErrors = listOf(
                SubtleError(
                    wrongStatement = "Loan to individual director Rohit approved by Special Resolution is valid",
                    errorExplanation = "Under Section 185(1), there is an absolute prohibition on giving loans, guarantees, or securities directly to any director or relative. Special resolution exemption under Sec 185(2) is available ONLY to entities/persons in whom director is interested, NOT directly to the director himself!",
                    correctRuleOrLimit = "Sec 185(1) strictly prohibits loans directly to individual directors."
                ),
                SubtleError(
                    wrongStatement = "Section 186 threshold is 50% of capital + free reserves or 100% of securities premium, whichever is lower",
                    errorExplanation = "The statutory threshold under Section 186(2) is 60% of paid-up share capital + free reserves + securities premium OR 100% of free reserves + securities premium, whichever is HIGHER.",
                    correctRuleOrLimit = "Sec 186(2): 60% of (Paid up capital + Free reserves + Securities premium) OR 100% of (Free reserves + Securities premium), whichever is more."
                ),
                SubtleError(
                    wrongStatement = "Sec 186 Board Resolution passed by simple majority with 3 out of 5 voting in favor",
                    errorExplanation = "Section 186(5) mandates a UNANIMOUS Board Resolution at a meeting of the Board with the consent of all directors present at the meeting. 3 out of 5 does not fulfill the unanimous requirement.",
                    correctRuleOrLimit = "Sec 186(5): Prior approval of all directors present at the Board meeting is mandatory."
                )
            ),
            correctIcaiProvision = "Section 185 forbids direct loans to individual directors. Section 186 allows loans/investments up to 60%/100% limit via unanimous board consent, and requires prior special resolution for exceeding limits."
        ),

        // Paper 3: GST ITC Blocked Credits Sec 17(5)
        CatchLabScenario(
            id = "cl_gst_17_5",
            paper = PaperCode.PAPER_3,
            topic = "GST: Section 17(5) Blocked Credits",
            title = "Lab's GSTR-3B Input Tax Credit Reconciliation",
            caseSnippet = """
Lab computed eligible ITC for Bharat Manufacturing Pvt Ltd for November 2024:
1. "Claimed ₹3.6 Lakhs GST paid on purchasing a 7-seater SUV used exclusively by the Sales Director to visit factory sites and meet prospective corporate clients."
2. "Claimed ₹80,000 GST on outdoor catering services hired for the Annual General Meeting of shareholders, reasoning that shareholder meetings are legally mandatory under the Companies Act."
3. "Claimed ₹50,000 GST on raw materials that were destroyed in a boiler explosion during trial production, since the loss was an unavoidable business expense."
            """.trimIndent(),
            errorCount = 3,
            subtleErrors = listOf(
                SubtleError(
                    wrongStatement = "Claimed ITC on 7-seater SUV used for company directors and factory visits",
                    errorExplanation = "Under Sec 17(5)(a), ITC on motor vehicles for transportation of persons having approved seating capacity <= 13 persons (including driver) is strictly BLOCKED, unless used for taxable supply of vehicles, passenger transport, or driving school.",
                    correctRuleOrLimit = "Sec 17(5)(a): Blocked credit for motor vehicles <= 13 seats used for ordinary business travel."
                ),
                SubtleError(
                    wrongStatement = "Claimed ITC on outdoor catering for mandatory shareholder AGM",
                    errorExplanation = "Under Sec 17(5)(b)(i), ITC on outdoor catering is blocked unless an employer is statutorily obligated to provide food under any law (e.g. Factories Act canteen). Companies Act requires AGM, but does NOT obligate providing catering!",
                    correctRuleOrLimit = "Sec 17(5)(b)(i): Catering is blocked unless specific statutory law mandates providing catering to employees."
                ),
                SubtleError(
                    wrongStatement = "Claimed ITC on raw materials destroyed in explosion",
                    errorExplanation = "Under Section 17(5)(h), input tax credit is blocked in respect of goods lost, stolen, destroyed, written off, or disposed of by way of gift or free samples.",
                    correctRuleOrLimit = "Sec 17(5)(h): ITC must be reversed/blocked on destroyed goods."
                )
            ),
            correctIcaiProvision = "Section 17(5) overrides Section 16. Passenger vehicles <= 13 seats, non-statutory catering, and destroyed inputs cannot be claimed as ITC under any circumstances."
        ),

        // Paper 4: Standard Costing Variances
        CatchLabScenario(
            id = "cl_cma_variance",
            paper = PaperCode.PAPER_4,
            topic = "Costing: Material & Labour Variances",
            title = "Lab's Factory Standard Cost Sheet Review",
            caseSnippet = """
Lab analyzed the variance report for product 'Kisan-X':
- Standard: 10 kg @ ₹20/kg per finished unit.
- Actual: Output 1,000 units, Material consumed 11,500 kg @ ₹22/kg.
Lab's working notes stated:
1. "Material Usage Variance = Actual Price * (Standard Quantity - Actual Quantity) = ₹22 * (10,000 - 11,500) = ₹33,000 Adverse."
2. "Material Price Variance = Standard Quantity * (Standard Price - Actual Price) = 10,000 * (₹20 - ₹22) = ₹20,000 Adverse."
3. "Lab noted: If material price variance is computed at point of purchase instead of usage, the reconciliation between total standard cost and total actual cost will remain identical without any inventory adjustment."
            """.trimIndent(),
            errorCount = 3,
            subtleErrors = listOf(
                SubtleError(
                    wrongStatement = "Material Usage Variance computed using Actual Price (₹22)",
                    errorExplanation = "Material Usage Variance must ALWAYS be valued at STANDARD PRICE (SP), not Actual Price. MUV = SP * (SQ - AQ) = ₹20 * (10,000 - 11,500) = ₹30,000 Adverse.",
                    correctRuleOrLimit = "Formula: MUV = Standard Price * (Standard Quantity - Actual Quantity)."
                ),
                SubtleError(
                    wrongStatement = "Material Price Variance computed using Standard Quantity (10,000 kg)",
                    errorExplanation = "Material Price Variance reflects the price difference paid on the ACTUAL QUANTITY (AQ) purchased/consumed, not the standard quantity! MPV = AQ * (SP - AP) = 11,500 * (₹20 - ₹22) = ₹23,000 Adverse.",
                    correctRuleOrLimit = "Formula: MPV = Actual Quantity * (Standard Price - Actual Price)."
                ),
                SubtleError(
                    wrongStatement = "Computing MPV at purchase point produces identical reconciliation without inventory adjustment",
                    errorExplanation = "When MPV is calculated at point of purchase, ending inventory of raw materials is held at standard price, requiring an inventory variance adjustment in cost reconciliation.",
                    correctRuleOrLimit = "Material variance reconciliation requires inventory holding adjustment when MPV is isolated at purchase."
                )
            ),
            correctIcaiProvision = "Material Cost Variance = MPV + MUV. MPV applies actual quantity to price differences; MUV isolates physical consumption using standard price."
        ),

        // Paper 5: SA 505 External Confirmations & SA 230
        CatchLabScenario(
            id = "cl_sa505_audit",
            paper = PaperCode.PAPER_5,
            topic = "Auditing: SA 505 External Confirmations",
            title = "Lab's Audit Working Papers on Trade Receivables",
            caseSnippet = """
During the statutory audit of Supreme Retail Ltd, Lab drafted audit documentation for debtor balances:
1. "The CFO asked the audit team not to send external balance confirmations to the top 3 debtors (representing 45% of total book debts) citing fragile customer relationships. Lab concluded: 'Since management has the authority over client relations, the auditor must defer to management and accept an internal management representation letter as sufficient appropriate audit evidence.'"
2. "For a foreign debtor confirmation returned with a discrepancy of ₹12 Lakhs, Lab noted that since it was an email reply from the debtor's commercial email id, it provides direct, conclusive proof requiring no further verification of sender authenticity or controls."
3. "Lab documented the entire audit file 90 days after the date of the auditor's report, stating that SA 230 allows an indefinite assembly period for non-listed entities."
            """.trimIndent(),
            errorCount = 3,
            subtleErrors = listOf(
                SubtleError(
                    wrongStatement = "Auditor should defer to management refusal and accept management representation letter alone",
                    errorExplanation = "Under SA 505, if management refuses to allow confirmation, auditor cannot simply accept it. Auditor MUST inquire into reasons, evaluate validity, assess fraud risk (SA 240), and perform ALTERNATIVE AUDIT PROCEDURES. Management representation is NOT a substitute for SAAE.",
                    correctRuleOrLimit = "SA 505 Para 8 & SA 580: Management refusal requires inquiry, assessing fraud implications, and alternative testing."
                ),
                SubtleError(
                    wrongStatement = "Email confirmation is conclusive proof with no need to verify sender authenticity",
                    errorExplanation = "Under SA 505 (A12), electronic responses involve risks regarding reliability (tampering, sender identity). The auditor must perform procedures to address these risks, such as verifying the sender's origin or making a direct telephone call to the sender.",
                    correctRuleOrLimit = "SA 505 Para 10: Auditor shall maintain control and verify authenticity of electronic confirmations."
                ),
                SubtleError(
                    wrongStatement = "Audit file assembled 90 days after report date under SA 230",
                    errorExplanation = "Under SA 230 and SQC 1, the assembly of the final audit file should ordinarily be completed within NOT MORE THAN 60 DAYS after the date of the auditor's report (not 90 days).",
                    correctRuleOrLimit = "SA 230 & SQC 1: Final audit file assembly deadline is within 60 days of the auditor's report date."
                )
            ),
            correctIcaiProvision = "SA 505 requires independent control over confirmation requests. Management refusal requires investigating motives and applying alternative procedures. SA 230 mandates file assembly within 60 days."
        ),

        // Paper 6: Financial Management - Capital Budgeting & Leverage
        CatchLabScenario(
            id = "cl_fm_leverage",
            paper = PaperCode.PAPER_6,
            topic = "FM: Leverage & Cost of Capital",
            title = "Lab's Investment Appraisal & WACC Sheet",
            caseSnippet = """
Lab prepared a project feasibility note for New Horizons Ltd:
1. "The company's EBIT is ₹20 Lakhs, Interest is ₹5 Lakhs, and Preference Dividend is ₹3 Lakhs (Tax rate 30%). Lab computed Degree of Financial Leverage (DFL) as: EBIT / (EBIT - Interest - Preference Dividend) = 20 / (20 - 5 - 3) = 1.67."
2. "In computing Weighted Average Cost of Capital (WACC), Lab assigned Book Value weights instead of available Market Value weights, stating that ICAI guidelines always mandate book values because market values fluctuate daily."
3. "For project evaluation under Net Present Value (NPV), Lab deducted the annual interest payment from the operating cash inflows before discounting at the WACC."
            """.trimIndent(),
            errorCount = 3,
            subtleErrors = listOf(
                SubtleError(
                    wrongStatement = "DFL calculated by directly deducting Preference Dividend without tax grossing-up",
                    errorExplanation = "Preference dividends are paid out of post-tax profits! To determine pre-tax financial burden, Preference Dividend must be grossed up by (1 - t). DFL = EBIT / [EBIT - I - (Dp / (1 - t))] = 20 / [20 - 5 - (3 / 0.70)] = 20 / [15 - 4.286] = 1.87.",
                    correctRuleOrLimit = "Formula: DFL = EBIT / [EBT - Dp/(1 - t)]."
                ),
                SubtleError(
                    wrongStatement = "WACC must always use Book Value weights even when Market Values are available",
                    errorExplanation = "Financial management principles explicitly prefer MARKET VALUE WEIGHTS because the cost of capital represents the current opportunity cost of raising capital in the market. Book values only reflect historical costs.",
                    correctRuleOrLimit = "WACC weights: Market value weights are conceptually superior and preferred over book value weights."
                ),
                SubtleError(
                    wrongStatement = "Deducted annual interest payments from project cash flows discounted at WACC",
                    errorExplanation = "This causes DOUBLE COUNTING of financing costs! WACC already reflects the cost of debt (Kd). Deducting interest from cash flows subtracts interest expense twice.",
                    correctRuleOrLimit = "Capital Budgeting Rule: Cash flows must be Operating Cash Flows before financing costs when discounted by WACC."
                )
            ),
            correctIcaiProvision = "Financial leverage reflects pre-tax equivalence for preference dividends. Cash flows for DCF must be unlevered operating cash flows to avoid double counting the cost of debt incorporated in WACC."
        )
    ) }

    fun getScenariosForPaper(paper: PaperCode): List<CatchLabScenario> {
        return scenarios.filter { it.paper == paper }
    }
}
