package com.athar.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductResponse(
        int id,
        String name,
        String description,
        BigDecimal price,
        double availableQuantity,
        Integer categoryId,
        String categoryName,
        String categoryDescription
) {
}
