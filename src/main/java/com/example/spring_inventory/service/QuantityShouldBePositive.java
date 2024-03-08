package com.example.spring_inventory.service;

import java.util.UUID;

public class QuantityShouldBePositive extends RuntimeException {

    public QuantityShouldBePositive(UUID id) {
        super("Required quantity for product " + id.toString() + " should be positive.");
    }

}
