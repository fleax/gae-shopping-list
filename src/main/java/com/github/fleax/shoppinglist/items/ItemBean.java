package com.github.fleax.shoppinglist.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.googlecode.objectify.annotation.Id;

/**
 * A shopping list item
 * 
 * @author fleax
 * 
 */
@JsonInclude(Include.NON_NULL)
public class ItemBean {

	@Id
	private String id;

	private String name;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
