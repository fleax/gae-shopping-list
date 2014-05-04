package com.github.fleax.shoppinglist.lists;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;

import com.github.fleax.shoppinglist.ObjectifyHelper;
import com.github.fleax.shoppinglist.UserBean;
import com.github.fleax.shoppinglist.items.ItemBean;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Cache
public class ListBean {

    @Parent
    @JsonIgnore
    private Ref<UserBean> user;

    @Id
    private Long id;

    @Load
    private List<Ref<ItemBean>> items;

    @Load
    private List<Ref<ItemBean>> checkedItems;

    private DateTime date;

    /**
     * Adds an item
     * 
     * @param item
     */
    public void addItem(Ref<ItemBean> item) {
	if (this.items == null) {
	    this.items = new ArrayList<>();
	}
	this.items.add(item);
    }

    /**
     * Removes an item
     * 
     * @param item
     */
    public void deleteItem(Long item) {
	Ref<ItemBean> ref = Ref.create(Key.create(ItemBean.class, item));
	if (this.items != null) {
	    this.items.remove(ref);
	}
	if (this.checkedItems != null) {
	    this.checkedItems.remove(ref);
	}
    }

    /**
     * Checks an item
     * 
     * @param item
     */
    public void checkItem(ItemBean item) {
	if (this.checkedItems == null) {
	    this.checkedItems = new ArrayList<>();
	}
	this.checkedItems.add(Ref.create(item));
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

    /**
     * @return the items
     */
    public List<ItemBean> getItems() {
	return ObjectifyHelper.resolveReferences(this.items);
    }

    /**
     * @return the checkedItems
     */
    public List<ItemBean> getCheckedItems() {
	return ObjectifyHelper.resolveReferences(this.checkedItems);
    }

    /**
     * @param items
     *            the items to set
     */
    public void setItems(List<Ref<ItemBean>> items) {
	this.items = items;
    }

    /**
     * @param checkedItems
     *            the checkedItems to set
     */
    public void setCheckedItems(List<Ref<ItemBean>> checkedItems) {
	this.checkedItems = checkedItems;
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
