package com.sun.authtech.appvtool.searchcerts;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;
import javax.security.cert.*;

public class SearchName {

   public ArrayList doSearch( String search_input, ArrayList report ) {

			Properties p = new Properties();

      try { 
				p.load( new FileInputStream( 
					"/opt/iws60/https-SunPartnerApprovalTool/PreAppvTool/tool_config"
					) ); 
			} catch (Exception e) { 
				e.printStackTrace();
			}

      String search_prefix = p.getProperty("searchcerts.name.search_prefix");
      String url = p.getProperty("searchcerts.name.url");
      String base = p.getProperty("searchcerts.name.base");
      String filter1 = p.getProperty("searchcerts.name.filter1");

      String search_string = search_prefix + search_input;
      String principal = null;
      String password = null;

      Hashtable systemProps = new Hashtable();

      //Static parameters
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

      try {
         DirContext context = new InitialDirContext(systemProps);
         SearchControls controls = new SearchControls();
         controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
         Object[] oa = new Object[5];
         oa[0] = filter1;
         NamingEnumeration sEnum = null;
         sEnum = context.search( base, search_string, oa, controls );

         int count = 0;
         while (sEnum.hasMore()) {
            SearchResult sr = (SearchResult)sEnum.next();
            Attributes attrs = sr.getAttributes();
                                                                                
            for (NamingEnumeration ae = attrs.getAll();
                  ae.hasMoreElements();) {
              Attribute attr = (Attribute)ae.next();
              String attrId = attr.getID();
              if( ((String)attr.getID()).equals( filter1 )) {
								report.add("Approved By: " + attr.get() );
              }
           }
           ++count;
				}
      } catch (javax.naming.NamingException e) {
         System.out.println("\nException thrown: " + e);
         e.printStackTrace();
      }
			return report;
   }
}
