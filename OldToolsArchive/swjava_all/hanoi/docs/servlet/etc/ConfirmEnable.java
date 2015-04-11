import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ConfirmEnable extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			 throws ServletException, IOException {

		doGet(req, res);

	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			 throws ServletException, IOException {

		String SunID = null;
		String RegisPW = null;
		String CardID = null;

		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		SunID = req.getParameter("sunid");
		RegisPW = req.getParameter("regispw");
		CardID = req.getParameter("cardid");

		HeadNTail ht = new HeadNTail();
		ht.putHead(out);

		out.println("<h2>New Token Card Registration</h2>");
		out.println("<FORM METHOD=POST ACTION=\"/servlet/eSunAuthEnable\">");
		out.println(
				"Enter the serial number of your token card again for confirmation.");
		out.println("<P>");
		out.println("Card number: <INPUT TYPE=TEXT NAME=\"confcardid\">");
		out.println("<P>");
		out.println("<INPUT TYPE=HIDDEN NAME=\"sunid\" value=" + SunID + ">");
		out.println("<INPUT TYPE=HIDDEN NAME=\"regispw\" value=" + RegisPW + ">");
		out.println("<INPUT TYPE=HIDDEN NAME=\"cardid\" value=" + CardID + ">");
		out.println("<INPUT TYPE=SUBMIT VALUE=\"Submit\">");

		ht.putTail(out);

	}
}

