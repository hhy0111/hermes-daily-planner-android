import { existsSync, readFileSync } from "node:fs";
import { resolve } from "node:path";
import { pathToFileURL } from "node:url";
import assert from "node:assert/strict";

const root = resolve("web-preview");
const html = readFileSync(resolve(root, "index.html"), "utf8");
const css = readFileSync(resolve(root, "styles.css"), "utf8");
const js = readFileSync(resolve(root, "script.js"), "utf8");
const server = readFileSync(resolve(root, "serve-preview.mjs"), "utf8");
const starter = readFileSync(resolve(root, "start-preview.mjs"), "utf8");
const translationsPath = resolve(root, "translations.mjs");
const translationsSource = existsSync(translationsPath) ? readFileSync(translationsPath, "utf8") : "";

const routes = [
  ["today", "Today", "Today Plan"],
  ["calendar", "Calendar", "Calendar"],
  ["vault", "Task List", "Task List"],
  ["write", "Notes", "Notes"],
  ["review", "Done", "Done"],
  ["settings", "Settings", "Settings"],
];
const languageCodes = [
  "system",
  "en",
  "ko",
  "ja",
  "zh-CN",
  "zh-TW",
  "fr",
  "es",
  "de",
  "it",
  "pt",
  "ru",
  "ar",
  "hi",
  "id",
  "vi",
  "th",
  "tr",
];

assert.match(html, /<title>Hermes Daily Planner Preview<\/title>/);
assert.match(html, /href="styles\.css"/);
assert.match(html, /type="module"/);
assert.match(html, /src="script\.js"/);
assert.match(html, /href="\.\.\/app\/build\/outputs\/apk\/debug\/app-debug\.apk"/);
assert.match(html, /data-download-apk/);
assert.match(html, /data-language-select/);
assert.match(html, /data-settings-open/);
assert.match(html, /data-add-source-open/);
assert.match(html, /data-source-form/);
assert.match(html, /data-source-title/);
assert.match(html, /data-source-url/);
assert.match(html, /data-source-field="PDF"/);
assert.match(html, /data-source-file-preview/);
assert.match(html, /data-source-image-file/);
assert.match(html, /data-source-image-preview/);
assert.match(html, /data-source-priority/);
assert.match(html, /data-source-repeat/);
assert.match(html, /data-vault-filter="important"/);
assert.match(html, /data-schedule-count/);
assert.match(html, /data-calendar-grid/);
assert.match(html, /data-schedule-form/);
assert.match(html, /data-schedule-title/);
assert.match(html, /data-schedule-date/);
assert.match(html, /data-schedule-time/);
assert.match(html, /data-schedule-reminder/);
assert.match(html, /data-schedule-kind/);
assert.match(html, /data-i18n="personalSchedule"/);
assert.match(html, /data-i18n="examSchedule"/);
assert.match(html, /data-i18n="assignmentSchedule"/);
assert.match(html, /data-i18n="meetingSchedule"/);
assert.match(html, /data-i18n="otherSchedule"/);
assert.match(html, /data-i18n="routineSchedule"/);
assert.match(html, /data-vault-results/);
assert.match(html, /data-note-form/);
assert.match(html, /data-completed-list/);
assert.match(html, /data-reset-data/);
assert.match(html, /data-i18n-placeholder="sourceTitlePlaceholder"/);
assert.match(html, /data-i18n="localMvp"/);
assert.match(html, /data-i18n="systemDefault"/);
assert.match(html, /data-i18n-aria="mainDestinations"/);
assert.match(html, /data-i18n-initial="today"/);

for (const [route, label, heading] of routes) {
  assert.match(html, new RegExp(`data-route="${route}"`));
  assert.match(html, new RegExp(`>${label}<`));
  assert.match(html, new RegExp(`<section[^>]+id="${route}"`));
  assert.match(html, new RegExp(`>${heading}<`));
}

for (const code of languageCodes) {
  const escapedCode = code.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
    assert.match(html, new RegExp(`option value="${code}"`));
  if (code === "system") {
    assert.match(translationsSource, /system:\s*\{\s*\.{3}en,\s*\.{3}extraEn\s*\}/);
  } else if (code === "en") {
    assert.match(translationsSource, /const\s+en\s*=/);
  } else {
    assert.match(translationsSource, new RegExp(`["']?${escapedCode}["']?\\s*:`));
  }
}

assert.match(css, /\.phone-shell/);
assert.match(css, /\.bottom-nav/);
assert.match(css, /@media \(max-width: 720px\)/);
assert.match(js, /querySelectorAll\("\[data-route\]"\)/);
assert.match(js, /setActiveRoute/);
assert.match(js, /from "\.\/translations\.mjs"/);
assert.match(js, /setLanguage/);
assert.match(js, /data-i18n-initial/);
assert.match(js, /data-i18n-placeholder/);
assert.match(js, /createListItem/);
assert.match(js, /vaultResults\.append/);
assert.match(js, /localStorage\.getItem/);
assert.match(js, /localStorage\.setItem/);
assert.match(js, /stateKey/);
assert.match(js, /renderAll/);
assert.match(js, /moveSourceToVault/);
assert.match(js, /reopenSource/);
assert.match(js, /prepareImageUpload/);
assert.match(js, /mediaForSource/);
assert.match(js, /gradeCurrentReview/);
assert.match(js, /removeSource/);
assert.match(server, /createServer/);
assert.match(server, /application\/vnd\.android\.package-archive/);
assert.match(starter, /spawn\(process\.execPath/);
assert.match(starter, /http:\/\/127\.0\.0\.1:\$\{port\}\/web-preview\//);
assert.equal(existsSync(translationsPath), true);

const { translations, translationOverrides } = await import(pathToFileURL(translationsPath));
const expectedCodes = languageCodes.toSorted();
assert.deepEqual(Object.keys(translations).toSorted(), expectedCodes);

const englishKeys = Object.keys(translations.en).toSorted();
const legacyEnglishKeys = Object.keys(translationOverrides.ko).toSorted();
for (const code of languageCodes) {
  const keys = Object.keys(translations[code]).toSorted();
  assert.deepEqual(keys, englishKeys, `${code} must define every translation key`);
  for (const key of englishKeys) {
    assert.notEqual(translations[code][key], "", `${code}.${key} must not be empty`);
    if (code !== "system" && code !== "en") {
      assert.doesNotMatch(translations[code][key], /\?\?|�/, `${code}.${key} appears to contain mojibake`);
    }
  }
}

for (const code of languageCodes.filter((code) => code !== "system" && code !== "en")) {
  assert.deepEqual(Object.keys(translationOverrides[code]).toSorted(), legacyEnglishKeys);
}

const allowedEnglishReuse = new Set(["appName", "sourceKindPdf", "sourceUrl"]);
for (const code of languageCodes.filter((code) => code !== "system" && code !== "en")) {
  for (const key of legacyEnglishKeys.filter((key) => !allowedEnglishReuse.has(key))) {
    assert.notEqual(translations[code][key], translations.en[key], `${code}.${key} must not reuse English copy`);
  }
}

for (const key of [
  "reviewRetrieval",
  "reviewRetrievalBody",
  "finishExtraction",
  "finishExtractionBody",
  "addSharedSource",
  "sourceImageFile",
  "imageUploadFailed",
  "calendar",
  "calendarTitle",
  "calendarBody",
  "addSchedule",
  "personalSchedule",
  "examSchedule",
  "assignmentSchedule",
  "meetingSchedule",
  "otherSchedule",
  "routineSchedule",
  "todaySchedule",
  "upcomingSchedule",
  "scheduleSaved",
  "workingMemory",
  "workingMemoryBody",
  "lectureQuote",
  "lectureQuoteBody",
  "search",
  "evidenceQuote",
  "evidenceBody",
  "memoryStudy",
  "draftBody",
  "sourceLocation",
  "exportMarkdown",
  "reviewQuestion",
  "again",
  "hard",
  "good",
  "easy",
  "browserReview",
  "previewTitle",
  "previewBody",
  "build",
  "debugReady",
  "scope",
  "phaseOne",
  "taskPriority",
  "priorityHigh",
  "priorityNormal",
  "priorityLow",
  "taskRepeat",
  "repeatDaily",
  "filterImportant",
  "completedDate",
  "originalDate",
  "scheduleTime",
  "scheduleReminder",
  "reminderAtTime",
  "browserReminderHint",
]) {
  assert.notEqual(translations.ko[key], translations.en[key], `ko.${key} must be localized`);
}

console.log("web preview checks passed");
