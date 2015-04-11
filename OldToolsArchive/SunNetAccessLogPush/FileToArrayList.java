import java.util.*;
import java.io.*;

public class FileToArrayList {

	public ArrayList getArrayList( String FileName ) {

    File fi = null;
    FileReader in = null;
    String dstring = null;
    try {
      fi = new File( FileName );
      in = new FileReader(fi);
      int size = (int)fi.length();
      char[] data = new char[size];
      int chars_read = 0;

      // Read in the record
      while(chars_read < size) {
        chars_read += in.read(data, chars_read, size - chars_read);
      }
      dstring = new String( data );
    } catch (IOException e ) {
      System.out.println("Could not open input file");
      System.exit(0);
    }

    StringTokenizer sst =
          new StringTokenizer( dstring, "\n", false);

		ArrayList al = new ArrayList();

    while( sst.hasMoreTokens() ) {
      al.add( sst.nextToken() + "\n" );
    }

    return al;

  }
}
