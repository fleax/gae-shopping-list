package com.github.fleax.shoppinglist.items;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;

import com.github.fleax.shoppinglist.UserBean;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

/**
 * A shopping list item
 * 
 * @author fleax
 * 
 */
@Entity
@Cache
public class ItemBean {

    @Parent
    @JsonIgnore
    private Ref<UserBean> user;

    @Id
    private Long id;

    @Index
    private String category;

    private String name;

    @Index
    private boolean disabled;

    private DateTime date;

    /**
     * Default constructor
     */
    public ItemBean() {
    }

    /**
     * @param category
     * @param name
     */
    public ItemBean(String category, String name) {
	this.category = category;
	this.name = name;
    }

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((category == null) ? 0 : category.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof ItemBean)) {
	    return false;
	}
	ItemBean other = (ItemBean) obj;
	if (category == null) {
	    if (other.category != null) {
		return false;
	    }
	} else if (!category.equals(other.category)) {
	    return false;
	}
	if (name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!name.equals(other.name)) {
	    return false;
	}
	return true;
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
	return disabled;
    }

    /**
     * @param disabled
     *            the disabled to set
     */
    public void setDisabled(boolean disabled) {
	this.disabled = disabled;
    }

    /**
     * @return the user
     */
    public UserBean getUser() {
	return user != null ? user.getValue() : null;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(UserBean user) {
	this.user = Ref.create(user);
    }

}
