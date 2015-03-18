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
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.xml.XmlRpcConfiguration;

/**
 * <b><code>XmlRpcConfigurationServlet</code></b> is an
 *   administration tool that allows configuration changes
 *   to be saved to the XML configuration file.
 *
 * @author Brett McLaughlin
 * @version 1.0
 */
public class XmlRpcConfigurationServlet extends HttpServlet {

    /** Store the XML-RPC configuration file as a constant */
    private static final String CONFIG_FILENAME =
        "/usr/local/projects/javaxml/xmlrpc/xmlrpc.xml";

    /**
     * Point action back at this servlet (and the
     *   <code>{@link #doPost()}</code> method).
     *   In Servlet API 2.1 or 2.2, this can be done programmatically,
     *   but this example allows this to work in Servlet 2.0 as well
     */
    private static final String FORM_ACTION =
        "/javaxml/servlet/XmlRpcConfigurationServlet";

    /** Configuration object to work with */
    XmlRpcConfiguration config;

    /**
     * <p>
     * GET requests are received when the client wants to see the current
     *   configuration information.  This provides a view-only look at the
     *   data.  The generated HTML form then submits back to this servlet
     *   through POST, which causes the <code>{@link #doPost}</code> method
     *   to be invoked.
     * </p>
     */
    public void doGet(HttpServletRequest req,
                      HttpServletResponse res)
        throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        // Load the configuration information with our utility class
        config = new XmlRpcConfiguration(CONFIG_FILENAME);

        // Output HTML user interface
        out.println("<html><head>");
        out.println("<title>XML-RPC Configurations</title>");
        out.println("</head><body>");
        out.println("<h2 align=\"center\">XML-RPC Configuration</h2>");
        out.println("<form action=\"" + FORM_ACTION + "\" " +
                    "method=\"POST\">");
        out.println("<b>Hostname:</b> ");
        out.println("<input type=\"text\" " +
                    "name=\"hostname\" " +
                    "value=\"" + config.getHostname() +
                    "\" />");
        //out.println("<br />");
        out.println("&nbsp;&nbsp;&nbsp;&nbsp;");
        out.println("<b>Port Number:</b> ");
        out.println("<input type=\"text\" " +
                    "name=\"port\" " +
                    "value=\"" + config.getPortNumber() +
                    "\" />");
        out.println("<br />");
        out.println("<b>SAX Driver Class:</b> ");
        out.println("<input type=\"text\" " +
                    "name=\"driverClass\" size=\"50\"" +
                    "value=\"" + config.getDriverClass() +
                    "\" />");
        out.println("<br />");
        out.println("<br />");
        out.println("<h3 align=\"center\">XML-RPC handlers</h3>");

        // Display current handlers
        Hashtable handlers = config.getHandlers();
        Enumeration keys = handlers.keys();
        int index = 0;
        while (keys.hasMoreElements()) {
            String handlerID =
                (String)keys.nextElement();
            String handlerClass =
                (String)handlers.get(handlerID);
            out.println("<b>Identifier:</b> ");
            out.println("<input type=\"text\" " +
                        "value=\"" + handlerID + "\" " +
                        "name=\"handlerID\" />  ");
            out.println("<b>Class:</b> ");
            out.println("<input type=\"text\" " +
                        "value=\"" + handlerClass + "\" " +
                        "size=\"30\" " +
                        "name=\"handlerClass\" />  ");
            out.println("<br />");
            index++;
        }

        // Display empty boxes for additional handlers
        for (int i=0; i<3; i++) {
            out.println("<b>Identifier:</b> ");
            out.println("<input type=\"text\" " +
                        "name=\"handlerID\" />  ");
            out.println("<b>Class:</b> ");
            out.println("<input type=\"text\" " +
                        "size=\"30\" " +
                        "name=\"handlerClass\" />  ");
            out.println("<br />");
            index++;
        }

        out.println("<br /><center>");
        out.println("<input type=\"submit\" value=\"Save Changes\" />");
        out.println("</center>");
        out.println("</form></body></html>");

        out.close();
    }

    /**
     * <p>
     * This method receives requests for modification of the
     *   XML-RPC configuration information, all from the
     *   <code>{@link #doGet}</code> method.  This will again
     *   use the utility class to update the configuration
     *   file, letting the <code>{@link XmlRpcConfiguration}</code>
     *   object handle the actual writing to a file.
     * </p>
     */
    public void doPost(HttpServletRequest req,
                      HttpServletResponse res)
        throws ServletException, IOException {

        // Update the configuration information
        if (config == null) {
            config = new XmlRpcConfiguration(CONFIG_FILENAME);
        }

        // Save the hostname
        String hostname =
          req.getParameterValues("hostname")[0];
        if ((hostname != null) && (!hostname.equals(""))) {
          config.setHostname(hostname);
        }

        // Save the port number
        int portNumber;
        try {
          portNumber =
            Integer.parseInt(
              req.getParameterValues("port")[0]);
        } catch (Exception e) {
          portNumber = 0;
        }
        if (portNumber > 0) {
          config.setPortNumber(portNumber);
        }

        // Save the SAX driver class
        String driverClass =
          req.getParameterValues("driverClass")[0];
        if ((driverClass != null) && (!driverClass.equals(""))) {
          config.setDriverClass(driverClass);
        }

        // Save the handlers
        String[] handlerIDs =
          req.getParameterValues("handlerID");
        String[] handlerClasses =
          req.getParameterValues("handlerClass");
        Hashtable handlers = new Hashtable();
        for (int i=0; i<handlerIDs.length; i++) {
          handlers.put(handlerIDs[i], handlerClasses[i]);
        }
        config.setHandlers(handlers);

        try {
        // Request the changes be written to the configuration store
        config.saveConfiguration(CONFIG_FILENAME);
        } catch (IOException e) {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println(e.getMessage());
        return;
        }

        // Output a confirmation message
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("Changes saved <br />");
        out.println("<a href=\"" + FORM_ACTION +
                    "\">Return to Configuration Administration" +
                    "</a>");
        out.close();

    }

}