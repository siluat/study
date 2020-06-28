// example 1.17
// async 함수는 항상 promise를 반환하기 때문에 .catch()로도 오류를 처리할 수 있다.

async function computeValue() {
  throw new Error('Oops!');
}

async function test() {
  let err = null;
  await computeValue().catch((_err) => {
    err = _err;
  });

  console.log(err.message);
}

test();
