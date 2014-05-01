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
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;

@Path("/items")
public class ItemResource {

    @GET
    @Path("/reset")
    public Response reset(@Context SecurityContext security) {
	Principal principal = security.getUserPrincipal();
	if (principal != null) {
	    List<String> items = new ArrayList<>();
	    boolean moreResults = true;
	    Index index = getIndexForUser(principal.getName());
	    do {
		List<String> ids = new ArrayList<>();
		GetRequest request = GetRequest.newBuilder()
			.setReturningIdsOnly(true).build();
		GetResponse<Document> response = index.getRange(request);
		if (response.getResults().isEmpty()) {
		    moreResults = false;
		}
		for (Document doc : response) {
		    ids.add(doc.getId());
		}
		index.delete(ids);
	    } while (moreResults);
	    return Response.ok(items).build();
	}
	return Response.status(Status.UNAUTHORIZED).build();
    }

    @GET
    public Response list(@Context SecurityContext security) {
	Principal principal = security.getUserPrincipal();
	if (principal != null) {
	    List<ItemBean> items = new ArrayList<>();
	    boolean moreResults = true;
	    do {
		GetRequest request = GetRequest.newBuilder().setLimit(1000)
			.build();
		GetResponse<Document> response = getIndexForUser(
			principal.getName()).getRange(request);
		if (response.getResults().isEmpty()) {
		    moreResults = false;
		}
		for (Document doc : response) {
		    items.add(documentToBean(doc));
		}
	    } while (moreResults);
	    return Response.ok(items).build();
	}
	return Response.status(Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/+name/{name}")
    public Response search(@Context SecurityContext security,
	    @PathParam("name") String name) {
	Principal principal = security.getUserPrincipal();
	if (principal != null) {
	    Index index = getIndexForUser(principal.getName());

	    List<ItemBean> items = new ArrayList<>();
	    for (ScoredDocument document : index.search(name)) {
		items.add(documentToBean(document));
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
	try {
	    getIndexForUser(user).put(beanToDocument(item));
	} catch (PutException e) {
	    if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult()
		    .getCode())) {
		Logger.exception(e);
	    }
	}
    }

    /**
     * @param user
     * @return index for user
     */
    private Index getIndexForUser(String user) {
	return SearchServiceFactory.getSearchService().getIndex(
		IndexSpec.newBuilder().setName(user).build());
    }

    private List<ItemBean> getItems(List<Document> results) {
	List<ItemBean> result = new ArrayList<>();
	for (Document document : results) {
	    result.add(documentToBean(document));
	}
	return result;
    }

    private ItemBean documentToBean(Document document) {
	ItemBean itemBean = new ItemBean();
	itemBean.setId(document.getId());
	itemBean.setName(document.getOnlyField("original").getText());
	return itemBean;
    }

    private Document beanToDocument(ItemBean item) {
	Document.Builder builder = Document.newBuilder();
	builder.addField(Field.newBuilder().setName("original")
		.setText(item.getName()).build());

	for (int i = 2; i < item.getName().length(); i++) {
	    builder.addField(Field.newBuilder().setName("tokens")
		    .setText(item.getName().subSequence(0, i).toString())
		    .build());
	}
	return builder.build();
    }

}
