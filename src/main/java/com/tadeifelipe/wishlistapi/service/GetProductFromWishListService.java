package com.tadeifelipe.wishlistapi.service;

import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.exception.ProductNotFoundException;
import com.tadeifelipe.wishlistapi.mapper.ProductMapper;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.tadeifelipe.wishlistapi.config.CacheConfig.WISH_LIST_CACHE;

@Service
public class GetProductFromWishListService {

    private final WishListRepository wishListRepository;
    private final ProductMapper productMapper;

    @Autowired
    public GetProductFromWishListService(WishListRepository wishListRepository, ProductMapper productMapper) {
        this.wishListRepository = wishListRepository;
        this.productMapper = productMapper;
    }

    @Cacheable(value = WISH_LIST_CACHE)
    public ProductRecord getProduct(String customerId, String productId) {
        var wishList = wishListRepository.findByCustomerAndProductId(customerId, productId)
                .orElseThrow(() -> new ProductNotFoundException("No products found for this customer"));

        return productMapper.domainToDto(wishList.getProduct());
    }
}
