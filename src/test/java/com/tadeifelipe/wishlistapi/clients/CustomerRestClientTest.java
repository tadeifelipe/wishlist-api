package com.tadeifelipe.wishlistapi.clients;

import com.tadeifelipe.wishlistapi.dto.CustomerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
class CustomerRestClientTest {

    private CustomerRestClient customerRestClient;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        customerRestClient = new CustomerRestClient(restTemplate, "http://localhost:8089/customers");
    }

    @Test
    public void shouldCallCustomersApi() {
        CustomerRecord response = new CustomerRecord("1", "Customer", "One");
        when(restTemplate.getForObject("http://localhost:8089/customers/1", CustomerRecord.class))
                .thenReturn(response);

        Optional<CustomerRecord> actualResponse = customerRestClient.findById("1");

        assertThat(actualResponse, is(Optional.of(response)));
    }


    @Test
    public void shouldReturnEmptyOptionalIfCustomersApiIsUnavailable() {
        when(restTemplate.getForObject("http://localhost:8089/customers/1", CustomerRecord.class))
                .thenThrow(new RestClientException("something went wrong"));

        Optional<CustomerRecord> actualResponse = customerRestClient.findById("1");

        assertThat(actualResponse, is(Optional.empty()));
    }
}