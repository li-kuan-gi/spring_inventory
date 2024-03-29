package com.example.spring_inventory.service.reserve;

import java.util.UUID;

import com.example.spring_inventory.service.QuantityShouldBePositive;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class ReserveRequirement {

    @EqualsAndHashCode.Include
    private UUID id;

    private Integer quantity;

    public ReserveRequirement(UUID id, Integer quantity) {
        if (quantity <= 0)
            throw new QuantityShouldBePositive(id);

        this.id = id;
        this.quantity = quantity;
    }

}
