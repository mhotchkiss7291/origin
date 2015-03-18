import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EditTHServices extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

		GSHeadNTail ht = new GSHeadNTail();
    ht.putHead( out );

    out.println("<center>");
    out.println("<h1>");
    out.println("Edit TH Services");
    out.println("</h1>");

    out.println("<b>Note: </b>");
    out.println("These services allow an authorized employee to ");
    out.println("authenticate to servers within Sun's infrastructure. ");
    out.println("The user must have a user account on the specfic ");
    out.println("server they will be accessing as well as these ");
    out.println("services to access the server.");
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

    out.println(
			"<FORM ACTION=\"/BCA/ReturnTHServices\" METHOD=\"POST\" NAME=\"userid_form\">");
    out.println(
      "Enter the employee's User ID (xx12345) <INPUT TYPE=TEXT NAME=\"userid\">");
    out.println("<P>");
    out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"goto\" SIZE=\"-1\" VALUE=\"\">");
    out.println("<INPUT TYPE=SUBMIT VALUE=\"Submit\">");
    out.println("</FORM>");
    out.println("<P>");
		out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/BusinessServices\">Return to Business Services</A></FONT>");
    out.println("</center>");

    ht.putTail( out );

  }
}
