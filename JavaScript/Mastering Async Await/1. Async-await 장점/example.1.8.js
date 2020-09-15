// example 1.8

async function computeValue() {
  // The resolved value is a promise. The promise returned from
  // `computeValue()` will be fulfilled with 'Hello, World!'
  // promise가 '해결값' 입니다.
  //   computeValue()에서 반환된 promise는 'Hello, World!'로 '이행'됩니다.
  return new Promise((resolve) => {
    setTimeout(() => resolve('Hello, World!'), 1000);
  });
}

async function test() {
  console.log(await computeValue());
}

test();
