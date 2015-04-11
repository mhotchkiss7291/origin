import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DeleteConfirm extends HttpServlet {

  private boolean DEBUG = true;

  public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    doGet( req, res );

  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    String UserID = null;

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

    UserID = req.getParameter("userid");

    GSHeadNTail ht = new GSHeadNTail();
    ht.putHead( out );

    out.println("<center>");
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

    out.println("<h2>Confirm User Delete</h2>");
    out.println("<FORM METHOD=POST ACTION=\"/BCA/DeleteResult\">");
    out.println(
      "Enter the user ID of the employee to be deleted for confirmation.");
    out.println("<P>");
    out.println("User ID to be deleted: <INPUT TYPE=TEXT NAME=\"confuserid\">");
    out.println("<P>");
    out.println("<INPUT TYPE=HIDDEN NAME=\"userid\" value=" + UserID + ">");
    out.println("<INPUT TYPE=SUBMIT VALUE=\"Submit\">");

    out.println("</center>");
    ht.putTail( out );

  }
}

