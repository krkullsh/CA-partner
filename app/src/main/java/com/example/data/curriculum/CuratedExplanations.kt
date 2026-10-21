package com.example.data.curriculum

import com.example.data.model.DepthExplanation
import com.example.data.model.PaperCode

object CuratedExplanations {
    val explanations: List<DepthExplanation> by lazy {
        listOf(
        // Topic 1: AS 4 Events after Balance Sheet Date
        DepthExplanation(
            topicId = "p1_as4",
            topicName = "AS 4: Contingencies & Events After Balance Sheet Date",
            paper = PaperCode.PAPER_1,
            depth1 = """
☕ **Real-World Kirana Store Analogy (Simple):**
Imagine you run Sharma Kirana Store. On 31st March, you close your accounts.
Your register shows that Gupta Ji owes you ₹10,000 for groceries.
On 10th April, Gupta Ji goes bankrupt and leaves town.
Did he suddenly go bankrupt on 10th April? No! His financial condition was already broken on 31st March, you just received proof on 10th April. So you must **ADJUST** your 31st March accounts and write off that debt!
Now imagine a fire accidentally burns your delivery bicycle on 15th April. Did the fire exist on 31st March? No, that was completely new. You do **NOT adjust** the 31st March accounts, but you write a note in your register explaining the fire to the bank manager.
            """.trimIndent(),
            depth2 = """
📊 **Standard Structured Notes & Decision Tree:**

```
                  Event Occurs Between 
           Balance Sheet Date & Approval Date
                          |
             ----------------------------
            |                            |
Does it relate to conditions     Does it relate to conditions
existing on/before B/S date?     arising after B/S date?
            |                            |
     [ADJUSTING EVENT]            [NON-ADJUSTING EVENT]
            |                            |
Adjust assets & liabilities       No adjustment in accounts.
in the Financial Statements.      Disclose in Approving Authority's 
                                  Report (Board's Report) if material.
```

**Key Exceptions to Memorize:**
1. **Going Concern Impairment:** If events after B/S date indicate going concern assumption is invalid, it is ALWAYS an Adjusting Event (AS 4 Para 13).
2. **Proposed Dividend:** Declared after balance sheet date = Non-Adjusting Event! Disclosed only in Notes to Accounts (AS 4 Revised).
            """.trimIndent(),
            depth3 = """
🏛️ **ICAI Exam Answer Format:**

**1. Relevant Accounting Standard Provision:**
As per **AS 4 (Revised) 'Contingencies and Events Occurring After the Balance Sheet Date'**, events occurring between the balance sheet date and the date on which financial statements are approved by the Board of Directors that provide **additional information materially affecting the determination of amounts relating to conditions existing at the balance sheet date** are classified as **Adjusting Events**. Assets and liabilities should be adjusted for such events.
Events that do not relate to conditions existing at the balance sheet date are **Non-Adjusting Events** and are disclosed in the **Report of the Approving Authority**, unless they affect the fundamental accounting assumption of **Going Concern**.

**2. Facts & Analysis of the Case:**
- The debtor was already in deep financial distress prior to the reporting date, and the adjudication of insolvency subsequent to the balance sheet date merely confirms the condition of **uncollectibility existing on 31st March**.
- Therefore, the insolvency is an adjusting event providing confirmatory evidence.
- Conversely, the post-reporting date fire loss arose from an unexpected catastrophic event subsequent to the reporting date with no pre-existing condition at 31st March. Since the entity's going concern status remains intact, this constitutes a non-adjusting event.

**3. Conclusion & Financial Presentation:**
- An **allowance for bad debts / impairment loss** must be recognized in the Statement of Profit and Loss for the year ended 31st March, adjusting the carrying value of Trade Receivables.
- The inventory fire loss shall **not be adjusted** in the balance sheet, but a disclosure stating the nature of the event and an estimate of the financial effect must be made in the **Board of Directors' Report**.
            """.trimIndent()
        ),

        // Topic 2: Companies Act Section 185
        DepthExplanation(
            topicId = "p2_sec185",
            topicName = "Section 185: Loans to Directors",
            paper = PaperCode.PAPER_2,
            depth1 = """
🏢 **The Bank Locker Analogy (Simple):**
Imagine you and your friends pool money into a piggy bank for a community football tournament.
The captain of the team suddenly says: "Hey, give me ₹50,000 from the piggy bank to buy my personal motorcycle, I'll pay you back later."
Would you allow it? Absolutely not! The captain might never return it.
Section 185 is exactly that safeguard: Public and private companies hold money from shareholders and banks. Company directors cannot treat company funds as their personal ATMs. Direct loans to directors are strictly **BANNED**!
            """.trimIndent(),
            depth2 = """
📋 **Standard Legal Breakdown & Hierarchy:**

1. **Absolute Prohibition [Section 185(1)]:**
   - No company shall directly or indirectly advance any loan (including book debts), guarantee, or security to:
     - (a) Any director of the lending company or of its holding company, or any partner/relative of such director.
     - (b) Any firm in which any such director or relative is a partner.

2. **Conditional Exemption [Section 185(2)]:**
   - Loans, guarantees, or securities may be given to **any person in whom any of the director of the lending company is interested**, subject to:
     - Prior approval by **Special Resolution** in General Meeting.
     - Explanatory statement disclosing full particulars.
     - The borrowing entity must utilize loans **strictly for its principal business activities**.

3. **Statutory Exceptions [Section 185(3)]:**
   - Loan to Managing Director / Whole-time Director as part of condition of service extended to all employees, or scheme approved by Special Resolution.
   - Company in ordinary course of lending business with interest >= prevailing RBI bank rate yield.
   - Loan / guarantee given by Holding Company to Wholly Owned Subsidiary (WOS).
            """.trimIndent(),
            depth3 = """
⚖️ **ICAI Exam Presentation (Case Study Scoring Layout):**

**1. Provision of Law (Section 185 of Companies Act, 2013):**
Under **Section 185(1)** of the Companies Act, 2013, no company shall advance any loan, including any loan represented by a book debt, or give any guarantee or provide any security in connection with any loan taken by **any director of company or of a company which is its holding company or any partner or relative of any such director**, or **any firm in which any such director or relative is a partner**.
Under **Section 185(2)**, loans/guarantees to entities where a director is interested are permitted **only if passed by Special Resolution** and the borrower company uses the borrowing **solely for its principal business activities**.

**2. Analysis of the Facts:**
- The company advanced an unsecured loan directly to an individual director for personal real estate investment.
- Direct lending to an individual director is covered squarely under the absolute prohibition of **Section 185(1)**.
- The relaxation under Section 185(2) is applicable solely to corporate entities or bodies corporate in which a director is interested, and cannot be invoked to sanitize a direct loan to an individual director.
- Furthermore, real estate speculative purchase does not qualify under employment service conditions or principal business usage.

**3. Conclusion & Penalties:**
- The transaction constitutes a **wilful contravention of Section 185**.
- The company is punishable with a fine of not less than **₹5 Lakhs extendable up to ₹25 Lakhs**.
- The director who accepted the loan is punishable with **imprisonment up to 6 months or fine of ₹5 Lakhs to ₹25 Lakhs, or both**, and the loan must be repaid immediately.
            """.trimIndent()
        ),

        // Topic 3: GST Section 17(5) Blocked Credits
        DepthExplanation(
            topicId = "p3_gst_itc_17_5",
            topicName = "GST Section 17(5): Blocked Credits",
            paper = PaperCode.PAPER_3,
            depth1 = """
🚗 **The Company Car vs Delivery Truck Analogy (Simple):**
Suppose you run a sweet shop (Mithai shop).
If you buy a delivery tempo to deliver Laddoos to customers, the government says: "Great, that delivery van directly sells goods. You can claim full GST input credit on it!"
But if you buy a luxury Honda City sedan for yourself to drive home in style, the government says: "Hold on! That sedan is personal comfort. You will use it on weekends for family trips."
Section 17(5) is the government's blocklist: Even if you are a registered business, personal comforts like passenger cars (<=13 seats), club gym memberships, and outdoor wedding catering are **BLOCKED from ITC**.
            """.trimIndent(),
            depth2 = """
🛑 **Section 17(5) Rapid Revision Matrix:**

| Clause | Item / Service | ITC Status | Permitted Exception |
| :--- | :--- | :--- | :--- |
| **17(5)(a)** | Motor vehicles for transport of persons (<= 13 seats) | **BLOCKED** | Taxable supply of vehicles, passenger transport, flying/driving training schools |
| **17(5)(aa)** | Vessels and Aircraft | **BLOCKED** | Commercial transport of goods/passengers, navigation training |
| **17(5)(b)(i)** | Food, beverages, outdoor catering, cosmetic surgery | **BLOCKED** | Statutory obligation by law, or used for outward taxable supply of same category |
| **17(5)(b)(ii)**| Club, health and fitness centre membership | **BLOCKED** | No exceptions |
| **17(5)(c)** | Works contract for construction of immovable property | **BLOCKED** | Plant and Machinery, or used for further supply of works contract |
| **17(5)(h)** | Lost, stolen, destroyed, written off, gifts / samples | **BLOCKED** | Nil (Must reverse input tax credit) |
            """.trimIndent(),
            depth3 = """
🏛️ **ICAI Exam Answer Format:**

**1. Statutory Legal Provision:**
As per **Section 17(5)(a) of the CGST Act, 2017**, input tax credit shall not be available in respect of **motor vehicles for transportation of persons having approved seating capacity of not more than 13 persons (including the driver)**, except when they are used for making the following taxable supplies, namely:
(A) Further supply of such motor vehicles; or
(B) Transportation of passengers; or
(C) Imparting training on driving such motor vehicles.
Furthermore, under **Section 17(5)(h)**, ITC is blocked in respect of **goods lost, stolen, destroyed, written off or disposed of by way of gift or free samples**.

**2. Facts & Analysis of the Case:**
- The assessee company procured a 7-seater sports utility vehicle (SUV) for use by executive personnel to commute to customer factories. The vehicle possesses a seating capacity below the 13-person threshold and is not deployed in passenger transportation business or driving school instruction.
- The company incurred catering expenses for employees during an internal marketing workshop. There is no provision under the Factories Act or any prevailing statute mandating the provision of free outdoor catering for corporate office employees.
- The raw materials damaged in storage represent goods destroyed, attracting mandatory reversal under clause (h).

**3. Conclusion:**
- ITC claimed on the motor vehicle (**₹3,60,000**) and outdoor catering (**₹80,000**) is **ineligible and must be reversed**.
- The credit availed on destroyed inventory must be **reversed in Table 4(B) of Form GSTR-3B**, along with applicable interest under Section 50.
            """.trimIndent()
        ),

        // Topic 4: Costing Material Variances
        DepthExplanation(
            topicId = "p4_standard_costing",
            topicName = "Standard Costing: Material Variances",
            paper = PaperCode.PAPER_4,
            depth1 = """
🍕 **The Dosa Corner Recipe Analogy (Simple):**
You run a South Indian food stall.
Your chef's standard recipe says: 100 Dosas require **10 kg batter at ₹30/kg** = **₹300 standard cost**.
At the end of the day, you made 100 Dosas, but the records show you used **12 kg batter at ₹35/kg** = **₹420 actual cost**.
Why did you spend ₹120 extra?
1. **The Price Leak (MPV):** You paid ₹5 extra per kg for 12 kg = ₹60 loss because purchasing was expensive.
2. **The Chef's Waste (MUV):** The chef used 2 extra kg. At the budgeted price of ₹30/kg, that wasted batter cost you ₹60.
Total variance = ₹60 (Price) + ₹60 (Usage) = **₹120 Adverse**.
Notice: You evaluated the chef's wastage using the **budgeted ₹30 price**, because the chef didn't negotiate the purchase price!
            """.trimIndent(),
            depth2 = """
📐 **Standard Costing Formulas & Working Note Architecture:**

```
                        [Material Cost Variance (MCV)]
                      (SQ * SP) - (AQ * AP)
                                 |
              ---------------------------------------
             |                                       |
  [Material Price Variance (MPV)]      [Material Usage Variance (MUV)]
         AQ * (SP - AP)                       SP * (SQ - AQ)
                                                     |
                                   ------------------------------------
                                  |                                    |
                       [Material Mix Variance]              [Material Yield Variance]
                         SP * (RSQ - AQ)                      SP * (SQ - RSQ)
```

**Where:**
- **SQ:** Standard Quantity for ACTUAL output achieved
- **SP:** Standard Price per unit
- **AQ:** Actual Quantity consumed (or purchased, if specified)
- **AP:** Actual Price paid per unit
- **RSQ:** Revised Standard Quantity (AQ in standard mix ratio)
            """.trimIndent(),
            depth3 = """
📝 **ICAI Practical Exam Answer Layout:**

**Working Note 1: Standard Quantity for Actual Output (SQ):**
- Standard raw material per unit of finished product = 10 kg
- Actual finished units produced = 1,000 units
- **SQ = 1,000 units * 10 kg = 10,000 kg**

**Working Note 2: Cost Matrix:**
- Standard Price (SP) = ₹20 per kg
- Actual Quantity (AQ) = 11,500 kg
- Actual Price (AP) = ₹22 per kg

**Statement of Computation of Material Variances:**

1. **Material Cost Variance (MCV):**
   = (SQ * SP) - (AQ * AP)
   = (10,000 kg * ₹20) - (11,500 kg * ₹22)
   = ₹2,00,000 - ₹2,53,000 = **₹53,000 (Adverse / A)**

2. **Material Price Variance (MPV):**
   = Actual Quantity * (Standard Price - Actual Price)
   = 11,500 kg * (₹20 - ₹22) = **₹23,000 (Adverse / A)**

3. **Material Usage Variance (MUV):**
   = Standard Price * (Standard Quantity - Actual Quantity)
   = ₹20 * (10,000 kg - 11,500 kg) = **₹30,000 (Adverse / A)**

**Reconciliation Verification:**
MCV = MPV + MUV
₹53,000 (A) = ₹23,000 (A) + ₹30,000 (A) [VERIFIED]
*(Examiner Note: Always value MUV using Standard Price SP, never Actual Price AP).*
            """.trimIndent()
        ),

        // Topic 5: Auditing SA 505 External Confirmations
        DepthExplanation(
            topicId = "p5_sa500_505",
            topicName = "SA 505: External Confirmations",
            paper = PaperCode.PAPER_5,
            depth1 = """
🔍 **The Report Card Signature Analogy (Simple):**
Imagine your younger brother brings home a school report card and says: "I scored 98% in Maths! Mom and Dad, please sign it."
Would you believe his handwritten note, or would you want to look at the school portal or call his teacher directly?
External confirmation is calling the teacher directly.
If the company accountant says: "XYZ Corp owes us ₹50 Lakhs," the auditor writes directly to XYZ Corp asking: "Do you really owe ₹50 Lakhs?"
If the client's manager panics and says: "Please don't call XYZ Corp, they will get annoyed!", you smell something fishy! That's SA 505.
            """.trimIndent(),
            depth2 = """
🛡️ **SA 505 Audit Procedures Flowchart:**

```
Auditor decides to send External Confirmation
                     |
        Does Management Refuse?
            /                \
        [YES]                [NO]
          |                     |
1. Inquire into reasons.    Send confirmation request directly.
2. Evaluate validity.       Maintain control over process.
3. Assess impact on RMM     
   and fraud risk (SA 240).
4. Perform Alternative 
   Audit Procedures.
          |
Is refusal unreasonable OR cannot obtain SAAE?
          |
Communicate to TCWG -> Evaluate impact on Audit Opinion (SA 705)
```
            """.trimIndent(),
            depth3 = """
🏛️ **ICAI Exam Answer Format (Audit Case Study):**

**1. Standard on Auditing Requirement (SA 505):**
According to **SA 505 'External Confirmations'**, external confirmation procedures frequently serve as relevant and reliable audit evidence. If management refuses to allow the auditor to send a confirmation request, the auditor shall:
(a) **Inquire as to management's reasons** for the refusal, and seek audit evidence as to their validity and reasonableness;
(b) **Evaluate the implications** of management's refusal on the auditor's assessment of the relevant **risks of material misstatement, including the risk of fraud**, and on the nature, timing and extent of other audit procedures; and
(c) **Perform alternative audit procedures** designed to obtain relevant and reliable audit evidence (such as examining subsequent cash receipts, shipping documentation, and sales contracts).

**2. Evaluation of Circumstances & SA 705 Interface:**
- Management's plea of "strained customer relations" is a generalized justification that cannot be accepted at face value without corroborative proof.
- A written representation from management under **SA 580** is supplementary and **cannot substitute for sufficient appropriate audit evidence** when independent verification is thwarted.
- If the auditor concludes that management's refusal is unreasonable, or the auditor is unable to obtain relevant and reliable audit evidence from alternative audit procedures, the auditor shall **communicate with Those Charged With Governance (TCWG)** in accordance with **SA 260**.

**3. Auditor's Reporting Duty:**
The auditor shall determine the implications for the audit and the auditor's opinion in accordance with **SA 705 (Revised)**. If the unconfirmed receivables are material and pervasive, the auditor must issue a **Disclaimer of Opinion**, or a **Qualified Opinion** if material but not pervasive.
            """.trimIndent()
        )
    ) }

    fun getExplanation(topicId: String): DepthExplanation? {
        return explanations.firstOrNull { it.topicId == topicId }
    }
}
