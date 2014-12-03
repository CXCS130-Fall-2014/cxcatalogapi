/**
 * Copyright (C) 2004 - 2013 Shopzilla, Inc. 
 * All rights reserved. Unauthorized disclosure or distribution is prohibited.
 */
package com.shopzilla.service.shoppingcart.resource;

import java.util.ArrayList;
import java.util.List;

import com.yammer.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File; //for test

/**
 * Index page controller.
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class IndexResource {
    @GET
    public IndexView handle() {
        IndexView view = new IndexView("/index.mustache");
        
        view.setHelloWorldMsg("Hello World!");

        
        //List<List<String>> worlds = new ArrayList<List<String>>();
        List<String> worlds = new ArrayList<String>();

        // for test
        File folder = new File("src/main/resources/assets/static/img");

          File curdir = new File(new File(".").getAbsolutePath());

        File[] listOfFiles = folder.listFiles();

        //int j = 0;
        //worlds.add(new ArrayList<String>());

        for (int i = 0; i < listOfFiles.length; i++) {
                String filePath = "style=background:url(/assets/static/img/" + listOfFiles[i].getName() + ");";
                worlds.add(filePath);
                /*worlds.get(j).add(filePath);
                if (j % 10 == 0) {
                    j++;
                    worlds.add(new ArrayList<String>());
                }*/
        }
        view.setWorlds(worlds);
        
        return view;
    }

    public static class IndexView extends View {

        private String helloWorldMsg;
        private List<String> worlds;
        //private List<List<String>> worlds;
        
        protected IndexView(String templateName) {
            super(templateName);
        }

        public void setHelloWorldMsg(String helloWorldMsg) {
            this.helloWorldMsg = helloWorldMsg;
        }
        
        public String getHelloWorldMsg() {
            return this.helloWorldMsg;
        }
    
        public void setWorlds(List<String> worlds) {
        //public void setWorlds(List<List<String>> worlds) {
            this.worlds = worlds;
        }
        
        public List<String> getWorlds() {
            return this.worlds;
        }
        
    }
}


