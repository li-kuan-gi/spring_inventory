package com.example.spring_inventory.service.reserve;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.example.spring_inventory.service.QuantityShouldBePositive;

public class ReserveRequirementTest {

    @Test
    void identifyById() {
        final var id = UUID.randomUUID();
        final var requirement1 = new ReserveRequirement(id, 3);
        final var requirement2 = new ReserveRequirement(id, 5);

        assertThat(requirement1).isEqualTo(requirement2);
    }

    @Test
    void quantityCannotBeNegative() {
        final var id = UUID.randomUUID();

        assertThrows(QuantityShouldBePositive.class, () -> new ReserveRequirement(id, -2));
    }

    @Test
    void quantityCannotBeZero() {
        final var id = UUID.randomUUID();

        assertThrows(QuantityShouldBePositive.class, () -> new ReserveRequirement(id, 0));
    }

}
