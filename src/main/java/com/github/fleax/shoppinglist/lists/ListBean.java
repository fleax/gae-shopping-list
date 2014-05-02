package com.github.fleax.shoppinglist.lists;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import com.github.fleax.shoppinglist.items.ItemBean;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class ListBean {

	@Id
	private Long id;

	private Map<String, String> items;

	private Set<String> checkedItems;

	private DateTime date;

	public void addItem(ItemBean item) {
		if (items == null) {
			items = new HashMap<>();
		}
		items.put(item.getId(), item.getName());
	}

	public void deleteItem(String id) {
		if (items != null) {
			items.remove(id);
		}
	}

	public void checkItems(List<ItemBean> itemsToCheck) {
		if (items != null) {
			checkedItems = new HashSet<>();
			for (ItemBean item : itemsToCheck) {
				checkedItems.add(item.getId());
			}
		}
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
	 * @return the items
	 */
	public Map<String, String> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(Map<String, String> items) {
		this.items = items;
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
