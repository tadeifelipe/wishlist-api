package com.tadeifelipe.wishlistapi.clients;

import com.tadeifelipe.wishlistapi.dto.CustomerRecord;
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
public class CustomerRestClient {

    private final RestTemplate restTemplate;
    private final String customerApiUrl;

    Logger logger = LoggerFactory.getLogger(CustomerRestClient.class);

    @Autowired
    public CustomerRestClient(RestTemplate restTemplate,
                              @Value("${customer.api.url}") String customerApiUrl) {
        this.restTemplate = restTemplate;
        this.customerApiUrl = customerApiUrl;
    }

    public Optional<CustomerRecord> findById(String customerId) {
        String url = format("%s/%s", customerApiUrl, customerId);
        logger.debug("Calling service customer for:" + customerId);

        try {
            var wishList = restTemplate.getForObject(url, CustomerRecord.class);

            return Optional.ofNullable(wishList);
        } catch (RestClientException ex) {
            return Optional.empty();
        }
    }
}
