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
import java.io.IOException;

import helma.xmlrpc.WebServer;
import helma.xmlrpc.XmlRpc;

/**
 * <b><code>HelloServer</code></b> is a simple XML-RPC server
 *   that will make the <code>HelloHandler</code> class available
 *   for XML-RPC calls.
 *
 * @author Brett McLaughlin
 * @version 1.0
 */
public class HelloServer {

    /**
	 * <p>
	 * Start up the XML-RPC server and register a handler.
	 * </p>
	 */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println(
                "Usage: java HelloServer [port]");
            System.exit(-1);
        }

        try {
            // Use the Apache Xerces SAX Driver
            XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");

            // Start the server
            System.out.println("Starting XML-RPC Server...");
            WebServer server = new WebServer(Integer.parseInt(args[0]));

            // Register our handler class
            server.addHandler("hello", new HelloHandler());
            System.out.println(
                "Registered HelloHandler class to \"hello\"");

            System.out.println("Now accepting requests...");

        } catch (ClassNotFoundException e) {
            System.out.println("Could not locate SAX Driver");
        } catch (IOException e) {
            System.out.println("Could not start server: " +
                e.getMessage());
        }
    }

}
