import java.net.*;
import java.io.*;
import java.util.*;

public class Notify {

	private String message;
	private BufferedReader in;
	private BufferedWriter out;

	public final static int SMTP_PORT = 25;

	public Notify(String message_in) {
		message = (new Date()) + ": " + message_in + "\n";
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
		//say("MAIL FROM: buster\n"); //CHECK!

		response = hear();

		// Hard coded to author/admin
		say("RCPT TO: " + getEmailAddress() + "\n");
		response = hear();

		// Single line delivery
		say("DATA\n");
		response = hear();
		say("Employee Async Server\n \n" +
				message + "\n.\n");
		response = hear();
		say("QUIT\n");

		s.close();
	}

	private String getEmailAddress() {
		File f;
		FileReader in = null;
		String directory = "./etc";
		String filename = "recipients";
		String DataString = null;
		String passwd = null;

		try {
			f = new File(directory, filename);
			in = new FileReader(f);
			int size = (int) f.length();
			char[] data = new char[size];
			int chars_read = 0;

			while (chars_read < size) {
				chars_read += in.read(data, chars_read, size - chars_read);
			}

			DataString = new String(data);

		}
		catch (IOException e) {
			System.out.println(e.getClass().getName() +
					": " + e.getMessage());
		}
		finally {
			try {
				if (in != null) {
					in.close();
				}
			}
			catch (IOException e) {
			}
		}
		return DataString.trim();
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

