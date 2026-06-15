# Hermes Study Vault Design Spec

Date: 2026-06-16
Platform: Android phone
Status: Approved product direction

## 1. Product Positioning

Hermes Study Vault is a local-first Android research and study app for students, graduate students, and mobile-first researchers.

The app is inspired by broad productivity patterns such as pages, blocks, links, tags, templates, and flexible views, but it is not a Notion clone. Its product identity is narrower and sharper: help users capture learning material, extract evidence, connect ideas, review knowledge, and export usable writing material from a phone.

The core product bet is:

> Notion is a general workspace. Hermes Study Vault is a mobile research vault.

Initial target users:

- University students managing courses, readings, assignments, exams, and presentations.
- Graduate students collecting articles, PDFs, citations, and research notes.
- Independent learners who read and summarize many web and document sources on Android.

Primary jobs:

- Save a source while reading on a phone.
- Extract a reliable evidence block with source context.
- Connect evidence to a note, deadline, topic, or review card.
- See what to read, review, or finish today.
- Export structured notes without losing source traces.

## 2. Benchmark Findings

### Notion

Notion combines docs, wikis, projects, databases, AI, calendar, mail, and agents into a connected workspace. This breadth is powerful, but it also makes the mobile experience broad rather than research-specific.

Relevant patterns to learn from:

- Flexible pages and blocks.
- Linked content.
- Databases and views.
- Templates.
- Mobile read/edit access.
- Offline access.
- AI-assisted organization.

Notable opportunity:

- Notion's offline mode exists on mobile, but users must manage offline availability per device and database offline access has row limits in the official help material.
- Notion is optimized for general workspace composition, not source-grounded academic evidence workflows.
- Mobile database and page editing can feel heavy for quick capture and research extraction.

### Obsidian

Obsidian is strong in local Markdown, backlinks, graph thinking, plugins, themes, and Android sharing. It is a useful benchmark for data ownership and local-first trust.

Opportunity:

- Obsidian is powerful but can require setup, conventions, and user discipline.
- Hermes should provide a guided student workflow instead of requiring users to build their own system.

### Anytype

Anytype emphasizes offline-first storage, local ownership, native mobile experience, object views, and graph/database-style organization.

Opportunity:

- Anytype is broad and object-oriented.
- Hermes should be narrower: source, evidence, note, deadline, review.

### Zotero

Zotero Android is strong for research literature and PDF annotation workflows.

Opportunity:

- Zotero is primarily a reference manager.
- Hermes should focus on the study loop around collected material: capture, extract, connect, review, and write.

## 3. Legal and Product Safety Boundary

Hermes must not copy Notion's brand, iconography, templates, proprietary layouts, wording, or distinctive visual identity.

Allowed inspiration:

- Common productivity app patterns: documents, blocks, tags, search, filters, templates, backlinks, calendar-like deadlines.
- General Android Material interactions.
- Common academic workflows such as citations, annotations, summaries, flashcards, and reading lists.

Distinct identity:

- Evidence-first data model.
- Research source traceability.
- Android capture-first workflow.
- Local-first study vault.
- Today/Deadline/Review loop tied to learning artifacts.

## 4. MVP Scope

The MVP is an Android native app with no account requirement and no cloud dependency.

Included:

- Bottom tabs: Today, Inbox, Vault, Write, Review.
- Android share target for links, text, images, and files.
- Source library with reading state.
- Evidence blocks linked to source, note, tag, collection, deadline, and review card.
- Simple note editor with evidence insertion.
- Deadlines for assignments, exams, presentations, readings, and paper reviews.
- Review cards with simple spaced repetition scheduling.
- Local search across titles, body text, tags, sources, deadlines, and evidence.
- Markdown and JSON export.
- Local backup file generation.
- Light/dark theme.

Excluded from MVP:

- Team collaboration.
- Cloud sync.
- Google Calendar sync.
- Full Notion-style databases.
- Public web publishing.
- Full PDF internal annotation engine.
- OCR.
- AI summary or AI chat as a core dependency.
- Desktop app.

## 5. Core Concepts

### Source

A Source is original material captured by the user.

Types:

- Web link.
- PDF/file.
- Image.
- Text capture.
- Manual source.

Key fields:

- id
- title
- type
- url
- localFilePath
- capturedText
- status: unread, extracting, noted, archived
- collectionId
- createdAt
- updatedAt

### EvidenceBlock

EvidenceBlock is the product's main differentiator. It represents a source-grounded unit of reusable knowledge.

Examples:

- A quoted sentence from a paper.
- A personal interpretation of a paragraph.
- A diagram screenshot with notes.
- A claim with source URL and page number.

Key fields:

- id
- sourceId
- quoteText
- userThought
- sourceLocation: URL fragment, page number, timestamp, or file location
- tags
- linkedNoteIds
- linkedDeadlineIds
- linkedReviewCardIds
- createdAt
- updatedAt

### Note

A Note is a writing workspace. It should support simple blocks first and evolve into a richer block editor later.

MVP block types:

- Heading.
- Paragraph.
- Checklist item.
- Quote.
- Evidence reference.

Key fields:

- id
- title
- bodyBlocksJson
- collectionId
- tags
- createdAt
- updatedAt

### Deadline

A Deadline is a study-related date, not a generic calendar event.

Types:

- Assignment.
- Exam.
- Presentation.
- Reading.
- Paper review.
- Personal milestone.

Key fields:

- id
- title
- dueAt
- type
- priority
- linkedSourceIds
- linkedNoteIds
- linkedEvidenceBlockIds
- completedAt

### ReviewCard

A ReviewCard is a memory or comprehension card created from evidence or notes.

Key fields:

- id
- front
- back
- sourceEvidenceBlockId
- sourceNoteId
- nextReviewAt
- difficulty
- reviewCount
- lastReviewedAt

### Tag and Collection

Tags are flexible labels. Collections are larger containers such as courses, research projects, exams, or papers.

## 6. App Structure

### Today Tab

Purpose: show what matters now.

Sections:

- Today reviews.
- Today readings.
- Upcoming deadlines.
- Recent captures.
- Quick add actions.

Behavior:

- Opening the app should immediately answer: what should I do next?
- Items are generated from deadlines, review schedules, unread sources, and recent activity.

### Inbox Tab

Purpose: capture first, organize later.

Sections:

- New captures.
- Unread sources.
- Extracting sources.
- Needs metadata.

Behavior:

- Android share intent saves sources with minimal required interaction.
- Users can assign collection, tag, status, and deadline later.

### Vault Tab

Purpose: browse the full local knowledge base.

Views:

- Sources.
- Evidence.
- Notes.
- Collections.
- Tags.

Behavior:

- Mobile-first filtering is preferred over deep page trees.
- Search should be prominent.

### Write Tab

Purpose: compose notes and outputs from evidence.

MVP capabilities:

- Create note.
- Edit title and body blocks.
- Insert evidence block.
- Convert selected text to evidence block.
- Export note to Markdown.

Behavior:

- The editor should remain simple and reliable.
- Evidence references preserve source context.

### Review Tab

Purpose: combine review and study deadlines.

Sections:

- Due review cards.
- Upcoming review cards.
- Deadlines.
- Completed items.

Behavior:

- Users can mark review results: again, hard, good, easy.
- MVP scheduling can use simple intervals instead of a complex spaced repetition algorithm.

## 7. User Flows

### Quick Capture

1. User reads a page, PDF, or text in another app.
2. User taps Android share.
3. User selects Hermes Study Vault.
4. App saves the item as a Source in Inbox.
5. User optionally sets collection, tags, and deadline.

### Evidence Extraction

1. User opens a Source from Inbox or Vault.
2. User selects a passage or adds a manual excerpt.
3. User creates an EvidenceBlock.
4. User adds thought, tag, page/location, and optional deadline/review link.

### Writing From Evidence

1. User opens Write.
2. User creates or opens a Note.
3. User searches evidence by tag, source, or keyword.
4. User inserts evidence references into the note.
5. User exports Markdown with source metadata.

### Study Review

1. User creates a ReviewCard from evidence or note content.
2. ReviewCard appears in Today and Review when due.
3. User grades the review.
4. App schedules the next review date.

### Deadline Planning

1. User creates a Deadline.
2. User links sources, notes, and evidence.
3. Deadline appears in Today as it approaches.
4. User opens the deadline to see all study material tied to it.

## 8. Technical Architecture

Stack:

- Kotlin.
- Jetpack Compose.
- Material 3.
- Room/SQLite.
- DataStore.
- Navigation Compose.
- WorkManager.
- Android app-specific file storage.

Architecture:

- UI Layer: Compose screens and reusable components.
- Domain Layer: use cases for capture, evidence creation, search, export, review scheduling, and deadline aggregation.
- Data Layer: Room DAOs, repositories, DataStore settings, file storage.
- Import/Share Layer: Android share intents and file ingestion.

Local data rule:

- Room is the source of truth for structured data.
- App-specific file storage is the source of truth for imported files.
- DataStore is used only for settings and small preferences.
- UI observes repositories through ViewModels and reactive streams.

Offline-first rule:

- Core reads and writes must work without a network.
- Optional network features must never block capture, search, note editing, review, or export.

## 9. UI/UX Principles

Use Android-native interaction patterns rather than copying web workspace UI.

Principles:

- Bottom navigation for the five main destinations.
- Quick actions for capture, evidence, note, deadline, and review card creation.
- Compact cards for repeated items.
- Clear status labels for sources and deadlines.
- Search-first Vault.
- Evidence metadata should be visible but not noisy.
- Avoid complex tables in MVP because phone screens are narrow.
- Use icons for common actions and short labels for high-frequency commands.
- Keep visual style calm, readable, and study-focused.

Notion-inspired but distinct:

- Use block-style note editing, but emphasize evidence insertion.
- Use flexible organization, but prefer tags/collections over deep nested page trees.
- Use templates later, but make them academic workflow templates, not productivity clones.

## 10. Roadmap

### Phase 1: Local MVP

Goal: reliable Android study vault.

Features:

- Local database.
- Bottom tabs.
- Share capture.
- Source management.
- Evidence blocks.
- Notes.
- Deadlines.
- Review cards.
- Search.
- Markdown/JSON export.

### Phase 2: Research Power Tools

Goal: stronger study and research workflows.

Features:

- PDF viewing and page-linked highlights.
- Better attachment preview.
- Simple citation fields.
- Improved spaced repetition.
- Templates for course notes, article reviews, exam prep, and project reports.
- Android notifications for reviews and deadlines.

### Phase 3: Optional Intelligence

Goal: helpful AI without making AI mandatory.

Features:

- Optional source summary.
- Optional evidence extraction suggestions.
- Optional flashcard generation.
- Optional outline generation from evidence.
- Local-first privacy controls and clear user consent.

### Phase 4: Sync and Ecosystem

Goal: safe multi-device and external workflow support.

Features:

- User-controlled backup restore.
- Optional cloud sync.
- Google Calendar import/export.
- Web clipper or companion extension.
- Desktop companion only after Android value is proven.

## 11. Business Strategy

Positioning:

- "A local-first research notebook for Android students."
- "Capture evidence now. Write with sources later."

Competitive wedge:

- More focused than Notion.
- Easier for students than Obsidian setup.
- Broader study loop than Zotero.
- More mobile-native and research-specific than generic note apps.

Monetization options:

- Free local MVP with paid Pro upgrade.
- Pro features: advanced export, backup automation, PDF annotation, AI credits, custom templates, deeper review scheduling.
- Student pricing should be simple and low-friction.

Initial go-to-market:

- Target university students and graduate students.
- Content angles: research note workflow, assignment evidence tracking, exam review system, Android-first productivity.
- Viral loop: exported Markdown notes and study templates.

## 12. Success Metrics

Product metrics:

- Time from Android share action to saved Source.
- Number of EvidenceBlocks created per active user.
- Percentage of Sources that become EvidenceBlocks.
- Number of ReviewCards completed per week.
- Number of notes exported.
- Seven-day retention for users who captured at least three sources.

Quality metrics:

- Zero data loss in local CRUD flows.
- Search results under a responsive threshold for typical local libraries.
- App usable offline for all MVP core features.
- Export files are readable and complete.

Business signals:

- Students use it for real assignments, not only casual notes.
- Users describe the app as faster than Notion for research capture.
- Users keep sources and evidence linked over multiple weeks.

## 13. Risks and Mitigations

Risk: The app becomes another generic note app.
Mitigation: Keep EvidenceBlock, Today, Review, and Deadline links central.

Risk: MVP scope expands too far.
Mitigation: Delay AI, cloud sync, OCR, and full PDF annotation until core storage and evidence workflow are stable.

Risk: Local-only data can be lost if phone is lost or app is deleted.
Mitigation: Include Markdown/JSON export and local backup in MVP.

Risk: Users expect full Notion database flexibility.
Mitigation: Be explicit that Hermes is research-first and mobile-first, not a general workspace clone.

Risk: AI features create cost, privacy, or academic integrity issues.
Mitigation: Keep AI optional and user-triggered, with transparent source-grounded outputs.

## 14. References

- Notion product overview: https://www.notion.com/product/notion
- Notion mobile help: https://www.notion.com/help/notion-for-mobile
- Notion offline help: https://www.notion.com/help/guides/working-offline-in-notion-everything-you-need-to-know
- Obsidian Android listing: https://play.google.com/store/apps/details?id=md.obsidian
- Anytype product site: https://anytype.io/
- Zotero Android announcement: https://www.zotero.org/blog/zotero-for-android/
- Android offline-first architecture: https://developer.android.com/topic/architecture/data-layer/offline-first
- Android Room documentation: https://developer.android.com/training/data-storage/room
- Android DataStore documentation: https://developer.android.com/topic/libraries/architecture/datastore
- Note-taking app market overview: https://www.thebusinessresearchcompany.com/report/note-taking-app-global-market-report
- Student note-taking app market overview: https://www.wiseguyreports.com/reports/note-taking-app-for-students-market
