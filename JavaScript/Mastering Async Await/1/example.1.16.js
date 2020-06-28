// example 1.16
// 비동기 함수에서 throw한 오류를 try/catch가 처리할 수 있도록 실제로 발생시키는건 await 키워드다.

async function computeValue() {
  throw new Error('Oops!');
}

async function test() {
  try {
    const promise = computeValue();

    // 아래 주석을 없애면 어떤 오류도 발생하지 않습니다.
    // await promise;
    console.log('No Error');
  } catch (error) {
    console.log(error.message); // Won't run
  }
}

test();
