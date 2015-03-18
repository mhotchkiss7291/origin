import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class UpdateEnable extends HttpServlet {

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
		String Update;

		res.setContentType ("text/html");
		PrintWriter out = new java.io.PrintWriter(res.getOutputStream());
		HttpSession session = req.getSession( true );
    String Table = (String)session.getAttribute("Table");

		//Get all parameter names from last servlet
		Param_names_enu = req.getParameterNames();   

		//Put parameter names and vaules into vectors
		while(Param_names_enu.hasMoreElements()) { 
			Current_name= Param_names_enu.nextElement().toString();
			Param_values.addElement(req.getParameter(Current_name));
			Param_names.addElement(Current_name);
		}

		//Start Update string
		Update = "Update " + Table + " SET "; 

		//Add parameter names and vaules to Update string
		for(int i = 1; i < Param_values.size(); i++) {

			Update += 
				Param_names.elementAt(i) + "='" + 
				Param_values.elementAt(i) + "'"; 

			if(i < Param_values.size() -1) {
				Update += ", ";
			} else {
				Update += " "; 
			}
		}

		//Add where clause to update string based on primary key
		Update += 
			"WHERE serial_num = '" + Param_values.elementAt(0) + "'";

		//Call function to update database
		ProcessUpdate( Update, out, req);

		//Close new window
		out.println("<FORM><INPUT TYPE= SUBMIT value=OK ONCLICK=window.close()></FORM>");

		out.flush();

	}

	public void ProcessUpdate(
			String Update, 
			PrintWriter out, 
			HttpServletRequest req) {

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
			String url = "jdbc:mysql://127.0.0.1:3306/" + GroupID ;

			//Use the DriverManager to get a Connection
			mySQLcon = DriverManager.getConnection( url );

			//Use the connection to create a Statement Object
			Statement stmt = mySQLcon.createStatement ();

			//Execute update statement
			stmt.executeUpdate(Update);

			out.println("<h1>Update Successful</h1>");

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("<h1>" + e.getMessage() + "</h1>");
		}

		//close the connection
		finally {
			if( mySQLcon != null ) {
				try {
					mySQLcon.close();
				} catch( Exception e ) {
				}
			}
		}
	}
}

