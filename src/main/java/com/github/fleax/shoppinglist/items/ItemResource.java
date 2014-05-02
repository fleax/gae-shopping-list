package com.github.fleax.shoppinglist.items;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import com.googlecode.objectify.ObjectifyService;

@Path("/items")
public class ItemResource {

    @GET
    public Response list() {
	return Response.ok(
		ObjectifyService.ofy().load().type(ItemBean.class).list())
		.build();
    }

    @GET
    @Path("/category/{name}")
    public Response searchByCategory(@PathParam("name") String name) {
	return Response.ok(
		ObjectifyService.ofy().load().type(ItemBean.class)
			.filter("category", name).list()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(final ItemBean item) {
	item.setDate(new DateTime());
	com.googlecode.objectify.Key<ItemBean> key = ObjectifyService.ofy()
		.save().entity(item).now();
	item.setId(key.getId());
	return Response.ok(item).build();
    }
}
