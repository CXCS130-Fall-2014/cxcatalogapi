/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc. 
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */
package com.shopzilla.service.shoppingcart.data;

import com.google.common.base.Objects;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database model that represents a single item in a shopping cart.
 * @author Chris McAndrews
 */
public class ShoppingCartEntry {

    private Long shopperId;
    private Long productId;
    private String productName;
    private Long productCost;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductCost() {
        return productCost;
    }

    public void setProductCost(Long productCost) {
        this.productCost = productCost;
    }

    /**
     * Map database rows to the {@link com.shopzilla.service.shoppingcart.data.ShoppingCartEntry} class.
     */
    public static class ShoppingCartEntryMapper implements ResultSetMapper<ShoppingCartEntry> {
        @Override
        public ShoppingCartEntry map(int index, ResultSet rs, StatementContext stx) throws SQLException {
            final ShoppingCartEntry toReturn = new ShoppingCartEntry();
            toReturn.setShopperId(rs.getLong("shopper_id"));
            toReturn.setProductId(rs.getLong("product_id"));
            toReturn.setProductName(rs.getString("product_name"));
            toReturn.setProductCost(rs.getLong("product_cost"));
            return toReturn;
        }
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

        ShoppingCartEntry rhs = (ShoppingCartEntry) o;
        return Objects.equal(shopperId, rhs.shopperId) && Objects.equal(productId, rhs.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(shopperId, productId);
    }
}
