import { Batch, OrderLine } from "./model";

describe("batches", () => {
  test("allocating to a batch reduces the available quantity", () => {
    const batch = new Batch("batch-001", "SMALL-TABLE", 20, new Date());
    const line = new OrderLine("order-ref", "SMALL-TABLE", 2);

    batch.allocate(line);

    expect(batch.getAvailabeQuantity()).toBe(18);
  });
});
