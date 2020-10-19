# 1장 OAuth 2.0이 무엇이고, 왜 관심을 가져야 할까?

OAuth 2.0은 자신이 소유한 리소스에 소프트웨어 애플리케이션이 접근할 수 있도록 허용해줌으로써 **접근 권한을 위임해주는 프로토콜**이다.

> [RFC 6749](https://tools.ietf.org/html/rfc6749)  
> The OAuth 2.0 authorization framework enables a third-party application to obtain limited access to an HTTP service, either on behalf of a resource owner by orchestrating an approval interaction between the resource owner and the HTTP service, or by allowing the third-party application to obtain access on its own behalf.

## OAuth 2.0이 아닌 것

OAuth는,

- HTTP 프로토콜과 독립적으로 정의되지 않는다.
- 인증 프로토콜이 아니다.
- 사용자 간의 권한 위임 메커니즘은 정의하지 않는다.
- 인가 절차 메커니즘을 정의하지 않는다.
- 토큰의 포맷을 정의하지 않는다.

OAuth 2.0은,

- OAuth 1.0과 달리, 암호화 방법을 정의하지 않는다.
- 단일 프로토콜이 아니다.

## 요약

- OAuth는 **토큰을 어떻게 획득하고 그것을 어떻게 사용**하는지에 대한 스펙이다.
- OAuth는 인가 접근 시스템을 위한 권한 위임 프로토콜이다.
- OAuth는 보다 안전하고 유용한 권한 위임 프로토콜로 비밀번호 공유 패턴을 대체한다.
- OAuth는 작은 문제들을 잘 해결하는 것에 초점을 맞추고 있으며, 이를 통해 보다 큰 보안 시스템에서 적합한 구성 요소가 될 수 있다.