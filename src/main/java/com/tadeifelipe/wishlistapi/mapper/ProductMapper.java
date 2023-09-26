package com.tadeifelipe.wishlistapi.mapper;

import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductRecord domainToDto(final Product product) {
        return new ProductRecord(product.getId(), product.getName(), product.getPrice());
    }

    public Product dtoToDomain(final ProductRecord dto) {
        return new Product()
                .setId(dto.id())
                .setName(dto.name())
                .setPrice(dto.price());
    }
}
