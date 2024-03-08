package com.example.spring_inventory.domain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, UUID> {

    Optional<Product> findById(UUID id);

    List<Product> findByIdIn(Collection<UUID> ids);

    <S extends Product> S save(S product);

}
