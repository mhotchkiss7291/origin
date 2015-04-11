import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class UpdateEnter extends HttpServlet {

	public void doPost (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {
		doGet( req, res );
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {

		String Serial_Number;
		String query;
		ResultSetMetaData rsmd;
		int numberColumns;
		int i;

		ResultSet results;
		Query q = new Query();

		res.setContentType ("text/html");
		PrintWriter out = new java.io.PrintWriter(res.getOutputStream());
    HttpSession session = req.getSession( true );
		String Table = (String)session.getAttribute("Table");

		Serial_Number = req.getParameter("submit");   

		//Start Query string
		query = "SELECT * FROM " + Table + " WHERE serial_num ='" + Serial_Number + "'"; 

		//Process Query string and get results from database
		results = ProcessQuery( query, out, req); 

		try {

			rsmd = results.getMetaData();
			numberColumns = rsmd.getColumnCount();

			out.println("<FORM METHOD=\"POST\" NAME=\"editform\" ACTION=\"/servlet/UpdateConfirm\" >");

			//Do not let primary key be edited
			out.println("<INPUT type=hidden name = serial_num value =\"" + Serial_Number + "\" >");

			//form table
			out.println("<table border=2 cellspacing=4><tr>");

			//insert column labels into table
			for(i = 1; i <= numberColumns/2; i++) {
				out.println("<th>" + rsmd.getColumnName(i) + "</th>");
			}

			out.println("</tr>");

			//insert data rows into table as input fields 
			//with current information inside
			results.next();

			out.println("<tr>");

			for(i = 1; i <= numberColumns/2; i++) {

				if(i == 1) {
					out.println("<td>" + results.getString(i) + "</td>");
				} else if(results.getString(i) != null)	{
					if(results.getString(i).length() > 0) {
						out.println(
							"<td><INPUT type=text name =" + 
							rsmd.getColumnName(i) + 
							" value =\"" + 
							results.getString(i) + "\" ></td>");
					} else {
						out.println(
							"<td> <INPUT type=text name =" + 
							rsmd.getColumnName(i) + " > </td>");  
					}
				} else {
					out.println(
						"<td> <INPUT type=text name =" + 
						rsmd.getColumnName(i) + " > </td>"); 	
				}
			}

			out.println("</tr>");

			//insert column labels into table
			for(i = numberColumns/2 + 1; i <= numberColumns; i++) {
				out.println("<th>" + rsmd.getColumnName(i) + "</th>");
			}

			out.println("</tr>");

			//insert data rows into table as input 
			//fields with current information inside
			out.println("<tr>");

			for(i = numberColumns/2 + 1; i <= numberColumns; i++) {

				if(i == 1) {
					out.println("<td>" + results.getString(i) + "</td>");
				} else if(results.getString(i) != null) {
					if(results.getString(i).length() > 0) {
						out.println(
							"<td><INPUT type=text name =" + 
							rsmd.getColumnName(i) + 
							" value =\"" + 
							results.getString(i) + "\" ></td>");
					} else {
						out.println(
							"<td> <INPUT type=text name =" + 
							rsmd.getColumnName(i) + 
							" > </td>");
					}

				} else {
					out.println(
						"<td> <INPUT type=text name =" + 
						rsmd.getColumnName(i) + 
						" > </td>");
				}
			}

			out.println("</tr>");
			out.println("</table>");
			out.println("<center><Input type=Submit value=EDIT>");
			out.println("</form>");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		out.flush();
	}

	public ResultSet ProcessQuery(
		String Query, 
		PrintWriter out,
		HttpServletRequest req) {

		HttpSession session = req.getSession( true );
    String GroupID = (String)session.getAttribute("GroupID");

		ResultSet rs = null;
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

			//Execute Query
			rs = stmt.executeQuery(Query);

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
		return rs;
	}
}
