package com.github.fleax.shoppinglist.rest;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {

	public Application() {
		register(ItemResource.class);
		register(JacksonJsonProvider.class);
	}
}
