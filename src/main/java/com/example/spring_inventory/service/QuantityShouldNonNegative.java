package com.example.spring_inventory.service;

import java.util.UUID;

public class QuantityShouldNonNegative extends RuntimeException {

    public QuantityShouldNonNegative(UUID id) {
        super("Required quantity for product " + id.toString() + " should be non-negative.");
    }

}
