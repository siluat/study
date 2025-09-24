# Memo

> 구조화하지 않은 단순 나열 메모

- Rust에서 변수는 불변이 기본이다. 이는 Rust가 제공하는 안정성과 동시성의 장점을 취할 수 있는 코드를 작성하도록 하는 수많은 넛지(nudge) 중의 하나이다.
- 변수 섀도잉을 허용한다. `let spaces = "   "; let spaces = spaces.len();` 같은 표현을 허용함으로써, `spaces_str`, `spaces_num` 같은 변수 사용을 할 필요가 없도록 한다. 섀도잉 허용으로 인해 발생할 수 있는 실수는 `let`으로 재할당하는 경우에만 섀도잉이 가능하다는 것으로 방지한다.
- 표현식(expression)은 `;`으로 끝나지 않는다.

  ```rust
  // This is an expression, it returns 4
  {
      let x = 3;
      x + 1
  }

  // This is not an expression, it returns ()
  {
      let x = 3;
      x + 1;
  }
  ```

- 함수는 암묵적으로 본문의 마지막 표현식의 값을 반환한다.

  ```rust
  // This is correct
  fn foo(x: i32) -> i32 {
      x + 1
  }

  // This is incorrect and produces a compile error
  fn foo(x: i32) -> i32 {
      x + 1;
  }

  // Using return keyword
  fn foo(x: i32) -> i32 {
      return x + 1;
  }
  ```

- 소유권의 주요 목표는 힙 데이터의 관리이다.
- Ownership Rules

  - Each value in Rust has an owner.
  - There can only be one owner at a time.
  - When the owner goes out of scope, the value will be dropped.

- Heap 영역의 변수값을 암묵적으로 복사하거나 이중 참조하지 않는다. '이동(move)' 시킨다. 이는 동일 heap 영역을 중복 해제하는 것을 방지하기 위함이다. 기존 변수는 무효화가 되기 때문에 복사라고 표현하지 않고 이동이라고 표현한다.
  ```rust
  let s1 = String::from("hello");
  let s2 = s1;
  println!("{s1}, world!"); // error!: s1 is invalidated reference
  ```
- 새로운 값을 할당하면 기존 값은 즉시 drop한다. (내부적으로 `drop`이라는 메모리 해제 처리를 위한 함수가 호출된다.)

  ```rust
  let mut s = String::from("hello");
  s = String::from("ahoy");
  println!("{s}, world!"); // ahoy, world!
  ```

- 명시적으로 복사할 수 있다.
  ```rust
  let s1 = String::from("hello");
  let s2 = s1.clone();
  println!("s1 = {s1}, s2 = {s2}");
  ```

- 러스트는 절대 자동으로 '깊은' 복사로 데이터를 복사하는 일이 없다.

- 참조자(reference) `&`를 사용하여 소유권을 이전하지 않고도 값만 대여(borrow)할 수 있다. 대여한 값을 수정할 수는 없다.

- 가변 참조자 (mutable reference) `&mut`를 사용하여 대여한 값을 수정할 수 있다. 단 어떤 값에 대한 가변 참조자가 있다면, 그 값에 대한 참조자를 만들 수 없다는 제약이 있다.

- 어떤 값에 대한 불편 참조자가 있는 동안 같은 값의 가변 참조자를 만드는 것도 불가능하다.

- 슬라이스(slice)를 사용하여 컬렉션의 연속된 일련의 요소를 참조할 수 있다. 참조자의 일종으로 소유권을 갖지 않는다.

# Method

- 함수와 유사하지만,
  - 구조체 컨텍스트와 정의되고 열거형이나 트레이트 객체 안에 정의되기도 한다.
  - 첫 번째 매개변수가 항상 `self`다.

# Automatic referencing and dereferencing

- 객체의 메서드를 호출하면 러스트에서 자동으로 해당 메서드의 시그니처에 맞도록 `&`, `&mut`, `*`를 추가한다.
- 자동 참조 동작은 메서드의 수신자(`self`의 타입)가 명확하기 때문에 가능하다.
- 메서드의 수신자를 러스트에서 암묵적으로 빌린다는 점은 실제로 소유권을 인체공학적으로 만드는 중요한 부분이다.

# Associated Functions

- `impl` 블록 내에 정의하지만 `self`를 첫 매개변수로 갖지 않는 함수다.
- 연관 함수는 구주체의 새 인스턴스를 반환하는 생성자로 자주 활용된다.

# `impl` Block

- 각 구조체는 여러 개의 `impl` 블록을 가질 수 있다.

```rust
impl Rectangle {
    fn area(&self) -> u32 {
        self.width * self.height
    }
}

impl Rectangle {
    fn can_hold(&self, other: &Rectangle) -> bool {
        self.width > other.width && self.height > other.height
    }
}
```

# Option Enum

- 널 값을 널이 아닌 값처럼 사용하려고 할 때 여러 종류의 에러가 발생할 수 있다. 널의 문제는 실제 개념에 있기보다, 특정 구현에 있다.
- 러스트에는 널(null) 개념이 없다. 러스트에서는 값의 존재 혹은 부재의 개념을 표현할 때 Option Enum을 사용한다. 

```rust
enum Option<T> {
  None,
  Some(T),
}

# `match`

- `match`는 모든 가능한 경우를 다루어야 한다. 모든 경우를 다루지 않으면 컴파일 에러가 발생한다. -> 안전을 높이는 장치가 된다.

# The module system of Rust

- Packages: A Cargo feature that lets you build, test, and share crates
- Crates: A tree of modules that produces a library or executable
- Modules and use: Let you control the organization, scope, and privacy of paths
- Paths: A way of naming an item, such as a struct, function, or module
- 러스트에서는 모든 아이템(함수, 메서드, 구조체, 열거형, 모듈, 상수 등)이 기본적으로 부모 모듈에 대해 비공개다. `pub` 키워드를 사용하여 공개할 수 있다.
- 부모 모듈 내 아이템은 자식 모듈 내 비공개 아이템을 사용할 수 없지만, 자식 모듈 내 아이템은 부모 모듈 내 아이템을 사용할 수 있다.
- 라이브러리 크레이트를 공유할 계획이라면, 공개 API의 변경사항을 관리할 때 고려해야 할 사항이 많다. [러스트 API 가이드라인](https://rust-lang.github.io/api-guidelines/)을 참고할 수 있다.
- 러스트 모듈 트리에서 아이템을 참조할 때는 절대 경로와 상대 경로를 사용할 수 있다. 무엇을 선택하든 자유지만 변경사항이 발생할 때 코드 수정 범위를 최소화할 수 있는 선택을 하는 것이 좋다.
- `super`를 사용하여 부모 모듈로부터 시작되는 상대 경로를 만들 수 있다.
- 구조체는 필드마다 공개 여부를 정할 수 있다. 구조체를 공개로 지정해도 필드는 기본적으로 비공개다.
- 열거형은 공개로 지정할 경우 모든 배리언트가 공개된다.
- 열거형은 배리언트가 공개되지 않는다면 큰 쓸모가 없고, 구조체의 경우 필드를 공개로 하지 않는 것이 종종 유용하기 때문이다.
- `use` 키워드를 사용하여 단축경로를 만들 수 있고, 스코프 안쪽 어디서라도 짧은 경로로 아이템에 접근할 수 있다.
- 이름이 같은 두개의 타입을 동일한 스코프에 가져오려면 부모 모듈을 명시해서 사용하거나 `as` 키워드를 사용해서 새 이름으로 사용할 수 있다.
- `pub use`를 사용하여 가져온 아이템을 다른 곳에서 가져갈 수 있도록 공개할 수 있다. (re-exporting)
- 예전 스타일의 모듈 구조에서는 ~mod.rs~ 파일에 모듈을 정의했지만, 여러 파일의 이름이 ~mod.rs~로 끝나게 되어, 에디터에서 이 파일들을 동시에 열어두었을 때 헷갈릴 수 있다는 점 때문에 ~mod.rs~가 아닌 모듈 이름과 동일하게 만들어서 사용하는 스타일이 생겼다. 러스트 컴파일러는 두 스타일을 모두 지원하지만 동시에 사용할 수는 없다.

# Common Collections

- A ~vector~ allows you to store a variable number of values next to each other.
- A ~string~ is a collection of characters. We’ve mentioned the `String` type previously, but in this chapter we’ll talk about it in depth.
- A `hash map` allows you to associate a value with a specific key. It’s a particular implementation of the more general data structure called a `map`.
- To learn about the other kinds of collections provided by the standard library, see [the documentation](https://doc.rust-lang.org/std/collections/index.html).

# String

- 러스트 문자열은 숫자 인덱싱(e.g. `s[0]`)을 지원하지 않는데, 공식 문서에 의하면 이유는 다음과 같다.
  - UTF-8 문자열의 바이트 안의 인덱스는 유효한 유니코드 스칼라 값과 항상 대응되지는 않는다. (언어마다 문자열의 바이트 수가 다르기 때문)
  - 러스트는 문자열을 보는 세 가지 방식이 있다. 바이트, 스칼라 값, 문자소 클러스터이다. 데이터가 담고 있는 것이 무슨 언어든 상관없이 각 프로그램이 필요로 하는 통역방식을 선택할 수 있도록 한다.
  - 일반적으로 인덱스 연산은 상수 시간(O(1))에 실행될 것으로 기대받지만, 문자열 내에 유효한 문자가 몇 개 있는지 알아내기 위해 내용물을 시작 지점부터 인덱스로 지정된 곳까지 훓어야 하기 때문에 상수 시간을 보장할 수 없다.
- 문자열 슬라이스(e.g. `s[0..3]`)는 가능하지만, 런타임에 유효하지 않은 인덱스를 참조하지 않도록 주의해서 사용해야 한다.
- 문자열 조각에 대한 연산을 하는 가장 좋은 방법은 `chars` 메서드나 `bytes` 메서드를 사용하는 것이다.
  - 문자를 원하는 것이라면 `chars` 메서드를 사용한다.
  - 바이트를 원하는 것이라면 `bytes` 메서드를 사용한다.

```rust
for c in "Зд".chars() {
    println!("{c}");
}

for b in "Зд".bytes() {
    println!("{b}");
}
```

# Error Handling

- 러스트는 에러를 복구 가능한 에러와 복구 불가능한 에러 두 가지 범주로 묶는다.
- 복구 불가능한 에러는 `panic!`으로 처리한다.
- `panic!` 발생시 프로그램은 함수 스택을 거꾸로 훑어가면서 데이터를 청소하는 되감기(unwinding)를 시도한다. 되감기 대신 그만두기(aborting) 방식을 설정해서, 프로젝트 결과 바이너리를 더 작게 만들 수 있다.
- 복구 가능한 에러는 `Result<T, E>` 타입으로 처리한다.
- `Result` 타입의 `unwrap` 메서드와 `expect` 메서드를 사용해서 패닉을 위한 숏컷을 사용할 수 있다. 프로덕션급 품질의 코드에서 대부분의 러스타시안은 `unwrap`보다 `expect`를 선택하여 해당 연산이 항시 성공한다고 기대하는 이유에 대한 더 많은 맥락을 제공한다.
- `?` 연산자를 사용해서 에러를 함수를 호출한 쪽으로 전파하는 숏컷을 사용할 수 있다.
- `main` 함수도 `Reuslt<(), E>` 타입을 반환하도록 정의할 수 있고, `main` 함수 내에서 `?` 연산자를 사용하여 에러를 전파할 수도 있다.
- 언제 `panic!`을 써야 하고 언제 `Result`를 반환할지 고민된다면, 공식 문서의 [일반적인 가이드라인](https://doc.rust-kr.org/ch09-03-to-panic-or-not-to-panic.html)을 참고해보자.

# Generic Types

- 제너릭 타입의 사용이 구체적인 타입을 사용했을 때와 비교해서 전혀 느려지지 않는다. 컴파일 타임에 제네릭 코드가 호출된 곳을 전부 찾고 제너릭 코드가 호출될 때 사용된 구체 타입으로 코드를 생성하는 단형성화(monomorphization) 과정을 진행하기 때문이다.

# Traits

- 약간의 차이는 있지만, 트레이트는 다른 언어에서 흔히 인터페이스(interface) 라고 부르는 기능과 유사하다.
- 트레이트나 트레이트를 구현할 타입 둘 중 하나는 반드시 자신의 크레이트 것이어야 해당 타입에 대한 트레이트를 구현할 수 있다. 외부 타입에 외부 트레이트를 구현하도록 허용하면 동일한 타입에 동일한 트레이트를 구현할 수 있게 되어 컴파일러는 어떤 구현체를 이용해야 할지 알 수 없게 된다.
- 트레이트의 메서드에 기본 동작을 제공할 수도 있고, 특정 타입의 트레이트 구현시 기본 동작을 유지할지 혹은 오버라이드 할지 선택할 수 있다.
- 함수의 매개변수로 트레이트를 명시하여 특정 트레이트를 구현한 타입의 매개변수만을 받도록 구현할 수 있다.
- 매개변수로 트레이트를 명시할 때 매개변수 수가 적다면 `impl Trait` 문법이 유용하지만 매개변수가 많을 때는 trait bound 문법 사용이 더 유용할 수 있다.

# Lifetimes

- 러스트의 모든 참조자는 라이프타임이라는 참조자의 유효성을 보장하는 범위를 갖는다.
- 대부분의 상황에서 라이프타임은 암묵적으로 추론된다. 하지만 참조자의 수명이 여러 방식으러 서로 연관될 수 있는 경우에는 라이프타임을 명시해 주어야 한다.
- 라이프타임의 주목적은 댕글링 참조(dangling reference)를 방지하는 것이다.
- 러스트 컴파일러는 대여 검사기 (borrow checker) 로 스코프를 비교하여 대여의 유효성을 판단한다. 이 때 특정 변수의 유효 범위를 라이프타임이라고 한다.
- 컴파일러가 라이프타임을 암묵적으로 추론할 수 없는 경우, 명시적으로 라이프타임을 명시해 주어야 한다.
- 라이프타임을 명시한다고 해서 참조자의 수명이 바뀌지 않는다. 어떤 값이 제약 조건을 지키지 않았을 때 대여 검사기가 불합격 판정을 내릴 수 있도록 명시할 뿐입이다.
- 구조체가 참조자를 들고 있도록 할 수 있다. 이 경우 구조체의 정의 내 모든 참조자에 라이프타임을 명시해야 한다.
- 컴파일러는 세 개의 규칙을 사용하여 참조자의 라이프타임을 알아내고, 라이프타임을 알 수 없는 참조자가 있다면 컴파일러는 에러와 함께 작동을 멈춘다. 세 개의 규칙에 대해서는 [공식 문서](https://doc.rust-lang.org/book/ch10-03-lifetime-syntax.html#lifetime-elision)를 참고하자.
- 프로그램의 전체 생애주기 동안 살아있음을 의미하는 정적 라이프타임(static lifetime)이 있고 `'static`으로 명시할 수 있다. 모든 문자열 리터럴은 `'static` 라이프타임을 가진다.
- 정적 라이프타임을 이용하라는 제안이 담긴 에러 메시지를 볼 수 있다. 하지만 정말 정적 라이프타임이 필요한 곳인지, 라이프타임을 올바르게 사용해야 하는 것인지 고민해야 한다.

# Automated Tests

- 러스트에는 API 문서에 작성해 놓은 예제 코드도 컴파일하여 테스트할 수 있는 기능이 있다. 작성한 코드와 문서의 내용이 달라지지 않도록 유지보수하는 데에 매우 유용하다.
- `aasert_eq!`, `assert_ne!` 매크로는 각각 내부적으로 `==`, `!=` 연산자를 사용하고 단언에 실패할 경우 디버그 형식으로 출력한다. 따라서 비교할 값은 `PartialEq`, `Debug` 트레이트를 구현해야 한다.
- `#[should_panic]` 속성을 사용하여 패닉 발생 여부를 테스트할 수 있다. 패닉 원인을 좁히기 위해 `expected` 매개변수를 추가하여 포함되어야 하는 실패 메시지의 일부 또는 전체를 지정할 수 있다.
- 테스트 함수의 반환값을 `Result<T, E>`로 설정하여 테스트할 수도 있다. 단, `Err` 배리언트 반환 검증에는 `assert!(value.is_err())`를 사용해야 한다.
- `cargo run` 명령어가 코드를 컴파일하고 생성된 바이너리를 실행하는 것과 마찬가지로, `cargo test` 명령어는 코드를 테스트 모드에서 컴파일하고 생성된 바이너리를 실행한다.
- 테스트로 생성된 바이너리의 기본 동작은 모든 테스트를 병렬로 실행하고 테스트가 수행되는 동안 발생된 출력을 캡처하는 것으로, 출력이 표시되는 것을 막고 테스트 결과와 관련된 출력을 읽기 편하게 해 준다.
- 커맨드 라인 옵션을 지정하여 테스트 기본 동작을 변경할 수 있다.
- 테스트에 `ignore` 속성을 어노테이션하여 기본 실행에서 제외할 수 있다. 테스트 명령어 실행시 `-- --ignored` 명령어를 사용해서 무시된 테스트만 실행할 수 있다. `cargo test -- --include-ignored` 명령어로 모든 테스트를 실행할 수 있다.
- 러스트 커뮤니티는 테스트를 크게 유닛 테스트 (unit test, 단위 테스트라고도 함), 통합 테스트 (integration test) 두 종류로 나눈다. 유닛 테스트는 한 번에 하나의 모듈만 테스트하며, 모듈의 비공개 인터페이스도 테스트할 수 있다. 통합 테스트는 완전히 라이브러리 외부에 위치하며, 작성한 라이브러리를 외부 코드에서 사용할 때와 똑같은 방식을 사용한다. 하나의 테스트에서 잠재적으로 여러 모듈이 사용되기도 한다.
- 유닛 테스트의 목적은 각 코드 단위를 나머지 코드와 분리하여, 제대로 작동하지 않는 코드가 어느 부분인지 빠르게 파악하는 것이다.
- 유닛 테스트는 src 디렉터리 내의 각 파일에 테스트 대상이 될 코드와 함께 작성한다. 각 파일에 tests 모듈을 만들고 cfg(test)를 어노테이션하는 게 일반적인 관례이다.
- `#[cfg(test)]` 어노테이션은 해당 코드가 `cargo build` 명령어가 아닌 `cargo test` 명령어 실행시에만 컴파일 및 실행될 것을 러스트에게 전달한다. 통합 테스트는 별도 디렉터리에 위치하므로 어노테이션이 필요 없다.
- 비공개 함수를 테스트하는 것에는 많은 논쟁이 있다. 일단 러스트의 비공개 규칙은 비공개 함수를 테스트하도록 허용한다.
- 통합 테스트의 목적은 라이브러리의 여러 부분을 함께 사용했을 때 제대로 작동하는지 확인하는 것이다.
- 통합 테스트 경로 내의 파일은 모두 통합 테스트 파일로 취급된다. 테스트 코드가 아닌 공유 코드를 관리하고 싶다면, `tests/common/mod.rs` 같은 규칙으로 파일을 생성하여 서브 모듈을 만들어 사용해야 한다.
