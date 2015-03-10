package com.managedmethods.box.longpoll;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BoxLongPollClient {

	private static final String API_KEY = "wUA3TjHOtQXwVvy8xPR5N8RianaJ17sq";
	private static final String EVENTS_URL = "https://api.box.com/2.0/events";
	private static final String STREAM_POS_PARAM = "stream_position";
	private static final String STREAM_POS_KEY = "next_stream_position";
	private static final String ENTRIES_KEY = "entries";
	private static final String LONGPOLL_URL_KEY = "url";
	private static final String MESSAGE_KEY = "message";
	private static final String NEW_EVENT_MESSAGE = "new_change";
	private static final String RECONNECT_MESSAGE = "reconnect";

	private static String lastStreamPos = "now";
	private static String longPollUrl = new String();

	public static void main(String[] args) {
		reconnect();
	}

	private static void setCurrentStreamPos() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(EVENTS_URL).queryParam(
				STREAM_POS_PARAM, lastStreamPos);
		Builder builder = target.request();
		builder = addAuthHeader(builder);

		Response response = builder.get();

		JSONObject jObj = parseResponseToJsonObj(response);

		if (jObj != null && jObj.containsKey(STREAM_POS_KEY)) {
			lastStreamPos = Long.toString((long) jObj.get(STREAM_POS_KEY));
			System.out.println(lastStreamPos);
		}
	}

	private static void setLongPollUrl() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(EVENTS_URL);
		Builder builder = target.request();
		builder = addAuthHeader(builder);
		Response response = builder.options();

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			System.out.println(response.getStatusInfo());
			System.exit(1);
		}

		JSONObject jObj = parseResponseToJsonObj(response);

		if (jObj != null && jObj.containsKey(ENTRIES_KEY)) {
			JSONArray subObj = (JSONArray) jObj.get(ENTRIES_KEY);
			if (subObj != null) {
				JSONObject s = (JSONObject) subObj.get(0);
				longPollUrl = (String) s.get(LONGPOLL_URL_KEY);
				System.out.println(longPollUrl);
			}
		}
	}

	private static void connectToLongPollUrl() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(longPollUrl).queryParam(
				STREAM_POS_PARAM, lastStreamPos);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		builder = addAuthHeader(builder);
		System.out.println("waiting for long poll response");
		Response response = builder.get();
		System.out.println("Response received");
		JSONObject jObj = parseResponseToJsonObj(response);

		if (jObj != null && jObj.containsKey(MESSAGE_KEY)) {
			String message = (String) jObj.get(MESSAGE_KEY);
			if (message.equals(RECONNECT_MESSAGE)) {
				System.out.println("No events");
				reconnect();
			} else if (message.equals(NEW_EVENT_MESSAGE)) {
				System.out.println("Events found");
				getLatestEvents();
				reconnect();
			}
		} else {
			reconnect();
		}
	}

	private static void reconnect() {
		System.out.println("Reconnecting");
		System.out.println("Setting current stream position");
		setCurrentStreamPos();
		System.out.println("Stream position is " + lastStreamPos);
		do {
			System.out.println("Setting long poll url");
			setLongPollUrl();
		} while (longPollUrl.isEmpty());

		System.out.println("Long Poll Url is: " + longPollUrl);
		System.out.println("Connecting to long poll url");
		connectToLongPollUrl();
	}

	private static Builder addAuthHeader(Builder builder) {
		return builder.header("Authorization", "Bearer " + API_KEY);
	}

	private static JSONObject parseResponseToJsonObj(Response response) {
		JSONObject jObj = null;

		try {
			jObj = (JSONObject) new JSONParser().parse(response
					.readEntity(String.class));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return jObj;
	}

	private static void getLatestEvents() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(EVENTS_URL).queryParam(STREAM_POS_KEY,
				lastStreamPos);
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		builder = addAuthHeader(builder);
		Response response = builder.get();
		System.out.println(response.readEntity(String.class));
	}

}
