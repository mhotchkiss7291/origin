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
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Import your vendor's DOM parser
import org.apache.xerces.parsers.DOMParser;

/**
 * <b><code>DOMParserDemo</code></b> will take an XML file and display
 *   the document using DOM.
 *
 * @author Brett McLaughlin
 * @version 1.0
 */
public class DOMParserDemo {

    /**
     * <p>
     * This parses the file, and then prints the document out
     *   using DOM.
     * </p>
     *
     * @param uri <code>String</code> URI of file to parse.
     */
    public void performDemo(String uri) {
        System.out.println("Parsing XML File: " + uri + "\n\n");

        // Instantiate your vendor's DOM parser implementation
        DOMParser parser = new DOMParser();
        try {
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setFeature("http://xml.org/sax/features/namespaces", false);
            parser.parse(uri);
            Document doc = parser.getDocument();

            // Print the document from the DOM tree and
            //   feed it an initial indentation of nothing
            printNode(doc, "");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in parsing: " + e.getMessage());
        }

    }

    /**
     * <p>
     * This will print a DOM <code>Node</code> and then recurse
     *   on its children.
     * </p>
     *
     * @param node <code>Node</code> object to print.
     * @param indent <code>String</code> spacing to insert
     *               before this <code>Node</code>
     */
    public void printNode(Node node, String indent)  {
        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                System.out.println("<xml version=\"1.0\">\n");
                // recurse on each child
                NodeList nodes = node.getChildNodes();
                if (nodes != null) {
                    for (int i=0; i<nodes.getLength(); i++) {
                        printNode(nodes.item(i), "");
                    }
                }
                break;

            case Node.ELEMENT_NODE:
                String name = node.getNodeName();
                System.out.print(indent + "<" + name);
                NamedNodeMap attributes = node.getAttributes();
                for (int i=0; i<attributes.getLength(); i++) {
                    Node current = attributes.item(i);
                    System.out.print(" " + current.getNodeName() +
                                     "=\"" + current.getNodeValue() +
                                     "\"");
                }
                System.out.println(">");

                // recurse on each child
                NodeList children = node.getChildNodes();
                if (children != null) {
                    for (int i=0; i<children.getLength(); i++) {
                        printNode(children.item(i), indent + "  ");
                    }
                }

                System.out.println(indent + "</" + name + ">");
                break;

            case Node.TEXT_NODE:
            case Node.CDATA_SECTION_NODE:
                System.out.print(node.getNodeValue());
                break;

            case Node.PROCESSING_INSTRUCTION_NODE:
                System.out.println("<?" + node.getNodeName() +
                                   " " + node.getNodeValue() +
                                   "?>");
                break;

            case Node.ENTITY_REFERENCE_NODE:
                System.out.print("&" + node.getNodeName() + ";");
                break;

            case Node.DOCUMENT_TYPE_NODE:
                DocumentType docType = (DocumentType)node;
                System.out.print("<!DOCTYPE " + docType.getName());
                if (docType.getPublicId() != null)  {
                    System.out.print(" PUBLIC \"" + docType.getPublicId() + "\" ");
                } else {
                    System.out.print(" SYSTEM ");
                }
                System.out.println("\"" + docType.getSystemId() + "\">");
                break;
        }

    }

    /**
     * <p>
     * This provides a command line entry point for this demo.
     * </p>
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java DOMParserDemo [XML URI]");
            System.exit(0);
        }

        String uri = args[0];

        DOMParserDemo parserDemo = new DOMParserDemo();
        parserDemo.performDemo(uri);
    }

}

