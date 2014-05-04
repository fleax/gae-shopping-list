package com.github.shoppinglist;

import static com.github.shoppinglist.Constants.APPLES;
import static com.github.shoppinglist.Constants.DRINKS;
import static com.github.shoppinglist.Constants.FRUIT;
import static com.github.shoppinglist.Constants.ORANGES;
import static com.github.shoppinglist.Constants.REST_ITEMS;
import static com.github.shoppinglist.Constants.REST_LISTS;
import static com.github.shoppinglist.Constants.WATER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;

import org.glassfish.jersey.client.ClientResponse;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.fleax.shoppinglist.items.ItemBean;
import com.github.fleax.shoppinglist.lists.ListBean;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ListsIntegrationTest extends LocalServerIntegrationTest {

    @Test
    public void test01_list() {
	List<ListBean> response = list(REST_LISTS,
		new GenericType<List<ListBean>>() {
		});
	assertTrue(response.isEmpty());
    }

    @Test
    public void test02_create() {
	ListBean list = create(REST_LISTS, new ListBean(), ListBean.class);
	assertEmptyList(list);
    }

    @Test
    public void test03_list_and_get() {
	List<ListBean> response = list(REST_LISTS,
		new GenericType<List<ListBean>>() {
		});
	assertEquals(1, response.size());
	ListBean list = get(REST_LISTS, response.get(0).getId(), ListBean.class);
	assertEmptyList(list);
    }

    @Test(expected = NotFoundException.class)
    public void test04_get_not_found() {
	prepareRequest(REST_LISTS + "/" + Long.MAX_VALUE).get(
		ClientResponse.class);
    }

    @Test
    public void test05_addItems() {
	// Create items
	ItemBean water = prepareRequest(REST_ITEMS).post(
		Entity.json(new ItemBean(DRINKS, WATER)), ItemBean.class);
	ItemBean oranges = prepareRequest(REST_ITEMS).post(
		Entity.json(new ItemBean(FRUIT, ORANGES)), ItemBean.class);
	ItemBean apples = prepareRequest(REST_ITEMS).post(
		Entity.json(new ItemBean(FRUIT, APPLES)), ItemBean.class);

	// Create list
	ListBean list = create(REST_LISTS, new ListBean(), ListBean.class);

	// Add to list
	ClientResponse response = prepareRequest(
		REST_LISTS + "/" + list.getId() + "/add").put(
		Entity.json(water), ClientResponse.class);
	assertEquals(1, list.getItems().size());
	assertTrue(list.getItems().contains(new ItemBean(DRINKS, WATER)));
	list = prepareRequest(REST_LISTS + "/" + list.getId() + "/add").put(
		Entity.json(oranges), ListBean.class);
	assertEquals(2, list.getItems().size());
	assertTrue(list.getItems().contains(new ItemBean(FRUIT, ORANGES)));
	list = prepareRequest(REST_LISTS + "/" + list.getId() + "/add").put(
		Entity.json(apples), ListBean.class);
	assertEquals(3, list.getItems().size());
	assertTrue(list.getItems().contains(new ItemBean(FRUIT, APPLES)));
    }

    private void assertEmptyList(ListBean list) {
	assertNotNull(list.getId());
	assertNotNull(list.getDate());
	assertTrue(list.getCheckedItems().isEmpty());
	assertTrue(list.getItems().isEmpty());
	assertNull(list.getUser());
    }

}
