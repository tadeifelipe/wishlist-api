package com.tadeifelipe.wishlistapi.service;

import com.tadeifelipe.wishlistapi.dto.WishListRecord;
import com.tadeifelipe.wishlistapi.exception.ProductNotFoundException;
import com.tadeifelipe.wishlistapi.mapper.WishListMapper;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import static com.tadeifelipe.wishlistapi.config.CacheConfig.WISH_LIST_CACHE;

@Service
public class DeleteProductOfWishListService {

    private final WishListRepository wishListRepository;
    private final WishListMapper wishListMapper;

    @Autowired
    public DeleteProductOfWishListService(WishListRepository wishListRepository, WishListMapper wishListMapper) {
        this.wishListRepository = wishListRepository;
        this.wishListMapper = wishListMapper;
    }

    @CacheEvict(value = WISH_LIST_CACHE)
    public WishListRecord delete(String customerId, String productId) {
        var wishList = wishListRepository.findByCustomerAndProductId(customerId, productId)
                        .orElseThrow(() -> new ProductNotFoundException("No products found for this customer"));

        wishListRepository.delete(wishList);
        return wishListMapper.toDto(wishList);
    }
}
