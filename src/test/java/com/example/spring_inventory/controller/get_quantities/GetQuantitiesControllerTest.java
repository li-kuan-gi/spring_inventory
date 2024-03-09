package com.example.spring_inventory.controller.get_quantities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.example.spring_inventory.view.GetQuantities;
import com.example.spring_inventory.view.QuantityInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GetQuantitiesController.class)
@AutoConfigureJsonTesters
public class GetQuantitiesControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    GetQuantities view;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getQuantitiesInNormalFlow() throws Exception {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var id3 = UUID.randomUUID();
        final var quantity1 = 5;
        final var quantity3 = 5;

        final var ids = collect(id1, id2, id3);

        final var infos = collect(
                new QuantityInfo(id1, quantity1),
                new QuantityInfo(id3, quantity3));

        doReturn(infos).when(view).query(any());

        final var response = getQuantitiesInfo(ids);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        final var result = objectMapper.readValue(
                response.getContentAsString(),
                new TypeReference<List<QuantityInfo>>() {
                });
        assertThat(result).isEqualTo(infos);
    }

    @Test
    void internalServerErrorForUnknownException() throws Exception {
        final var ids = collect(UUID.randomUUID());
        doThrow(new UnknownException()).when(view).query(any());

        final var response = getQuantitiesInfo(ids);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private class UnknownException extends RuntimeException {
    }

    private <T> List<T> collect(@SuppressWarnings("unchecked") T... elements) {
        return Arrays.asList(elements);
    }

    private MockHttpServletResponse getQuantitiesInfo(List<UUID> ids) throws Exception {
        final var idArray = ids.stream().map(id -> id.toString()).toArray(String[]::new);
        return mvc.perform(
                get("/quantities")
                        .param("id", idArray))
                .andReturn()
                .getResponse();
    }

}
