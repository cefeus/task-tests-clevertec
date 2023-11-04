package ru.clevertec.product.mapper.impl;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.mapper.ProductMapperImpl;
import ru.clevertec.product.util.TestProductBuilder;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperImplTest {

    private ProductMapper productMapper = new ProductMapperImpl();

    private static Stream<UUID> paramUuid() {
        return Stream.of(
                UUID.fromString("b915eae0-430f-4c02-ac99-c2a3cdccddfa"),
                UUID.fromString("e824c72b-da38-4a5d-9ddd-a0c22bc063c9")
        );
    }

    @ParameterizedTest
    @MethodSource("paramUuid")
    @NullSource
    @Test
    void toProduct_shouldReturnProductWithoutUUID_IndependentlyOfUUIDValue(UUID uuid) {
        // given
        Product expected = TestProductBuilder.builder()
                .withUuid(uuid)
                .build().buildProduct();
        ProductDto productDto = TestProductBuilder.builder().build().buildProductDto();

        // when
        Product actual = productMapper.toProduct(productDto);

        //then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Product.Fields.name, expected.getName())
                .hasFieldOrPropertyWithValue(Product.Fields.description, expected.getDescription())
                .hasFieldOrPropertyWithValue(Product.Fields.price, expected.getPrice());
    }

    @Test
    void toInfoProductDto_shouldReturnInfoProductDto_withPassedProductFields() {
        // given
        Product productToPass = TestProductBuilder.builder().build().buildProduct();
        InfoProductDto expected = TestProductBuilder.builder().build().buildInfoProductDto();

        // when
        InfoProductDto actual = productMapper.toInfoProductDto(productToPass);

        // then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.uuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.name())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.description())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.price());
            assertThat(actual.uuid())
                    .isNotNull();
            assertThat(actual.name())
                    .matches(TestProductBuilder.PRODUCT_NAME_REGEX);
            assertThat(actual.description())
                    .isNotNull();
            assertThat(actual.price())
                    .isNotNull()
                    .isGreaterThanOrEqualTo(BigDecimal.ZERO);
        });

    }

    @Test
    void merge_shouldReturnProductWithoutChangingDateAndUUID() {
        // given
        Product consumedProduct = TestProductBuilder.builder().build().buildProduct();
        ProductDto consumedProductDto = TestProductBuilder.builder().build().buildProductDto();

        // when
        Product actual = productMapper.merge(consumedProduct, consumedProductDto);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Product.Fields.uuid, consumedProduct.getUuid())
                .hasFieldOrPropertyWithValue(Product.Fields.name, consumedProductDto.name())
                .hasFieldOrPropertyWithValue(Product.Fields.description, consumedProductDto.description())
                .hasFieldOrPropertyWithValue(Product.Fields.price, consumedProductDto.price())
                .hasFieldOrPropertyWithValue(Product.Fields.created, consumedProduct.getCreated());
    }
}