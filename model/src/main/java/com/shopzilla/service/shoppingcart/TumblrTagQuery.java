package com.shopzilla.service.shoppingcart;

import com.google.common.base.Objects;

/**
 * Query parameters for Tumblr Tag Data
 * Created by justin on 11/12/14.
 */
public class TumblrTagQuery {

    private String tumblrTag;
    private String tagDate;

    private TumblrTagQuery() {
        //
    }

    //Accessors
    public String getTumblrTag() {
        return tumblrTag;
    }
    public String getTagDate() {
        return tagDate;
    }
    //Mutators
    public void setTumblrTag(String tumblrTag) {
        this.tumblrTag = tumblrTag;
    }
    public void setTagDate(String tagDate) {
        this.tagDate = tagDate;
    }

    //@Override
    public boolean equals(Objects o) {
        if (o == null) {
            return false;
        }
        /*
        if (o == this) {
            return true;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        */
        //Do I really need this? Set as false for now. Maybe duplicates are okay : / ..I'll decide later
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tumblrTag);
    }

    //Do I need a toString?

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final TumblrTagQuery q = new TumblrTagQuery();

        public Builder tumblrTag(String tumblrTag) {
            q.tumblrTag = tumblrTag;
            return this;
        }

        public Builder tagDate(String tagDate) {
            q.tagDate = tagDate;
            return this;
        }

        public TumblrTagQuery build() {
            return q;
        }
    }



}
