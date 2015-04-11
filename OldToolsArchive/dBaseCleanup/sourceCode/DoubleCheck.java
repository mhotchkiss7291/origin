import java.io.*;
import java.util.*;

public class DoubleCheck {

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

		String[] ldaplist = loadArray( "./ldapout" );
		String[] strippedlist = loadArray( "./stripped" );
		String[] foundlist = new String[ 1000 ];

		String buf = null;
		String buf2 = null;

		int i = 0;
		int j = 0;
		int k = 0;
		int ldapcount = ldaplist.length;
		int strippedcount = strippedlist.length;

		while( i < 1000 ) {
			foundlist[ i ] = null;
			i++;
		}

		i = 0;

		while( i < strippedcount ) {

			System.out.println("count = " + i + " of " + strippedcount );

			buf = strippedlist[i];

			while( j < ldapcount ) {

				buf2 = ldaplist[j];

				if( buf.equals( buf2 ) ) {
					foundlist[ k++ ] = strippedlist[i] + "\n";
					System.out.println("Found: j = " + j + ": i = " + 
								i + ": \"" + strippedlist[i] + "\"");
					break;
				}
				j++;
			}

			j = 0;
			i++;
		}

		File f = new File( "./doubleout" );
    FileOutputStream fos = null;

    try {
      fos = new FileOutputStream( f );
    } catch (IOException e) {
      e.printStackTrace();
    }

    j = 0;

    while( j < foundlist.length && foundlist[ j ] != null ) {
 
      byte[] buffer = foundlist[ j ].getBytes();

      try {
        fos.write( buffer );
        j++;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

	}

	public static void main( String args[] ) {
		DoubleCheck sl = new DoubleCheck();
		sl.go();
	}

}
