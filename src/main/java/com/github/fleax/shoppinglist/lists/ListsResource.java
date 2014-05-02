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

import com.github.fleax.shoppinglist.items.ItemBean;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

@Path("/lists")
public class ListsResource {

	private ListCompletedProcessor processor = new ListCompletedProcessor();

	@GET
	public Response list() {
		return Response.ok(
				ObjectifyService.ofy().load().type(ListBean.class).list())
				.build();
	}

	@POST
	public Response create() {
		ListBean list = new ListBean();
		list.setDate(new DateTime());
		Key<ListBean> key = ObjectifyService.ofy().save().entity(list).now();
		list.setId(key.getId());
		return Response.ok(list).build();
	}

	@PUT
	@Path("/{list}/add")
	public Response addItem(@PathParam("list") Long id, ItemBean item) {
		ListBean list = ObjectifyService.ofy().load().type(ListBean.class)
				.id(id).safeGet();
		if (list == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		list.addItem(item);
		list.setDate(new DateTime());
		ObjectifyService.ofy().save().entity(list);
		return Response.ok(list).build();
	}

	@DELETE
	@Path("/{list}/delete/{item}")
	public Response deleteItem(@PathParam("list") Long id,
			@PathParam("item") String item) {
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
		list.checkItems(checkedItems);
		list.setDate(new DateTime());
		ObjectifyService.ofy().save().entity(list);
		processor.processList(list);
		return Response.ok(list).build();
	}
}
