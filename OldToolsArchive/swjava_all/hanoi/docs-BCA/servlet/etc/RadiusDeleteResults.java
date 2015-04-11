import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class RadiusDeleteResults extends HttpServlet {

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
			ht.putTail( out ) ;
			return;
		}

		radiuslist.remove( foundindex ); 

    File f = new File( 
			"/opt/netscape/server4/docs-BCA/servlet/etc/newclients" );
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


		File f2 = new File(
			"/opt/netscape/server4/docs-BCA/servlet/etc/clients" );

		f.renameTo( f2 );


		PushFiles pf = new PushFiles();
		if ( !pf.push( radiuslist ) ) {
 			out.println("<center>");
			out.println("Transmission of the radius file to the other ");
			out.println("servers failed.");
 			out.println(
      	"<a href=\"/BCA/DeleteRadiusServer\">Return to Delete Radius Server</a href>");
 			out.println("<p>");
 			out.println(
      	"<a href=\"/BCA/BusinessServices\">Return to Business Services</a href>");
 			out.println("</center>");
			ht.putTail( out );
			return;
		}
			
 		out.println("<center>");
		out.println("The entry for " + IPAddress + " has been deleted ");
		out.println("and the file has been pushed to the servers.");

 		out.println("<p>");
 		out.println(
      "<a href=\"/BCA/DeleteRadiusServer\">Return to Delete Radius Server</a href>");
 		out.println("<p>");
 		out.println(
      "<a href=\"/BCA/BusinessServices\">Return to Business Services</a href>");
 		out.println("</center>");

		ht.putTail( out );

  }
}
