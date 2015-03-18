package com.sun.authtech.appvtool.searchcerts;

import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;
import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;
import javax.security.cert.*;

//Entry point for searching all certs by Company
public class SearchOwnerCert {

   public ArrayList doSearch( String search_input, ArrayList report ) {

			Properties p = new Properties();

      try { 
				p.load( new FileInputStream( 
					"/opt/iws60/https-SunPartnerApprovalTool/PreAppvTool/tool_config"
					) ); 
			} catch (Exception e) { 
				e.printStackTrace();
			}

      String base = p.getProperty("search_owner_cert_base");
      String search_prefix = p.getProperty("search_owner_cert_search_prefix");
      String search_suffix = p.getProperty("search_owner_cert_search_suffix");
      String url = p.getProperty("search_owner_cert_url");
      String filter1 = p.getProperty("search_owner_cert_filter1");

      String search_string = search_prefix + search_input + search_suffix;
      String principal = null;
      String password = null;

      Hashtable systemProps = new Hashtable();
      systemProps.put(
         Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
      systemProps.put(Context.SECURITY_AUTHENTICATION, "simple");
      systemProps.put(Context.REFERRAL, "throw");

      //Variable parameters
      systemProps.put(Context.PROVIDER_URL, url);


      if( principal != null ) {
        systemProps.put(Context.SECURITY_PRINCIPAL, principal );
      }
      if( password != null ) {
        systemProps.put(Context.SECURITY_CREDENTIALS, password );
      }

      byte[] myBytes = null;

      try {
         DirContext context = new InitialDirContext(systemProps);
         SearchControls controls = new SearchControls();
         controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
         Object[] oa = new Object[5];
         oa[0] = filter1;
         NamingEnumeration sEnum = null;
         sEnum = context.search( base, search_string, oa, controls );

         while (sEnum.hasMore()) {
            SearchResult sr = (SearchResult)sEnum.next();
            Attributes attrs = sr.getAttributes();
                                                                                
            for (NamingEnumeration ae = attrs.getAll();
                  ae.hasMoreElements();) {
              Attribute attr = (Attribute)ae.next();
              String attrId = attr.getID();

              if( ((String)attr.getID()).equals( filter1 )) {
                myBytes = (byte[])attr.get();
                X509Certificate cert = null;
                try {
                  cert = X509Certificate.getInstance( myBytes );
                } catch (Exception e) {
                  e.printStackTrace();
                }

								String serial_number = cert.getSerialNumber().toString();
								Integer hex_sn_Int = new Integer( serial_number ); 
								String hex_sn = Integer.toHexString( hex_sn_Int.intValue() );

						    report.add( "DN: " + sr.getName() );
								report.add( 
									"Serial Number: " + 
									cert.getSerialNumber().toString() + 
									" (" + hex_sn + ")" );

								String subject_line = cert.getSubjectDN().getName();
								report.add( "Subject: " + subject_line );

                int i = subject_line.indexOf("CN=", 0);
                int j = subject_line.indexOf(",", i+3);
                String s_owner = null;
								if( i != -1 ) {
                  s_owner = subject_line.substring( i+3, j );
								} else {
                  s_owner = "Not Available";
								}

                report.add( "CN: " + s_owner );

                i = subject_line.indexOf("OU=", 0);
                j = subject_line.indexOf(",", i+3);
                String company = null;

                if( i != -1 ) {
                  company = subject_line.substring( i+3, j );
								} else {
                  company = "Not Available";
								}

                report.add( "Company: " + company );
                report.add( "Not Before: " + cert.getNotBefore().toString() ); 
                report.add( "Not After: " + cert.getNotAfter().toString() );

                SearchApprover sa = new SearchApprover();
								report = sa.doSearch( s_owner, report );
              }
           }
				}
      } catch (javax.naming.NamingException e) {
         e.printStackTrace();
      }

	    return report;
   }
}
