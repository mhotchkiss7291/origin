import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.io.*;
import java.sql.*;

public class Delete extends HttpServlet {

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

		HeadNTail ht = new HeadNTail();

		if( !valuesOkay( req, out ) ) {
			ht.putHead( out );
			out.println("You failed to select a Table; try again.");
			ht.putTail( out );
			return;
		}

		//Formulate query based on results from html page
		Query = MakeQuery(req);	

		//Get results from database and output them
		if( Query != null )	{ 
			result = ProcessQuery(Query, out, req);
			OutputResults(result, out, Query);
		} else {
			out.println(
				"<html><body><title>Nothing to Display</title>Nothing to Display</body></html>");
		}
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
	public void OutputResults(ResultSet result, PrintWriter out, String Query)
			throws ServletException, java.io.IOException {

		ResultSetMetaData rsmd = null;

		int numberColumns;

		//output title
		out.println("<head><SCRIPT LANGUAGE=\"JavaScript\">");
		out.println("function check_All()");
		out.println("{");
		out.println("var i;");
		out.println("for (i = 0; i < window.document.forms[0].elements.length; ++i)");
		out.println("{");
		out.println("if (window.document.forms[0].elements[i].type == \"checkbox\")");
		out.println("{");
		out.println("window.document.forms[0].elements[i].checked=true;");
		out.println("}");
		out.println("}");
		out.println("}");
		out.println("</script></head>");
		out.println("Check the boxes of which entries you would like to delete.");
		out.println(
			"<FORM METHOD=\"POST\" NAME=\"queryform\" ACTION=\"/servlet/DeleteConfirm\">"); 

		try {

			rsmd = result.getMetaData();
			numberColumns = rsmd.getColumnCount();

			//form table
			out.println("<table border=2 cellspacing=4><tr>");

			//insert blank column header for delete column
			out.println("<th> </th>");

			//insert column labels into table
			for(int i = 1; i <= numberColumns; i++) {
				out.println( rsmd.getColumnName(i) );
			}

			out.println("</tr>");

			//insert data rows into table
			while (result.next()) {

				if(result.getString(1).length() > 0) {      
					out.println("<tr>"); 
					out.println("<td>"); 
					out.println(
						"Delete <INPUT type=\"checkbox\" name=" + 
						result.getString(1) + ">");
					out.println("</td>"); 

					for(int i = 1; i <= numberColumns; i++) {
						out.println(
							"<td>" + 
							result.getString(i) + 
							"</td>");
					}
					out.println("</tr>");
				}	
			}
			out.println("</table>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.println("<br>"); 

		out.println("<INPUT type=button name=checkall onclick=\"check_All();\" value=\"Select All\"><br>"); 
		out.println("<INPUT type=submit value=\"DELETE\" name=Delete>");
		out.println("</form>"); 
		out.println("</center></h1>");
		out.flush();
	}

	public String MakeQuery(HttpServletRequest req) {

		boolean add_comma = true;
		String columns[] = new String[12];
		int index = 0;
		String Query = null;
		String NullString = null;

		//get variable data from webpage
		String owner = req.getParameter("owner");
		String model = req.getParameter("model");
		String vendor = req.getParameter("vendor");
		String serialnumber = req.getParameter("serialnumber");
		String project = req.getParameter("project");
		String description = req.getParameter("description");
		String hostname_x = req.getParameter("hostname_x");
		String ip_x = req.getParameter("ip_x");
		String vendor_x = req.getParameter("vendor_x");
		String model_x = req.getParameter("model_x");
		String sitelocation_x = req.getParameter("sitelocation_x");
		String roomlocation_x = req.getParameter("roomlocation_x");
		String assetnumber_x = req.getParameter("assetnumber_x");
		String description_x = req.getParameter("description_x");
		String tagnumber_x = req.getParameter("tagnumber_x");
		String project_x = req.getParameter("project_x");
		String owner_x = req.getParameter("owner_x");
		String order = req.getParameter("order");
		String ALL_x = req.getParameter("ALL_x");

		String Table = req.getParameter("table");
		HttpSession session = req.getSession( true );
		session.setAttribute("Table", Table );

		//start query string

		Query = ("SELECT ");

		//add to columns array the columns to display
		if(hostname_x != null || ALL_x != null)
		columns[index++]="hostname";
		if(ip_x != null || ALL_x != null)
		columns[index++]="ip";
		if(vendor_x != null || ALL_x != null)
		columns[index++]="vendor";
		if(model_x != null || ALL_x != null)
		columns[index++]="model";
		if(sitelocation_x != null || ALL_x != null)
		columns[index++]="site_location";
		if(roomlocation_x != null || ALL_x != null)
		columns[index++]="room_location";
		if(assetnumber_x != null || ALL_x != null)
		columns[index++]="asset_number";
		if(description_x != null || ALL_x != null)
		columns[index++]="description";
		if(tagnumber_x != null || ALL_x != null)
		columns[index++]="tag_number";
		if(project_x != null || ALL_x != null)
		columns[index++]="project";
		if(owner_x != null || ALL_x != null)
		columns[index++]="owner";

		//if there are no columns to display return null string
		if(index == 0)
		return NullString;

		Query += "serial_num, ";

		//add columns array to query string putting commas where necessary
		for(int j = 0; j < index; j++) {
			if(j < index - 1) {
				Query += columns[j] + ", ";
			} else {
				Query += columns[j] + " ";
			}
		}

		//specify database
		Query += "from " + Table + " where ";

		//change null search values to wildcard '%'
		if(owner.length() ==  0) {
			owner = "%";
		}

		if(model.length() == 0) {
			model = "%";
		}

		if(vendor.length() == 0) {
			vendor = "%";
		}

		if(serialnumber.length() == 0) {
			serialnumber = "%";
		}

		if(project.length() == 0) {
			project = "%";
		}

		if(description.length() == 0) {
			description = "%";
		}


		//specify search parameters
		Query += "owner like '%" + owner + "%' and ";
		Query += "model like '%" + model + "%' and ";
		Query += "vendor like '%" + vendor + "%' and ";
		Query += "serial_num like '%" + serialnumber + "%' and ";
		Query += "project like '%" + project + "%' and ";
		Query += "description like '%" + description + "%'";

		//specify ordering
		Query += " order by " + order;
		return Query;

	}
}	
