package com.example.spring_inventory.service.reserve;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_inventory.domain.Product;
import com.example.spring_inventory.domain.ProductRepository;
import com.example.spring_inventory.service.SomeProductIdInvalid;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class ReserveServiceImpl implements ReserveService {

    private ProductRepository repo;

    @Override
    public void execute(Set<ReserveRequirement> requirements) {
        final var ids = requirements.stream().map(r -> r.getId()).collect(Collectors.toSet());
        final var products = repo.findByIdIn(ids);

        if (products.size() != ids.size())
            throw new SomeProductIdInvalid();

        for (Product product : products) {
            final var quantity = requirements
                    .stream()
                    .filter(r -> r.getId().equals(product.getId()))
                    .findFirst()
                    .get()
                    .getQuantity();
            product.reserve(quantity);
            repo.save(product);
        }
    }

}
