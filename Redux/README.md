# Redux

## 기본개념

### Action

- 상태의 변화가 필요할 때 변화에 대한 정보를 담아 객체로 표현한 것
- `type` 속성을 필수로 가져야 하고 다른 속성은 마음대로 구성할 수 있다

> 액션 정의에 대한 표준 문서도 있긴 하다.
> [Flux Standard Action](https://github.com/redux-utilities/flux-standard-action#flux-standard-action)

- 애플리케이션에서 스토어로 보내게 된다.

```js
const ADD_TODO = "ADD_TODO";

{
  type: ADD_TODO;
  text: "Build my first Redux app";
}
```

### Action Creator

- 액션을 만드는(반환하는) 함수

```js
function addTodo(text) {
  return {
    type: ADD_TODO,
    text,
  };
}
```

### Reducer

- 액션을 받아 상태를 변화시킨다
- 변화 전 상태와 액션을 받아서 변화 후 상태를 반환한다

```
  function todoApp(state = initialState, action) {
  // action에 대한 처리
  return nextState
  }
```

### Store

- 애플리케이션의 상태를 유지한다
- 상태를 조회하거나 업데이트 할 수 있는 함수를 제공한다
- 상태 변화에 대한 리스너를 등록하거나 삭제하는 함수를 제공한다
- 하나의 애플리케이션은 하나의 스토어만 가진다

```js
import { createStore } from "redux";
import todoApp from "./reducers";

const store = createStore(todoApp);
```

### Data Flow

- 액션과 리듀서를 정의하고 `redux`의 `createStore()` 함수로 스토어를 만들 수 있다
- 스토어를 통한 데이터 흐름은 다음 4단계를 따른다
  1. `store.dispatch()` 함수로 액션을 전달
  2. 액션을 받은 스토어는 생성 시 전달받은 리듀서를 호출
  3. 호출된 리듀서는 액션을 처리하고 상태를 변경
  4. 리듀서의 처리로 변경된 상태를 스토어가 저장

## 연습코드

- [Redux Counter Vanilla Example](./counter-vanilla) : 바닐라 자바스크립트로 redux 사용

## 참고자료

- [Redux 또는 MobX 를 통한 상태 관리](https://velog.io/@velopert/series/redux-or-mobx)
- [Redux Basic Tutorial](https://redux.js.org/basics/basic-tutorial)
- [Redux Counter Vanilla Example](https://github.com/reduxjs/redux/tree/master/examples/counter-vanilla)
