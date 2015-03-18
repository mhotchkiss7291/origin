import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SearchPKI extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        			throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String title = "Search PKI";

        out.println("<html>");
        out.println("<body>");
        out.println("<head>");
        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");
        out.println("<h3>" + title + "</h3>");
        out.println("<P>");

        out.print("<form action=\"/TestServlet/servlet/SearchDone\"");
        out.println(" method=POST>");
        out.println( "Search by: <select name=\"searchby\" size=1>");
        out.println( "<option selected> " +  "Owner" );
        out.println("<option>Owner</option>");
        out.println("<option>Company</option>");
        out.println("<option>Approver</option>");
        out.println("</select>");
        out.println("<p>"); 
        out.println( "Search string:" );
        out.println("<input type=text size=40 name=\"searchstring\">");
        out.println("<p>");
        out.println( "SunID:" );
        out.println("<input type=text size=40 name=\"sunid\">");
        out.println("<br>");
        out.println("<input type=submit>");
        out.println("</form>");

        out.println("</body>");
        out.println("</html>");
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException {

        doGet(request, response);
    }

}
