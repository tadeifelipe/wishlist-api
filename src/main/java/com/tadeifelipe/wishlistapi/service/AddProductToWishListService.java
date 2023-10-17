package com.tadeifelipe.wishlistapi.service;

import com.tadeifelipe.wishlistapi.clients.CustomerRestClient;
import com.tadeifelipe.wishlistapi.clients.ProductRestClient;
import com.tadeifelipe.wishlistapi.domain.WishListMaxSize;
import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.dto.WishListRecord;
import com.tadeifelipe.wishlistapi.exception.CustomerNotFoundException;
import com.tadeifelipe.wishlistapi.exception.ProductNotFoundException;
import com.tadeifelipe.wishlistapi.mapper.WishListMapper;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import com.tadeifelipe.wishlistapi.domain.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddProductToWishListService {

    private final WishListRepository wishListRepository;
    private final WishListMapper wishListMapper;
    private final CustomerRestClient customerRestClient;
    private final ProductRestClient productRestClient;
    private final WishListMaxSize wishListMaxSize;

    @Autowired
    public AddProductToWishListService(WishListRepository wishListRepository, WishListMapper wishListMapper,
                                       CustomerRestClient customerRestClient, ProductRestClient productRestClient, WishListMaxSize wishListMaxSize) {
        this.wishListRepository = wishListRepository;
        this.wishListMapper = wishListMapper;
        this.customerRestClient = customerRestClient;
        this.productRestClient = productRestClient;
        this.wishListMaxSize = wishListMaxSize;
    }

    public WishListRecord add(String customerId, String productId) {
        var customer = customerRestClient.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));

        ProductRecord product = productRestClient.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        List<WishList> wishLists = wishListRepository.findByCustomer(customerId);
        var productAlreadyInserted = wishLists.stream()
                .anyMatch(wishList -> wishList.getProduct().getId().equals(productId));

        if (productAlreadyInserted)
            throw new IllegalArgumentException("Product already inserted for this customer: " + productId);

        if (wishLists.size() > wishListMaxSize.getMaxSize())
            throw new IllegalArgumentException("Wishlist size exceeded. Max value:" + wishListMaxSize.getMaxSize());

        var entity = wishListMapper.toEntity(customer.id(), product);
        wishListRepository.insert(entity);

        return wishListMapper.toDto(entity);
    }
}
