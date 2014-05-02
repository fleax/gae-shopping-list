package com.github.fleax.shoppinglist;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import com.github.fleax.shoppinglist.items.ItemBean;
import com.github.fleax.shoppinglist.items.ItemResource;
import com.github.fleax.shoppinglist.lists.ListBean;
import com.github.fleax.shoppinglist.lists.ListsResource;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.DateTimeZoneTranslatorFactory;
import com.googlecode.objectify.impl.translate.opt.joda.LocalDateTimeTranslatorFactory;
import com.googlecode.objectify.impl.translate.opt.joda.LocalDateTranslatorFactory;
import com.googlecode.objectify.impl.translate.opt.joda.ReadableInstantTranslatorFactory;

public class Application extends ResourceConfig {

    public Application() {
	// Add translators for Joda objects
	ObjectifyService.factory().getTranslators()
		.add(new ReadableInstantTranslatorFactory());
	ObjectifyService.factory().getTranslators()
		.add(new LocalDateTranslatorFactory());
	ObjectifyService.factory().getTranslators()
		.add(new LocalDateTimeTranslatorFactory());
	ObjectifyService.factory().getTranslators()
		.add(new DateTimeZoneTranslatorFactory());

	ObjectifyService.register(ListBean.class);
	ObjectifyService.register(ItemBean.class);
	register(ItemResource.class);
	register(ListsResource.class);
	register(JacksonJsonProvider.class);
    }
}
