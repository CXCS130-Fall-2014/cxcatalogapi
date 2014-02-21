/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc. 
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */
package com.shopzilla.service.shoppingcart.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;

/**
 * Version controller.
 */
@Path("/version")
@Produces(MediaType.TEXT_PLAIN)
public class VersionResource {

    public static final String ERROR_MESSAGE = "Unable to read version information";
    public static final String VERSION_FILE_NAME = "/version.properties";

    @GET
    public String handle() throws IOException {
        InputStream versionStream = VersionResource.class.getResourceAsStream(VERSION_FILE_NAME);

        if (versionStream == null){
            return ERROR_MESSAGE;
        }

        return prepareVersionOutputString(versionStream);
    }

    private String prepareVersionOutputString(InputStream versionStream) throws IOException {
        StringBuffer versionBuffer = new StringBuffer();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(versionStream));

        String sCurrentLine;
        while ((sCurrentLine = bufferedReader.readLine()) != null) {
            versionBuffer.append(sCurrentLine).append("\n");
        }

        bufferedReader.close();
        versionStream.close();

        return versionBuffer.toString();
    }
}
