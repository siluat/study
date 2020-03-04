function solution(arr) {
  var answer = [];
  var min = Math.min(...arr);
  answer = arr.filter(element => element > min);
  return answer.length ? answer : [-1];
}
