package com.shopzilla.service.shoppingcart.resource;

/**
 * Created by mac on 14-10-27.
 */
public class Offer {
    private String image_url = "";
    private String redirect_url = "";
    public void setImage_url(String url) {
        this.image_url = url;
    }
    public void setRedirect_url(String url) {
        this.redirect_url = url;
    }
    public String getImage_url() {
        return this.image_url;
    }
    public String getRedirect_url() {
        return this.redirect_url;
    }
}
