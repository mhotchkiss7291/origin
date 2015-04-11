import java.io.IOException;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.apache.xerces.parsers.SAXParser;

public class SAXParserDemo {

	public void performDemo(String uri) {

		System.out.println("Parsing XML File: " + uri + "\n\n");

		try {	
			XMLReader parser = new SAXParser();
			parser.parse( uri );
		} catch (IOException e) {
			System.out.println("Error reading URI: " + e.getMessage());
		} catch (SAXException) {
			System.out.println("Error parsing: " + e.getMessage());
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
