package com.example.spring_inventory.service.restock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.spring_inventory.domain.Product;
import com.example.spring_inventory.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class RestockServiceImplTest {

    @InjectMocks
    RestockServiceImpl service;

    @Mock
    ProductRepository repo;

    @Test
    void restockingProductIncreaseAvailableQuantity() {
        final var id = UUID.randomUUID();
        final var availableQuantity = 5;
        final var restockQuantity = 3;
        final var detail = new RestockDetail(id, restockQuantity);
        doReturn(Optional.of(new Product(id, availableQuantity))).when(repo).findById(id);

        service.execute(detail);

        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(argThat(p -> p.getId().equals(id) &&
                p.getAvailableQuantity().equals(availableQuantity + restockQuantity)));
    }

    @Test
    void throwInvlidProductIdForNonExistentProduct() {
        final var id = UUID.randomUUID();
        final var restockQuantity = 3;
        final var detail = new RestockDetail(id, restockQuantity);

        final var thrown = assertThrows(InvalidProductId.class, () -> service.execute(detail));

        assertThat(thrown.getId()).isEqualTo(id);
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void propagateExcepitonFromRepositoty() {
        final var id = UUID.randomUUID();
        final var restockQuantity = 3;
        final var detail = new RestockDetail(id, restockQuantity);
        doThrow(new RepoException()).when(repo).findById(any());

        assertThrows(RepoException.class, () -> service.execute(detail));
    }

    private class RepoException extends RuntimeException {
    }

}
