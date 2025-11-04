package com.athar.ecommerce.product;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
        int id,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
