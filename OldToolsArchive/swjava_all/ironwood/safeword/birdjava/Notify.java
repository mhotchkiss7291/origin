import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class Notify {

	private String message;
	private BufferedReader in;
	private BufferedWriter out;
	public static final int SMTP_PORT = 25;

	public Notify(String string) {
		message = String.valueOf(new Date()) + ": " + string + "\n";
	}

	private String getEmailAddress() {

		FileReader filereader = null;
		String string = "./etc";
		String string_0_ = "recipients";
		String string_1_ = null;
		Object object = null;

		try {
			File file = new File(string, string_0_);
			filereader = new FileReader(file);
			int i = (int) file.length();
			char[] cs = new char[i];

			for (int i_2_ = 0; i_2_ < i; 
						i_2_ += filereader.read(cs, i_2_, i - i_2_)) { 
			}
			string_1_ = new String(cs);
		} catch (IOException ioexception) {
			System.out.println(ioexception.getClass().getName() + ": "
				 + ioexception.getMessage());
		} finally {
			try {
				if (filereader != null) {
					filereader.close();
				}
			} catch (IOException ioexception) {
			}
		}
		return string_1_.trim();
	}

	private String hear() throws IOException {
		String string = in.readLine();

		if ("23".indexOf(string.charAt(0)) < 0) {
			throw new IOException("SMTP problem: " + string);
		}
		return string;
	}

	private void say(String string) throws IOException {
		out.write(string);
		out.flush();
	}

	public void send() throws IOException {
		Socket socket = new Socket("hanoi", 25);
		in = new BufferedReader(
				new InputStreamReader(socket.getInputStream(), "8859_1"));
		out = new BufferedWriter(
				new OutputStreamWriter(socket .getOutputStream(), "8859_1"));
		String string = hear();
		say("HELO hanoi\n");
		string = hear();
		say("MAIL FROM: hanoi\n");
		string = hear();
		say("RCPT TO: " + getEmailAddress() + "\n");
		string = hear();
		say("DATA\n");
		string = hear();
		say("Safeword Java Interface Notice\n \n" + message + "\n.\n");
		string = hear();
		say("QUIT\n");
		socket.close();
	}
}
