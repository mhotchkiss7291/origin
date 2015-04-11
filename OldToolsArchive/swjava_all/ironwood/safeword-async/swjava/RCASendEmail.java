import java.net.*;
import java.io.*;
import java.util.*;

public class RCASendEmail {

  private String[] message = null;
  private BufferedReader in;
  private BufferedWriter out;
  private String Email;
  private int message_length;
  public static final int SMTP_PORT = 25;
  private String Service;

  public boolean debug = false;


  // Constructor
  public RCASendEmail(
			String email, 
			Vector resultsVector, 
			String service) {

    Service = service;
    message_length = 0;
    message = new String[resultsVector.size() + 3];
    Email =  new String(email);
    String s = (String)resultsVector.elementAt(0);

		if( s.equals("REPORTCONFIRMED") ) {

    	for(int record = 1; record < resultsVector.size() ; record++) {
				message[message_length++] = 
					(String)resultsVector.elementAt(record); 
			} 

		} else {
			message[message_length++]=
				"No users were returned for this service.";

			if( s.equals("no data") ) {
				message[message_length++]="";

				message[message_length++]=
					"Null values returned from the database.";
	  	}
    }

		if(debug) System.out.println("Constructed");

  }
  
  // Delivery method
  public void send() throws IOException {

		if(debug) System.out.println("FIRST CHECK");
    
    // I/O setup
    Socket s = new Socket("hanoi", SMTP_PORT); //CHECK!
    //Socket s = new Socket("buster", SMTP_PORT); //CHECK!

		if(debug) System.out.println("SECOND CHECK");

    in = new BufferedReader(
          new InputStreamReader(s.getInputStream(), "8859_1"));
    out = new BufferedWriter(
          new OutputStreamWriter(s.getOutputStream(), "8859_1"));
    String response;

		if(debug) System.out.println("THIRD CHECK");

    // Converse with SMTP
    response = hear();
    say("HELO hanoi\n"); //CHECK!
    //say("HELO buster\n"); //CHECK!
    response = hear();
    say("MAIL FROM: hanoi\n"); //CHECK!
    //say("MAIL FROM: buster\n"); //CHECK!
    response = hear();

		if(debug) System.out.println("FOURTH CHECK");

    // Hard coded to author/admin
    say("RCPT TO: " + Email + "\n");
    response = hear();

		if(debug) System.out.println("Starting to send 1");

    // Single line delivery
    say("DATA\n");
    response = hear();    

    say("This email was sent as a request. \n \n");
    say( "Here are the results from your search: \n \n");

		if(debug) System.out.println("Starting to send 2");

    say("Users with service " + Service + "\n \n");

    for(int i = 0; i < message_length; i++) {
      say(message[i] + "\n");
    }

		say("\n.\n");
    response = hear();

		if(debug) System.out.println("Starting to send 3");

    say("QUIT\n");

    s.close();

		if(debug) System.out.println("JUST SENT");

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
