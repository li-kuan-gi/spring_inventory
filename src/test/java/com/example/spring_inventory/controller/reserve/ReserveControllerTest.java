package com.example.spring_inventory.controller.reserve;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.example.spring_inventory.domain.OutOfStock;
import com.example.spring_inventory.service.SomeProductIdInvalid;
import com.example.spring_inventory.service.reserve.ReserveRequirement;
import com.example.spring_inventory.service.reserve.ReserveService;

@AutoConfigureJsonTesters
@WebMvcTest(ReserveController.class)
public class ReserveControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ReserveService service;

    @Test
    void reserveProductsInNormalFlow() throws Exception {
        final var id = UUID.randomUUID();
        final var quantity = 3;
        final var requirementsJson = "[" + requirementJson(id, quantity) + "]";
        final var requirements = collectRequirements(new ReserveRequirement(id, quantity));

        final var response = postRequirements(requirementsJson);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        verify(service, times(1)).execute(argThat(r -> areEqualRequirements(r, requirements)));
    }

    @Test
    void BadRequestForRequirementsWithDuplicatedIds() throws Exception {
        final var id = UUID.randomUUID();
        final var quantity1 = 3;
        final var quantity2 = 5;
        final var requirementsJson = "[" +
                requirementJson(id, quantity1) +
                "," +
                requirementJson(id, quantity2) +
                "]";

        final var response = postRequirements(requirementsJson);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString().toLowerCase()).contains("duplicate");
        verify(service, never()).execute(any());
    }

    @Test
    void NotFoundForSomeInvalidProductId() throws Exception {
        final var id = UUID.randomUUID();
        final var quantity = 3;
        final var requirementsJson = "[" + requirementJson(id, quantity) + "]";
        final var requirements = collectRequirements(new ReserveRequirement(id, quantity));
        doThrow(new SomeProductIdInvalid()).when(service).execute(any());

        final var response = postRequirements(requirementsJson);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString().toLowerCase()).contains("found");
        verify(service, times(1)).execute(requirements);
    }

    @Test
    void BadRequestForNonPositiveQuantity() throws Exception {
        final var id = UUID.randomUUID();
        final var quantity = -3;
        final var requirementJson = "[" + requirementJson(id, quantity) + "]";

        final var response = postRequirements(requirementJson);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString().toLowerCase()).contains("positive");
        verify(service, never()).execute(any());
    }

    @Test
    void InternalServerErrorForUnknownException() throws Exception {
        final var id = UUID.randomUUID();
        final var quantity = 3;
        final var requirementJson = "[" + requirementJson(id, quantity) + "]";
        doThrow(new UnknownException()).when(service).execute(any());

        final var response = postRequirements(requirementJson);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void ConflictForOutOfStock() throws Exception {
        final var id = UUID.randomUUID();
        final var quantity = 3;
        final var requirementJson = "[" + requirementJson(id, quantity) + "]";
        doThrow(new OutOfStock(id)).when(service).execute(any());

        final var response = postRequirements(requirementJson);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(response.getContentAsString().toLowerCase()).contains("stock");
        assertThat(response.getContentAsString().toLowerCase()).contains(id.toString());
    }

    private class UnknownException extends RuntimeException {
    }

    private Set<ReserveRequirement> collectRequirements(ReserveRequirement... requirements) {
        return new HashSet<ReserveRequirement>(Arrays.asList(requirements));
    }

    private MockHttpServletResponse postRequirements(String requirementsJson) throws Exception {
        return mvc.perform(
                post("/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requirementsJson))
                .andReturn()
                .getResponse();
    }

    private String requirementJson(UUID id, Integer quantity) {
        return String.format("""
                            {
                                \"id\": \"%s\",
                                \"quantity\": %d
                            }
                """, id.toString(), quantity);
    }

    private boolean areEqualRequirements(Set<ReserveRequirement> r1, Set<ReserveRequirement> r2) {
        final var map1 = r1.stream()
                .collect(Collectors.toMap(ReserveRequirement::getId, ReserveRequirement::getQuantity));
        final var map2 = r2.stream()
                .collect(Collectors.toMap(ReserveRequirement::getId, ReserveRequirement::getQuantity));
        return map1.equals(map2);
    }

}
