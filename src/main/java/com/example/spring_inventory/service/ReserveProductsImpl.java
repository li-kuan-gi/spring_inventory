package com.example.spring_inventory.service;

import java.util.Map;
import java.util.UUID;

import com.example.spring_inventory.domain.Product;
import com.example.spring_inventory.domain.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReserveProductsImpl implements ReserveProducts {

    private ProductRepository repo;

    @Override
    public void execute(Map<UUID, Integer> requirements) {
        final var ids = requirements.keySet();
        final var products = repo.findByIdIn(ids);

        if (products.size() != ids.size())
            throw new SomeProductIdInvalid();

        for (Product product : products) {
            product.reserve(requirements.get(product.getId()));
            repo.save(product);
        }
    }

}
