class Promise {
  constructor(executor) {
    this.state = "PENDING";
    this.chained = [];
    this.value = undefined;
    try {
      executor(
        (v) => this.resolve(v),
        (err) => this.reject(err)
      );
    } catch (err) {
      this.reject(err);
    }
  }

  resolve(value) {
    if (this.state !== "PENDING") return;

    Object.assign(this, { value, state: "FULFILLED" });

    this.chained
      .filter((obj) => obj.onFulfilled instanceof Function)
      .forEach((obj) => setImmediate(obj.onFulfilled, value));
  }

  reject(value) {
    if (this.state !== "PENDING") return;

    Object.assign(this, { value, state: "REJECTED" });
    ㄱ;

    this.chained
      .filter((obj) => obj.onRejected instanceof Function)
      .forEach((obj) => setImmediate(obj.onFulfilled, value));
  }

  then(onFulfilled, onRejected) {
    const { value, state } = this;

    // 정착 상태라면 상태에 맞는 핸들러를 이벤트 루프의 이벤트 큐에 넣는다
    if (state === "FULFILLED") return setImmediate(onFulfilled, value);
    if (state === "REJECTED") return setImmediate(onRejected, value);

    // 정착 상태가 아니라면 나중에 실행될 수 있도록 핸들러를 보관
    this.chained.push({ onFulfilled, onRejected });
  }
}

export default Promise;
