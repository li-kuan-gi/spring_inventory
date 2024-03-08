package com.example.spring_inventory.service;

import java.util.UUID;

import lombok.Getter;

@Getter
public class QuantityShouldBePositive extends RuntimeException {

    private UUID id;

    public QuantityShouldBePositive(UUID id) {
        super("Required quantity for product " + id.toString() + " should be positive.");
        this.id = id;
    }

}
