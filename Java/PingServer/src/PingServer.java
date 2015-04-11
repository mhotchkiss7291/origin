import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PingServer {

	private String email_address = null;
	private ArrayList<String> ip_list = null;
	private ArrayList<String> report_list = null;
	private String report_string = null;

	public static void main(String[] args) {
		PingServer ps = new PingServer();
		ps.readIPs(args[0]);
		ps.pingServers();
		ps.sendReport();
	}

	void readIPs(String email) {

		// Set the email local variable
		email_address = email;

		// Stuff the IP addresses into a list
		Scanner s = null;
		try {
			s = new Scanner(new File("C:\\PingServers\\ip_addresses.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Construct the local ip array_list
		ip_list = new ArrayList<String>();

		// Populate the list from the scanned file
		while (s.hasNext()) {
			ip_list.add(s.next());
		}
		
		// Close the Scanner
		s.close();

	}

	void pingServers() {

		// Initalize local variables
		int i = 0;
		InetAddress inet = null;

		// Construct a list for reporting ping replies
		report_list = new ArrayList<String>();

		// Ping each IP
		while (i < ip_list.size()) {

			try {
				inet = InetAddress.getByName(ip_list.get(i));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			// Temporary debug print
			System.out.println("Sending Ping Request to " + ip_list.get(i));

			// Determine if the hosts are reachable
			try {
				
				// Ping the IP for 5000 for reply.   
				if (inet.isReachable(5000)) {
					report_list.add(i, ip_list.get(i) + ": Host is reachable");
				} else {
					report_list.add(i, ip_list.get(i) + ": Host is NOT reachable");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			i++;
		}

		// Construct a message for email
		StringBuilder sb = new StringBuilder();
		i = 0;

		// Populate the SB with values
		while (i < report_list.size()) {
			sb.append(report_list.get(i) + "\n");
			i++;
		}

		// Convert the SB to 
		this.report_string = sb.toString() ;
		
	}

	void sendReport() {

		// Send the message to the sender (yourself)
		String to = this.email_address;
		String from = this.email_address;

		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Set Subject: header field
			message.setSubject("Checking server status...");

			// Now set the actual message
			message.setText(this.report_string);

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
