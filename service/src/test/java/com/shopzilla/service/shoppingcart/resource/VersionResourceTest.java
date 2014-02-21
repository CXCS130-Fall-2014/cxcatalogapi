/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc. 
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */
package com.shopzilla.service.shoppingcart.resource;

import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Oleksandr Murha
 * 
 * @since 06/09/2013
 */
public class VersionResourceTest extends ResourceTest {

    @Override
    protected void setUpResources() {
        addResource(new VersionResource());
    }

    @Test
    public void simpleResourceTest() throws Exception {
        assertThat(client().resource("/version").get(String.class)).contains("GIT_HASH");
    }
}
