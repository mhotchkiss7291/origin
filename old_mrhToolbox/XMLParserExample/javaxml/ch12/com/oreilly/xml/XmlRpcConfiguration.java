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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.Builder;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;

/**
 * <b><code>XmlRpcConfiguration</code></b> is a utility class
 *   that will load configuration information for XML-RPC servers
 *   and clients to use.
 *
 * @author Brett McLaughlin
 * @version 1.0
 */
public class XmlRpcConfiguration {

    /** The stream to read the XML configuration from */
    private InputStream in;

    /** Port number server runs on */
    private int portNumber;

    /** Hostname server runs on */
    private String hostname;

    /** SAX Driver Class to load */
    private String driverClass;

    /** Handlers to register in XML-RPC server */
    private Hashtable handlers;

    /** JDOM Document tied to underlying XML */
    private Document doc;

    /**
     * <p>
     * This will set a filename to read configuration
     *   information from.
     * </p>
     *
     * @param filename <code>String</code> name of
     *                 XML configuration file.
     */
    public XmlRpcConfiguration(String filename)
        throws IOException {

        this(new FileInputStream(filename));
    }

    /**
     * <p>
     * This will set a filename to read configuration
     *   information from.
     * </p>
     *
     * @param in <code>InputStream</code> to read
     *           configuration information from.
     */
    public XmlRpcConfiguration(InputStream in)
        throws IOException {

        this.in = in;
        portNumber = 0;
        hostname = "";
        handlers = new Hashtable();

        // Parse the XML configuration information
        parseConfiguration();
    }

    /**
     * <p>
     * This returns the port number the server listens on.
     * </p>
     *
     * @return <code>int</code> - number of server port.
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * <p>
     * This will set the port number to listen to.
     * </p>
     *
     * @param portNumber <code>int</code> port to listen to.
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * <p>
     * This returns the hostname the server listens on.
     * </p>
     *
     * @return <code>String</code> - hostname of server.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * <p>
     * This will set the hostname for the server to listen to.
     * </p>
     *
     * @param hostname <code>String</code> name of server's host.
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * <p>
     * This returns the SAX driver class to load.
     * </p>
     *
     * @return <code>String</code> - name of SAX driver class.
     */
    public String getDriverClass() {
        return driverClass;
    }

    /**
     * <p>
     * This will set the driver class for parsing.
     * </p>
     *
     * @param driverClass <code>String</code> name of parser class.
     */
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    /**
     * <p>
     * This returns the handlers the server should register.
     * </p>
     *
     * @return <code>Hashtable</code> of handlers.
     */
    public Hashtable getHandlers() {
        return handlers;
    }

    /**
     * <p>
     * This will set the handlers to register.
     * </p>
     *
     * @param handlers <code>Hashtable</code> of handler to register.
     */
    public void setHandlers(Hashtable handlers) {
        this.handlers = handlers;
    }

    /**
     * <p>
     * Parse the XML configuration information and
     *   make it available to clients.
     * </p>
     *
     * @throws <code>IOException</code> when errors occur.
     */
    private void parseConfiguration() throws IOException {

        try {
            // Request DOM Implementation and Xerces Parser
            Builder builder =
                new DOMBuilder("org.jdom.adapters.XercesDOMAdapter");

            // Get the Configuration Document, with validation
            doc = builder.build(in);

            // Get the root element
            Element root = doc.getRootElement();

            // Get the JavaXML namespace
            Namespace ns = Namespace.getNamespace("JavaXML",
                           "http://www.oreilly.com/catalog/javaxml/");

            // Load the hostname, port, and handler class
            hostname = root.getChild("hostname", ns).getContent();
            driverClass = root.getChild("parserClass", ns).getContent();
            portNumber =
                Integer.parseInt(root.getChild("port", ns).getContent());

            // Get the handlers
            List handlerElements =
                root.getChild("xmlrpc-server", ns)
                    .getChild("handlers", ns)
                    .getChildren("handler", ns);

            Iterator i = handlerElements.iterator();
            while (i.hasNext()) {
                Element current = (Element)i.next();
                handlers.put(current.getChild("identifier", ns).getContent(),
                             current.getChild("class", ns).getContent());
            }
        } catch (JDOMException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * <p>
     * This will save the current state out to the XML-RPC configuration
     *   file.
     * </p>
     *
     * @throws <code>IOException</code> - when errors occur in saving.
     */
    public synchronized void saveConfiguration(String filename)
        throws IOException {

        saveConfigurationFromScratch(new FileOutputStream(filename));
    }

    /**
     * <p>
     * This will save the current state out to the specified
     *   <code>OutputStream</code>.
     * </p>
     *
     * @throws <code>IOException</code> - when errors occur in saving.
     */
    public synchronized void saveConfiguration(OutputStream out)
        throws IOException {

        try {
            Element root = doc.getRootElement();

            // Get the JavaXML namespace
            Namespace ns = Namespace.getNamespace("JavaXML",
                           "http://www.oreilly.com/catalog/javaxml/");

            // Update the hostname
            root.getChild("hostname", ns)
                .setContent(hostname);

            // Update the SAX driver class
            root.getChild("parserClass", ns)
                .setContent(driverClass);

            // Update the port number
            root.getChild("port", ns)
                .setContent(portNumber + "");

            // Easier to remove and re-add handlers
            Element handlersElement =
                root.getChild("xmlrpc-server", ns)
                    .getChild("handlers", ns);
            handlersElement.removeChildren("handler", ns);

            // Add new handlers
            Enumeration handlerIDs = handlers.keys();
            while (handlerIDs.hasMoreElements()) {
                String handlerID =
                    (String)handlerIDs.nextElement();

                // Ensure we don't register any blank string
                if (handlerID.trim().equals("")) {
                    continue;
                }

                String handlerClass =
                    (String)handlers.get(handlerID);

                handlersElement.addChild(
                    new Element("handler", ns)
                        .addChild(
                            new Element("identifier", ns)
                                .setContent(handlerID))
                        .addChild(
                            new Element("class", ns)
                                .setContent(handlerClass))
                        );
            }

            // Output the document, use standard formatter
            XMLOutputter fmt = new XMLOutputter();
            fmt.output(doc, out);

        } catch (JDOMException e) {
            // Log an error
            throw new IOException(e.getMessage());
        }
    }

    /**
     * <p>
     * This will save the current state out to the specified
     *   <code>OutputStream</code>.
     * </p>
     *
     * @throws <code>IOException</code> - when errors occur in saving.
     */
    public synchronized void saveConfigurationFromScratch(OutputStream out)
        throws IOException {

        // Get the JavaXML namespace
        Namespace ns = Namespace.getNamespace("JavaXML",
                       "http://www.oreilly.com/catalog/javaxml/");

        // Create the root element
        Element root = new Element("xmlrpc-config", ns);
        Document doc = new Document(root);
        doc.setDocType(new DocType("JavaXML:xmlrpc-config",
                                   "DTD/XmlRpc.dtd"));

        root.addChild(new Element("hostname", ns)
                .setContent(hostname))
            .addChild(new Element("port", ns)
                .addAttribute("type", "unprotected")
                .setContent(portNumber + ""))
            .addChild(new Element("parserClass", ns)
                .setContent(driverClass));

        Element handlersElement = new Element("handlers", ns);
        Enumeration e = handlers.keys();
        while (e.hasMoreElements()) {
            String handlerID = (String)e.nextElement();
            String handlerClass = (String)handlers.get(handlerID);

            handlersElement.addChild(new Element("handler", ns)
                .addChild(new Element("identifier", ns)
                    .setContent(handlerID))
                .addChild(new Element("class", ns)
                    .setContent(handlerClass))
                );
        }

        root.addChild(new Element("xmlrpc-server", ns)
                .addChild(handlersElement));

        // Output the document, use standard formatter
        XMLOutputter fmt = new XMLOutputter();
        fmt.output(doc, out);
    }

}