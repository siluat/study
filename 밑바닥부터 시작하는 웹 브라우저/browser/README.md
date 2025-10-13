# browser

실습으로 만드는 웹 브라우저

## 사용 기술

- Python 3

## 테스트

```bash
make test
```

## 실행

### Chapter 1 버전

```bash
python3 src/lab1.py https://example.org/
python3 src/lab1.py https://browser.engineering/
```

- 기능
  - 명령어에 전달한 URL에 해당하는 응답에서 태그를 제외한 모든 텍스트를 출력한다.
- 특징
  - http, https 스킴만 지원한다.
  - HTTP 1.0 프로토콜만 지원한다.
  - 태그를 제외한 텍스트를 출력하는 기능 외에는 아무 기능도 없다.

### Chapter 2 버전

```bash
python3 src/lab2.py https://example.org/
python3 src/lab2.py https://browser.engineering/
```

- 기능
  - 명령어에 전달한 URL에 해당하는 응답에서 태그를 제외한 모든 텍스트를 GUI 화면으로 출력한다.
  - 키보드의 '아래 화살표 키'를 누르면 페이지 아래로 스크롤 된다.
- 특징
  - http, https 스킴만 지원한다.
  - HTTP 1.0 프로토콜만 지원한다.
  - 태그를 제외한 텍스트를 출력하는 기능 외에는 아무 기능도 없다.
  

### Chapter 3 버전

```bash
python3 src/lab3.py https://browser.engineering/
```

- 기능
  - 명령어에 전달한 URL에 해당하는 응답에서 태그를 제외한 모든 텍스트를 GUI 화면으로 출력한다.
  - `<b>`, `<i>`, `<small>`, `<big>` 태그 사용시 스타일 및 크기를 적용하여 텍스트를 출력한다.
  - `<br>`, `<p>` 태그 사용시 줄바꿈 처리를 한다.
  - 키보드의 '아래 화살표 키'를 누르면 페이지 아래로 스크롤 된다.
- 특징
  - http, https 스킴만 지원한다.
  - HTTP 1.0 프로토콜만 지원한다.
  - 태그를 제외한 텍스트를 출력하는 기능 외에는 아무 기능도 없다.

### Chapter 4 버전

```bash
python3 src/lab4.py https://browser.engineering/
```

- 기능
  - 명령어에 전달한 URL에 해당하는 응답에서 태그를 제외한 모든 텍스트를 GUI 화면으로 출력한다.
  - `<b>`, `<i>`, `<small>`, `<big>` 태그 사용시 스타일 및 크기를 적용하여 텍스트를 출력한다.
  - `<br>`, `<p>` 태그 사용시 줄바꿈 처리를 한다.
  - 키보드의 '아래 화살표 키'를 누르면 페이지 아래로 스크롤 된다.
- 특징
  - http, https 스킴만 지원한다.
  - HTTP 1.0 프로토콜만 지원한다.
  - 태그를 제외한 텍스트를 출력하는 기능 외에는 아무 기능도 없다.
  - HTML 문서를 문서 트리로 구조화하여 처리한다.

### Chapter 5 버전

```bash
python3 src/lab5.py https://browser.engineering/
```

- 기능
  - 명령어에 전달한 URL에 해당하는 응답에서 태그를 제외한 모든 텍스트를 GUI 화면으로 출력한다.
  - `<b>`, `<i>`, `<small>`, `<big>` 태그 사용시 스타일 및 크기를 적용하여 텍스트를 출력한다.
  - `<br>`, `<p>` 태그 사용시 줄바꿈 처리를 한다.
  - 키보드의 '위, 아래 화살표 키'를 누르면 페이지 위, 아래로 스크롤 된다.
  - 블록 엘리먼트와 인라인 엘리먼트를 구분하여 기본적인 규칙으로 배치할 수 있다.
  - `<pre>` 태그 사용시 해당 블록 엘리먼트에 회색 배경을 적용한다.
- 특징
  - http, https 스킴을 지원한다.
  - HTTP 1.0 프로토콜을 지원한다.
  - HTML 문서를 문서 트리로 구조화하여 처리한다.
  - 레이아웃 트리를 구성하여 각 요소의 크기와 위치를 계산한다.
