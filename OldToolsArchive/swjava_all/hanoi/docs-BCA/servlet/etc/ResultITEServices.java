import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ResultITEServices extends HttpServlet {

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
		String Ite = null;

    UserID = req.getParameter("userid");
    Ite = req.getParameter("ite");

		GSHeadNTail ht = new GSHeadNTail();
		ht.putHead( out );

    Vector requestVector = new Vector();
    Vector replyVector = new Vector();

		GSServiceEdit sse = null;

		sse = new GSServiceEdit();
		sse.getServices( UserID );

		if( !Ite.equals("-Select-") ) {
			sse.setIte( Ite );
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
		out.println("<h1>Results: ITE Services</h1>");
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
					out.println("<th>ITE</th>");
					out.println("<td>" + sse.getIte() + "</td>");
				out.println("</tr>");

			out.println("</table>");

			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/EditITEServices\">Return to Edit ITE Services</A></FONT>");
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/BusinessServices\">Return to Business Services</A></FONT>");

			out.println("</center>");

			ht.putTail( out );

		} else {

			out.println( "The user's ID is not found in the database." );
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/EditITEServices\">Return to Edit ITE Services</A></FONT>");
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/BusinessServices\">Return to Business Services</A></FONT>");
			out.println("</center>");
			ht.putTail( out );
			return;
		}
	}
}
