## Async/await 장점

- async 키워드는 일반 함수를 비동기 함수로 바꾸어 놓는다.
- 비동기 함수는 await 키워드를 사용해서 promise가 "정착" 상태가 될 때까지 실행을 일시 중지시킬 수 있다.
- await 키워드는 비동기 함수의 본문 내부 어디에서도 사용할 수 있다.
- async로 표시한 함수의 내부에서만 await를 사용할 수 있다.

### Return Values: 반환값들

- async/await는 단순히 코드 실행을 일시 중지하는 것 이상으로 활용된다.
- await의 반환값은 promise가 완료 상태일 때의 값이다.
- **async 함수는 항상 promise를 반환한다.**
- **async 함수 내부에서 값을 return 하면, 자바스크립트는 그 값을 promise의 이행값으로 만든다.**
- async 함수 호출에 대해 await 키워드를 사용하면 그 async 함수의 처리 완료된 "반환값"을 얻을 수 있다.
- 이 책은 async 함수에서 직접 return한 값을 "해결값(resolved)"이라고 부른다.
- "반환값"과 "해결값"의 구분은 매우 중요하다.

> promise가 아닌 값을 return해도 해당 값은 해결값이 되며 실제 반환값은 promise가 된다.

- async 함수에서 promise를 직접 반환할 수도 있다. 이 경우에도 해결값과 반환값은 같지 않다.
- async 함수에서 항상 직접 promise를 반환할 필요는 없다. 자바스크립트 런타임이 알아서 한다.

### Error Handling: 오류 처리

- async/await의 중요한 특징들 중 하나는 try/catch를 이용하여 비동기적 오류를 처리할 수 있다는 것이다.
- promise p가 "이행"이면, await p를 그 promise의 값으로 평가한다.
- promise p가 "실패"라면, await p는 예외를 발생시킨다.
- callback 기반 코드에 비해 오류 처리 패턴이 단순해진다.
- 비동기 함수에서 오류를 throw하면, 자바스크립트는 반환된 promise를 거절한다.
- 이 책은 async 함수에서 throw한 (오류)값을 "거절값"이라고 표현한다.
- 비동기 함수에서 throw한 오류를 try/catch가 처리할 수 있도록 실제로 발생시키는건 await 키워드다.
- async 함수는 항상 promise를 반환하기 때문에 .catch()로도 오류를 처리할 수 있다.

> try/catch와 .catch(), 상황에 따라 두 방식 중 적절한 것을 선택하여 쓰는 것이 좋을 것 같다.

### 실패한 HTTP Request 재시도

- 실패한 요청 재시도 처리에 async/await를 사용하면 callback 기반 코드보다 더 쉬운 코드로 문제를 해결할 수 있다.
- 일반적으로 async/await를 사용하면 비동기 연산들을 순차적으로 처리하는 것이 사소해 보인다.
