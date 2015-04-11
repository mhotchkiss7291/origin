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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import helma.xmlrpc.XmlRpc;
import helma.xmlrpc.WebServer;

/**
 * <b><code>LightweightXmlRpcServer</code></b> is a utility class
 *   that will start an XML-RPC server listening for HTTP requests
 *   and register a set of handlers, defined in a configuration file.
 * 
 * @author Brett McLaughlin
 * @version 1.0
 */
public class LightweightXmlRpcServer {
  
    /** The XML-RPC server utility class */
    private WebServer server;
    
    /** Port number to listen on */
    private int port;
    
    /** Configuration file to use */
    private String configFile;
    
    /**
     * <p>
     * This will store the requested port and configuration file
     *   for the server to use.
     * </p>
     *
     * @param port <code>int</code> number of port to listen to
     * @param configFile <code>String</code> filename to read for
     *                   configuration information.
     */
    public LightweightXmlRpcServer(int port, String configFile) {
        this.port = port;
        this.configFile = configFile;
    }
    
    /**
     * <p>
     * This will start up the server.
     * </p>
     *
     * @throws <code>IOException</code> when problems occur.
     */
    public void start() throws IOException {
        try {
            // Use Apache Xerces SAX Parser
            XmlRpc.setDriver("org.apache.xerces.parsers.SAXParser");

            System.out.println("Starting up XML-RPC Server...");
            server = new WebServer(port);                      
            
            // Register handlers
            registerHandlers(getHandlers());
            
        } catch (ClassNotFoundException e) {
            throw new IOException("Error loading SAX parser: " + 
                e.getMessage());
        }         
    } 

    /**
     * <p>
     * This is a method that parses the configuration file
     *   (in a very simplistic manner) and reads the handler
     *   definitions supplied.
     * </p>
     * 
     * @return <code>Hashtable</code> - class id/class pairs.
     * @throws <code>IOException</code> - when errors occur in 
     *                                    reading/parsing the file.
     */
    private Hashtable getHandlers() throws IOException {

        Hashtable handlers = new Hashtable();
        
        BufferedReader reader = 
            new BufferedReader(new FileReader(configFile));
        String line = null;
        
        while ((line = reader.readLine()) != null) {
            // Syntax is "handlerName, handlerClass"
            int comma;
            
            // Skip comments
            if (line.startsWith("#")) {
                continue;
            }
            
            // Skip empty or useless lines            
            if ((comma = line.indexOf(",")) < 2) {
                continue;
            }
            
            // Add the handler name and the handler class
            handlers.put(line.substring(0, comma), 
                         line.substring(comma+1));
        }
        
        return handlers;        
    }

    /**
     * <p>
     * This will register the handlers supplied in the XML-RPC
     *   server (typically from <code>{@link #getHandlers()}</code>.
     * </p>
     *
     * @param handlers <code>Hashtable</code> of handlers to register.
     */ 
    private void registerHandlers(Hashtable handlers) {        
        Enumeration handlerNames = handlers.keys();
        
        // Loop through the requested handlers
        while (handlerNames.hasMoreElements()) {
            String handlerName = (String)handlerNames.nextElement();
            String handlerClass = (String)handlers.get(handlerName);
            
            // Add this handler to the server
            try {
                server.addHandler(handlerName, 
                    Class.forName(handlerClass).newInstance());
                
                System.out.println("Registered handler " + handlerName +
                                   " to class " + handlerClass);
            } catch (Exception e) {
                System.out.println("Could not register handler " + 
                                   handlerName + " with class " + 
                                   handlerClass);
            }
        }
    }  
  
    /**
     * <p>
     * Provide a static entry point.
     * </p>
     */
    public static void main(String[] args) {
      
        if (args.length < 2) {
            System.out.println(
                "Usage: " +
                "java com.oreilly.xml.LightweightXmlRpcServer " +
                "[port] [configFile]");
            System.exit(-1);
        }
        
        LightweightXmlRpcServer server =
            new LightweightXmlRpcServer(Integer.parseInt(args[0]),
                                        args[1]);   

        try {
            // Start the server
            server.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }                               
    }

}