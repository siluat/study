package productimporter.suppliers.wayneenterprises;

import java.util.Arrays;

public class WayenEnterprisesProductSourceStub implements WayneEnterprisesProductSource {

    private WayneEnterprisesProduct[] products;

    public WayenEnterprisesProductSourceStub(WayneEnterprisesProduct... products) {
        this.products = products;
    }

    @Override
    public Iterable<WayneEnterprisesProduct> fetchProducts() {
        return Arrays.asList(products);
    }

}