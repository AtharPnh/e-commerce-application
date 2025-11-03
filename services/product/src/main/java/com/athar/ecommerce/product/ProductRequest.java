package com.athar.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        int id,
        @NotNull(message = "Product name is required.")
        String name,
        @NotNull(message = "Product description is required.")
        String description,
        @Positive(message = "Product price should be positive")
        BigDecimal price,
        @Positive(message = "Product available quantity should be positive")
        double availableQuantity,
        @NotNull(message = "Product category id is required.")
        Integer categoryId
) {
}
