package com.tadeifelipe.wishlistapi.service;

import com.tadeifelipe.wishlistapi.mapper.ProductMapper;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import com.tadeifelipe.wishlistapi.domain.WishList;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class GetAllProductsFromWishListServiceIntegrationTest {

    @Autowired
    private WishListRepository wishListRepository;

    private GetAllProductsFromWishListService getAllProductsFromWishListService;

    @BeforeEach
    public void setUp() {
        getAllProductsFromWishListService = new GetAllProductsFromWishListService(wishListRepository, new ProductMapper());

        wishListRepository.insert(new WishList(getIdCustomer(), getProductOne()));
        wishListRepository.insert(new WishList(getIdCustomer(), getProductTwo()));
        wishListRepository.insert(new WishList(getIdCustomer(), getProductThree()));
    }

    @AfterEach
    public void tearDown() {
        wishListRepository.deleteAll();
    }

    @Test
    public void shouldReturnAllProductsFromCustomerWishList() {
        var actualResponse = getAllProductsFromWishListService.getAllProducts(getIdCustomer());

        assertThat(actualResponse, is(not(empty())));
        MatcherAssert.assertThat(actualResponse, hasSize(3));
    }

    @Test
    public void shouldReturnEmptyListGivenWrongCustomer() {
        var actualResponse = getAllProductsFromWishListService.getAllProducts("99999");

        assertThat(actualResponse, is(empty()));
    }
}