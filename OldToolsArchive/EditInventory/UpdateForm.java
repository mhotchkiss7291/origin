import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class UpdateForm extends HttpServlet {

  public void doPost(HttpServletRequest req, HttpServletResponse res)
       throws ServletException, IOException {
    doGet(req, res);
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
       throws ServletException, IOException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
		HttpSession session = req.getSession( true );

		String GroupID = req.getParameter("groupid");

		HeadNTail ht = new HeadNTail();
		ht.putHead( out );

		if( GroupID == null ) {
			GroupID = (String)session.getAttribute("GroupID");
		}

		session.setAttribute("GroupID", GroupID);
			
		out.println("<h3>Database: <b>" + GroupID + "</b></h3>");
		out.println("<p>");

		out.println(
			"<a href= \"./InsertForm\"> Insert a Row in a Table </a>");
		out.println("<p>");
		out.println(
			"<a href= \"./DeleteForm\"> Delete a Row in a Table </a>");
		out.println("<p>");

		DBConnection dbc = new DBConnection( GroupID );

	  if( dbc.getConnection()	== null ) {
			out.println("The Group entered does not match an existing database.");
			out.println("<br>Please try again.");
			ht.putTail( out );
			return;
		}

		out.println("<hr>");
		out.println("<h3>Edit Existing Rows</h3>");
		out.println("<p>");

		Vector tables = dbc.showTables();
		dbc.closeConnection();

		out.println("<P>");
		out.println("<b>Select Table to Edit:</b>");
		out.println("<FORM METHOD=POST ACTION=\"/servlet/UpdateQuery\">");
 		out.println("<SELECT NAME=table size=1>");
    out.println("<OPTION> -Select-");
		int i = 0;

		while( i < tables.size() ) {
			out.println("<OPTION>" + tables.get( i ).toString() );
			i++;
		}
		out.println("</SELECT");

		out.println("<p>");
		out.println("<P>");
		out.println("<b>Search</b>");
		out.println("<P>");
		out.println("Optional: ");
		out.println(
			"You may enter information into any of the following " + 
			"boxes to refine the search.");
		out.println("<TABLE>");
		out.println("<TR><TD>Owner:</TD><TD>Model:</TD><TD>Vendor:</TD></TR>");
		out.println("<TR>");
		out.println("<TD><INPUT type=text value = \"\" name=owner></TD><TD>");
		out.println("<INPUT type=text value = \"\" name=model></TD><TD>");
		out.println("<INPUT type=text value = \"\" name=vendor></TD></TR>");
		out.println(
			"<TR><TD>Project:</TD><TD>Serial Number:</TD><TD>Description:</TD></TR>");
		out.println("<TR>");
		out.println("<TD><INPUT type=text value = \"\" name=project></TD>");
		out.println("<TD><INPUT type=text value = \"\" name=serialnumber></TD>");
		out.println("<TD><INPUT type=text value = \"\" name=description></TD>");
		out.println("</TR>");
		out.println("</TABLE>");
		out.println(
			"<hr>Choose which information you would like the search to " +
			"display.<br><br>");
		out.println(
			"<INPUT type=\"checkbox\" name=serialnumber_x checked> Serial Number <br>");
		out.println("<INPUT type=\"checkbox\" name=hostname_x checked> Hostname <br>");
		out.println("<INPUT type=\"checkbox\" name=ip_x checked> IP Address <br>");
		out.println("<INPUT type=\"checkbox\" name=vendor_x checked> Vendor <br>");
		out.println("<INPUT type=\"checkbox\" name=model_x checked> Model <br>");
		out.println(
			"<INPUT type=\"checkbox\" name=sitelocation_x checked> Site Location <br>");
		out.println(
			"<INPUT type=\"checkbox\" name=roomlocation_x checked> Room Location <br>");
		out.println(
			"<INPUT type=\"checkbox\" name=assetnumber_x checked> Asset Number <br>");
		out.println(
			"<INPUT type=\"checkbox\" name=description_x checked> Description <br>");
		out.println("<INPUT type=\"checkbox\" name=tagnumber_x checked> Tag Number <br>");
		out.println("<INPUT type=\"checkbox\" name=project_x checked> Project <br>");
		out.println("<INPUT type=\"checkbox\" name=owner_x checked> Owner <br><br>");
		out.println("<tr><td>");
		out.println("Order By:");
		out.println("<SELECT NAME=\"order\" size=1>");
		out.println("<option value=\"serial_num\"> Serial Number </option>");
		out.println("<option value=\"hostname\" > Hostname </option>");
		out.println("<option value=\"ip\" > IP Address</option>");
		out.println("<option value=\"vendor\" > Vendor </option>");
		out.println("<option value=\"model\" > Model </option>");
		out.println("<option value=\"site_location\" > Site Location </option>");
		out.println("<option value=\"room_location\" > Room Location </option>");
		out.println("<option value=\"asset_number\" > Asset Number </option>");
		out.println("<option value=\"description\" > Description </option>");
		out.println("<option value=\"tag_number\" > Tag Number </option>");
		out.println("<option value=\"project\" > Project </option>");
		out.println("<option value=\"owner\" > Owner </option>");
		out.println("</SELECT>");
		out.println("</td></tr>");
		out.println("</table><br>");
		out.println("<INPUT type=submit value=\"EDIT\" name=search>");
		out.println("</FORM>");

		ht.putTail( out );
	}
}

