# Rust Glossary

> 참고: 아래 항목들의 각 설명은 LLM을 통해 생성 및 정리된 내용

## cargo

- Rust의 공식 패키지 매니저이자 빌드 시스템
- 프로젝트 생성 및 관리
- 의존성 관리 (Cargo.toml)
- 빌드, 실행, 테스트, 문서화 등 다양한 기능 제공
- `cargo check`: 빠른 컴파일 검사
- `cargo fmt`: 코드 포맷팅
- `cargo clippy`: 린트 도구

## rustc

- Rust의 공식 컴파일러
- LLVM 기반의 최적화된 네이티브 코드 생성
- cargo를 통해 간접적으로 사용
- 컴파일 타임 메모리 안전성 검사
- 크로스 플랫폼 컴파일 지원

## rustup

- Rust의 공식 도구 체인 관리자
- 컴파일러와 패키지 매니저 설치 및 관리
- 여러 Rust 버전 관리
- 크로스 컴파일 지원
- 컴포넌트 관리 (rustfmt, clippy 등)
- 채널 관리 (stable, beta, nightly)
