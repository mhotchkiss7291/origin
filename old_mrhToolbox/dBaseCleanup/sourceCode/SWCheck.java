import java.io.*;
import java.util.*;

public class SWCheck {

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

		String[] swlist = loadArray( "./swout" );
		String[] ldaplist = loadArray( "./ldapout" );
		String[] deletelist = new String[ 60000 ];
		String[] questionlist = new String[ 1000 ];

		String buf = null;
		String buf2 = null;

		int i = 0;
		int j = 0;
		int k = 0;
		int m = 0;
		int swcount = swlist.length;
		int ldapcount = ldaplist.length;
		int endcount = 0;
		int bookmark = 0;
		boolean foundflag = false;
		boolean swflagged = false;
		boolean ldapflagged = false;

		while( i < 60000 ) {
			deletelist[ i ] = null;
			i++;
		}

		i = 0;

		while( i < 1000 ) {
			questionlist[ i ] = null;
			i++;
		}

		i = 0;

		while( i < swcount ) {

			System.out.println("count = " + i + " of " + swcount );

			if( (swlist[i].charAt(2) == 't') || 
						(swlist[i].charAt(2) == 'c')) {
				buf = swlist[i].substring(3);
				swflagged = true;
			} else {
				buf = swlist[i].substring(2);
			}

			while( j < ldapcount ) {

				if( (ldaplist[j].charAt(0) == 't') || 
							(ldaplist[j].charAt(0) == 'c')) {
					buf2 = ldaplist[j].substring(1);
					ldapflagged = true;
				} else {
					buf2 = ldaplist[j];
				}

				//if( i == 8835 ) {

				//	System.out.println( j + ": \"" + buf2 + "\"");

				//}

				if( buf.equals( buf2 ) ) {
					foundflag = true;
					//deletelist[ k++ ] = swlist[i] + "\n";
					//System.out.println("Found: j = " + j + ": i = " + 
					//			i + ": \"" + swlist[i] + "\"");
					break;
				}
				j++;
			}

			//If Sun ID is the same AND one or the other prefix is different
			// send it to the questionable list 
			if( (foundflag == true) &&  
							(swflagged == true || ldapflagged == true )) {
				if( !(swlist[i].charAt(2) == ldaplist[j].charAt(0)) ) {
					questionlist[ m++ ] = swlist[i] + " : " + ldaplist[j] + "\n";
				}
			}

			if( !foundflag ) {
				if( buf.indexOf(" ") >= 0 ) {
					questionlist[ m++ ] = "Space Found in: " + swlist[i] + "\n";
				} else {
					deletelist[ k++ ] = swlist[i] + "\n";
					//System.out.println("Not Found: j = " + j + ": i = " + 
					//				i + ": \"" + swlist[i] + "\"");
				}
				//j = 0;
				ldapflagged = false;
			}
			j = 0;
			foundflag = false;
			swflagged = false;
			i++;
		}

		File f = new File( "./checkout" );
    FileOutputStream fos = null;

    try {
      fos = new FileOutputStream( f );
    } catch (IOException e) {
      e.printStackTrace();
    }

    j = 0;

    while( j < deletelist.length && deletelist[ j ] != null ) {
 
      byte[] buffer = deletelist[ j ].getBytes();

      try {
        fos.write( buffer );
        j++;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

		File f2 = new File( "./questionable" );
    FileOutputStream fos2 = null;

    try {
      fos2 = new FileOutputStream( f2 );
    } catch (IOException e) {
      e.printStackTrace();
    }

    j = 0;

    while( j < questionlist.length && questionlist[ j ] != null ) {
 
      byte[] buffer = questionlist[ j ].getBytes();

      try {
        fos2.write( buffer );
        j++;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

	}

	public static void main( String args[] ) {
		SWCheck sl = new SWCheck();
		sl.go();
	}

}
