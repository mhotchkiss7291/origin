import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import com.sun.net.ssl.*;
import java.security.KeyStore;

public class GSReturnPIN extends HttpServlet {

	private boolean DEBUG = false;

  public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		doGet( req, res );

	}

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		PrintWriter out = null;

		String SunID = null;

		String RequestType = null;
		String UserID = null;
		String CardID = null;
		String PIN = null;

    String AsynchSafewordIP = null; 
    int AsynchPort;

		Vector requestVector = null;
		Vector replyVector = null;

    res.setContentType("text/html");
    out = res.getWriter();

    // Make sure that the user is really a Sun employee
    // based on their Sun ID and a Regis password
    SunID = req.getParameter("sunid");

		GSHeadNTail ht = new GSHeadNTail();
		ht.putHead( out );

		requestVector = new Vector();
		requestVector.setSize( 3 );
		replyVector = new Vector();
		replyVector.setSize( 3 );

		RequestType = new String("VERIFYACCOUNT");
    UserID = req.getParameter("userid");
    CardID = req.getParameter("cardid");

		// Index of the Vector going to Safeword
		requestVector.setElementAt( RequestType, 0 ); 	// 0
		requestVector.setElementAt( UserID, 1 ); 			// 1
		requestVector.setElementAt( CardID, 2 ); 			// 2

		out.println("<center>");

		if( DEBUG ) {
			out.println( "Verify request = " + requestVector );
			out.println("<P>");
			out.println("<P>");
		}

		//IP for alcatraz server
		AsynchSafewordIP = "129.147.30.70"; //CHECK!

		// Check port
		AsynchPort = 5432; //CHECK!

		replyVector = callSafeword( 
			AsynchSafewordIP, AsynchPort, requestVector, out );

		//IP for ironwood server
		AsynchSafewordIP = "129.147.30.50"; //CHECK!
		replyVector = callSafeword( 
			AsynchSafewordIP, AsynchPort, requestVector, out );
/***End of repeat call to ironwood **/

		if( DEBUG ) {
			out.println( "Verify reply = " + replyVector );
			out.println("<P>");
			out.println("<P>");
		}

		if( !((String)replyVector.elementAt( 0 )).equals("ACCOUNTVERIFIED")) {

			out.println(
				"There has been a problem retrieving the information requested. Either an account could not be found or the token card serial number given does not match the token card serial number in the record.");
			out.println("<P>");
			out.println(
				"Transfer the ticket to the RCS_reqs queue for backline support and resolution.");

		} else {

			RequestType = "GETPIN";

			requestVector.setElementAt( RequestType, 0 );

			if( DEBUG ) {
				out.println( "Getpin request = " + requestVector );
				out.println("<P>");
				out.println("<P>");
			}

			replyVector = getPin( requestVector, out );

			if( replyVector == null ) {
				if( DEBUG ) {
					out.println( "reply = " + replyVector );
				}
				out.println("Your PIN could not be retrieved.");
				out.println("<P>");
				out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/GSRetrieveUserPIN\">Return to Retrieve User PIN</A></FONT>");
				out.println("<P>");
				out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/GeneralServices\">Return to General Services</A></FONT>");
				out.println("</center>");
				ht.putTail( out );
				return;
			}

			if( DEBUG ) {
				out.println( "Getpin reply = " + replyVector );
				out.println("<P>");
				out.println("<P>");
			}

			PIN = new String( (String) replyVector.elementAt( 0 ) );

			out.println( "UserID : <b>" + UserID + "</b>" ); 
			out.println( "<P>" ); 
			out.println( "Card Serial Number : <b>" + CardID.toUpperCase() + "</b>" ); 
			out.println( "<P>" ); 
			out.println( "PIN is : <b>" + PIN + "</b>" ); 
			out.println( "<P>" ); 

		}

		out.println("<P>");
		out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/GSRetrieveUserPIN\">Return to Retrieve User PIN</A></FONT>");
		out.println( "<P>" ); 
		out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/GeneralServices\">Return to General Services</A></FONT>");
		out.println("</center>");
		ht.putTail( out );

  }

  private Vector callSafeword(
			String IP, int port, Vector v, PrintWriter out) {

    SSLSocket s1 = null;
    InputStream s1In = null;
    ObjectInputStream ois = null;
		Vector replyVector = null;

    try {

			SSLSocketFactory factory = null;

			try {
				SSLContext ctx;
				KeyManagerFactory kmf;
				KeyStore ks;

				// Password to the alias
        char[] passphrase = "password".toCharArray(); //CHECK!

				// Set keystore property variables
        System.setProperty(
					"javax.net.ssl.trustStore", 
					"/opt/netscape/server4/docs-BCA/ks"); //CHECK!

				// Set transport layer security and other settings
				ctx = SSLContext.getInstance("TLS");
				kmf = KeyManagerFactory.getInstance("SunX509");
				ks = KeyStore.getInstance("JKS");

				// Submit passphrase
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

			// Construct Socket and handshake with server
			s1 = (SSLSocket)factory.createSocket(IP, port);
			s1.startHandshake();

			replyVector = new Vector();
			replyVector.setSize( 3 );

    } catch (UnknownHostException uhe) {
      out.println("Can't get socket...");
      out.println("<P>");
      out.println("The server may be down.");
      uhe.printStackTrace();
      return null;
    } catch (IOException e) {
      out.println("IOException in opening socket...");
      out.println("<P>");
      out.println("The server may be down.");
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



  private Vector getPin( Vector v, PrintWriter out ) {

    Socket s1 = null;
    InputStream s1In = null;
    ObjectInputStream ois = null;

		Vector replyVector = new Vector();
		replyVector.setSize( 3 );

    try {

      // ironwood
      s1 = new Socket("129.147.30.50", 5600); //CHECK!

    } catch (UnknownHostException uhe) {
      out.println("Can't get socket...");
      out.println("<P>");
      out.println("The server may be down");
      uhe.printStackTrace();
      return null;
    } catch (IOException e) {
      out.println("IOException in opening socket...");
      out.println("<P>");
      out.println("The server may be down");
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
