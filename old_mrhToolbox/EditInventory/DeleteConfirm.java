import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class DeleteConfirm extends HttpServlet {

	public void doPost (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {
		doGet( req, res );
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {

		Enumeration Param_names;
		String Param_name;
		Vector Param_values = new Vector();
		res.setContentType ("text/html");
		PrintWriter out = new java.io.PrintWriter(res.getOutputStream());

		//Get all Parameters from previous servlet
		Param_names = req.getParameterNames();   
		out.println(
			"<FORM METHOD=\"POST\" NAME=\"deleteform\" ACTION=\"/servlet/DeleteEnable\">");

		//Pass all Parameters to next servlet in hidden fields
		while(Param_names.hasMoreElements()) { 
			Param_name = Param_names.nextElement().toString();
			out.println(
				"<input type=hidden name=" + Param_name + " value=" + 
				req.getParameter(Param_name) + ">");
		}

		out.println("<center>");
		out.println("<br><br>");

		//Ask for deletion confirmation
		out.println(
			"<b>Are you sure you want to remove all checked entries <br> from the database?<br>");
		out.println("<INPUT type=submit value=\"YES\" name=delete_confim><br>");
		out.println("<a href=\"../EditForm\"> NO </a>"); 
		out.flush();
	}
}
