package ru.clevertec.product.util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

@Builder(setterPrefix = "with")
@Data
public class TestProductBuilder {

    public static final String STR_UUID = "d03c916f-a036-46e8-ae21-8c454f58b246";
    public static final String PRODUCT_NAME_REGEX = "^[а-яА-ЯЁё ]{5,10}$";
    public static final String PRODUCT_DESCRIPTION_REGEX = "^[а-яА-ЯЁё ]{10,30}$|null";
    private final String DEFAULT_NAME = "Яблоко";
    private final String DEFAULT_DESCRIPTION = "Сочное";
    private final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(1.53);
    private final LocalDateTime DEFAULT_CREATED = LocalDateTime.of(2023, Month.NOVEMBER, 1, 20, 15);

    @Builder.Default
    private UUID uuid = UUID.fromString(STR_UUID);

    @Builder.Default
    private String name = DEFAULT_NAME;

    @Builder.Default
    private String description = DEFAULT_DESCRIPTION;

    @Builder.Default
    private BigDecimal price = DEFAULT_PRICE;

    @Builder.Default
    private LocalDateTime created = DEFAULT_CREATED;

    public Product buildProduct() {
        return new Product(uuid, name, description, price, created);
    }

    public ProductDto buildProductDto() {
        return new ProductDto(name, description, price);
    }

    public InfoProductDto buildInfoProductDto() {
        return new InfoProductDto(uuid, name, description, price);
    }


}
