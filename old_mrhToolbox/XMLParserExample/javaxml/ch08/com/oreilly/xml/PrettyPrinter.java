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
package com.oreilly.xml;

import java.io.File;

import org.jdom.Document;
import org.jdom.input.Builder;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * <b><code>PrettyPrinter</code></b> will output the XML document at a
 *   given URI
 *
 * @author Brett McLaughlin
 * @author Jason Hunter
 * @version 1.0
 */
public class PrettyPrinter {

    /**
     * <p>
     * Pretty prints a given XML URI
     * </p>
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: " +
                "java com.oreilly.xml.PrettyPrinter [XML_URI]");
            return;
        }

        String filename = args[0];

        try {
            // Build the Document with SAX and Xerces, no validation
            Builder builder = new SAXBuilder();

            // Create the document (with validation)
            Document doc = builder.build(new File(filename));

            // Output the document, use standard formatter
            XMLOutputter fmt = new XMLOutputter();
            fmt.output(doc, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

