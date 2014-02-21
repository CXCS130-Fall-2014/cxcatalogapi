package com.shopzilla.service.shoppingcart.data;

import com.shopzilla.service.shoppingcart.ShoppingCartQuery;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * @author Rachel Baldovino <rbaldovino@shopzilla.com>
 * @created 1/22/14
 */
@RegisterMapper(ShoppingCartEntry.ShoppingCartEntryMapper.class)
public interface ShoppingCartDao extends Transactional<ShoppingCartDao> {
    /* Create */
    @SqlUpdate(
            " INSERT INTO shopping_cart_entry (shopper_id, product_id, product_name, product_cost) " +
                    " VALUES (:q.shopperId, :q.productId, :q.productName, :q.productCost)"
    )
    public void createShoppingCartEntry(@BindBean("q") ShoppingCartEntry shoppingCart);

    /* Read */
    @SqlQuery(
            "SELECT * FROM shopping_cart_entry WHERE shopper_id = :q.shopperId "
    )
    public List<ShoppingCartEntry> getShoppingCartEntries(@BindBean("q") ShoppingCartQuery query);

    /* Update */
    @SqlUpdate(
            " UPDATE shopping_cart_entry " +
                    " SET product_id = :q.productId, product_name = :q.productName, product_cost = :q.productCost " +
                    " WHERE shopper_id = :q.shopperId "
    )
    public int updateShoppingCartEntry(@BindBean("q") ShoppingCartEntry shoppingCart);

    /* Delete */
    @SqlUpdate(
            "DELETE from shopping_cart_entry WHERE shopper_id = :q.shopperId "
    )
    public int deleteShoppingCartEntry(@BindBean("q") ShoppingCartQuery query);
}
