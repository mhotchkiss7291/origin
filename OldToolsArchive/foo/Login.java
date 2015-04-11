import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Login extends HttpServlet {

  public void doPost(HttpServletRequest req, HttpServletResponse res)
       throws ServletException, IOException {
    doGet(req, res);
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
       throws ServletException, IOException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

		HeadNTail ht = new HeadNTail();
		ht.putHead( out );

		out.println("<center><h2>Welcome to the IT Technology Office");
		out.println("<br>");
		out.println("Inventory Control System</h2></center>");
		out.println("<p>");
		out.println("To use this service, you need to apply for a <b>GroupID</b>");
		out.println(" and have a corresponding database created for you.");
		out.println("<p>");
		out.println("Contact ");
		out.println("<A HREF=mailto:mark.hotchkiss@Sun.COM>Mark Hotchkiss</A>");
		out.println(" to apply for a GroupID.");
		out.println("<p>");
		out.println("If you already have a GroupID, please log in below.");
		out.println("<p>");

		out.println("<FORM METHOD=POST ACTION=\"/servlet/QueryForm\">");
    out.println("Enter your Group ID: <INPUT TYPE=TEXT NAME=\"groupid\">");
		out.println("<P>");
    out.println("<INPUT TYPE=SUBMIT VALUE=\"Submit\">");
    out.println("</FORM>");

		ht.putTail( out );

	}
}
