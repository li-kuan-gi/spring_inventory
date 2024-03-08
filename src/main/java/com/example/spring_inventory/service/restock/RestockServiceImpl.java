package com.example.spring_inventory.service.restock;

import org.springframework.stereotype.Service;

import com.example.spring_inventory.domain.ProductRepository;

import lombok.AllArgsConstructor;

@Service
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
