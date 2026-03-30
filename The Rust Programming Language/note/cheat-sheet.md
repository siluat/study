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

### Function

```rust
fn plus_one(x: i32) -> i32 {
    x + 1
}
```

### Control Flow

```rust
// Using if in a let statement
let n = if condition { 5 } else { 6 };

// loop
let result = loop {
    // 'continue', 'break' and 'return' keywords are available
    // 'break' returns the value from the loop
}

// loop label
'loop_name: loop {
    loop {
        break; // break current loop
        break 'loop_name; // break outer loop
    }
}

// white
while condition {
    // ...
}

// for loop with an array
let a = [10, 20, 30, 40, 50];
for element in a {
    // ...
}

// for loop with a range
for number in 1..4 {
    // ...
}
```

### Struct

```rust
struct User {
    active: bool,
    username: String,
    email: String,
    sign_in_count: u64,
}
```

### Struct update syntax

```rust
let user2 = User {
    email: String::from("another@example.com"),
    ..user1
}
```

### Tuple Struct

```rust
struct Color(i32, i32, i32);
struct Point(i32, i32, i32);

let black = Color(0, 0, 0);
let origin = Point(0, 0, 0);
```

### Unit-Like Struct

```rust
struct AlwaysEqual;
```

### Derive Debug

```rust
#[derive(Debug)]
struct Rectangle {
    width: u32,
    height: u32,
}
```

### Debug Print

```rust
println!("{:#?}", rect1);
```

### Debug Print with dbg!

```rust
let scale = 2;
let rect1 = Rectangle {
    width: dbg!(30 * scale),
    height: 50,
};

dbg!(&rect1);
```

### Associated Functions

```rust
struct Rectangle {
    width: u32,
    height: u32,
}

impl Rectangle {
    fn square(size: u32) -> Self {
}

let sq = Rectangle::square(3);
```

### Enum

#### Basic

```rust
enum IpAddrKind {
    V4,
    V6,
}

let four = IpAddrKind::V4;
let six = IpAddrKind::V6;
```

#### with Data

```rust
enum IpAddr {
    V4(u8, u8, u8, u8),
    V6(String),
}

let home = IpAddr::V4(127, 0, 0, 1);
let loopback = IpAddr::V6(String::from("::1"));
```

#### with Struct

```rust
enum IpAddr {
    V4(Ipv4Addr),
    V6(Ipv6Addr),
}
```

#### with Multiple Types Variant

```rust
enum Message {
    Quit,
    Move { x: i32, y: i32 },
    Write(String),
    ChangeColor(i32, i32, i32),
}
```

#### with Method

```rust
enum Message {
    // ...
}

impl Message {
    fn call(&self) {
        // ...
    }
}

let m = Message::Write(String::from("hello"));
m.call();
```

### `match`

```rust
enum Coin {
    Penny,
    Nickel,
    Dime,
    Quarter,
}

fn value_in_cents(coin: Coin) -> u8 {
    match coin {
        Coin::Penny => 1,
        Coin::Nickel => 5,
        Coin::Dime => 10,
        Coin::Quarter => 25,
    }
}
```

#### Pattern That Bind to Values

```Rust

#[derive(Debug)]
enum UsState {
    Alabama,
    Alaska,
    // --snip--
}
enum Coin {
    Penny,
    Nickel,
    Dime,
    Quarter(UsState),
}

fn value_in_cents(coin: Coin) -> u8 {
    match coin {
        Coin::Penny => 1,
        Coin::Nickel => 5,
        Coin::Dime => 10,
        Coin::Quarter(state) => {
            println!("State quarter from {state:?}!");
            25
        }
    }
}
```

#### Matching with `Option<T>`

```rust
fn plus_one(x: Option<i32>) -> Option<i32> {
    match x {
        None => None,
        Some(i) => Some(i + 1),
    }
}

let five = Some(5);
let six = plus_one(five);
let none = plus_one(None);
```

#### Catch-All Patterns and the `_` Placeholder

```rust
let dice_roll = 9;
match dice_roll {
    3 => add_fancy_hat(),
    7 => remove_fancy_hat(),
    other => move_player(other),
}
```

```rust
let dice_roll = 9;
match dice_roll {
    3 => add_fancy_hat(),
    7 => remove_fancy_hat(),
    _ => reroll(),
}
```

```rust
let dice_roll = 9;
match dice_roll {
    3 => add_fancy_hat(),
    7 => remove_fancy_hat(),
    _ => (),
}
```

### `if let`

```rust
let config_max = Some(3u8);
if let Some(max) = config_max {
    println!("The maximum is configured to be {}", max);
}
```

### Traits

#### Define

```rust
pub trait Summary {
    fn summarize(&self) -> String;
}
```

#### Implement

```rust
impl Summary for NewsArticle {
    fn summarize(&self) -> String {
        format!("{}, by {} ({})", self.headline, self.author, self.location)
    }
}
```

#### Default Implementation

```rust
pub trait Summary {
    fn summarize(&self) -> String {
        String::from("(Read more...)")
    }
}
```

#### Default Implementation with Method

```rust
pub trait Summary {
    fn summarize_author(&self) -> String;

    fn summarize(&self) -> String {
        format!("(Read more from {}...)", self.summarize_author())
    }
}
```

#### Trait as Parameters

```rust
pub fn notify(item: &impl Summary) {
    println!("Breaking news! {}", item.summarize());
}
```

#### Trait Bound Syntax

```rust
pub fn notify<T: Summary>(item: &T) {
    println!("Breaking news! {}", item.summarize());
}
```

#### `impl Trait` vs Trait Bound

```rust
pub fn notify(item1: &impl Summary, item2: &impl Summary) {}
```

```rust
pub fn notify<T: Summary>(item1: &T, item2: &T) {}
```

#### Multiple Trait Bounds with `+` syntax

```rust
pub fn notify(item: &(impl Summary + Display)) {}
```

```rust
pub fn notify<T: Summary + Display>(item: &T) {}
```

#### `where` Clauses

```rust
pub fn some_function<T, U>(t: &T, u: &U) -> i32
where
    T: Display + Clone,
    U: Clone + Debug,
```

#### Returning Types That Implement Traits

```rust
fn returns_summarizable() -> impl Summary {}
```

#### Using Trait Bounds to Conditionally Implement Methods

```rust
struct Pair<T> {}

impl<T> Pair<T> {}

impl<T: Display + PartialOrd> Pair<T> {}

impl<T: Display> ToString for T {}
```

### Closure

```rust
let add_one_v2 = |x: u32| -> u32 { x + 1 };
let add_one_v3 = |x|             { x + 1 };
let add_one_v4 = |x|               x + 1  ;
```

## Module

- Start from the crate root: When compiling a crate, the compiler first looks in the crate root file (usually src/lib.rs for a library crate or src/main.rs for a binary crate) for code to compile.
- Declaring modules: In the crate root file, you can declare new modules; say you declare a “garden” module with `mod garden;`. The compiler will look for the module’s code in these places:
    - Inline, within curly brackets that replace the semicolon following `mod garden`
    - In the file ~src/garden.rs~
    - In the file ~src/garden/mod.rs~
- Declaring submodules: In any file other than the crate root, you can declare submodules. For example, you might declare `mod vegetables;` in ~src/garden.rs~. The compiler will look for the submodule’s code within the directory named for the parent module in these places:
    - Inline, directly following `mod vegetables`, within curly brackets instead of the semicolon
    - In the file ~src/garden/vegetables.rs~
    - In the file ~src/garden/vegetables/mod.rs~
- Paths to code in modules: Once a module is part of your crate, you can refer to code in that module from anywhere else in that same crate, as long as the privacy rules allow, using the path to the code. For example, an `Asparagus` type in the garden vegetables module would be found at `crate::garden::vegetables::Asparagus`.
- Private vs. public: Code within a module is private from its parent modules by default. To make a module public, declare it with `pub mod` instead of `mod`. To make items within a public module public as well, use `pub` before their declarations.
- The `use` keyword: Within a scope, the `use` keyword creates shortcuts to items to reduce repetition of long paths. In any scope that can refer to `crate::garden::vegetables::Asparagus`, you can create a shortcut with `use crate::garden::vegetables::Asparagus;` and from then on you only need to write `Asparagus` to make use of that type in the scope.

## Standard Collections

- [Module collections](https://doc.rust-lang.org/std/collections/index.html)

### String

#### Creating a New String

```rust
let mut s1 = String::new();

let s2 = "initial contents".to_string();

let s3 = String::from("initial contents");
```

#### Updating a String

```rust
let mut s = String::from("foo");
s.push_str("bar");
```

#### Concatenation with `+` Operators

```rust
let s1 = String::from("Hello, ");
let s2 = String::from("world!");
let s3 = s1 + &s2;  // fn add(self, s: &str) -> String {
```

#### Concatenation with `format!` Macro

```rust
let s1 = String::from("tic");
let s2 = String::from("tac");
let s3 = String::from("toe");

let s = format!("{s1}-{s2}-{s3}");
```

#### Indexing into Strings

```rust
let s1 = String::from("hello");
let h = s1[0];
```

#### Iterating Through Strings

```rust
for c in "Зд".chars() {
    println!("{c}");
}
// З
// д

for b in "Зд".bytes() {
    println!("{b}");
}
// 208
// 151
// 208
// 180
```

## Error Handling

### Unrecoverable Errors with `panic!`

```rust
panic!("crash and burn");
```

### Recoverable Errors with `Result<T, E>` and `match`

```rust
let greeting_file = match File::open("hello.txt") {
    Ok(file) => file,
    Err(error) => panic!("Problem opening the file: {:?}", error),
};
```

### Recoverable Errors with `unwrap_or_else`

```rust
let greeting_file = File::open("hello.txt").unwrap_or_else(|error| {
    if error.kind() == ErrorKind::NotFound {
        File::create("hello.txt").unwrap_or_else(|error| {
            panic!("Problem creating the file: {:?}", error);
        })
    } else {
        panic!("Problem opening the file: {:?}", error);
    }
});
```

### Recoverable Errors with `unwrap` and `expect` Operator

```rust
let greeting_file = File::open("hello.txt").unwrap();
```

```rust
let greeting_file = File::open("hello.txt")
    .expect("hello.txt should be included in this project");
```

### Error Propagation

```rust
fn read_username_from_file() -> Result<String, io::Error> {
    let username_file_result = File::open("hello.txt");

    let mut username_file = match username_file_result {
        Ok(file) => file,
        Err(e) => return Err(e),
    };

    let mut username = String::new();

    match username_file.read_to_string(&mut username) {
        Ok(_) => Ok(username),
        Err(e) => Err(e),
    }
}
```

### Error Propagation with `?` Operator

```rust
fn read_username_from_file() -> Result<String, io::Error> {
    let mut username_file = File::open("hello.txt")?;
    let mut username = String::new();
    username_file.read_to_string(&mut username)?;
    Ok(username)
}
```

```rust
fn read_username_from_file() -> Result<String, io::Error> {
    let mut username = String::new();
    File::open("hello.txt")?.read_to_string(&mut username)?;
    Ok(username)
}
```

```rust
fn read_username_from_file() -> Result<String, io::Error> {
    fs::read_to_string("hello.txt")
}
```
