import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class InsertForm extends HttpServlet {

  public void doPost(HttpServletRequest req, HttpServletResponse res)
       throws ServletException, IOException {
    doGet(req, res);
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
       throws ServletException, IOException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
		HttpSession session = req.getSession( true );

		String GroupID = req.getParameter("groupid");

		HeadNTail ht = new HeadNTail();
		ht.putHead( out );

		if( GroupID == null ) {
			GroupID = (String)session.getAttribute("GroupID");
		}

		session.setAttribute("GroupID", GroupID);
			
		out.println("<h3>Database: <b>" + GroupID + "</b></h3>");
		out.println("<p>");

		DBConnection dbc = new DBConnection( GroupID );

	  if( dbc.getConnection()	== null ) {
			out.println("The Group entered does not match an existing database.");
			out.println("<br>Please try again.");
			ht.putTail( out );
			return;
		}

		Vector tables = dbc.showTables();
		dbc.closeConnection();

		out.println("<P>");
		out.println("<h3>Select Table to Insert Rows:</h3>");
		out.println(
			"<FORM METHOD=\"POST\" NAME=\"insertform\" ACTION=\"/servlet/Insert\">");
 		out.println("<SELECT NAME=table size=1>");
    out.println("<OPTION> -Select-");
		int i = 0;

		while( i < tables.size() ) {
			out.println("<OPTION>" + tables.get( i ).toString() );
			i++;
		}
		out.println("</SELECT>");

		out.println("<p>");
		out.println("<p>");

		out.println("<TABLE><TR><TD>Serial Number:</TD>");
		out.println("<TD>Hostname:</TD>");
		out.println("<TD>Ip:</TD>");
		out.println("<TD>Vendor:</TD>");
		out.println("<TD>Model:</TD>");
		out.println("<TD>Site Location:</TD></TR>");

		out.println("<TR>");
		out.println("<TD><INPUT type=text value = \"\" name=serial_num></TD>");
		out.println("<TD><INPUT type=text value = \"\" name=hostname></TD>");
		out.println("<TD><INPUT type=text value = \"\" name=ip></TD>");
		out.println("<TD><INPUT type=text value = \"\" name=vendor></TD>");
		out.println("<TD><INPUT type=text value = \"\" name=model></TD>");
		out.println("<TD><INPUT type=text value = \"\" name=site_location></TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD>Room Location:</TD>");
		out.println("<TD>Asset Number:</TD>");
		out.println("<TD>Description:</TD>");
		out.println("<TD>Tag Number:</TD>");
		out.println("<TD>Project:</TD>");
		out.println("<TD>Owner:</TD>");
		out.println("</TR>");

		out.println("<TR>");
		out.println("<TD><INPUT type=text value = \"\" name=room_location></TD>");
		out.println("<TD><INPUT type=text value = \"\" name=asset_number></TD>");
		out.println("<TD><INPUT type=text value = \"\" name=description></TD>");
		out.println("<TD><INPUT type=text value = \"\" name=tag_number></TD>");
		out.println("<TD><INPUT type=text value = \"\" name=project></TD>");
		out.println("<TD><INPUT type=text value = \"\" name=owner></TD>");
		out.println("</TR>");
		out.println("</TABLE>");

		out.println("<hr>");

		out.println("<INPUT type=submit value=\"INSERT\" name=insert>");

		out.println("</FORM>");

		ht.putTail( out );
	}
}

