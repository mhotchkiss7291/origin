package com.sun.authtech.appvtool.searchcerts;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;

public class SearchDone extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        			throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");

        String searchString = request.getParameter("searchstring");
        String searchBy = request.getParameter("searchby");
        String sunId = request.getParameter("sunid");

        //out.println("searchString = " + searchString + "<br>" );
        //out.println("searchBy = " + searchBy + "<br>" );
        out.println("sunId = " + sunId + "<br>" );

				SearchEmailAddress eml = new SearchEmailAddress();
        String emailAddress = eml.doSearch( sunId );

        out.println("emailAddress = " + emailAddress + "<br>" );

				ArrayList report = new ArrayList();

        if ( searchString != null || emailAddress != null ) {

				  SearchRequest sr = SearchRequest.getInstance();

					if ( searchBy.equals("Company") ) {
						report = sr.searchByCompany( searchString, report );
					} else if ( searchBy.equals("Owner") ) {
						report = sr.searchByOwner( searchString, report );
					} else if ( searchBy.equals("Approver") ) {
						out.println("search_type not available yet.");
					} else {
						out.println("Your entry for <searchby> was not recognized");
					}
        }

				StringBuffer sb = new StringBuffer();
				StringBuffer sb2 = new StringBuffer();
				String buf = null;

        int i = 0;
        while( i < report.size() ) {

					buf = report.get( i ).toString();

          if( buf.startsWith("DN") ) {
            sb.append( "\n" );
            sb2.append( "\n" );
					}
					 
          sb.append( buf + "\n" );
          sb2.append( buf + "," );
          i++;
				}

        out.println("<p>Done<p>");

        out.println("</body>");
        out.println("</html>");

    EmailNotify mail = new EmailNotify( 
      sb.toString(),
			sb2.toString(), 
			emailAddress );
		try {
      mail.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
  }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException {

        doGet(request, response);
    }

}
