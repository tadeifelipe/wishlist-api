package com.tadeifelipe.wishlistapi.service;

import com.tadeifelipe.wishlistapi.clients.CustomerRestClient;
import com.tadeifelipe.wishlistapi.clients.ProductRestClient;
import com.tadeifelipe.wishlistapi.domain.Product;
import com.tadeifelipe.wishlistapi.domain.WishList;
import com.tadeifelipe.wishlistapi.domain.WishListMaxSize;
import com.tadeifelipe.wishlistapi.dto.CustomerRecord;
import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.exception.CustomerNotFoundException;
import com.tadeifelipe.wishlistapi.exception.ProductNotFoundException;
import com.tadeifelipe.wishlistapi.mapper.ProductMapper;
import com.tadeifelipe.wishlistapi.mapper.WishListMapper;
import com.tadeifelipe.wishlistapi.repository.WishListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

class AddProductToWishListServiceTest {

    private AddProductToWishListService addProductToWishListService;

    @Mock
    private WishListRepository wishListRepository;

    @Mock
    private CustomerRestClient customerRestClient;

    @Mock
    private ProductRestClient productRestClient;

    @Mock
    private WishListMaxSize wishListMaxSize;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        addProductToWishListService = new AddProductToWishListService(wishListRepository,
                new WishListMapper(new ProductMapper()), customerRestClient, productRestClient, wishListMaxSize);
    }

    @Test
    public void shouldAddProductToWishList() {
        CustomerRecord customerRecord = new CustomerRecord(getIdCustomer(), "Customer", "One");
        when(customerRestClient.findById(getIdCustomer())).thenReturn(Optional.of(customerRecord));

        ProductRecord productRecord = new ProductRecord(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice());
        when(productRestClient.findById(getProductOne().getId()))
                .thenReturn(Optional.of(productRecord));

        when(wishListMaxSize.getMaxSize()).thenReturn(2);

        var actualResponse = addProductToWishListService.add(getIdCustomer(), getProductOne().getId());

        verify(customerRestClient, times(1)).findById(getIdCustomer());
        verify(productRestClient, times(1)).findById(getProductOne().getId());

        assertThat(actualResponse, is(notNullValue()));
        assertThat(actualResponse.customer(), is(getIdCustomer()));
        assertThat(actualResponse.product(), is(productRecord));
    }

    @Test
    public void shouldThrowExceptionWhenCustomerNotFound() {
        when(customerRestClient.findById(getIdCustomer())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addProductToWishListService.add(getIdCustomer(),getProductOne().getId()))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageStartingWith("Customer not found");
    }

    @Test
    public void shouldThrowExceptionWhenProductNotFound() {
        CustomerRecord customerRecord = new CustomerRecord(getIdCustomer(), "Customer", "One");
        when(customerRestClient.findById(getIdCustomer())).thenReturn(Optional.of(customerRecord));

        when(productRestClient.findById(getProductOne().getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> addProductToWishListService.add(getIdCustomer(),getProductOne().getId()))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageStartingWith("Product not found");
    }

    @Test
    public void shouldThrowExceptionWhenProductAlreadyInserted() {
        CustomerRecord customerRecord = new CustomerRecord(getIdCustomer(), "Customer", "One");
        when(customerRestClient.findById(getIdCustomer())).thenReturn(Optional.of(customerRecord));

        ProductRecord productRecord = new ProductRecord(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice());
        when(productRestClient.findById(getProductOne().getId()))
                .thenReturn(Optional.of(productRecord));

        List<WishList> wishLists = List.of(new WishList(getIdCustomer(), new Product(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice())),
                new WishList(getIdCustomer(), new Product(getProductTwo().getId(), getProductTwo().getName(), getProductTwo().getPrice())));
        when(wishListRepository.findByCustomer(getIdCustomer()))
                .thenReturn(wishLists);

        assertThatThrownBy(() -> addProductToWishListService.add(getIdCustomer(),getProductOne().getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Product already inserted for this customer");
    }

    @Test
    public void shouldThrowExceptionWhenWishlistSizeExceeded() {
        CustomerRecord customerRecord = new CustomerRecord(getIdCustomer(), "Customer", "One");
        when(customerRestClient.findById(getIdCustomer())).thenReturn(Optional.of(customerRecord));

        ProductRecord productRecord = new ProductRecord(getProductThree().getId(), getProductThree().getName(), getProductThree().getPrice());
        when(productRestClient.findById(getProductThree().getId()))
                .thenReturn(Optional.of(productRecord));

        when(wishListMaxSize.getMaxSize()).thenReturn(2);

        List<WishList> wishLists = List.of(new WishList(getIdCustomer(), new Product(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice())),
                new WishList(getIdCustomer(), new Product(getProductTwo().getId(), getProductTwo().getName(), getProductTwo().getPrice())));
        when(wishListRepository.findByCustomer(getIdCustomer()))
                .thenReturn(wishLists);

        assertThatThrownBy(() -> addProductToWishListService.add(getIdCustomer(),getProductThree().getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Wishlist size exceeded");
    }
}