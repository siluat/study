package productimporter;

import java.util.stream.StreamSupport;

public final class ProductSynchronizer {

    private ProductImporter importer;
    private ProductValidator validator;
    private ProductInventory inventory;

    public ProductSynchronizer(ProductImporter importer, ProductValidator validator, ProductInventory inventory) {
        this.importer = importer;
        this.validator = validator;
        this.inventory = inventory;
    }

    public void run() {
        StreamSupport.stream(importer.fetchProducts().spliterator(), false).filter(validator::isValid)
                .forEach(inventory::upsertProduct);
    }
}
