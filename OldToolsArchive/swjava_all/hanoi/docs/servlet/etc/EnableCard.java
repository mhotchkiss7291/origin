import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EnableCard extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			 throws ServletException, IOException {

		doGet(req, res);

	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			 throws ServletException, IOException {

		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		HeadNTail ht = new HeadNTail();
		ht.putHead(out);

		out.println("<h2>New Token Card Registration</h2>");

		out.println("<p>");
		out.println("<b>Note:</b> You will have to have changed your Regis Password from ");
		out.println("its original default (your Social Security Number) to something else for ");
		out.println("you to be authenticated for any of these services. ");
		out.println("Go to <a href=\"http://regis.central\"> http://regis.central</a> to do this.");
		out.println("<p>");

		out.println("<FORM METHOD=POST ACTION=\"/servlet/ConfirmEnable\">");
		out.println("Enter your SunID: <INPUT TYPE=TEXT NAME=\"sunid\">");
		out.println("<P>");
		out.println("Enter your Regis Password: <INPUT TYPE=PASSWORD NAME=\"regispw\">");
		out.println("<P>");
		out.println(
				"Enter the serial number that appears on the sticker on the back of your Safeword token card.");
		out.println("<P>");
		out.println("<b>Note:</b> The number begins with one or two letters and is not case sensitive.");
		out.println("<P>");
		out.println("Card number: <INPUT TYPE=TEXT NAME=\"cardid\">");
		out.println("<P>");
		out.println("<INPUT TYPE=SUBMIT VALUE=\"Submit\">");

		ht.putTail(out);

	}
}

