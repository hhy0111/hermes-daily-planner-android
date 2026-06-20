# Release Competitiveness Design

## Goal

Make the Android app valuable enough for a first public release by replacing placeholder planner screens with working local-first planning flows before producing a signed AAB.

## Current Gap

The Play Console setup, ads, privacy policy, screenshots, and store listing are mostly ready, but the native Android app still has several screens that only show descriptive text. A user who installs the app must be able to create tasks, save notes, review schedules, and mark work complete without relying on the web preview.

## Chosen Approach

Implement a focused local-first MVP:

- Today shows live counts and a practical focus list from saved tasks, notes, and schedules.
- Task List creates tasks and lets users mark them done or reopen them.
- Notes creates plain notes and shows saved notes.
- Done shows completed tasks and lets users reopen them.
- Calendar remains the schedule/reminder entry point.

This gives the first release a complete daily planning loop while staying inside the existing Room, Compose, and local-storage architecture.

## Release Bar

The app is competitive enough for first release when:

- A new user can create a task, write a note, add a schedule, and complete a task in the Android app.
- Today reflects saved local data instead of static copy.
- Ads remain limited to non-blocking placements.
- Unit tests pass.
- A release AAB is signed and `jarsigner` verifies it is not unsigned.

