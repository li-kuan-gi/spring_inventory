package com.example.spring_inventory.service;

import java.util.Set;

public interface ReserveProducts {

    void execute(Set<ReserveRequirement> requirements);

}
