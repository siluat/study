import { observable, reaction, computed, autorun } from "mobx";

const calculator = observable({
  a: 1,
  b: 2,
});

const sum = computed(() => {
  console.log("계산중이에요!");
  return calculator.a + calculator.b;
});

autorun(() => console.log(`a 값이 ${calculator.a} 로 바뀌었네요!`));
autorun(() => console.log(`b 값이 ${calculator.b} 로 바뀌었네요!`));
autorun(() => sum.get());

calculator.a = 10;
calculator.b = 20;

console.log(sum.value);
console.log(sum.value);

calculator.a = 20;
console.log(sum.value);
