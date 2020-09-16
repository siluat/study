import assert from "assert";
import Promise from "./Promise.js";

async function test() {
  const res = await new Promise((resolve) => {
    setTimeout(() => resolve("Hello"), 50);
  });
  assert.equal(res, "Hello");
}

test();
