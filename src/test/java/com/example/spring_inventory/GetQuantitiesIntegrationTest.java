package com.example.spring_inventory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.example.spring_inventory.view.QuantityInfo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetQuantitiesIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    @AfterEach
    void clearDatabase(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "products");
    }

    @Test
    void layersConnectForNormalFlow(@Autowired JdbcTemplate jdbcTemplate) {
        final var id = UUID.randomUUID();
        final var availableQuantity = 5;
        jdbcTemplate.execute(String.format(
                "INSERT INTO products (id, availableQuantity) VALUES ('%s', %d);",
                id.toString(), availableQuantity));

        final var response = restTemplate.getForEntity("/quantities?id=" + id.toString(), QuantityInfo[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()[0]).isEqualTo(new QuantityInfo(id, availableQuantity));
    }

}
