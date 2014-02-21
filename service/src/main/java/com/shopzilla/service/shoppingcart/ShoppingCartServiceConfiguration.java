/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc. 
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */
package com.shopzilla.service.shoppingcart;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shopzilla.service.shoppingcart.config.CustomHttpConfiguration;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Shopping cart service configuration.
 */
public class ShoppingCartServiceConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("database")
    private DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();

    @JsonProperty("http")
    private CustomHttpConfiguration httpConfiguration = new CustomHttpConfiguration();

    public DatabaseConfiguration getDatabaseConfiguration() {
        return databaseConfiguration;
    }

    public void setDatabaseConfiguration(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }

    public CustomHttpConfiguration getHttpConfiguration() {
        return httpConfiguration;
    }

    public void setHttpConfiguration(CustomHttpConfiguration httpConfiguration) {
        this.httpConfiguration = httpConfiguration;
    }
}
