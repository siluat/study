// example 1.11
// promise p가 "실패"라면, await p는 예외를 발생시킨다.

async function test() {
  try {
    const p = Promise.reject(new Error('Oops!')); // The below `await` throws
    await p;
  } catch (error) {
    console.log(error.message); // "Oops!"
  }
}

test();
