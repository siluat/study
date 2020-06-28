// example 1.6

async function test() {
  // promise가 아닌 값/객체에도 await을 할 수는 있습니다.
  let res = await 'Hello World!';
  console.log(res); // "Hello, World!"

  const promise = new Promise((resolve) => {
    // promise는 1초 후에 "Hello, World!"가 완료된/이행된 값이 됩니다.
    setTimeout(() => resolve('Hello, World!'), 1000);
  });

  res = await promise;

  // "Hello, World!"가 출력됩니다. res는 promise의 "이행값"입니다.
  console.log(res);
  // "Hello, World!"가 출력됩니다. 함수 파라미터에 'await'을 사용할 수 있습니다.
  console.log(await promise);
}

test();
