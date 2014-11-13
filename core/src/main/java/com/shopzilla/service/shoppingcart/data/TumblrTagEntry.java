package com.shopzilla.service.shoppingcart.data;

/**
 * Created by justin on 11/12/14.
 */

import com.google.common.base.Objects;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TumblrTagEntry {
    private String tumblrTag;
    private String tagDate;

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

    //Map the database rows to com.shopsilla.service.shoppingcart.data.TumblrTagEntry class
    public static class TumblrTagEntryMapper implements ResultSetMapper<TumblrTagEntry> {
        @Override
        public TumblrTagEntry map(int index, ResultSet rs, StatementContext stx) throws SQLException {
            final TumblrTagEntry toReturn = new TumblrTagEntry();
            toReturn.setTumblrTag(rs.getString("tumblr_tag"));
            toReturn.setTagDate(rs.getString("tag_date"));
            return toReturn;
        }
    }


    /* Reference ShoppingCartEntry.java */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() != getClass()) {
            return false;
        }

        TumblrTagEntry rhs = (TumblrTagEntry) o;
        return Objects.equal(tumblrTag, rhs.tumblrTag);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tumblrTag);
    }
}
