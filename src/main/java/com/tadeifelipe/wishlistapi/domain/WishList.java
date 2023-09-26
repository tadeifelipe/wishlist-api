package com.tadeifelipe.wishlistapi.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class WishList {

    @Id
    private String id;
    @Indexed
    private String customer;
    private Product product;

    public WishList() {}

    public WishList(String id, String customerId, Product product) {
        this.id = id;
        this.customer = customerId;
        this.product = product;
    }

    public WishList(String customerId, Product product) {
        this.customer = customerId;
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishList wishList = (WishList) o;
        return Objects.equals(id, wishList.id) && Objects.equals(customer, wishList.customer) && Objects.equals(product, wishList.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, product);
    }

    @Override
    public String toString() {
        return "WishList{" +
                "id='" + id + '\'' +
                ", customer='" + customer + '\'' +
                ", product='" + product + '\'' +
                '}';
    }
}
