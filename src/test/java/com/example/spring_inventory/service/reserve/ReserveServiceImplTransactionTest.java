package com.example.spring_inventory.service.reserve;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.spring_inventory.domain.Product;
import com.example.spring_inventory.domain.ProductRepository;

@SpringBootTest
public class ReserveServiceImplTransactionTest {

    @Autowired
    ReserveService service;

    @Autowired
    ProductRepository repo;

    @BeforeEach
    @AfterEach
    void clearup() {
        repo.deleteAll();
    }

    @Test
    void rollbackWhenExceptionOccurs() {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var p1 = new Product(id1, 5);
        final var p2 = new Product(id2, 3);
        repo.save(p1);
        repo.save(p2);

        final var requirements = Arrays.asList(
                new ReserveRequirement(id1, 4),
                new ReserveRequirement(id2, 4)).stream().collect(Collectors.toSet());

        assertThrows(Exception.class, () -> service.execute(requirements));

        assertThat(repo.findById(id1).get().getAvailableQuantity()).isEqualTo(5);
        assertThat(repo.findById(id2).get().getAvailableQuantity()).isEqualTo(3);
    }

}
