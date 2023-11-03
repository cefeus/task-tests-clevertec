package ru.clevertec.product.repository.impl;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.util.TestProductBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class InMemoryProductRepositoryTest {

    private InMemoryProductRepository productRepository = new InMemoryProductRepository();

    @Test
    void findById_shouldReturnProduct_whenExists() {
        // given
        UUID passedUuid = UUID.fromString(TestProductBuilder.STR_UUID);
        Optional<Product> expected = Optional.of(TestProductBuilder.builder().build().buildProduct());

        // when
        Optional<Product> actual = productRepository.findById(passedUuid);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void findById_shouldReturnOptionalEmpty_whenNotExist() {
        //  given
        UUID passedUuid = UUID.fromString(TestProductBuilder.STR_UUID);
        Optional<Product> expected = Optional.empty();

        // when
        Optional<Product> actual = productRepository.findById(passedUuid);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void findAll_shouldReturnProductList() {
        // given

        // when
        List<Product> actual = productRepository.findAll();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actual)
                    .hasOnlyElementsOfType(Product.class);
            softly.assertThat(actual)
                    .allSatisfy(inp -> {
                                assertThat(inp)
                                        .extracting(inp.getName())
                                        .matches(i -> i.toString().matches(TestProductBuilder.PRODUCT_NAME_REGEX))
                                        .isNotNull();
                                assertThat(inp).
                                        extracting(inp.getDescription())
                                        .matches(i -> i.toString().matches(TestProductBuilder.PRODUCT_DESCRIPTION_REGEX));
                                assertThat(inp.getPrice())
                                        .isNotNull()
                                        .isGreaterThanOrEqualTo(BigDecimal.ZERO);
                                assertThat(inp)
                                        .isNotNull();
                    });


        });
    }

    @Test
    void save_shouldReturnExactSavedProduct() {
        // given
        Product expected = TestProductBuilder.builder().build().buildProduct();
        // when
        Product actual = productRepository.save(expected);
        // then
        assertEquals(expected, actual);

    }

    @Test
    void delete() {
        // given
        UUID passedUuid = UUID.fromString(TestProductBuilder.STR_UUID);
        // when
        productRepository.delete(passedUuid);
        // then
        verify(productRepository)
                .delete(passedUuid);
    }
}