package com.example.spring_inventory.controller.restock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.example.spring_inventory.service.restock.InvalidProductId;
import com.example.spring_inventory.service.restock.RestockDetail;
import com.example.spring_inventory.service.restock.RestockService;

@WebMvcTest(RestockController.class)
public class RestockControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    RestockService service;

    @Test
    void restockProductInNormalFlow() throws Exception {
        final var id = UUID.randomUUID();
        final var quantity = 3;
        final var detail = new RestockDetail(id, quantity);

        final var response = postRestock(id, quantity);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        verify(service, times(1)).execute(detail);
    }

    @Test
    void notFoundForInvalidProductId() throws Exception {
        final var id = UUID.randomUUID();
        final var quantity = 3;
        final var detail = new RestockDetail(id, quantity);
        doThrow(new InvalidProductId(id)).when(service).execute(any());

        final var response = postRestock(id, quantity);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).contains(id.toString());
        verify(service, times(1)).execute(detail);
    }

    @Test
    void badRequestForNonPositiveQuantity() throws Exception {
        final var id = UUID.randomUUID();
        final var quantity = -3;

        final var response = postRestock(id, quantity);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("positive");
        verify(service, never()).execute(any());
    }

    @Test
    void internalServerErrorForUnknownException() throws Exception {
        final var id = UUID.randomUUID();
        final var quantity = 3;
        doThrow(new UnknownException()).when(service).execute(any());

        final var response = postRestock(id, quantity);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private MockHttpServletResponse postRestock(UUID id, Integer quantity) throws Exception {
        final var detailJson = String.format("{\"id\": \"%s\", \"quantity\": %d}", id.toString(), quantity);

        return mvc.perform(
                post("/restock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(detailJson))
                .andReturn()
                .getResponse();
    }

    private class UnknownException extends RuntimeException {
    }

}
