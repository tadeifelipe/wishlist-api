package com.tadeifelipe.wishlistapi.mapper;

import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.dto.WishListRecord;
import com.tadeifelipe.wishlistapi.domain.WishList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WishListMapper {

    private final ProductMapper productMapper;

    @Autowired
    public WishListMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public WishList toEntity(String idCustomer, ProductRecord product) {
        return new WishList(idCustomer, productMapper.dtoToDomain(product));
    }

    public WishListRecord toDto(WishList entity) {
        return new WishListRecord(entity.getCustomer(), productMapper.domainToDto(entity.getProduct()));
    }
}
