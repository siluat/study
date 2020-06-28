// example 1.5
// 새로운 함수를 만들지 않는 한, if, loop문장 안에 await을 사용할 수 있다.

async function test() {
  while (true) {
    // Convoluted way to print out "Hello, World!" once per
    // second by pausing execution for 200ms 5 times
    for (let i = 0; i < 10; ++i) {
      if (i % 2 === 0) {
        await new Promise((resolve) => setTimeout(resolve, 200));
      }
    }
    console.log('Hello, World!');
  }
}

test();
