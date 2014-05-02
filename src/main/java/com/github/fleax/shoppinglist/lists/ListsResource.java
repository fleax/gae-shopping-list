package com.github.fleax.shoppinglist.lists;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.joda.time.DateTime;

import com.github.fleax.shoppinglist.items.ItemBean;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

@Path("/lists")
public class ListsResource {

	private ListCompletedProcessor processor = new ListCompletedProcessor();

	@GET
	public Response list(@Context SecurityContext security) {
		return Response.ok(
				ObjectifyService.ofy().load().type(ListBean.class)
						.filter("user", security.getUserPrincipal().getName())
						.list()).build();
	}

	@POST
	public Response create(@Context SecurityContext security) {
		ListBean list = new ListBean();
		list.setUser(security.getUserPrincipal().getName());
		list.setDate(new DateTime());
		Key<ListBean> key = ObjectifyService.ofy().save().entity(list).now();
		list.setId(key.getId());
		return Response.ok(list).build();
	}

	@PUT
	@Path("/{list}/add")
	public Response addItem(@Context SecurityContext security,
			@PathParam("list") Long id, ItemBean item) {
		ListBean list = ObjectifyService.ofy().load().type(ListBean.class)
				.id(id).safeGet();
		if (list == null
				|| !list.getUser()
						.equals(security.getUserPrincipal().getName())) {
			return Response.status(Status.NOT_FOUND).build();
		}
		list.addItem(item);
		list.setDate(new DateTime());
		ObjectifyService.ofy().save().entity(list);
		return Response.ok(list).build();
	}

	@DELETE
	@Path("/{list}/delete/{item}")
	public Response deleteItem(@Context SecurityContext security,
			@PathParam("list") Long id, @PathParam("item") String item) {
		ListBean list = ObjectifyService.ofy().load().type(ListBean.class)
				.id(id).safeGet();
		if (list == null
				|| !list.getUser()
						.equals(security.getUserPrincipal().getName())) {
			return Response.status(Status.NOT_FOUND).build();
		}
		list.deleteItem(item);
		list.setDate(new DateTime());
		ObjectifyService.ofy().save().entity(list);
		return Response.ok(list).build();
	}

	@POST
	@Path("/{list}/completed")
	public Response completeList(@Context SecurityContext security,
			@PathParam("list") Long id, List<ItemBean> checkedItems) {
		ListBean list = ObjectifyService.ofy().load().type(ListBean.class)
				.id(id).safeGet();
		if (list == null
				|| !list.getUser()
						.equals(security.getUserPrincipal().getName())) {
			return Response.status(Status.NOT_FOUND).build();
		}
		list.checkItems(checkedItems);
		list.setDate(new DateTime());
		ObjectifyService.ofy().save().entity(list);
		processor.processList(list);
		return Response.ok(list).build();
	}
}
