package productimporter.suppliers.starkindustries;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import productimporter.DomainArgumentsSource;
import productimporter.Product;

public class StarkIndustriesProductImporter_specs {

    @ParameterizedTest
    @DomainArgumentsSource
    void sut_projects_all_products(StarkIndustriesProduct[] sourceProducts, Product[] products) {
        // Arrange
        var productSource = mock(StarkIndustriesProductSource.class);
        when(productSource.getAllProducts()).thenReturn(Arrays.asList(sourceProducts));

        var translator = mock(StarkIndustriesProductTranslator.class);

        var sut = new StarkIndustriesProductImporter(productSource, translator);

        // Act
        Iterable<Product> actual = sut.fetchProducts();

        // Assert
        assertThat(actual).hasSize(sourceProducts.length);
    }

    @ParameterizedTest
    @DomainArgumentsSource
    void sut_correctly_translates_source_products(StarkIndustriesProduct[] sourceProducts, Product[] products) {
        // Arrange
        var productSource = mock(StarkIndustriesProductSource.class);
        when(productSource.getAllProducts()).thenReturn(Arrays.asList(sourceProducts));

        var translator = mock(StarkIndustriesProductTranslator.class);

        List<Tuple> tuples = IntStream.range(0, Math.min(sourceProducts.length, products.length))
                .mapToObj(index -> Tuple.tuple(sourceProducts[index], products[index])).collect(Collectors.toList());
        for (Tuple tuple : tuples) {
            Object[] values = tuple.toArray();
            when(translator.translate((StarkIndustriesProduct) values[0])).thenReturn((Product) values[1]);
        }

        var sut = new StarkIndustriesProductImporter(productSource, translator);

        // Act
        Iterable<Product> actual = sut.fetchProducts();

        // Assert
        assertThat(actual).containsExactly(products);
    }

}
