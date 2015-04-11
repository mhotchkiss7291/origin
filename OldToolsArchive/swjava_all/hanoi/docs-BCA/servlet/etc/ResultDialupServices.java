import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ResultDialupServices extends HttpServlet {

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
		String Dialup = null;

    UserID = req.getParameter("userid");
    Dialup = req.getParameter("dialup");

		GSHeadNTail ht = new GSHeadNTail();
		ht.putHead( out );

    Vector requestVector = new Vector();
    Vector replyVector = new Vector();

		GSServiceEdit sse = null;

		sse = new GSServiceEdit();
		sse.getServices( UserID );

		if( !Dialup.equals("-Select-") ) {
			sse.setDialup( Dialup );
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
		out.println("<h1>Results: Dialup Services</h1>");
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
					out.println("<th>DIALUP</th>");
					out.println("<td>" + sse.getDialup() + "</td>");
				out.println("</tr>");

			out.println("</table>");

			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/EditDialupServices\">Return to Edit Dialup Services</A></FONT>");
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/BusinessServices\">Return to Business Services</A></FONT>");

			out.println("</center>");

			ht.putTail( out );

		} else {

			out.println( "The user's ID is not found in the database." );
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/EditDialupServices\">Return to Edit Dialup Services</A></FONT>");
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/BusinessServices\">Return to Business Services</A></FONT>");
			out.println("</center>");
			ht.putTail( out );
			return;
		}
	}
}
