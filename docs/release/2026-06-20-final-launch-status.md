# Hermes Daily Planner Final Launch Status

Date: 2026-06-20 KST

## Current Release Decision

Hermes Daily Planner is ready for a first Google Play production submission.

This means the project is ready to submit to Google review. It does not mean Google review has already approved the app.

## App Identity

- App name: Hermes Daily Planner
- Package name: `com.hermes.studyvault`
- Version name: `1.0.0`
- Version code: `1`
- GitHub repository: `https://github.com/hhy0111/hermes-daily-planner-android.git`
- Current pushed commit: `4024c89 feat: complete release-ready planner workflows`

## Final AAB For Play Console

Upload this signed AAB to Play Console:

```text
D:\dev\app103\output\release\hermes-daily-planner-v1.0.0-code1-release.aab
```

The previous `app-release.aab` upload shown in Play Console should be removed if it still shows the unsigned bundle error.

Recommended release name:

```text
1.0.0 (1)
```

## Release Notes

Use this in the Play Console release notes field:

```text
<en-US>
Initial release of Hermes Daily Planner.

- Plan daily tasks and schedules
- Write notes connected to tasks
- Track reminders and important items
- Review completed tasks
- Includes a focused, simple daily planning workflow
</en-US>
```

## Implemented Product Improvements

The native Android app is no longer just static screen copy. The first release now includes a complete local-first planner loop:

- Today screen shows live local counts and focus items.
- Task List can create tasks with details.
- Task List can mark tasks done and reopen completed tasks.
- Notes screen can create and list saved notes.
- Done screen shows completed tasks and supports reopening.
- Calendar keeps schedule/reminder creation.
- AdMob banner placements remain non-blocking and are not shown in writing/editing flows.

## Store Listing Assets

Store assets are committed under `image/`:

- `image/play-store-icon-512.png`
- `image/play-store-feature-graphic-1024x500.png`
- `image/store-01-today-plan.png`
- `image/store-02-calendar-schedule.png`
- `image/store-03-task-list.png`
- `image/store-04-notes.png`
- `image/store-05-done-tasks.png`

## Play Console Fields

Short description:

```text
Plan tasks, notes, reviews, and reminders in one focused daily planner.
```

Full description:

```text
Hermes Daily Planner helps you organize your day with tasks, notes, reminders, and review items in one simple planner.

Use it to:
- Create and manage daily tasks
- Write notes and planning items
- Set reminders and review important items
- Keep completed work organized
- Focus on your day without unnecessary complexity

The app is designed for everyday planning, study routines, personal organization, and lightweight task tracking.
```

Privacy policy URL:

```text
https://hhy0111.github.io/hermes-daily-planner-android/release/privacy-policy.html
```

## AdMob Configuration

- AdMob app ID: `ca-app-pub-4402708884038037~2410627211`
- Banner ad unit: `ca-app-pub-4402708884038037/8784463877`
- Debug builds use the official Google test banner ID.
- Release builds use the registered banner ad unit.

## Data Safety Summary

Because Google Mobile Ads SDK is included, Play Console data safety should declare that the app collects or shares data.

Selected categories:

- Approximate location
- App activity: app interactions
- App info and performance: diagnostics
- Device or other IDs

For each selected category:

- Collected: yes
- Shared: yes
- Temporary processing: no
- Required collection: yes
- Purposes: advertising or marketing, analytics, fraud prevention/security/compliance

The app does not require account creation or login.

## Signing Files

Release signing was configured locally. These files are intentionally not committed to GitHub:

- `D:\dev\app103\output\release\hermes-upload-key.jks`
- `D:\dev\app103\output\release\release-signing-secrets.txt`

Do not delete these files. They are needed for future app updates.

## Verification Completed

The following verification commands were run successfully before finalizing this status:

```powershell
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:compileDebugAndroidTestKotlin
.\gradlew.bat :app:bundleRelease
jarsigner -verify -verbose -certs output\release\hermes-daily-planner-v1.0.0-code1-release.aab
```

Observed signing verification:

```text
Signed by "CN=Hermes Daily Planner, OU=Release, O=Hermes Daily Planner, L=Seoul, ST=Seoul, C=KR"
jar verified.
The signer certificate will expire on 2053-11-05.
```

## Remaining External Steps

These steps happen in Play Console and cannot be completed by source code changes:

- Remove the unsigned AAB from the current release draft.
- Upload `hermes-daily-planner-v1.0.0-code1-release.aab`.
- Keep release name as `1.0.0 (1)`.
- Save the release draft.
- Check the app dashboard for any remaining required declarations.
- Submit changes for Google review.

