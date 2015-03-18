import java.net.*;
import java.io.*;
import java.util.*;

public class NotifyPINUser {

	private String message;
	private String EMail;
	private BufferedReader in;
	private BufferedWriter out;

	public final static int SMTP_PORT = 25;

	public NotifyPINUser(String in_email) {

		EMail = in_email;

		message =
				"Your Remote Access Token Card PIN was accessed on: \n" +
				(new Date()) + "\n\n" +
				"WARNING: If you were not the person accessing this PIN, \n" +
				"please contact your local Resolution Center immediately: \n\n" +
				"\tAmericas: x27000 or 303-272-7000\n" +
				"\tEurope: x15555 or +31-33-454-2555\n" +
				"\tJapan: x55119 or +81-3-5717-5119\n" +
				"\tKorea: x56056 or +82-2-3469-0056\n" +
				"\tChina/Taiwan: x88888 or +65-63959888\n" +
				"\tAsia South: x88888 or +65-63959888\n" +
				"\tAustralia/NZ: x88888 or +65-63959888\n";

	}

	public void send() throws IOException {

		// I/O setup

		Socket s = new Socket("hanoi", SMTP_PORT); //CHECK!
		//Socket s = new Socket("buster", SMTP_PORT); //CHECK!

		in = new BufferedReader(
				new InputStreamReader(s.getInputStream(), "8859_1"));
		out = new BufferedWriter(
				new OutputStreamWriter(s.getOutputStream(), "8859_1"));
		String response;

		// Converse with SMTP
		response = hear();

		say("HELO hanoi\n"); //CHECK!
		//say("HELO buster\n"); //CHECK!

		response = hear();

		say("MAIL FROM: hanoi\n"); //CHECK!
		//say("MAIL FROM: buster\n"); //CHECK

		response = hear();

		// Hard coded to author/admin
		say("RCPT TO: " + EMail + "\n");
		response = hear();

		// Single line delivery
		say("DATA\n");
		response = hear();
		say("Subject: Token Card PIN Accessed\n" +
				message + "\n.\n");
		response = hear();
		say("QUIT\n");

		s.close();
	}

	private void say(String toSay) throws IOException {
		out.write(toSay);
		out.flush();
	}

	private String hear() throws IOException {
		String inString = in.readLine();
		if ("23".indexOf(inString.charAt(0)) < 0) {
			throw new IOException("SMTP problem: " + inString);
		}
		return inString;
	}
}

