# Release Competitiveness Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Turn the Android planner from release-prepared into release-worthy by completing the local-first task, note, today, and done workflows, then generate a signed AAB.

**Architecture:** Keep the existing Room database and Compose navigation. Add small pure domain factories for task/note creation so behavior is unit-tested, then wire screens directly to existing DAOs following the current Calendar screen pattern.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, Room, JUnit, Gradle Android plugin, keytool/jarsigner.

---

### Task 1: Tested Planner Item Factories

**Files:**
- Create: `app/src/main/java/com/hermes/studyvault/domain/planner/PlannerItemFactory.kt`
- Test: `app/src/test/java/com/hermes/studyvault/domain/planner/PlannerItemFactoryTest.kt`

- [ ] Write tests that verify task creation trims input, stores details, sets `unread`, and uses the supplied timestamps.
- [ ] Write tests that verify note creation wraps plain text in the existing JSON-ish paragraph format.
- [ ] Run the test and confirm it fails because the factory does not exist.
- [ ] Implement the factory methods.
- [ ] Re-run the targeted test and confirm it passes.

### Task 2: DAO Status Transition

**Files:**
- Modify: `app/src/main/java/com/hermes/studyvault/data/local/dao/SourceDao.kt`

- [ ] Add `updateStatus(id, status, updatedAt)` so Task List and Done can complete/reopen tasks without replacing all fields.
- [ ] Keep schema version unchanged because this only adds a DAO query.

### Task 3: Native Screens With Real Data

**Files:**
- Modify: `app/src/main/java/com/hermes/studyvault/ui/today/TodayScreen.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/ui/vault/VaultScreen.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/ui/write/WriteScreen.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/ui/review/ReviewScreen.kt`
- Modify: `app/src/main/res/values/strings.xml`

- [ ] Today reads Room flows and renders live counts plus top open tasks/upcoming schedules.
- [ ] Task List provides task title/details fields, a save button, open/completed sections, mark-done and reopen actions.
- [ ] Notes provides note title/body fields, a save button, and saved note cards.
- [ ] Done lists completed tasks and supports reopen.
- [ ] Keep banner ads only in previously approved placements.

### Task 4: Signed Release AAB

**Files:**
- Modify ignored local file: `local.properties`
- Create ignored local files under `output/release/`

- [ ] Generate an upload keystore with `keytool` if no release signing values exist.
- [ ] Store generated local signing values in ignored files only.
- [ ] Run `.\gradlew.bat :app:bundleRelease`.
- [ ] Run `jarsigner -verify -verbose -certs app\build\outputs\bundle\release\app-release.aab`.
- [ ] Copy the verified AAB to `output/release/hermes-daily-planner-v1.0.0-code1-release.aab`.

