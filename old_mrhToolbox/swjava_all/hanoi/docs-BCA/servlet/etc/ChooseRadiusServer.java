import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChooseRadiusServer extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

		GSHeadNTail ht = new GSHeadNTail();
    ht.putHead( out );

		out.println("<p>");
		out.println("<center>");

    out.println(
			"<a href=\"/BCA/AddRadiusServer\"> Add Radius Entry");
		out.println("<p>");
		out.println(
			"<a href=\"/BCA/DeleteRadiusServer\"> Delete Radius Entry");
		out.println("<p>");
		out.println("</center>");

    ht.putTail( out );

  }
}
