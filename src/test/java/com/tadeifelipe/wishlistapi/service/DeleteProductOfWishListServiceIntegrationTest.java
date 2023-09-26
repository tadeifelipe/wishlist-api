package com.tadeifelipe.wishlistapi.service;

import com.tadeifelipe.wishlistapi.exception.ProductNotFoundException;
import com.tadeifelipe.wishlistapi.mapper.ProductMapper;
import com.tadeifelipe.wishlistapi.mapper.WishListMapper;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import com.tadeifelipe.wishlistapi.domain.WishList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.getIdCustomer;
import static com.tadeifelipe.wishlistapi.WishListApplicationTests.getProductOne;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class DeleteProductOfWishListServiceIntegrationTest {

    @Autowired
    private WishListRepository wishListRepository;

    private DeleteProductOfWishListService deleteProductOfWishListService;

    @BeforeEach
    public void setUp() {
        deleteProductOfWishListService = new DeleteProductOfWishListService(wishListRepository,
                new WishListMapper(new ProductMapper()));

        WishList wishList = new WishList(getIdCustomer(), getProductOne());

        wishListRepository.insert(wishList);
    }

    @AfterEach
    public void tearDown() {
        wishListRepository.deleteAll();
    }

    @Test
    public void shouldDeleteProductOfWishList() {
        deleteProductOfWishListService.delete(getIdCustomer(), getProductOne().getId());

        List<WishList> actualResponse = wishListRepository.findByCustomer(getIdCustomer());

        assertThat(actualResponse, is(empty()));
    }

    @Test
    public void shouldThrowExceptionWhenWrongProduct() {
        assertThatThrownBy(() -> deleteProductOfWishListService.delete(getIdCustomer(),"999999"))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageStartingWith("No products found for this customer");
    }

    @Test
    public void shouldThrowExceptionWhenWrongCustomer() {
        assertThatThrownBy(() -> deleteProductOfWishListService.delete("999999",getProductOne().getId()))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageStartingWith("No products found for this customer");
    }
}