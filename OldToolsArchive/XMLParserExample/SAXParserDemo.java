import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import org.apache.xerces.parsers.SAXParser;


public class SAXParserDemo {

	public void performDemo(String uri) {

		System.out.println("Parsing XML File: " + uri + "\n\n");

		ContentHandler contentHandler = new MyContentHandler();
		ErrorHandler errorHandler = new MyErrorHandler();

		try {	
			XMLReader parser = new SAXParser();
			parser.setContentHandler( contentHandler );
			parser.setErrorHandler( errorHandler );
			parser.parse( uri );
		} catch (IOException e) {
			System.out.println("Error reading URI: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Error parsing: " + e.getMessage());
		}

	}


	class MyContentHandler implements ContentHandler {

		private Locator locator;

		public void setDocumentLocator( Locator locator ) {
			//System.out.println("    * setDocumentLocator() called");
			this.locator = locator;
		}

		public void startDocument() throws SAXException {
			//System.out.println("Parsing begins...");
		}

		public void endDocument() throws SAXException {
			//System.out.println("Parsing ends...");
		}

		public void processingInstruction( String target, String data ) 
				throws SAXException {
			//System.out.println("PI: Target: " + target + " and Data: " + data);
		}

		public void startPrefixMapping( String prefix, String uri ) 
				throws SAXException {
			//System.out.println("Mapping starts for prefix " + prefix +
					" mapped to URI " + uri);
		}

		public void endPrefixMapping( String prefix ) 
				throws SAXException {
			//System.out.println("Mapping ends for prefix " + prefix);
		}

		public void startElement( 
						String namespaceURI,
						String localName,
						String rawName,
						Attributes atts) 
				throws SAXException {

			System.out.println("startElement: " + localName);

			if( !namespaceURI.equals("")) {
				System.out.println(" in namespace " + namespaceURI + 
													 " (" + rawName + ")");
			} else {
				System.out.println(" has no associated namespace");
			}

			for( int i = 0; i < atts.getLength(); i++ ) {
				System.out.println("  Attribute: " + atts.getLocalName(i) +
													 "=" + atts.getValue(i));
			}
		}

		public void endElement( 
						String namespaceURI,
						String localName,
						String rawName)
				throws SAXException {

			//System.out.println("endElement: " + localName + "\n");

		}

		public void characters( char[] ch, int start, int length)
				throws SAXException {

			String s = new String( ch, start, length );
			System.out.println("characters: " + s );
		}

		public void ignorableWhitespace( char[] ch, int start, int length)
				throws SAXException {

			String s = new String(ch, start, length);
			//System.out.println("ignorableWhitespace: [" + s + "]");

		}

		public void skippedEntity( String name ) throws SAXException {

			//System.out.println("Skipping entity " + name);

		}

	}

	class MyErrorHandler implements ErrorHandler {

		public void warning(SAXParseException exception)
				throws SAXException {

			System.out.println(
					"**Parsing Warning**\n" +
					"  Line:   " + exception.getLineNumber() + "\n" +
					"  URI:    " + exception.getSystemId() + "\n" +
					"  Message: " + exception.getMessage() );

			throw new SAXException("Warning encountered");
					
		}

		public void error(SAXParseException exception)
				throws SAXException {

			System.out.println(
					"**Parsing Error**\n" +
					"  Line:   " + exception.getLineNumber() + "\n" +
					"  URI:    " + exception.getSystemId() + "\n" +
					"  Message: " + exception.getMessage() );

			throw new SAXException("Error encountered");
			
		}

		public void fatalError(SAXParseException exception)
				throws SAXException {

			System.out.println(
					"**Parsing Fatal Error**\n" +
					"  Line:   " + exception.getLineNumber() + "\n" +
					"  URI:    " + exception.getSystemId() + "\n" +
					"  Message: " + exception.getMessage() );

			throw new SAXException("Fatal Error encountered");
		}
	
	}

	public static void main( String args[]) {

		if( args.length != 1 ) {
			System.out.println("Usage: java SAXParserDemo [XML, URI]");
		}

		String uri = args[0];
		SAXParserDemo parserDemo = new SAXParserDemo();
		parserDemo.performDemo( uri );

	}

}
