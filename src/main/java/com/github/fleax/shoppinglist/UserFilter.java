package com.github.fleax.shoppinglist;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.users.UserServiceFactory;

public class UserFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (NamespaceManager.get() == null) {
			String email = UserServiceFactory.getUserService().getCurrentUser()
					.getEmail();
			// @ not supported as valid namespace char
			NamespaceManager.set(email.replace('@', '_'));
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
