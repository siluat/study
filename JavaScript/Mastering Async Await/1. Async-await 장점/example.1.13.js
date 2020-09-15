// example 1.13
// callback 기반 코드에서는 복잡한 오류 처리 패턴이 필요하다.

function testWrapper(callback) {
  try {
    // (1) test() 수행중 동기적 오류가 발생 할 수 있음
    test(function (error, res) {
      // (2) test()는 error를 가진 callback()을 호촐할 수 있음.
      if (error) {
        return callback(error);
      }

      // (3) res.x를 다루거나 callback()이 예외를 발생시키지 않는지 주의할 필요가 있습니다.***
      try {
        return callback(null, res.x);
      } catch (error) {
        return callback(error);
      }
    });
  } catch {}
}
