# GatsbyJS

리액트 기반의 정적 사이트 생성 도구, GatsbyJS(이하 Gatsby)의 사용 방법과 원리에 대한 내용을 정리한다.

## 페이지의 자동 생성

Gatsby는 `src/pages` 디렉토리 하위에 있는 React 컴포넌트들을 접근 가능한 URL과 페이지로 자동 변환한다. 예를 들어 `src/pages/index.js`, `src/pages/about.js` 두 컴포넌트가 있다면, 각각 `/`, `/about` 이라는 URL로 접근 가능한 페이지로 해당 컴포넌트가 사용된다.

> 즉, 컴포넌트의 이름과 동일한 주소로 라우팅 처리를 자동으로 한다. Next.js와 동일하다.

## 데이터 처리 과정

### 데이터

Gatsby에서 React 컴포넌트 이외의 모든 것은 데이터이다. Gatsby는 이런 데이터들을 React 컴포넌트들에 집어넣고, 데이터에 따라 페이지를 생성하고 라우팅 처리를 한다.

데이터를 제어하는 방법으로 GraphQL을 사용할 수 있다. GraphQL을 사용하지 않고도 Gatsby를 사용할 수는 있으며, 해당 방법에 대한 공식 문서도 제공하고 있다.

공식 문서에서는 작은 사이트의 경우 GraphQL 사용을 하지 않는 것도 좋은 선택이라고 하고 있다.

> 하지만 나는 GraphQL을 그냥 써야겠다. 내가 본 예제 코드들은 대부분 GraphQL을 사용하는 코드들이었다.

## 참고 자료

- https://www.gatsbyjs.org/docs/recipes/pages-layouts/#creating-pages-automatically