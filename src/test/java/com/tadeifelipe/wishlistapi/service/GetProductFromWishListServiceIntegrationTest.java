package com.tadeifelipe.wishlistapi.service;

import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.exception.ProductNotFoundException;
import com.tadeifelipe.wishlistapi.mapper.ProductMapper;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import com.tadeifelipe.wishlistapi.domain.WishList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class GetProductFromWishListServiceIntegrationTest {

    @Autowired
    private WishListRepository wishListRepository;

    private GetProductFromWishListService getProductFromWishListService;

    @BeforeEach
    public void setUp() {
        getProductFromWishListService = new GetProductFromWishListService(wishListRepository, new ProductMapper());

        wishListRepository.insert(new WishList(getIdCustomer(), getProductOne()));
    }

    @AfterEach
    public void tearDown() {
        wishListRepository.deleteAll();
    }

    @Test
    public void shouldGetProductFromWishListGivenCustomerAndProduct() {
        ProductRecord actualResponse = getProductFromWishListService.getProduct(getIdCustomer(), getProductOne().getId());

        assertThat(actualResponse, notNullValue());
        assertThat(actualResponse.id(), is(getProductOne().getId()));
        assertThat(actualResponse.name(), is(getProductOne().getName()));
        assertThat(actualResponse.price(), is(getProductOne().getPrice()));
    }

    @Test
    public void shouldThrowExceptionWhenWrongProduct() {
        assertThatThrownBy(() -> getProductFromWishListService.getProduct(getIdCustomer(), "99999"))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageStartingWith("No products found for this customer");
    }

    @Test
    public void shouldThrowExceptionWhenWrongCustomer() {
        assertThatThrownBy(() -> getProductFromWishListService.getProduct("99999", getProductOne().getId()))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageStartingWith("No products found for this customer");
    }
}