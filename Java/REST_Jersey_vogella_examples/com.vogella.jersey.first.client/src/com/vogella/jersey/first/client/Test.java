package com.vogella.jersey.first.client;

import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

public class Test {

	public static void main(String[] args) {
		Test test = new Test();
		test.run();
	}

	private void run() {

		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(getBaseURI());

		// Response class returns log message 
		log(target.path("rest").path("hello").request()
				.accept(MediaType.TEXT_PLAIN).get(Response.class).toString());

		// Plain text response content
		log(target.path("rest").path("hello").request()
				.accept(MediaType.TEXT_PLAIN).get(String.class));

		// XML response content
		log(target.path("rest").path("hello").request()
				.accept(MediaType.TEXT_XML).get(String.class));

		// HTML response content
		log(target.path("rest").path("hello").request()
				.accept(MediaType.TEXT_HTML).get(String.class));
	}

	private static URI getBaseURI() {
		
		// Return the URL as a URI to Client WebTarget
		return UriBuilder.fromUri(
				"http://localhost:8080/com.vogella.jersey.first").build();
	}
	
	private void log(String message) {
		System.out.println(message);
	}
}