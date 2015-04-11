import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ReturnRemoteAccess extends HttpServlet {

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

		Vector requestVector = new Vector();
		Vector replyVector = new Vector();

		GSHeadNTail ht = new GSHeadNTail();
    ht.putHead( out );

    out.println("<center>");
    out.println("<h1>");
    out.println("Return Remote Access");
    out.println("</h1>");

    UserID = req.getParameter("userid");

		GSServiceEdit sse = new GSServiceEdit();
		sse.getServices( UserID );

		// Update the GUI buttons
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

			out.println("<p>");

			out.println("<table border cellspacing=0 cellpadding=5>");
				out.println("<tr>");
					out.println("<th>Service</th>");
					out.println("<th>Status</th>");
				out.println("</tr>");

				out.println("<tr align=center>");
					out.println("<th>Modem Pool</th>");
					out.println("<td>" + sse.getModemPool() + "</td>");
				out.println("</tr>");

				out.println("<tr align=center>");
					out.println("<th>High Speed</th>");
					out.println("<td>" + sse.getHighSpeed() + "</td>");
				out.println("</tr>");

			out.println("</table>");

		} else {

			out.println( "The user's ID is not found in the database." );
			out.println("<p>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/EditRemoteAccess\">Return to Edit Remote Access</A></FONT>");
			out.println("<p>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/BusinessServices\">Return to Business Services</A></FONT>");
    	out.println("</center>");
			ht.putTail( out );
			return;

		}

		out.println("<P>");
		out.println("Change services for: <b>" + UserID + "</b>" );

		out.println("<FORM METHOD=POST ACTION=\"/BCA/ResultRemoteAccess\">");

		out.println("<P>");
		out.println("<P>");
		out.println("<b>Modem Pool</b> ");
		out.println("<SELECT NAME=modempool size=1>");
		out.println("<OPTION> -Select-");
		out.println("<OPTION> No");
		out.println("<OPTION> Yes");
		out.println("</SELECT>");

		out.println("<P>");
		out.println("<P>");
		out.println("<b>High Speed</b> ");
		out.println("<SELECT NAME=highspeed size=1>");
		out.println("<OPTION> -Select-");
		out.println("<OPTION> No");
		out.println("<OPTION> Yes");
		out.println("</SELECT>");
		out.println("<P>");

		out.println(
			"<INPUT TYPE=HIDDEN NAME=\"userid\" VALUE=\"" + UserID + "\">");

		out.println("<INPUT TYPE=SUBMIT VALUE=\"Submit Change\">");
		out.println("</FORM>");

		out.println("<p>");
		out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/EditRemoteAccess\">Return to Edit Remote Access</A></FONT>");
		out.println("<P>");
		out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/BusinessServices\">Return to Business Services</A></FONT>");
    out.println("</center>");

		ht.putTail( out );

  }

}
