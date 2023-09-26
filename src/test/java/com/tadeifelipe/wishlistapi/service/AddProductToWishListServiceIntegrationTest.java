package com.tadeifelipe.wishlistapi.service;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.tadeifelipe.wishlistapi.clients.CustomerRestClient;
import com.tadeifelipe.wishlistapi.clients.ProductRestClient;
import com.tadeifelipe.wishlistapi.domain.WishList;
import com.tadeifelipe.wishlistapi.domain.WishListMaxSize;
import com.tadeifelipe.wishlistapi.exception.CustomerNotFoundException;
import com.tadeifelipe.wishlistapi.exception.ProductNotFoundException;
import com.tadeifelipe.wishlistapi.mapper.ProductMapper;
import com.tadeifelipe.wishlistapi.mapper.WishListMapper;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static com.github.tomakehurst.wiremock.common.ContentTypes.CONTENT_TYPE;
import static com.tadeifelipe.wishlistapi.WishListApplicationTests.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@WireMockTest(httpPort = 8089)
@SpringBootTest
class AddProductToWishListServiceIntegrationTest {

    private AddProductToWishListService addProductToWishListService;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private CustomerRestClient customerRestClient;

    @Autowired
    private ProductRestClient productRestClient;

    @Autowired
    private WishListMaxSize wishListMaxSize;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        addProductToWishListService = new AddProductToWishListService(wishListRepository,
                new WishListMapper(new ProductMapper()), customerRestClient, productRestClient, wishListMaxSize);
    }

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

        addProductToWishListService.add("1", getProductOne().getId());

        var actualResponse = wishListRepository.findByCustomerAndProductId("1", getProductOne().getId());

        assertThat(actualResponse.isPresent(), is(true));

        WishList savedWishList = actualResponse.get();

        assertThat(savedWishList.getCustomer(), is("1"));
        assertThat(savedWishList.getProduct().getId(), is(getProductOne().getId()));
        assertThat(savedWishList.getProduct().getName(), is(getProductOne().getName()));
        assertThat(savedWishList.getProduct().getPrice(), is(getProductOne().getPrice()));
    }

    @Test
    public void shouldThrowExceptionWhenCustomerNotFound() {
        stubFor(get(urlPathEqualTo("/customers/1"))
                .willReturn(aResponse()
                        .withBody("")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(200)));

        assertThatThrownBy(() -> addProductToWishListService.add("1",getProductOne().getId()))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageStartingWith("Customer not found");
    }

    @Test
    public void shouldThrowExceptionWhenProductNotFound() {
        stubFor(get(urlPathEqualTo("/customers/1"))
                .willReturn(aResponse()
                        .withBody("{\"id\":\"1\",\"name\":\"Customer\",\"lastName\":\"One\"}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(200)));

        stubFor(get(urlPathEqualTo("/products/5c4fb2b8-0c79-4443-affd-e22815888d7e"))
                .willReturn(aResponse()
                        .withBody("")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(200)));

        assertThatThrownBy(() -> addProductToWishListService.add("1", getProductOne().getId()))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageStartingWith("Product not found");
    }

    @Test
    public void shouldThrowExceptionWhenProductAlreadyInserted() {
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

        addProductToWishListService.add("1", getProductOne().getId());

        assertThatThrownBy(() -> addProductToWishListService.add("1",getProductOne().getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Product already inserted for this customer");
    }

    @Test
    public void shouldThrowExceptionWhenWishlistSizeExceeded() {
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

        stubFor(get(urlPathEqualTo("/products/aa2eb0b6-ff40-4ad5-9c31-c2c3a2dd2fb6"))
                .willReturn(aResponse()
                        .withBody("{\"id\":\"aa2eb0b6-ff40-4ad5-9c31-c2c3a2dd2fb6\"," +
                                "\"name\":\"Product Two\",\"price\": 45.90}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(200)));

        stubFor(get(urlPathEqualTo("/products/4a8de092-c8a7-4be7-be84-97b9aae3adb7"))
                .willReturn(aResponse()
                        .withBody("{\"id\":\"4a8de092-c8a7-4be7-be84-97b9aae3adb7\"," +
                                "\"name\":\"Product Three\",\"price\": 25.90}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(200)));

        addProductToWishListService.add("1", getProductOne().getId());
        addProductToWishListService.add("1", getProductTwo().getId());

        assertThatThrownBy(() -> addProductToWishListService.add("1", getProductThree().getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Wishlist size exceeded");
    }
}