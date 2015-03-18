import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class BusinessServices extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

		GSHeadNTail ht = new GSHeadNTail();
    ht.putHead( out );

		out.println("<p>");
		out.println("<center>");

		out.println("<h1>Business Services</h1>");

    out.println(
			"<a href=\"/BCA/BViewUserServices\"> View User Services");
		out.println("<p>");
		out.println(
			"<a href=\"/BCA/EditITEServices\"> Edit ITE Services");
		out.println("<p>");
		out.println(
			"<a href=\"/BCA/EditECCPXServices\"> Edit ECCPX Services");
		out.println("<p>");
		out.println(
			"<a href=\"/BCA/EditPSCServices\"> Edit PSC Services");
		out.println("<p>");
		out.println(
			"<a href=\"/BCA/EditTHServices\"> Edit Trusted Host");
		out.println("<p>");
		out.println(
			"<a href=\"/BCA/EditNSGServices\"> Edit NSG Services");
		out.println("<p>");
		out.println(
			"<a href=\"/BCA/EditIcabadServices\"> Edit Icabad Services");
		out.println("<p>");
		out.println(
			"<a href=\"/BCA/EditDialupServices\"> Edit Dialup Services");
		out.println("<p>");
		out.println(
			"<a href=\"/BCA/ChooseRadiusServer\"> Radius Servers");
		out.println("<p>");
    out.println(
          "<a href=\"/BCA/RCServicesReport\"> Reports");
    out.println("<p>");
		//CHECK!
		out.println("<h2><A HREF=\"https://hanoi.central.sun.com:5443/swp/wls\">Logout</A></h2>");
		out.println("<p>");
		out.println("</center>");

    ht.putTail( out );

  }
}
