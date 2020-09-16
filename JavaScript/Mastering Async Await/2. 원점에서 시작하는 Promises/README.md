# 원점에서 시작하는 Promises

```jsx
// 단순화된 Promise 클래스의 골격
class Promise {
  // executor는 두 개의 파라미터를 받습니다; resolve(), reject()
  //    executor 함수는 async 함수가 성공 또는 실패인 경우 그에 대응되는
  //    resolve()나 reject()를 호출할 책임이 있습니다.
  constructor(executor) {}

  // promise가 성공적으로 이행됐으면 onFulfilled가 호출되며,
  //   실패면 경우 onRejected가 호출됩니다.
  // 당분간 여러분은 fulfilled와 resolved를 같다고 생각할 수 있습니다.
  then(onFulfilled, onRejected) {}
}
```

- promise는 **3가지 상태를 갖는 상태 기계**
  - pending(보류): 초기 상태이며, 현재 내부 작업(코드)이 수행 중인 상태
  - fulfilled(이행): 내부 작업이 성공적으로 수행되었으며 연관된 결과값을 가지고 있는 상태
  - rejected(실패): 내부 작업이 실패하였고 그와 연관된 오류값을 가지고 있는 상태
- pending 상태가 아닌 promise는 "정착 상태:settled"라고 부른다.
- 일단 promise가 정착되면 다른 상태로 바꿀 수 없다.

  ```jsx
  const p = new Promise((resolve, reject) => {
    resolve("foo");
    // The below `reject()` is a no-op. A fulfilled promise stays
    // fulfilled with the same value forever.
    reject(new Error("bar"));
  });
  ```

- await 키워드는 자신이 다루는 값이 instance of Promise인지 검사하지 않고, then() 함수의 존재만을 검사한다.
- 일반적으로 자바스크립트는 then() 함수를 가지는 모든 객체를 thenable이라 부른다.
  - thenable 객체라면 직접 만든 클래스라도 await/async와 함께 사용할 수 있다.
