import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Enter extends HttpServlet {

  public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
    doGet( req, res );
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

    out.println("<FORM METHOD=POST ACTION=\"/jbservlet/RegisNLdap\">");
    out.println("Enter your SunID: <INPUT TYPE=TEXT NAME=\"sunid\">");
    out.println("<P>");
    out.println(
      "Enter your Regis Password: <INPUT TYPE=PASSWORD NAME=\"regispw\">");
    out.println("<P>");
    out.println("<INPUT TYPE=SUBMIT VALUE=\"Submit\">"); 
  }
}
