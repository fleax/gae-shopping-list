package com.github.fleax.shoppinglist.items;

import java.net.URI;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
	return Response.ok(sort(ObjectifyHelper.list(ItemBean.class))).build();
    }

    @GET
    @Path("/category/{name}")
    public Response searchByCategory(@PathParam("name") String name) {
	return Response.ok(
		sort(ObjectifyHelper.list(ItemBean.class, "category", name)))
		.build();
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

    @PUT
    @Path("/{id}/disable")
    public Response disable(@PathParam("id") Long id) {
	return disableItem(id, true);
    }

    @PUT
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
	ItemBean item = ObjectifyHelper.get(ItemBean.class, id);
	if (item == null) {
	    return Response.status(Status.NOT_FOUND).build();
	} else {
	    item.setDisabled(true);
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
