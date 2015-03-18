import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import java.security.KeyStore;
import com.sun.net.ssl.*;

public class RCReplaceResult extends HttpServlet {


	private boolean DEBUG = true;

  public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		doGet( req, res );

	}

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		String RequestType = null;
		String UserID = null;
		String FirstName = null;
		String LastName = null;
		String CardID = null;
		String ConfirmCardID = null;

		String AsynchSafewordIP = null;
		String SynchSafewordIP = null;
		int AsynchPort;
		int SynchPort;

		Vector requestVector = null;
		Vector replyVector = null;

		PrintWriter out = null;

    res.setContentType("text/html");
    out = res.getWriter();

		UserID = req.getParameter("userid");
		FirstName = req.getParameter("firstname");
		LastName = req.getParameter("lastname");
		CardID = req.getParameter("cardid");
		ConfirmCardID = req.getParameter("confirmcardid");

		GSHeadNTail ht = new GSHeadNTail();
		ht.putHead( out );

		requestVector = new Vector();
    replyVector = new Vector();

		if ( !ConfirmCardID.equals( CardID ) ) {
      out.println("The token card serial numbers you entered do not match.");
      out.println("\nTry again.");
      ht.putTail( out );
      return;
    }

		RequestType = new String("RCREPLACE");

		// Index of the Vector going to Safeword
    requestVector.addElement( RequestType );   // 0
    requestVector.addElement( UserID );        // 1
    requestVector.addElement( CardID );        // 2
    requestVector.addElement( FirstName );     // 3
    requestVector.addElement( LastName );      // 4

		//5 - used as a place holder is case a rollback is needed
    requestVector.addElement( "nothing" );

		out.println("<center>");

	 	// IP for alcatraz server
		AsynchSafewordIP = "129.147.30.70"; //CHECK!

		// check port
		AsynchPort = 5432; //CHECK!

		replyVector = callSafeword( 
			AsynchSafewordIP, AsynchPort, requestVector, out );

	 	// IP for ironwood server
		AsynchSafewordIP = "129.147.30.50"; //CHECK!
		replyVector = callSafeword( 
			AsynchSafewordIP, AsynchPort, requestVector, out );
/***End of repeat call to ironwood ***/

		String s = (String)replyVector.elementAt( 0 );

		if( s.equals("REPLACECONFIRMED") ) {

			//Connect to Synchrozined database for VPN

			//IP for ironwood server
			SynchSafewordIP = "129.147.30.50"; //CHECK!

			// check port
			SynchPort = 6300; //CHECK!

			replyVector = callSafeword(
					SynchSafewordIP, SynchPort, requestVector, out);

			String s2 = (String) replyVector.elementAt(0);

	 		if(s2.equals("REPLACECONFIRMED")) {
				out.println(
					"The user's replacement token card has been successfully registered.");
				out.println("<P>");
				out.println("The user's LoginID is:");
				out.println("<b> " + UserID + "</b>" );
			} else {

				//request will be type "ROLLBACK"
				requestVector.setElementAt("RCROLLBACK", 0);

				//used to determine which type of rollback
				requestVector.setElementAt("RCREPLACE", 5);

				replyVector = callSafeword(
					AsynchSafewordIP, AsynchPort, requestVector, out);

				String s3 = (String)replyVector.elementAt(0);

			 	if (s3.equals("ROLLBACKCONFIRMED")) {
					out.println(
						"Your token card was not registered due to the following reason:");
					out.println("<P>");
					out.println("An error occurred while trying to synchronize the databases.");
				} else {
					out.println( s2 );
					out.println("<P>");
					out.println("Contact ServiceDesk if you have questions.");
				}
			}
		} else {
			out.println(
				"The user's token card was not registered due to the following reason:");
			out.println("<P>");

			if( s == null ) {
				out.println("Null values returned from the database.");
			} else {
				out.println( s );
				out.println("<P>");
				out.println("Contact ServiceDesk if you have questions.");
			}
		}

    out.println("<p>");
    out.println(
			"<a href=\"/BCA/RCReplaceTokenCard\">Return to Replace User's Token Card.</a href>");
    out.println("<p>");
    out.println(
			"<a href=\"/BCA/GeneralServices\">Return to General Services.</a href>");

		ht.putTail( out );

	}

	// Connect to dedicated Safeword server
  private Vector callSafeword(
			String IP, int port, Vector v, PrintWriter out) {

    SSLSocket s1 = null;
    InputStream s1In = null;
    ObjectInputStream ois = null;
		String AsynchSafewordIP = null;
		int AsynchPort = 0;

    Vector replyVector = new Vector();
    replyVector.setSize( 1 );

  	try {

			SSLSocketFactory factory = null;

			try {
				SSLContext ctx;
				KeyManagerFactory kmf;
				KeyStore ks;

				//Password to the alias
				char[] passphrase = "password".toCharArray(); //CHECK!

				System.setProperty(
					"javax.net.ssl.trustStore", 
					"/opt/netscape/server4/docs-BCA/ks"); //CHECK!

				//Set transport layer security and other settings
				ctx = SSLContext.getInstance("TLS");

				kmf = KeyManagerFactory.getInstance("SunX509");

				ks = KeyStore.getInstance("JKS");

				ks.load(
					new FileInputStream(
						"/opt/netscape/server4/docs-BCA/ks"), //CHECK!
						passphrase);

				kmf.init(ks, passphrase);

				ctx.init(kmf.getKeyManagers(), null, null);

				factory = ctx.getSocketFactory();
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}

			try {
				s1 = (SSLSocket)factory.createSocket( IP, port );
			} catch (IOException e) {

				//request will be type "ROLLBACK"
				v.setElementAt("RCROLLBACK", 0);

				//value used to determine which type of rollback
				v.setElementAt("RCREPLACE", 5);

	 			// IP for alcatraz server
				AsynchSafewordIP = "129.147.30.70"; //CHECK!

				// check port
				AsynchPort = 5432; //CHECK!

				replyVector = callSafeword( 
						AsynchSafewordIP, AsynchPort, v, out );

	 			// IP for ironwood server
				AsynchSafewordIP = "129.147.30.50"; //CHECK!
				replyVector = callSafeword( 
						AsynchSafewordIP, AsynchPort, v, out );
/****End of repeat call to ironwood ***/
			}

			s1.startHandshake();

    } catch (UnknownHostException uhe) {
      out.println("Can't get socket...");
      uhe.printStackTrace();
      return null;
    } catch (IOException e) {
      out.println("IOException in opening socket...");
      e.printStackTrace();
      return null;
    }

    OutputStream s1Out = null;

    // Open up output streams for shipping user data to the
    // server
    try {
      s1Out = s1.getOutputStream();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    ObjectOutputStream oos = null;

		try {
      oos = new ObjectOutputStream(s1Out);
    } catch (IOException e) {
      out.println("IOException in constructing ObjectOutputStream...");
      e.printStackTrace();
      return null;
    }

    // Ship out the data
    try {
      oos.writeObject( v );
    } catch (IOException e) {
      out.println("IOException when trying to write Object");
      e.printStackTrace();
      return null;
    }

    // Open input streams for replies from the server,
    // regardless of the outcome
    try {
      s1In = s1.getInputStream();
    } catch (IOException e) {
      out.println("IOException when trying to get InputStream");
      e.printStackTrace();
      return null;
    }

    try {
      ois = new ObjectInputStream(s1In);
    } catch (StreamCorruptedException sce) {
      out.println("Corrupted ObjectInputStream");
      sce.printStackTrace();
      return null;
    } catch (IOException e) {
      out.println("IOException in ObjectInputStream construction");
      e.printStackTrace();
      return null;
    }

		// Return a vector just in case info needs to come
    // back. Not used right now
    try {
      replyVector = (Vector)ois.readObject();
    } catch ( OptionalDataException ode ) {
      out.println("OptionalDataException when readObject of ois");
      ode.printStackTrace();
      return null;
    } catch ( ClassNotFoundException cnf) {
      out.println("Class Not Found in readObject");
      cnf.printStackTrace();
      return null;
    } catch (IOException e) {
      out.println("IO Exception in readObject");
      e.printStackTrace();
      return null;
    }

    // When done, just close the connection and exit
    try {
      ois.close();
    } catch (IOException e) {
      out.println("IOException in closing ois");
      e.printStackTrace();
      return null;
    }

    try {
      s1In.close();
    } catch (IOException e) {
      out.println("IOException in closing s1In");
      e.printStackTrace();
      return null;
    }

    try {
      s1.close();
    } catch (IOException e) {
      out.println("IOException in closing s1");
      e.printStackTrace();
      return null;
    }

    return replyVector;
  }

}
