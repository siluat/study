# Redux

## 기본 개념

### Action

- 상태의 변화가 필요할 때 변화에 대한 정보를 담아 객체로 표현한 것
- `type` 속성을 필수로 가져야 하고 다른 속성은 마음대로 구성할 수 있다

> 액션 정의에 대한 표준 문서도 있긴 하다.
> [Flux Standard Action](https://github.com/redux-utilities/flux-standard-action#flux-standard-action) (FSA)

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
- 리듀서는 순수 함수여야 한다
- 상태를 직접 바꾸지 않고 기존 상태를 복사해서 새로운 상태를 만든다
  > `Object.assign()` 이나 Spread syntax를 사용한다

> 리덕스도 상태 변경 감지를 위해 shallow equality 검사를 하기 때문에 불변성을 유지해야 한다.

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

## React와 함께 사용하기

### Ducks: Redux Reducer Bundles

Redux 공식 매뉴얼은 액션 파일, 리듀서 파일 등을 별도로 구성하도록 나오지만 해당 방법은 불편하다고 하는 의견도 있다. 대안으로 관련 파일들을 하나로 작성하는 [Ducks: Redux Reducer Bundles](https://github.com/erikras/ducks-modular-redux) 방식의 제안도 있다.

### 컴포넌트의 구분

역할에 따라 컴포넌트를 구분할 수 있다. 이러한 구분은 이해하기 쉽고 컴포넌트의 재사용이 쉬운 앱을 만드는데 도움이 된다.

> [공식 문서의 표](https://redux.js.org/basics/usage-with-react#presentational-and-container-components)에 구분 기준이 잘 정리되어 있다.

#### 컨테이너 컴포넌트

- 리덕스와 실제로 연동되는 컴포넌트

#### 프레젠테이셔널 컴포넌트

- props 값대로 시각적인 요소를 보여주는 컴포넌트

### 주요 유틸리티

#### combineReducers()

여러 리듀서를 하나로 합친다. 모듈 단위로 여러 리듀서를 만들게 되는데 스토어를 만들때는 여러 리듀서를 하나로 합쳐서 전달한다.

#### <Provider>

리액트 앱에 리덕스 스토어를 연동한다.

#### connect()

리액트 컴포넌트(컨테이너 컴포넌트)에 리덕스 스토어에 있는 상태나 액션 함수를 연동한다. `mapStateToProps()` 함수와 `mapDispatchToProps()` 함수와 함께 사용하여 연동할 상태와 액션 함수를 전달해줘야 한다.

#### mapStateToProps()

`connect()` 함수로 컴포넌트와 스토어를 연동할 때, 어떤 상태들을 연동할지 정의한다.

#### mapDispatchToProps()

`connect()` 함수를 컴포넌트와 스토어를 연동할 때, 어떤 액션 함수들을 연동할지 정의한다.

## 연습 코드

- [Redux Counter Vanilla Example](./counter-vanilla) : 바닐라 자바스크립트로 redux 사용
- [리덕스를 리액트와 함께 사용하기](./velopert-react-redux) : [Redux (3) 리덕스를 리액트와 함께 사용하기](https://velog.io/@velopert/Redux-3-%EB%A6%AC%EB%8D%95%EC%8A%A4%EB%A5%BC-%EB%A6%AC%EC%95%A1%ED%8A%B8%EC%99%80-%ED%95%A8%EA%BB%98-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0-nvjltahf5e) 예제 코드

## 참고 자료

- [Redux 또는 MobX 를 통한 상태 관리](https://velog.io/@velopert/series/redux-or-mobx)
- [Redux Basic Tutorial](https://redux.js.org/basics/basic-tutorial)
- [Redux Counter Vanilla Example](https://github.com/reduxjs/redux/tree/master/examples/counter-vanilla)
- [Ducks: Redux Reducer Bundles](https://github.com/erikras/ducks-modular-redux)
