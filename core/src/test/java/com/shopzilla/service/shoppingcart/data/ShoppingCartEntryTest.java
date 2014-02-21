/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc.
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */

package com.shopzilla.service.shoppingcart.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Chris McAndrews <cmcandrews@shopzilla.com>
 */
public class ShoppingCartEntryTest {
    @Test
    public void testSettersAndGetters() {
        ShoppingCartEntry shoppingCart = new ShoppingCartEntry();

        shoppingCart.setShopperId(123L);
        assertEquals(new Long(123L), shoppingCart.getShopperId());

        shoppingCart.setProductId(456L);
        assertEquals(new Long(456L), shoppingCart.getProductId());

        shoppingCart.setProductName("product name");
        assertEquals("product name", shoppingCart.getProductName());

        shoppingCart.setProductCost(199L);
        assertEquals(new Long(199L), shoppingCart.getProductCost());
    }
}
