import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;


public class RCConfirmEnable extends HttpServlet {

	private boolean DEBUG = true;

  public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		doGet( req, res );

	}

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		PrintWriter out = null;

		String RequestType = null;
		String SunID = null;
		String UserID = null;
		String CardID = null;
		String PIN = null;

		Vector requestVector = null;
		Vector replyVector = null;

    res.setContentType("text/html");
    out = res.getWriter();

		GSHeadNTail ht = new GSHeadNTail();
		ht.putHead( out );
		out.println("<center>");

		SunID = req.getParameter("sunid");
		CardID = req.getParameter("cardid");

		String FirstName = null;
		String LastName = null;

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
						out.println("<h1>Error</h2>");
						out.println("Please make sure you entered the ");
            out.println("user's employee #, not his/her user id.");
						ht.putTail( out );
						return;
					}

					if ( !results.hasMore() ) {
						out.println("<h1>Error</h2>");
						out.println("Please make sure you entered the ");
            out.println("user's employee #, not his/her user id.");
						ht.putTail( out );
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
							}
						}
					}
					break;

				// not sure what this is for, but left it in
				} catch (ReferralException e) {
					e.printStackTrace();

					if (! followReferral(e.getReferralInfo())) {
						if (! e.skipReferral()) {
							out.println( "skipReferral" );
							return;
						}
					}
					ctx = (DirContext) e.getReferralContext();
				}
			}

		// If LDAP server is down or not working....
		} catch (NamingException e) {
			e.printStackTrace();
			out.println("<center>");
			out.println("This employee's information is not available from LDAP.");
			out.println("<p>");
			out.println("Therefore the token card can not be enabled.");
			out.println(" Contact ServiceDesk for backline support.");
			out.println("</center>");
			ht.putTail( out );
			return;
		}

		// Construct a Safeword UserID if needed
		StringBuffer buf = new StringBuffer();
		buf.append(FirstName.charAt(0));
		buf.append(LastName.charAt(0));
		buf.append(SunID);
		String safeID = new String( buf );
		UserID = safeID.toLowerCase();

		out.println("<BODY BGCOLOR=\"#FFFFFF\" ONLOAD=\"setFieldFocus()\">");
		out.println("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html\">");
		out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
		out.println("<!--");
		out.println("function setFieldFocus()");
		out.println("{");
		out.println("document.userid_form.userid.focus();");
		out.println("}");
		out.println("//-->");
		out.println("</SCRIPT>");

		out.println("<center>");
		out.println(
			"<FORM ACTION=\"/BCA/RCEnableResult\" METHOD=\"POST\" NAME=\"userid_form\">");
		out.println(
			"Enter the employee's token card a second time for confirmation <INPUT TYPE=TEXT NAME=\"confirmcardid\">");
		out.println("<P>");
//			out.println("<INPUT TYPE=HIDDEN NAME=\"adminuserid\" VALUE=\"" + AdminUserID + "\">");
		out.println("<INPUT TYPE=HIDDEN NAME=\"userid\" VALUE=\"" + UserID + "\">");
		out.println("<INPUT TYPE=HIDDEN NAME=\"firstname\" VALUE=\"" + FirstName + "\">");
		out.println("<INPUT TYPE=HIDDEN NAME=\"lastname\" VALUE=\"" + LastName + "\">");
		out.println("<INPUT TYPE=HIDDEN NAME=\"cardid\" VALUE=\"" + CardID + "\">");
		out.println("<P>");

		out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"goto\" SIZE=\"-1\" VALUE=\"\">");
		out.println("<INPUT TYPE=SUBMIT VALUE=\"Submit\">");
		out.println("</FORM>");
		out.println("<p>");
		out.println("<a href=\"/BCA/GeneralServices\">Return to General Services.</a href>");

		out.println("</center>");
		ht.putTail( out );

  }


	private static boolean followReferral(Object referralInfo)
            throws IOException {
    byte[] reply = new byte[4];
    System.in.read(reply);
    return (reply[0] != 'n');
  }

}
