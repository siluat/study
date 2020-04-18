# MobX

## 기본 개념

### Observable state

MobX에 의해 관찰되고 있는 상태. `observable()` 함수나 `@observable` 데코레이터를 사용해서 특정 데이터를 `Observable State`로 만들 수 있다.
즉, 해당 데이터의 상태 관리를 MobX가 해준다.

### Computed values

정의된 연산대로 만들어지는 값이다. 연산에 필요한 상태값이 변경되었다면 새로 연산하고, 변경된 상태가 없다면 연산을 하지 않고 이전 값을 계속 사용한다.

### Reactions

`Computed values`와 유사하지만, 새 값을 만드는 것이 아니라 상태 변경에 따라 해야할 일들을 정의할 때 사용한다.

### Actions

상태의 변화를 일으키는 행위를 액션이라고 한다. 리덕스와 다르게 액션을 객체로 정의하거나 액션 함수를 사용해서 이벤트를 디스패치하지도 않는다. 그냥 상태가 변하는 코드를 작성하면 그것이 곧 액션을 발생시킨 것이 된다.

## 참고 자료

- [Redux 또는 MobX 를 통한 상태 관리](https://velog.io/@velopert/series/redux-or-mobx)
- [MobX Core concepts](https://mobx.js.org/README.html#core-concepts)
