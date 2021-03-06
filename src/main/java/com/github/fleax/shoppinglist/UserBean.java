package com.github.fleax.shoppinglist;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Bean containing user data, used to enforce strong consistency as it is parent
 * of entity group
 * 
 * @author fleax
 * 
 */
@Entity
@Cache
public class UserBean {

    @Id
    private String id;

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
}
