import { createServer } from "node:http";
import { readFile } from "node:fs/promises";
import { extname, relative, resolve, sep } from "node:path";
import { fileURLToPath } from "node:url";

const repoRoot = resolve(fileURLToPath(new URL("..", import.meta.url)));
const portArgIndex = process.argv.indexOf("--port");
const port = Number(
  portArgIndex >= 0 ? process.argv[portArgIndex + 1] : process.env.PORT || 4173,
);

const contentTypes = new Map([
  [".html", "text/html; charset=utf-8"],
  [".css", "text/css; charset=utf-8"],
  [".js", "text/javascript; charset=utf-8"],
  [".mjs", "text/javascript; charset=utf-8"],
  [".json", "application/json; charset=utf-8"],
  [".apk", "application/vnd.android.package-archive"],
]);

function isInsideRoot(filePath) {
  const candidate = resolve(filePath);
  const pathFromRoot = relative(repoRoot, candidate);
  return pathFromRoot === "" || (!pathFromRoot.startsWith("..") && !pathFromRoot.startsWith(sep));
}

function sendText(response, status, text) {
  response.writeHead(status, {
    "Cache-Control": "no-store",
    "Content-Type": "text/plain; charset=utf-8",
  });
  response.end(text);
}

const server = createServer(async (request, response) => {
  const requestUrl = new URL(request.url ?? "/", `http://127.0.0.1:${port}`);
  let pathname = decodeURIComponent(requestUrl.pathname);

  if (pathname === "/") {
    response.writeHead(302, { Location: "/web-preview/" });
    response.end();
    return;
  }

  if (pathname.endsWith("/")) {
    pathname += "index.html";
  }

  const filePath = resolve(repoRoot, `.${pathname}`);
  if (!isInsideRoot(filePath)) {
    sendText(response, 403, "Forbidden");
    return;
  }

  try {
    const body = await readFile(filePath);
    response.writeHead(200, {
      "Cache-Control": "no-store",
      "Content-Type": contentTypes.get(extname(filePath).toLowerCase()) ?? "application/octet-stream",
    });
    response.end(body);
  } catch {
    sendText(response, 404, "Not found");
  }
});

server.listen(port, "127.0.0.1", () => {
  console.log(`Hermes Daily Planner preview: http://127.0.0.1:${port}/web-preview/`);
});
