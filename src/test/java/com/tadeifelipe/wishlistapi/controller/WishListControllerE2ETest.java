package com.tadeifelipe.wishlistapi.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.tadeifelipe.wishlistapi.domain.WishList;
import com.tadeifelipe.wishlistapi.dto.ProductIdRecord;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static com.github.tomakehurst.wiremock.common.ContentTypes.CONTENT_TYPE;
import static com.tadeifelipe.wishlistapi.WishListApplicationTests.*;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8089)
class WishListControllerE2ETest {

    @Autowired
    private WishListRepository wishListRepository;

    @LocalServerPort
    private int port;

    @AfterEach
    public void tearDown() {
        wishListRepository.deleteAll();
    }

    @Test
    public void shouldAddProductToWishList() {
        stubFor(get(urlPathEqualTo("/customers/1"))
                .willReturn(aResponse()
                        .withBody("{\"id\":\"1\",\"name\":\"Customer\",\"lastName\":\"One\"}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(200)));

        stubFor(get(urlPathEqualTo("/products/5c4fb2b8-0c79-4443-affd-e22815888d7e"))
                .willReturn(aResponse()
                        .withBody("{\"id\":\"5c4fb2b8-0c79-4443-affd-e22815888d7e\"," +
                                "\"name\":\"Product One\",\"price\": 55.90}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(200)));

        ProductIdRecord productId = new ProductIdRecord(getProductOne().getId());

        given().log().all()
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(productId)
        .when()
                .post(format("http://localhost:%s/customer/wishlist/1", port))
        .then()
                .log().all()
                .statusCode(is(201))
                .body(
                        "customer", is("1"),
                        "product.id", is("5c4fb2b8-0c79-4443-affd-e22815888d7e"),
                        "product.name", is("Product One"),
                        "product.price", is(55.9F)
                );
    }

    @Test
    public void shouldDeleteProductFromWishList() {
        WishList wishList = new WishList(getIdCustomer(), getProductOne());
        wishListRepository.insert(wishList);

        given()
                .auth().basic("admin","admin")
        .when()
                .delete(format("http://localhost:%s/customer/wishlist/%s/product=%s", port, getIdCustomer(), getProductOne().getId()))
        .then()
                .statusCode(is(200))
                .body(
                        "customer", is(getIdCustomer()),
                        "product.id", is("5c4fb2b8-0c79-4443-affd-e22815888d7e"),
                        "product.name", is("Product One"),
                        "product.price", is(55.9F)
                );
    }

    @Test
    public void shouldGetAllProductSFromWishListCustomer() {
        WishList wishList = new WishList(getIdCustomer(), getProductOne());
        wishListRepository.insert(wishList);
        wishList = new WishList(getIdCustomer(), getProductTwo());
        wishListRepository.insert(wishList);

        given()
                .auth().basic("admin","admin")
        .when()
                .get(format("http://localhost:%s/customer/wishlist/%s", port, getIdCustomer()))
        .then()
                .statusCode(is(200))
                .body(
                        containsString(
                                "{\"id\":\"5c4fb2b8-0c79-4443-affd-e22815888d7e\",\"name\":\"Product One\",\"price\":55.9}," +
                                        "{\"id\":\"aa2eb0b6-ff40-4ad5-9c31-c2c3a2dd2fb6\",\"name\":\"Product Two\",\"price\":45.9}"
                        )
                );
    }

    @Test
    public void shouldGetOneProductFromWishListCustomer() {
        WishList wishList = new WishList(getIdCustomer(), getProductOne());
        wishListRepository.insert(wishList);

        given()
                .auth().basic("admin","admin")
                .contentType(ContentType.JSON)
                .when()
                .get(format("http://localhost:%s/customer/wishlist/%s/product=%s", port, getIdCustomer(), getProductOne().getId()))
                .then()
                .statusCode(is(200))
                .body(
                        containsString(
                                "{\"id\":\"5c4fb2b8-0c79-4443-affd-e22815888d7e\",\"name\":\"Product One\",\"price\":55.9}"
                        )
                );
    }
}