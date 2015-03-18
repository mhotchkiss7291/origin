import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;
import COM.sun.ir.password.*;

public class RegisNLdap extends HttpServlet {

	private boolean DEBUG = true;

  public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
    doGet( req, res );
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    PrintWriter out = null;

    String SunID = null;
    String RegisPW = null;

    res.setContentType("text/html");
    out = res.getWriter();

    SunID = req.getParameter("sunid");
    RegisPW = req.getParameter("regispw");

    try {
      Password.checkPassword(SunID, RegisPW);
    } catch (PasswordException pe) {
      pe.printStackTrace();
      out.println("Failed");
      return;
    }

		String UserID = null;
		String FirstName = null;
		String LastName = null;
		String Email = null;

		Hashtable env = new Hashtable(5, 0.75f);
    env.put(Context.INITIAL_CONTEXT_FACTORY,
            "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://ldap1.ebay.sun.com:389");
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

    out.println( "FirstName = " + FirstName + "<br>" );
    out.println( "LastName = " + LastName + "<br>" );
    out.println( "UserID = " + UserID + "<br>" );
    out.println( "Email = " + Email + "<br>" );

  }

	private static boolean followReferral(Object referralInfo)
            throws IOException {
    byte[] reply = new byte[4];
    System.in.read(reply);
    return (reply[0] != 'n');
  }

}
