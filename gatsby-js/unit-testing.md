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
const babelOptions = {
  presets: ["babel-preset-gatsby"],
  // TypeScript 사용시
  // presets: ["babel-preset-gatsby", "@babel/preset-typescript"],
}

module.exports = require("babel-jest").createTransformer(babelOptions)
```

**References**

- https://www.gatsbyjs.com/docs/unit-testing/