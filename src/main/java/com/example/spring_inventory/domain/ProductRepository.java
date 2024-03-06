package com.example.spring_inventory.domain;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ProductRepository {

    List<Product> findByIdIn(Collection<UUID> ids);

    void save(Product product);

}
