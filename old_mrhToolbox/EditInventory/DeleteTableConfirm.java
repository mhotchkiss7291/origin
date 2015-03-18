import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class DeleteTableConfirm extends HttpServlet {

	public void doPost (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {
		doGet( req, res );
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {

		res.setContentType ("text/html");
		PrintWriter out = new java.io.PrintWriter(res.getOutputStream());

		HeadNTail ht = new HeadNTail();

		if( !valuesOkay( req, out ) ) {
			ht.putHead( out );
			out.println("You failed to select a Table; try again.");
			ht.putTail( out );
			return;
		}

		out.println(
			"<FORM METHOD=\"POST\" NAME=\"deletetable\" ACTION=\"/servlet/DeleteTableEnable\">");

		out.println("<center>");
		out.println("<br><br>");

		//Ask for deletion confirmation
		out.println(
			"<b>Are you sure you want to delete this table?<br>" +
			"All of the data contained in it will be destroyed!</b>");
		out.println("<p>");
		out.println(
			"<INPUT type=submit value=\"YES\" name=deletetableconfirm><br>");
		out.println("<a href=\"./EditForm\"> NO </a>"); 
		out.flush();
	}

	public boolean valuesOkay( HttpServletRequest req, PrintWriter out ) {

		HttpSession session = req.getSession( true );
    String GroupID = (String)session.getAttribute("GroupID");

		if( GroupID == null ) {
			return false;
		}

		String Table = req.getParameter("table");
		session.setAttribute("Table", Table);

		if( Table.equals("-Select-") ) {
			return false;
		}

		return true;
	}
}
