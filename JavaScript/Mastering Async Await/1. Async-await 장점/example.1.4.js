// example 1.4
// async 함수 안의 클로저 함수도 비동기 함수가 아니라면 await를 사용할 수 없다.

const assert = require('assert');

async function test() {
  const p = Promise.resolve('test');
  assert.doesNotThrow(function () {
    // SyntaxError: Unexpected identifier
    await p;
  });
}

test();