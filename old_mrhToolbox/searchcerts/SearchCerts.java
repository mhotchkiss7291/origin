package com.sun.authtech.appvtool.searchcerts;

import com.sun.authtech.appvtool.*;

import com.sun.authtech.pinmgr.*;
import com.sun.authtech.enrolldb.*;
import com.sun.authtech.authodb.*;
import com.sun.authtech.appvgrpdb.*;
import com.sun.authtech.partner.authedb.*;
import com.sun.authtech.corpldap.*;
import com.sun.authtech.util.*;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;
import java.util.*;

public class SearchCerts extends AppvToolServlet {

   private EnrollmentDB myEnrollmentDB = null;
   private ApprovalGroupDB myAppvGrpDB = null;
   private AuthorizationDB myRoleDB = null;
   private PinManager myPinManager = null;
   private CorpLdap myCorpLdap = null;
   private XSLTemplateFactory myTemplateFactory = null;

   protected final void init(AppvToolConfig theConfig) 
                      throws ServletException,
                             IOException {
      myEnrollmentDB = theConfig.getEnrollmentDB();
      myAppvGrpDB = theConfig.getApprovalGroupDB();
      myRoleDB = theConfig.getRoleDB();
      myPinManager = theConfig.getEnrollmentPinManager();
      myCorpLdap = theConfig.getCorpLdap();
      myTemplateFactory = theConfig.getTemplateFactory();
   }

   protected void processRequest(AppvToolRequest theRequest,
                                 PrintWriter theResponse)
                          throws MissingParamException,
                                 IOException {

      FormExpert form = new FormExpert( theRequest );

      if ( form.showSearchForm() ) {
         myTemplateFactory.getTemplate("SearchCertsForm",true).transform(
            "<searchcerts/>",
            theResponse);
         return;
      }

      // Business operations start HERE!!
      String sunId = theRequest.getUserID();
			String searchString = form.getSearchFor();
			String searchBy = form.getSearchBy();

			SearchEmailAddress eml = new SearchEmailAddress();
			String emailAddress = eml.doSearch( sunId );

			ArrayList report = new ArrayList();

			if ( searchString != null || emailAddress != null ) {

				SearchRequest sr = SearchRequest.getInstance();

				if ( searchBy.equals("Company") ) {
					report = sr.searchByCompany( searchString, report );
				} else if ( searchBy.equals("Owner") ) {
					report = sr.searchByOwner( searchString, report );
				} else if ( searchBy.equals("Approver") ) {
					//out.println("search_type not available yet.");
				} else {
					//out.println("Your entry for <searchby> was not recognized");
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

      EmailNotify mail = new EmailNotify( 
				sb.toString(), sb2.toString(), emailAddress );
			try {
				mail.send();
			} catch (Exception e) {
				e.printStackTrace();
			}

      // Outputing the results
      try {

         StringWriter xmldoc = new StringWriter();
         XMLWriter xmldocOut = new XMLWriter(xmldoc);

         xmldocOut.startDocument("searchcerts");

         theRequest.appendUserInfo(xmldocOut);
         myAppvGrpDB.appendAppvGroups(xmldocOut);
         xmldocOut.startElement("search");
         xmldocOut.addTextElement("search_by",form.getSearchBy());
         xmldocOut.addTextElement("search_for",form.getSearchFor());
         xmldocOut.endElement();

         xmldocOut.endDocument();

         myTemplateFactory.getTemplate("SearchCertsResponse",true).transform(
            xmldoc.toString(),
            theResponse);

      } catch (Exception e) {
				e.printStackTrace();
      }
   }
}
