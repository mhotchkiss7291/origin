import java.io.*;
import java.util.*;

public class LDAPList {

	public void strip() {

    File fi = null;
    FileReader in = null;
    String dstring = null;
    try {
      fi = new File( "./ldap.dat" );
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
		int count = 0;
		String[] sa = new String[ 60000 ];

		while( sst.hasMoreTokens() ) {

      buf = sst.nextToken();
			i = buf.indexOf( "|" );
			sa[ count ] = buf.substring( 0, i ) + "\n";
			//System.out.println("count = " + count + ": \"" + sa[ count ] + "\"");
			count++;

		}

		File f = new File( "./ldapout" );
    FileOutputStream fos = null;

    try {
      fos = new FileOutputStream( f );
    } catch (IOException e) {
      e.printStackTrace();
    }

    int j = 0;

    while( j < sa.length && sa[ j ] != null ) {
			
      byte[] buffer = sa[ j ].getBytes(); 

			try {
				fos.write( buffer );
				j++;
			} catch (IOException e) {
				e.printStackTrace();
			}
    }
	}

	public static void main( String args[] ) {

		LDAPList sl = new LDAPList();
		sl.strip();

	}

}
