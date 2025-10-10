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
python3 src/lab1.py http://example.org/
python3 src/lab1.py http://browser.engineering/http.html
python3 src/lab1.py https://example.org/
python3 src/lab1.py https://browser.engineering/http.html
```

- 기능
  - 명령어에 전달한 URL에 해당하는 응답에서 태그를 제외한 모든 텍스트를 출력한다.
- 특징
  - http, https 스킴만 지원한다.
  - HTTP 1.0 프로토콜만 지원한다.
  - 태그를 제외한 텍스트를 출력하는 기능 외에는 아무 기능도 없다.

### Chapter 2 버전

```bash
python3 src/lab2.py http://example.org/
python3 src/lab2.py http://browser.engineering/http.html
python3 src/lab2.py https://example.org/
python3 src/lab2.py https://browser.engineering/http.html
```

- 기능
  - 명령어에 전달한 URL에 해당하는 응답에서 태그를 제외한 모든 텍스트를 GUI 화면으로 출력한다.
- 특징
  - http, https 스킴만 지원한다.
  - HTTP 1.0 프로토콜만 지원한다.
  - 태그를 제외한 텍스트를 출력하는 기능 외에는 아무 기능도 없다.
  - 키보드의 '아래 화살표 키'를 누르면 페이지 아래로 스크롤 된다.
