package com.example.spring_inventory.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class ReserveRequirementTest {

    @Test
    void identifyById() {
        final var id = UUID.randomUUID();
        final var requirement1 = new ReserveRequirement(id, 3);
        final var requirement2 = new ReserveRequirement(id, 5);

        assertThat(requirement1).isEqualTo(requirement2);
    }

    @Test
    void quantityShouldNonNegative() {
        final var id = UUID.randomUUID();

        assertThrows(QuantityShouldNonNegative.class, () -> new ReserveRequirement(id, -2));
    }

}
