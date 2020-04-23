# Storybook

> ### Build bulletproof UI components faster
>
> Storybook is an open source tool for developing UI components in isolation for React, Vue, and Angular. It makes building stunning UIs organized and efficient.

## 학습 동기

스토리북을 사용해보지는 않았지만 소개글이나 결과물들을 본 적이 있다. UI 컴포넌트의 실제 랜더링 결과 또는 애니메이션 효과의 룩앤필을 수시로 확인하면서 개발하기에 좋은 도구 같아서 사용법을 익혀보기로 했다.

생각대로의 사용이 가능하다면 마침 시작 중인 사이드 프로젝트에 바로 적용해볼까 한다.

## 스토리

스토리북은 스토리 단위로 컴포넌트를 다룬다. `.stories.js` 확장자로 파일을 생성하고 스토리북이 해당 스토리를 어떻게 다룰지 작성한다.

## 애드온

스토리를 만들고 브라우저에서 확인 하는 것이 스토리북의 기본적인 기능이다. 애드온을 설치하여 더 다양한 기능들을 사용할 수 있다.

### Knobs 애드온

컴포넌트의 `props`를 스토리북 화면에서 바꿔볼 수 있게 하는 애드온이다. 스토리에서 `props`를 제어하는 단위를 `Knobs`라고 볼 수 있는 것 같다.

> [Storybook Addon Knobs](https://github.com/storybookjs/storybook/tree/master/addons/knobs)

### Actions 애드온

컴포넌트의 이벤트 핸들러가 받는 데이터를 볼 수 있게 해준다. 기본적으로 설치가 되어 있어 직접 설치할 필요는 없다.

리액트 라우터의 주소 변경 정보나 리덕스 스토어에 디스패치되는 액션의 정보도 볼 수 있다고 한다.

> [Storybook Addon Actions](https://github.com/storybookjs/storybook/tree/master/addons/actions)

### Docs 애드온

`DocsPage` 또는 `MDX` 사용을 통해 컴포넌트에 대한 문서를 자동 생성해준다.

- `DocsPage`만을 사용해서 표현하기 어려울 때 `MDX`를 사용하면 좋다.
- 컴포넌트가 아닌 문서를 작성할 때 `MDX` 만으로 작성하면 된다.
- 컴포넌트에 대한 문서 작성을 `MDX` 만으로 작성하면 나중에 `TypeScript`를 사용할 경우 IDE에서 `.mdx` 확장자에 대한 `TypeScript` 지원이 제대로 되지 않는 문제가 있다.

> [Storybook Docs](https://github.com/storybookjs/storybook/tree/master/addons/docs)

## React, TypeScript와 함께 사용하기

### 필요 패키지

Storybook, React, TypeScript를 함께 사용하기 위해 다음 패키지들이 필요하다

- babel-preset-react-app
- react-docgen-typescript-loader
- typescript

`.storybook` 경로에 `webpack.config.js` 파일을 만들어 스토리북의 기본 웹팩 설정을 커스터마이징한다.

```js
module.exports = ({ config, mode }) => {
  config.module.rules.push({
    test: /\.(ts|tsx)$/,
    use: [
      {
        loader: require.resolve("babel-loader"),
        options: {
          presets: [["react-app", { flow: false, typescript: true }]],
        },
      },
      require.resolve("react-docgen-typescript-loader"),
    ],
  });
  config.resolve.extensions.push(".ts", ".tsx");
  return config;
};
```

`.storybook/main.js` 파일을 열고, `tsx`도 처리하도록 `stories`에 확장자를 추가한다.

`src/typings.d.ts` 파일을 만들고 `declare module '*.mdx';`를 작성하여 Found Not Module 에러를 방지한다.

## 참고 자료

- [TypeScript와 Storybook을 사용한 리액트 디자인 시스템 구축하기](https://velog.io/@velopert/series/storybook-typescript-design-system)
- [Storybook Addons](https://storybook.js.org/addons/)
