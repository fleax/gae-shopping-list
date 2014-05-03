package com.github.fleax.shoppinglist.items;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;

import com.github.fleax.shoppinglist.ObjectifyHelper;
import com.googlecode.objectify.Key;

@Path("/items")
public class ItemResource {

    @GET
    public Response list() {
	return Response.ok(ObjectifyHelper.list(ItemBean.class)).build();
    }

    @GET
    @Path("/category/{name}")
    public Response searchByCategory(@PathParam("name") String name) {
	return Response.ok(
		ObjectifyHelper.list(ItemBean.class, "category", name)).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
	ItemBean item = ObjectifyHelper.get(ItemBean.class, id);
	return item != null ? Response.ok(item).build() : Response.status(
		Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(final ItemBean item) {
	item.setDate(new DateTime());
	Key<ItemBean> key = ObjectifyHelper.save(item);
	item.setId(key.getId());
	return Response
		.created(URI.create("/items/" + item.getId().toString()))
		.entity(item).build();
    }
}
