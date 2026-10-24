# Dynamic Excel Cheat Matrices & Syllabus Quotas Expansion

Comprehensive upgrade to the **Spot the Difference (Excel Cheat Matrices)** engine across all 6 CA Inter papers. Combines curated statutory limits, full markdown tables extracted from official Strategy Blueprints, and syllabus chapter weightages and study quotas into a unified, high-density Excel-style grid with real-time category filtering.

## User Review & Critical Decisions

> [!IMPORTANT]
> Based on your responses, we have aligned on the following implementation strategy:

- **Confirmed Decision 1**: Prioritize **Dynamic Excel Matrices and Syllabus Quotas** as the primary focus for feature enhancement and bug prevention.
- **Confirmed Decision 2**: Build a **Full Combined Matrix** per paper that dynamically aggregates:
  1. Curated statutory limits from `CuratedDifferences.statutoryMatrices`
  2. All parsed markdown tables and section rules from `app/src/main/assets/CA_Inter_*_Strategy.md`
  3. Every syllabus chapter from `CaSyllabusData.getTopicsForPaper(paper)` mapped with past paper frequencies, weightages, and 3-way mastery quotas (Written, MCQ, Oral Recall).
- **Recommended Default**: Display an entries count badge (`Total Entries: X`) alongside dedicated category filter chips (`All Rows`, `Statutory Limits`, `Blueprint Tables`, `Syllabus & Quotas`) for instant 1-tap filtering.

---

## 1. Overview & Core Concept

Currently, the "Excel Cheat Matrices" sub-tab in `SpotDifferenceScreen.kt` displays 5 static rows per paper from `CuratedDifferences`. Students preparing for CA Intermediate require a complete, comprehensive reference sheet covering section numbers, time limits, penalties, formula benchmarks, and revision quotas across all chapters.

This upgrade transforms the static cheat matrix into a **dynamic, multi-source revision matrix** that scales from 5 rows to 40–90+ high-yield exam rows per paper without slowing down the UI thread.

### Key Value
- **Zero-Friction Reference**: CA students can cross-examine limits, threshold caps, and formula parameters in a single Excel-style spreadsheet view.
- **Quota Visibility**: Direct visibility of each chapter's exam priority tier, expected marks weightage, and target written/MCQ/oral quotas.
- **Instant Search & Category Filtering**: Filter rows by Section number, keyword, or source category.

---

## 2. User Experience & Visual Design

### User Flow
1. User taps the **Differences** tab in bottom navigation.
2. User toggles the top sub-mode to **"Excel Cheat Matrices"**.
3. User selects any paper (Paper 1 to Paper 6).
4. The screen displays:
   - Paper selector chips (`Adv. Accounts`, `Law`, `Taxation`, `Costing`, `Audit & Ethics`, `FM & SM`).
   - Category Filter Chips (`All Rows`, `Statutory Limits`, `Blueprint Tables`, `Syllabus & Quotas`).
   - Search bar with live row count badge (`Total Entries: 68`).
   - Sticky header Excel grid (`Section / Clause`, `Provision Name`, `Threshold / Limit`, `Approval / Rate`, `Penalty / Exception`).
   - Clean horizontal and vertical scrolling with zebra-striped row backgrounds, colored threshold values (Sky/Amber), and high-contrast penalty warnings.

### Design System Alignment
- **Palette**: Facebook Light (`#F0F2F5` background, `#FFFFFF` cards) / WhatsApp Dark (`#0B141A` background, `#111B21` cards).
- **Cards & Elevation**: Soft `14dp` corner radius with `1dp` elevation and subtle border outlines.
- **Grid Density**: Clean `150dp` column widths with bold statutory references and soft badge pill chips.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Async Parsing on `Dispatchers.IO` with In-Memory Caching**
  - *Chosen Approach*: Compute `getFullExcelMatrixForPaper(context, paper)` on `Dispatchers.IO` and cache in a thread-safe `ConcurrentHashMap`.
  - *Why*: Parsing 900+ lines of markdown on the main UI thread during tab switching causes frame drops. Background loading with caching guarantees instantaneous tab switching once loaded.
  - *Alternatives Considered*: Hardcoding 300+ Kotlin data classes (rejected due to high code bloat and maintenance overhead).

- **Decision 2: Tagging Row Categories for Fast In-Memory Filtering**
  - *Chosen Approach*: Categorize rows into `Statutory Limits`, `Blueprint Tables`, and `Syllabus & Quotas` based on source origin, allowing the Compose UI to filter instantly with filter chips without re-parsing files.
  - *Why*: Gives users granular control over whether they want to study legal compliance limits or chapter exam quotas.

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        SpotDifferenceScreen.kt                         │
│   (Mode 1: Excel Cheat Matrices Sub-Tab with Horizontal/Vertical Grid) │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ produceState(Dispatchers.IO)
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│                      StrategyBlueprintReader.kt                        │
│             getFullExcelMatrixForPaper(context, paper)                 │
└───────┬───────────────────────────┬────────────────────────────┬───────┘
        │                           │                            │
        ▼                           ▼                            ▼
┌──────────────────┐    ┌───────────────────────────┐    ┌───────────────┐
│CuratedDifferences│    │   Assets Markdown Parser  │    │CaSyllabusData │
│ statutoryMatrices│    │ (CA_Inter_*_Strategy.md)  │    │  (Topics &    │
│ (Curated Limits) │    │  (Table Rows & Sections)  │    │ Exam Quotas)  │
└──────────────────┘    └───────────────────────────┘    └───────────────┘
```

### Core Implementation Steps
1. **`StrategyBlueprintReader.kt`**:
   - Implement `getFullExcelMatrixForPaper(context: Context, paper: PaperCode): StatutoryCheatMatrix`.
   - Read and parse markdown tables from `assets/CA_Inter_*_Strategy.md` safely using `BufferedReader` on `Dispatchers.IO`.
   - Convert `CaSyllabusData.getTopicsForPaper(paper)` into structured `StatutoryMatrixRow` items with exam quotas and pitfalls.
   - Deduplicate rows by `sectionOrClause + provisionName` and cache the final matrix.
2. **`SpotDifferenceScreen.kt`**:
   - Replace static `CuratedDifferences.getMatrixForPaper(activeMatrixPaper)` with asynchronous `produceState` loading.
   - Add Category Filter Chips: `All Rows`, `Statutory Limits`, `Blueprint Tables`, `Syllabus & Quotas`.
   - Add live entry count badge (`"Total Entries: ${filteredRows.size}"`).
   - Enhance table cell styling with clean horizontal scrolling and text truncation safeguards.
3. **Verification**:
   - Verify zero compilation errors with `compile_applet`.
   - Run local JVM unit tests via `gradle :app:testDebugUnitTest`.
