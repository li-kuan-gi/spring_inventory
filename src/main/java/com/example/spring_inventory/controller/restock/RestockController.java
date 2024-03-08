package com.example.spring_inventory.controller.restock;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_inventory.service.QuantityShouldBePositive;
import com.example.spring_inventory.service.restock.InvalidProductId;
import com.example.spring_inventory.service.restock.RestockDetail;
import com.example.spring_inventory.service.restock.RestockService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("restock")
@AllArgsConstructor
public class RestockController {

    private RestockService service;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restock(@RequestBody RestockDetail detail) {
        service.execute(detail);
    }

    @ExceptionHandler(InvalidProductId.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String invalidIdHandler(InvalidProductId ex) {
        return String.format("Product %s is not found.", ex.getId().toString());
    }

    @ExceptionHandler(QuantityShouldBePositive.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleQuantityShouldBePositive(QuantityShouldBePositive ex) {
        return ex.getMessage();
    }

}
