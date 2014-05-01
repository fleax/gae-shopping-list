package com.github.fleax.shoppinglist.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.github.fleax.shoppinglist.model.ItemBean;
import com.github.fleax.shoppinglist.utils.Logger;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import com.googlecode.objectify.ObjectifyService;

@Path("/items")
public class ItemResource {

	@GET
	public Response list(@Context SecurityContext security) {
		Principal principal = security.getUserPrincipal();
		if (principal != null) {
			return Response.ok(
					ObjectifyService.ofy().load().type(ItemBean.class)
							.filter("user", principal.getName()).list())
					.build();

		}
		return Response.status(Status.UNAUTHORIZED).build();
	}

	@GET
	@Path("/+name/{name}")
	public Response search(@Context SecurityContext security,
			@PathParam("name") String name) {
		Principal principal = security.getUserPrincipal();
		if (principal != null) {

			IndexSpec indexSpec = IndexSpec.newBuilder()
					.setName(principal.getName()).build();
			Index index = SearchServiceFactory.getSearchService().getIndex(
					indexSpec);

			List<ItemBean> items = new ArrayList<>();
			for (ScoredDocument document : index.search(name)) {
				ItemBean item = new ItemBean();
				item.setId(document.getId());
				item.setName(document.getOnlyField("original").getText());
				items.add(item);
			}

			return Response.ok(items).build();
		}
		return Response.status(Status.UNAUTHORIZED).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@Context SecurityContext security,
			final ItemBean item) {
		Principal principal = security.getUserPrincipal();
		if (principal != null) {
			createDocument(principal.getName(), item);
			return Response.ok(item).build();
		}
		return Response.status(Status.UNAUTHORIZED).build();
	}

	private void createDocument(String user, ItemBean item) {
		Document.Builder builder = Document.newBuilder();
		builder.addField(Field.newBuilder().setName("original")
				.setText(item.getName()).build());

		for (int i = 2; i < item.getName().length(); i++) {
			builder.addField(Field.newBuilder().setName("tokens")
					.setText(item.getName().subSequence(0, i).toString())
					.build());
		}

		IndexSpec indexSpec = IndexSpec.newBuilder().setName(user).build();
		Index index = SearchServiceFactory.getSearchService().getIndex(
				indexSpec);

		try {
			index.put(builder.build());
		} catch (PutException e) {
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult()
					.getCode())) {
				Logger.exception(e);
			}
		}
	}
}
