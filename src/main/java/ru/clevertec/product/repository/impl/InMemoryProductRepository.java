package ru.clevertec.product.repository.impl;

import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryProductRepository implements ProductRepository {

    private Map<UUID, Product> repository = new HashMap<>();

    @Override
    public Optional<Product> findById(UUID uuid) {
        return Optional.ofNullable(repository.get(uuid));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(repository.values());
    }

    @Override
    public Product save(Product product) {
        if(product.getUuid() == null) {
            UUID uuid = UUID.randomUUID();
            product.setUuid(uuid);
            repository.put(uuid, product);
        }
        else {
            findById(product.getUuid()).orElseThrow();
            repository.replace(product.getUuid(), product);
        }

        return repository.get(product.getUuid());
    }

    @Override
    public void delete(UUID uuid) {
        findById(uuid).orElseThrow();
        repository.remove(uuid);
    }
}
