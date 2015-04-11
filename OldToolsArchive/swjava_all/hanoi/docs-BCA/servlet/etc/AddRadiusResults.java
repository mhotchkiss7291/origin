import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AddRadiusResults extends HttpServlet {

  public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		doGet( req, res );

	}

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

		GSHeadNTail ht = new GSHeadNTail();
    ht.putHead( out );

		// Read the existing file
		FileToArrayList fta = new FileToArrayList();
		ArrayList radiuslist = fta.getArrayList( 
			"/opt/netscape/server4/docs-BCA/servlet/etc/clients" );

		String IPAddress = req.getParameter("ipaddress");
		String Comment = req.getParameter("comment");
		String Password = req.getParameter("password");
		String ConfirmPassword = req.getParameter("confirmpassword");

		// Check for password consistency
		if( !Password.equals( ConfirmPassword )) {
			out.println("The passwords you entered do not match. Please try again.");
			ht.putTail( out );
			return;
		}

		// Add the new entry to the local file
		String newentry = IPAddress + "   " + 
											Password + "   # " + 
											Comment + "\n"; 

		// Check to see if the entry already exists
		int i = 0;
		while( i < radiuslist.size() ) {
			if( ((String)radiuslist.get( i )).indexOf( IPAddress ) >= 0 ) {
				out.println("This server already exists. To make changes, delete ");
				out.println("it from the list and then re-enter it with new ");
				out.println("information.");
				ht.putTail( out );
				return;
			}
			i++;
		}
		
		radiuslist.add( newentry );

		//CHECK
    File f = new File( 
			"/opt/netscape/server4/docs-BCA/servlet/etc/newclients" );
    File f2 = new File( 
			"/opt/netscape/server4/docs-BCA/servlet/etc/clients" );
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream( f );
    } catch (IOException e) {
      e.printStackTrace();
    }
		i = 0;
    while( i < radiuslist.size() ) {
      byte[] buffer = ((String)radiuslist.get( i )).getBytes();
      try {
        fos.write( buffer );
        i++;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
		f.renameTo( f2 );

		PushFiles pf = new PushFiles();
		if ( !pf.push( radiuslist ) ) {
			out.println("Transmission of the radius file to the other ");
			out.println("servers failed.");
 			out.println("<center>");
			out.println("<p>");
			out.println(
				"<a href=\"/BCA/AddRadiusServer\">Return to Add Radius Server</a href>");
			out.println("<p>");
			out.println(
				"<a href=\"/BCA/BusinessServices\">Return to Business Services</a href>");
 			out.println("</center>");
			ht.putTail( out );
			return;
		}

 		out.println("<center>");

		out.println("The entry for " + IPAddress + 
			" has been added and pushed to the servers.");

 		out.println("<p>");
 		out.println(
      "<a href=\"/BCA/AddRadiusServer\">Return to Add Radius Server</a href>");
 		out.println("<p>");
 		out.println(
      "<a href=\"/BCA/BusinessServices\">Return to Business Services</a href>");
 		out.println("</center>");

		ht.putTail( out );

  }
}
