package com.github.fleax.shoppinglist.rest;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import com.github.fleax.shoppinglist.model.ItemBean;
import com.googlecode.objectify.ObjectifyService;

public class Application extends ResourceConfig {

	public Application() {
		ObjectifyService.register(ItemBean.class);

		register(ItemResource.class);

		register(JacksonJsonProvider.class);
	}
}
