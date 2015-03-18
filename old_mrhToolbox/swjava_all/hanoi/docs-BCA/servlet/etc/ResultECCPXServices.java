import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ResultECCPXServices extends HttpServlet {

  public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		doGet( req, res );

	}

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		res.setContentType("text/html");
    PrintWriter out = res.getWriter();
		String UserID = null;
		String RequestType = null;
		String Eccpx = null;

    UserID = req.getParameter("userid");
    Eccpx = req.getParameter("eccpx");

		GSHeadNTail ht = new GSHeadNTail();
		ht.putHead( out );

    Vector requestVector = new Vector();
    Vector replyVector = new Vector();

		GSServiceEdit sse = null;

		sse = new GSServiceEdit();
		sse.getServices( UserID );

		if( !Eccpx.equals("-Select-") ) {
			sse.setEccpx( Eccpx );
		}

		sse.setUserID( UserID );

		if( !sse.setServices() ) {
			out.println("Transaction failed.");
			out.println("<b>");
			out.println("The server may be down.");
			ht.putTail( out );
			return;
		}

		out.println("<center>");
		out.println("<h1>Results: ECCPX Services</h1>");
		out.println("<p>");

		sse = new GSServiceEdit();
		sse.getServices( UserID );

		if ( (sse.getReplyType()).equals("SERVICESRETURNED") ) {

			out.println("<table border cellspacing=0 cellpadding=5>");
				out.println("<tr align=center>");
					out.println("<th>Name</th>");
					out.println("<td>" + sse.getFullName() + "</td>");
				out.println("</tr>");
				out.println("<tr align=center>");
					out.println("<th>Comments</th>");
					out.println("<td>" + sse.getComment() + "</td>");
				out.println("</tr>");
			out.println("</table>");

			out.println("<P>");

			out.println("<table border cellspacing=0 cellpadding=5>");
				out.println("<tr>");
					out.println("<th>Service</th>");
					out.println("<th>Status</th>");
				out.println("</tr>");

				out.println("<tr align=center>");
					out.println("<th>ECCPX</th>");
					out.println("<td>" + sse.getEccpx() + "</td>");
				out.println("</tr>");

			out.println("</table>");

			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/EditECCPXServices\">Return to Edit ECCPX Services</A></FONT>");
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/BusinessServices\">Return to Business Services</A></FONT>");

			out.println("</center>");

			ht.putTail( out );

		} else {

			out.println( "The user's ID is not found in the database." );
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/EditECCPXServices\">Return to Edit ECCPX Services</A></FONT>");
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/BusinessServices\">Return to Business Services</A></FONT>");
			out.println("</center>");
			ht.putTail( out );
			return;
		}
	}
}
