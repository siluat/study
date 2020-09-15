class Promise {
  constructor(executor) {
    this.state = 'PENDING';
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
    if (this.state !== 'PENDING') return;
    this.state = 'FULFILLED';
    this.value = value;
  }

  reject(value) {
    if (this.state !== 'PENDING') return;
    this.state = 'REJECTED';
    this.value = value;
  }

  then(onFulfilled, onRejected) {}
}
