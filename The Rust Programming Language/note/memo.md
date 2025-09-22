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
