// example 1.14
// callback 기반 코드에 비해 오류 처리 패턴이 단순해진다.

async function testWrapper() {
  try {
    // `try/catch`는 test()내부에서 발생하는 동기적 오류,
    //  비동기 수행이 완료되었으나 rejected인 경우,
    //  그리고 res.x를 접근할 때 발생하는 동기적 오류를 모두 처리합니다.
    const res = await test();
    return res.x;
  } catch (error) {
    throw error;
  }
}
