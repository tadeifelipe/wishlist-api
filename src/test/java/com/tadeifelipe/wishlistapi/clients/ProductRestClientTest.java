package com.tadeifelipe.wishlistapi.clients;

import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.getProductOne;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;


@ExtendWith({SpringExtension.class})
class ProductRestClientTest {

    private ProductRestClient productRestClient;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        productRestClient = new ProductRestClient(restTemplate, "http://localhost:8089/products");
    }

    @Test
    public void shouldCallProductsApi() {
        ProductRecord response = new ProductRecord(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice());
        when(restTemplate.getForObject("http://localhost:8089/products/5c4fb2b8-0c79-4443-affd-e22815888d7e", ProductRecord.class))
                .thenReturn(response);

        Optional<ProductRecord> actualResponse = productRestClient.findById(getProductOne().getId());

        assertThat(actualResponse, is(Optional.of(response)));
    }

    @Test
    public void shouldReturnEmptyOptionalIfCustomersApiIsUnavailable() {
        when(restTemplate.getForObject("http://localhost:8089/products/5c4fb2b8-0c79-4443-affd-e22815888d7e", ProductRecord.class))
                .thenThrow(new RestClientException("something went wrong"));

        Optional<ProductRecord> actualResponse = productRestClient.findById("1");

        assertThat(actualResponse, is(Optional.empty()));
    }
}