import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.io.*;
import java.sql.*;

public class DeleteTableEnable extends HttpServlet {

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

		//Formulate query based on results from html page
		Query = MakeQuery(req);	

		//Get results from database and output them
		if(Query != null)	{ 
			result = ProcessQuery(Query, out, req);
			OutputResults(result, out);
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

		HttpSession session = req.getSession( true );
    String Table = (String)session.getAttribute("Table");

		String Query = null;
		Query = ( "DROP TABLE " + Table);
		return Query;

	}

	public void OutputResults(ResultSet result, PrintWriter out)
			throws ServletException, java.io.IOException {

		HeadNTail ht = new HeadNTail();
	
		ht.putHead( out );
		out.println("Table deletion successful.");
		out.println("</center></h1><a href = \"/servlet/EditForm\"> Return </a>");
		ht.putTail( out );

	}
}	
