package com.example.spring_inventory.domain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Product {

    private UUID id;

    private Integer availableQuantity;

    public void reserve(Integer quantity) {
        if (availableQuantity < quantity) {
            throw new OutOfStock(id);
        }
        availableQuantity -= quantity;
    }

}
