function solution(n) {
  return parseInt(
    n
      .toString()
      .split("")
      .sort((a, b) => parseInt(b) - parseInt(a))
      .join("")
  );
}
