package com.tadeifelipe.wishlistapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomerRecord(String id, String name, String lastName) {
}
