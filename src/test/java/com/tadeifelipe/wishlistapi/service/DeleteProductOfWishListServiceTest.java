package com.tadeifelipe.wishlistapi.service;

import com.tadeifelipe.wishlistapi.exception.ProductNotFoundException;
import com.tadeifelipe.wishlistapi.mapper.ProductMapper;
import com.tadeifelipe.wishlistapi.mapper.WishListMapper;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import com.tadeifelipe.wishlistapi.domain.WishList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.getIdCustomer;
import static com.tadeifelipe.wishlistapi.WishListApplicationTests.getProductOne;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DeleteProductOfWishListServiceTest {

    @Mock
    private WishListRepository wishListRepository;

    private DeleteProductOfWishListService deleteProductOfWishListService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteProductOfWishListService = new DeleteProductOfWishListService(wishListRepository,
                new WishListMapper(new ProductMapper()));
    }

    @Test
    public void shouldDeleteProductOfWishList() {
        WishList wishList = new WishList(getIdCustomer(), getProductOne());
        when(wishListRepository.findByCustomerAndProductId(anyString(), anyString()))
                .thenReturn(Optional.of(wishList));

        deleteProductOfWishListService.delete(getIdCustomer(), getProductOne().getId());

        verify(wishListRepository,times(1)).delete(wishList);
    }

    @Test
    public void shouldThrowExceptionWhenProductNotFound() {
        when(wishListRepository.findByCustomerAndProductId(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteProductOfWishListService.delete(getIdCustomer(),getProductOne().getId()))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageStartingWith("No products found for this customer");
    }
}