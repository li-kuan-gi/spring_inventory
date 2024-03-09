package com.example.spring_inventory.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

@SpringBootTest
public class GetQuantitiesTest {

    @Autowired
    GetQuantities view;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    @AfterEach
    void setup() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "products");
    }

    @Test
    void getPricesForExistentProducts() {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var quantity1 = 3;

        jdbcTemplate.execute(String.format(
                "INSERT INTO products (id, availableQuantity) VALUES ('%s', %d);",
                id1.toString(), quantity1));

        final var infos = view.query(Arrays.asList(id1, id2));

        assertThat(infos.size()).isEqualTo(1);
        assertThat(infos.get(0)).isEqualTo(new QuantityInfo(id1, quantity1));
    }

}
