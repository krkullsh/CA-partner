# CA-Lab (CA-Partner) — Offline-First ICAI CA Intermediate Study & Feynman Engine

**CA-Lab** (`com.example.calab`) is a native Android study application built with **Kotlin**, **Jetpack Compose (Material 3)**, and **Room SQLite Database (v5)** for **ICAI Chartered Accountancy (CA) Intermediate (New Scheme)** students across all 6 papers.

Built around the **Feynman Learning Technique** (*"You understand a provision or Accounting Standard best when you can teach it"*), CA-Lab combines a **100% offline self-study library, heuristic evaluator, and statutory cheat matrix** with optional **dual-cloud AI support (Google Gemini 2.5 Flash + Groq Llama 3.3 70B)** featuring automatic cross-provider fallback.

---

## Key Highlights (v6 Architecture)

* **Sticky Study Context Bar & Dual Cataloguing**: Select your active Paper and Chapter once at the top of the app—your context stays locked across all 5 tabs. Switch anytime in Settings between **Official ICAI Module & Chapter Chronology** (default, zero memory fog) and **Exam Priority Tier View (Tiers 1–4)**, complete with `[🆕 Updated]` amendment badges.
* **3-Stage Experience Slider (`Novice` | `Knowledged` | `Exam Ready`)**: Adapts explanation depth, Offline Teacher formatting, active-recall masking in Differences, and grading strictness in Teach Lab to match your current revision stage.
* **6 Subject Archetypes**: Customizes presentation mechanics per paper (*Standards & Practical Proformas* for Adv. Accounts; *Statute & 4-Step Case Rubric* for Law; *Amendment & Computation Flow* for Tax; *Formula & Proforma Statements* for CMA/FM; *Audit Procedures & CARO 2020 Clauses* for Audit; *Keyword Frameworks* for SM).
* **100% Offline-First Guarantee**: Includes 12 embedded Markdown asset packs (6 Strategy Blueprints + 6 Subject Question Banks covering Papers 1–6), 89 structured syllabus topics, curated explanations, trick scenarios, differences, and diagnostic MCQs.

---

## The 5 Core Study Tabs

| Tab | Screen File | Workflow & Key Features |
| :--- | :--- | :--- |
| **1. Teach Lab** | `TeachLabScreen.kt` | Explain a provision or AS in your own words. Features a **30-Sec Offline Brush-Up Card**, **Experience-Adaptive Persona Grading**, offline **Provision Hints**, and a 0–100 **Mastery Verdict Card**. |
| **2. Catch Traps** | `CatchLabOutScreen.kt` | Spot 2–3 subtle statutory or accounting traps embedded in realistic CA Inter case scenarios. Includes an offline **Peek Official Provision/Threshold** hint pill and official ICAI correction cards. |
| **3. Study & Ask** | `AskLabScreen.kt` | Unified 3-mode workspace:<br>1. **Offline Teacher**: 2–3 screen scrollable lessons with a **Sticky Reading Outline** and embedded trap cards.<br>2. **3-Tier Notes**: Switch between `1x Analogy`, `2x Revision`, and `3x ICAI Exam Format`.<br>3. **Timed Q-Bank**: Descriptive practice questions for all 6 papers with countdown timer, offline provision hints, and step-wise ICAI rubrics. |
| **4. Differences** | `SpotDifferenceScreen.kt` | • **Sub-Mode A (Basis-First Stacked Cards)**: Mobile-optimized vertical comparison strips with inline **Tap-to-Recall** masking.<br>• **Sub-Mode B (Progressive Excel Cheat Matrix & Swipe Drill)**: Starts with **Column 1 Only** for self-testing (`+ Expand Col 2` / `Full 3-Col Sheet`) and includes an animated **Tinder-Swipe Card Drill** (Swipe Right = *Mastered*, Swipe Left = *Pin for Practice*). |
| **5. My Progress** | `VerdictsHistoryScreen.kt` | Calm, "Anti-Duolingo" **CA Audit Progress Ledger** (zero noisy streaks on study screens). Tracks 4-segment topic completion (*Read / Recalled / Swipe Drill / Written-Oral*), **Pinned "Need Practice" Drill Items**, **Diagnostic 2-Mark MCQs**, **Error Log**, and a **5-Min Low-Energy Revision** launcher. |

---

## Project Structure

```text
app/src/main/
├── assets/                          # 12 Offline Markdown Packs (6 Blueprints + 6 Q-Banks incl. Taxation.md)
├── java/com/example/
│   ├── MainActivity.kt              # App entry point & ViewModel initialization
│   ├── data/
│   │   ├── ai/                      # GeminiApiClient, GroqApiClient (full JSON parity), OfflineLabEngine
│   │   ├── curriculum/              # CaSyllabusData (89 topics), StrategyBlueprintReader, MarkdownQuestionParser, Curated* banks
│   │   ├── local/                   # CaLabDatabase (Room v5 with explicit MIGRATION_4_5)
│   │   ├── model/                   # CaLabModels (PaperCode, ExperienceLevel, CatalogSortOrder, SubjectArchetype, Entities)
│   │   └── repository/              # StudyRepository (Offline-first single source of truth + Dual-AI router)
│   └── ui/
│       ├── CaLabApp.kt              # Root scaffold, Sticky Study Context Bar, & 5-Tab Bottom Navigation
│       ├── components/              # FormattedText, StrategyBlueprintBottomSheet, SettingsDialog
│       ├── screens/                 # TeachLab, CatchLabOut, AskLab, SpotDifference, VerdictsHistory
│       ├── theme/                   # Color, Theme, Type
│       └── viewmodel/               # CaLabViewModel (Global context sync & reactive StateFlows)
```

---

## Zero-PC Build Workflow (GitHub Actions)

1. Push any code or `.md` asset change to the `main` branch.
2. Open the **Actions** tab on GitHub and select **Build Android APK & Upload Artifact** (`.github/workflows/build-apk.yml`).
3. Once the run turns green (~4 minutes), scroll to **Artifacts** at the bottom of the run summary and download **`app-debug-apk.zip`**.
