package com.tadeifelipe.wishlistapi.controller;

import com.tadeifelipe.wishlistapi.dto.ProductIdRecord;
import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.dto.WishListRecord;
import com.tadeifelipe.wishlistapi.service.AddProductToWishListService;
import com.tadeifelipe.wishlistapi.service.DeleteProductOfWishListService;
import com.tadeifelipe.wishlistapi.service.GetAllProductsFromWishListService;
import com.tadeifelipe.wishlistapi.service.GetProductFromWishListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

class WishListControllerTest {

    private WishListController wishListController;

    @Mock
    private AddProductToWishListService addProductToWishListService;

    @Mock
    private DeleteProductOfWishListService deleteProductOfWishListService;

    @Mock
    private GetAllProductsFromWishListService getAllProductsFromWishListService;

    @Mock
    private GetProductFromWishListService getProductFromWishListService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        wishListController = new WishListController(addProductToWishListService, deleteProductOfWishListService,
                getAllProductsFromWishListService, getProductFromWishListService);
    }

    @Test
    public void shouldAddProductToWishList() {
        WishListRecord response = new WishListRecord(getIdCustomer(),
                new ProductRecord(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice()));
        when(addProductToWishListService.add(getIdCustomer(), getProductOne().getId()))
                .thenReturn(response);

        var actualResponse = wishListController.addProduct(getIdCustomer(),
                new ProductIdRecord((getProductOne().getId())));

        assertThat(actualResponse.getBody(), is(response));
        assertThat(actualResponse.getStatusCode(), is(HttpStatus.CREATED));
    }

    @Test
    public void shouldDeleteProductFromWishList() {
        WishListRecord response = new WishListRecord(getIdCustomer(),
                new ProductRecord(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice()));
        when(deleteProductOfWishListService.delete(getIdCustomer(), getProductOne().getId()))
                .thenReturn(response);

        var actualResponse = wishListController.deleteProduct(getIdCustomer(), getProductOne().getId());

        assertThat(actualResponse.getBody(), is(response));
        assertThat(actualResponse.getStatusCode(), is(org.springframework.http.HttpStatus.OK));
    }

    @Test
    public void shouldGetAllProductsFromCustomer() {
        List<ProductRecord> response = List.of(
                new ProductRecord(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice()),
                new ProductRecord(getProductTwo().getId(), getProductTwo().getName(), getProductTwo().getPrice()),
                new ProductRecord(getProductThree().getId(), getProductThree().getName(), getProductThree().getPrice())
        );

        when(getAllProductsFromWishListService.getAllProducts(getIdCustomer()))
                .thenReturn(response);

        var actualResponse = wishListController.getAllProducts(getIdCustomer());

        assertThat(actualResponse.getBody(), is(response));
        assertThat(actualResponse.getStatusCode(), is(org.springframework.http.HttpStatus.OK));
    }

    @Test
    public void shouldGetOneProductsFromCustomer() {
        ProductRecord response = new ProductRecord(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice());

        when(getProductFromWishListService.getProduct(getIdCustomer(), getProductOne().getId()))
                .thenReturn(response);

        var actualResponse = wishListController.getProduct(getIdCustomer(), getProductOne().getId());

        assertThat(actualResponse.getBody(), is(response));
        assertThat(actualResponse.getStatusCode(), is(org.springframework.http.HttpStatus.OK));
    }
}