package com.sun.authtech.appvtool.searchcerts;

import java.io.*;
import java.lang.*;
import java.text.*;
import java.util.*;

public class SearchRequest {

  static final SearchRequest instance;

  static {
		instance = new SearchRequest();
	}

  public static SearchRequest getInstance() {
		return instance;
	}

  private SearchRequest() {
	}

  public ArrayList searchByCompany( 
			String search_string, ArrayList report ) {

		SearchCompany sc = new SearchCompany();
		sc.doSearch( search_string );
    String[] myorgs = sc.getOrgs();
    SearchCompanyCert scc = new SearchCompanyCert();
    int i = 0;

    while( myorgs[ i ] != null )  {
		  report = scc.doSearch(  myorgs[ i ], report );
      i++;
		}

    return report;

	}

  public ArrayList searchByOwner( 
			String search_string, ArrayList report ) {

    SearchOwnerCert soc = new SearchOwnerCert();
	  report = soc.doSearch( search_string, report );

    return report;

	}
}
