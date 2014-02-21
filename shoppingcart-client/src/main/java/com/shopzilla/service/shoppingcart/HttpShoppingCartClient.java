/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc.
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */

package com.shopzilla.service.shoppingcart;

import com.shopzilla.site.service.shoppingcart.model.jaxb.ShoppingCartEntry;
import com.shopzilla.site.service.shoppingcart.model.jaxb.ShoppingCartResponse;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;

/**
 * @author Chris McAndrews <cmcandrews@shopzilla.com>
 */
public class HttpShoppingCartClient implements ShoppingCartClient {
    private final WebResource resource;

    public HttpShoppingCartClient(WebResource resource) {
        this.resource = resource;
    }

    @Override
    public ShoppingCartResponse getShoppingCartEntries(ShoppingCartQuery query)
            throws ShoppingCartException {

        WebResource request = resource.path(
                String.format("/shopperId/%d/productId/%d", query.getShopperId(), query.getProductId()));
        try {
            return request.accept(MediaType.APPLICATION_XML_TYPE).get(ShoppingCartResponse.class);
        } catch (UniformInterfaceException e) {
            throw new ShoppingCartException(e);
        } catch (ClientHandlerException e) {
            throw new ShoppingCartException(e);
        }
    }

    @Override
    public ShoppingCartResponse createShoppingCartEntry(ShoppingCartEntry entry)
            throws ShoppingCartException {

        WebResource request = resource.path("/create");

        try {
            return request.accept(MediaType.APPLICATION_XML_TYPE).entity(entry)
                    .post(ShoppingCartResponse.class);
        } catch (UniformInterfaceException e) {
            throw new ShoppingCartException(e);
        } catch (ClientHandlerException e) {
            throw new ShoppingCartException(e);
        }
    }

    @Override
    public ShoppingCartResponse updateShoppingCartEntry(ShoppingCartEntry entry)
            throws ShoppingCartException {
        WebResource request = resource.path(String.format("/shopperId/%d/productId/%d", entry.getShopperId(), entry.getProductId()));

        try {
            return request.accept(MediaType.APPLICATION_XML_TYPE).entity(entry)
                    .post(ShoppingCartResponse.class);
        } catch (UniformInterfaceException e) {
            throw new ShoppingCartException(e);
        } catch (ClientHandlerException e) {
            throw new ShoppingCartException(e);
        }
    }

    @Override
    public ShoppingCartResponse deleteShoppingCartEntry(ShoppingCartEntry entry)
            throws ShoppingCartException {

        WebResource request = resource.path("/delete");

        try {
            return request.accept(MediaType.APPLICATION_XML_TYPE)
                    .delete(ShoppingCartResponse.class);
        } catch (UniformInterfaceException e) {
            throw new ShoppingCartException(e);
        } catch (ClientHandlerException e) {
            throw new ShoppingCartException(e);
        }
    }
}
