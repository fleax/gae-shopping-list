package com.github.shoppinglist;

import static com.github.shoppinglist.Constants.APPLES;
import static com.github.shoppinglist.Constants.DRINKS;
import static com.github.shoppinglist.Constants.FRUIT;
import static com.github.shoppinglist.Constants.ORANGES;
import static com.github.shoppinglist.Constants.REST_ITEMS;
import static com.github.shoppinglist.Constants.WATER;

import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.GenericType;

import org.glassfish.jersey.client.ClientResponse;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemsIntegrationTest extends LocalServerIntegrationTest {

    @Test
    public void test01_list() {
	List<ClientItemBean> response = list(REST_ITEMS,
		new GenericType<List<ClientItemBean>>() {
		});
	Assert.assertTrue(response.isEmpty());
    }

    @Test
    public void test02_create() {
	ClientItemBean oranges = create(REST_ITEMS, new ClientItemBean(FRUIT,
		ORANGES), ClientItemBean.class);
	assertClientItemBean(oranges, FRUIT, ORANGES, false);
    }

    @Test
    public void test03_list_and_get() {
	List<ClientItemBean> response = list(REST_ITEMS,
		new GenericType<List<ClientItemBean>>() {
		});
	Assert.assertEquals(1, response.size());
	ClientItemBean bean = get(REST_ITEMS, response.get(0).getId(),
		ClientItemBean.class);
	assertClientItemBean(bean, FRUIT, ORANGES, false);
    }

    @Test(expected = NotFoundException.class)
    public void test04_get_not_found() {
	prepareRequest(REST_ITEMS + "/" + Long.MAX_VALUE).get(
		ClientResponse.class);
    }

    @Test
    public void test05_create() {
	ClientItemBean apples = create(REST_ITEMS, new ClientItemBean(FRUIT,
		APPLES), ClientItemBean.class);
	assertClientItemBean(apples, FRUIT, APPLES, false);
    }

    @Test
    public void test06_create() {
	ClientItemBean response = create(REST_ITEMS, new ClientItemBean(DRINKS,
		WATER), ClientItemBean.class);
	assertClientItemBean(response, DRINKS, WATER, false);
    }

    @Test
    public void test07_categories() {
	List<ClientItemBean> response = prepareRequest(
		"/rest/items/category/" + FRUIT).get(
		new GenericType<List<ClientItemBean>>() {
		});
	Assert.assertEquals(2, response.size());
	assertClientItemBean(response.get(0), FRUIT, APPLES, false);
	assertClientItemBean(response.get(1), FRUIT, ORANGES, false);
    }

    @Test
    public void test08_categories() {
	List<ClientItemBean> drinks = prepareRequest(
		"/rest/items/category/" + DRINKS).get(
		new GenericType<List<ClientItemBean>>() {
		});
	Assert.assertEquals(1, drinks.size());
	Assert.assertTrue(drinks.contains(new ClientItemBean(DRINKS, WATER)));
    }

    @Test
    public void test09_sorted_list() {
	List<ClientItemBean> response = list(REST_ITEMS,
		new GenericType<List<ClientItemBean>>() {
		});
	Assert.assertEquals(3, response.size());
	assertClientItemBean(response.get(0), DRINKS, WATER, false);
	assertClientItemBean(response.get(1), FRUIT, APPLES, false);
	assertClientItemBean(response.get(2), FRUIT, ORANGES, false);
    }

    @Test
    public void test10_disable() {
	List<ClientItemBean> response = list(REST_ITEMS,
		new GenericType<List<ClientItemBean>>() {
		});
	// Disable Water
	ClientItemBean bean = prepareRequest(
		REST_ITEMS + "/" + response.get(0).getId() + "/disable")
		.delete(ClientItemBean.class);
	assertClientItemBean(bean, DRINKS, WATER, true);
    }

    @Test
    public void test11_disabled_list() {
	List<ClientItemBean> response = list(REST_ITEMS + "/disabled",
		new GenericType<List<ClientItemBean>>() {
		});
	Assert.assertEquals(1, response.size());
	assertClientItemBean(response.get(0), DRINKS, WATER, true);
    }

    @Test
    public void test12_enabled_list() {
	List<ClientItemBean> response = list(REST_ITEMS + "/enabled",
		new GenericType<List<ClientItemBean>>() {
		});
	Assert.assertEquals(2, response.size());
	assertClientItemBean(response.get(0), FRUIT, APPLES, false);
	assertClientItemBean(response.get(1), FRUIT, ORANGES, false);
    }

    @Test
    public void test13_enable() {
	List<ClientItemBean> response = list(REST_ITEMS,
		new GenericType<List<ClientItemBean>>() {
		});
	// Enable Water
	ClientItemBean bean = prepareRequest(
		REST_ITEMS + "/" + response.get(0).getId() + "/enable").delete(
		ClientItemBean.class);
	assertClientItemBean(bean, DRINKS, WATER, false);
    }

    @Test
    public void test14_disabled_list() {
	List<ClientItemBean> response = list(REST_ITEMS + "/disabled",
		new GenericType<List<ClientItemBean>>() {
		});
	Assert.assertEquals(0, response.size());
    }

    @Test
    public void test15_enabled_list() {
	List<ClientItemBean> response = list(REST_ITEMS + "/enabled",
		new GenericType<List<ClientItemBean>>() {
		});
	Assert.assertEquals(3, response.size());
    }

    private void assertClientItemBean(ClientItemBean item, String category,
	    String name, boolean disabled) {
	Assert.assertNotNull(item.getId());
	Assert.assertEquals(category, item.getCategory());
	Assert.assertEquals(name, item.getName());
	Assert.assertNull(item.getUser());
    }

}
