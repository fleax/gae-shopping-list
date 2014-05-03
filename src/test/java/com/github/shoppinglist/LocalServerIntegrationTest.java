package com.github.shoppinglist;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.client.ClientProperties;
import org.junit.Assert;
import org.junit.Before;

public class LocalServerIntegrationTest {

    private Client client;

    private Cookie cookie;

    @Before
    public void login() {
	client = ClientBuilder.newClient();
	client.register(JacksonJsonProvider.class);
	client.property(ClientProperties.FOLLOW_REDIRECTS, false);

	// Send form to login
	MultivaluedMap<String, String> form = new MultivaluedHashMap<>();
	form.add("email", "test@example.com");
	form.add("continue", "http://localhost:8888");
	form.add("action", "Log In");

	Response response = client.target("http://localhost:8888")
		.path("/_ah/login")
		.request(MediaType.APPLICATION_FORM_URLENCODED)
		.post(Entity.form(form));

	// Assert is a redirection
	Assert.assertEquals(302, response.getStatus());

	cookie = response.getCookies().get("dev_appserver_login");
    }

    protected Builder prepareRequest(String path) {
	return client.target("http://localhost:8888").path(path).request()
		.cookie(cookie);
    }
}
