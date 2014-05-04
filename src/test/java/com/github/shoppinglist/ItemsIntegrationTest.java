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
    public void test01_list() {
	List<ItemBean> response = list(REST_ITEMS,
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertTrue(response.isEmpty());
    }

    @Test
    public void test02_create() {
	ItemBean oranges = create(REST_ITEMS, new ItemBean(FRUIT, ORANGES),
		ItemBean.class);
	assertItemBean(oranges, FRUIT, ORANGES, false);
    }

    @Test
    public void test03_list_and_get() {
	List<ItemBean> response = list(REST_ITEMS,
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(1, response.size());
	ItemBean bean = get(REST_ITEMS, response.get(0).getId(), ItemBean.class);
	assertItemBean(bean, FRUIT, ORANGES, false);
    }

    @Test(expected = NotFoundException.class)
    public void test04_get_not_found() {
	prepareRequest(REST_ITEMS + "/" + Long.MAX_VALUE).get(
		ClientResponse.class);
    }

    @Test
    public void test05_create() {
	ItemBean apples = create(REST_ITEMS, new ItemBean(FRUIT, APPLES),
		ItemBean.class);
	assertItemBean(apples, FRUIT, APPLES, false);
    }

    @Test
    public void test06_create() {
	ItemBean response = create(REST_ITEMS, new ItemBean(DRINKS, WATER),
		ItemBean.class);
	assertItemBean(response, DRINKS, WATER, false);
    }

    @Test
    public void test07_categories() {
	List<ItemBean> response = prepareRequest(
		"/rest/items/category/" + FRUIT).get(
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(2, response.size());
	assertItemBean(response.get(0), FRUIT, APPLES, false);
	assertItemBean(response.get(1), FRUIT, ORANGES, false);
    }

    @Test
    public void test08_categories() {
	List<ItemBean> drinks = prepareRequest("/rest/items/category/" + DRINKS)
		.get(new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(1, drinks.size());
	Assert.assertTrue(drinks.contains(new ItemBean(DRINKS, WATER)));
    }

    @Test
    public void test09_sorted_list() {
	List<ItemBean> response = list(REST_ITEMS,
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(3, response.size());
	assertItemBean(response.get(0), DRINKS, WATER, false);
	assertItemBean(response.get(1), FRUIT, APPLES, false);
	assertItemBean(response.get(2), FRUIT, ORANGES, false);
    }

    @Test
    public void test10_disable() {
	List<ItemBean> response = list(REST_ITEMS,
		new GenericType<List<ItemBean>>() {
		});
	// Disable Water
	ItemBean bean = prepareRequest(
		REST_ITEMS + "/" + response.get(0).getId() + "/disable")
		.delete(ItemBean.class);
	assertItemBean(bean, DRINKS, WATER, true);
    }

    @Test
    public void test11_disabled_list() {
	List<ItemBean> response = list(REST_ITEMS + "/disabled",
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(1, response.size());
	assertItemBean(response.get(0), DRINKS, WATER, true);
    }

    @Test
    public void test12_enabled_list() {
	List<ItemBean> response = list(REST_ITEMS + "/enabled",
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(2, response.size());
	assertItemBean(response.get(0), FRUIT, APPLES, false);
	assertItemBean(response.get(1), FRUIT, ORANGES, false);
    }

    @Test
    public void test13_enable() {
	List<ItemBean> response = list(REST_ITEMS,
		new GenericType<List<ItemBean>>() {
		});
	// Enable Water
	ItemBean bean = prepareRequest(
		REST_ITEMS + "/" + response.get(0).getId() + "/enable").delete(
		ItemBean.class);
	assertItemBean(bean, DRINKS, WATER, false);
    }

    @Test
    public void test14_disabled_list() {
	List<ItemBean> response = list(REST_ITEMS + "/disabled",
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(0, response.size());
    }

    @Test
    public void test15_enabled_list() {
	List<ItemBean> response = list(REST_ITEMS + "/enabled",
		new GenericType<List<ItemBean>>() {
		});
	Assert.assertEquals(3, response.size());
    }

    private void assertItemBean(ItemBean item, String category, String name,
	    boolean disabled) {
	Assert.assertNotNull(item.getId());
	Assert.assertEquals(category, item.getCategory());
	Assert.assertEquals(name, item.getName());
    }

}
