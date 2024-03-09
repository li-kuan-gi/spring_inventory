package com.example.spring_inventory.service.add_product;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.spring_inventory.domain.Product;
import com.example.spring_inventory.domain.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AddProductServiceImpl implements AddProductService {

    private ProductRepository repo;

    @Override
    public void execute(UUID id) {
        final var result = repo.findById(id);

        if (result.isPresent())
            throw new ProductHasExisted(id);

        final var product = new Product(id, 0);
        repo.save(product);
    }

}
