package ru.clevertec.product.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.util.TestProductBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repositoryMock;
    @Mock
    private ProductMapper mapperMock;
    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<Product> captor;

    @Test
    void get_shouldReturnInfoProductDto_whenExist() {
        // given
        InfoProductDto expected = TestProductBuilder.builder().build().buildInfoProductDto();

        doReturn(expected)
                .when(mapperMock).toInfoProductDto(any(Product.class));
        doReturn(Optional.of(TestProductBuilder.builder().build().buildProduct()))
                .when(repositoryMock).findById(UUID.fromString(TestProductBuilder.STR_UUID));

        // when
        InfoProductDto actual = productService.get(UUID.fromString(TestProductBuilder.STR_UUID));

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAll_shouldReturnInfoProductList() {
        // given
        List<Product> products = List.of(
                TestProductBuilder.builder().build().buildProduct(),
                TestProductBuilder.builder()
                        .withUuid(UUID.fromString("b915eae0-430f-4c02-ac99-c2a3cdccddfa"))
                        .withName("Яблоко")
                        .withDescription("Садовое")
                        .withPrice(BigDecimal.valueOf(1.55))
                        .build().buildProduct(),
                TestProductBuilder.builder()
                        .withUuid(UUID.fromString("e824c72b-da38-4a5d-9ddd-a0c22bc063c9"))
                        .withName("Груша")
                        .withDescription("Дачная")
                        .withPrice(BigDecimal.valueOf(1.57))
                        .build().buildProduct()
        );
        List<InfoProductDto> expected = List.of(
                TestProductBuilder.builder().build().buildInfoProductDto(),
                TestProductBuilder.builder()
                        .withUuid(UUID.fromString("b915eae0-430f-4c02-ac99-c2a3cdccddfa"))
                        .withName("Яблоко")
                        .withDescription("Садовое")
                        .withPrice(BigDecimal.valueOf(1.55))
                        .build().buildInfoProductDto(),
                TestProductBuilder.builder()
                        .withUuid(UUID.fromString("e824c72b-da38-4a5d-9ddd-a0c22bc063c9"))
                        .withName("Груша")
                        .withDescription("Дачная")
                        .withPrice(BigDecimal.valueOf(1.57))
                        .build().buildInfoProductDto()
        );

        doReturn(products)
                .when(repositoryMock).findAll();

        doReturn(expected.get(0))
                .when(mapperMock).toInfoProductDto(products.get(0));
        doReturn(expected.get(1))
                .when(mapperMock).toInfoProductDto(products.get(1));
        doReturn(expected.get(2))
                .when(mapperMock).toInfoProductDto(products.get(2));

        // when
        List<InfoProductDto> actual = productService.getAll();

        // then
        verify(repositoryMock)
                .findAll();
        assertThat(actual)
                .hasSameClassAs(expected);
    }

    @Test
    void create_shouldInvokeRepoSave() {
        // given
        Product productToSave = TestProductBuilder.builder()
                .withUuid(null)
                .build().buildProduct();
        Product expected = TestProductBuilder.builder().build().buildProduct();
        ProductDto dto = TestProductBuilder.builder().build().buildProductDto();

        doReturn(expected)
                .when(repositoryMock).save(productToSave);
        doReturn(productToSave)
                .when(mapperMock).toProduct(dto);

        // when
        productService.create(dto);

        // then
        verify(repositoryMock)
                .save(captor.capture());
    }

    @Test
    void update_shouldInvokeRepositorySave() {
        // given
        ProductDto passedDto = TestProductBuilder.builder().build().buildProductDto();
        UUID uuid = UUID.fromString(TestProductBuilder.STR_UUID);
        Product passedProduct = TestProductBuilder.builder().build().buildProduct();

        doReturn(passedProduct)
                .when(mapperMock).toProduct(passedDto);
        doReturn(passedProduct)
                .when(repositoryMock).save(passedProduct);
        // when
        productService.update(uuid, passedDto);

        // then
        verify(repositoryMock)
                .save(passedProduct);
    }

    @Test
    void delete() {
        // given
        UUID uuid = UUID.fromString(TestProductBuilder.STR_UUID);

        // when
        productService.delete(uuid);

        // then
        verify(repositoryMock).
                delete(uuid);
    }
}