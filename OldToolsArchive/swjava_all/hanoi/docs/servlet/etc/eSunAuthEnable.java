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

//main class for a user to enable a token card 
public class eSunAuthEnable extends HttpServlet {


	public void doPost(HttpServletRequest req, HttpServletResponse res)
			 throws ServletException, IOException {

		doGet(req, res);

	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			 throws ServletException, IOException {

		String SunID = null;
		String RegisPW = null;
		String UserDataString = null;

		String RequestType = null;
		String UserID = null;
		String CardID = null;
		String ConfirmCardID = null;
		String FirstName = null;
		String LastName = null;
		String Email = null;
		String AsynchSafewordIP = null;
		String SynchSafewordIP = null;
		int AsynchPort;              
		int SynchPort;              

		Vector requestVector = null;
		Vector replyVector = null;

		PrintWriter out = null;

		res.setContentType("text/html");
		out = res.getWriter();

		// Make sure that the user is really a Sun employee
		// based on their Sun ID and a Regis password
		SunID = req.getParameter("sunid");
		RegisPW = req.getParameter("regispw");

		HeadNTail ht = new HeadNTail();
		ht.putHead(out);
		out.println("<h2>New Token Card Registration</h2>");

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
		replyVector.setSize(1);

		RequestType = new String("ENABLE");
		CardID = req.getParameter("cardid");
		ConfirmCardID = req.getParameter("confcardid");

		if (!ConfirmCardID.equals(CardID)) {
			out.println("<b>ERROR: </b>");
			out.println(
					"The two token card serial numbers you entered do not match.");
			out.println("<p>");
			out.println("\nPlease try again.");
			ht.putTail(out);
			return;
		}

		// Index of the Vector going to Safeword
		requestVector.setElementAt(RequestType, 0);
		requestVector.setElementAt(UserID, 1);
		requestVector.setElementAt(CardID, 2);
		requestVector.setElementAt(FirstName, 3);
		requestVector.setElementAt(LastName, 4);
		requestVector.setElementAt(Email, 5);

		
	 	// IP for alcatraz server
		AsynchSafewordIP = "129.147.30.70"; //CHECK

		AsynchPort = 5532;							 //CHECK

		replyVector = callSafeword( 
			AsynchSafewordIP, AsynchPort, requestVector, out );   

	 	// IP for ironwood server
		AsynchSafewordIP = "129.147.30.50"; //CHECK
		replyVector = callSafeword( 
			AsynchSafewordIP, AsynchPort, requestVector, out );   
/****End of repeat call ***/

		String s = (String)replyVector.elementAt( 0 );

		if( s.equals("ENABLECONFIRMED") ) {
	 
		  //Connect to Synchrozined database for VPN
	 
			//IP for ironwood server
			SynchSafewordIP = "129.147.30.50"; //CHECK!

			SynchPort = 6500; //CHECK!

			replyVector = callSafeword(
				SynchSafewordIP, SynchPort, requestVector, out);

			String s2 = (String) replyVector.elementAt(0);

	 		if(s2.equals("ENABLECONFIRMED")) {

				out.println("Your token card has been successfully registered.");
				out.println("<P>");
				out.println("When logging in with your token card, your LoginID is:");
				out.println("<P>");
				out.println("<b>" + UserID + "</b>" );
				out.println("<P>");
				out.println("Now retrieve your token card PIN.");
				out.println("<p>");
				out.println(
					"<a href=\"/servlet/GetPin\"> Retrieve your token card PIN</a>");

			} else {

				//request will be type "ROLLBACK"
				requestVector.setElementAt("ROLLBACK", 0);

				//value used to determine which type of rollback
				requestVector.setElementAt("ENABLE", 5);

				replyVector = callSafeword(
					AsynchSafewordIP, AsynchPort, requestVector, out);

				String s3 = (String)replyVector.elementAt(0);

				if (s3.equals("ROLLBACKCONFIRMED")) {
					out.println(
						"Your token card was not registered due to the following reason:");
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

			out.println("Your card was not registered due to the following reason:");
		  out.println("<P>");

		  if( s == null ) {
				out.println("Null values returned from the database.");
			} else {
				out.println( s );
				out.println("<P>");
				out.println("Contact ServiceDesk if you have questions.");
			}
		}
		 
		ht.putTail(out);

	}

	private Vector callSafeword(
			String IP, int port, Vector v, PrintWriter out) {

		SSLSocket s1 = null;
		InputStream s1In = null;
		ObjectInputStream ois = null;
		String AsynchSafewordIP = null;

		Vector replyVector = new Vector();
		replyVector.setSize(1);

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

				ks.load(
					new FileInputStream(
						"/opt/netscape/server4/docs/servlet/ks"), //CHECK!
					passphrase);

				kmf.init(ks, passphrase);

				ctx.init(kmf.getKeyManagers(), null, null);

				factory = ctx.getSocketFactory();
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}

			try{
				s1 = (SSLSocket)factory.createSocket( IP, port );
			} catch (IOException e) {

				//request will be type "ROLLBACK"
				v.setElementAt("ROLLBACK", 0);

				//value used to determine which type of rollback
				v.setElementAt("ENABLE", 5);

				// IP for alcatraz server
				AsynchSafewordIP = "129.147.30.70"; //CHECK!

				int AsynchPort = 5532;							 //CHECK

				replyVector = callSafeword( 
					AsynchSafewordIP, AsynchPort, v, out );

				//IP for ironwood
				AsynchSafewordIP = "129.147.30.50"; //CHECK!
				replyVector = callSafeword( 
					AsynchSafewordIP, AsynchPort, v, out );
/*********End of repeat call to ironwood ****/
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
			oos.writeObject(v);
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

		// Return a vector with transaction result
		try {
			replyVector = (Vector) ois.readObject();
		} catch (OptionalDataException ode) {
			out.println("OptionalDataException when readObject of ois");
			ode.printStackTrace();
			return null;
		} catch (ClassNotFoundException cnf) {
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

  private static boolean followReferral(Object referralInfo)
            throws IOException {
    byte[] reply = new byte[4];
    System.in.read(reply);
    return (reply[0] != 'n');
  }
}

