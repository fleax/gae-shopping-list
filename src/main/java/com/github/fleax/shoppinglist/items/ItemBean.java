package com.github.fleax.shoppinglist.items;

import org.joda.time.DateTime;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * A shopping list item
 * 
 * @author fleax
 * 
 */
@Entity
@Cache
public class ItemBean {

    @Id
    private Long id;

    @Index
    private String category;

    private String name;

    private DateTime date;

    /**
     * @return the id
     */
    public Long getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @return the category
     */
    public String getCategory() {
	return category;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(String category) {
	this.category = category;
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

    /**
     * @return the date
     */
    public DateTime getDate() {
	return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(DateTime date) {
	this.date = date;
    }
}
