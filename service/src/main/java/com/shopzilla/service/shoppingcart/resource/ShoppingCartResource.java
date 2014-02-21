/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc. 
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */
package com.shopzilla.service.shoppingcart.resource;

import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import com.shopzilla.service.shoppingcart.Format;
import com.shopzilla.service.shoppingcart.data.ShoppingCartDao;
import com.shopzilla.service.shoppingcart.ShoppingCartQuery;
import com.shopzilla.site.service.shoppingcart.model.jaxb.ShoppingCartEntry;
import com.shopzilla.site.service.shoppingcart.model.jaxb.ShoppingCartResponse;
import com.yammer.metrics.annotation.Timed;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Controller for handling CRUD operations for a shopping cart.
 * @author Chris McAndrews
 */
@Path("/shoppingcart")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
public class ShoppingCartResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartResource.class);

    private ShoppingCartDao dao;
    private Mapper mapper;

    public ShoppingCartResource(ShoppingCartDao dao, Mapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Timed(name = "getShoppingCart")
    @GET
    @JSONP
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("shopperId/{shopperId}")
    public Response get(@PathParam("shopperId") Long shopperId,
                        @QueryParam("format") Format format) {

        if (shopperId == null) {
            LOG.debug("A valid shopper id must be provided");
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        ShoppingCartResponse response = new ShoppingCartResponse();
        ShoppingCartQuery query = ShoppingCartQuery.builder().shopperId(shopperId).build();
        List<com.shopzilla.service.shoppingcart.data.ShoppingCartEntry> daoResults =
                dao.getShoppingCartEntries(query);
        for (com.shopzilla.service.shoppingcart.data.ShoppingCartEntry shoppingCart : daoResults) {
            response.getShoppingCartEntry().add(mapper.map(shoppingCart, ShoppingCartEntry.class));
        }
        return buildResponse(response, format);
    }

    @Timed(name = "createShoppingCart")
    @POST
    @JSONP
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("create")
    public Response create(@Valid ShoppingCartEntry shoppingCart,
                           @QueryParam("format") Format format) {

        if (shoppingCart == null || shoppingCart.getShopperId() == null
                || shoppingCart.getProductId() == null) {

            LOG.debug("A valid shopper id and product id must be provided");
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        // Verify that the entry doesn't already exist
        ShoppingCartQuery query = ShoppingCartQuery.builder()
                .shopperId(shoppingCart.getShopperId())
                .productId(shoppingCart.getProductId())
                .build();
        List<com.shopzilla.service.shoppingcart.data.ShoppingCartEntry> entries =
                dao.getShoppingCartEntries(query);
        if (CollectionUtils.isNotEmpty(entries)) {
            LOG.debug("A shopping cart entry with the given shopper id and product id already exists");
            return Response.status(Response.Status.CONFLICT).build();
        }

        ShoppingCartResponse response = new ShoppingCartResponse();
        dao.createShoppingCartEntry(mapper.map(shoppingCart, com.shopzilla.service.shoppingcart.data.ShoppingCartEntry.class));
        return buildResponse(response, format);
    }

    @Timed(name = "updateShoppingCart")
    @POST
    @JSONP
    @Path("shopperId/{shopperId}/productId/{productId}")
    public Response update(@PathParam("shopperId") Long shopperId,
                       @PathParam("productId") Long productId,
                       @QueryParam("productName") String productName,
                       @QueryParam("productCost") Long productCost,
                       @QueryParam("format") Format format) {

        if (shopperId == null || productId == null) {
            LOG.debug("A valid shopper id and product id must be provided");
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        // Check to make sure that the entry exists
        ShoppingCartQuery query = ShoppingCartQuery.builder()
                .shopperId(shopperId)
                .productId(productId)
                .build();
        List<com.shopzilla.service.shoppingcart.data.ShoppingCartEntry> shoppingCarts =
                dao.getShoppingCartEntries(query);
        if (CollectionUtils.isEmpty(shoppingCarts)) {
            LOG.debug("Could not find an existing shopping cart entry to update.");
            return Response.status(Response.Status.CONFLICT).build();
        }

        com.shopzilla.service.shoppingcart.data.ShoppingCartEntry shoppingCart = shoppingCarts.get(0);
        shoppingCart.setProductName(productName);
        shoppingCart.setProductCost(productCost);

        dao.updateShoppingCartEntry(shoppingCart);
        return buildResponse(new ShoppingCartResponse(), format);
    }

    @Timed(name = "deleteShoppingCart")
    @DELETE
    @JSONP
    @Path("shopperId/{shopperId}/productId/{productId}")
    public Response delete(@PathParam("shopperId") Long shopperId,
                       @PathParam("productId") Long productId,
                       @QueryParam("format") Format format) {

        if (shopperId == null || productId == null) {
            LOG.debug("A valid shopper id and product id must be provided");
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        ShoppingCartQuery shoppingCartQuery = ShoppingCartQuery.builder()
                .shopperId(shopperId)
                .productId(productId)
                .build();
        dao.deleteShoppingCartEntry(shoppingCartQuery);
        return buildResponse(new ShoppingCartResponse(), format);
    }

    private Response buildResponse(Object response, Format format) {
        return Response.ok(response)
                .type(format != null ? format.getMediaType() : Format.xml.getMediaType())
                .build();
    }
}
