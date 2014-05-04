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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ListsIntegrationTest extends LocalServerIntegrationTest {

    @Test
    public void test01_list() {
	List<ClientListBean> response = list(REST_LISTS,
		new GenericType<List<ClientListBean>>() {
		});
	assertTrue(response.isEmpty());
    }

    @Test
    public void test02_create() {
	ClientListBean list = create(REST_LISTS, new ClientListBean(),
		ClientListBean.class);
	assertEmptyList(list);
    }

    @Test
    public void test03_list_and_get() {
	List<ClientListBean> response = list(REST_LISTS,
		new GenericType<List<ClientListBean>>() {
		});
	assertEquals(1, response.size());
	ClientListBean list = get(REST_LISTS, response.get(0).getId(),
		ClientListBean.class);
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
	ClientListBean list = create(REST_LISTS, new ClientListBean(),
		ClientListBean.class);

	// Add to list
	list = prepareRequest(REST_LISTS + "/" + list.getId() + "/add").put(
		Entity.json(water), ClientListBean.class);
	assertEquals(1, list.getItems().size());
	assertTrue(list.getItems().contains(new ItemBean(DRINKS, WATER)));
	list = prepareRequest(REST_LISTS + "/" + list.getId() + "/add").put(
		Entity.json(oranges), ClientListBean.class);
	assertEquals(2, list.getItems().size());
	assertTrue(list.getItems().contains(new ItemBean(FRUIT, ORANGES)));
	list = prepareRequest(REST_LISTS + "/" + list.getId() + "/add").put(
		Entity.json(apples), ClientListBean.class);
	assertEquals(3, list.getItems().size());
	assertTrue(list.getItems().contains(new ItemBean(FRUIT, APPLES)));
    }

    private void assertEmptyList(ClientListBean list) {
	assertNotNull(list.getId());
	assertNotNull(list.getDate());
	assertTrue(list.getCheckedItems().isEmpty());
	assertTrue(list.getItems().isEmpty());
	assertNull(list.getUser());
    }

}
