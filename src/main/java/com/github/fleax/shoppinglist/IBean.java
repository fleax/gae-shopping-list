package com.github.fleax.shoppinglist;

/**
 * Bean interface with common attributes: id and user
 * 
 * @author fleax
 * 
 */
public interface IBean {

	/**
	 * @param id
	 */
	void setId(Long id);

	/**
	 * @return id
	 */
	Long getId();

	/**
	 * @param user
	 */
	void setUser(String user);

	/**
	 * @return user
	 */
	String getUser();
}
