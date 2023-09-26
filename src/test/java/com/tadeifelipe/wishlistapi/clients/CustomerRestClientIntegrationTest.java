package com.tadeifelipe.wishlistapi.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.tadeifelipe.wishlistapi.dto.CustomerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static com.github.tomakehurst.wiremock.common.ContentTypes.CONTENT_TYPE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@WireMockTest(httpPort = 8089)
class CustomerRestClientIntegrationTest {

    @Autowired
    private CustomerRestClient customerRestClient;

    @Test
    public void shouldCallCustomersApi() {
        stubFor(get(urlPathEqualTo("/customers/1"))
                .willReturn(aResponse()
                        .withBody("{\"id\":\"1\",\"name\":\"Customer\",\"lastName\":\"One\"}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(200)));

        Optional<CustomerRecord> actualResponse = customerRestClient.findById("1");

        assertThat(actualResponse, is(not(Optional.empty())));

        CustomerRecord customerRecord = actualResponse.get();

        assertThat(customerRecord.id(), is("1"));
        assertThat(customerRecord.name(), is("Customer"));
        assertThat(customerRecord.lastName(), is("One"));
    }

    @Test
    public void shouldReturnEmptyOptionalIfCustomerNotFound() {
        stubFor(get(urlPathEqualTo("/customers/1"))
                .willReturn(aResponse()
                        .withBody("")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withStatus(200)));

        Optional<CustomerRecord> actualResponse = customerRestClient.findById("1");

        assertThat(actualResponse, is(Optional.empty()));
    }
}