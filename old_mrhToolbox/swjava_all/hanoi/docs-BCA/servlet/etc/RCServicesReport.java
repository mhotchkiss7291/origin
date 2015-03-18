//Called by GeneralServices class.
//Service is selected and passed 
//to RetrunServicesReport class. 

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class RCServicesReport extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

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

    out.println(
			"<FORM ACTION=\"/BCA/ReturnServicesReport\" METHOD=\"POST\" NAME=\"userid_form\">");
    out.println("<h1>WARNING!!</h1>");
    out.println("<P>");
    out.println("This report may take 5 - 10 mintues to load.");
    out.println("A new web page will come up with the results.");
    out.println("<P>");
    out.println("Select a service to query by:");
    out.println("<SELECT NAME=\"service\">");
	  out.println("<OPTION VALUE=\"DIALUP\">DIALUP");
    out.println("<OPTION VALUE=\"PSC\">PSC");
    out.println("<OPTION VALUE=\"NSG\">NSG");
    out.println("<OPTION VALUE=\"TH\">TH");
    out.println("<OPTION VALUE=\"PACRIMP3\">PacRim");
    out.println("<OPTION VALUE=\"SAFEW\">SAFEW");
    out.println("<OPTION VALUE=\"ITE\">ITE");
    out.println("<OPTION>ECCPX");
    out.println("</SELECT>");
    out.println("<br>");
    out.println("<br>");
    out.println("<br>");
    out.println("<br>");
    out.println("<br>");
    out.println("<br>");
    out.println("<br>");

/*----------- NEW CODE------------ */


out.println("<big> <big> Optional </big> </big>");
out.println("<br>");
out.println("If you would like to be sent a copy of these results, enter your email address: <INPUT TYPE=TEXT NAME=\"email\">");
    out.println("<P>");

/*----------- END NEW CODE -------- */



    //Not sure why following line is in here, but left it since it doesn't do any harm.
    out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"goto\" SIZE=\"-1\" VALUE=\"\">");
    out.println("<INPUT TYPE=SUBMIT VALUE=\"Submit\">");
    out.println("</FORM>");
    out.println("<P>");
		out.println("<FONT FACE=\"helvetica, lucida sans\"><A HREF=\"/BCA/BusinessServices\">Return to Business Services</A></FONT>");
    out.println("</center>");

    ht.putTail( out );

  }
}
