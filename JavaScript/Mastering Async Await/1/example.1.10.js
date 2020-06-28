// example 1.10
// async 함수에서 항상 직접 promise를 반환할 필요는 없다. 자바스크립트 런타임이 알아서 한다.

async function computeValue() {
  // 아래 Promise.resolve()는 불필요한 코드입니다.
  // "불필요한 코드가 생각만큼 해롭지는 않지만, 그것이 필요할 수도 있다는 잘못된 신호를 주게 됩니다." - Paul Graham
  return Promise.resolve('Hello, World!');
}
