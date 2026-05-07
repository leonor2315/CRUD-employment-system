import React from "react";
import { createRoot } from "react-dom/client";
import App from "./App";
import "./styles.css";

// Mount the React app to the root DOM node.
createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
