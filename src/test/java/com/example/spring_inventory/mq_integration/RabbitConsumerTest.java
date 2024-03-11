package com.example.spring_inventory.mq_integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.spring_inventory.domain.ProductRepository;

@SpringBootTest
public class RabbitConsumerTest {

    @Value("${rabbitmq.exchange}")
    String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingkey;

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    ProductRepository repo;

    @BeforeEach
    @AfterEach
    void cleanup() {
        repo.deleteAll();
    }

    @Test
    void canReceiveMessage() throws InterruptedException {
        final var id = UUID.randomUUID();
        amqpTemplate.convertAndSend(exchange, routingkey, id);

        Thread.sleep(1000);

        assertThat(repo.count()).isEqualTo(1);
    }

}
