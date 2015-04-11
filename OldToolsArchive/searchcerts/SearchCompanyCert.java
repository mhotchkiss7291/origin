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
public class SearchCompanyCert {

   public ArrayList doSearch( String search_input, ArrayList report ) {

			Properties p = new Properties();

      try { 
				p.load( new FileInputStream( 
					"/opt/iws60/https-SunPartnerApprovalTool/PreAppvTool/tool_config"
					) ); 
			} catch (Exception e) { 
				e.printStackTrace();
			}

      String base_arg_prefix = p.getProperty("searchcerts.compcert.base_arg_prefix");
      String base_arg_suffix = p.getProperty("searchcerts.compcert.base_arg_suffix");
      String search_prefix = p.getProperty("searchcerts.compcert.search_prefix");
      String url = p.getProperty("searchcerts.compcert.url");
      String filter1 = p.getProperty("searchcerts.compcert.filter1");
      String principal = p.getProperty("searchcerts.compcert.principal");
      String password = p.getProperty("searchcerts.compcert.password");

      String base = base_arg_prefix + search_input + base_arg_suffix ;

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
         sEnum = context.search( base, search_prefix, oa, controls );

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

                //Subject: CN=Janeth Calvillo, OU=Samsung, O=SMI Partner
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
         System.out.println("\nException thrown: " + e);
         e.printStackTrace();
      }

	    return report;
   }
}
