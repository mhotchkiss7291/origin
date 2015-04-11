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
import java.util.Iterator;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

// JDOM
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.Builder;
import org.jdom.input.SAXBuilder;

public class GetRSSChannelServlet extends HttpServlet {

    /** Host to connect to for books list */
    private static final String hostname = "newInstance.com";
    /** Port number to connect to for books list */
    private static final int portNumber = 80;
    /** File to request (URI path) for books list */
    private static final String file = "/cgi/supplyBooks.pl";

    public void service(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException {            
            
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        
        // Connect and get XML listing of books
        URL getBooksURL = new URL("http", hostname, portNumber, file);
        InputStream in = getBooksURL.openStream();

        try {
            // Request SAX Implementation and use default parser
            Builder builder = new SAXBuilder();

            // Create the document
            Document doc = builder.build(in);
            
            // Output XML
            out.println(generateRSSContent(doc));
            
        } catch (JDOMException e) {        
            out.println("Error: " + e.getMessage());
        } finally {
            out.close();
        }
    }   
    
    /**
     * <p>
     * This will generate an RSS XML document using the supplied 
     *   JDOM <code>Document</code>.
     * </p.
     *
     * @param doc <code>Document</code> to use for input.
     * @return <code>String</code> - RSS file to output.
     * @throws <code>JDOMException</code> when errors occur.
     */
    private String generateRSSContent(Document doc) throws JDOMException {
        StringBuffer rss = new StringBuffer();
        
        rss.append("<?xml version=\"1.0\"?>\n")
           .append("<!DOCTYPE rss PUBLIC ")
           .append("\"-//Netscape Communications//DTD RSS 0.91//EN\" ")
           .append("\"http://my.netscape.com/publish/formats/rss-0.91.dtd\">\n")
           .append("<rss version=\"0.91\">\n")
           .append(" <channel>\n")
           .append("  <title>Technical Books</title>\n")
           .append("  <link>http://newInstance.com/javaxml/techbooks</link>\n")
           .append("  <description>\n")
           .append("   Your online source for technical materials, computers, ")
           .append("and computing books!\n")
           .append("  </description>\n")
           .append("  <language>en-us</language>\n")
           .append("  <image>\n")
           .append("   <title>mytechBooks.com</title>\n")
           .append("   <url>")
           .append("http://newInstance.com/javaxml/techbooks/images/techbooksLogo.gif")
           .append("</url>\n")
           .append("   <link>http://newInstance.com/javaxml/techbooks</link>\n")
           .append("   <width>140</width>\n")
           .append("   <height>23</height>\n")
           .append("   <description>\n")
           .append("    Your source on the Web for technical books.\n")
           .append("   </description>\n")
           .append("  </image>\n");
           
        // Add an item for each new title with Computers as subject
        List books = doc.getRootElement().getChildren("book");
        for (Iterator i = books.iterator(); i.hasNext(); ) {
            Element book = (Element)i.next();
            if (book.getAttribute("subject")
                    .getValue().equals("Computers")) {
                        
                // Output an item
                rss.append("<item>\n")
                    // Add title
                   .append(" <title>")
                   .append(book.getChild("title").getContent())
                   .append("</title>\n")
                    // Add link to buy book
                   .append(" <link>")
                   .append("http://newInstance.com/javaxml/techbooks/buy.xsp?isbn=")
                   .append(book.getChild("saleDetails")
                               .getChild("isbn")
                               .getContent())
                   .append("</link>\n")
                   .append(" <description>")
                    // Add description
                   .append(book.getChild("description").getContent())
                   .append("</description>\n")                       
                   .append("</item>\n");
                        
            }
        }          
         
        rss.append(" </channel>\n")
           .append("</rss>");
        
        return rss.toString();
    }

}