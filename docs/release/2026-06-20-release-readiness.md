# Hermes Study Vault Release Readiness

Date: 2026-06-20
Target: Google Play initial release
Current app version: `0.1.0`
Current version code: `1`

## Current Decision

AdMob banner ads are integrated. Debug builds use Google's banner test ad unit. Release builds use the registered Hermes Daily Planner banner ad unit.

## Release Blockers

- Connect an Android device or emulator and run the full instrumented test suite.
- Create a release upload key and store it outside source control.
- Add release signing values to ignored `local.properties`, Gradle properties, or environment variables.
- Generate a signed Android App Bundle (`.aab`) for Play. The current local bundle builds, but is unsigned until signing values are provided.
- Review `docs/release/privacy-policy-draft.md`, replace contact details, host it at a public HTTPS URL, and add the URL to Play Console.
- Complete Google Play Data safety for Google Mobile Ads SDK before submitting a build with ads.
- Complete Play Console Data safety.
- Complete Play Console content rating.
- Complete target audience and ads declaration.
- If using a newly created personal Play developer account, complete the closed testing requirement with at least 12 opted-in testers for 14 continuous days before production access.
- Prepare production app icon, feature graphic, screenshots, descriptions, and release notes.
- Decide final launch countries and whether every listed language should be advertised at launch.

## Code And Build Checklist

- [x] Unit tests run locally.
- [x] Debug APK builds locally.
- [x] Web preview static checks run locally.
- [x] AdMob app ID is registered in the manifest.
- [x] Debug builds use Google's banner test ad unit.
- [x] Release builds use the registered Hermes Daily Planner banner ad unit.
- [x] Release build variant compiles and produces `app-release.aab`.
- [x] `targetSdk = 37`, which is above the current Google Play Android 15/API 35 submission requirement.
- [x] Launcher label uses `@string/app_name` so app name can stay consistent with localized resources.
- [ ] Full connected Android tests pass on a device or emulator.
- [x] Release build type can read signing values from local/private properties.
- [ ] Release upload key exists outside the repository.
- [ ] Signed release Android App Bundle is generated and verified.
- [ ] Release minification and resource shrinking decision is made after testing Room, Compose, notifications, and localization.
- [x] `applicationId` is confirmed final in source: `com.hermes.studyvault`.
- [x] Manifest launcher label now resolves to `@string/app_name`.
- [ ] Store listing name is finalized to match the visible app name.
- [ ] Notification and exact-alarm permissions are documented in the store listing and privacy policy.

## Product QA Checklist

- [ ] First launch opens Today without crashes.
- [ ] Bottom navigation opens Today, Calendar, Task List, Notes, and Done.
- [ ] Settings opens and returns to the current main screen.
- [ ] Language selection persists after app restart.
- [ ] Korean, English, and system default language flows are verified manually.
- [ ] Calendar schedule creation saves to the local database.
- [ ] Reminder permission buttons do not crash on Android 13+ and older versions.
- [ ] Exact alarm settings intent opens or fails silently without crashing.
- [ ] Stored schedules are still visible after app restart.
- [ ] Share intent capture is tested for text, links, images, and PDFs if the launch listing claims share support.
- [ ] Offline mode is tested with airplane mode enabled.
- [ ] RTL layout is checked if Arabic is included in the store listing.

## Ads And Monetization Checklist

- [x] Internal ad placement policy exists.
- [x] Banner ads are limited to Today, Task List, and Done passive content surfaces.
- [x] Ads are blocked from Settings, Write, and Calendar form surfaces.
- [x] Create AdMob app.
- [x] Create production banner ad unit.
- [x] Add Google Mobile Ads SDK dependency.
- [x] Add AdMob app ID manifest metadata.
- [x] Use Google test ad unit ID in debug builds.
- [ ] Add consent flow if personalized ads are served in regions requiring consent.
- [ ] Update Data safety for Google Mobile Ads SDK data collection and sharing.
- [ ] Confirm whether Advertising ID collection is allowed or disabled.
- [ ] Verify ads never cover bottom navigation or primary save actions.

## Play Console Checklist

- [ ] Developer account identity and payment profile are ready.
- [ ] App access instructions are completed if reviewers need special steps.
- [x] Privacy policy draft exists in `docs/release/privacy-policy-draft.md`.
- [ ] Privacy policy is reviewed, contact details are replaced, and the final version is hosted at a public HTTPS URL.
- [ ] Privacy policy URL is added to Play Console.
- [ ] Data safety form is completed.
- [ ] Content rating questionnaire is completed.
- [ ] Target audience questionnaire is completed.
- [ ] Ads declaration is completed.
- [ ] Store category is selected.
- [ ] Contact email is added.
- [ ] Short description is written.
- [ ] Full description is written.
- [ ] App icon is uploaded.
- [ ] Feature graphic is uploaded.
- [ ] Phone screenshots are uploaded.
- [ ] Initial release notes are written.
- [ ] Internal testing release is created before production.
- [ ] Closed or open testing is used if the Play account requires it before production.

## Suggested Store Listing Draft

Short description:

> A local-first planner for tasks, schedules, notes, and study reminders.

Full description:

> Hermes Study Vault helps students and focused learners organize daily tasks, schedules, notes, and completed work in a simple local-first Android app. Capture what matters, plan the day, save reminders, and keep quick notes connected to your workflow.
>
> Initial features:
> - Today view for daily focus
> - Calendar schedule entry with reminders
> - Task list and done list
> - Quick notes
> - Local app language selection
> - Local-first storage with no account required
>
> This first release focuses on reliable personal planning. More study vault features, export improvements, and research workflows will be added after the launch baseline is stable.

Release notes:

> Initial release with Today, Calendar, Task List, Notes, Done, Settings, local language selection, and schedule reminders.

## Verification Commands

Run before release handoff:

```powershell
node .\web-preview\check-preview.mjs
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:bundleRelease
jarsigner -verify -verbose -certs app\build\outputs\bundle\release\app-release.aab
.\gradlew.bat :app:connectedDebugAndroidTest
```

If `connectedDebugAndroidTest` fails with `No connected devices!`, start an emulator or attach a physical device and rerun it.

If `jarsigner` says `jar is unsigned`, add the signing values described in `docs/release/release-signing.md` and rebuild the release bundle.

## Official References

- Android release preparation: https://developer.android.com/studio/publish/preparing
- Google Play release rollout: https://support.google.com/googleplay/android-developer/answer/9859348
- Google Play Data safety: https://support.google.com/googleplay/android-developer/answer/10787469
- Google AdMob Android banner ads: https://developers.google.com/admob/android/banner
- Google Mobile Ads SDK setup: https://developers.google.com/admob/android/quick-start
- Google Mobile Ads SDK data disclosure: https://developers.google.com/ad-manager/mobile-ads-sdk/android/privacy/play-data-disclosure
- Google Play Advertising ID: https://support.google.com/googleplay/android-developer/answer/6048248
