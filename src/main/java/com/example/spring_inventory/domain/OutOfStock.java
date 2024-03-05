package com.example.spring_inventory.domain;

import java.util.UUID;

public class OutOfStock extends RuntimeException {

    public OutOfStock(UUID id) {
        super("The product " + id.toString() + " is out of stock.");
    }

}
