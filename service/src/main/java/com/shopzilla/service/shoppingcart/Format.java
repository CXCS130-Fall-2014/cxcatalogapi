/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc.
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */

package com.shopzilla.service.shoppingcart;

import javax.ws.rs.core.MediaType;

/**
 * @author Chris McAndrews <cmcandrews@shopzilla.com>
 */
public enum Format {
    json(MediaType.APPLICATION_JSON_TYPE),
    xml(MediaType.APPLICATION_XML_TYPE);

    private final MediaType mediaType;

    private Format(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
