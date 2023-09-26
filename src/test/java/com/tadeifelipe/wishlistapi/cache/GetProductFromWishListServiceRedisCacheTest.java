package com.tadeifelipe.wishlistapi.cache;

import com.tadeifelipe.wishlistapi.config.CacheConfig;
import com.tadeifelipe.wishlistapi.domain.WishList;
import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.mapper.ProductMapper;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import com.tadeifelipe.wishlistapi.service.GetProductFromWishListService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;


import java.util.Optional;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@Import({ CacheConfig.class, GetProductFromWishListService.class})
@ExtendWith(SpringExtension.class)
@EnableCaching
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class,
        RedisAutoConfiguration.class
})
public class GetProductFromWishListServiceRedisCacheTest {

    @MockBean
    private WishListRepository wishListRepository;

    @MockBean
    private ProductMapper productMapper;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private GetProductFromWishListService getProductFromWishListService;

    @Test
    public void shouldHitOnRedisCacheForGetSameProduct() {
        WishList wishList = new WishList(getIdCustomer(), getProductOne());
        when(wishListRepository.findByCustomerAndProductId(getIdCustomer(), getProductOne().getId()))
                .thenReturn(Optional.of(wishList));

        ProductRecord firstRecord = getProductFromWishListService.getProduct(getIdCustomer(), getProductOne().getId());
        ProductRecord secondProduct = getProductFromWishListService.getProduct(getIdCustomer(), getProductOne().getId());

        verify(wishListRepository, times(1)).findByCustomerAndProductId(getIdCustomer(), getProductOne().getId());
        assertThat(firstRecord, is(equalTo(secondProduct)));
    }

    @TestConfiguration
    static class EmbeddedRedisConfiguration {

        private final RedisServer redisServer;

        public EmbeddedRedisConfiguration() {
            this.redisServer = new RedisServer();
        }

        @PostConstruct
        public void startRedis() {
            redisServer.start();
        }

        @PreDestroy
        public void stopRedis() {
            this.redisServer.stop();
        }
    }
}
