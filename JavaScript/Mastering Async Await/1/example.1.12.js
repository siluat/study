// example 1.12
// 비동기 오류 뿐만 아니라 동기 오류도 하나의 try/catch 블럭으로 처리할 수 있다.

async function test() {
  try {
    const bad = undefined;

    bad.x;

    const p = Promise.reject(new Error('Oops!'));

    await p;
  } catch (error) {
    // "cannot read property 'x' of undefined"
    console.log(error.message);
  }
}

test();
