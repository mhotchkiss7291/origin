import java.io.*;
import java.util.*;

public class SWList {

	public void strip() {

    File fi = null;
    FileReader in = null;
    String dstring = null;
    try {
      fi = new File( "./swlist" );
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
		String buf = null;
		int i = 0;
		int j = 0;
		int count = 0;
		String[] sa = new String[ 50000 ];

		while( sst.hasMoreTokens() ) {

      buf = sst.nextToken();

			i = buf.indexOf( "," );
			j = buf.indexOf( ",", i+1 );
			i = buf.indexOf( ",", j+1 );
			if( !buf.substring( j+3, i-2 ).startsWith("DG")) {
				sa[ count ] = buf.substring( j+3, i-2 ) + "\n";
				//System.out.println("count = " + count + ": \"" + sa[ count ] + "\"");
				count++;
			}
		}

		File f = new File( "./swout" );
    FileOutputStream fos = null;

    try {
      fos = new FileOutputStream( f );
    } catch (IOException e) {
      e.printStackTrace();
    }

    j = 0;

    while( j < sa.length && sa[ j ] != null ) {
			
      byte[] buffer = sa[ j ].toLowerCase().getBytes(); 

			try {
				fos.write( buffer );
				j++;
			} catch (IOException e) {
				e.printStackTrace();
			}
    }
	}

	public static void main( String args[] ) {

		SWList sl = new SWList();
		sl.strip();

	}

}
