package ru.clevertec.product.service.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.service.ProductService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository productRepository;

    @Override
    public InfoProductDto get(UUID uuid) {
        Optional<Product> retrievedProduct = productRepository.findById(uuid);

        if (retrievedProduct.isEmpty())
            throw new ProductNotFoundException(uuid);

        return mapper.toInfoProductDto(retrievedProduct.get());
    }

    @Override
    public List<InfoProductDto> getAll() {
        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(product -> mapper.toInfoProductDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public UUID create(ProductDto productDto) {
        Product productToCreate = mapper.toProduct(productDto);
        return productRepository.save(productToCreate).getUuid();
    }

    @Override
    public void update(UUID uuid, ProductDto productDto) {
        Product productToUpdate = mapper.toProduct(productDto);
        productToUpdate.setUuid(uuid);

        try {
            productRepository.save(productToUpdate);
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException(uuid);
        }
    }

    @Override
    public void delete(UUID uuid) {
        try {
            productRepository.delete(uuid);
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException(uuid);
        }
    }
}
