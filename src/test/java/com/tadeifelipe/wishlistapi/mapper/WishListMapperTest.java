package com.tadeifelipe.wishlistapi.mapper;

import com.tadeifelipe.wishlistapi.domain.WishList;
import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.dto.WishListRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class WishListMapperTest {

    private WishListMapper wishListMapper;

    @BeforeEach
    public void setUp() {
        wishListMapper = new WishListMapper(new ProductMapper());
    }

    @Test
    public void shouldMapToEntity() {
        ProductRecord productRecord = new ProductRecord("5c4fb2b8-0c79-4443-affd-e22815888d7e", "Product One", 55.9);
        WishList entity = wishListMapper.toEntity(getIdCustomer(), productRecord);

        assertThat(entity.getCustomer(), is(getIdCustomer()));
        assertThat(entity.getProduct().getId(), is(productRecord.id()));
        assertThat(entity.getProduct().getName(), is(productRecord.name()));
        assertThat(entity.getProduct().getPrice(), is(productRecord.price()));
    }

    @Test
    public void shouldMapEntityToDto() {
        WishList wishList = new WishList(getIdCustomer(), getProductOne());
        WishListRecord dto = wishListMapper.toDto(wishList);

        assertThat(dto.customer(), is(getIdCustomer()));
        assertThat(dto.product().id(), is(getProductOne().getId()));
        assertThat(dto.product().name(), is(getProductOne().getName()));
        assertThat(dto.product().price(), is(getProductOne().getPrice()));
    }
}