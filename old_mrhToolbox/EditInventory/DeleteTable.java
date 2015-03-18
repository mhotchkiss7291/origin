import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class DeleteTable extends HttpServlet {

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
		out.println("<b>Select Table to Delete:</b>");
		out.println("<FORM METHOD=POST ACTION=\"/servlet/DeleteTableConfirm\">");
 		out.println("<SELECT NAME=table size=1>");
    out.println("<OPTION> -Select-");
		int i = 0;

		while( i < tables.size() ) {
			out.println("<OPTION>" + tables.get( i ).toString() );
			i++;
		}
		out.println("</SELECT");

		out.println("<P>");
		out.println("<P>");
		out.println("<INPUT type=submit value=\"DELETE\" name=search>");
		out.println("</FORM>");

		ht.putTail( out );

	}
}

