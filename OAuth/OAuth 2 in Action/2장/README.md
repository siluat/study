# 2장 OAuth 2.0의 기본

## **표준 OAuth 트랜잭션**

1. 클라이언트는 인가 서버의 리소스 소유자에게 인가를 요청한다.
2. 리소스 소유자는 클라이언트를 인가한다.
3. 클라이언트는 인가 서버로부터 토큰을 전달받는다.
4. 클라이언트는 보호된 리소스에 접근하기 위해 토큰을 사용한다.

> 여러 단계를 하나의 작업으로 묶어 절차를 최적화하는 경우가 많다.

## OAuth 2.0 인가 그랜트(Authorization Grant) 절차

```mermaid
sequenceDiagram
  autonumber
  participant RO as 리소스 소유자
  participant C as 클라이언트
  participant AE as 인가 서버: 인가 엔드 포인트
  participant TE as 인가 서버: 토큰 엔드 포인트
  participant RS as 보호된 리소스
  C-->>RO: 클라이언트는 사용자 에이전트를 인가 엔드 포인트로 리다이렉트시킨다.
  RO->>AE: 사용자 에이전트는 인가 엔드 포인트를 로드한다.
  RO->AE: 리소스 소유자가 인가 서버에 인증을 수행한다.
  RO->AE: 리소스 소유자가 클라이언트를 인가한다.
  AE-->>RO: 인가 서버는 사용자 에이전트를 클라이언트로 리다이렉트시키면서 인가 코드를 전달한다.
  RO->>C: 사용자 에이전트는 인가 코드로 클라이언트의 리다이렉트 URI를 로드한다.
  C->>TE: 클라이언트는 인가 코드와 자신의 자격 증명 정보를 토큰 엔드 포인트에 전달한다.
  TE->>C: 인가 서버는 액세스 토큰을 클라이언트에게 전달한다.
  C->>RS: 클라이언트는 액세스 토큰을 보호된 리소스에 전달한다.
  RS->>C: 보호된 리소스는 클라이언트에게 요청된 리소스를 전달한다.
```
