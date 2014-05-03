package com.github.shoppinglist;

import java.util.List;

import javax.ws.rs.core.GenericType;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.fleax.shoppinglist.items.ItemBean;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemsIntegrationTest extends LocalServerIntegrationTest {

    private static final String REST_ITEMS = "/rest/items";
    private static final String ORANGES = "Oranges";
    private static final String FRUIT = "Fruit";

    @Test
    public void test1_list() {
	List<ItemBean> response = list(REST_ITEMS,
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertTrue(response.isEmpty());
    }

    @Test
    public void test2_create() {
	ItemBean item = new ItemBean(FRUIT, ORANGES);
	ItemBean bean = create(REST_ITEMS, item, ItemBean.class);
	Assert.assertNotNull(bean.getId());
	Assert.assertEquals(FRUIT, bean.getCategory());
	Assert.assertEquals(ORANGES, bean.getName());
    }

    @Test
    public void test3_list_and_get() {
	List<ItemBean> response = list(REST_ITEMS,
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(1, response.size());
	ItemBean bean = get(REST_ITEMS, response.get(0).getId(), ItemBean.class);
	Assert.assertNotNull(bean.getId());
	Assert.assertEquals(FRUIT, bean.getCategory());
	Assert.assertEquals(ORANGES, bean.getName());
    }
}
