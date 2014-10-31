package com.shopzilla.service.shoppingcart.resource;

/**
 * Created by mac on 14-10-27.
 */
import java.util.Vector;
public class Item {
    private Vector<Offer> offers = new Vector<Offer>();
    private Vector<String> image_url = new Vector<String>();
    private String redirect_url = "";
    private String title = "";
    private String description = "";
    private Vector<Integer> price_range = new Vector<Integer>();
    private int price = 0;

    //setters
    public void setOffers(Vector<Offer> offers) {
        this.offers = offers;
    }
    public void setImage_url(Vector<String> url) {
        this.image_url = url;
    }
    public void setRedirect_url(String url) {
        this.redirect_url = url;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice_range(Vector<Integer> price_range) {
        this.price_range = price_range;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    // getters
    public Vector<Offer> getOffers() {
        return this.offers;
    }
    public Vector<String> getImage_url() {
        return this.image_url;
    }
    public String getRedirect_url() {
        return this.redirect_url;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDescription() {
        return this.description;
    }
    public Vector getPrice_range() {
        return this.price_range;
    }
    public int getPrice() {
        return this.price;
    }
}
