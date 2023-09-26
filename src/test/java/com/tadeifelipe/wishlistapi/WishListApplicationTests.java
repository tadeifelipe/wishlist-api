package com.tadeifelipe.wishlistapi;

import com.tadeifelipe.wishlistapi.domain.Product;


public class WishListApplicationTests {

	public static Product getProductOne() {
		return new Product("5c4fb2b8-0c79-4443-affd-e22815888d7e", "Product One", 55.90);
	}

	public static Product getProductTwo() {
		return new Product("aa2eb0b6-ff40-4ad5-9c31-c2c3a2dd2fb6", "Product Two", 45.90);
	}

	public static Product getProductThree() {
		return new Product("4a8de092-c8a7-4be7-be84-97b9aae3adb7", "Product Three", 25.90);
	}

	public static String getIdCustomer() {
		return "123456";
	}
}
