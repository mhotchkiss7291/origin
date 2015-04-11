package com.sun.authtech.appvtool.searchcerts;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;
import javax.security.cert.*;

public class SearchCompany {

	 private static int MAX_ORG_UNITS = 10;
	 private static String[] orgunits = new String[ MAX_ORG_UNITS ];

   public void doSearch( String search_input ) {

			Properties p = new Properties();

      try { 
				p.load( new FileInputStream( 
					  "/opt/iws60/https-SunPartnerApprovalTool/PreAppvTool/tool_config"
					) ); 
			} catch (Exception e) { 
				e.printStackTrace();
			}

      String search_prefix = p.getProperty("searchcerts.company.search_prefix");
      String search_suffix = p.getProperty("searchcerts.company.search_suffix");
      String base = p.getProperty("searchcerts.company.base");
      String url = p.getProperty("searchcerts.company.url");
      String filter1 = p.getProperty("searchcerts.company.filter1");
      String principal = p.getProperty("searchcerts.company.principal");
      String password = p.getProperty("searchcerts.company.password");

      String search_string = search_prefix + search_input + search_suffix ;

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

         int count = 0;
         int i = 0;
         while (sEnum.hasMore()) {
            SearchResult sr = (SearchResult)sEnum.next();
            Attributes attrs = sr.getAttributes();
                                                                                
            for (NamingEnumeration ae = attrs.getAll();
                  ae.hasMoreElements();) {
              Attribute attr = (Attribute)ae.next();
              String attrId = attr.getID();
              if( ((String)attr.getID()).equals( filter1 )) {

								// Stuff the array for transport
							  orgunits[ i ] = attr.get().toString();
                i++;
              }
           }
           ++count;
				}
      } catch (javax.naming.NamingException e) {
         System.out.println("\nException thrown: " + e);
         e.printStackTrace();
      }
   }

	String[] getOrgs() {
    return orgunits;
	}
}
