package com.tadeifelipe.wishlistapi.clients;

import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.lang.String.format;

@Component
public class ProductRestClient {

    private final RestTemplate restTemplate;
    private final String productsApiUrl;

    Logger logger = LoggerFactory.getLogger(ProductRestClient.class);

    @Autowired
    public ProductRestClient(RestTemplate restTemplate,
                              @Value("${products.api.url}") String productsApiUrl) {
        this.restTemplate = restTemplate;
        this.productsApiUrl = productsApiUrl;
    }

    public Optional<ProductRecord> findById(String productId) {
        String url = format("%s/%s", productsApiUrl, productId);
        logger.debug("Calling service product for:" + productId);

        try {
            var product = restTemplate.getForObject(url, ProductRecord.class);

            return Optional.ofNullable(product);
        } catch (RestClientException ex) {
            return Optional.empty();
        }
    }
}
