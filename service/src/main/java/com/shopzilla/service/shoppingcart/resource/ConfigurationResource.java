package com.shopzilla.service.shoppingcart.resource;

import com.yammer.dropwizard.config.Configuration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Configuration info controller.
 */
@Path("/config-info")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigurationResource {

    private Configuration configuration;

    public ConfigurationResource(Configuration configuration) {
        this.configuration = configuration;
    }

    @GET
    public Configuration handle() throws Exception {
        return configuration;
    }
}
