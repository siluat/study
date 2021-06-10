import { includes } from "lodash";

export class Batch {
  reference: string;
  sku: string;
  eta: Date;

  private purchasedQuantity: number;
  private allocations: OrderLine[] = [];

  constructor(reference: string, sku: string, quantity: number, eta?: Date) {
    this.reference = reference;
    this.sku = sku;
    this.purchasedQuantity = quantity;
    this.eta = eta;
  }

  allocate(line: OrderLine): void {
    if (!includes(this.allocations, line)) {
      this.allocations = [...this.allocations, line];
    }
  }

  getAllocatedQuantity(): number {
    return this.allocations.map((line) => line.quantity).reduce((q) => q);
  }

  getAvailabeQuantity(): number {
    const allocatedQuantity = this.getAllocatedQuantity();
    return this.purchasedQuantity - allocatedQuantity;
  }
}

export class OrderLine {
  orderId: string;
  sku: string;
  quantity: number;

  constructor(orderId: string, sku: string, quantity: number) {
    this.orderId = orderId;
    this.sku = sku;
    this.quantity = quantity;
  }
}
