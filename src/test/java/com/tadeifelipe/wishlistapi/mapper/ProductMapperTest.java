package com.tadeifelipe.wishlistapi.mapper;

import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.getProductOne;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest(classes=ProductMapper.class)
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void shouldMapProductToProductRecord() {
        ProductRecord productDto = productMapper.domainToDto(getProductOne());

        assertThat(productDto.id(), is(getProductOne().getId()));
        assertThat(productDto.name(), is(getProductOne().getName()));
        assertThat(productDto.price(), is(getProductOne().getPrice()));
    }

    @Test
    public void shouldMapProductRecordToProduct() {
        ProductRecord productRecord = new ProductRecord("5c4fb2b8-0c79-4443-affd-e22815888d7e", "Product One", 55.90);
        Product product = productMapper.dtoToDomain(productRecord);

        assertThat(product.getId(), is(productRecord.id()));
        assertThat(product.getName(), is(productRecord.name()));
        assertThat(product.getPrice(), is(productRecord.price()));
    }
}