package com.github.shoppinglist;

import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.GenericType;

import org.glassfish.jersey.client.ClientResponse;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.fleax.shoppinglist.items.ItemBean;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemsIntegrationTest extends LocalServerIntegrationTest {

    private static final String WATER = "Water";
    private static final String DRINKS = "Drinks";
    private static final String APPLES = "Apples";
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
	ItemBean oranges = create(REST_ITEMS, new ItemBean(FRUIT, ORANGES),
		ItemBean.class);
	Assert.assertNotNull(oranges.getId());
	Assert.assertEquals(FRUIT, oranges.getCategory());
	Assert.assertEquals(ORANGES, oranges.getName());
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

    @Test(expected = NotFoundException.class)
    public void test4_get_not_found() {
	prepareRequest(REST_ITEMS + "/" + Long.MAX_VALUE).get(
		ClientResponse.class);
    }

    @Test
    public void test5_create() {
	ItemBean apples = create(REST_ITEMS, new ItemBean(FRUIT, APPLES),
		ItemBean.class);
	Assert.assertNotNull(apples.getId());
	Assert.assertEquals(FRUIT, apples.getCategory());
	Assert.assertEquals(APPLES, apples.getName());
    }

    @Test
    public void test6_create() {
	ItemBean apples = create(REST_ITEMS, new ItemBean(DRINKS, WATER),
		ItemBean.class);
	Assert.assertNotNull(apples.getId());
	Assert.assertEquals(DRINKS, apples.getCategory());
	Assert.assertEquals(WATER, apples.getName());
    }

    @Test
    public void test7_categories() {
	List<ItemBean> fruits = prepareRequest("/rest/items/category/" + FRUIT)
		.get(new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(2, fruits.size());
	Assert.assertEquals(APPLES, fruits.get(0).getName());
	Assert.assertEquals(ORANGES, fruits.get(1).getName());
    }

    @Test
    public void test8_categories() {
	List<ItemBean> drinks = prepareRequest("/rest/items/category/" + DRINKS)
		.get(new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(1, drinks.size());
	Assert.assertTrue(drinks.contains(new ItemBean(DRINKS, WATER)));
    }

    @Test
    public void test9_sorted_list() {
	List<ItemBean> response = list(REST_ITEMS,
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(3, response.size());
	Assert.assertEquals(DRINKS, response.get(0).getCategory());
	Assert.assertEquals(WATER, response.get(0).getName());
	Assert.assertEquals(FRUIT, response.get(1).getCategory());
	Assert.assertEquals(APPLES, response.get(1).getName());
	Assert.assertEquals(FRUIT, response.get(2).getCategory());
	Assert.assertEquals(ORANGES, response.get(2).getName());
    }

}
