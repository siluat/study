import React from "react";
import ReactDOM from "react-dom";
import App from "./App";
import compositeContentRefinerFactory from "./content-refiners/compositeContentRefinerFactory";
import compactWhitespaces from "./content-refiners/compactWhitespaces";
import trimWhitepaces from "./content-refiners/trimWhitespaces";
import commentComposerFactory from "./commentComposerFactory";

const commentRefiner = compositeContentRefinerFactory([
  compactWhitespaces,
  trimWhitepaces,
]);

ReactDOM.render(
  <React.StrictMode>
    <App commentComposer={commentComposerFactory(commentRefiner)} />
  </React.StrictMode>,
  document.getElementById("root")
);
