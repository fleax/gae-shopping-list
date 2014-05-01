package com.github.fleax.shoppinglist.rest;

import java.net.URI;
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
import com.github.fleax.shoppinglist.utils.StringUtils;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import com.googlecode.objectify.Key;
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

			QueryOptions options = QueryOptions.newBuilder().setLimit(25)
					.setReturningIdsOnly(true).build();
			Query query = Query.newBuilder().setOptions(options)
					.build(StringUtils.getString("name = ", name));

			List<ItemBean> items = new ArrayList<>();
			for (ScoredDocument document : index.search(query)) {
				items.add(ObjectifyService.ofy().load().type(ItemBean.class)
						.id(Long.parseLong(document.getId())).getValue());
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
			item.setUser(principal.getName());
			Key<ItemBean> key = ObjectifyService.ofy().save().entity(item)
					.now();
			item.setId(key.getId());

			createDocument(item);

			return Response.created(URI.create(item.getId().toString()))
					.entity(item).build();
		}
		return Response.status(Status.UNAUTHORIZED).build();
	}

	private void createDocument(ItemBean item) {
		Document.Builder builder = Document.newBuilder().setId(
				item.getId().toString());

		for (int i = 1; i <= item.getName().length(); i++) {
			builder.addField(Field.newBuilder().setName("name")
					.setText(item.getName().substring(0, i)).build());
		}

		IndexSpec indexSpec = IndexSpec.newBuilder().setName(item.getUser())
				.build();
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
