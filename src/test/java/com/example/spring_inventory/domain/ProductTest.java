package com.example.spring_inventory.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    void canReserveIfAvailableQuantityIsSufficient() {
        final var id = UUID.randomUUID();
        final var availableQuantity = 5;
        final var requiredQuantity = 3;
        final var product = new Product(id, availableQuantity);

        product.reserve(requiredQuantity);

        assertThat(product.getAvailableQuantity()).isEqualTo(availableQuantity - requiredQuantity);
    }

    @Test
    void throwOutOfStockIfAvailableQuantityIsInsufficient() {
        final var id = UUID.randomUUID();
        final var availableQuantity = 5;
        final var requiredQuantity = 9;
        final var product = new Product(id, availableQuantity);

        final var thrown = assertThrows(OutOfStock.class, () -> product.reserve(requiredQuantity));
        assertThat(thrown.getMessage()).contains(id.toString());
    }

    @Test
    void canRestock() {
        final var id = UUID.randomUUID();
        final var availableQuantity = 5;
        final var restockQuantity = 3;
        final var product = new Product(id, availableQuantity);

        product.restock(restockQuantity);

        assertThat(product.getAvailableQuantity()).isEqualTo(availableQuantity + restockQuantity);
    }

}
