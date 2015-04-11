// The Notify class is a simple SMTP mailer for
// urgent notification to a hard coded recipient

import java.net.*;
import java.io.*;
import java.util.*;

public class Notify {

	private String message;
	private BufferedReader in;
	private BufferedWriter out;

	public static final int SMTP_PORT = 25;

	// Constructor
	public Notify( String message_in ) {
		message = (new Date()) + ": " + message_in + "\n";
	}

	// Delivery method
	public void send() throws IOException {

		// I/O setup
		Socket s = new Socket("hanoi", SMTP_PORT);
		in = new BufferedReader(
					new InputStreamReader(s.getInputStream(), "8859_1"));
		out = new BufferedWriter(
					new OutputStreamWriter(s.getOutputStream(), "8859_1"));
		String response;

		// Converse with SMTP
		response = hear();
		say("HELO hanoi\n");
		response = hear();
		say("MAIL FROM: hanoi\n");
		response = hear();

		// Hard coded to author/admin
		say("RCPT TO: mark.hotchkiss@sun.com\n");
		response = hear();

		// Single line delivery
		say("DATA\n");
		response = hear();
		say( "Problem with Safeword Java Interface\n \n" +
						message + "\n.\n");
		response = hear();
		say("QUIT\n");

		s.close();
	}

	// Send a command
	private void say(String toSay) throws IOException {
		out.write(toSay);
		out.flush();
	}

	// Return a command
	private String hear() throws IOException {
		String inString = in.readLine();
		if( "23".indexOf(inString.charAt(0)) < 0) {
			throw new IOException("SMTP problem: " + inString);
		}
		return inString;
	}
}
