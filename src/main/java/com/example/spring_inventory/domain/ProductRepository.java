package com.example.spring_inventory.domain;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, UUID> {

    List<Product> findByIdIn(Collection<UUID> ids);

    <S extends Product> S save(S product);

}
