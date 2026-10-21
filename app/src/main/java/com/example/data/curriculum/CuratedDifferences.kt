package com.example.data.curriculum

import com.example.data.model.ComparisonRow
import com.example.data.model.ConceptDifference
import com.example.data.model.PaperCode

object CuratedDifferences {
    val differences: List<ConceptDifference> by lazy {
        listOf(
        // AS 5: Prior Period Items vs Change in Accounting Policy
        ConceptDifference(
            id = "diff_as5",
            paper = PaperCode.PAPER_1,
            title = "Prior Period Items vs Change in Accounting Policy",
            conceptA = "Prior Period Items (AS 5)",
            conceptB = "Change in Accounting Policy (AS 5)",
            comparisonPoints = listOf(
                ComparisonRow(
                    basisOfDifference = "Nature / Origin",
                    conceptAValue = "Arises from errors, mathematical mistakes, or omissions in preparing earlier periods' financial statements.",
                    conceptBValue = "Deliberate change in accounting principles/bases adopted for preparing and presenting financial statements."
                ),
                ComparisonRow(
                    basisOfDifference = "Permissibility / Justification",
                    conceptAValue = "Unintended error discovered subsequently; not a matter of choice or standard requirement.",
                    conceptBValue = "Permitted ONLY IF required by statute, compliance with an AS, or more appropriate presentation of financial statements."
                ),
                ComparisonRow(
                    basisOfDifference = "Disclosure in P&L",
                    conceptAValue = "Shown separately in the statement of Profit & Loss after determining profit/loss from ordinary activities.",
                    conceptBValue = "Impact of change disclosed in the notes/P&L in the period of change, and future periods if material."
                ),
                ComparisonRow(
                    basisOfDifference = "Quantification",
                    conceptAValue = "Exact error amount identified from prior years is stated separately so effect on current operating results is clear.",
                    conceptBValue = "Ascertainable financial impact disclosed. If unascertainable wholly or partly, fact must be disclosed."
                ),
                ComparisonRow(
                    basisOfDifference = "ICAI Exam Example",
                    conceptAValue = "Omitting invoice of ₹5 Lakhs in FY 2023-24 discovered during audit of FY 2024-25.",
                    conceptBValue = "Switching inventory valuation method from Weighted Average to FIFO."
                )
            ),
            memoryHook = "Error in past = Prior Period. Choice for better presentation = Accounting Policy."
        ),

        // Income Tax: Sec 194C vs Sec 194J
        ConceptDifference(
            id = "diff_tds_194c_194j",
            paper = PaperCode.PAPER_3,
            title = "Section 194C (Contractors) vs Section 194J (Professionals)",
            conceptA = "Section 194C (Work Contracts)",
            conceptB = "Section 194J (Professional & Technical Fees)",
            comparisonPoints = listOf(
                ComparisonRow(
                    basisOfDifference = "Scope of Work",
                    conceptAValue = "Carrying out any 'work' including advertising, broadcasting, catering, carriage of goods/passengers (excluding railways).",
                    conceptBValue = "Fees for professional services, technical services (FTS), royalty, or non-compete fees."
                ),
                ComparisonRow(
                    basisOfDifference = "TDS Rate",
                    conceptAValue = "1% if payee is Individual/HUF; 2% for any other person (company, firm).",
                    conceptBValue = "10% for professional fees/royalty; 2% for Fees for Technical Services (FTS) & call centers."
                ),
                ComparisonRow(
                    basisOfDifference = "Threshold Exemption",
                    conceptAValue = "Single contract sum <= ₹30,000 AND aggregate contracts in financial year <= ₹1,00,000.",
                    conceptBValue = "₹30,000 separate threshold for each category (Professional, Technical, Royalty, Non-compete)."
                ),
                ComparisonRow(
                    basisOfDifference = "Sub-contractors",
                    conceptAValue = "Applies equally to contractors and sub-contractors.",
                    conceptBValue = "No contractor/sub-contractor distinction; applies directly to payee providing professional/technical service."
                ),
                ComparisonRow(
                    basisOfDifference = "Transport Operators (44AE)",
                    conceptAValue = "No TDS if contractor owns <= 10 goods carriages at any time during the year and furnishes PAN and declaration.",
                    conceptBValue = "No such transport operator exemption exists under 194J."
                )
            ),
            memoryHook = "194C = physical contract work (1%/2%, 30k/1L). 194J = intellectual brains/services (10%/2%, 30k)."
        ),

        // Corporate Law: Section 185 vs Section 186
        ConceptDifference(
            id = "diff_law_185_186",
            paper = PaperCode.PAPER_2,
            title = "Section 185 vs Section 186 of Companies Act, 2013",
            conceptA = "Section 185 (Loans to Directors)",
            conceptB = "Section 186 (Loans & Investments by Co.)",
            comparisonPoints = listOf(
                ComparisonRow(
                    basisOfDifference = "Target Beneficiary",
                    conceptAValue = "Prohibits/regulates loans, guarantees, or securities to DIRECTORS or persons in whom director is interested.",
                    conceptBValue = "Regulates loans, guarantees, securities, and investments in shares/securities of ANY PERSON or body corporate."
                ),
                ComparisonRow(
                    basisOfDifference = "General Principle",
                    conceptAValue = "General Rule is PROHIBITION on direct loans to directors (with narrow statutory exceptions).",
                    conceptBValue = "General Rule is PERMISSION up to statutory ceilings (60%/100%), requiring approval beyond limits."
                ),
                ComparisonRow(
                    basisOfDifference = "Board Approval Threshold",
                    conceptAValue = "Allowed only for MD/WTD as part of conditions of service, or entities with Special Resolution + utilized for principal business.",
                    conceptBValue = "Requires UNANIMOUS Board approval at Board meeting (all directors present voting in favor)."
                ),
                ComparisonRow(
                    basisOfDifference = "Statutory Cap",
                    conceptAValue = "No monetary percentage limit; either strictly prohibited or allowed under conditions.",
                    conceptBValue = "60% of (Paid-up capital + Free reserves + Securities premium) OR 100% of (Free reserves + Securities premium), whichever is higher."
                ),
                ComparisonRow(
                    basisOfDifference = "Shareholder Approval",
                    conceptAValue = "Special Resolution required for entities in which director is interested.",
                    conceptBValue = "Special Resolution required ONLY IF statutory threshold (60%/100%) is exceeded."
                )
            ),
            memoryHook = "185 checks WHO gets the loan (Directors - STOP!). 186 checks HOW MUCH is given (Limits & Ceilings)."
        ),

        // Auditing: Internal Audit vs External Audit
        ConceptDifference(
            id = "diff_audit_internal_external",
            paper = PaperCode.PAPER_5,
            title = "Internal Audit (Sec 138) vs Statutory External Audit (Sec 139)",
            conceptA = "Internal Audit (Section 138)",
            conceptB = "Statutory External Audit (Sec 139)",
            comparisonPoints = listOf(
                ComparisonRow(
                    basisOfDifference = "Governing Section",
                    conceptAValue = "Section 138 of Companies Act, 2013 read with Rule 13.",
                    conceptBValue = "Section 139 to 147 of Companies Act, 2013."
                ),
                ComparisonRow(
                    basisOfDifference = "Primary Objective",
                    conceptAValue = "Appraise internal control systems, operational efficiency, risk management, and governance processes.",
                    conceptBValue = "Express independent opinion on whether financial statements show a TRUE AND FAIR VIEW."
                ),
                ComparisonRow(
                    basisOfDifference = "Who Can Be Appointed?",
                    conceptAValue = "Chartered Accountant, Cost Accountant, or any other professional decided by the Board. May even be an employee!",
                    conceptBValue = "MUST be an independent Chartered Accountant / CA Firm in practice. Cannot be an employee or officer."
                ),
                ComparisonRow(
                    basisOfDifference = "Appointed By & Reports To",
                    conceptAValue = "Appointed by Board of Directors; submits report to Board / Audit Committee.",
                    conceptBValue = "Appointed by Shareholders (members) in AGM; submits report to Shareholders."
                ),
                ComparisonRow(
                    basisOfDifference = "Scope Determination",
                    conceptAValue = "Scope determined by Board/Management and Audit Committee.",
                    conceptBValue = "Scope mandated by statute (Companies Act) and Standards on Auditing (SAs). Management cannot restrict scope."
                )
            ),
            memoryHook = "Internal Audit serves the Board (Controls & Ops). Statutory Audit serves Shareholders (True & Fair View)."
        ),

        // Law: Void Agreement vs Voidable Contract vs Illegal Agreement
        ConceptDifference(
            id = "diff_law_void_illegal",
            paper = PaperCode.PAPER_2,
            title = "Void Agreement vs Illegal Agreement",
            conceptA = "Void Agreement (Sec 2(g))",
            conceptB = "Illegal Agreement",
            comparisonPoints = listOf(
                ComparisonRow(
                    basisOfDifference = "Definition",
                    conceptAValue = "An agreement not enforceable by law from the beginning (void ab initio).",
                    conceptBValue = "An agreement expressly forbidden by law or opposed to public policy / criminal in nature."
                ),
                ComparisonRow(
                    basisOfDifference = "Effect on Collateral Transactions",
                    conceptAValue = "Collateral transactions remain VALID and enforceable.",
                    conceptBValue = "Collateral transactions are also TAINTED and rendered completely VOID."
                ),
                ComparisonRow(
                    basisOfDifference = "Punitive Consequence",
                    conceptAValue = "Parties are not subject to criminal punishment merely for entering into a void agreement.",
                    conceptBValue = "Parties may be liable to penal prosecution and statutory fines/imprisonment."
                ),
                ComparisonRow(
                    basisOfDifference = "Breadth of Scope",
                    conceptAValue = "All illegal agreements are void, but NOT all void agreements are illegal.",
                    conceptBValue = "Subset of void agreements that carry statutory prohibition or criminal taint."
                )
            ),
            memoryHook = "Void is an empty shell (no effect). Illegal is poisonous (destroys collateral transactions too)."
        ),

        // Costing: Cost Control vs Cost Reduction
        ConceptDifference(
            id = "diff_cma_control_reduction",
            paper = PaperCode.PAPER_4,
            title = "Cost Control vs Cost Reduction",
            conceptA = "Cost Control",
            conceptB = "Cost Reduction",
            comparisonPoints = listOf(
                ComparisonRow(
                    basisOfDifference = "Primary Philosophy",
                    conceptAValue = "Maintaining costs in accordance with established standards or budgets.",
                    conceptBValue = "Real and permanent reduction in the unit cost of goods or services without impairing quality or suitability."
                ),
                ComparisonRow(
                    basisOfDifference = "Time Horizon",
                    conceptAValue = "Continuous, routine operational function (temporary/static benchmark).",
                    conceptBValue = "Continuous, dynamic process of challenging standards (permanent improvement)."
                ),
                ComparisonRow(
                    basisOfDifference = "Standard Assumption",
                    conceptAValue = "Assumes standards are fixed and achievable; seeks to eliminate variance.",
                    conceptBValue = "Challenges the standard itself; assumes there is always room for cost improvement."
                ),
                ComparisonRow(
                    basisOfDifference = "Techniques Used",
                    conceptAValue = "Budgetary control, Standard costing, Variance analysis.",
                    conceptBValue = "Value engineering, Value analysis, Work study, Business Process Re-engineering (BPR)."
                )
            ),
            memoryHook = "Control keeps you within the speed limit. Reduction builds a faster, more fuel-efficient car."
        ),

        // FM: Operating Leverage vs Financial Leverage
        ConceptDifference(
            id = "diff_fm_leverage",
            paper = PaperCode.PAPER_6,
            title = "Operating Leverage (DOL) vs Financial Leverage (DFL)",
            conceptA = "Operating Leverage (DOL)",
            conceptB = "Financial Leverage (DFL)",
            comparisonPoints = listOf(
                ComparisonRow(
                    basisOfDifference = "Core Definition",
                    conceptAValue = "Tendency of EBIT to vary disproportionately with changes in Sales volume.",
                    conceptBValue = "Tendency of EPS to vary disproportionately with changes in EBIT."
                ),
                ComparisonRow(
                    basisOfDifference = "Risk Measured",
                    conceptAValue = "Business / Operating Risk (inherent to cost structure).",
                    conceptBValue = "Financial Risk (arising from debt capital and fixed interest/preference burden)."
                ),
                ComparisonRow(
                    basisOfDifference = "Caused By",
                    conceptAValue = "Presence of Operating Fixed Costs (depreciation, rent, factory salaries).",
                    conceptBValue = "Presence of Fixed Financing Costs (interest on debentures/loans, preference dividends)."
                ),
                ComparisonRow(
                    basisOfDifference = "Formula",
                    conceptAValue = "DOL = Contribution / EBIT = (% Change in EBIT) / (% Change in Sales).",
                    conceptBValue = "DFL = EBIT / (EBT - [Dp / (1 - t)]) = (% Change in EPS) / (% Change in EBIT)."
                ),
                ComparisonRow(
                    basisOfDifference = "Ideal Strategy",
                    conceptAValue = "High DOL should ideally be combined with Low DFL to keep overall risk balanced.",
                    conceptBValue = "High DFL is safe only when DOL is low and operating cash flows are highly stable."
                )
            ),
            memoryHook = "DOL = Factory Fixed Costs (Sales to EBIT). DFL = Bank & Debt Fixed Costs (EBIT to EPS)."
        )
    ) }

    fun getDifferencesForPaper(paper: PaperCode): List<ConceptDifference> {
        return differences.filter { it.paper == paper }
    }
}
