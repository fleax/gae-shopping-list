package com.github.fleax.shoppinglist;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.security.Principal;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.github.fleax.shoppinglist.model.IBean;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

/**
 * Resource for CRUD operations using Objectify
 * 
 * @author fleax
 * 
 * @param <B>
 */
public class CRUDResource<B extends IBean> {

	private final Class<B> dataClass;

	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	public CRUDResource() {
		this.dataClass = (Class<B>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@GET
	public Response list(@Context SecurityContext security) {
		Principal principal = security.getUserPrincipal();
		if (principal != null) {
			return Response.ok(
					ObjectifyService.ofy().load().type(dataClass)
							.filter("user", principal.getName()).list())
					.build();

		}
		return Response.status(Status.UNAUTHORIZED).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@Context SecurityContext security, final B item) {
		Principal principal = security.getUserPrincipal();
		if (principal != null) {
			Key<B> key = ObjectifyService.ofy().save().entity(item).now();
			item.setId(key.getId());
			item.setUser(principal.getName());
			return Response.created(URI.create(item.getId().toString()))
					.entity(item).build();
		}
		return Response.status(Status.UNAUTHORIZED).build();
	}

}
