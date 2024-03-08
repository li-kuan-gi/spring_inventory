package com.example.spring_inventory.service.reserve;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.spring_inventory.domain.OutOfStock;
import com.example.spring_inventory.domain.Product;
import com.example.spring_inventory.domain.ProductRepository;
import com.example.spring_inventory.service.SomeProductIdInvalid;

@ExtendWith(MockitoExtension.class)
public class ReserveServiceImplTest {

    @InjectMocks
    ReserveServiceImpl service;

    @Mock
    ProductRepository repo;

    @Test
    void reservingProductsDecreaseAvailableQuantity() {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var availableQuantity1 = 5;
        final var availableQuantity2 = 5;
        final var requiredQuantity1 = 3;
        final var requiredQuantity2 = 2;

        final var ids = collectSet(id1, id2);

        final var requirements = collectSet(
                new ReserveRequirement(id1, requiredQuantity1),
                new ReserveRequirement(id2, requiredQuantity2));

        final var products = collectList(
                new Product(id1, availableQuantity1),
                new Product(id2, availableQuantity2));

        doReturn(products).when(repo).findByIdIn(anySet());

        service.execute(requirements);

        verify(repo, times(1)).findByIdIn(ids);
        verify(repo, times(2)).save(any(Product.class));
        verify(repo, atLeastOnce()).save(argThat(p -> p.getId().equals(products.get(0).getId())
                && p.getAvailableQuantity().equals(availableQuantity1 - requiredQuantity1)));
        verify(repo, atLeastOnce()).save(argThat(p -> p.getId().equals(products.get(1).getId())
                && p.getAvailableQuantity().equals(availableQuantity2 - requiredQuantity2)));
    }

    @Test
    void throwSomeProductIdInvalidWhenProductsAmountUnexpected() {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var availableQuantity1 = 5;
        final var requiredQuantity1 = 3;
        final var requiredQuantity2 = 2;

        final var ids = collectSet(id1, id2);

        final var requirements = collectSet(
                new ReserveRequirement(id1, requiredQuantity1),
                new ReserveRequirement(id2, requiredQuantity2));

        final var products = collectList(new Product(id1, availableQuantity1));

        doReturn(products).when(repo).findByIdIn(anySet());

        assertThrows(SomeProductIdInvalid.class, () -> service.execute(requirements));

        verify(repo, times(1)).findByIdIn(ids);
        verify(repo, never()).save(any());
    }

    @Test
    void throwOutOfStockWhenSomeProductQuantityInsufficient() {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var availableQuantity1 = 5;
        final var availableQuantity2 = 5;
        final var requiredQuantity1 = 3;
        final var requiredQuantity2 = 6;

        final var ids = collectSet(id1, id2);

        final var requirements = collectSet(
                new ReserveRequirement(id1, requiredQuantity1),
                new ReserveRequirement(id2, requiredQuantity2));

        final var products = collectList(
                new Product(id1, availableQuantity1),
                new Product(id2, availableQuantity2));

        doReturn(products).when(repo).findByIdIn(anySet());

        final var thrown = assertThrows(OutOfStock.class, () -> service.execute(requirements));

        verify(repo, times(1)).findByIdIn(ids);
        assertThat(thrown.getMessage()).contains(products.get(1).getId().toString());
    }

    @Test
    void propagateExceptionFromRepository() {
        final var id1 = UUID.randomUUID();
        final var id2 = UUID.randomUUID();
        final var requiredQuantity1 = 3;
        final var requiredQuantity2 = 2;

        final var requirements = collectSet(
                new ReserveRequirement(id1, requiredQuantity1),
                new ReserveRequirement(id2, requiredQuantity2));

        final var exception = new RuntimeException();
        doThrow(exception).when(repo).findByIdIn(anySet());

        final var thrown = assertThrows(Exception.class, () -> service.execute(requirements));

        assertThat(thrown).isEqualTo(exception);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> collectList(T... elements) {
        return Arrays.asList(elements);
    }

    @SuppressWarnings("unchecked")
    private <T> Set<T> collectSet(T... elements) {
        return Arrays.asList(elements).stream().collect(Collectors.toSet());
    }

}