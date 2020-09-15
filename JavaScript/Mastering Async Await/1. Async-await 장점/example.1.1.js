// example 1.1
// 비동기 함수는 await 키워드를 사용해서 promise가 "정착" 상태가 될 때까지 실행을 일시 중지시킬 수 있다.

async function test() {
  await new Promise((resolve) => setTimeout(() => resolve(), 1000));

  console.log('Hello, World!');
}

test();
