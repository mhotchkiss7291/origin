package com.sun.authtech.appvtool.searchcerts;

import com.sun.authtech.appvtool.*;
import com.sun.authtech.util.*;
import javax.servlet.http.*;
import java.util.*;

final class FormExpert {

   private final AppvToolRequest myRequest;

   FormExpert(AppvToolRequest theRequest) {
      myRequest = theRequest;
   }

   final Properties getSearchProps() {

      Properties props = new Properties();

      String searchBy = myRequest.getParam("search.by");
      if ((searchBy != null) && (searchBy.length() > 0)) {
         props.setProperty( "search_by", searchBy );
      }

      String searchFor = myRequest.getParam( "search.for" );
      if ((searchFor != null) && (searchFor.length() > 0)) {
         props.setProperty( "search_for", searchFor );
      }

      return props;

   }

   final String getSearchBy() {
      return myRequest.getParam("search.by","");
   }

   final String getSearchFor() {
      return myRequest.getParam("search.for","");
   }

   private final static String DEFAULT_MAX_ENTRIES = "20";

   final int getMaxEntries() {
      try {
         String stringValue = myRequest.getParam("search.max",DEFAULT_MAX_ENTRIES);
         int intValue = Integer.parseInt(stringValue);
         return (intValue < 1 ? 1 : intValue);
      }
      catch (Throwable theThrowable) { // In case String value isn't an int.
         return Integer.parseInt(DEFAULT_MAX_ENTRIES);
      }
   }

   final boolean showSearchForm() {
      try {
         return !myRequest.getRequiredParam("dosearch").equalsIgnoreCase("true");
      }
      catch (MissingParamException theException) {
         return true;
      }
   }
}
