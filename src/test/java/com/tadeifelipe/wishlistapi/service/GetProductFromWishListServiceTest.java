package com.tadeifelipe.wishlistapi.service;

import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.exception.ProductNotFoundException;
import com.tadeifelipe.wishlistapi.mapper.ProductMapper;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;

class GetProductFromWishListServiceTest {

    @Mock
    private WishListRepository wishListRepository;

    private GetProductFromWishListService getProductFromWishListService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        getProductFromWishListService = new GetProductFromWishListService(wishListRepository, new ProductMapper());
    }

    @Test
    public void shouldGetProductFromWishListGivenCustomerAndProduct() {
        WishList wishList = new WishList(getIdCustomer(), getProductOne());
        when(wishListRepository.findByCustomerAndProductId(getIdCustomer(), getProductOne().getId()))
                .thenReturn(Optional.of(wishList));

        ProductRecord actualResponse = getProductFromWishListService.getProduct(getIdCustomer(), getProductOne().getId());
        assertThat(actualResponse, notNullValue());
        verify(wishListRepository, times(1)).findByCustomerAndProductId(getIdCustomer(),
                getProductOne().getId());
    }

    @Test
    public void shouldThrowExceptionWhenWrongProduct() {
        WishList wishList = new WishList(getIdCustomer(), getProductOne());
        when(wishListRepository.findByCustomerAndProductId(getIdCustomer(), getProductOne().getId()))
                .thenReturn(Optional.of(wishList));

        assertThatThrownBy(() -> getProductFromWishListService.getProduct(getIdCustomer(), "99999"))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageStartingWith("No products found for this customer");
    }

    @Test
    public void shouldThrowExceptionWhenWrongCustomer() {
        WishList wishList = new WishList(getIdCustomer(), getProductOne());
        when(wishListRepository.findByCustomerAndProductId(getIdCustomer(), getProductOne().getId()))
                .thenReturn(Optional.of(wishList));

        assertThatThrownBy(() -> getProductFromWishListService.getProduct("99999", getProductOne().getId()))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageStartingWith("No products found for this customer");
    }
}