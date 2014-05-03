package com.github.shoppinglist;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.fleax.shoppinglist.items.ItemBean;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemsIntegrationTest extends LocalServerIntegrationTest {

    @Test
    public void test1_list() {
	List<ItemBean> response = prepareRequest("/rest/items").get(
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertTrue(response.isEmpty());
    }

    @Test
    public void test2_create() {
	ItemBean item = new ItemBean();
	item.setCategory("Fruit");
	item.setName("Oranges");
	ItemBean bean = prepareRequest("/rest/items").post(Entity.json(item),
		ItemBean.class);
	Assert.assertNotNull(bean.getId());
	Assert.assertEquals("Fruit", bean.getCategory());
	Assert.assertEquals("Oranges", bean.getName());
    }

    @Test
    public void test3_list_and_get() {
	List<ItemBean> response = prepareRequest("/rest/items").get(
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(1, response.size());
	ItemBean bean = prepareRequest(
		"/rest/items/" + response.get(0).getId().toString()).get(
		ItemBean.class);
	Assert.assertNotNull(bean.getId());
	Assert.assertEquals("Fruit", bean.getCategory());
	Assert.assertEquals("Oranges", bean.getName());
    }
}
