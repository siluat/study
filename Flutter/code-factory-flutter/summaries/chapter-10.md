# 10장 만난 지 며칠 U&I : 상태 관리, CupertinoDatePicker, Dialog, DateTime

## .of 생성자

`.of(context)`로 정의된 모든 생성자는 일반적으로 BuildContent를 매개변수로 받고 위젯 트리에서 가장 가까이에 있는 상위 객체의 값을 찾아낸다.

```dart
MediaQuery.of(context) // 위젯 트리에서 가장 가까운 MediaQuery 값을 가져온다.
```

## MediaQuery

스크린 크기 정보에 접근할 수 있다.

## ThemeData

플러터가 기본으로 제공하는 대부분의 위젯의 기본 스타일을 지정할 수 있다.

## 느낀 점

- Widget 사용 시 `const` 여부를 직접 관리해야 하는 점이 불편하다. 유사한 경험으로 React의 `useMemo()`, `useCallback()`이 생각난다. 아마도 메모리 관리 등 내부 사정이 있을 것 같지만, 아무튼 사용자 입장에서 불편하다.
- StatefulWidget의 구조는 흥미롭니다. Widget 본체와 State를 분리하도록 강제하고 있어, 개발자가 의식적으로 StatefulWidget과 StatelessWidget을 구분해서 사용하는 것에 조금이라도 더 도움이 되는 것 같기도 하다.
