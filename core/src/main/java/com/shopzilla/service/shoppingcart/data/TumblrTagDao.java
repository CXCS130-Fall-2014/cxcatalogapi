package com.shopzilla.service.shoppingcart.data;

import com.shopzilla.service.shoppingcart.TumblrTagQuery;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;
import java.util.List;
/**
 * Created by justin on 11/12/14.
 */
@RegisterMapper(TumblrTagEntry.TumblrTagEntryMapper.class)
public interface TumblrTagDao extends Transactional<TumblrTagDao> {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Create Tumblr Data with tag and date */
    @SqlUpdate(
            "INSERT INTO tumblr_tags_entry (tumblr_tag, tag_date)" +
                    "VALUES (:q.tumblrTag, :q.tagDate)"
    )
    public void createTumblrTagEntry(@BindBean("q") TumblrTagEntry tumblrTag);

    /* Read Tumblr Data, filter how many you want */
    @SqlUpdate(
            "SELECT * FROM tumblr_tags_entry LIMIT :q.number"
    )
    public List<TumblrTagEntry> getTumblrTagEntries(@BindBean("q") TumblrTagQuery query);

    /* Update */
    //TODO

    /* Delete */
    //TODO

}
