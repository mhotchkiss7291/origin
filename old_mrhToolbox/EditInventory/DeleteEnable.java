import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class DeleteEnable extends HttpServlet {

	public void doPost (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {
		doGet( req, res );
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {

		Enumeration Param_names_enu;
		String Current_name; 
		Vector Param_names = new Vector();
		Vector Param_values = new Vector();
		String[] Delete_String;

		res.setContentType ("text/html");
		PrintWriter out = new java.io.PrintWriter(res.getOutputStream());

		//Get names of all Parameters from last servlet
		Param_names_enu = req.getParameterNames();   

		//Put Parameter Names and values in vectors
		while(Param_names_enu.hasMoreElements()) { 
			Current_name= Param_names_enu.nextElement().toString();
			Param_values.addElement(req.getParameter(Current_name));
			Param_names.addElement(Current_name);
		}

		//Create correct number of Deletion strings
		Delete_String = new String[Param_values.size()];

		HttpSession session = req.getSession( true );
		String Table = (String)session.getAttribute("Table");

		//Fill up Delete String array with Delete 
		//Strings except for last two garbage parameters
		for(int i = 0; i < Param_values.size() - 2; i++) {
			Delete_String[i] = 
				"Delete from " + Table + " where serial_num='" + 
				Param_names.elementAt(i) + "'"; 
		}

		//Call function to delete entries from database
		ProcessDelete( 
			Delete_String, 
			Param_values.size() - 2, 
			out,
			req );

		out.flush();
	}

	public void ProcessDelete (
			String[] Delete, 
			int size, 
			PrintWriter out,
			HttpServletRequest req ) {

		HttpSession session = req.getSession( true );
    String GroupID = (String)session.getAttribute("GroupID");

		Connection mySQLcon = null;

		try {

			try {
				Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				out.println("<h1>" + e.getMessage() + "</h1>");
			}

			//create the "URL"
			String url = "jdbc:mysql://127.0.0.1:3306/" + GroupID;

			//Use the DriverManager to get a Connection
			mySQLcon = DriverManager.getConnection (url);

			//Use the connection to create a Statement Object
			Statement stmt = mySQLcon.createStatement ();

			//Run every delete statement
			for(int i = 0; i < size; i++) {
				stmt.executeUpdate(Delete[i]);
			}

			out.println("<h1>Delete Successful</h1>");
			out.println("<p>");
			out.println(
				"<a href= \"./DeleteForm\"> Return </a>");

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("<h1>" + e.getMessage() + "</h1>");
		}

		//close the connection
		finally {
			if( mySQLcon != null ) {
				try {
					mySQLcon.close();
				}catch( Exception e ){
				}
			}
		}
	}
}
