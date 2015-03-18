import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class GSRetrieveUserPIN extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

		GSHeadNTail ht = new GSHeadNTail();
    ht.putHead( out );

		out.println("<BODY BGCOLOR=\"#FFFFFF\" ONLOAD=\"setFieldFocus()\">");
    out.println("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html\">");
    out.println("<SCRIPT LANGUAGE=\"JavaScript\">");
    out.println("<!--");
    out.println("function setFieldFocus()");
    out.println("{");
    out.println("document.userid_form.userid.focus();");
    out.println("}");
    out.println("//-->");
    out.println("</SCRIPT>");

    out.println("<center>");
    out.println(
			"<FORM ACTION=\"/BCA/GSReturnPIN\" METHOD=\"POST\" NAME=\"userid_form\">");
    out.println(
      "Enter the employee's User ID (xx12345) <INPUT TYPE=TEXT NAME=\"userid\">");
    out.println("<P>");
    out.println(
      "Enter the card serial number <INPUT TYPE=TEXT NAME=\"cardid\">");
    out.println("<P>");
    out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"goto\" SIZE=\"-1\" VALUE=\"\">");
    out.println("<INPUT TYPE=SUBMIT VALUE=\"Submit\">");
    out.println("</FORM>");
    out.println("<P>");
		out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/GeneralServices\">Return to General Services</A></FONT>");
    out.println("</center>");

    ht.putTail( out );

  }
}
