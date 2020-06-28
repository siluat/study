// example 1.18
// .catch()로 오류 처리를 하는 것이 좋을 때도 있다.

// 이런 방식의 코드를 작성하고 있다고 생각된다면 이젠는 아래 방식을 이용하세요
async function fn1() {
  try {
    /* Bunch of logic here */
  } catch (err) {
    handleError(err);
  }
}

// 이 방식이요~
async function fn2() {
  /* Bunch of logic here */
}

fn2().catch(handleError);
