# Implementation Plan: Light / Dark Mode Setting in Settings Dialog

Implement a persistent theme setting in CA-Lab enabling users to switch between **Light Mode**, **Dark Mode**, and **System Default**, accessible directly inside the Settings Dialog as a quick toggle.

---

## User Review Required

> [!IMPORTANT]
> The theme toggle will be housed in `SettingsDialog.kt` right under the header with a dedicated quick selector (`Light`, `Dark`, `System`). Theme preferences are persisted in `SharedPreferences` (`ca_lab_settings`) and reactive via StateFlow in `CaLabViewModel`, dynamically changing the root `MaterialTheme` in `MainActivity.kt` without requiring app reload.

---

## Proposed Changes

### 1. Data Model & Theme State
#### File: `app/src/main/java/com/example/data/model/ThemeMode.kt` (New)
- Define `enum class ThemeMode { SYSTEM, LIGHT, DARK }` with display labels and icon representations.

---

### 2. ViewModel State & Persistence
#### File: `app/src/main/java/com/example/ui/viewmodel/CaLabViewModel.kt`
- Add `_themeMode` StateFlow initialized from `SharedPreferences ("ca_lab_settings")`, defaulting to `ThemeMode.SYSTEM`.
- Expose `themeMode: StateFlow<ThemeMode>`.
- Add `fun setThemeMode(mode: ThemeMode)` which updates StateFlow and persists the value to SharedPreferences.

---

### 3. Application Root & Theme Binding
#### File: `app/src/main/java/com/example/MainActivity.kt`
- Instantiate `CaLabViewModel` using `androidx.activity.viewModels<CaLabViewModel>()` or Compose `viewModel()`.
- Collect `themeMode` as state.
- Resolve whether dark mode is active:
  ```kotlin
  val isDark = when (themeMode) {
      ThemeMode.LIGHT -> false
      ThemeMode.DARK -> true
      ThemeMode.SYSTEM -> isSystemInDarkTheme()
  }
  ```
- Pass `darkTheme = isDark` to `MyApplicationTheme` and pass `viewModel` to `CaLabApp(viewModel = viewModel)`.

---

### 4. Settings Dialog Quick Toggle UI
#### File: `app/src/main/java/com/example/ui/components/SettingsDialog.kt`
- Add a new "Appearance & Theme" section inside `SettingsDialog`:
  - Quick 3-way toggle row (`Light ☀️`, `Dark 🌙`, `System ⚙️`) using styled `FilterChip` or segmented card buttons.
  - Test tags: `theme_mode_light`, `theme_mode_dark`, `theme_mode_system`.
  - Immediate visual feedback on selection and instant app re-theme.

---

## Verification Plan

### Automated Verification
- Run `compile_applet` to confirm error-free compilation and type safety across all updated files.

### Manual / Visual Verification
1. Open the Settings dialog via the gear icon in the top header.
2. Select **Light**: Verify that the background shifts to `#F8FAFC`, cards to pure white, and text to high-contrast deep slate ink.
3. Select **Dark**: Verify that the entire app instantly transitions to `#0B0F19` background, `#111827` cards, and high-readability light ink.
4. Select **System**: Verify that the app follows system dark/light configuration.
5. Close and reopen the Settings dialog: verify that the selected mode remains persisted.
