import express from "express";
import { URL } from "url";
import cons from "consolidate";
import _ from "underscore";

const app = express();
const port = 9000;

app.engine("html", cons.underscore);
app.set("view engine", "html");
app.set("views", "files/client");

const authServer = {
  authorizationEndpoint: "http://localhost:9001/authorize",
  tokenEndpoint: "http://localhost:9001/token",
};

const client = {
  client_id: "",
  client_secret: "",
  redirect_uris: ["http://localhost:9000/callback"],
};

const protectedResource = "http://localhost:9002/resource";

const state = null;

const access_token = null;
const scope = null;

app.get("/", (_req, res) => {
  res.render("index", { access_token, scope });
});

app.get("/authorize", (_req, res) => {
  const authorizeUrl = buildUrl(authServer.authorizationEndpoint, {
    response_type: "code",
    client_id: client.client_id,
    redirect_uri: client.redirect_uris[0],
  });
  res.redirect(authorizeUrl);
});

const buildUrl = (base: string, options: Object, hash?: string) => {
  const newUrl = new URL(base);
  for (const [key, value] of Object.entries(options)) {
    newUrl.searchParams.append(key, value);
  }
  if (hash) {
    newUrl.hash = hash;
  }
  return newUrl.toString();
};

app.listen(port, "localhost", () => {
  console.log(`OAuth Client is listening at http://localhost:${port}`);
});
