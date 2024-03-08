package com.example.spring_inventory.controller.reserve;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_inventory.domain.OutOfStock;
import com.example.spring_inventory.service.QuantityShouldBePositive;
import com.example.spring_inventory.service.SomeProductIdInvalid;
import com.example.spring_inventory.service.reserve.ReserveRequirement;
import com.example.spring_inventory.service.reserve.ReserveService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("reserve")
@AllArgsConstructor
public class ReserveController {

    private ReserveService service;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reserveProducts(@RequestBody List<ReserveRequirement> entity) {
        final var requirements = entity.stream().collect(Collectors.toSet());
        if (requirements.size() != entity.size())
            throw new DuplicatedRequirement();
        service.execute(requirements);
    }

    @ExceptionHandler(DuplicatedRequirement.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDuplicatedRequirements() {
        return "Duplicated requirements";
    }

    @ExceptionHandler(SomeProductIdInvalid.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleSomeProductIdInvalid() {
        return "Some product not found.";
    }

    @ExceptionHandler(QuantityShouldBePositive.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleQuantityShouldBePositive(QuantityShouldBePositive ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(OutOfStock.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleOutOfStock(OutOfStock ex) {
        return ex.getMessage();
    }

}
