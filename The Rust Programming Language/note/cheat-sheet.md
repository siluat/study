# My cheat sheet for Rust

## Cargo

```shell
# create new project
cargo new {project_name}

# Run the project
cargo run
```

## Syntax

### Variables

```rust
let x = 5;  // variables are immutable by default
let mut y = 5;  // mutable variables are declared with `mut`
```

### Constants

```rust
const THREE_HOURS_IN_SECONDS: u32 = 60 * 60 * 3;
// MUST be annotated with a type, SHOULD be in SCREAMING_SNAKE_CASE
```

### Data Types

```rust
// Integer Types, i32 is the default integer type
let i: i32 = 1; // i8, i16, i32, i64, i128, isize, u8, u16, u32, u64, u128, usize

// Floating-Point Types, f64 is the default floating-point type
let f: f64 = 1.0; // f32, f64

// The Boolean Type
let b: bool = true; // bool

// The Character Type
let c: char = 'a'; // char

// The Tuple Type
let tup: (i32, f64, u8) = (500, 6.4, 1);
let (x, y, z) = tup;
let five_hundred = tup.0;
let six_point_four = tup.1;
let one = tup.2;

// The Array Type
let a = [1, 2, 3, 4, 5];
// Equals to `let a: [i32; 5] = [1, 2, 3, 4, 5];`
// `let a = [3; 5];` equals to `let a = [3, 3, 3, 3, 3];`
let first = a[0];
let second = a[1];
```
