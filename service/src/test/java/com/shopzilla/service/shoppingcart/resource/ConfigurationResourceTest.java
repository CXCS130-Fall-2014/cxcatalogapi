/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc. 
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */
package com.shopzilla.service.shoppingcart.resource;

import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author Oleksandr Murha
 * 
 * @since 06/09/2013
 */
public class ConfigurationResourceTest extends ResourceTest {

    private Configuration configuration = new Configuration();

    @Override
    protected void setUpResources() {
        addResource(new ConfigurationResource(configuration));
    }

    @Test
    public void simpleResourceTest() throws Exception {
        assertThat(client().resource("/config-info").get(Configuration.class).getHttpConfiguration().getPort()).isEqualTo(8080);
    }
}
