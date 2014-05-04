package com.github.fleax.shoppinglist;

import com.google.appengine.api.users.UserServiceFactory;

/**
 * Manages creation of user beans
 * 
 * @author alex
 * 
 */
public class UserEntityGroup {

    /**
     * Creates user bean if not already created
     * 
     * @return user bean
     */
    public UserBean getUserBean() {
	String email = UserServiceFactory.getUserService().getCurrentUser()
		.getEmail();
	UserBean userBean = ObjectifyHelper.get(UserBean.class, email);
	if (userBean == null) {
	    userBean = new UserBean();
	    userBean.setId(email);
	    ObjectifyHelper.save(userBean);
	}
	return userBean;
    }

}
