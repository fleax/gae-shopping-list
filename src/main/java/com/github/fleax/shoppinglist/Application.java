package com.github.fleax.shoppinglist;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import com.github.fleax.shoppinglist.items.ItemResource;
import com.github.fleax.shoppinglist.lists.ListBean;
import com.github.fleax.shoppinglist.lists.ListsResource;
import com.googlecode.objectify.ObjectifyService;

public class Application extends ResourceConfig {

	public Application() {
		ObjectifyService.register(ListBean.class);
		register(ItemResource.class);
		register(ListsResource.class);
		register(JacksonJsonProvider.class);
	}
}
