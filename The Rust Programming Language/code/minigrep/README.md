# minigrep

## Commands

### Search

```sh
❯ cargo run -- to poem.txt
   Compiling minigrep v0.1.0 (/Users/siluat/Projects/study/The Rust Programming Language/code/minigrep)
    Finished `dev` profile [unoptimized + debuginfo] target(s) in 0.16s
     Running `target/debug/minigrep to poem.txt`
Are you nobody, too?
How dreary to be somebody!
```

### Case-Insensitive search

```sh
❯ IGNORE_CASE=1 cargo run -- to poem.txt
    Finished `dev` profile [unoptimized + debuginfo] target(s) in 0.02s
     Running `target/debug/minigrep to poem.txt`
Are you nobody, too?
How dreary to be somebody!
To tell your name the livelong day
To an admiring bog!
```
