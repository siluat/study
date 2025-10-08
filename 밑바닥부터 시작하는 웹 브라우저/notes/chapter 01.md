# Chapter 01 웹페이지 다운로드

## 이론 학습

### URL 구조

- 스킴, 호스트, 경로, 포트, 쿼리, 프래그먼트 등
- 정의: [RFC 3986](https://datatracker.ietf.org/doc/html/rfc3986)

### 서버 연결 과정

```mermaid
sequenceDiagram
    actor User as 사용자
    participant Browser as 브라우저
    participant OS as 로컬 OS
    participant DNS as DNS 서버
    participant Router as 라우터들
    participant Server as 서버

    User->>Browser: URL 입력
    Browser->>OS: 서버와 연결 요청<br/>(호스트명 전달)
    OS->>DNS: 호스트명 조회 요청
    DNS->>DNS: 호스트명을 IP 주소로 변환<br/>(example.org → 93.184.216.34)
    DNS-->>OS: IP 주소 반환<br/>(93.184.216.34)
    OS->>OS: 라우팅 테이블 확인<br/>(최적 하드웨어 선택:<br/>유선/무선)
    OS->>OS: 디바이스 드라이버로<br/>신호 전송
    OS->>Router: 네트워크 신호 전송
    Router->>Router: 최적 경로 선택 및<br/>메시지 전달
    Note over Router: 응답 경로를 위해<br/>출발지 기록
    Router->>Server: 메시지 도착
    Server->>Server: 연결 생성
    Server-->>Browser: 연결 확립
```

### 정보 요청

서버와 연결된 후 브라우저는 HTTP 프로토콜로 요청을 주고받는다.

## 실습

## 웹 브라우저는 URL로 서버의 정보를 다운로드할 수 있어야 한다.

- URL 클래스 구현
  - 스킴 처리
