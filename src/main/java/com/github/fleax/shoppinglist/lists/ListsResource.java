package com.github.fleax.shoppinglist.lists;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;

import com.github.fleax.shoppinglist.ObjectifyHelper;
import com.github.fleax.shoppinglist.UserEntityGroup;
import com.github.fleax.shoppinglist.items.ItemBean;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;

@Path("/lists")
public class ListsResource {

    private final UserEntityGroup user = new UserEntityGroup();

    private final ListCompletedProcessor processor = new ListCompletedProcessor();

    @GET
    public Response list() {
	return Response.ok(
		ObjectifyHelper.list(user.getUserBean(), ListBean.class))
		.build();
    }

    @POST
    public Response create() {
	ListBean list = new ListBean();
	list.setUser(user.getUserBean());
	list.setDate(new DateTime());
	Key<ListBean> key = ObjectifyHelper.save(list);
	list.setId(key.getId());
	return Response.ok(list).build();
    }

    @GET
    @Path("/{list}")
    public Response get(@PathParam("list") Long id) {
	ListBean list = ObjectifyHelper.get(user.getUserBean(), ListBean.class,
		id);
	return list != null ? Response.ok(list).build() : Response.status(
		Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{list}/add")
    public Response addItem(@PathParam("list") Long id, ItemBean item) {
	ListBean list = ObjectifyHelper.get(user.getUserBean(), ListBean.class,
		id);
	// Force fecth from datastore to get proper Ref and return items list
	// updated
	ItemBean itemDS = ObjectifyHelper.get(user.getUserBean(),
		ItemBean.class, item.getId());
	if (list == null || itemDS == null) {
	    return Response.status(Status.NOT_FOUND).build();
	}
	list.addItem(Ref.create(itemDS));
	list.setDate(new DateTime());
	ObjectifyHelper.save(list);
	return Response.ok(list).build();
    }

    @DELETE
    @Path("/{list}/delete/{item}")
    public Response deleteItem(@PathParam("list") Long id,
	    @PathParam("item") Long item) {
	ListBean list = ObjectifyService.ofy().load().type(ListBean.class)
		.id(id).safeGet();
	if (list == null) {
	    return Response.status(Status.NOT_FOUND).build();
	}
	list.deleteItem(item);
	list.setDate(new DateTime());
	ObjectifyService.ofy().save().entity(list);
	return Response.ok(list).build();
    }

    @POST
    @Path("/{list}/completed")
    public Response completeList(@PathParam("list") Long id,
	    List<ItemBean> checkedItems) {
	ListBean list = ObjectifyService.ofy().load().type(ListBean.class)
		.id(id).safeGet();
	if (list == null) {
	    return Response.status(Status.NOT_FOUND).build();
	}
	for (ItemBean item : checkedItems) {
	    list.checkItem(item);
	}
	list.setDate(new DateTime());
	ObjectifyService.ofy().save().entity(list);
	processor.processList(list);
	return Response.ok(list).build();
    }
}
