package com.shopzilla.service.shoppingcart;

import com.shopzilla.site.service.shoppingcart.model.jaxb.ShoppingCartEntry;
import com.shopzilla.site.service.shoppingcart.model.jaxb.ShoppingCartResponse;

/**
 * @author Chris McAndrews <cmcandrews@shopzilla.com>
 */
public interface ShoppingCartClient {
    ShoppingCartResponse getShoppingCartEntries(ShoppingCartQuery query) throws ShoppingCartException;
    ShoppingCartResponse createShoppingCartEntry(ShoppingCartEntry entry) throws ShoppingCartException;
    ShoppingCartResponse updateShoppingCartEntry(ShoppingCartEntry entry) throws ShoppingCartException;
    ShoppingCartResponse deleteShoppingCartEntry(ShoppingCartEntry entry) throws ShoppingCartException;
}
