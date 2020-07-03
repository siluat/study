# Funtional Component

## React.FC 사용할까 말까

함수형 컴포넌트 정의에 `React.FC` 사용하면,

- props에 children이 기본적으로 들어간다. 묵시적인 동작이라는 측면에서 문제라고 볼 수도 있다.
- defaultProps 사용을 통한 기본값 설정이 동작하지 않는다.

> 현재로서는 React.FC 없이 함수형 컴포넌트를 사용하는 것이 더 좋아 보인다.
