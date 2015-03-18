import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CreateTableForm extends HttpServlet {

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

		DBConnection dbc = new DBConnection( GroupID );

	  if( dbc.getConnection()	== null ) {
			out.println("The Group entered does not match an existing database.");
			out.println("<br>Please try again.");
			ht.putTail( out );
			return;
		}

		out.println("<FORM METHOD=POST ACTION=\"/servlet/CreateTable\">");
    out.println("Enter a name for your new Table: <INPUT TYPE=TEXT NAME=\"tablename\">");
		out.println("<P>");
    out.println("<INPUT TYPE=SUBMIT VALUE=\"Submit\">");
    out.println("</FORM>");
		ht.putTail( out );

	}
}
