# Hermes Study Vault Web Preview Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a local browser preview for the completed Android MVP so it can be reviewed at a localhost URL.

**Architecture:** Build a dependency-free static preview under `web-preview/`. The preview mirrors the MVP's five Android destinations, includes an APK download link, and keeps all behavior in one small JavaScript file.

**Tech Stack:** HTML, CSS, vanilla JavaScript, Node.js built-in test script, local static HTTP server.

---

### Task 1: Static Preview

**Files:**
- Create: `web-preview/check-preview.mjs`
- Create: `web-preview/index.html`
- Create: `web-preview/styles.css`
- Create: `web-preview/script.js`

- [ ] **Step 1: Write preview validation script**

Create `web-preview/check-preview.mjs` with assertions for the expected tabs, screen headings, APK link, stylesheet, and script references.

- [ ] **Step 2: Run validation to verify it fails**

Run:

```powershell
node .\web-preview\check-preview.mjs
```

Expected: failure because the preview files do not exist yet.

- [ ] **Step 3: Implement static preview**

Create `index.html`, `styles.css`, and `script.js` with a mobile app shell, bottom navigation, five tab panels, and a local APK download action.

- [ ] **Step 4: Run validation to verify it passes**

Run:

```powershell
node .\web-preview\check-preview.mjs
```

Expected: `web preview checks passed`.

- [ ] **Step 5: Run browser smoke check**

Start a local static server and verify the page loads, tabs switch, and no console errors are emitted.
