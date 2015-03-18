import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.io.*;
import java.sql.*;

public class CreateTable extends HttpServlet {

	public void doPost (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {
		doGet( req, res );
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {

		String Query = null;
		ResultSet result = null;

		res.setContentType ("text/html");
		PrintWriter out = new java.io.PrintWriter(res.getOutputStream());

		String TableName = req.getParameter("tablename");

		HeadNTail ht = new HeadNTail();
		
    if( TableName.equals( "" ) ) {
			ht.putHead( out );
      out.println("You failed to enter a Table name");
      out.println("<p><a href=\"./EditForm \">Return</a>");
			ht.putTail( out );
      return;
    }

    char a[] = TableName.toCharArray();

    int i = 0;

    while( i < TableName.length() ) {

      if( !(Character.isLetterOrDigit( a[ i ] )) ) {
				ht.putHead( out );
        out.println("Your Table name can only contain letters or numbers.<br>");
        out.println("Please remove spaces or special characters.");
        out.println("<p><a href=\"./EditForm\">Continue</a>");
				ht.putTail( out );
        return;
      }

      i++;
    }

		//Formulate query based on results from html page
		Query = MakeQuery(req);	

		//Get results from database and output them
		if(Query != null)	{ 
			result = ProcessQuery(Query, out, req);
			OutputResults(result, out, req );
		} else {
			out.println("<title>Nothing to Display</title>Nothing to Display");
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

	public String MakeQuery(HttpServletRequest req) {

		String TableName = req.getParameter("tablename");

		String Query = null;

		Query = (
			"CREATE TABLE " + 
			TableName +
			"(serial_num char(20) NOT NULL default '', " +
			"hostname char(40) default NULL, " +
			"ip char(15) NOT NULL default '', " +
			"vendor char(20) default NULL, " +
			"model char(20) default NULL, " +
			"description char(40) default NULL, " +
			"site_location char(15) default NULL, " +
			"room_location char(15) default NULL, " +
			"asset_number char(20) default NULL, " +
			"tag_number char(20) default NULL, " +
			"project char(20) default NULL, " +
			"owner char(30) default NULL, " +
			"PRIMARY KEY  (serial_num) " +
			") TYPE=MyISAM;");

		return Query;

	}

	public void OutputResults(
			ResultSet result, 
			PrintWriter out,
			HttpServletRequest req)
				throws ServletException, java.io.IOException {

		HttpSession session = req.getSession( true );
    String GroupID = (String)session.getAttribute("GroupID");

		HeadNTail ht = new HeadNTail();

		ht.putHead( out );
		out.println("<h3>Database: <b>" + GroupID + "</b></h3>");
		out.println("<p>");
		out.println("<b>Table successfully created.</b>");
		out.println("<p>");
		out.println("<a href= \"./EditForm\"> Return </a>");
		out.println("<p>");
		ht.putTail( out );
	}
}	
