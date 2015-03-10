package de.vogella.jersey.jaxb.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.vogella.jersey.todo.model.Todo;

public class Test {

	private static final String baseUrl = "http://localhost:8080/de.vogella.jersey.todo";

	public static void main(String[] args) {

		Client client = ClientBuilder.newClient();
		Todo todo = new Todo("3", "something else");
		
		// Add something of my own
		Todo todo2 = new Todo("4", "Groucho");

		WebTarget putTodo = client.target(baseUrl).path("rest").path("todos")
				.path(todo.getId());
		Response response = putTodo.request().put(
				Entity.entity(todo, MediaType.APPLICATION_XML));

		// Add groucho
		putTodo = client.target(baseUrl).path("rest").path("todos")
				.path(todo2.getId());
		response = putTodo.request().put(
				Entity.entity(todo2, MediaType.APPLICATION_XML));

		WebTarget getTodos = client.target(baseUrl).path("rest").path("todos");
		String todos = getTodos.request().accept(MediaType.TEXT_XML)
				.get(String.class);
		System.out.println(todos);

	}

}
