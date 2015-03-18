import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class BReturnUserServices extends HttpServlet {

  public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		doGet( req, res );

	}

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

		res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    String UserID = req.getParameter("userid");

		GSHeadNTail ht = new GSHeadNTail();
		ht.putHead( out );

    Vector requestVector = new Vector();
    Vector replyVector = new Vector();

    String RequestType = new String("GETPRIVILEDGES");
		GSServiceEdit sse = null;

		sse = new GSServiceEdit();
		sse.getServices( UserID );

		if ( (sse.getReplyType()).equals("GETRECORDFAILED") ) {
			out.println( "The user's ID is not found in the database." );
			out.println("</center>");
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/ViewUserServices\">Return to View User Services</A></FONT>");
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/GeneralServices\">Return to General Services</A></FONT>");
			ht.putTail( out );
			return;
		}

		out.println("<center>");

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

			out.println("<P>");

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

				out.println("<tr align=center>");
					out.println("<th>ECCPX</th>");
					out.println("<td>" + sse.getEccpx() + "</td>");
				out.println("</tr>");

				out.println("<tr align=center>");
					out.println("<th>PSC</th>");
					out.println("<td>" + sse.getPsc() + "</td>");
				out.println("</tr>");

				out.println("<tr align=center>");
					out.println("<th>NSG</th>");
					out.println("<td>" + sse.getNsgSun() + "</td>");
				out.println("</tr>");

				out.println("<tr align=center>");
					out.println("<th>TH</th>");
					out.println("<td>" + sse.getTh() + "</td>");
				out.println("</tr>");

				out.println("<tr align=center>");
					out.println("<th>Icabad</th>");
					out.println("<td>" + sse.getIcabad() + "</td>");
				out.println("</tr>");

				out.println("<tr align=center>");
					out.println("<th>Dialup</th>");
					out.println("<td>" + sse.getDialup() + "</td>");
				out.println("</tr>");

				out.println("<tr align=center>");
					out.println("<th>SAFEW</th>");
					out.println("<td>" + sse.getSafew() + "</td>");
				out.println("</tr>");

				out.println("<tr align=center>");
					out.println("<th>ITE</th>");
					out.println("<td>" + sse.getIte() + "</td>");
				out.println("</tr>");

			out.println("</table>");

		} else {

			out.println( "The user's ID is not found in the database." );
			out.println("</center>");
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/ViewUserServices\">Return to View User Services</A></FONT>");
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/GeneralServices\">Return to General Services</A></FONT>");
			ht.putTail( out );
			return;

		}

			out.println("<P>");
			out.println("<P>");
			out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/BViewUserServices\">Return to View User Services</A></FONT>");
			out.println("<P>");
    	out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/BusinessServices\">Return to Business Services</A></FONT>");
			out.println("</center>");
			ht.putTail( out );
  }

}
