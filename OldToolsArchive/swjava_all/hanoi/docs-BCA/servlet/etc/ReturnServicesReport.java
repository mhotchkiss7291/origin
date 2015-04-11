//Called by RCServicesReport class.
//Sends request to server and displays response.

import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import java.security.KeyStore;
import com.sun.net.ssl.*;

public class ReturnServicesReport extends HttpServlet {

  private String RequestType = null;
  private String Service = null;

  private String AsynchSafewordIP = null;
  private String SynchSafewordIP = null;
  private int AsynchPort;
  private int SynchPort;

  private Vector requestVector = null;
  private Vector replyVector = null;

	private boolean DEBUG = false;

  public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		doGet( req, res );

	}

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		PrintWriter out = null;

    res.setContentType("text/html");
    out = res.getWriter();
    HttpSession session = req.getSession(true);
  
//NEW CODE 
    String Email = (String)req.getParameter("email");
 
    Service = req.getParameter("service");
  
    GSHeadNTail ht = new GSHeadNTail();
    ht.putHead( out );
    
    requestVector = new Vector();
    replyVector = new Vector();
    
    RequestType = new String("RCREPORT");
    
    // Index of the Vector going to Safeword
    requestVector.addElement( RequestType );   // 0
    requestVector.addElement( Service );			 // 1
    requestVector.addElement( Email );                         // 2

    out.println("<center>");
    
    //IP address for alcatraz
    AsynchSafewordIP = "129.147.30.70"; //CHECK!

    AsynchPort = 5432; //CHECK!

    replyVector = callSafeword(
			AsynchSafewordIP, AsynchPort, requestVector, out );

    //IP address for ironwood
    AsynchSafewordIP = "129.147.30.50"; //CHECK!
    replyVector = callSafeword(
			AsynchSafewordIP, AsynchPort, requestVector, out );
/***End of repeat call to ironwood **/

    String s = (String)replyVector.elementAt( 0 );

    replyVector.trimToSize();

    Service = req.getParameter("service");
 
    out.println("<h1>Users with service " + Service + "</h1>");


    if( s.equals("REPORTCONFIRMED") ) {
      
      out.println("<p>");

      out.println("<table>");
      out.println("<tr>");

      //place user names in table for display
      int td = 1;
      for(int record=1; record < replyVector.size(); record++) {
        if( td < 3 ) {
          out.println("<td>" + replyVector.elementAt(record) + "</td>");
          td++;
        } else {
          out.println("<td>" + replyVector.elementAt(record) + "</td></tr>");
          td = 1;
        }
      }

      
      out.println("</table>");
      out.println("<p>");

		} else {
			out.println("No users were returned for this service.");
			out.println("<p>");

			if( s.equals("no data") ) {
				out.println("Null values returned from the database.");
			} else {
				out.println( s );
				out.println("<p>");
				out.println("Contact ServiceDesk if you have questions.");
			}
		}

    out.println("<p>");
    out.println(
			"<a href=\"/BCA/RCServicesReport\">Return to Services Report.</a href>");
    out.println("<p>");
    out.println(
			"<a href=\"/BCA/BusinessServices\">Return to Business Services.</a href>");

		ht.putTail( out );

	}

	// Connect to dedicated Safeword server
  private Vector callSafeword(
			String IP, int port, Vector v, PrintWriter out) { 

    SSLSocket s1 = null;
    InputStream s1In = null;
    ObjectInputStream ois = null;

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

      //Construct socket and start handshake with server
      try{
        s1 = (SSLSocket)factory.createSocket( IP, port );
      } catch (IOException e) {
			  e.printStackTrace();
        return null; 
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
