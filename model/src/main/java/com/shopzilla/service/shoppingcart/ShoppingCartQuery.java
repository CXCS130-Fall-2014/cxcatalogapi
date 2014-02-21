/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc. 
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */
package com.shopzilla.service.shoppingcart;

import com.google.common.base.Objects;

/**
 * Query parameters for the shopping cart service.
 * @author Chris McAndrews
 */
public class ShoppingCartQuery {

    private Long shopperId;
    private Long productId;

    private ShoppingCartQuery() {
        // use the builder
    }

    public Long getShopperId() {
        return shopperId;
    }

    public void setShopperId(Long shopperId) {
        this.shopperId = shopperId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() != getClass()) {
            return false;
        }

        ShoppingCartQuery rhs = (ShoppingCartQuery) o;
        return Objects.equal(shopperId, rhs.shopperId)
                && Objects.equal(productId, rhs.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(shopperId, productId);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(ShoppingCartQuery.class)
                .add("shopperId", shopperId)
                .add("productId", productId)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final ShoppingCartQuery q = new ShoppingCartQuery();

        public Builder shopperId(Long shopperId) {
            q.shopperId = shopperId;
            return this;
        }

        public Builder productId(Long productId) {
            q.productId = productId;
            return this;
        }

        public ShoppingCartQuery build() {
            return q;
        }
    }
}
