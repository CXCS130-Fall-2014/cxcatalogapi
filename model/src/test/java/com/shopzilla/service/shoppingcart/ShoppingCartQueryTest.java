/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc.
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */

package com.shopzilla.service.shoppingcart;

import com.google.common.base.Objects;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Chris McAndrews <cmcandrews@shopzilla.com>
 */
public class ShoppingCartQueryTest {
    private static final Long SHOPPER_ID = 1L;
    private static final Long PRODUCT_ID = 2L;

    @Test
    public void testBuilder() {
        ShoppingCartQuery query = ShoppingCartQuery.builder()
                .shopperId(SHOPPER_ID)
                .productId(PRODUCT_ID)
                .build();
        assertEquals(SHOPPER_ID, query.getShopperId());
        assertEquals(PRODUCT_ID, query.getProductId());
    }

    @Test
    public void testSetters() {
        ShoppingCartQuery query = ShoppingCartQuery.builder().build();
        query.setShopperId(SHOPPER_ID);
        query.setProductId(PRODUCT_ID);
        assertEquals(SHOPPER_ID, query.getShopperId());
        assertEquals(PRODUCT_ID, query.getProductId());
    }

    @Test
    public void testToString() {
        ShoppingCartQuery query = ShoppingCartQuery.builder()
                .shopperId(SHOPPER_ID)
                .productId(PRODUCT_ID)
                .build();
        String expected = Objects.toStringHelper(ShoppingCartQuery.class)
                .add("shopperId", SHOPPER_ID)
                .add("productId", PRODUCT_ID)
                .toString();
        assertEquals(expected, query.toString());
    }

    @Test
    public void testEquals() {
        ShoppingCartQuery query1 = ShoppingCartQuery.builder()
                .shopperId(SHOPPER_ID)
                .productId(PRODUCT_ID)
                .build();
        assertFalse(query1.equals(null));
        assertTrue(query1.equals(query1));
        assertFalse(query1.equals(1));

        ShoppingCartQuery query2 = ShoppingCartQuery.builder().build();

        assertFalse(query1.equals(query2));
        query2.setShopperId(SHOPPER_ID);
        assertFalse(query1.equals(query2));
        query2.setProductId(PRODUCT_ID);
        assertTrue(query1.equals(query2));
    }

    @Test
    public void testHashCode() {
        ShoppingCartQuery query1 = ShoppingCartQuery.builder()
                .shopperId(SHOPPER_ID)
                .productId(PRODUCT_ID)
                .build();
        ShoppingCartQuery query2 = ShoppingCartQuery.builder()
                .shopperId(SHOPPER_ID)
                .productId(PRODUCT_ID)
                .build();
        assertEquals(query1.hashCode(), query2.hashCode());
    }
}
