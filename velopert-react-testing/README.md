# velopert-react-testing

[벨로퍼트와 함께하는 리액트 테스팅](https://velog.io/@velopert/series/react-testing)을 보고 리액트 테스팅 환경 살펴보기

## 2. 자바스크립트 테스팅의 기초

> 코드 : [/velopert-react-testing/react-testing-2](/velopert-react-testing/react-testing-2)

- [Jest](https://jestjs.io/)를 사용
- @types/jest를 설치하면 VS Code에서 인텔리센스 지원을 받을 수 있다.
- `test`와 `it`의 작동방식은 완전히 동일

## 3. TDD의 소개

> 코드 : [/velopert-react-testing/react-testing-3](/velopert-react-testing/react-testing-3)

## 5. Enzyme을 사용한 리액트 컴포넌트 테스트

> 코드 : [/velopert-react-testing/react-testing-5](/velopert-react-testing/react-testing-5)

### 스냅샷 테스팅

- 렌더링된 결과가 이전에 렌더링한 결과와 일치하는지 확인하는 작업
- Enzyme에서 스냅샷 테스팅을 하려면 enzyme-to-json 필요

```js
describe("<Profile />", () => {
  it("matches snapshot", () => {
    const wrapper = mount(<Profile username="siluat" name="siluat" />);
    expect(wrapper).toMatchSnapshot();
  });
});
```

- `mount`함수는 Enzyme 을 통하여 리액트 컴포넌트를 렌더링
- `wrapper`를 통해서 props 조회, DOM 조회, state 조회 등이 가능

### props 접근

```js
it("renders username and name", () => {
  const wrapper = mount(<Profile username="siluat" name="siluat" />);
  expect(wrapper.props().username).toBe("siluat");
  expect(wrapper.props().name).toBe("siluat");
});
```

### DOM 확인

```js
it("renders username and name", () => {
  const wrapper = mount(<Profile username="siluat" name="siluat" />);

  const boldElement = wrapper.find("b");
  expect(boldElement.contains("siluat")).toBe(true);
  const spanElement = wrapper.find("span");
  expect(spanElement.text()).toBe("(siluat)");
});
```

- `find` 함수에는 Query selector를 사용할 수 있다. 컴포넌트 이름으로 특정 컴포넌트의 인스턴스도 찾을 수 있다.

### 클래스형 컴포넌트의 테스팅

클래스형 컴포넌트의 내부 메서드 호출 및 state 조회를 이용한 테스트

```js
it("matches snapshot", () => {
  const wrapper = shallow(<Counter />);
  expect(wrapper).toMatchSnapshot();
});
it("has initial number", () => {
  const wrapper = shallow(<Counter />);
  expect(wrapper.state().number).toBe(0);
});
it("increases", () => {
  const wrapper = shallow(<Counter />);
  wrapper.instance().handleIncrease();
  expect(wrapper.state().number).toBe(1);
});
it("decreases", () => {
  const wrapper = shallow(<Counter />);
  wrapper.instance().handleDecrease();
  expect(wrapper.state().number).toBe(-1);
});
```

- `mount` 대신 `shallow`를 사용. `shallow`는 `mount`와 다르게 컴포넌트 내부에 또다른 리액트 컴포넌트가 있다면 이를 렌더링하지 않음
- 또한 `mount`에서는 최상위 요소가 해당 컴포넌트인 반면, `shallow`에서는 최상위 요소가 div이다. 그래서 wrapper.props()로 조회하는 결과가 다르다.
- 컴포넌트의 state를 조회할 때는 `state()` 함수를, 내장 메서드를 호출할 때는 `instance()` 함수를 사용

### DOM 이벤트 시뮬레이트

내장 메서드를 호출이 아닌 이벤트를 시뮬레이트하여 테스트

```js
it("calls handlesIncrease", () => {
  const wrapper = shallow(<Counter />);
  const plusButton = wrapper.findWhere(
    node => node.type() === "button" && node.text() === "+1"
  );
  plusButton.simulate("click");
  expect(wrapper.state().number).toBe(1);
});
```

- `findWhere` 함수와 `simulate` 함수를 사용

### 함수형 컴포넌트와 Hooks 테스팅

- Hook을 사용하는 경우 `shallow`가 아닌 `mount`를 사용
  - `shallow`에서는 useEffect가 작동하지 않고,
  - 이벤트 시뮬레이트를 의도대로 테스트할 수 없다.

```js
it("increases", () => {
  const wrapper = mount(<HookCounter />);
  let plusButton = wrapper.findWhere(
    node => node.type() === "button" && node.text() === "+1"
  );
  plusButton.simulate("click");
  plusButton.simulate("click");

  const number = wrapper.find("h2");

  expect(number.text()).toBe("2");
});
```
