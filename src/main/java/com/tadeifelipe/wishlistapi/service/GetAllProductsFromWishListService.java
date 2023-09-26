package com.tadeifelipe.wishlistapi.service;

import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.mapper.ProductMapper;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import com.tadeifelipe.wishlistapi.domain.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.tadeifelipe.wishlistapi.config.CacheConfig.WISH_LIST_CACHE;

@Service
public class GetAllProductsFromWishListService {

    private final WishListRepository wishListRepository;

    private final ProductMapper productMapper;

    @Autowired
    public GetAllProductsFromWishListService(WishListRepository wishListRepository, ProductMapper productMapper) {
        this.wishListRepository = wishListRepository;
        this.productMapper = productMapper;
    }

    @Cacheable(value = WISH_LIST_CACHE)
    public List<ProductRecord> getAllProducts(String customerId) {
        List<WishList> wishLists = wishListRepository.findByCustomer(customerId);

        return wishLists.stream().map(wishList -> productMapper.domainToDto(wishList.getProduct()))
                .collect(Collectors.toList());
    }
}
