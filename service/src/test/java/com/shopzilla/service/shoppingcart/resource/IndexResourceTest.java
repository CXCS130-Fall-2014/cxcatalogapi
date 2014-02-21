/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc. 
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */
package com.shopzilla.service.shoppingcart.resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Oleksandr Murha
 * 
 * @since 06/09/2013
 */
public class IndexResourceTest {

    private IndexResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new IndexResource();
    }

    @Test
    public void test() {
        assertTrue(resource.handle() instanceof IndexResource.IndexView);
    }

}
