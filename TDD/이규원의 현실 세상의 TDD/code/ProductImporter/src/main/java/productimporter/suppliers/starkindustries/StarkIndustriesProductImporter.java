package productimporter.suppliers.starkindustries;

import java.util.ArrayList;

import productimporter.Product;
import productimporter.ProductImporter;

public class StarkIndustriesProductImporter implements ProductImporter {

    private final StarkIndustriesProductSource productSource;
    private StarkIndustriesProductTranslator translator;

    public StarkIndustriesProductImporter(StarkIndustriesProductSource productSource,
            StarkIndustriesProductTranslator translator) {
        this.productSource = productSource;
        this.translator = translator;
    }

    @Override
    public Iterable<Product> fetchProducts() {
        var products = new ArrayList<Product>();
        for (StarkIndustriesProduct s : productSource.getAllProducts()) {
            products.add(translator.translate(s));
        }

        return products;
    }

}
