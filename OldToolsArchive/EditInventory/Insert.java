import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.io.*;
import java.sql.*;

public class Insert extends HttpServlet {

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

		String Insert = null;

		//Formulate insert string based on results from html page
		Insert = MakeInsert (req);	

		ProcessInsert (Insert, out, req);
		out.flush();
	}

	public boolean valuesOkay( HttpServletRequest req, PrintWriter out ) {

		HttpSession session = req.getSession( true );
    String GroupID = (String)session.getAttribute("GroupID");

		if( GroupID == null ) {
			return false;
		}

		String Table = req.getParameter("table");

		if( Table.equals("-Select-") ) {
			return false;
		}

		return true;
	}

	public void ProcessInsert (
			String Insert, 
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

			//Execute Insert
			stmt.executeUpdate(Insert);

			out.println("<h1>Insert Successful</h1>");
			out.println("<p>");
			out.println(
				"<a href= \"./InsertForm\"> Return </a>");

		}catch (SQLException e) {
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

	public String MakeInsert (HttpServletRequest req) {

		String Insert = null;

		//get variable data from webpage
		String owner = req.getParameter("owner");
		String model = req.getParameter("model");
		String vendor = req.getParameter("vendor");
		String serial_num = req.getParameter("serial_num");
		String project = req.getParameter("project");
		String description = req.getParameter("description");
		String hostname = req.getParameter("hostname");
		String ip = req.getParameter("ip");
		String site_location = req.getParameter("site_location");
		String room_location = req.getParameter("room_location");
		String asset_number = req.getParameter("asset_number");
		String tag_number = req.getParameter("tag_number");

		String Table = req.getParameter("table");

		//Create Insert string
		Insert= 
			("INSERT INTO " + 
			Table + 
			" (owner, model, vendor, serial_num, project, description, hostname, ip, site_location, room_location, asset_number, tag_number) VALUES ('" + owner +"', '" + model + "', '" + vendor +"', '" + serial_num +"', '" + project + "', '" + description + "', '" + hostname + "', '" + ip + "', '" + site_location + "', '" + room_location + "', '" + asset_number + "', '" + tag_number + "')" );

		return Insert ;
	} 
}	
