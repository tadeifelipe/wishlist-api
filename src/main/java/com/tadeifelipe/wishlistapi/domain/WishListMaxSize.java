package com.tadeifelipe.wishlistapi.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WishListMaxSize {

    private final int maxSize;

    public WishListMaxSize(@Value("${wishlist.max.size}") int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxSize(){
        return maxSize;
    }
}
