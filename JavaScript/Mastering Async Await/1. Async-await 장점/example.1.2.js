// example 1.2
// await 키워드는 비동기 함수의 본문 내부 어디에서도 사용할 수 있다.

async function test() {
  for (let i = 0; i < 10; ++i) {
    await new Promise((resolve) => setTimeout(resolve, 100));
  }

  console.log('Hello, World!');
}

test();
