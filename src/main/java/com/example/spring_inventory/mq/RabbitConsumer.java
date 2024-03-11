package com.example.spring_inventory.mq;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.spring_inventory.service.add_product.AddProductService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RabbitConsumer {

    private AddProductService service;

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void handleMessage(String message) {
        final var id = UUID.fromString(message);
        service.execute(id);
    }

}
