# 17장 일정 관리 앱 만들기 : table_calendar

- 주요 사용 패키지 및 플러그인

  - table_calendar: 달력 기능
  - intl: 다국어화 기능
  - drift: ORM

- 주요 내용

  - 달력 기반의 일정 목록 UI 구현
  - 일정 입력을 위한 Form field UI 구현

- 기타 기록
  - UI 위젯들을 복잡하게 구성하다보면 적절한 사이즈를 참조할 수 없는 상황이 되기도 하는데, 그럴 때 `IntrinsicHeight`, `Expanded` 등의 위젯으로 감싸주어야 하는 경우가 있다. 그런데 그런 상황을 사전에 구분하는 것이 어렵다. 🤔 또한 해당 상황에 디버깅 콘솔에 출력되는 에러 메시지도 그리 도움이 된다고 느껴지지 않았다.
