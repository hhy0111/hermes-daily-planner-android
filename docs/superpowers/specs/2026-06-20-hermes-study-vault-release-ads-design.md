# Hermes Study Vault Release Ads Design Spec

Date: 2026-06-20
Platform: Android phone, Google Play release
Status: Approved by user direction to proceed with agent judgment

## 1. Goal

Prepare Hermes Study Vault for release while adding a safe advertising architecture that can show banner or sponsored placements later without blocking study workflows, editing, navigation, reminders, or capture.

## 2. Current Project State

The app is a Kotlin Jetpack Compose Android app with local-first storage, DataStore settings, Room, notifications, multilingual resources, and a browser preview. The current release version is `0.1.0` with `versionCode = 1`.

The project includes Google Mobile Ads SDK banner integration. It does not include Play billing, analytics, a consent flow, hosted privacy policy URL, production release signing key, app icon assets, or final Play Store listing assets.

## 3. Monetization Approach Options

### Option A: Immediate AdMob Integration

Add Google Mobile Ads SDK, manifest app ID, banner ad unit IDs, consent handling, and live ad loading now.

Trade-off: fastest path to real ads, but risky before privacy policy, Play Data safety, consent, and production ad IDs are ready. It also adds Advertising ID and Google Mobile Ads SDK disclosures immediately.

### Option B: Safe Ad Slot Architecture First

Add internal placement policy and reusable Compose ad slot UI, disabled by default. Actual AdMob loading is added only after real AdMob app ID, ad unit IDs, privacy policy, and Play Console disclosures are ready.

Trade-off: does not show revenue ads today, but avoids accidental live-ad testing, release crashes from missing AdMob app IDs, and privacy disclosure drift.

### Option C: Paid Upgrade First, Ads Later

Skip ads for initial release and focus on a paid Pro path.

Trade-off: cleanest UX and privacy story, but slower monetization setup because Play Billing and paid feature gating would be a larger product decision.

## 4. Chosen Approach

Use Option B now.

The app uses a small `domain.monetization` policy and a reusable `ui.ads` banner slot. Debug builds use Google's official banner test ad unit. Release builds use the registered Hermes Daily Planner AdMob banner unit.

## 5. Ad UX Rules

Ads must never interrupt the core use cases:

- No interstitial ads on launch, navigation, save, export, reminder scheduling, language switching, or Android share capture.
- No ads inside the Write editor surface.
- No ads inside Settings.
- No ads between form fields and primary actions.
- No ads that resize while the user is typing or saving.
- Banner slots may appear only after the introductory text or between passive content sections.
- The first ad should be a single banner-like slot per screen, not repeated feed ads.
- If ad loading fails later, the slot collapses without showing an error to users.

## 6. Initial Allowed Placements

Allowed later:

- Today: after the overview text, before generated task sections.
- Vault: after the search or header area, before passive results.
- Review: after the screen description, before completed/review lists.

Blocked:

- Settings.
- Write.
- Calendar schedule form area.
- Any modal or permission request surface.

## 7. Technical Design

Create `AdPlacement` as a small enum describing user-facing placements. Create `AdPlacementPolicy` to decide whether a placement can be shown. The policy has one global enable flag and an allowlist of non-intrusive placements.

Create `AdBannerSlot` as a Compose component backed by Google Mobile Ads `AdView`. It loads a banner only when the placement policy allows ads and a non-blank ad unit ID is supplied.

Official Google AdMob docs require test ads during development. The debug build uses Google's Android fixed-size banner test unit, while the release build uses the production unit supplied from AdMob.

## 8. Privacy And Policy Requirements

Before submitting a build with live ads:

- Confirm the AdMob app ID and production ad unit ID are correct.
- Use only test ad IDs during development.
- Complete Play Console Data safety based on actual SDK and app data practices.
- Publish an active privacy policy URL and link it in Play Console and inside the app if required.
- Decide whether to allow Advertising ID collection or disable it through manifest configuration.
- Add consent handling if serving personalized ads in jurisdictions where consent is required.

## 9. Release Readiness Checklist

### Code And Build

- Fix language instrumentation test expectations to match current string resources.
- Run unit tests.
- Run connected instrumentation tests on a real device or emulator.
- Build debug APK.
- Add release signing configuration outside source control.
- Build signed Android App Bundle for Play.
- Enable release minification and resource shrinking after checking Room, Compose, and notification behavior.
- Confirm `applicationId` is final because it cannot be changed after distribution.
- Confirm app label matches product naming.
- Confirm notification and exact-alarm permissions are justified by visible reminder functionality.

### Store Listing

- Prepare app icon, feature graphic, screenshots, short description, full description, category, tags, contact email, and privacy policy URL.
- Prepare release notes for `0.1.0`.
- Complete content rating.
- Complete target audience and ads declaration.
- Complete Data safety.
- Complete app access instructions if reviewers need them.

### Product QA

- Verify first launch.
- Verify language switching.
- Verify Today, Calendar, Task List, Notes, Done, and Settings navigation.
- Verify schedule creation and notification permission flows.
- Verify local database behavior across app restart.
- Verify backup/export flows if included in the launch claim.
- Verify offline behavior.
- Verify RTL layout for Arabic if Arabic is advertised in the listing.

### Monetization QA

- Keep ad slots disabled in production until real policy prerequisites are complete.
- When AdMob is added, use test ads until final pre-release QA is complete.
- Ensure banner height does not obscure bottom navigation.
- Ensure save/edit flows work the same with ads enabled and disabled.
- Ensure no live ad unit IDs are used in automated tests.

## 10. References

- Google AdMob Android banner docs: https://developers.google.com/admob/android/banner
- Google AdMob Android SDK setup: https://developers.google.com/admob/android/quick-start
- Google Mobile Ads SDK Play data disclosure: https://developers.google.com/ad-manager/mobile-ads-sdk/android/privacy/play-data-disclosure
- Google Play release preparation: https://support.google.com/googleplay/android-developer/answer/9859348
- Android release preparation: https://developer.android.com/studio/publish/preparing
- Google Play Advertising ID policy: https://support.google.com/googleplay/android-developer/answer/6048248
