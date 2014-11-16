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
import org.json.*;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.net.*;
import java.io.*;
import java.util.Vector;

import com.shopzilla.service.shoppingcart.SQLAccess;
/*
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
*/
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
    @Path("apicall/shopperId/{shopperId}")
    public Response get(@PathParam("shopperId") Long shopperId,
                        @QueryParam("format") Format format) throws Exception {

        if (shopperId == null) {
            LOG.debug("A valid shopper id must be provided");
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

//        ShoppingCartResponse response = new ShoppingCartResponse();
//        ShoppingCartQuery query = ShoppingCartQuery.builder().shopperId(shopperId).build();
//        List<com.shopzilla.service.shoppingcart.data.ShoppingCartEntry> daoResults =
//                dao.getShoppingCartEntries(query);
//        for (com.shopzilla.service.shoppingcart.data.ShoppingCartEntry shoppingCart : daoResults) {
//            response.getShoppingCartEntry().add(mapper.map(shoppingCart, ShoppingCartEntry.class));
//        }

        Vector<String> new_tags = new Vector<String>();
        new_tags = getTags("clothes", "YW6bwCsUWy31u7ZWNkOGoBAeI4sqyKEgWT8Pnkhug2Z3y2MVcf", new_tags);
        int size = new_tags.size();
        System.out.println(size);
        for(int i = 0; i < size; i++){
            String new_keywords = new_tags.get(i).toString();
            new_tags = getTags(new_keywords, "YW6bwCsUWy31u7ZWNkOGoBAeI4sqyKEgWT8Pnkhug2Z3y2MVcf", new_tags);
        }
        System.out.println(new_tags);

        String catalog_response = "";
        URL apicall = new URL("http://catalog.bizrate.com/services/catalog/v1/us/product?apiKey=f94ab04178d1dea0821d5816dfb8af8d&publisherId=608865&keyword=shoes&results=20&resultsOffers=10&format=json");
        URLConnection ac = apicall.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        ac.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            catalog_response+=inputLine;
        in.close();
        Vector<Item> items = parseItems(catalog_response);
        System.out.println(items);
        return buildVectorResponse(items, format);
    }

    @Timed(name = "createShoppingCart")
    @POST
    @JSONP
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("create")
    public Response create(@Valid ShoppingCartEntry shoppingCart,
                           @QueryParam("format") Format format) {

        //Sticking debugging stuff in here


/*

        String driverName = "org.gjt.mm.mysql.Driver";
       // Class.forName(driverName);

        String serverName = "localhost";
        String mydatabase = "mydatabase";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

        String username = "username";
        String password = "password";
        Connection connection = DriverManager.getConnection(url, username, password);

*/

    System.out.println("LOOK HERE TOO!!!");
    //SQLAccess testConnection = new SQLAccess();
    //testConnection.testInsertTumblrTags();

    System.out.println("OMG LOOK AT ME!!!!!");


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
    private Response buildVectorResponse(Vector<Item> response, Format format) {
        return Response.ok(response)
                .type(format != null ? format.getMediaType() : Format.xml.getMediaType())
                .build();
    }

    private Vector<Item> parseItems(String response) {
        JSONObject obj = new JSONObject(response);
        JSONArray products = obj.getJSONObject("products").getJSONArray("product");

        Vector<Item> items = new Vector<Item>();
        for (int i = 0; i < products.length(); i++)
        {
            Item new_item = new Item();
            Vector<String> image_url = new Vector<String>();
            JSONObject cur_product = products.getJSONObject(i);
            JSONArray images = cur_product.getJSONObject("images").getJSONArray("image");
            for (int j = 0; j < images.length(); j++) {
                image_url.add(images.getJSONObject(j).get("value").toString());
            }
            String title = cur_product.get("title").toString();
            String description = cur_product.get("description").toString();
            String url = cur_product.getJSONObject("url").get("value").toString();

            new_item.setImage_url(image_url);
            new_item.setDescription(description);
            new_item.setRedirect_url(url);
            // todo add offers
            // todo add price range and price
            items.add(new_item);
        }
        return items;
    }
    //interact with tumblr api
    private Vector<String> getTags(String keyword, String api_key, Vector<String> old_tags) throws Exception{
        TumblrTags ttags = new TumblrTags();
        old_tags  = ttags.tumblrcalls(keyword, api_key, old_tags);
        return old_tags;
    }

    
}
