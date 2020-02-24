import "mocha";
import { expect } from "chai";

describe("Tuple", () => {
  it("세 개 이상의 요소 사용", () => {
    let x: [string, number, boolean];
    x = ["5", 15, true];
    expect(x).to.have.lengthOf(3);
  });
});

describe("Enum", () => {
  it("인덱스 사용", () => {
    enum Color {
      Red = 1,
      Green,
      Blue
    }
    let colorName: string = Color[2];
    expect(colorName).to.be.equal("Green");
  });

  describe("Null and Undefined", () => {
    it("undefined, null 상호 대입", () => {
      let u: undefined = undefined;
      let n: null = null;
      expect(u).to.be.equal(undefined);
    });
  });
});
