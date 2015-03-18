import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class TokenCardReg extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			 throws ServletException, IOException {

		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		HeadNTail ht = new HeadNTail();
		ht.putHead(out);

		out.println("<h2>Select one of the following operations:</h2>");

		out.println("<p>");
		out.println("<b>Note:</b> You will have to have changed your Regis Password from ");
		out.println("its original default (your Social Security Number) to something else for ");
		out.println("you to be authenticated for any of these services. ");
		out.println("Go to <a href=\"http://regis.central\"> http://regis.central</a> to do this.");
		out.println("<p>");

		out.println("<ul>");
		out.println("<li>");
		out.println(
				"<a href=\"/servlet/EnableCard\"> New Token Card Registration");

		out.println("<p>");

		out.println("<li>");
		out.println(
			"<a href=\"/servlet/ReplaceCard\"> Replacement Token Card Registration");

		out.println("<p>");

		out.println("<li>");
		out.println(
				"<a href=\"/servlet/GetPin\"> Retrieve your Token Card PIN");

		out.println("</ul></a>");

		ht.putTail(out);

	}
}

