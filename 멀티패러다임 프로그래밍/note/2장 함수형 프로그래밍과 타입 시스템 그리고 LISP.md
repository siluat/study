# 2장 함수형 프로그래밍과 타입 시스템 그리고 LISP

## 2.1 타입 추론과 함수 타입 그리고 제네릭

*요약 생략*

## 2.2 멀티패러다임 언어에서 함수형 타입 시스템

### 2.2.1 이터레이션 프로토콜과 타입 다시 보기

### 2.2.2 함수형 고차 함수와 타입 시스템

**forEach와 타입**

```ts
// forEach 함수와 타입
function forEach<A>(f: (a: A) => void, iterable: Iterable<A>): void {
  for (const a of iterable) {
    f(a);
  }
}

const array = [1, 2, 3];
forEach(a => console.log(a.toFixed(2)), array);
// 1.00
// 2.00
// 3.00
```

**map과 타입**

```ts
// 제네레이터로 구현한 map 함수와 타입
function* map<A, B>(f: (a: A) => B, iterable: Iterable<A>): IterableIterator<B> {
  for (const a of iterable) {
    yield f(a);
  }
}

const array = ['1', '2', '3'];
const mapped = map(a => parseInt(a), array);
const array2: number[] = [...mapped];
console.log(array2);
// [1, 2, 3]

const [head] = map(a => a.toUpperCase(), ['a', 'b', 'c']);
console.log(head);
// A
```

**filter와 타입**

```ts
// fitler와 타입
function* filter<A>(f: (a: A) => boolean, iterable: Iterable<A>): IterableIterator<A> {
  for (const a of iterable) {
    if (f(a)) {
      yield a;
    }
  }
}

const array = [1, 2, 3, 4];
const filtered = filter(a => a % 2 === 0, array);

const array2: number[] = [...filtered];
console.log(array2); 
// [2, 4]
```

**reduce와 타입**

```ts
// reduce 함수와 타입
function reduce<A, Acc>(
  f: (acc: Acc, a: A) => Acc, acc: Acc, iterable: Iterable<A>
): Acc {
  for (const a of iterable) {
    acc = f(acc, a);
  }
  return acc;
}

const array = [1, 2, 3];
const sum = reduce((acc, a) => acc + a, 0, array);
console.log(sum); 
// 6

const strings = ['a', 'b', 'c'];
const abc = reduce((acc, a) => `${acc}${a}`, '', strings);
console.log(abc);
// abc
```

**reduce 함수 오버로드**

함수 오버로드를 사용해서 초깃값을 생략할 수 있는 reduce 함수 구현

```ts
// reduce(f, iterable);
function baseReduce<A, Acc>(
  f: (acc: Acc, a: A) => Acc, acc: Acc, iterator: Iterator<A>
): Acc {
  while (true) {
    const { done, value } = iterator.next();
    if (done) break;
    acc = f(acc, value);
  }
  return acc;
}

function reduce<A, Acc>(
  f: (acc: Acc, a: A) => Acc, acc: Acc, iterable: Iterable<A>
): Acc;
function reduce<A, Acc>(
  f: (a: A, b: A) => Acc, iterable: Iterable<A>
): Acc;

function reduce<A, Acc>(
  f: (a: Acc | A, b: A) => Acc,
  accOrIterable: Acc | Iterable<A>,
  iterable?: Iterable<A>
): Acc {
  if (iterable === undefined) {
    const iterator = (accOrIterable as Iterable<A>)[Symbol.iterator]();
    const { done, value: acc } = iterator.next();
    if (done) throw new TypeError("'reduce' of empty iterable with no initial value");
    return baseReduce(f, acc, iterator) as Acc;
  } else {
    return baseReduce(f, accOrIterable as Acc, (iterable as Iterable<A>)[Symbol.iterator]());
  }
}

// 첫 번째 reduce 함수: 초깃값을 포함한 예제
const array = [1, 2, 3];
const sum = reduce((acc, a) => acc + a, 0, array);
console.log(sum); // 6

const strings = ['a', 'b', 'c'];
const abc = reduce((acc, a) => `${acc}${a}`, '', strings);
console.log(abc); // abc

// 두 번째 reduce 함수: 초깃값을 포함하지 않은 예제
const array2 = [1, 2, 3];
const sum2 = reduce((a, b) => a + b, array2);
console.log(sum2); // 6

const words = ['hello', 'beautiful', 'world'];
const sentence = reduce((a, b) => `${a} ${b}`, words);
console.log(sentence); // hello beautiful world

const array3 = [3, 2, 1];
const str = reduce((a, b) => `${a}${b}`, array3);
console.log(str); // 321
```

### 2.2.3 함수 시그니처와 중첩된 함수들의 타입 추론

*요약 생략*
