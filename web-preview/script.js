import { translations } from "./translations.mjs";

const routeButtons = document.querySelectorAll("[data-route]");
const panels = document.querySelectorAll("[data-panel]");
const languageSelect = document.querySelector("[data-language-select]");
const addSourceOpen = document.querySelector("[data-add-source-open]");
const sourceForm = document.querySelector("[data-source-form]");
const sourceType = document.querySelector("[data-source-type]");
const sourceFields = document.querySelectorAll("[data-source-field]");
const sourceTitle = document.querySelector("[data-source-title]");
const sourceNote = document.querySelector("[data-source-note]");
const sourceUrl = document.querySelector("[data-source-url]");
const sourceFile = document.querySelector("[data-source-file]");
const sourceFilePreview = document.querySelector("[data-source-file-preview]");
const sourcePage = document.querySelector("[data-source-page]");
const sourceText = document.querySelector("[data-source-text]");
const sourceImage = document.querySelector("[data-source-image]");
const sourceImageFile = document.querySelector("[data-source-image-file]");
const sourceImagePreview = document.querySelector("[data-source-image-preview]");
const sourceDueDate = document.querySelector("[data-source-due-date]");
const sourceTags = document.querySelector("[data-source-tags]");
const sourcePriority = document.querySelector("[data-source-priority]");
const sourceRepeat = document.querySelector("[data-source-repeat]");
const sourceCancel = document.querySelector("[data-source-cancel]");
const sourceFeedback = document.querySelector("[data-source-feedback]");
const capturedCount = document.querySelector("[data-captured-count]");
const reviewCount = document.querySelector("[data-review-count]");
const scheduleCount = document.querySelector("[data-schedule-count]");
const todayList = document.querySelector("[data-today-list]");
const addScheduleOpen = document.querySelector("[data-add-schedule-open]");
const scheduleForm = document.querySelector("[data-schedule-form]");
const scheduleTitle = document.querySelector("[data-schedule-title]");
const scheduleDate = document.querySelector("[data-schedule-date]");
const scheduleTime = document.querySelector("[data-schedule-time]");
const scheduleReminder = document.querySelector("[data-schedule-reminder]");
const scheduleKind = document.querySelector("[data-schedule-kind]");
const scheduleNote = document.querySelector("[data-schedule-note]");
const scheduleCancel = document.querySelector("[data-schedule-cancel]");
const scheduleFeedback = document.querySelector("[data-schedule-feedback]");
const scheduleList = document.querySelector("[data-schedule-list]");
const calendarMonth = document.querySelector("[data-calendar-month]");
const calendarGrid = document.querySelector("[data-calendar-grid]");
const calendarWeekdays = document.querySelector("[data-calendar-weekdays]");
const calendarPrev = document.querySelector("[data-calendar-prev]");
const calendarNext = document.querySelector("[data-calendar-next]");
const vaultSearch = document.querySelector("[data-vault-search]");
const vaultResults = document.querySelector("[data-vault-results]");
const vaultFilterButtons = document.querySelectorAll("[data-vault-filter]");
const noteForm = document.querySelector("[data-note-form]");
const noteTitle = document.querySelector("[data-note-title]");
const noteBody = document.querySelector("[data-note-body]");
const noteSource = document.querySelector("[data-note-source]");
const noteFeedback = document.querySelector("[data-note-feedback]");
const noteList = document.querySelector("[data-note-list]");
const exportMarkdown = document.querySelector("[data-export-markdown]");
const completedList = document.querySelector("[data-completed-list]");
const reviewFeedback = document.querySelector("[data-review-feedback]");
const gradeButtons = document.querySelectorAll(".grade-row button");
const resetData = document.querySelector("[data-reset-data]");
const showGuide = document.querySelector("[data-show-guide]");
const settingsFeedback = document.querySelector("[data-settings-feedback]");
const onboarding = document.querySelector("[data-onboarding]");
const onboardingStart = document.querySelector("[data-onboarding-start]");
const onboardingDismiss = document.querySelector("[data-onboarding-dismiss]");

const stateKey = "hermes-study-vault-preview-state-v2";
const legacySourcesKey = "hermes-study-vault-preview-sources";
const languageKey = "hermes-study-vault-preview-language";
const onboardingKey = "hermes-study-vault-preview-onboarding-seen-v1";

let currentLanguage = localStorage.getItem(languageKey) ?? "en";
let appState = loadState();
let selectedNoteSourceId = "";
let selectedScheduleDate = todayYmd();
let calendarCursor = monthStart(new Date());
let pendingImageUpload = null;
let pendingFileUpload = null;
let currentVaultFilter = "all";
let browserReminderTimers = new Map();

function defaultState() {
  return {
    sources: [],
    notes: [],
    reviews: [],
    events: [],
  };
}

function loadState() {
  try {
    const saved = JSON.parse(localStorage.getItem(stateKey) ?? "null");
    if (saved && Array.isArray(saved.sources) && Array.isArray(saved.notes) && Array.isArray(saved.reviews)) {
      return {
        ...defaultState(),
        ...saved,
        events: Array.isArray(saved.events) ? saved.events : [],
      };
    }
  } catch {
    // Ignore broken preview storage and rebuild below.
  }

  const migrated = defaultState();
  try {
    const legacySources = JSON.parse(localStorage.getItem(legacySourcesKey) ?? "[]");
    if (Array.isArray(legacySources)) {
      migrated.sources = legacySources
        .filter((source) => source && source.type && source.title)
        .map((source) => ({
          id: source.id ?? createId(),
          type: source.type,
          title: source.title,
          note: source.note ?? "",
          meta: source.meta ?? {},
          inVault: false,
          createdAt: source.createdAt ?? Date.now(),
        }));
    }
  } catch {
    // Legacy migration is best effort.
  }
  persistState(migrated);
  return migrated;
}

function persistState(nextState = appState) {
  try {
    localStorage.setItem(stateKey, JSON.stringify(nextState));
  } catch {
    // The preview still works for the current session if browser storage is unavailable.
  }
}

function createId() {
  return crypto.randomUUID?.() ?? `${Date.now()}-${Math.random().toString(16).slice(2)}`;
}

function activeLocale() {
  return currentLanguage === "system" ? "en" : currentLanguage;
}

function todayYmd() {
  return toYmd(new Date());
}

function toYmd(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

function dateFromYmd(value) {
  const [year, month, day] = value.split("-").map(Number);
  return new Date(year, month - 1, day);
}

function monthStart(date) {
  return new Date(date.getFullYear(), date.getMonth(), 1);
}

function addMonths(date, count) {
  return new Date(date.getFullYear(), date.getMonth() + count, 1);
}

function formatDate(value, options = { dateStyle: "medium" }) {
  return new Intl.DateTimeFormat(activeLocale(), options).format(dateFromYmd(value));
}

function formatTimestamp(value) {
  return new Intl.DateTimeFormat(activeLocale(), { dateStyle: "medium", timeStyle: "short" }).format(new Date(value));
}

function formatEventDateTime(event) {
  if (!event.time) {
    return formatDate(event.date, { weekday: "long", month: "short", day: "numeric" });
  }
  const [hour, minute] = event.time.split(":").map(Number);
  const date = dateFromYmd(event.date);
  date.setHours(hour, minute, 0, 0);
  return new Intl.DateTimeFormat(activeLocale(), {
    weekday: "long",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(date);
}

function translationFor(languageCode, key) {
  const language = languageCode === "system" ? "en" : languageCode;
  return translations[language]?.[key] ?? translations.en[key] ?? "";
}

function textFor(item, field, fallback = "") {
  const key = item[`${field}Key`];
  if (key) {
    return translationFor(currentLanguage, key);
  }
  return item[field] ?? fallback;
}

function sourceKindKey(type) {
  if (type === "TASK" || type === "TXT") {
    return "sourceKindText";
  }
  if (type === "LINK") {
    return "sourceKindLink";
  }
  if (type === "IMG") {
    return "sourceKindImage";
  }
  return "sourceKindPdf";
}

function sourceTypeLabel(type) {
  return translationFor(currentLanguage, sourceKindKey(type));
}

function scheduleKindLabel(kind) {
  if (kind === "personal") {
    return translationFor(currentLanguage, "personalSchedule");
  }
  if (kind === "work") {
    return translationFor(currentLanguage, "studySession");
  }
  if (kind === "exam") {
    return translationFor(currentLanguage, "examSchedule");
  }
  if (kind === "assignment" || kind === "errand") {
    return translationFor(currentLanguage, "assignmentSchedule");
  }
  if (kind === "deadline") {
    return translationFor(currentLanguage, "deadlines");
  }
  if (kind === "routine") {
    return translationFor(currentLanguage, "routineSchedule");
  }
  if (kind === "meeting") {
    return translationFor(currentLanguage, "meetingSchedule");
  }
  if (kind === "other") {
    return translationFor(currentLanguage, "otherSchedule");
  }
  if (kind === "study") {
    return translationFor(currentLanguage, "studySession");
  }
  return kind || translationFor(currentLanguage, "studySession");
}

function chipClassFor(type) {
  if (type === "TASK" || type === "TXT") {
    return "file-chip text";
  }
  if (type === "LINK") {
    return "file-chip link";
  }
  if (type === "IMG") {
    return "file-chip image";
  }
  return "file-chip";
}

function priorityLabel(priority) {
  if (priority === "high") {
    return translationFor(currentLanguage, "priorityHigh");
  }
  if (priority === "low") {
    return translationFor(currentLanguage, "priorityLow");
  }
  return translationFor(currentLanguage, "priorityNormal");
}

function repeatLabel(repeat) {
  if (repeat === "daily") {
    return translationFor(currentLanguage, "repeatDaily");
  }
  if (repeat === "weekly") {
    return translationFor(currentLanguage, "repeatWeekly");
  }
  if (repeat === "monthly") {
    return translationFor(currentLanguage, "repeatMonthly");
  }
  return translationFor(currentLanguage, "repeatNone");
}

function chipClassForSchedule(kind) {
  if (kind === "deadline" || kind === "exam" || kind === "assignment") {
    return "file-chip";
  }
  if (kind === "routine" || kind === "meeting") {
    return "file-chip text";
  }
  if (kind === "personal" || kind === "other") {
    return "file-chip image";
  }
  return "file-chip link";
}

function allSources() {
  return [...appState.sources];
}

function findSourceById(id) {
  return allSources().find((source) => source.id === id);
}

function setActiveRoute(route) {
  for (const panel of panels) {
    const active = panel.dataset.panel === route;
    panel.hidden = !active;
    panel.classList.toggle("active", active);
  }

  for (const button of routeButtons) {
    const active = button.dataset.route === route;
    button.classList.toggle("active", active);
    if (active) {
      button.setAttribute("aria-current", "page");
    } else {
      button.removeAttribute("aria-current");
    }
  }
}

function setLanguage(languageCode) {
  currentLanguage = languageCode;
  localStorage.setItem(languageKey, languageCode);
  document.documentElement.lang = languageCode === "system" ? "en" : languageCode;

  for (const element of document.querySelectorAll("[data-i18n]")) {
    element.textContent = translationFor(languageCode, element.dataset.i18n);
  }

  for (const element of document.querySelectorAll("[data-i18n-aria]")) {
    element.setAttribute("aria-label", translationFor(languageCode, element.dataset.i18nAria));
  }

  for (const element of document.querySelectorAll("[data-i18n-value]")) {
    element.value = translationFor(languageCode, element.dataset.i18nValue);
  }

  for (const element of document.querySelectorAll("[data-i18n-placeholder]")) {
    element.placeholder = translationFor(languageCode, element.dataset.i18nPlaceholder);
  }

  for (const element of document.querySelectorAll("[data-i18n-initial]")) {
    const label = translationFor(languageCode, element.dataset.i18nInitial).trim();
    const initial = Array.from(label)[0] ?? "";
    element.textContent = initial.toLocaleUpperCase(document.documentElement.lang);
  }

  if (languageSelect.value !== languageCode) {
    languageSelect.value = languageCode;
  }
  sourceFeedback.textContent = "";
  scheduleFeedback.textContent = "";
  noteFeedback.textContent = "";
  reviewFeedback.textContent = "";
  settingsFeedback.textContent = "";
  renderAll();
}

function updateSourceFields() {
  for (const field of sourceFields) {
    field.hidden = field.dataset.sourceField !== sourceType.value;
  }
  sourceUrl.required = sourceType.value === "LINK";
  sourceFile.required = sourceType.value === "PDF";
  sourceImageFile.required = sourceType.value === "IMG";
  if (sourceType.value !== "PDF") {
    clearFileUpload();
  }
  if (sourceType.value !== "IMG") {
    clearImageUpload();
  }
}

function openSourceForm() {
  sourceForm.hidden = false;
  sourceFeedback.textContent = "";
  sourceFeedback.dataset.sourceFeedback = "";
  sourceDueDate.value ||= todayYmd();
  updateSourceFields();
  sourceTitle.focus();
}

function closeSourceForm() {
  sourceForm.reset();
  sourceForm.hidden = true;
  sourceType.value = "TASK";
  sourceDueDate.value = todayYmd();
  clearImageUpload();
  clearFileUpload();
  updateSourceFields();
}

function clearFileUpload(clearInput = true) {
  pendingFileUpload = null;
  if (clearInput) {
    sourceFile.value = "";
  }
  sourceFilePreview.hidden = true;
  sourceFilePreview.innerHTML = "";
}

function clearImageUpload(clearInput = true) {
  pendingImageUpload = null;
  if (clearInput) {
    sourceImageFile.value = "";
  }
  sourceImagePreview.hidden = true;
  sourceImagePreview.innerHTML = "";
}

function renderUploadPreview(container, upload, onRemove) {
  container.innerHTML = "";
  if (!upload) {
    container.hidden = true;
    return;
  }
  const caption = document.createElement("span");
  caption.textContent = `${upload.name} (${Math.ceil(upload.size / 1024)} KB)`;
  const remove = document.createElement("button");
  remove.type = "button";
  remove.className = "attachment-remove";
  remove.textContent = "\u00d7";
  remove.setAttribute("aria-label", translationFor(currentLanguage, "removeAttachment"));
  remove.addEventListener("click", onRemove);
  container.append(caption, remove);
  container.hidden = false;
}

function renderImagePreview(upload) {
  sourceImagePreview.innerHTML = "";
  if (!upload?.dataUrl) {
    sourceImagePreview.hidden = true;
    return;
  }
  const image = document.createElement("img");
  image.src = upload.dataUrl;
  image.alt = upload.name;
  const caption = document.createElement("span");
  caption.textContent = `${upload.name} (${Math.ceil(upload.size / 1024)} KB)`;
  const remove = document.createElement("button");
  remove.type = "button";
  remove.className = "attachment-remove";
  remove.textContent = "\u00d7";
  remove.setAttribute("aria-label", translationFor(currentLanguage, "removeAttachment"));
  remove.addEventListener("click", () => clearImageUpload());
  sourceImagePreview.append(image, caption, remove);
  sourceImagePreview.hidden = false;
}

function prepareFileUpload(file) {
  return {
    name: file.name,
    type: file.type || "application/octet-stream",
    size: file.size,
  };
}

function readFileAsDataUrl(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.addEventListener("load", () => resolve(reader.result));
    reader.addEventListener("error", () => reject(reader.error));
    reader.readAsDataURL(file);
  });
}

function loadImage(dataUrl) {
  return new Promise((resolve, reject) => {
    const image = new Image();
    image.addEventListener("load", () => resolve(image));
    image.addEventListener("error", reject);
    image.src = dataUrl;
  });
}

async function prepareImageUpload(file) {
  const originalDataUrl = await readFileAsDataUrl(file);
  try {
    const image = await loadImage(originalDataUrl);
    const maxEdge = 960;
    const scale = Math.min(1, maxEdge / Math.max(image.naturalWidth, image.naturalHeight));
    const width = Math.max(1, Math.round(image.naturalWidth * scale));
    const height = Math.max(1, Math.round(image.naturalHeight * scale));
    const canvas = document.createElement("canvas");
    canvas.width = width;
    canvas.height = height;
    const context = canvas.getContext("2d");
    context.drawImage(image, 0, 0, width, height);
    const dataUrl = canvas.toDataURL("image/jpeg", 0.82);
    return {
      name: file.name,
      type: "image/jpeg",
      size: Math.round((dataUrl.length * 3) / 4),
      dataUrl,
    };
  } catch {
    return {
      name: file.name,
      type: file.type,
      size: file.size,
      dataUrl: originalDataUrl,
    };
  }
}

function openScheduleForm() {
  scheduleForm.hidden = false;
  scheduleFeedback.textContent = "";
  scheduleDate.value = selectedScheduleDate;
  scheduleTitle.focus();
}

function closeScheduleForm() {
  scheduleForm.reset();
  scheduleForm.hidden = true;
  scheduleDate.value = selectedScheduleDate;
  scheduleReminder.value = "none";
}

function showOnboarding() {
  onboarding.hidden = false;
  document.body.classList.add("modal-open");
  onboardingStart.focus();
}

function hideOnboarding() {
  onboarding.hidden = true;
  document.body.classList.remove("modal-open");
  localStorage.setItem(onboardingKey, "true");
}

function startFromOnboarding() {
  hideOnboarding();
  setActiveRoute("vault");
  openSourceForm();
}

async function buildSourceMeta(type) {
  const meta = {
    dueDate: sourceDueDate.value,
    category: sourceTags.value,
    tags: sourceTags.value,
    priority: sourcePriority.value,
    repeat: sourceRepeat.value,
    attachmentType: type,
  };
  if (type === "LINK") {
    meta.url = sourceUrl.value.trim();
  }
  if (type === "PDF") {
    const file = sourceFile.files?.[0];
    if (file && (!pendingFileUpload || pendingFileUpload.name !== file.name)) {
      pendingFileUpload = prepareFileUpload(file);
      renderUploadPreview(sourceFilePreview, pendingFileUpload, () => clearFileUpload());
    }
    if (pendingFileUpload) {
      meta.file = pendingFileUpload.name;
      meta.fileName = pendingFileUpload.name;
      meta.fileType = pendingFileUpload.type;
      meta.fileSize = pendingFileUpload.size;
    }
    meta.page = sourcePage.value.trim();
  }
  if (type === "TXT" && sourceText) {
    meta.text = sourceText.value.trim();
  }
  if (type === "IMG") {
    const file = sourceImageFile.files?.[0];
    if (file && (!pendingImageUpload || pendingImageUpload.name !== file.name)) {
      pendingImageUpload = await prepareImageUpload(file);
      renderImagePreview(pendingImageUpload);
    }
    meta.imageDescription = sourceImage.value.trim();
    if (pendingImageUpload) {
      meta.imageName = pendingImageUpload.name;
      meta.imageType = pendingImageUpload.type;
      meta.imageSize = pendingImageUpload.size;
      meta.imageDataUrl = pendingImageUpload.dataUrl;
    }
  }
  return meta;
}

function detailLines(source, options = {}) {
  const meta = source.meta ?? {};
  const includeDate = options.includeDate !== false;
  const dueDate =
    includeDate && meta.dueDate ? `${translationFor(currentLanguage, "scheduleDate")}: ${formatDate(meta.dueDate)}` : "";
  const category = meta.category || meta.tags;
  const categoryLine = category ? `${translationFor(currentLanguage, "sourceTags")}: ${scheduleKindLabel(category)}` : "";
  const priorityLine = meta.priority ? `${translationFor(currentLanguage, "taskPriority")}: ${priorityLabel(meta.priority)}` : "";
  const repeatLine =
    meta.repeat && meta.repeat !== "none"
      ? `${translationFor(currentLanguage, "taskRepeat")}: ${repeatLabel(meta.repeat)}`
      : "";
  const fileLine = meta.fileName || meta.file;
  return [
    dueDate,
    categoryLine,
    priorityLine,
    repeatLine,
    meta.url,
    fileLine,
    meta.page,
    meta.text,
    meta.imageDescription,
    meta.imageName,
  ]
    .filter(Boolean)
    .join(" \u00b7 ");
}

function completedLines(source) {
  const meta = source.meta ?? {};
  const completed = source.completedAt
    ? `${translationFor(currentLanguage, "completedDate")}: ${formatTimestamp(source.completedAt)}`
    : "";
  const originalDate = meta.dueDate
    ? `${translationFor(currentLanguage, "originalDate")}: ${formatDate(meta.dueDate)}`
    : "";
  return [completed, originalDate, detailLines(source, { includeDate: false })].filter(Boolean).join(" \u00b7 ");
}

function mediaForSource(source) {
  const imageDataUrl = source.meta?.imageDataUrl;
  if (!imageDataUrl) {
    return null;
  }
  return {
    src: imageDataUrl,
    alt: source.meta?.imageDescription || textFor(source, "title"),
  };
}

function createButton(labelKey, onClick) {
  const button = document.createElement("button");
  button.type = "button";
  button.textContent = translationFor(currentLanguage, labelKey);
  button.addEventListener("click", onClick);
  return button;
}

function createListItem({
  chipText,
  chipClass,
  title,
  body,
  meta,
  media,
  actions = [],
  removableId,
  onRemove,
  removeLabelKey = "removeSource",
}) {
  const item = document.createElement("article");
  item.className = "list-item";

  const chip = document.createElement("span");
  chip.className = chipClass;
  chip.textContent = chipText;

  const content = document.createElement("div");
  content.className = "source-card-body";
  const heading = document.createElement("h3");
  heading.textContent = title;
  content.append(heading);
  if (body) {
    const copy = document.createElement("p");
    copy.textContent = body;
    content.append(copy);
  }

  if (media?.src) {
    const figure = document.createElement("div");
    figure.className = "source-media";
    const image = document.createElement("img");
    image.src = media.src;
    image.alt = media.alt || title;
    figure.append(image);
    content.append(figure);
  }

  if (meta) {
    const metaLine = document.createElement("span");
    metaLine.className = "meta-line";
    metaLine.textContent = meta;
    content.append(metaLine);
  }

  if (actions.length > 0) {
    const actionRow = document.createElement("div");
    actionRow.className = "card-actions";
    actionRow.append(...actions);
    content.append(actionRow);
  }

  item.append(chip, content);

  if (removableId || onRemove) {
    const remove = document.createElement("button");
    remove.type = "button";
    remove.className = "source-remove";
    remove.setAttribute("aria-label", translationFor(currentLanguage, removeLabelKey));
    remove.textContent = "\u00d7";
    remove.addEventListener("click", () => {
      if (onRemove) {
        onRemove();
        return;
      }
      appState.sources = appState.sources.filter((source) => source.id !== removableId);
      persistState();
      renderAll();
    });
    item.append(remove);
  }

  return item;
}

function draftFromSource(source) {
  const isNote = source.kind === "note";
  noteTitle.value = textFor(source, "title");
  noteBody.value = isNote
    ? source.body
    : [textFor(source, "note"), detailLines(source)].filter(Boolean).join("\n\n");
  selectedNoteSourceId = isNote ? source.sourceId ?? "" : source.id;
  populateNoteSources(selectedNoteSourceId);
  noteSource.value = selectedNoteSourceId;
  setActiveRoute("write");
}

function moveSourceToVault(sourceId) {
  const source = appState.sources.find((item) => item.id === sourceId);
  if (!source) {
    return;
  }
  source.inVault = true;
  source.completedAt = Date.now();
  persistState();
  sourceFeedback.textContent = translationFor(currentLanguage, "movedToVault");
  renderAll();
}

function reopenSource(sourceId) {
  const source = appState.sources.find((item) => item.id === sourceId);
  if (!source) {
    return;
  }
  source.inVault = false;
  delete source.completedAt;
  persistState();
  reviewFeedback.textContent = translationFor(currentLanguage, "reviewedMessage");
  renderAll();
}

function createEmptyCard(messageKey) {
  return createListItem({
    chipText: "0",
    chipClass: "file-chip",
    title: translationFor(currentLanguage, messageKey),
    body: "",
  });
}

function eventTriggerDate(event) {
  if (!event.date || !event.time) {
    return null;
  }
  const [hour, minute] = event.time.split(":").map(Number);
  const date = dateFromYmd(event.date);
  date.setHours(hour, minute, 0, 0);
  return date;
}

function reminderDateForEvent(event) {
  const trigger = eventTriggerDate(event);
  if (!trigger || event.reminderOffsetMinutes === "none") {
    return null;
  }
  const offset = Number(event.reminderOffsetMinutes ?? "none");
  if (!Number.isFinite(offset)) {
    return null;
  }
  return new Date(trigger.getTime() - offset * 60_000);
}

function reminderLabel(event) {
  const value = event.reminderOffsetMinutes ?? "none";
  if (value === "0") {
    return translationFor(currentLanguage, "reminderAtTime");
  }
  if (value === "10") {
    return translationFor(currentLanguage, "reminder10");
  }
  if (value === "30") {
    return translationFor(currentLanguage, "reminder30");
  }
  if (value === "60") {
    return translationFor(currentLanguage, "reminder60");
  }
  if (value === "1440") {
    return translationFor(currentLanguage, "reminderDay");
  }
  return "";
}

async function ensureBrowserNotificationPermission() {
  if (!("Notification" in window)) {
    return false;
  }
  if (Notification.permission === "granted") {
    return true;
  }
  if (Notification.permission === "denied") {
    return false;
  }
  return (await Notification.requestPermission()) === "granted";
}

function clearBrowserReminderTimers() {
  for (const timer of browserReminderTimers.values()) {
    window.clearTimeout(timer);
  }
  browserReminderTimers = new Map();
}

function scheduleBrowserReminders() {
  clearBrowserReminderTimers();
  if (!("Notification" in window) || Notification.permission !== "granted") {
    return;
  }
  const now = Date.now();
  const maxDelay = 2_147_483_647;
  for (const event of appState.events) {
    if (event.notifiedAt || event.reminderOffsetMinutes === "none") {
      continue;
    }
    const reminderAt = reminderDateForEvent(event);
    if (!reminderAt) {
      continue;
    }
    const delay = reminderAt.getTime() - now;
    if (delay < 0 || delay > maxDelay) {
      continue;
    }
    const timer = window.setTimeout(() => {
      new Notification(event.title, {
        body: formatEventDateTime(event),
        tag: event.id,
      });
      event.notifiedAt = Date.now();
      persistState();
      browserReminderTimers.delete(event.id);
    }, delay);
    browserReminderTimers.set(event.id, timer);
  }
}

function renderToday() {
  const inboxSources = appState.sources.filter((source) => !source.inVault);
  const completedTasks = appState.sources.filter((source) => source.inVault);
  const todayEvents = eventsForDate(todayYmd());
  const nextEvent = upcomingEvents(1)[0];
  const hasAnyUserData =
    appState.sources.length > 0 || appState.notes.length > 0 || appState.events.length > 0;
  capturedCount.textContent = String(inboxSources.length);
  reviewCount.textContent = String(todayEvents.length);
  scheduleCount.textContent = String(completedTasks.length);
  todayList.innerHTML = "";

  const dueTasks = inboxSources.filter((source) => source.meta?.dueDate === todayYmd());

  const actions = [
    {
      show: inboxSources.length > 0 || !hasAnyUserData,
      chip: inboxSources.length > 0 ? String(inboxSources.length) : "+",
      titleKey: inboxSources.length > 0 ? "openInbox" : "addSharedSource",
      bodyKey: inboxSources.length > 0 ? "inboxBody" : "onboardingCapture",
      onClick: () => {
        setActiveRoute("vault");
        if (inboxSources.length === 0) {
          openSourceForm();
        }
      },
    },
    {
      show: dueTasks.length > 0,
      chip: String(dueTasks.length),
      titleKey: "todaySchedule",
      body: dueTasks.map((task) => textFor(task, "title")).join(" | "),
      onClick: () => setActiveRoute("vault"),
    },
    {
      show: appState.events.length === 0,
      chip: "+",
      titleKey: "addSchedule",
      bodyKey: "calendarBody",
      onClick: () => {
        setActiveRoute("calendar");
        openScheduleForm();
      },
    },
    {
      show: todayEvents.length > 0,
      chip: String(todayEvents.length),
      titleKey: "todaySchedule",
      body: todayEvents.map((event) => event.title).join(" | "),
      onClick: () => setActiveRoute("calendar"),
    },
    {
      show: todayEvents.length === 0 && Boolean(nextEvent),
      chip: formatDate(nextEvent?.date ?? todayYmd(), { month: "short", day: "numeric" }),
      titleKey: "upcomingSchedule",
      body: nextEvent ? nextEvent.title : "",
      onClick: () => {
        selectedScheduleDate = nextEvent.date;
        calendarCursor = monthStart(dateFromYmd(nextEvent.date));
        setActiveRoute("calendar");
        renderAll();
      },
    },
    {
      show: appState.sources.length > 0 && appState.notes.length === 0,
      chip: "+",
      titleKey: "openWrite",
      bodyKey: "writeBody",
      onClick: () => setActiveRoute("write"),
    },
    {
      show: completedTasks.length > 0,
      chip: String(completedTasks.length),
      titleKey: "openReview",
      bodyKey: "reviewBody",
      onClick: () => setActiveRoute("review"),
    },
  ].filter((action) => action.show);

  if (actions.length === 0) {
    todayList.append(createEmptyCard("noTodayItems"));
    return;
  }

  for (const action of actions) {
    todayList.append(
      createListItem({
        chipText: action.chip,
        chipClass: "file-chip link",
        title: translationFor(currentLanguage, action.titleKey),
        body: action.body ?? translationFor(currentLanguage, action.bodyKey),
        actions: [createButton(action.titleKey, action.onClick)],
      }),
    );
  }
}

function eventsForDate(date) {
  return appState.events
    .filter((event) => event.date === date)
    .sort((a, b) => (a.createdAt ?? 0) - (b.createdAt ?? 0));
}

function upcomingEvents(limit = 3) {
  const today = todayYmd();
  return appState.events
    .filter((event) => event.date >= today)
    .sort((a, b) => {
      const dateSort = a.date.localeCompare(b.date);
      if (dateSort !== 0) return dateSort;
      return (a.time ?? "").localeCompare(b.time ?? "") || (a.createdAt ?? 0) - (b.createdAt ?? 0);
    })
    .slice(0, limit);
}

function renderCalendar() {
  calendarMonth.textContent = new Intl.DateTimeFormat(activeLocale(), { month: "long", year: "numeric" }).format(
    calendarCursor,
  );
  calendarWeekdays.innerHTML = "";
  const weekdayStart = new Date(2026, 5, 14);
  for (let index = 0; index < 7; index += 1) {
    const label = document.createElement("span");
    label.textContent = new Intl.DateTimeFormat(activeLocale(), { weekday: "short" }).format(
      new Date(weekdayStart.getFullYear(), weekdayStart.getMonth(), weekdayStart.getDate() + index),
    );
    calendarWeekdays.append(label);
  }

  calendarGrid.innerHTML = "";
  const first = monthStart(calendarCursor);
  const cursor = new Date(first);
  cursor.setDate(first.getDate() - first.getDay());
  const today = todayYmd();
  for (let index = 0; index < 42; index += 1) {
    const date = new Date(cursor);
    date.setDate(cursor.getDate() + index);
    const value = toYmd(date);
    const button = document.createElement("button");
    button.type = "button";
    button.className = "calendar-day";
    button.textContent = String(date.getDate());
    button.setAttribute("aria-label", formatDate(value, { dateStyle: "full" }));
    button.classList.toggle("outside", date.getMonth() !== calendarCursor.getMonth());
    button.classList.toggle("today", value === today);
    button.classList.toggle("selected", value === selectedScheduleDate);
    button.classList.toggle("has-events", eventsForDate(value).length > 0);
    button.addEventListener("click", () => {
      selectedScheduleDate = value;
      calendarCursor = monthStart(date);
      scheduleDate.value = selectedScheduleDate;
      renderAll();
    });
    calendarGrid.append(button);
  }
}

function renderScheduleList() {
  scheduleDate.value = selectedScheduleDate;
  scheduleList.innerHTML = "";
  const events = eventsForDate(selectedScheduleDate);
  if (events.length === 0) {
    scheduleList.append(createEmptyCard("emptySchedule"));
    return;
  }

  for (const event of events) {
    scheduleList.append(
      createListItem({
        chipText: scheduleKindLabel(event.kind),
        chipClass: chipClassForSchedule(event.kind),
        title: event.title,
        body: event.note || formatEventDateTime(event),
        meta: [formatEventDateTime(event), reminderLabel(event)].filter(Boolean).join(" \u00b7 "),
        onRemove: () => {
          appState.events = appState.events.filter((item) => item.id !== event.id);
          persistState();
          renderAll();
        },
        removeLabelKey: "removeSchedule",
      }),
    );
  }
}

function vaultItems() {
  return [
    ...appState.sources.map((source) => ({ ...source, kind: "source" })),
    ...appState.notes.map((note) => ({ ...note, kind: "note", type: "TXT" })),
  ];
}

function matchesVaultFilter(item) {
  if (item.kind === "note") {
    return currentVaultFilter === "all";
  }
  if (currentVaultFilter === "open") {
    return !item.inVault;
  }
  if (currentVaultFilter === "today") {
    return !item.inVault && item.meta?.dueDate === todayYmd();
  }
  if (currentVaultFilter === "important") {
    return !item.inVault && item.meta?.priority === "high";
  }
  if (currentVaultFilter === "done") {
    return item.inVault;
  }
  return true;
}

function renderVaultFilters() {
  for (const button of vaultFilterButtons) {
    const active = button.dataset.vaultFilter === currentVaultFilter;
    button.classList.toggle("active", active);
    if (active) {
      button.setAttribute("aria-pressed", "true");
    } else {
      button.setAttribute("aria-pressed", "false");
    }
  }
}

function renderVault() {
  renderVaultFilters();
  const query = vaultSearch.value.trim().toLocaleLowerCase();
  vaultResults.innerHTML = "";
  const items = vaultItems().filter((item) => {
    if (!matchesVaultFilter(item)) {
      return false;
    }
    const haystack = [
      textFor(item, "title"),
      textFor(item, "note"),
      item.body,
      detailLines(item),
    ].join(" ").toLocaleLowerCase();
    return !query || haystack.includes(query);
  });

  if (items.length === 0) {
    vaultResults.append(createEmptyCard("vaultSearchEmpty"));
    return;
  }

  for (const item of items) {
    const isNote = item.kind === "note";
    const actions = isNote
      ? [createButton("writeTitle", () => draftFromSource(item))]
      : [
          createButton(item.inVault ? "again" : "sendToVault", () =>
            item.inVault ? reopenSource(item.id) : moveSourceToVault(item.id),
          ),
          createButton("draftFromSource", () => draftFromSource(item)),
        ];
    vaultResults.append(
      createListItem({
        chipText: translationFor(currentLanguage, isNote ? "vaultNoteLabel" : "vaultSourceLabel"),
        chipClass: isNote ? "file-chip text" : chipClassFor(item.type),
        title: textFor(item, "title"),
        body: isNote ? item.body : textFor(item, "note"),
        meta: isNote ? "" : item.inVault ? completedLines(item) : detailLines(item),
        media: isNote ? null : mediaForSource(item),
        actions,
      }),
    );
  }
}

function populateNoteSources(selectedId = selectedNoteSourceId || noteSource.value) {
  noteSource.innerHTML = "";
  const empty = document.createElement("option");
  empty.value = "";
  empty.textContent = translationFor(currentLanguage, "noEvidenceSource");
  noteSource.append(empty);

  for (const source of allSources()) {
    const option = document.createElement("option");
    option.value = source.id;
    option.textContent = textFor(source, "title");
    noteSource.append(option);
  }
  noteSource.value = selectedId && findSourceById(selectedId) ? selectedId : "";
  selectedNoteSourceId = noteSource.value;
}

function renderNotes() {
  populateNoteSources();
  noteList.innerHTML = "";
  if (appState.notes.length === 0) {
    noteList.append(createEmptyCard("emptyNotes"));
    return;
  }

  for (const note of appState.notes) {
    const source = findSourceById(note.sourceId);
    noteList.append(
      createListItem({
        chipText: translationFor(currentLanguage, "vaultNoteLabel"),
        chipClass: "file-chip text",
        title: note.title,
        body: note.body,
        meta: source ? textFor(source, "title") : "",
        actions: [createButton("openReview", () => setActiveRoute("review"))],
      }),
    );
  }
}

function exportCurrentNote() {
  const title = noteTitle.value.trim() || translationFor(currentLanguage, "writeTitle");
  const body = noteBody.value.trim();
  const source = findSourceById(noteSource.value);
  const sourceLine = source ? `\n\nTask: ${textFor(source, "title")}` : "";
  const markdown = `# ${title}\n\n${body}${sourceLine}\n`;
  const blob = new Blob([markdown], { type: "text/markdown" });
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = `${title.replace(/[^\w-]+/g, "-") || "note"}.md`;
  link.click();
  URL.revokeObjectURL(link.href);
}

function reviewQueue() {
  return appState.reviews.filter((review) => review.due !== false);
}

function renderReview() {
  completedList.innerHTML = "";
  const completedTasks = appState.sources
    .filter((source) => source.inVault)
    .sort((a, b) => (b.completedAt ?? 0) - (a.completedAt ?? 0));

  if (completedTasks.length === 0) {
    completedList.append(createEmptyCard("reviewEmpty"));
    return;
  }

  for (const source of completedTasks) {
    completedList.append(
      createListItem({
        chipText: translationFor(currentLanguage, "review"),
        chipClass: chipClassFor(source.type),
        title: textFor(source, "title"),
        body: textFor(source, "note"),
        meta: completedLines(source),
        media: mediaForSource(source),
        actions: [
          createButton("again", () => reopenSource(source.id)),
          createButton("draftFromSource", () => draftFromSource(source)),
        ],
        removableId: source.id,
      }),
    );
  }
}

function gradeCurrentReview(grade) {
  const current = reviewQueue()[0];
  if (!current) {
    return;
  }
  const review = appState.reviews.find((item) => item.id === current.id);
  if (!review) {
    return;
  }
  review.due = false;
  review.grade = grade;
  review.reviewedAt = Date.now();
  persistState();
  reviewFeedback.textContent = translationFor(currentLanguage, "reviewedMessage");
  renderAll();
}

function renderAll() {
  renderToday();
  renderCalendar();
  renderScheduleList();
  renderVault();
  renderNotes();
  renderReview();
  scheduleBrowserReminders();
}

for (const button of routeButtons) {
  button.addEventListener("click", () => {
    setActiveRoute(button.dataset.route);
  });
}

languageSelect.addEventListener("change", () => {
  setLanguage(languageSelect.value);
});

sourceType.addEventListener("change", updateSourceFields);
addSourceOpen.addEventListener("click", openSourceForm);
sourceCancel.addEventListener("click", closeSourceForm);
sourceFile.addEventListener("change", () => {
  clearFileUpload(false);
  const file = sourceFile.files?.[0];
  if (!file) {
    return;
  }
  pendingFileUpload = prepareFileUpload(file);
  renderUploadPreview(sourceFilePreview, pendingFileUpload, () => clearFileUpload());
  sourceFeedback.textContent = "";
});
sourceImageFile.addEventListener("change", async () => {
  clearImageUpload(false);
  const file = sourceImageFile.files?.[0];
  if (!file) {
    return;
  }
  try {
    pendingImageUpload = await prepareImageUpload(file);
    renderImagePreview(pendingImageUpload);
    sourceFeedback.textContent = "";
  } catch {
    sourceFeedback.textContent = translationFor(currentLanguage, "imageUploadFailed");
  }
});
addScheduleOpen.addEventListener("click", openScheduleForm);
scheduleCancel.addEventListener("click", closeScheduleForm);
calendarPrev.addEventListener("click", () => {
  calendarCursor = addMonths(calendarCursor, -1);
  renderAll();
});
calendarNext.addEventListener("click", () => {
  calendarCursor = addMonths(calendarCursor, 1);
  renderAll();
});
for (const button of vaultFilterButtons) {
  button.addEventListener("click", () => {
    currentVaultFilter = button.dataset.vaultFilter;
    renderVault();
  });
}
vaultSearch.addEventListener("input", renderVault);
noteSource.addEventListener("change", () => {
  selectedNoteSourceId = noteSource.value;
});
exportMarkdown.addEventListener("click", exportCurrentNote);
resetData.addEventListener("click", () => {
  appState = defaultState();
  persistState();
  localStorage.removeItem(legacySourcesKey);
  localStorage.removeItem(onboardingKey);
  settingsFeedback.textContent = translationFor(currentLanguage, "resetDone");
  renderAll();
  showOnboarding();
});
showGuide.addEventListener("click", showOnboarding);
onboardingStart.addEventListener("click", startFromOnboarding);
onboardingDismiss.addEventListener("click", hideOnboarding);
document.addEventListener("keydown", (event) => {
  if (event.key === "Escape" && !onboarding.hidden) {
    hideOnboarding();
  }
});

sourceForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  if (!sourceForm.reportValidity()) {
    return;
  }

  let meta;
  try {
    meta = await buildSourceMeta(sourceType.value);
  } catch {
    sourceFeedback.textContent = translationFor(currentLanguage, "imageUploadFailed");
    return;
  }
  if (sourceType.value === "IMG" && !meta.imageDataUrl) {
    sourceFeedback.textContent = translationFor(currentLanguage, "imageUploadFailed");
    return;
  }

  const source = {
    id: createId(),
    type: sourceType.value,
    title: sourceTitle.value.trim(),
    note: sourceNote.value.trim(),
    meta,
    inVault: false,
    createdAt: Date.now(),
  };
  appState.sources.unshift(source);
  persistState();
  closeSourceForm();
  sourceFeedback.textContent = translationFor(currentLanguage, "sourceAdded");
  sourceFeedback.dataset.sourceFeedback = "added";
  renderAll();
});

scheduleForm.addEventListener("submit", (event) => {
  event.preventDefault();
  if (!scheduleForm.reportValidity()) {
    return;
  }

  const eventDate = scheduleDate.value || selectedScheduleDate;
  const eventItem = {
    id: createId(),
    title: scheduleTitle.value.trim(),
    date: eventDate,
    time: scheduleTime.value,
    reminderOffsetMinutes: scheduleReminder.value,
    kind: scheduleKind.value,
    note: scheduleNote.value.trim(),
    createdAt: Date.now(),
  };
  appState.events.unshift(eventItem);
  selectedScheduleDate = eventDate;
  calendarCursor = monthStart(dateFromYmd(eventDate));
  persistState();
  closeScheduleForm();
  scheduleFeedback.textContent = translationFor(currentLanguage, "scheduleSaved");
  renderAll();
  if (eventItem.reminderOffsetMinutes !== "none") {
    ensureBrowserNotificationPermission().then((granted) => {
      scheduleFeedback.textContent = translationFor(
        currentLanguage,
        granted ? "browserReminderSet" : "browserReminderBlocked",
      );
      scheduleBrowserReminders();
    });
  }
});

noteForm.addEventListener("submit", (event) => {
  event.preventDefault();
  if (!noteForm.reportValidity()) {
    return;
  }
  const source = findSourceById(noteSource.value);
  const note = {
    id: createId(),
    title: noteTitle.value.trim(),
    body: noteBody.value.trim(),
    sourceId: source?.id ?? "",
    createdAt: Date.now(),
  };
  appState.notes.unshift(note);
  persistState();
  noteForm.reset();
  selectedNoteSourceId = "";
  noteFeedback.textContent = translationFor(currentLanguage, "noteSaved");
  renderAll();
});

for (const [index, button] of Array.from(gradeButtons).entries()) {
  button.addEventListener("click", () => {
    gradeCurrentReview(["again", "hard", "good", "easy"][index]);
  });
}

updateSourceFields();
setLanguage(currentLanguage);
setActiveRoute("today");
if (localStorage.getItem(onboardingKey) !== "true") {
  showOnboarding();
}
