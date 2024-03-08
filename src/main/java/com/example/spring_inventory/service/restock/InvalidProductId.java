package com.example.spring_inventory.service.restock;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidProductId extends RuntimeException {

    private UUID id;

}
