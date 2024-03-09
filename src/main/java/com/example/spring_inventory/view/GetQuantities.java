package com.example.spring_inventory.view;

import java.util.List;
import java.util.UUID;

public interface GetQuantities {

    List<QuantityInfo> query(List<UUID> ids);
    
}
