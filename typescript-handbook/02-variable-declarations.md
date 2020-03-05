## Variable Declarations

변수 선언

### var, let, const

자바스크립트와 동일하다.

### Destructuring

코드로 보는 것이 빠르다.
ECMAScript 2015의 그것과 같다.

#### Array destructuring

```ts
let input = [1, 2];
let [first, second] = input;
console.log(first); // outputs 1
console.log(second); // outpus 2

// swap variables
[first, second] = [second, first];

function f([first, second]: [number, number]) {
  console.log(first);
  console.log(second);
}
f([1, 2]);

let [first, ...rest] = [1, 2, 3, 4];
console.log(first); // outputs 1
console.log(rest); // outputs [ 2, 3, 4 ]

let [first] = [1, 2, 3, 4];
console.log(first); // outputs 1

let [, second, , fourth] = [1, 2, 3, 4];
console.log(second); // outputs 2
console.log(fourth); // outputs 4
```

#### Tuple destructuring

배열과 동일하지만, 튜플 요소들의 타입이 따라 가는 차이점이 있다.

```ts
let tuple: [number, string, boolean] = [7, "hello", true];
let [a, b, c] = tuple; // a: number, b: string, c: boolean

let [a, b, c, d] = tuple; // Error, no element at index 3

let [a, ...bc] = tuple; // bc: [string, boolean]
let [a, b, c, ...d] = tuple; // d: [], the empty tuple

let [a] = tuple; // a: number
let [, b] = tuple; // b: string
```

#### Object destructuring

```ts
let o = {
  a: "foo",
  b: 12,
  c: "bar"
};
let { a, b } = o;

// 프로퍼티의 이름을 바꿔서 할당할 수도 있다.
let { a: newName1, b: newName2 } = o;
console.log(newName1); // outputs "foo"
console.log(newName2); // outputs 12

// 위 코드를 보면 타입스크립트의 타입 선언과 혼동할 수도 있을 것 같다.
// 타입 선언이 필요한 경우 다음처럼 한다.
let { a, b }: { a: string; b: number } = o;
```

기본값을 명시할 수도 있다.

```ts
function keepWholeObject(wholeObject: { a: string; b?: number }) {
  let { a, b = 1001 } = wholeObject;
}
```

- wholeObject.b의 값이 유요하지 않을 경우 b의 기본값이 1001이 된다.
- 함수 파라미터에서 `b?`는 b가 선택적(optional)이라는 것을 명시한다.

#### Function declarations

디스트럭처링은 함수 선언에도 사용할 수 있다.

```ts
type C = { a: string; b?: number };
function f({ a, b }: C): void {
  // ...
}

function f({ a = "", b = 0 } = {}): void {
  // ...
}
f();

function f({ a, b = 0 } = { a: "" }): void {
  // ...
}
f({ a: "yes" }); // ok, default b = 0
f(); // ok, default to { a: "" }, which then defaults b = 0
f({}); // error, 'a' is required if you supply an argument
```

디스트럭처링을 여러번 중첩해서 사용하는 등의 잘못된 사용은 코드를 이해하기 어렵게 만들기 때문에, 디스트럭처링을 사용할 때에는 작고 간결하게 사용하길 권장한다.

#### Spread

디스트럭처링의 반대 연산

```ts
let first = [1, 2];
let second = [3, 4];
let bothPlus = [0, ...first, ...second, 5];
console.log(bothPlus); // outputs [0, 1, 2, 3, 4, 5]

// 순서에 주의! Spread는 왼쪽에서 오른쪽 순서대로 진행된다.
let defaults = { food: "spicy", price: "$$", ambiance: "noisy" };
let search = { ...defaults, food: "rich" };
// search is { food: "rich", price: "$$", ambiance: "noisy" }
let defaults = { food: "spicy", price: "$$", ambiance: "noisy" };
let search = { food: "rich", ...defaults };
// search is { food: "spicy", price: "$$", ambiance: "noisy" }
```

객체 스프레드에는 두 가지 한계가 있다.

첫째, 객체를 스프레드할 경우 메서드는 유실된다.

```ts
class C {
  p = 12;
  m() {}
}
let c = new C();
let clone = { ...c };
clone.p; // ok
clone.m(); // error!
```

둘째, 일반 함수의 파라미터에는 스프레드를 허용하지 않는다. 차후 버전에서는 가능해질 수도 있다.
