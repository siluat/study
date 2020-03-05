## Basic Types

기본 타입

### Boolean, Number, String

타입을 명시한다는 것을 제외하면 자바스크립트와 모든 특징이 동일

```typescript
// Boolean
let isDone: boolean = false;

// Number
let decimal: number = 6;
let hex: number = 0xf00d;
let binary: number = 0b1010;
let octal: number = 0o744;

// String
let color: string = "blue";
color = "red";
```

### Array

```typescript
// 두 가지 방법으로 사용 가능
let list: number[] = [1, 2, 3];
let list: Array<number> = [1, 2, 3];
```

### Tuple

고정된 수와 타입의 배열을 표현할 수 있다.

```typescript
// 튜플 타입을 정의
let x: [string, number];
// 초기화
x = ["hello", 10];
// 잘못된 초기화
x = [10, "hello"]; // 에러
// 3개 이상도 가능
let y: [string, number, boolean];
```

### Enum

```typescript
enum Color {
  Red,
  Green,
  Blue
}
let c: Color = Color.Green;
```

배열의 인덱스처럼 접근하면 이름값을 얻을 수 있는 특징이 있다.

```typescript
enum Color {
  Red = 1,
  Green,
  Blue
}
let colorName: string = Color[2];
console.log(colorName); // 'Green'이 출력된다
```

### Any

어떤 타입이 오게 될지 알 수 없는 경우 사용할 수 있다. 해당 변수에 대해서는 컴파일 시간에 타입 체크를 하지 않는다. 자바스크립트의 변수처럼 여러 타입의 값을 대입할 수 있다.

```typescript
let notSure: any = 4;
notSure = "maybe a string instead";
notSure = false;
```

`Object`와 유사해보일 수 있다. 하지만 `Object` 타입에는 아무 값이나 대입할 수는 있지만, 대입 후 `Object` 타입에 없는 임의의 메서드를 호출할 수 없다.

```typescript
let notSure: any = 4;
notSure.ifItExists(); // 문제 없다
notSure.toFixed(); // 문제 없다

let prettySure: Object = 4;
prettySure.toFixed(); // 에러, number 타입에 'toFixed'가 존재하지만 Object 타입에는 존재하지 않아 에러
```

배열로 사용할 수도 있다.

```typescript
let list: any[] = [1, true, "free"];
list[1] = 100;
```

### Void

다음과 같이 주로 아무 것도 리턴하지 않는 함수에 사용한다.

```typescript
function warnUser(): void {
  console.log("This is my warning message");
}
```

변수의 타입으로도 사용할 수도 있지만, `null`과 `undefined` 두 값만 할당 할 수 있기 때문에 변수 타입으로 사용할만한 일은 없다.

```typescript
let unusable: void = undefined;
unusable = null; // 이 외의 타입의 값은 사용할 수 없다.
```

### Null and Undefined

변수 타입으로써의 `null`과 `undefined`는 `null`과 `undefined` 값 외의 값은 대입할 수 없다. 따라서 `void` 타입과 마찬가지로 변수 타입으로써 사용할만한 일은 없다.

`null`과 `undefined`는 다른 모든 타입의 서브 타입이다. 즉 다른 모든 타입의 변수에 `null`과 `undefined`를 할당할 수 있다.

타입스크립트 컴파일 옵션에서 `--strictNullChecks`을 활성화하면 `null`과 `undefined`를 `any` 타입 변수 또는 각각 `null` 타입, `undefined` 타입의 변수에만 할당할 수 있도록 강제할 수 있다.(예외적으로 `undefined`는 `void` 타입의 변수에도 할당할 수 있다.) 해당 옵션의 사용으로 많은 일반적인 오류들을 피할 수 있다.

> 공식 문서에서는 `--strictNullChecks` 옵션의 사용을 권장하고 있다.

### Never

절대 발생하지 않는 값을 표현할 때 사용한다. 항상 예외를 던지는 함수, 함수의 끝(리턴)에 도달하지 않는 함수 등에 사용한다.

```typescript
// 항상 예외를 던지는 함수
function error(message: string): never {
  throw new Error(message);
}
// 타입 추론 결과 리턴 타입이 never인 함수
function fail() {
  return error("Something failed");
}
// 끝나지 않는 함수
function infiniteLoop(): never {
  while (true) {}
}
```

`never` 타입은 모든 타입의 서브 타입이며 모든 타입의 변수에 할당될 수 있다. 반면에 `never` 타입 변수에는 어떤 타입의 값도 할당할 수 없다.(`never` 타입에 `never`타입을 할당 할 수는 있다.) `any` 타입의 변수에도 `never`는 할당할 수 없다.

### Object

`object`는 원시타입(primitive type)이 아닌 타입을 표현할 때 사용한다.

사용 예로, `object` 타입을 이용해 `Object.create` 같은 API들을 좀 더 잘 표현할 수 있다.

```typescript
declare function create(o: object | null): void;

create({ prop: 0 }); // OK
create(null); // OK

// 원시타입을 사용할 경우 모두 에러
create(42); // Error
create("string"); // Error
create(false); // Error
create(undefined); // Error
```

### Type assertions

`Type assertions`는 다른 언어의 형 변환(tyep cast) 같은 동작을 한다. 하지만 별도의 타입 체크나 데이터 변환을 하지는 않는다. 런타임에 아무 영향을 주지 않고, 오직 컴파일러에 의해서만 처리된다.

`Type assertions`는 두 가지 문법으로 사용할 수 있다.

```typescript
let someValue: any = "this is a string";
// <> 사용
let strLength: number = (<string>someValue).length;
// as 사용
let strLength: number = (someValue as string).length;
```

단, JSX에서 타입스크립트를 사용하는 경우에는 `as`만 사용할 수 있다.
