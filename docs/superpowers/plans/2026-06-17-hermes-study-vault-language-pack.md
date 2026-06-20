# Hermes Study Vault Language Pack Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add major-language localization and an in-app Settings language selector to the Android MVP and browser preview.

**Architecture:** Android uses a small domain model for supported languages, DataStore-backed preference storage, Android string resources for translated UI text, and a Compose Settings destination reachable from the app header. The web preview uses the same language list and a client-side dictionary so language switching can be checked in the browser without rebuilding.

**Tech Stack:** Kotlin, Jetpack Compose, Navigation Compose, DataStore Preferences, Android string resources, JUnit/coroutines tests, vanilla HTML/CSS/JavaScript.

---

### Task 1: Language Domain

**Files:**
- Create: `app/src/test/java/com/hermes/studyvault/domain/settings/AppLanguageTest.kt`
- Create: `app/src/main/java/com/hermes/studyvault/domain/settings/AppLanguage.kt`

- [ ] **Step 1: Write failing AppLanguage tests**

Create tests proving the app exposes System Default plus English, Korean, Japanese, Simplified Chinese, Traditional Chinese, French, Spanish, German, Italian, Portuguese, Russian, Arabic, Hindi, Indonesian, Vietnamese, Thai, and Turkish.

- [ ] **Step 2: Run test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.settings.AppLanguageTest"
```

Expected: fail because `AppLanguage` does not exist.

- [ ] **Step 3: Implement AppLanguage**

Create an enum with `code`, `displayName`, and `localeTag`, plus `fromCode(code: String)`.

- [ ] **Step 4: Run test to verify it passes**

Run the same Gradle test command. Expected: pass.

### Task 2: Language Preference Store

**Files:**
- Create: `app/src/test/java/com/hermes/studyvault/data/settings/LanguagePreferenceRepositoryTest.kt`
- Create: `app/src/main/java/com/hermes/studyvault/data/settings/LanguagePreferenceRepository.kt`

- [ ] **Step 1: Write failing repository test**

Create an in-memory DataStore test verifying the repository defaults to System Default and persists a selected language.

- [ ] **Step 2: Run test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.data.settings.LanguagePreferenceRepositoryTest"
```

Expected: fail because repository does not exist.

- [ ] **Step 3: Implement repository**

Use `PreferenceDataStoreFactory`, `stringPreferencesKey`, and `Flow<AppLanguage>`.

- [ ] **Step 4: Run test to verify it passes**

Run the same Gradle command. Expected: pass.

### Task 3: Android Settings UI And Resources

**Files:**
- Create/Modify: `app/src/main/res/values*/strings.xml`
- Modify: `app/src/main/java/com/hermes/studyvault/MainActivity.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/StudyVaultApp.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/navigation/VaultRoute.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/navigation/VaultNavHost.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/ui/components/VaultBottomBar.kt`
- Modify: existing screen composables to use `stringResource`
- Create: `app/src/main/java/com/hermes/studyvault/ui/settings/SettingsScreen.kt`
- Modify: `app/src/androidTest/java/com/hermes/studyvault/navigation/VaultNavigationInstrumentedTest.kt`
- Create: `app/src/androidTest/java/com/hermes/studyvault/settings/LanguageSettingsInstrumentedTest.kt`

- [ ] **Step 1: Write failing UI tests**

Update the navigation test to use resources where needed and add a settings language test that opens Settings, selects Korean, and verifies `Today Focus` changes to Korean.

- [ ] **Step 2: Run UI tests to verify failure**

Run:

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.hermes.studyvault.settings.LanguageSettingsInstrumentedTest
```

Expected: fail because Settings does not exist.

- [ ] **Step 3: Implement resources, locale application, and Settings UI**

Add translated string resource files for all supported languages, apply the selected locale at the app composition level, and add a Settings route with a language list.

- [ ] **Step 4: Run unit and instrumented tests**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:connectedDebugAndroidTest
```

Expected: pass.

### Task 4: Web Preview Language Selector

**Files:**
- Modify: `web-preview/check-preview.mjs`
- Modify: `web-preview/index.html`
- Modify: `web-preview/styles.css`
- Modify: `web-preview/script.js`

- [ ] **Step 1: Extend preview checks first**

Assert the preview includes Settings, a language select, all supported language codes, and translation dictionary behavior.

- [ ] **Step 2: Run check to verify it fails**

Run:

```powershell
node .\web-preview\check-preview.mjs
```

Expected: fail until the preview is updated.

- [ ] **Step 3: Implement web language selector**

Add Settings tab, language dropdown, translation dictionary, and immediate text updates.

- [ ] **Step 4: Run static and browser checks**

Run:

```powershell
node .\web-preview\check-preview.mjs
```

Then use Playwright to load `http://127.0.0.1:4173/web-preview/`, select Korean, and verify Korean UI text appears with zero console errors.
