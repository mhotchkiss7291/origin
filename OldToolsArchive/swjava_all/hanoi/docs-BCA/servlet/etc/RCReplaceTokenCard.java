import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class RCReplaceTokenCard extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

		GSHeadNTail ht = new GSHeadNTail();
    ht.putHead( out );

    out.println("<center>");
    out.println("<h1>WARNING!!</h1>");
    out.println("<p>");
		
		out.println("Replacing a user's token card for them is subject to strict guidelines ");
		out.println("and Sun's Security Policy. Failure to follow these guidelines ");
		out.println("will result in termination. Your actions are logged. ");

    out.println("<p>");

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

    out.println("<b>Enter User Data:</b>");
    out.println("<p>");
    out.println(
			"<FORM ACTION=\"/BCA/RCConfirmReplace\" METHOD=\"POST\" NAME=\"userid_form\">");
    out.println(
      "Enter the employee's Sun ID (12345) <INPUT TYPE=TEXT NAME=\"sunid\">");
    out.println("<P>");
    out.println(
      "Enter the card serial number <INPUT TYPE=TEXT NAME=\"cardid\">");
    out.println("<P>");
    out.println(
      "It is printed on the white sticker on the back of the token card.");
    out.println("<P>");
    out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"goto\" SIZE=\"-1\" VALUE=\"\">");
    out.println("<INPUT TYPE=SUBMIT VALUE=\"Submit\">");
    out.println("<p>");
    out.println("<a href=\"/BCA/GeneralServices\">Return to General Services.</a href>"); 
    out.println("</FORM>");
    out.println("</center>");

    ht.putTail( out );

  }
}
