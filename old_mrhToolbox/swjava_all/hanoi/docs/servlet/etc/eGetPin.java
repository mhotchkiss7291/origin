import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import java.security.KeyStore;
import com.sun.net.ssl.*;
import COM.sun.ir.password.*;
import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;
import COM.sun.ir.password.*;

public class eGetPin extends HttpServlet {

	private boolean DEBUG = false;

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			 throws ServletException, IOException {

		doGet(req, res);

	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			 throws ServletException, IOException {

		PrintWriter out = null;

		String SunID = null;
		String RegisPW = null;

		String RequestType = null;
		String UserDataString = null;
		String UserID = null;
		String CardID = null;
		String PIN = null;
		String FirstName = null;
		String LastName = null;
		String Email = null;

		Vector requestVector = null;
		Vector replyVector = null;

		res.setContentType("text/html");
		out = res.getWriter();

		// Make sure that the user is really a Sun employee
		// based on their Sun ID and a Regis password
		SunID = req.getParameter("sunid");
		RegisPW = req.getParameter("regispw");


		HeadNTail ht = new HeadNTail();
		ht.putHead(out);
		out.println("<h2>Retrieve Your Token Card PIN</h2>");

		try {
      Password.checkPassword(SunID, RegisPW);
    } catch (PasswordException pe) {
			out.println("Combination of SunID and password invalid");
			out.println("\nTry again.");
			ht.putTail(out);
			return;
    }

    Hashtable env = new Hashtable(5, 0.75f);
    env.put(Context.INITIAL_CONTEXT_FACTORY,
            "com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.PROVIDER_URL, "ldap://sun-ds");
    env.put(Context.REFERRAL, "throw");

    try {

      DirContext ctx = new InitialDirContext(env);
      SearchControls controls = new SearchControls();
      controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

      NamingEnumeration results;

      while (true) {
        try {
          results = ctx.search("dc=sun, dc=com",
                      "employeenumber="+ SunID, controls);

          if (results == null) {
            out.println("There is not sufficient data available about ");
            out.println("this employee in the HR database.");
            return;
          }

          if ( !results.hasMore() ) {
            out.println("There is not sufficient data available about ");
            out.println("this employee in the HR database.");
            return;
          }

          /* for each entry print out name + all attrs and values */
          while (results != null && results.hasMore()) {
            SearchResult si = (SearchResult) results.next();

            /* print its name */
            Attributes attrs = si.getAttributes();

            if (attrs == null) {
             out.println("attrs=null");
              return;
            } else {

              /* print each attribute */
              for (NamingEnumeration ae = attrs.getAll();
                        ae.hasMoreElements();) {

                Attribute attr = (Attribute)ae.next();
                String attrId = attr.getID();

                // Extract the user's first and last name from the database
                if( attrId.equals("givenname")) {
                  FirstName = "";
                  FirstName = (String)attr.get();
                  if( FirstName.equals("") || FirstName.equals(null)) {
                    out.println("FirstName = null");
                    return;
                  }
                }

                if( attrId.equals("sn")) {
                  LastName = "";
                  LastName = (String)attr.get();
                  if( LastName.equals("") || LastName.equals(null)) {
                    out.println("LastName = null");
                    return;
                  }
                }

                if( attrId.equals("mail")) {
                  Email = "";
                  Email = (String)attr.get();
                  if( Email.equals("") || Email.equals(null)) {
                    out.println("Email = null");
                    return;
                  }
                }
              }
            }
          }
          break;

        // not sure what this is for, but left it in
        } catch (ReferralException e) {
          if (! followReferral(e.getReferralInfo())) {
            if (! e.skipReferral()) {
              out.println( "skipReferral" );
              return;
            }
          }

					/* point to the new context */
          ctx = (DirContext) e.getReferralContext();
        }
      }

    // If LDAP server is down or not working....
    } catch (NamingException e) {
      e.printStackTrace();
      out.println("LDAP server not contacted");
      return;
    }

    // Construct a Safeword UserID if needed
    StringBuffer buf = new StringBuffer();
    buf.append(FirstName.charAt(0));
    buf.append(LastName.charAt(0));
    buf.append(SunID);
    String safeID = new String( buf );
    UserID = safeID.toLowerCase();

		requestVector = new Vector();
		requestVector.setSize(6);
		replyVector = new Vector();
		replyVector.setSize(3);

		RequestType = new String("VERIFYACCOUNT");
		CardID = req.getParameter("cardid");

		// Index of the Vector going to Safeword
		requestVector.setElementAt(RequestType, 0);	// 0
		requestVector.setElementAt(UserID, 1);		// 1
		requestVector.setElementAt(CardID, 2);		// 2
		requestVector.setElementAt(FirstName, 3);	// 3
		requestVector.setElementAt(LastName, 4);	// 4
		requestVector.setElementAt(Email, 5);		// 5

		if (DEBUG) {
			out.println("Verify request = " + requestVector);
			out.println("<P>");
			out.println("<P>");
		}

		//IP for alcartaz server
		String AsyncServerIP = "129.147.30.70"; //CHECK!

		//Port for server
		int AsyncServerPort = 5532;

		replyVector = callSafeword(
				AsyncServerIP, AsyncServerPort, requestVector, out);

/****Start of repeat call ***/
		//Repeat the call to ironwood
		//IP for ironwood server
		AsyncServerIP = "129.147.30.50"; //CHECK!

		replyVector = callSafeword(
				AsyncServerIP, AsyncServerPort, requestVector, out);
/****End of repeat call ***/

		String s = (String) replyVector.elementAt(0);

		if (DEBUG) {
			out.println("Verify reply = " + replyVector);
			out.println("<P>");
			out.println("<P>");
		}

		if (!(s.equals("ACCOUNTVERIFIED"))) {

			out.println(
					"Your account or token card number could not be verified.");
			out.println("<P>");
			out.println("<P>");
			out.println(
					"Please try again or submit a Servicedesk ticket to obtain your PIN manually");
			out.println("<P>");
			out.println("<P>");
			out.println("Thank you.");

		} else {

			RequestType = "GETPIN";

			requestVector.setElementAt(RequestType, 0);

			if (DEBUG) {
				out.println("Getpin request = " + requestVector);
				out.println("<P>");
				out.println("<P>");
			}

			replyVector = getPin(requestVector, out);

			if (replyVector == null) {
				if (DEBUG) {
					out.println("reply = " + replyVector);
				}
				out.println("Your PIN could not be retrieved. Contact ServiceDesk.");
				ht.putTail(out);
				return;
			}

			if (DEBUG) {
				out.println("Getpin reply = " + replyVector);
				out.println("<P>");
				out.println("<P>");
			}

			PIN = new String((String) replyVector.elementAt(0));

			out.println(FirstName + " " + LastName);
			out.println("<P>");
			out.println("Your LoginID is : <b>" + UserID + "</b>");
			out.println("<P>");
			out.println("Your Token Card PIN is : <b>" + PIN + "</b>");
			out.println("<P>");
			out.println("Keep it secret!");
			out.println("<P>");
			out.println("<b>Do not store your token card PIN with your token card.");
			out.println("<br>");
			out.println("Doing so is a violation of Sun Security policy.</b>");
			out.println("<P>");
			out.println("<P>");
			out.println("You may want to change your token card PIN to something");
			out.println(" that is easier to remember. However, if you do change it, ");
			out.println("ServiceDesk will not be able to retrieve it for you if you forget it.");
			out.println("To change your token card PIN, perform the following steps.");
			out.println("<P>");
			out.println("<h2>Changing Your Token Card PIN </h2>");
			out.println("<ol>");
			out.println("<li>Press the Token Card's On button to turn on the card. ");
			out.println("The card's panel displays an EP prompt.");
			out.println("<li>Enter your 5-digit PIN. The card's panel displays an EH prompt.");
			out.println("<li>Press the Pin (P) key. The card's panel displays an SP prompt.");
			out.println("<li>Enter your new 5-digit PIN. (Your new PIN must have 5 digits.) Your PIN has just changed!");
			out.println("<p><b>Note:  If you enter your new PIN incorrectly, ");
			out.println("press the Clr key to cancel the new number. ");
			out.println("Your PIN reverts back to your pre-existing number.</b>");
			out.println("</ol>");

		}

		ht.putTail(out);

	}

	private Vector getPin(Vector v, PrintWriter out) {

		Socket s1 = null;
		InputStream s1In = null;
		ObjectInputStream ois = null;

		Vector replyVector = new Vector();
		replyVector.setSize(3);

		try {

			// birdman
			//s1 = new Socket("129.147.31.69", 5600);

			// ironwood
			s1 = new Socket("129.147.30.50", 5600);


		}
		catch (UnknownHostException uhe) {
			out.println("Can't get socket...");
			out.println("<P>");
			out.println("The server may be down");
			uhe.printStackTrace();
			return null;
		}
		catch (IOException e) {
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
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		ObjectOutputStream oos = null;

		try {
			oos = new ObjectOutputStream(s1Out);
		}
		catch (IOException e) {
			out.println("IOException in constructing ObjectOutputStream...");
			e.printStackTrace();
			return null;
		}

		// Ship out the data
		try {
			oos.writeObject(v);
		}
		catch (IOException e) {
			out.println("IOException when trying to write Object");
			e.printStackTrace();
			return null;
		}

		// Open input streams for replies from the server,
		// regardless of the outcome
		try {
			s1In = s1.getInputStream();
		}
		catch (IOException e) {
			out.println("IOException when trying to get InputStream");
			e.printStackTrace();
			return null;
		}

		try {
			ois = new ObjectInputStream(s1In);
		}
		catch (StreamCorruptedException sce) {
			out.println("Corrupted ObjectInputStream");
			sce.printStackTrace();
			return null;
		}
		catch (IOException e) {
			out.println("IOException in ObjectInputStream construction");
			e.printStackTrace();
			return null;
		}

		// Return a vector with transaction result
		try {
			replyVector = (Vector) ois.readObject();
		}
		catch (OptionalDataException ode) {
			out.println("OptionalDataException when readObject of ois");
			ode.printStackTrace();
			return null;
		}
		catch (ClassNotFoundException cnf) {
			out.println("Class Not Found in readObject");
			cnf.printStackTrace();
			return null;
		}
		catch (IOException e) {
			out.println("IO Exception in readObject");
			e.printStackTrace();
			return null;
		}

		// When done, just close the connection and exit
		try {
			ois.close();
		}
		catch (IOException e) {
			out.println("IOException in closing ois");
			e.printStackTrace();
			return null;
		}

		try {
			s1In.close();
		}
		catch (IOException e) {
			out.println("IOException in closing s1In");
			e.printStackTrace();
			return null;
		}

		try {
			s1.close();
		}
		catch (IOException e) {
			out.println("IOException in closing s1");
			e.printStackTrace();
			return null;
		}

		return replyVector;
	}

	private Vector callSafeword(
			String IP, int port, Vector v, PrintWriter out) {

		SSLSocket s1 = null;
		InputStream s1In = null;
		ObjectInputStream ois = null;

		Vector replyVector = new Vector();
		replyVector.setSize(3);

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
					"/opt/netscape/server4/docs/servlet/ks"); //CHECK!

        //Set transport layer security and other settings
        ctx = SSLContext.getInstance("TLS");

        kmf = KeyManagerFactory.getInstance("SunX509");

        ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(
					"/opt/netscape/server4/docs/servlet/ks"), //CHECK!
					passphrase);

        kmf.init(ks, passphrase);

        ctx.init(kmf.getKeyManagers(), null, null);

        factory = ctx.getSocketFactory();
      } catch (Exception e) {
          throw new IOException(e.getMessage());
      }
      //Construct socket and start handshake with server
      s1 = (SSLSocket)factory.createSocket( IP, port );
      s1.startHandshake();

			} catch (Exception e) {
					out.println("Exception in opening socket...");
					e.printStackTrace();
					return null;
			}

		OutputStream s1Out = null;

		// Open up output streams for shipping user data to the
		// server
		try {
			s1Out = s1.getOutputStream();
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		ObjectOutputStream oos = null;

		try {
			oos = new ObjectOutputStream(s1Out);
		}
		catch (IOException e) {
			out.println("IOException in constructing ObjectOutputStream...");
			e.printStackTrace();
			return null;
		}

		// Ship out the data
		try {
			oos.writeObject(v);
		}
		catch (IOException e) {
			out.println("IOException when trying to write Object");
			e.printStackTrace();
			return null;
		}

		// Open input streams for replies from the server,
		// regardless of the outcome
		try {
			s1In = s1.getInputStream();
		}
		catch (IOException e) {
			out.println("IOException when trying to get InputStream");
			e.printStackTrace();
			return null;
		}

		try {
			ois = new ObjectInputStream(s1In);
		}
		catch (StreamCorruptedException sce) {
			out.println("Corrupted ObjectInputStream");
			sce.printStackTrace();
			return null;
		}
		catch (IOException e) {
			out.println("IOException in ObjectInputStream construction");
			e.printStackTrace();
			return null;
		}

		// Return a vector just in case info needs to come
		// back. Not used right now
		try {
			replyVector = (Vector) ois.readObject();
		}
		catch (OptionalDataException ode) {
			out.println("OptionalDataException when readObject of ois");
			ode.printStackTrace();
			return null;
		}
		catch (ClassNotFoundException cnf) {
			out.println("Class Not Found in readObject");
			cnf.printStackTrace();
			return null;
		}
		catch (IOException e) {
			out.println("IO Exception in readObject");
			e.printStackTrace();
			return null;
		}

		// When done, just close the connection and exit
		try {
			ois.close();
		}
		catch (IOException e) {
			out.println("IOException in closing ois");
			e.printStackTrace();
			return null;
		}

		try {
			s1In.close();
		}
		catch (IOException e) {
			out.println("IOException in closing s1In");
			e.printStackTrace();
			return null;
		}

		try {
			s1.close();
		}
		catch (IOException e) {
			out.println("IOException in closing s1");
			e.printStackTrace();
			return null;
		}

		return replyVector;
	}

  private static boolean followReferral(Object referralInfo)
            throws IOException {
    byte[] reply = new byte[4];
    System.in.read(reply);
    return (reply[0] != 'n');
  }

}
