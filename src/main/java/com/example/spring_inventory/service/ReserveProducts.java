package com.example.spring_inventory.service;

import java.util.Map;
import java.util.UUID;

public interface ReserveProducts {

    void execute(Map<UUID, Integer> requirements);

}
