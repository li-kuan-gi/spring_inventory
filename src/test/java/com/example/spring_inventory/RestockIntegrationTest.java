package com.example.spring_inventory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestockIntegrationTest {

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
        final var restockQuantity = 3;
        jdbcTemplate.execute(String.format(
                "INSERT INTO products (id, availableQuantity) VALUES ('%s', %d);",
                id.toString(), availableQuantity));

        final var response = postRestock(id, restockQuantity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void layersConnectForExceptionFlow() {
        final var id = UUID.randomUUID();
        final var requiredQuantity = 3;

        final var response = postRestock(id, requiredQuantity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Void> postRestock(UUID id, Integer quantity) {
        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        final var body = String.format("{\"id\": \"%s\", \"quantity\": %d}", id.toString(), quantity);

        HttpEntity<String> request = new HttpEntity<String>(body, headers);

        return restTemplate.postForEntity("/restock", request, Void.class);
    }

}
