package com.example.spring_inventory.controller.get_quantities;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_inventory.view.GetQuantities;
import com.example.spring_inventory.view.QuantityInfo;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("quantities")
@AllArgsConstructor
public class GetQuantitiesController {

    private GetQuantities view;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<QuantityInfo> getQuantities(@RequestParam(value = "id") List<UUID> ids) {
        return view.query(ids);
    }

}
