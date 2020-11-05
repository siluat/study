## Gatsby Unit Testing

- Gatsby에는 테스트 환경이 기본적으로 포함되어 있지 않다. 직접 구성해야 한다.
- Jest 기반의 테스트 환경을 구성할 수 있다.

**의존성 설치**

```shell
yarn add --dev jest babel-jest react-test-renderer babel-preset-gatsby identity-obj-proxy
# or
# npm install --save-dev jest babel-jest react-test-renderer babel-preset-gatsby identity-obj-proxy
```

- jest: 테스팅 프레임워크
- babel-jest: Jest를 위한 Babel 플러그인
- react-test-renderer: 리액트 컴포넌트의 렌더링 테스트 도구
- babel-preset-gatsby: Gatsby 환경을 위한 babel 프리셋
- identity-obj-proxy: 

**Jest 설정 파일 준비**

- Jest 런타임이 babel-jest를 사용하도록 `jest.config.js` 파일과 `jest-preprocess.js` 파일을 생성하여 다음과 같이 설정한다.

  ```js
  // jest.config.js
  module.exports = {
    transform: {
      "^.+\\.jsx?$": `<rootDir>/jest-preprocess.js`,
      // TypeScript 사용시
      // "^.+\\.[jt]sx?$": "<rootDir>/jest-preprocess.js",
    },
    moduleNameMapper: {
      ".+\\.(css|styl|less|sass|scss)$": `identity-obj-proxy`,
      ".+\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$": `<rootDir>/__mocks__/file-mock.js`,
    },
    testPathIgnorePatterns: [`node_modules`, `\\.cache`, `<rootDir>.*/public`],
    transformIgnorePatterns: [`node_modules/(?!(gatsby)/)`],
    globals: {
      __PATH_PREFIX__: ``,
    },
    testURL: `http://localhost`,
    setupFiles: [`<rootDir>/loadershim.js`],
  }
  ```

  ```js
  // jest-preprocess.js
  const babelOptions = {
    presets: ["babel-preset-gatsby"],
    // TypeScript 사용시
    // presets: ["babel-preset-gatsby", "@babel/preset-typescript"],
  }

  module.exports = require("babel-jest").createTransformer(babelOptions)
  ```

- moduleNameMapper: 특정 확장자의 `import` 처리에 대한 규칙을 명시한다. 주로 실제 테스트에 사용할 필요가 없거나 사용하기 어려운 파일을 모킹(mocking)할 목적으로 사용한다. 이 설정에서는 file-mock.js 파일을 만들어 미디어 파일들을 모킹한다.
  ```js
  // __mocks__/file-mock.js
  module.exports = "test-file-stub"
  ```

- testPathIgnorePatterns: 테스트 대상에서 제외할 디렉토리를 설정한다.
- transformIgnorePatterns: Gatsby 모듈에는 트랜스파일되어 있지 않은 ES6 코드가 포함되어 있는데 이 부분이 Jest 실행에 문제가 된다. Jest는 기본적으로 node_modules 디렉터리의 코드는 트랜스파일하지 않기 때문에, 이 설정을 통해 기본 설정을 변환하여 Jest 실행 전에 node_modules/gatsby 디렉토리의 코드를 트랜스파일하도록 할 수 있다.
- globals: Gatsby가 `__PATH_PREFIX__`를 사용한다. `__PATH_PREFIX__`를 전역 변수로 설정한다.
- testURL: 일부 DOM API를 위해 유효한 URL을 설정해야 한다. (e.g. localStorage) Jest 23.5.0 이후부터는 기본값으로 http://localhost가 설정되기 때문에 별도로 설정하지 않아도 무방하다.
- setupFiles: 글로벌로 설정해야 하는 함수가 하나 있는데, jest.config.js 파일에서는 글로벌 함수를 직접 설정할 수 없다. setupFiles 옵션을 통해 모든 테스트가 실행되기 전에 포함할 파일의 목록을 설정할 수 있다. 해당 파일을 통해 테스트 런타임에서의 글로벌 함수를 설정할 수 있다. loadershim.js 파일을 만들고 다음과 같이 작성한다.
  ```js
  // loadershim.js
  global.___loader = {
    enqueue: jest.fn(),
  }
  ```

**Mocking gatsby**

필수는 아니지만 gatsby 모듈을 다음과 같이 모킹해놓으면 편리하다.

```js
// __mocks__/gatsby.js
const React = require("react")
const gatsby = jest.requireActual("gatsby")
module.exports = {
  ...gatsby,
  graphql: jest.fn(),
  Link: jest.fn().mockImplementation(
    // these props are invalid for an `a` tag
    ({
      activeClassName,
      activeStyle,
      getProps,
      innerRef,
      partiallyActive,
      ref,
      replace,
      to,
      ...rest
    }) =>
      React.createElement("a", {
        ...rest,
        href: to,
      })
  ),
  StaticQuery: jest.fn(),
  useStaticQuery: jest.fn(),
}
```
  
**References**

- https://www.gatsbyjs.com/docs/unit-testing/