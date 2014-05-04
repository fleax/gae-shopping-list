package com.github.shoppinglist;

import java.util.List;

import org.joda.time.DateTime;

import com.github.fleax.shoppinglist.items.ItemBean;

/**
 * ListBean used by client
 * 
 * @author fleax
 * 
 */
public class ClientListBean {

    private ClientUserBean user;

    private Long id;

    private List<ItemBean> items;

    private List<ItemBean> checkedItems;

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
	return this.items;
    }

    /**
     * @return the checkedItems
     */
    public List<ItemBean> getCheckedItems() {
	return this.checkedItems;
    }

    /**
     * @param items
     *            the items to set
     */
    public void setItems(List<ItemBean> items) {
	this.items = items;
    }

    /**
     * @param checkedItems
     *            the checkedItems to set
     */
    public void setCheckedItems(List<ItemBean> checkedItems) {
	this.checkedItems = checkedItems;
    }

    /**
     * @return the user
     */
    public ClientUserBean getUser() {
	return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(ClientUserBean user) {
	this.user = user;
    }
}
