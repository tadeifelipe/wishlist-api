package com.tadeifelipe.wishlistapi.controller;

import com.tadeifelipe.wishlistapi.dto.ProductIdRecord;
import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.dto.WishListRecord;
import com.tadeifelipe.wishlistapi.service.AddProductToWishListService;
import com.tadeifelipe.wishlistapi.service.DeleteProductOfWishListService;
import com.tadeifelipe.wishlistapi.service.GetAllProductsFromWishListService;
import com.tadeifelipe.wishlistapi.service.GetProductFromWishListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customer/wishlist/{customerId}")
public class WishListController {

    private final AddProductToWishListService addProductToWishList;
    private final DeleteProductOfWishListService deleteProductOfWishListService;

    private final GetAllProductsFromWishListService getAllProductsWishListService;

    private final GetProductFromWishListService getProductFromWishListService;

    Logger logger = LoggerFactory.getLogger(WishListController.class);

    @Autowired
    public WishListController(AddProductToWishListService wishListService, DeleteProductOfWishListService deleteProductOfWishListService,
                              GetAllProductsFromWishListService getAllProductsWishListService,
                              GetProductFromWishListService getProductFromWishListService) {
        this.addProductToWishList = wishListService;
        this.deleteProductOfWishListService = deleteProductOfWishListService;
        this.getAllProductsWishListService = getAllProductsWishListService;
        this.getProductFromWishListService = getProductFromWishListService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WishListRecord> addProduct(@PathVariable("customerId") final String customerId,
                                                     @RequestBody ProductIdRecord product) {
        logger.debug("Adding product to wishlist: " + product.id());

        var wishListRecord = addProductToWishList.add(customerId, product.id());

        return ResponseEntity.created(URI.create("/customer/wishlist/" + customerId)).body(wishListRecord) ;
    }

    @DeleteMapping(value = "/product={productId}")
    public ResponseEntity<WishListRecord> deleteProduct(@PathVariable("customerId") final String customerId,
                                        @PathVariable("productId") String productId) {
        logger.debug("Deleting product from wishlist:" + productId);

        var wishListDeleted = deleteProductOfWishListService.delete(customerId, productId);

        return ResponseEntity.ok(wishListDeleted);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductRecord>> getAllProducts(@PathVariable("customerId") String customerId) {
        logger.debug("Returning all products from wishlist:" + customerId);

        List<ProductRecord> products = getAllProductsWishListService.getAllProducts(customerId);

        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/product={productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductRecord> getProduct(@PathVariable("customerId") String customerId,
                                                    @PathVariable("productId") String product) {
        logger.debug("Returning product from wishlist:" + product);

        ProductRecord productRecord = getProductFromWishListService.getProduct(customerId, product);

        return ResponseEntity.ok(productRecord);
    }
}
