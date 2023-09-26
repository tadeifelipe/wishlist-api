package com.tadeifelipe.wishlistapi.service;

import com.tadeifelipe.wishlistapi.mapper.ProductMapper;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import com.tadeifelipe.wishlistapi.domain.WishList;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GetAllProductsFromWishListServiceTest {

    @Mock
    private WishListRepository wishListRepository;

    private GetAllProductsFromWishListService getAllProductsFromWishListService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        getAllProductsFromWishListService = new GetAllProductsFromWishListService(wishListRepository,
                new ProductMapper());
    }

    @Test
    public void shouldGetAllProductsFromWishListGivenCustomer() {
        List<WishList> response = asList(new WishList(getIdCustomer(), getProductOne()),
                new WishList(getIdCustomer(), getProductTwo()),
                new WishList(getIdCustomer(), getProductThree()));

        when(wishListRepository.findByCustomer(anyString()))
                .thenReturn(response);

        var actualResponse = getAllProductsFromWishListService.getAllProducts("12345");

        MatcherAssert.assertThat(actualResponse, hasSize(3));
        verify(wishListRepository, times(1)).findByCustomer("12345");
    }

    @Test
    public void shouldGetEmptyListGivenWrongCustomer() {
        when(wishListRepository.findByCustomer(anyString()))
                .thenReturn(emptyList());

        var actualResponse = getAllProductsFromWishListService.getAllProducts(getIdCustomer());

        MatcherAssert.assertThat(actualResponse, is(empty()));
    }
}