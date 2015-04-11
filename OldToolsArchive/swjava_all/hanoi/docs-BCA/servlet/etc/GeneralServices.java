import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class GeneralServices extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

		GSHeadNTail ht = new GSHeadNTail();
    ht.putHead( out );

		out.println("<title>Token Card Administration: General Services</title>");
		out.println("<p>");
		out.println("<center>");

		out.println("<h1>General Services</h1>");

    out.println(
      "<a href=\"/BCA/RCEnableTokenCard\"> Enable User Token Card");
    out.println("<p>");
    out.println(
      "<a href=\"/BCA/RCReplaceTokenCard\"> Replace User Token Card");
    out.println("<p>");
		out.println(
			"<a href=\"/BCA/GSRetrieveUserPIN\"> Retrieve User PIN");
		out.println("<p>");
    out.println(
			"<a href=\"/BCA/ViewUserServices\"> View User Services");
		out.println("<p>");
		out.println(
			"<a href=\"/BCA/DeleteAccount\"> Employee Account Deletion");
		out.println("<p>");
		//CHECK
		out.println("<h2><A HREF=\"https://hanoi.central.sun.com:5443/swp/wls\">Logout</A></h2>");
		out.println("<p>");
		out.println("</center>");

    ht.putTail( out );

  }
}
