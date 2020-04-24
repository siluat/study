import "mocha";

describe("Interface", () => {
  it("기본적인 사용", () => {
    interface LabeledValue {
      label: string;
    }

    function printLabel(labeledObj: LabeledValue) {
      console.log(labeledObj.label);
    }

    let myObj = { size: 10, label: "Size 10 Object" };
    printLabel(myObj);
  });
});
