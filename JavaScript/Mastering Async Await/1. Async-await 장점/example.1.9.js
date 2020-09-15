// example 1.9
// async 함수에서 promise를 직접 반환해도 해결값과 반환값은 같지 않다.

let resolvedValue = Promise.resolve('Hello, World!');
const computeValue = async () => resolvedValue;

async function test() {
  // await 없이 호출하면, returnValue는 promise입니다.
  const returnValue = computeValue();

  // `false`. Return value never strictly equals resolved value.
  // 'false'. 둘은 같지 않습니다.
  console.log(returnValue === resolvedValue);
}

test();
