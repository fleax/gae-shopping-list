package com.github.fleax.shoppinglist.rest;

import javax.ws.rs.Path;

import com.github.fleax.shoppinglist.CRUDResource;
import com.github.fleax.shoppinglist.model.ItemBean;

@Path("/items")
public class ItemResource extends CRUDResource<ItemBean> {

}
