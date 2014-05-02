package com.github.fleax.shoppinglist.lists;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

public class ListsResourceTest extends JerseyTest {

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		return new ResourceConfig(ListsResource.class);
	}

	@Test
	public void test() {
		Response response = target("/lists").request().get();
	}
}
