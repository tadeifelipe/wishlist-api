package com.tadeifelipe.wishlistapi.dto;

import java.io.Serializable;

public record ProductRecord(String id, String name, Double price) implements Serializable {
}
