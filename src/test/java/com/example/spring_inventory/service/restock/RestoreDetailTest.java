package com.example.spring_inventory.service.restock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.example.spring_inventory.service.QuantityShouldBePositive;

public class RestoreDetailTest {

    @Test
    void identifiedByValues() {
        final var id = UUID.randomUUID();
        final var quantity = 5;
        final var detail1 = new RestockDetail(id, quantity);
        final var detail2 = new RestockDetail(id, quantity);

        assertThat(detail1).isEqualTo(detail2);
    }

    @Test
    void throwQuantityShouldBePositiveForZeroQuantity() {
        final var id = UUID.randomUUID();
        final var quantity = 0;

        final var thrown = assertThrows(QuantityShouldBePositive.class, () -> new RestockDetail(id, quantity));
        assertThat(thrown.getId()).isEqualTo(id);
    }

    @Test
    void throwQuantityShouldBePositiveForNegativeQuantity() {
        final var id = UUID.randomUUID();
        final var quantity = -3;

        final var thrown = assertThrows(QuantityShouldBePositive.class, () -> new RestockDetail(id, quantity));
        assertThat(thrown.getId()).isEqualTo(id);
    }

}
