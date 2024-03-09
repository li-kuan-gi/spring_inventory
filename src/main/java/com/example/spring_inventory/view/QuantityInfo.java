package com.example.spring_inventory.view;

import java.util.UUID;

import lombok.Value;

@Value
public class QuantityInfo {

    private UUID id;

    private Integer quantity;
    
}
