# Hermes Study Vault Release Ads Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a safe, disabled-by-default advertising slot architecture and document the final release checklist.

**Architecture:** A small monetization domain policy decides which placements may show ads. Compose screens receive that policy and render a reusable banner slot only when ads are explicitly enabled. Release readiness remains documented separately so live AdMob integration is not mixed with policy, privacy, and store-listing work.

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, JUnit, Android string resources, Markdown docs.

---

### Task 1: Monetization Policy

**Files:**
- Create: `app/src/test/java/com/hermes/studyvault/domain/monetization/AdPlacementPolicyTest.kt`
- Create: `app/src/main/java/com/hermes/studyvault/domain/monetization/AdPlacement.kt`
- Create: `app/src/main/java/com/hermes/studyvault/domain/monetization/AdPlacementPolicy.kt`

- [ ] **Step 1: Write the failing policy test**

Add tests for disabled-by-default behavior and the non-intrusive placement allowlist:

```kotlin
package com.hermes.studyvault.domain.monetization

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdPlacementPolicyTest {
    @Test
    fun defaultPolicyHidesEveryPlacement() {
        val policy = AdPlacementPolicy()

        AdPlacement.entries.forEach { placement ->
            assertFalse(policy.shouldShow(placement))
        }
    }

    @Test
    fun enabledPolicyAllowsOnlyPassiveContentPlacements() {
        val policy = AdPlacementPolicy(adsEnabled = true)

        assertTrue(policy.shouldShow(AdPlacement.TodayAfterOverview))
        assertTrue(policy.shouldShow(AdPlacement.VaultAfterHeader))
        assertTrue(policy.shouldShow(AdPlacement.ReviewAfterHeader))
        assertFalse(policy.shouldShow(AdPlacement.CalendarScheduleForm))
        assertFalse(policy.shouldShow(AdPlacement.WriteEditor))
        assertFalse(policy.shouldShow(AdPlacement.Settings))
    }
}
```

- [ ] **Step 2: Run the test to verify it fails**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.monetization.AdPlacementPolicyTest"
```

Expected: fail because `AdPlacementPolicy` and `AdPlacement` do not exist.

- [ ] **Step 3: Implement the policy**

Create:

```kotlin
package com.hermes.studyvault.domain.monetization

enum class AdPlacement {
    TodayAfterOverview,
    VaultAfterHeader,
    ReviewAfterHeader,
    CalendarScheduleForm,
    WriteEditor,
    Settings,
}
```

Create:

```kotlin
package com.hermes.studyvault.domain.monetization

class AdPlacementPolicy(
    private val adsEnabled: Boolean = false,
) {
    fun shouldShow(placement: AdPlacement): Boolean {
        return adsEnabled && placement in PassivePlacements
    }

    private companion object {
        val PassivePlacements = setOf(
            AdPlacement.TodayAfterOverview,
            AdPlacement.VaultAfterHeader,
            AdPlacement.ReviewAfterHeader,
        )
    }
}
```

- [ ] **Step 4: Run the test to verify it passes**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests "com.hermes.studyvault.domain.monetization.AdPlacementPolicyTest"
```

Expected: pass.

### Task 2: Compose Ad Slot

**Files:**
- Create: `app/src/main/java/com/hermes/studyvault/ui/ads/AdBannerSlot.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/navigation/VaultNavHost.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/StudyVaultApp.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/ui/today/TodayScreen.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/ui/vault/VaultScreen.kt`
- Modify: `app/src/main/java/com/hermes/studyvault/ui/review/ReviewScreen.kt`
- Modify: `app/src/main/res/values/strings.xml`

- [ ] **Step 1: Add string resources**

Add to `app/src/main/res/values/strings.xml`:

```xml
<string name="ad_sponsored_label">Sponsored</string>
<string name="ad_banner_placeholder">Ad space reserved for release builds.</string>
```

- [ ] **Step 2: Add the reusable slot**

Create:

```kotlin
package com.hermes.studyvault.ui.ads

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hermes.studyvault.R
import com.hermes.studyvault.domain.monetization.AdPlacement
import com.hermes.studyvault.domain.monetization.AdPlacementPolicy

@Composable
fun AdBannerSlot(
    placement: AdPlacement,
    policy: AdPlacementPolicy,
    modifier: Modifier = Modifier,
) {
    if (!policy.shouldShow(placement)) {
        return
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ad_slot_${placement.name}"),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.small,
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                text = stringResource(R.string.ad_sponsored_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.ad_banner_placeholder),
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
```

- [ ] **Step 3: Thread the policy through the app**

Add an `adsEnabled: Boolean = false` parameter to `StudyVaultApp`, create `AdPlacementPolicy(adsEnabled = adsEnabled)`, and pass it to `VaultNavHost`.

Add an `adPolicy: AdPlacementPolicy` parameter to `VaultNavHost`, then pass it to `TodayScreen`, `VaultScreen`, and `ReviewScreen`. Do not pass or render ads in Settings, Write, or Calendar.

- [ ] **Step 4: Render the slot in passive screens**

In `TodayScreen`, after the body copy, call:

```kotlin
AdBannerSlot(
    placement = AdPlacement.TodayAfterOverview,
    policy = adPolicy,
    modifier = Modifier.padding(top = 16.dp),
)
```

In `VaultScreen`, use `AdPlacement.VaultAfterHeader`.

In `ReviewScreen`, use `AdPlacement.ReviewAfterHeader`.

- [ ] **Step 5: Run unit tests**

Run:

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

Expected: pass.

### Task 3: Release Cleanup

**Files:**
- Create: `docs/release/2026-06-20-release-readiness.md`
- Modify: `app/src/androidTest/java/com/hermes/studyvault/settings/LanguageSettingsInstrumentedTest.kt`

- [ ] **Step 1: Fix language instrumentation expectations**

Change the expected Korean text from `오늘 집중` to `오늘 계획`, and English text from `Today Focus` to `Today Plan`.

- [ ] **Step 2: Add release readiness checklist**

Create `docs/release/2026-06-20-release-readiness.md` with sections for build, QA, Play Console, privacy, ads, and launch blockers. Mark connected device testing as blocked until an emulator or device is available.

- [ ] **Step 3: Run verification**

Run:

```powershell
node .\web-preview\check-preview.mjs
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.hermes.studyvault.settings.LanguageSettingsInstrumentedTest"
```

Expected: first three commands pass. The connected test may fail with `No connected devices!` until a device or emulator is connected.
