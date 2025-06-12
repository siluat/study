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
