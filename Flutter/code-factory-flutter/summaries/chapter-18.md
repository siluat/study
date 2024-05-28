# 18장 데이터베이스 적용하기 : SQL, SQLite, Drift, Dismissble

## 주요 내용

- Drift 등을 활용해서 SQLite 사용하기
  - 테이블, 데이터베이스 클래스 정의 및 자동 코드 생성
  - 데이터베이스 인스턴스 초기화 처리
  - 앱 코드 내에서 접근하기 쉽도록 get_it을 활용하여 인스턴스 등록
- Form 활용법
  - 사용자 입력값 접근
  - 사용자 입력값 검증
- Dismissible 위젯으로 밀어서 삭제 구현

## 주요 사용 패키지 및 플러그인

- drift: ORM
  - drift_dev, build_runner: Database code generation
- get_it: Service locator
