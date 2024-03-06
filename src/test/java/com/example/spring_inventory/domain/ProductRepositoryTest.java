package com.example.spring_inventory.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    ProductRepository repo;

    @Test
    void injectedRepositoryNotNull() {
        assertThat(repo).isNotNull();
    }

    @Test
    void canSaveProduct() {
        final var product = new Product(UUID.randomUUID(), 5);

        repo.save(product);

        assertThat(repo.count()).isEqualTo(1);
    }

    @Test
    void canFindByIdIn() {
        final var id = UUID.randomUUID();
        final var product = new Product(id, 5);
        repo.save(product);

        final var result = repo.findByIdIn(Set.of(id));

        assertThat(result.get(0).getId()).isEqualTo(product.getId());
        assertThat(result.get(0).getAvailableQuantity()).isEqualTo(product.getAvailableQuantity());
    }

}
