package com.example.spring_inventory.service.add_product;

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
public class AddProductServiceImplTest {

    @InjectMocks
    AddProductServiceImpl service;

    @Mock
    ProductRepository repo;

    @Test
    void addProductInNormalFlow() {
        final var id = UUID.randomUUID();

        service.execute(id);

        verify(repo, times(1)).save(argThat(p -> p.getId().equals(id)
                && p.getAvailableQuantity().equals(0)));
    }

    @Test
    void throwProductHasExistedForExistentProductId() {
        final var id = UUID.randomUUID();
        doReturn(Optional.of(new Product(id, 0))).when(repo).findById(id);

        assertThrows(ProductHasExisted.class, () -> service.execute(id));

        verify(repo, never()).save(any());
    }

    @Test
    void propagateExceptionFromRepository() {
        final var id = UUID.randomUUID();
        doThrow(new CustomException()).when(repo).findById(any());

        assertThrows(CustomException.class, () -> service.execute(id));
    }

    private class CustomException extends RuntimeException {
    }

}
