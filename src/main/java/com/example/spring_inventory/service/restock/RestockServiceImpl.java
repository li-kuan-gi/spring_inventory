package com.example.spring_inventory.service.restock;

import com.example.spring_inventory.domain.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RestockServiceImpl implements RestockService {

    private ProductRepository repo;

    @Override
    public void execute(RestockDetail detail) {
        final var result = repo.findById(detail.getId());

        if (result.isEmpty())
            throw new InvalidProductId(detail.getId());

        final var product = result.get();
        product.restock(detail.getQuantity());
        repo.save(product);
    }

}
