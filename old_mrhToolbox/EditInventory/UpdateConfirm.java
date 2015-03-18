import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class UpdateConfirm extends HttpServlet {

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

		//Get all parameter names from last servlet
		Param_names = req.getParameterNames();   

		out.println(
			"<FORM METHOD=\"POST\" NAME=\"deleteform\" ACTION=\"/servlet/UpdateEnable\">");

		//Pass all parameters to next servlet using hidden input type
		while(Param_names.hasMoreElements()) { 
			Param_name = Param_names.nextElement().toString();
			out.println(
				"<input type=hidden name=" + Param_name + 
				" value=" + req.getParameter(Param_name) + ">");
		}

		//Ask for Edit confirmation
		out.println("<center>");
		out.println("<br><br>");
		out.println("<b>Are you sure you want to apply changes to the entry?<br>");
		out.println("<INPUT type=submit value=\"YES\" ><br>");
		out.println("<a href=\"../EditForm\" onclick=window.close();> NO </a>"); 

		out.flush();

	}
}
