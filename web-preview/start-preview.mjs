import { spawn } from "node:child_process";
import { fileURLToPath } from "node:url";
import { setTimeout as delay } from "node:timers/promises";

const portArgIndex = process.argv.indexOf("--port");
const port = Number(
  portArgIndex >= 0 ? process.argv[portArgIndex + 1] : process.env.PORT || 4173,
);
const url = `http://127.0.0.1:${port}/web-preview/`;
const serverPath = fileURLToPath(new URL("./serve-preview.mjs", import.meta.url));

async function canLoadPreview() {
  try {
    const response = await fetch(url, { signal: AbortSignal.timeout(1200) });
    return response.ok;
  } catch {
    return false;
  }
}

if (await canLoadPreview()) {
  console.log(`server already running url=${url}`);
  process.exit(0);
}

const child = spawn(process.execPath, [serverPath, "--port", String(port)], {
  detached: true,
  stdio: "ignore",
  windowsHide: true,
});

child.unref();

for (let attempt = 0; attempt < 40; attempt += 1) {
  await delay(250);
  if (await canLoadPreview()) {
    console.log(`server started pid=${child.pid} url=${url}`);
    process.exit(0);
  }
}

console.error(`server did not start url=${url} pid=${child.pid}`);
process.exit(1);
