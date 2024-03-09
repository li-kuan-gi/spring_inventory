package com.example.spring_inventory.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GetQuantitiesImpl implements GetQuantities {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<QuantityInfo> query(List<UUID> ids) {
        final var idStrings = ids.stream()
                .map(i -> "'" + i.toString() + "'")
                .reduce((acc, item) -> acc + "," + item);

        if (idStrings.isEmpty())
            return Arrays.asList();

        final var sql = String.format("SELECT id, availableQuantity FROM products WHERE id IN (%s)", idStrings.get());
        return jdbcTemplate.query(sql, new QuantityInfoRowMapper());
    }

    private class QuantityInfoRowMapper implements RowMapper<QuantityInfo> {

        @Override
        public QuantityInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            final var id = UUID.fromString(rs.getString("id"));
            final var quantity = rs.getInt("availableQuantity");
            return new QuantityInfo(id, quantity);
        }
    }

}
