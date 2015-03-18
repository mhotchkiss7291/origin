/*--

 Copyright (C) 2000 Brett McLaughlin. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, the disclaimer that follows these conditions,
    and/or other materials provided with the distribution.

 3. Products derived from this software may not be called "Java and XML", nor may
    "Java and XML" appear in their name, without prior written permission from
    Brett McLaughlin (brett@newInstance.com).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 JDOM PROJECT  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software was originally created by Brett McLaughlin <brett@newInstance.com>.
 For more  information on "Java and XML", please see <http://www.oreilly.com/catalog/javaxml/>
 or <http://www.newInstance.com>.

 */
package com.techbooks;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import javax.servlet.*;
import javax.servlet.http.*;

// Import Xalan XSLT Processor components
import org.apache.xalan.xslt.XSLTInputSource;
import org.apache.xalan.xslt.XSLTProcessor;
import org.apache.xalan.xslt.XSLTProcessorFactory;
import org.apache.xalan.xslt.XSLTResultTarget;

public class ListBooksServlet extends HttpServlet {

    /** Host to connect to for books list */
    private static final String hostname = "newInstance.com";
    /** Port number to connect to for books list */
    private static final int portNumber = 80;
    /** File to request (URI path) for books list */
    private static final String file = "/cgi/supplyBooks.pl";
    
    /** Stylesheet to apply to XML */
    private static final String stylesheet = 
        "d:\\prod\\Java and XML\\content\\computerBooks.xsl";
        //"/home/client/java/newinstance/www/javaxml/techbooks/XSL/computerBooks.xsl";

    public void service(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException {            
            
        res.setContentType("text/html");            
        
        // Connect and get XML listing of books
        //URL getBooksURL = new URL("http", hostname, portNumber, file);
        URL getBooksURL = new URL("http://newInstance.com/cgi/supplyBooks.pl");
        InputStream in = getBooksURL.openStream();

        try {            
            XSLTProcessor processor = XSLTProcessorFactory.getProcessor();
            
            // Transform XML with XSL stylesheet
            processor.process(new XSLTInputSource(in),
                              new XSLTInputSource(new FileInputStream(stylesheet)),
                              new XSLTResultTarget(res.getOutputStream()));
            
        } catch (Exception e) {
            PrintWriter out = res.getWriter();            
            out.println("Error: " + e.getMessage());
            out.close();
        }
    }    

}