package com.tadeifelipe.wishlistapi.repository;

import com.tadeifelipe.wishlistapi.domain.WishList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;


@DataMongoTest
@ExtendWith(SpringExtension.class)
class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;

    @AfterEach
    public void tearDown() {
        wishListRepository.deleteAll();
    }

    @Test
    void shouldSaveAndfindWishListByCustomerAndProduct() {
        WishList wishList = new WishList(getIdCustomer(), getProductOne());

        wishListRepository.insert(wishList);

        Optional<WishList> actualResponse = wishListRepository.findByCustomerAndProductId(getIdCustomer(), getProductOne().getId());

        assertThat(actualResponse, is(Optional.of(wishList)));
    }

    @Test
    void shouldNotFindWishListByWrongCustomer() {
        WishList wishList = new WishList(getIdCustomer(), getProductOne());

        wishListRepository.insert(wishList);

        Optional<WishList> actualResponse = wishListRepository.findByCustomerAndProductId("9999", getProductOne().getId());

        assertThat(actualResponse, is(Optional.empty()));
    }

    @Test
    void shouldNotFindWishListByWrongProduct() {
        WishList wishList = new WishList(getIdCustomer(), getProductOne());

        wishListRepository.insert(wishList);

        Optional<WishList> actualResponse = wishListRepository.findByCustomerAndProductId(getIdCustomer(), "99999");

        assertThat(actualResponse, is(Optional.empty()));
    }

    @Test
    void shouldFindAllWishListByCustomer() {
        WishList first = wishListRepository.insert(new WishList(getIdCustomer(), getProductOne()));
        WishList second = wishListRepository.insert(new WishList(getIdCustomer(), getProductTwo()));
        WishList third = wishListRepository.insert(new WishList(getIdCustomer(), getProductThree()));

        List<WishList> actualResponse = wishListRepository.findByCustomer(getIdCustomer());

        assertThat(actualResponse, is(not(empty())));
        assertThat(actualResponse, hasSize(3));
        assertThat(actualResponse, hasItem(first));
        assertThat(actualResponse, hasItem(second));
        assertThat(actualResponse, hasItem(third));
    }

    @Test
    void shouldReturnEmptyListWhenFindAllWishListByWrongCustomer() {
        WishList first = new WishList(getIdCustomer(), getProductOne());
        wishListRepository.insert(first);

        WishList second = new WishList(getIdCustomer(), getProductTwo());
        wishListRepository.insert(second);

        WishList third = new WishList(getIdCustomer(), getProductThree());
        wishListRepository.insert(third);

        List<WishList> actualResponse = wishListRepository.findByCustomer("9999");

        assertThat(actualResponse, empty());
    }
}