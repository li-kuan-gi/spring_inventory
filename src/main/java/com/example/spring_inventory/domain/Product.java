package com.example.spring_inventory.domain;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Product {

    @Id
    private UUID id;

    private Integer availableQuantity;

    public void reserve(Integer quantity) {
        if (availableQuantity < quantity) {
            throw new OutOfStock(id);
        }
        availableQuantity -= quantity;
    }

    public void restock(Integer quantity) {
        availableQuantity += quantity;
    }

}
