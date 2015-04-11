import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class RadiusDeleteConfirm extends HttpServlet {

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

		ArrayList radiuslist = null;

		radiuslist = loadArray( 
			"/opt/netscape/server4/docs-BCA/servlet/etc/clients" );

		String IPAddress = req.getParameter("ipaddress");

		int i = 0;
		int foundindex = -1;


		while( i < radiuslist.size() ) {

			if( ((String)radiuslist.get( i )).indexOf( IPAddress ) != -1) {
				foundindex = i;
			}
			i++;
		}

		if( foundindex == -1 ) {

			out.println("The IP address you entered was not found. Try again.");
			out.println("<p>");
			out.println(
				"<a href=\"/BCA/DeleteRadiusServer\"> Return to Delete Radius Server");
			ht.putTail( out ) ;
			return;
		}

		int endindex = 0;
		
		String targetIP = (String)radiuslist.get( foundindex );

		endindex = targetIP.indexOf( ' ', 0);

		targetIP = targetIP.substring( 0, endindex );

		out.println("<center>");
		out.println("Confirm that the following entry is one you want to delete:");
		out.println("<p>");
		out.println( targetIP );
		out.println("<p>");

    out.println(
			"<FORM ACTION=\"/BCA/RadiusDeleteResults\" METHOD=\"POST\" NAME=\"userid_form\">");
    out.println( 
			"<INPUT TYPE=HIDDEN NAME=\"ipaddress\" VALUE=\"" + targetIP + "\">");
    out.println("<P>");
    out.println("<INPUT TYPE=SUBMIT VALUE=\"Confirm\">");
    out.println("</FORM>");

		out.println("<p>");

		out.println(
			"<a href=\"/BCA/DeleteRadiusServer\"> Reject");
		out.println("<p>");
		out.println(
			"<a href=\"/BCA/BusinessServices\"> Return to Business Services");
		out.println("</center>");

		ht.putTail( out );

  }

	public ArrayList loadArray( String FileName ) {

    File fi = null;
    FileReader in = null;
    String dstring = null;
    try {
      fi = new File( FileName );
      in = new FileReader(fi);
      int size = (int)fi.length();
      char[] data = new char[size];
      int chars_read = 0;

      // Read in the record
      while(chars_read < size) {
        chars_read += in.read(data, chars_read, size - chars_read);
      }
      dstring = new String( data );
    } catch (IOException e ) {
      System.out.println("Could not open input file");
      System.exit(0);
    }

    StringTokenizer sst =
          new StringTokenizer( dstring, "\n", false);

		ArrayList al = new ArrayList();

    while( sst.hasMoreTokens() ) {
      al.add( sst.nextToken() + "\n" );
    }

    return al;

  }


}
