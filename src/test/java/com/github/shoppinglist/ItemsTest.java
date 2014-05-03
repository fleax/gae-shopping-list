package com.github.shoppinglist;

import java.util.ArrayList;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import com.github.fleax.shoppinglist.items.ItemBean;

public class ItemsTest extends LocalServerIntegrationTest {

    @Test
    public void list() {
	@SuppressWarnings("unchecked")
	ArrayList<ItemBean> response = prepareRequest("/rest/items").get(
		ArrayList.class);
	Assert.assertTrue(response.isEmpty());
    }

    @Test
    public void create() {
	ItemBean item = new ItemBean();
	item.setCategory("Fruit");
	item.setName("Oranges");
	Response response = prepareRequest("/rest/items").post(
		Entity.json(item));
	Assert.assertEquals(200, response.getStatus());
    }
}
