# 🎓 CA-Lab (CA-Partner) — ICAI CA Intermediate Study & Practice Companion

> *"Knowing isn't understanding—you understand a provision or standard best when you can teach it."*

**CA-Lab** is an offline-first, AI-augmented Android study application built with **Kotlin**, **Jetpack Compose**, and **Room Database** for **ICAI Chartered Accountancy (CA) Intermediate (New Scheme)** students covering **Papers 1 to 6**.

---

## 📌 Important: Developer Handover & Architecture Reference Document

> ⚠️ **Returning to this project after a break or reading the code for the first time?**  
> Before editing the code, read the companion reference manual:  
> 📄 **[`CA_Lab_Architecture_and_Developer_Guide.docx`](./CA_Lab_Architecture_and_Developer_Guide.docx)** *(keep a copy in the root of this repository or in `/docs`)*  
>
> **What is inside `CA_Lab_Architecture_and_Developer_Guide.docx`:**
> 1. **Complete Feature Summary** across all 5 bottom navigation labs and sub-modes.
> 2. **File-by-File Configuration & Directory Map** explaining every `.kt`, `.kts`, `.yml`, and `.md` file in brief.
> 3. **Known Bottlenecks, Restrictions & Guardrails** (Groq vs. Gemini JSON routing, Compose nested scroll rules, Room DB v4 destructive migration notes, Paper 3 Question Bank fallback).
> 4. **First-Timer Onboarding Recipes** for adding new syllabus chapters, updating `.md` Strategy Blueprints, and building APKs directly from a phone via GitHub Actions.

---

## ✨ Key Features (5 Core Study Labs)

| Lab / Tab | Core Capabilities | Offline vs. Cloud AI Support |
| :--- | :--- | :--- |
| **1. 🗣️ Teach Lab** *(Feynman Mode)* | Explain any CA Inter provision or Accounting Standard in your own words to a virtual junior article assistant. Receive probing follow-up doubts on thresholds, provisos, and exceptions, plus a **0–100 Mastery Verdict Card**. | **100% Offline** heuristic engine + **Cloud AI** via **Gemini** or **Groq (Llama 3.3 70B)**. |
| **2. 🕵️ Catch Lab Out** *(Trap Spotting)* | Read realistic case scenarios containing 2–3 subtle statutory traps or wrong threshold limits. Spot the traps and compare against official ICAI provisions. | Pre-loaded curated scenarios offline + **AI Trap Scenario Generator** online. |
| **3. 📖 Ask Lab** *(3-Tier Depth + Blueprints + Q-Bank)* | • **3-Tier Explanations:** Depth 1 *(Kirana/Chai Analogy)*, Depth 2 *(Revision Notes)*, Depth 3 *(ICAI 3-Step Exam Answer Format)*.<br>• **Strategy Blueprints:** Full paper exam-weightage blueprints.<br>• **Timed Practice Q-Bank:** Past exam/RTP/MTP questions with timer & step-wise marking rubrics. | Reads embedded `.md` blueprints & question banks offline + **Gemini / Groq** custom query support online. |
| **4. ⚖️ Differences** *(Cards + Excel Cheat Sheet)* | • **Concept Comparison Cards:** 30+ side-by-side comparisons with **Self-Quiz Mode** (hide/reveal columns).<br>• **Dynamic Excel Cheat Matrices:** Full scrollable spreadsheet view combining all `.md` Blueprint tables, all 89 syllabus chapters, and statutory limits. | Dynamically merges `.md` assets + `CaSyllabusData` + `CuratedDifferences` offline + **AI Custom Comparison** online. |
| **5. 📊 Gap Analysis** *(MCQ Test + Weakness Radar + Errors)* | • **Diagnostic 2-Mark MCQ Test:** Case-based MCQs with instant grading.<br>• **Weakness Radar ("Revise First"):** Ranks all 89 topics by Priority Tiers (Tiers 1–4) and tracks Written, MCQ, and Oral quotas.<br>• **Running Error Log:** Automatically logs wrong MCQ attempts with ICAI provision references. | Persisted locally in **Room Database (v4)** + **AI MCQ Generator** online. |

---

## 📚 Syllabus Coverage (ICAI CA Intermediate — New Scheme)

- **Paper 1:** Advanced Accounting *(All AS 1–29 + Company Accounts, Schedule III, Buyback Sec 68, Amalgamation, Consolidation, Branch)*
- **Paper 2:** Corporate and Other Laws *(Companies Act 2013 Sec 1–148, Foreign Companies, LLP Act, General Clauses Act, Interpretation, FEMA 1999)*
- **Paper 3:** Taxation *(Direct Tax / Income Tax Act 1961 + Indirect Tax / CGST & IGST Act 2017)*
- **Paper 4:** Cost and Management Accounting *(Cost Sheet, Material, Labour, Overheads, ABC, Process, Standard Costing Variances, Marginal, Budgets)*
- **Paper 5:** Auditing and Ethics *(All SAs 200–720, SQC 1, CARO 2020 Clauses i–xxi, Bank Audit, Ethics)*
- **Paper 6:** Financial and Strategic Management *(Part I FM Ratios, Capital Budgeting, WACC, Working Capital + Part II SM Chapters 1–5)*

---

## 🗂️ Repository File Structure (Quick Map)

```text
CA-partner/
├── .github/workflows/
│   └── build-apk.yml                        # CI/CD: JDK 17 + Dummy Keystore + Gradle 9.3.1 + APK Artifact upload
├── CA_Lab_Architecture_and_Developer_Guide.docx  # 📖 Complete Developer Reference & Bottleneck Guide
├── README.md                                # Project overview & quick start
├── build.gradle.kts & settings.gradle.kts   # Root Gradle configuration
├── gradle/libs.versions.toml                # Dependency version catalog
└── app/
    ├── build.gradle.kts                     # App dependencies, Room KSP, BuildConfig keys
    └── src/main/
        ├── AndroidManifest.xml              # App permissions & MainActivity registration
        ├── assets/                          # Offline Markdown Strategy Blueprints (6) & Question Banks (5)
        └── java/com/example/
            ├── MainActivity.kt              # Compose entry point
            ├── data/
            │   ├── ai/                      # GeminiApiClient.kt, GroqApiClient.kt, OfflineLabEngine.kt
            │   ├── curriculum/              # CaSyllabusData.kt, StrategyBlueprintReader.kt, MarkdownQuestionParser.kt, Curated*.kt
            │   ├── local/                   # CaLabDatabase.kt (Room DB v4: Verdicts, Bookmarks, Progress, Errors, CustomDiffs)
            │   ├── model/                   # CaLabModels.kt (All enums & data classes)
            │   └── repository/              # StudyRepository.kt
            └── ui/
                ├── CaLabApp.kt              # Scaffold, TopBar & 5-Tab Bottom Navigation
                ├── components/              # FormattedText.kt, SettingsDialog.kt, StrategyBlueprintBottomSheet.kt
                ├── screens/                 # TeachLab, CatchLabOut, AskLab, SpotDifference, VerdictsHistory
                ├── theme/                   # WhatsApp/FB-inspired Color.kt, Theme.kt, Type.kt
                └── viewmodel/               # CaLabViewModel.kt (StateFlows, AI Provider Routing, SharedPreferences)
```

---

## 🚀 How to Build & Download the APK

### Option 1: Directly via GitHub Actions (No PC Required — Phone Friendly)
1. Push or commit any change to the `main` branch (or go to the **Actions** tab and click **Run workflow**).
2. Wait ~3 minutes for the **Build Android APK & Upload Artifact** workflow to finish with a green checkmark ✅.
3. Open the completed workflow run, scroll to the bottom of the **Summary** page under **Artifacts**, and click **`app-debug-apk`** to download the compiled `.apk` inside a `.zip`.

### Option 2: Local Build via Android Studio
1. Clone this repository and open it in **Android Studio (Ladybug or newer)** with **JDK 17**.
2. *(Optional)* Add `GEMINI_API_KEY=your_key_here` to `local.properties` or configure your **Gemini** / **Groq (`gsk_...`)** API key directly inside the in-app **Settings Dialog** ⚙️.
3. Run `./gradlew assembleDebug` or click **Run 'app'**.

---

## ⚠️ Known Restrictions & Upgrade Notes

For a deep dive into current architectural constraints—including why complex JSON generation tasks default to Gemini while chat/explanations support both **Groq (Llama 3.3 70B)** and **Gemini**, Compose nested-scroll rules in `FormattedText.kt`, and Room DB v4 migration behavior—please refer to **Section 4 of [`CA_Lab_Architecture_and_Developer_Guide.docx`](./CA_Lab_Architecture_and_Developer_Guide.docx)**.
