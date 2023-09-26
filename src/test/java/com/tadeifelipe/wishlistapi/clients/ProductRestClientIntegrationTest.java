package com.tadeifelipe.wishlistapi.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static com.github.tomakehurst.wiremock.common.ContentTypes.CONTENT_TYPE;
import static com.tadeifelipe.wishlistapi.WishListApplicationTests.getProductOne;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@WireMockTest(httpPort = 8089)
class ProductRestClientIntegrationTest {

    @Autowired
    private ProductRestClient productRestClient;

    @Test
    public void shouldCallProductsApi() {
        stubFor(get(urlPathEqualTo("/products/5c4fb2b8-0c79-4443-affd-e22815888d7e"))
                .willReturn(aResponse()
                        .withBody("{\"id\":\"5c4fb2b8-0c79-4443-affd-e22815888d7e\"," +
                                "\"name\":\"Product One\",\"price\": 55.90}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(200)));

        Optional<ProductRecord> actualResponse = productRestClient.findById(getProductOne().getId());

        assertThat(actualResponse, is(not(Optional.empty())));

        ProductRecord customerRecord = actualResponse.get();

        assertThat(customerRecord.id(), is(getProductOne().getId()));
        assertThat(customerRecord.name(), is(getProductOne().getName()));
        assertThat(customerRecord.price(), is(getProductOne().getPrice()));
    }

    @Test
    public void shouldReturnEmptyOptionalIfProductNotFound() {
        stubFor(get(urlPathEqualTo("/products/5c4fb2b8-0c79-4443-affd-e22815888d7e"))
                .willReturn(aResponse()
                        .withBody("")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(200)));

        Optional<ProductRecord> actualResponse = productRestClient.findById(getProductOne().getId());

        assertThat(actualResponse, is(Optional.empty()));
    }
}