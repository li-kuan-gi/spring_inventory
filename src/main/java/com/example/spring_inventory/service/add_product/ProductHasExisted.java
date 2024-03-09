package com.example.spring_inventory.service.add_product;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductHasExisted extends RuntimeException {

    private UUID id;

}
