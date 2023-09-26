package com.tadeifelipe.wishlistapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tadeifelipe.wishlistapi.auth.SecurityConfiguration;
import com.tadeifelipe.wishlistapi.dto.ProductIdRecord;
import com.tadeifelipe.wishlistapi.dto.ProductRecord;
import com.tadeifelipe.wishlistapi.dto.WishListRecord;
import com.tadeifelipe.wishlistapi.exception.CustomerNotFoundException;
import com.tadeifelipe.wishlistapi.exception.ProductNotFoundException;
import com.tadeifelipe.wishlistapi.service.AddProductToWishListService;
import com.tadeifelipe.wishlistapi.service.DeleteProductOfWishListService;
import com.tadeifelipe.wishlistapi.service.GetAllProductsFromWishListService;
import com.tadeifelipe.wishlistapi.service.GetProductFromWishListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static com.tadeifelipe.wishlistapi.WishListApplicationTests.*;
import static io.lettuce.core.KillArgs.Builder.user;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(WishListController.class)
@ExtendWith(SpringExtension.class)
@Import(SecurityConfiguration.class)
class WishListControllerAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddProductToWishListService addProductToWishListService;

    @MockBean
    private DeleteProductOfWishListService deleteProductOfWishListService;

    @MockBean
    private GetAllProductsFromWishListService getAllProductsFromWishListService;

    @MockBean
    private GetProductFromWishListService getProductFromWishListService;

    @Test
    @WithMockUser("admin")
    public void shouldAddProductToWishList() throws Exception {
        WishListRecord response = new WishListRecord(getIdCustomer(),
                new ProductRecord(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice()));
        when(addProductToWishListService.add(getIdCustomer(), getProductOne().getId()))
                .thenReturn(response);

        ProductIdRecord productId = new ProductIdRecord(getProductOne().getId());

        mockMvc
               .perform(post("/customer/wishlist/"+ getIdCustomer())
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(new ObjectMapper().writeValueAsBytes(productId)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.customer").value(getIdCustomer()))
                .andExpect(jsonPath("$.product.id").value(getProductOne().getId()))
                .andExpect(jsonPath("$.product.name").value(getProductOne().getName()))
                .andExpect(jsonPath("$.product.price").value(getProductOne().getPrice()));
    }

    @Test
    @WithMockUser("admin")
    public void shouldReturnNotFoundWhenThrowCustomerNotFoundException() throws Exception {
        when(addProductToWishListService.add(getIdCustomer(), getProductOne().getId()))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        ProductIdRecord productId = new ProductIdRecord(getProductOne().getId());

        mockMvc
                .perform(post("/customer/wishlist/"+ getIdCustomer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(productId)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found"));
    }

    @Test
    @WithMockUser("admin")
    public void shouldReturnNotFoundWhenThrowProductNotFoundException() throws Exception {
        when(addProductToWishListService.add(getIdCustomer(), getProductOne().getId()))
                .thenThrow(new ProductNotFoundException("Product not found"));

        ProductIdRecord productId = new ProductIdRecord(getProductOne().getId());

        mockMvc
                .perform(post("/customer/wishlist/"+ getIdCustomer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(productId)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"));
    }

    @Test
    @WithMockUser(username = "admin")
    public void shouldReturnNotFoundWhenThrowIllegalException() throws Exception {
        when(addProductToWishListService.add(getIdCustomer(), getProductOne().getId()))
                .thenThrow(new IllegalArgumentException());

        ProductIdRecord productId = new ProductIdRecord(getProductOne().getId());

        mockMvc
                .perform(post("/customer/wishlist/"+ getIdCustomer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(productId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser("admin")
    public void shouldDeleteProductFromWishList() throws Exception {
        WishListRecord response = new WishListRecord(getIdCustomer(),
                new ProductRecord(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice()));
        when(deleteProductOfWishListService.delete(getIdCustomer(), getProductOne().getId()))
                .thenReturn(response);

        mockMvc
                .perform(delete("/customer/wishlist/"+ getIdCustomer() + "/product=" + getProductOne().getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.customer").value(getIdCustomer()))
                .andExpect(jsonPath("$.product.id").value(getProductOne().getId()))
                .andExpect(jsonPath("$.product.name").value(getProductOne().getName()))
                .andExpect(jsonPath("$.product.price").value(getProductOne().getPrice()));
    }

    @Test
    @WithMockUser("admin")
    public void shouldReturnNotWhenDeleteAndThrowProductNotFoundException() throws Exception {
        when(deleteProductOfWishListService.delete(getIdCustomer(), getProductOne().getId()))
                .thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc
                .perform(delete("/customer/wishlist/"+ getIdCustomer() + "/product=" + getProductOne().getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"));
    }

    @Test
    @WithMockUser("admin")
    public void shouldReturnAllProductsFromWishList() throws Exception {
        List<ProductRecord> response = List.of(
                new ProductRecord(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice()),
                new ProductRecord(getProductTwo().getId(), getProductTwo().getName(), getProductTwo().getPrice()),
                new ProductRecord(getProductThree().getId(), getProductThree().getName(), getProductThree().getPrice())
                );

        when(getAllProductsFromWishListService.getAllProducts(getIdCustomer()))
                .thenReturn(response);

        mockMvc
                .perform(get("/customer/wishlist/"+ getIdCustomer()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @WithMockUser("admin")
    public void shouldProductsFromWishList() throws Exception {
        ProductRecord response = new ProductRecord(getProductOne().getId(), getProductOne().getName(), getProductOne().getPrice());

        when(getProductFromWishListService.getProduct(getIdCustomer(), getProductOne().getId()))
                .thenReturn(response);

        mockMvc
                .perform(get("/customer/wishlist/"+ getIdCustomer() +"/product="+ getProductOne().getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(getProductOne().getId()))
                .andExpect(jsonPath("$.name").value(getProductOne().getName()))
                .andExpect(jsonPath("$.price").value(getProductOne().getPrice()));
    }
}