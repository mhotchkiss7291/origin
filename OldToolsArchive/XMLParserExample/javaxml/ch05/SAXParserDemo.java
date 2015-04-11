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

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <b><code>SAXParserDemo</code></b> will take an XML file and parse it using SAX, 
 *   displaying the callbacks in the parsing lifecycle.
 * 
 * @author Brett McLaughlin
 * @version 1.0
 */
public class SAXParserDemo {
    
    /**
     * <p>
     * This parses the file, using registered SAX handlers, and output 
     *   the events in the parsing process cycle.
     * </p>
     *
     * @param uri <code>String</code> URI of file to parse.
     */
    public void performDemo(String uri) {
        System.out.println("Parsing XML File: " + uri + "\n\n");
        
        // Get instances of our handlers        
        ContentHandler contentHandler = new MyContentHandler();
        ErrorHandler errorHandler = new MyErrorHandler();        
        
        try {
            // Instantiate a parser
            XMLReader parser = 
                XMLReaderFactory.createXMLReader(
                    "org.apache.xerces.parsers.SAXParser");
                
            // Register the content handler
            parser.setContentHandler(contentHandler);
            
            // Register the error handler
            parser.setErrorHandler(errorHandler); 

            // Turn on validation
            parser.setFeature("http://xml.org/sax/features/validation",
                              true);           

            // Turn off namespace awareness
            parser.setFeature("http://xml.org/sax/features/namespaces",
                              false);                     
                
            // Parse the document
            parser.parse(uri);
            
        } catch (IOException e) {
            System.out.println("Error reading URI: " + e.getMessage());
        } catch (SAXException e) {
            System.out.println("Error in parsing: " + e.getMessage());
        }

            
    }
    
    /**
     * <p>
     * This provides a command line entry point for this demo.
     * </p>
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java SAXParserDemo [XML URI]");
            System.exit(0);
        }
        
        String uri = args[0];
    
        SAXParserDemo parserDemo = new SAXParserDemo();
        parserDemo.performDemo(uri);
    }
    
}

/**
 * <b><code>MyContentHandler</code></b> implements the SAX 
 *   <code>ContentHandler</code> interface and defines callback
 *   behavior for the SAX callbacks associated with an XML
 *   document's content.
 */
class MyContentHandler implements ContentHandler {

    /** Hold onto the locator for location information */
    private Locator locator;
    
    /**
     * <p>
     * Provide reference to <code>Locator</code> which provides
     *   information about where in a document callbacks occur.
     * </p>
     *
     * @param locator <code>Locator</code> object tied to callback
     *                process
     */
    public void setDocumentLocator(Locator locator) {
        System.out.println("    * setDocumentLocator() called");
        // We save this for later use if desired.
        this.locator = locator;
    }
    
    /**
     * <p>
     * This indicates the start of a Document parse - this precedes
     *   all callbacks in all SAX Handlers with the sole exception
     *   of <code>{@link #setDocumentLocator}</code>.
     * </p>
     *
     * @throws <code>SAXException</code> when things go wrong
     */
    public void startDocument() throws SAXException {
        System.out.println("Parsing begins...");
    }

    /**
     * <p>
     * This indicates the end of a Document parse - this occurs after
     *   all callbacks in all SAX Handlers.</code>.
     * </p>
     *
     * @throws <code>SAXException</code> when things go wrong
     */    
    public void endDocument() throws SAXException {
        System.out.println("...Parsing ends.");
    }
    
    /**
     * <p>
     * This will indicate that a processing instruction (other than
     *   the XML declaration) has been encountered.
     * </p>
     *
     * @param target <code>String</code> target of PI
     * @param data <code>String</code containing all data sent to the PI.
     *             This typically looks like one or more attribute value
     *             pairs.
     * @throws <code>SAXException</code> when things go wrong
     */         
    public void processingInstruction(String target, String data)
        throws SAXException {
            
        System.out.println("PI: Target:" + target + " and Data:" + data);
    }
    
    /**
     * <p>
     * This will indicate the beginning of an XML Namespace prefix 
     *   mapping.  Although this typically occur within the root element 
     *   of an XML document, it can occur at any point within the 
     *   document.  Note that a prefix mapping on an element triggers 
     *   this callback <i>before</i> the callback for the actual element 
     *   itself (<code>{@link #startElement}</code>) occurs.
     * </p>
     *
     * @param prefix <code>String</code> prefix used for the namespace 
     *               being reported
     * @param uri <code>String</code> URI for the namespace 
     *            being reported
     * @throws <code>SAXException</code> when things go wrong
     */                  
    public void startPrefixMapping(String prefix, String uri) {
        System.out.println("Mapping starts for prefix " + prefix + 
                           " mapped to URI " + uri);
    }

    /**
     * <p>
     * This indicates the end of a prefix mapping, when the namespace 
     *   reported in a <code>{@link #startPrefixMapping}</code> callback 
     *   is no longer available.
     * </p>
     *
     * @param prefix <code>String</code> of namespace being reported
     * @throws <code>SAXException</code> when things go wrong
     */      
    public void endPrefixMapping(String prefix) {
        System.out.println("Mapping ends for prefix " + prefix);
    }
    
    /**
     * <p>
     * This reports the occurrence of an actual element.  It will include
     *   the element's attributes, with the exception of XML vocabulary 
     *   specific attributes, such as 
     *   <code>xmlns:[namespace prefix]</code> and
     *   <code>xsi:schemaLocation</code>.
     * </p>
     *
     * @param namespaceURI <code>String</code> namespace URI this element
     *                     is associated with, or an empty 
     *                     <code>String</code>
     * @param localName <code>String</code> name of element (with no 
     *                  namespace prefix, if one is present)
     * @param rawName <code>String</code> XML 1.0 version of element name:
     *                [namespace prefix]:[localName]
     * @param atts <code>Attributes</code> list for this element
     * @throws <code>SAXException</code> when things go wrong
     */     
    public void startElement(String namespaceURI, String localName,
                             String rawName, Attributes atts)
        throws SAXException {
            
        System.out.print("startElement: " + localName);
        if (!namespaceURI.equals("")) {
            System.out.println(" in namespace " + namespaceURI + 
                               " (" + rawName + ")");
        } else {
            System.out.println(" has no associated namespace");
        }

        for (int i=0; i<atts.getLength(); i++)
            System.out.println("  Attribute: " + atts.getLocalName(i) +
                               "=" + atts.getValue(i));          
    }
    
    /** 
     * <p>
     * Indicates the end of an element 
     *   (<code>&lt;/[element name]&gt;</code>) is reached.  Note that 
     *   the parser does not distinguish between empty
     *   elements and non-empty elements, so this will occur uniformly.
     * </p>
     *
     * @param namespaceURI <code>String</code> URI of namespace this 
     *                     element is associated with
     * @param localName <code>String</code> name of element without prefix
     * @param rawName <code>String</code> name of element in XML 1.0 form
     * @throws <code>SAXException</code> when things go wrong
     */        
    public void endElement(String namespaceURI, String localName, 
                           String rawName) 
        throws SAXException {
          
        System.out.println("endElement: " + localName + "\n");
    }

    /**
     * <p>
     * This will report character data (within an element).
     * </p>
     *
     * @param ch <code>char[]</code> character array with character data
     * @param start <code>int</code> index in array where data starts.
     * @param end <code>int</code> index in array where data ends.
     * @throws <code>SAXException</code> when things go wrong
     */         
    public void characters(char[] ch, int start, int end) 
        throws SAXException {
            
        String s = new String(ch, start, end);
        System.out.println("characters: " + s);
    }

	/**
     * <p>
     * This will report whitespace that can be ignored in the 
     *   originating document.  This is typically only invoked when
     *   validation is ocurring in the parsing process.
     * </p>
     *
     * @param ch <code>char[]</code> character array with character data
     * @param start <code>int</code> index in array where data starts.
     * @param end <code>int</code> index in array where data ends.
     * @throws <code>SAXException</code> when things go wrong     
     */
    public void ignorableWhitespace(char[] ch, int start, int end)
        throws SAXException {
            
        String s = new String(ch, start, end);
        System.out.println("ignorableWhitespace: [" + s + "]");
    }

	/**
     * <p>
     * This will report an entity that is skipped by the parser.  This
     *   should only occur for non-validating parsers, and then is still
     *   implementation-dependent behavior.
     * </p>
     *
     * @param name <code>String</code> name of entity being skipped
     * @throws <code>SAXException</code> when things go wrong     
     */
    public void skippedEntity(String name) throws SAXException {
        System.out.println("Skipping entity " + name);
    }

}

/**
 * <b><code>MyErrorHandler</code></b> implements the SAX 
 *   <code>ErrorHandler</code> interface and defines callback
 *   behavior for the SAX callbacks associated with an XML
 *   document's errors.
 */
class MyErrorHandler implements ErrorHandler  {

    /**
     * <p>
     * This will report a warning that has occurred; this indicates
     *   that while no XML rules were "broken", something appears
     *   to be incorrect or missing.
     * </p>
     *
     * @param exception <code>SAXParseException</code> that occurred.
     * @throws <code>SAXException</code> when things go wrong     
     */     
    public void warning(SAXParseException exception)
        throws SAXException {
            
        System.out.println("**Parsing Warning**\n" +
                           "  Line:    " + 
                              exception.getLineNumber() + "\n" +
                           "  URI:     " + 
                              exception.getSystemId() + "\n" +
                           "  Message: " + 
                              exception.getMessage());        
        throw new SAXException("Warning encountered");
    }

    /**
     * <p>
     * This will report an error that has occurred; this indicates
     *   that a rule was broken, typically in validation, but that
     *   parsing can reasonably continue.
     * </p>
     *
     * @param exception <code>SAXParseException</code> that occurred.
     * @throws <code>SAXException</code> when things go wrong     
     */  
    public void error(SAXParseException exception)
        throws SAXException {
            
        System.out.println("**Parsing Error**\n" +
                           "  Line:    " + 
                              exception.getLineNumber() + "\n" +
                           "  URI:     " + 
                              exception.getSystemId() + "\n" +
                           "  Message: " + 
                              exception.getMessage());
        throw new SAXException("Error encountered");
    }

    /**
     * <p>
     * This will report a fatal error that has occurred; this indicates
     *   that a rule has been broken that makes continued parsing either
     *   impossible or an almost certain waste of time.
     * </p>
     *
     * @param exception <code>SAXParseException</code> that occurred.
     * @throws <code>SAXException</code> when things go wrong     
     */  
    public void fatalError(SAXParseException exception)
        throws SAXException {
            
        System.out.println("**Parsing Fatal Error**\n" +
                           "  Line:    " + 
                              exception.getLineNumber() + "\n" +
                           "  URI:     " + 
                              exception.getSystemId() + "\n" +
                           "  Message: " + 
                              exception.getMessage());        
        throw new SAXException("Fatal Error encountered");        
    }
	
}

