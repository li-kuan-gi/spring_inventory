package com.example.spring_inventory.service.restock;

import java.util.UUID;

import com.example.spring_inventory.service.QuantityShouldBePositive;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class RestockDetail {

    private UUID id;

    private Integer quantity;

    public RestockDetail(UUID id, Integer quantity) {
        if (quantity <= 0)
            throw new QuantityShouldBePositive(id);

        this.id = id;
        this.quantity = quantity;
    }

}
