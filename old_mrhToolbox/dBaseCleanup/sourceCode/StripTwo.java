import java.io.*;
import java.util.*;

public class StripTwo {

	public String[] loadArray( String FileName ) {

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

		String[] sa = new String[ sst.countTokens() ];

		int i = 0;

		while( sst.hasMoreTokens() ) {
			sa[ i ] = sst.nextToken();
			i++;
		}

		return sa;

	}

	public void go() {

		String[] swlist = loadArray( "./finalout" );
		int swcount = swlist.length;
		String[] outlist = new String[ swcount ];

		int i = 0;

		while( i < swcount ) {
			outlist[ i ] = swlist[i].substring(2) + "\n";
			i++;
		}

		i = 0;

		File f = new File( "./stripped" );
    FileOutputStream fos = null;

    try {
      fos = new FileOutputStream( f );
    } catch (IOException e) {
      e.printStackTrace();
    }

    while( i < swcount ) {
 
      byte[] buffer = outlist[ i ].getBytes();

      try {
        fos.write( buffer );
        i++;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
	}

	public static void main( String args[] ) {
		StripTwo sl = new StripTwo();
		sl.go();
	}

}
