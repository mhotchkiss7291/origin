import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import com.sun.net.ssl.*;
import java.security.KeyStore;

public class DeleteResult extends HttpServlet {


	private boolean DEBUG = false;

  public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		doGet( req, res );

	}

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		String AsynchSafewordIP = null;
		String SynchSafewordIP = null;
		int AsynchPort;              
		int SynchPort;              

		Vector requestVector = null;
		Vector replyVector = null;

		PrintWriter out = null;

		String SunID = null;

		String RequestType = null;
		String UserID = null;
		String ConfirmUserID = null;

    res.setContentType("text/html");
    out = res.getWriter();

    // Make sure that the user is really a Sun employee
    // based on their Sun ID and a Regis password
    SunID = req.getParameter("sunid");

		GSHeadNTail ht = new GSHeadNTail();
		ht.putHead( out );

		requestVector = new Vector();
		requestVector.setSize( 6 );
		replyVector = new Vector();
		replyVector.setSize( 1 );

    UserID = req.getParameter("userid");
    ConfirmUserID = req.getParameter("confuserid");

		if( !(UserID.equals( ConfirmUserID ))) {
			out.println( "<P>" );
			out.println( "The User ID you entered for confirmation does not match the original one. " );
			out.println( "<P>" );
			out.println( "Please try again. " );
			out.println( "<P>" );
			ht.putTail( out );
			return;
		}

		RequestType = new String("DELETEACCOUNT");

		// Index of the Vector going to Safeword
		requestVector.setElementAt( RequestType, 0 ); 	// 0
		requestVector.setElementAt( UserID, 1 ); 			// 1

		out.println("<center>");

		if( DEBUG ) {
			out.println( "Verify request = " + requestVector );
			out.println("<P>");
			out.println("<P>");
		}


    // IP for alcatraz server
    AsynchSafewordIP = "129.147.30.70"; //CHECK!

    AsynchPort = 5432; //CHECK!

    replyVector = callSafeword( 
			AsynchSafewordIP, AsynchPort, requestVector, out );

    // IP for ironwood server
    AsynchSafewordIP = "129.147.30.50"; //CHECK!
    replyVector = callSafeword( 
			AsynchSafewordIP, AsynchPort, requestVector, out );
/*****End of repeat call to ironwood  **/

		if( DEBUG ) {
			out.println( "Verify reply = " + replyVector );
			out.println("<P>");
			out.println("<P>");
		}

    String s = (String)replyVector.elementAt( 0 );

		if(s.equals("DELETEVERIFIED")) {

       //connect to synchronized database for VPN

       //IP for ironwood VPN server
       SynchSafewordIP = "129.147.30.50"; //CHECK!

			 //	Check port
       SynchPort = 6300; //CHECK!

       replyVector = callSafeword(
					SynchSafewordIP, SynchPort, requestVector, out);

       String s2 = (String) replyVector.elementAt(0);

       if (s2.equals("DELETEVERIFIED")) {

          out.println("The employee's token card has been successfully deleted.");
          out.println("<P>");
          out.println("The employee's LoginID was:");
          out.println("<b> " + UserID + "</b>" );

       } else {

   				//request will be type "ROLLBACK"
          requestVector.setElementAt("RCROLLBACK", 0);

					//value used to determine which type of rollback
          requestVector.setElementAt("RCDELETE", 5);

          replyVector = callSafeword(
							AsynchSafewordIP, AsynchPort, requestVector, out);

          String s3 = (String)replyVector.elementAt(0);

          if (s3.equals("ROLLBACKCONFIRMED")) {

            out.println(
							"The employee's token card was not deleted due to the following reason:");
            out.println("<P>");
            out.println(
							"An error occurred while trying to synchronize the databases.");
          } else {
            out.println( s2 );
            out.println("<P>");
            out.println("Contact ServiceDesk if you have questions.");
          }
       }

     } else {
        out.println(
					"There has been a problem deleting the account. Either an account could not be found or some other error occurred.");
        out.println("<P>");
        out.println(
					"Transfer the ticket to the RCS_reqs queue for backline support and resolution.");
     }

    out.println("<p>");
    out.println(
			"<a href=\"/BCA/DeleteAccount\">Return to Employee Account Deletion.</a href>");
    out.println("<p>");
    out.println(
			"<a href=\"/BCA/GeneralServices\">Return to General Services.</a href>");

		out.println("</center>");
		ht.putTail( out );

  }


  private Vector callSafeword(
			String IP, int port, Vector v, PrintWriter out) { 

    SSLSocket s1 = null;
    InputStream s1In = null;
    ObjectInputStream ois = null;
		Vector replyVector = null;

		String AsynchSafewordIP = null;
		int AsynchPort = 0;

    try {

			SSLSocketFactory factory = null;

			try {
				SSLContext ctx;
				KeyManagerFactory kmf;
				KeyStore ks;

				// Password to the alias
        char[] passphrase = "password".toCharArray();	 //CHECK!			

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
    try{
      s1 = (SSLSocket)factory.createSocket( IP, port );
    } catch (IOException e) {

			//request will be type "ROLLBACK"
      v.setElementAt("RCROLLBACK", 0); 

			//value used to determine which type of rollback
      v.setElementAt("RCDELETE", 5);

    	// IP for alcatraz server
    	AsynchSafewordIP = "129.147.30.70"; //CHECK!

    	AsynchPort = 5432; //CHECK!

      replyVector = callSafeword( 
					AsynchSafewordIP, AsynchPort, v, out );

    	// IP for ironwood server
    	AsynchSafewordIP = "129.147.30.50"; //CHECK!
      replyVector = callSafeword( 
					AsynchSafewordIP, AsynchPort, v, out );
/*****End of repeat call to ironwood ****/

    }

		s1.startHandshake();

		replyVector = new Vector();

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
}
