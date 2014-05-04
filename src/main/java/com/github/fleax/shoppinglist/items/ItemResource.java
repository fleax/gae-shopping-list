package com.github.fleax.shoppinglist.items;

import java.net.URI;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;

import com.github.fleax.shoppinglist.ObjectifyHelper;
import com.github.fleax.shoppinglist.UserEntityGroup;
import com.googlecode.objectify.Key;

@Path("/items")
public class ItemResource {

    private UserEntityGroup user = new UserEntityGroup();

    @GET
    public Response list() {
	return Response.ok(
		sort(ObjectifyHelper.list(user.getUserBean(), ItemBean.class)))
		.build();
    }

    @GET
    @Path("/disabled")
    public Response listDisabled() {
	return Response.ok(
		sort(ObjectifyHelper.list(user.getUserBean(), ItemBean.class,
			"disabled", true))).build();
    }

    @GET
    @Path("/enabled")
    public Response listEnabled() {
	return Response.ok(
		sort(ObjectifyHelper.list(user.getUserBean(), ItemBean.class,
			"disabled", false))).build();
    }

    @GET
    @Path("/category/{name}")
    public Response searchByCategory(@PathParam("name") String name) {
	return Response.ok(
		sort(ObjectifyHelper.list(user.getUserBean(), ItemBean.class,
			"category", name))).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
	ItemBean item = ObjectifyHelper.get(user.getUserBean(), ItemBean.class,
		id);
	return item != null ? Response.ok(item).build() : Response.status(
		Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(final ItemBean item) {
	item.setUser(user.getUserBean());
	item.setDate(new DateTime());
	Key<ItemBean> key = ObjectifyHelper.save(item);
	item.setId(key.getId());
	return Response
		.created(URI.create("/items/" + item.getId().toString()))
		.entity(item).build();
    }

    @DELETE
    @Path("/{id}/disable")
    public Response disable(@PathParam("id") Long id) {
	return disableItem(id, true);
    }

    @DELETE
    @Path("/{id}/enable")
    public Response enable(@PathParam("id") Long id) {
	return disableItem(id, false);
    }

    /**
     * @param id
     * @param disabled
     * @return response after disabling or enabling an item
     */
    private Response disableItem(Long id, boolean disabled) {
	ItemBean item = ObjectifyHelper.get(user.getUserBean(), ItemBean.class,
		id);
	if (item == null) {
	    return Response.status(Status.NOT_FOUND).build();
	} else {
	    item.setDisabled(disabled);
	    item.setDate(new DateTime());
	    ObjectifyHelper.save(item);
	    return Response.ok(item).build();
	}
    }

    /**
     * @param c
     * @return list sorted by default locale collator
     */
    private List<ItemBean> sort(List<ItemBean> c) {
	final Collator collator = Collator.getInstance(Locale.getDefault());
	Collections.sort(c, new Comparator<ItemBean>() {
	    @Override
	    public int compare(ItemBean o1, ItemBean o2) {
		int compare = collator.compare(o1.getCategory(),
			o2.getCategory());
		if (compare != 0) {
		    return compare;
		} else {
		    return collator.compare(o1.getName(), o2.getName());
		}
	    }
	});
	return c;
    }

}
