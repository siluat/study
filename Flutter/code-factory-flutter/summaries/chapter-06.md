# 06장 기본 위젯 알아보기

## 위젯 소개

> 'Everything is a Widget' - 구글에서 플러터를 소개하는 문구입니다.

위 내용을 공식 문서에서 찾아보다가 다음 문구를 공식 문서에서 발견했다.

> Flutter widgets are built using a modern framework that takes inspiration from React. - [Building user interfaces with Flutter](https://docs.flutter.dev/ui)

React 컴포넌트를 생각하며 플러터 위젯을 구경할 생각을 하니 기대가 된다.

그리고 2024년 5월 기준, 실제로 발견한 문구는 다음과 같다.

> In Flutter, almost everything is a widget - [Layouts in Flutter](https://docs.flutter.dev/ui/layout)

## 위젯의 기본 특징

- 플러터에서 화면에 보여지는 UI와 관련된 모든 요소는 위젯으로 구성
- 위젯은 상태를 기반으로 UI 구현을 정의
- 위젯은 상태가 변경되면 변경 사항에 맞게 UI를 화면에 다시 그려준다.
- 플러터 프레임워크는 **최소한의 변경 사항을 산출**해서 화면에 그려낸다.

## 제스처

- 사용자가 키보드로 글자를 입력하는 행위 외의 모든 입력을 제스처라고 부른다.
- GestureDetector 위젯은 모든 제스처를 매개변수로 제공해준다.

## 위젯 실습

_책의 안내에 따라 기본 위젯 실습을 진행했다._

## 느낀 점

- 위젯의 레이아웃 및 스타일 설정이 React에서 Panda CSS를 사용하는 감각과 유사하다고 느껴졌다. 해당 경험이 위젯 사용에 적응하는 데 도움이 될 것 같다.
