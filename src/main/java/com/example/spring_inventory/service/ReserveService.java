package com.example.spring_inventory.service;

import java.util.Set;

public interface ReserveService {

    void execute(Set<ReserveRequirement> requirements);

}
