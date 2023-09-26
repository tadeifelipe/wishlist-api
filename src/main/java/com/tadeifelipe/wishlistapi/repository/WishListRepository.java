package com.tadeifelipe.wishlistapi.repository;

import com.tadeifelipe.wishlistapi.domain.WishList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends MongoRepository<WishList, String> {

    Optional<WishList> findByCustomerAndProductId(String customer, String productId);

    List<WishList> findByCustomer(String customer);
}
