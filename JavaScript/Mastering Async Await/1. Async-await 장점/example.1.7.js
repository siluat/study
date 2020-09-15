// example 1.7

async function computeValue() {
  await new Promise((resolve) => setTimeout(resolve, 1000));

  // "Hello, World"는 이 함수 호출이 "완료상태가 되었을때 값"입니다.
  return 'Hello, World!';
}

async function test() {
  // 1초 후 "Hello, World!"를 출력합니다. computeValue()는 promise를 반환하며 반환값에
  //   await을 하면 처리완료된 값 "Hello, World!"가 됩니다.
  console.log(await computeValue());
}

test();
