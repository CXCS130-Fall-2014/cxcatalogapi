/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc.
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */

package com.shopzilla.service.shoppingcart.resource;

import com.google.common.collect.Lists;
import com.shopzilla.service.shoppingcart.data.ShoppingCartDao;
import com.shopzilla.service.shoppingcart.ShoppingCartQuery;
import com.shopzilla.site.service.shoppingcart.model.jaxb.ShoppingCartEntry;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * @author Chris McAndrews <cmcandrews@shopzilla.com>
 */
@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartResourceTest {
    private ShoppingCartResource resource;
    @Mock ShoppingCartDao dao;
    @Mock Mapper mapper;

    private static final Long SHOPPER_ID = 1L;
    private static final Long PRODUCT_ID = 123L;
    private static final String PRODUCT_NAME = "product name";
    private static final Long PRODUCT_COST = 199L;

    @Before
    public void setUp() throws Exception {
        resource = new ShoppingCartResource(dao, mapper);
    }

    @Test
    public void testGet() throws Exception {
        when(dao.getShoppingCartEntries(any(ShoppingCartQuery.class)))
                .thenReturn(Lists.<com.shopzilla.service.shoppingcart.data.ShoppingCartEntry>newArrayList());
        when(mapper.map(anyList(), eq(ShoppingCartEntry.class))).thenReturn(new ShoppingCartEntry());

        Response response = resource.get(SHOPPER_ID, null);
        assertNotNull(response.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        ArgumentCaptor<ShoppingCartQuery> captor = ArgumentCaptor.forClass(ShoppingCartQuery.class);
        verify(dao).getShoppingCartEntries(captor.capture());
        ShoppingCartQuery query = captor.getValue();
        assertEquals(SHOPPER_ID, query.getShopperId());
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testGetWithNoShopperId() throws Exception {
        Response response = resource.get(null, null);
        assertNull(response.getEntity());
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());

        verifyZeroInteractions(dao);
    }

    @Test
    public void testCreate() throws Exception {
        when(mapper.map(any(ShoppingCartEntry.class), eq(com.shopzilla.service.shoppingcart.data.ShoppingCartEntry.class)))
                .thenReturn(new com.shopzilla.service.shoppingcart.data.ShoppingCartEntry());
        when(dao.getShoppingCartEntries(any(ShoppingCartQuery.class))).thenReturn(null);
        ShoppingCartEntry shoppingCart = new ShoppingCartEntry();
        shoppingCart.setShopperId(SHOPPER_ID);
        shoppingCart.setProductId(PRODUCT_ID);
        shoppingCart.setProductName(PRODUCT_NAME);
        shoppingCart.setProductCost(PRODUCT_COST);

        Response response = resource.create(shoppingCart, null);
        assertNotNull(response.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        ArgumentCaptor<ShoppingCartQuery> captor = ArgumentCaptor.forClass(ShoppingCartQuery.class);
        verify(dao).getShoppingCartEntries(captor.capture());
        ShoppingCartQuery query = captor.getValue();
        assertEquals(SHOPPER_ID, query.getShopperId());
        assertEquals(PRODUCT_ID, query.getProductId());

        verify(dao).createShoppingCartEntry(any(com.shopzilla.service.shoppingcart.data.ShoppingCartEntry.class));
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testCreateWithExistingEntry() throws Exception {
        when(dao.getShoppingCartEntries(any(ShoppingCartQuery.class)))
                .thenReturn(Lists.newArrayList(new com.shopzilla.service.shoppingcart.data.ShoppingCartEntry()));
        ShoppingCartEntry shoppingCart = new ShoppingCartEntry();
        shoppingCart.setShopperId(SHOPPER_ID);
        shoppingCart.setProductId(PRODUCT_ID);
        shoppingCart.setProductName(PRODUCT_NAME);
        shoppingCart.setProductCost(PRODUCT_COST);

        Response response = resource.create(shoppingCart, null);
        assertNull(response.getEntity());
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
        verify(dao).getShoppingCartEntries(any(ShoppingCartQuery.class));
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testCreateWithNoShoppingCart() throws Exception {
        Response response = resource.create(null, null);
        assertNull(response.getEntity());
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
        verifyZeroInteractions(dao);
    }

    @Test
    public void testCreateWithNoShopperId() throws Exception {
        ShoppingCartEntry shoppingCart = new ShoppingCartEntry();
        shoppingCart.setProductId(PRODUCT_ID);

        Response response = resource.create(shoppingCart, null);
        assertNull(response.getEntity());
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
        verifyZeroInteractions(dao);
    }

    @Test
    public void testCreateWithNoProductId() throws Exception {
        ShoppingCartEntry shoppingCart = new ShoppingCartEntry();
        shoppingCart.setShopperId(SHOPPER_ID);

        Response response = resource.create(shoppingCart, null);
        assertNull(response.getEntity());
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
        verifyZeroInteractions(dao);
    }

    @Test
    public void testUpdate() throws Exception {
        when(dao.getShoppingCartEntries(any(ShoppingCartQuery.class)))
                .thenReturn(Lists.newArrayList(new com.shopzilla.service.shoppingcart.data.ShoppingCartEntry()));

        Response response = resource.update(SHOPPER_ID, PRODUCT_ID, PRODUCT_NAME, PRODUCT_COST, null);
        assertNotNull(response.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        ArgumentCaptor<ShoppingCartQuery> queryCaptor = ArgumentCaptor.forClass(ShoppingCartQuery.class);
        verify(dao).getShoppingCartEntries(queryCaptor.capture());
        ShoppingCartQuery query = queryCaptor.getValue();
        assertEquals(SHOPPER_ID, query.getShopperId());
        assertEquals(PRODUCT_ID, query.getProductId());

        ArgumentCaptor<com.shopzilla.service.shoppingcart.data.ShoppingCartEntry> modelCaptor =
                ArgumentCaptor.forClass(com.shopzilla.service.shoppingcart.data.ShoppingCartEntry.class);
        verify(dao).updateShoppingCartEntry(modelCaptor.capture());
        com.shopzilla.service.shoppingcart.data.ShoppingCartEntry shoppingCart = modelCaptor.getValue();
        assertEquals(PRODUCT_NAME, shoppingCart.getProductName());
        assertEquals(PRODUCT_COST, shoppingCart.getProductCost());

        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testUpdateWithNoShopperId() throws Exception {
        Response response = resource.update(null, PRODUCT_ID, PRODUCT_NAME, PRODUCT_COST, null);
        assertNull(response.getEntity());
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
        verifyZeroInteractions(dao);
    }

    @Test
    public void testUpdateWithNoProductId() throws Exception {
        Response response = resource.update(SHOPPER_ID, null, PRODUCT_NAME, PRODUCT_COST, null);
        assertNull(response.getEntity());
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
        verifyZeroInteractions(dao);
    }

    @Test
    public void testDelete() throws Exception {
        Response response = resource.delete(SHOPPER_ID, PRODUCT_ID, null);
        assertNotNull(response.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        ArgumentCaptor<ShoppingCartQuery> captor = ArgumentCaptor.forClass(ShoppingCartQuery.class);
        verify(dao).deleteShoppingCartEntry(captor.capture());
        ShoppingCartQuery query = captor.getValue();
        assertEquals(SHOPPER_ID, query.getShopperId());
        assertEquals(PRODUCT_ID, query.getProductId());
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testDeleteWithNoShopperId() throws Exception {
        Response response = resource.delete(null, PRODUCT_ID, null);
        assertNull(response.getEntity());
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
        verifyZeroInteractions(dao);
    }

    @Test
    public void testDeleteWithNoProductId() throws Exception {
        Response response = resource.delete(SHOPPER_ID, null, null);
        assertNull(response.getEntity());
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
        verifyZeroInteractions(dao);
    }
}
