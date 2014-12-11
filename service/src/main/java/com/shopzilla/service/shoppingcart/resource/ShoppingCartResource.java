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
import java.util.HashMap;
import java.util.List;
import java.net.*;
import java.io.*;
import java.util.Map;
import java.util.Vector;
import java.lang.Object;
import java.text.NumberFormat;
import java.util.Locale;

import com.shopzilla.service.shoppingcart.SQLAccess;
//import com.shopzilla.service.shoppingcart.resource.ranking;


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
                        @QueryParam("format") Format format,
                        @QueryParam("load") Integer load,
                        @QueryParam("category") String category) throws Exception {

        if (shopperId == null) {
            LOG.debug("A valid shopper id must be provided");
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        /* TESTING
        Map<String, Integer> hello = new HashMap<String, Integer>();
        hello.put("mario", 10);
        hello.put("test2", 14);

        SQLAccess fart = new SQLAccess();
        System.out.println("TEST" + fart.getPopularTags("test1"));
        fart.insertPopularTags(hello);
        */
        //System.out.println("OKAY TEST....");
        //ranking ranking_obj = new ranking();
        //ranking_obj.run();

//        ShoppingCartResponse response = new ShoppingCartResponse();
//        ShoppingCartQuery query = ShoppingCartQuery.builder().shopperId(shopperId).build();
//        List<com.shopzilla.service.shoppingcart.data.ShoppingCartEntry> daoResults =
//                dao.getShoppingCartEntries(query);
//        for (com.shopzilla.service.shoppingcart.data.ShoppingCartEntry shoppingCart : daoResults) {
//            response.getShoppingCartEntry().add(mapper.map(shoppingCart, ShoppingCartEntry.class));
//        }

        Vector<String> new_tags = new Vector<String>();
        //new_tags = getTags("clothes", "YW6bwCsUWy31u7ZWNkOGoBAeI4sqyKEgWT8Pnkhug2Z3y2MVcf", new_tags, 10);
        new_tags = getTags(category, "YW6bwCsUWy31u7ZWNkOGoBAeI4sqyKEgWT8Pnkhug2Z3y2MVcf", new_tags, 10);
        int size = new_tags.size();
        for(int i = 0; i < size; i++){
            String new_keywords = new_tags.get(i).toString();
            new_tags = getTags(new_keywords, "YW6bwCsUWy31u7ZWNkOGoBAeI4sqyKEgWT8Pnkhug2Z3y2MVcf", new_tags, 10);
        }

        // System.out.println(new_tags);
        String url = "http://catalog.bizrate.com/services/catalog/v1/us/product?apiKey=f94ab04178d1dea0821d5816dfb8af8d&publisherId=608865&keyword=";
        String url_end = "&results=1&resultsOffers=1&format=json";
        Vector<String> keyword_urls = new Vector<String>();
        for(int j = 0; j < new_tags.size(); j++) {
            String tag = new_tags.elementAt(j);
            if (tag != null && !tag.isEmpty()) {
                String encoded = tag.replaceAll(" ", "%20");
                if (!isAlpha(encoded)) {
                    continue;
                }
                String url_formatted = url + encoded + url_end;
                keyword_urls.add(url_formatted);
            }
        }
        Vector<Item> all_items = new Vector<Item>();
        for(int k = 0; k < keyword_urls.size(); k++) {
            String catalog_response = "";
            String api_url = keyword_urls.elementAt(k);
            URL api_call = new URL(api_url);
            URLConnection ac = api_call.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            ac.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                catalog_response += inputLine;
            in.close();
            Vector<Item> items = parseItems(catalog_response);
//            for (int i =0; i < items.size(); i ++) {
//                all_items.addAll(items.elementAt(i).getImage_url());
//            }
            all_items.addAll(items);
        }
        //System.out.println("done");
        //return buildVectorResponse(all_items, format);
        Vector<Item> new_items = new Vector<Item>();
        if ((load+1)*10 < all_items.size()) {
            for (int i = load*10; i < load*10+10; i++) {
                new_items.add(all_items.get(i));
            }
        } else if (load*10 < all_items.size()) {
            for (int i = load*10; i < all_items.size(); i++) {
                new_items.add(all_items.get(i));
            }
        }

        //System.out.println(new_items);

        return buildVectorResponse(new_items, format);
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
    private Response buildVectorResponse(Vector<Item> response, Format format) {
        System.out.println(Response.ok(response)
                .type(format != null ? format.getMediaType() : Format.xml.getMediaType())
                .build());
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
            if (images.length() == 0) {
                continue;
            }
            for (int j = 0; j < images.length(); j++) {
                image_url.add(images.getJSONObject(j).get("value").toString());
            }
            // System.out.println(image_url);
            String title = cur_product.get("title").toString();
            String description = cur_product.has("description") ? cur_product.get("description").toString() : "";
            String url = cur_product.getJSONObject("url").get("value").toString();
            String price_dollar = cur_product.has("price") ? cur_product.getJSONObject("price").get("value").toString() : "";
            double price = 0;
            if (price_dollar != "") {
                //NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                //Number number = format.parse(price_dollar.substring(1));
                //price = number.doubleValue();
                price = Double.parseDouble(price_dollar.substring(1).replaceAll(",",""));
                System.out.println(price);
            }

            new_item.setImage_url(image_url);
            new_item.setTitle(title);
            new_item.setDescription(description);
            new_item.setRedirect_url(url);
            new_item.setPrice(price);
            // todo add offers
            // todo add price range and price
            items.add(new_item);
        }
        return items;
    }
    //interact with tumblr api
    private Vector<String> getTags(String keyword, String api_key, Vector<String> old_tags, int count) throws Exception{
        TumblrTags ttags = new TumblrTags();
        old_tags  = ttags.tumblrcalls(keyword, api_key, old_tags, count);
        return old_tags;
    }

    public boolean isAlpha(String tag) {
        char[] chars = tag.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c) && c!='%' && !Character.isDigit(c)) {
                return false;
            }
        }
        // System.out.println(tag);

        return true;
    }
    
}
