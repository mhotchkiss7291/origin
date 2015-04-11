import java.io.*;
import java.util.*;

public class DeleteRecord {

	private Process mChild = null;
	private BufferedInputStream dis = null;
  private BufferedOutputStream dos = null;

	static final String completed = "COMPLETED: ";
  static final int DEFAULT_WAIT = 3;


	public void go() {

		String[] dellist = loadArray( "./finalout" );
		int delcount = dellist.length;

		connectToServer();

		int i = 0;

		while( i < delcount ) {
			//System.out.println( "delete " + dellist[i] );
			delete( dellist[i] );
			i++;
		}

		logout();

	}

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

	public void connectToServer() {

    try {
      Runtime process = Runtime.getRuntime();
      String cmd = "./cli.bin";
      String[] env = { "SAFEWORD=" +
            System.getProperty("com.scc.safeword") };
      try {
        mChild = process.exec(cmd,env);
        Thread.sleep(1000);
      } catch (Throwable t) {
        try {
          SWErrorHandler seh = new SWErrorHandler("CLI_ERROR",
              "Error while loading the CLI " + cmd);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      // Open streams
      dos = new BufferedOutputStream (mChild.getOutputStream());
      dis = new BufferedInputStream(mChild.getInputStream());
    } catch(Throwable e) {
      try {
        SWErrorHandler seh = new SWErrorHandler("CLI_ERROR",
            "Error while creating I/O streams to the local CLI.");
      } catch (IOException ee) {
        ee.printStackTrace();
      }
    }

    // Log in as super user
    try {
      sendAndWait("login");
    } catch (SafewordException se) {
      se.printStackTrace();
      System.out.println("Login failed");
    }
    try {
      sendAndWait("super");
    } catch (SafewordException se) {
      se.printStackTrace();
      System.out.println("password entry failed");
    }
		
		// Extract password from protected file
    String pswd = "";
    try {
      sendAndWait( pswd );
    } catch (SafewordException se) {
      se.printStackTrace();
      System.out.println("Password confirmation failed");
    }
  }


	public void deleteRecord( String id ) {

    try {
      sendAndWait("delete " + id );
    } catch (SafewordException se) {
      se.printStackTrace();
      System.out.println("Record " + id + " not found");
      return;
    }
	}

	private synchronized Vector sendAndWait(String s, int waitTime)
            throws SafewordException {

    String outputFromServer = "";
    Vector d = new Vector();

    // Send the command to the CLI.
    if (!write(s))
      return null;

    // Read the responses from the CLI and put each returned line
    // in a Vector.
    while ((outputFromServer = read(waitTime)) != null ) {
      // Put response in the Vector.
      d.addElement(outputFromServer);

      // Break if the CLI has finished. It will always send COMPLETED.
      if (outputFromServer.startsWith(completed))
        break;

      // Break if this is a 1-line login dialog
      if (outputFromServer.indexOf("DLG:") == 2 )
        break;

      // Check for 2-line login dialog
      if (outputFromServer.indexOf("DLG2:") == 2 ) {
        // Pick up second line of dialog and then break
        if ((outputFromServer = read(waitTime)) == null)
          break;
        d.addElement(outputFromServer);
        break;
      }
    }

		if (outputFromServer == null)
      throw new SafewordException("ERROR Timeout in CLI connection.");

    // Strip "READY" prompt
    if (!d.isEmpty() && ((String)d.firstElement()).equals("READY:>"))
      d.removeElementAt(0);

    // Check COMPLETED: line for error messages.
    // If present, throw an exception.
    if (!d.isEmpty() && ((String)d.lastElement()).startsWith(completed)) {
      outputFromServer =
            ((String)d.lastElement()).substring(completed.length());
      d.removeElementAt(d.size()-1);
      if (outputFromServer.indexOf("ERROR") == 0) {
        throw new SafewordException(outputFromServer);
      }
    }
     
    // If we never got anything from the server then just return null.
    if (d.isEmpty()) {
      return null;
    }

    // Return the CLI's responses.
    return d;
  }

  // Overload version
  private synchronized Vector sendAndWait(String s)
        throws SafewordException {
    return sendAndWait(s, DEFAULT_WAIT);
  }

	private boolean write(String s) {
    if (dos == null) {
      return false;
    }

    //if( DEBUG ) {
     // System.out.println("\tTo CLI:> "+ s);
      //log("\tTo CLI:> "+ s);
    //}

    try {
      dos.write((s+"\n").getBytes());
    }
    catch (Throwable t) {
      try {
        SWErrorHandler seh = new SWErrorHandler("CLI_ERROR",
            "Error writing to CLI ");
      } catch (IOException e) {
        e.printStackTrace();
      }
      return false;
    }
    return true;
  }

	private String read(int waitsec) {
    if (dis == null) {
      return null;
    }
    try {
      dos.flush();
    } catch(IOException e) {
      try {
        SWErrorHandler seh = new SWErrorHandler("CLI_ERROR",
            "Error flushing CLI output - " + e.getMessage());
      } catch (IOException ee) {
        ee.printStackTrace();
      }
    }

    try {
      // Wait for data to be available for reading.
      for (int i = 0; dis.available() == 0 && i < 10*waitsec; i++) {
        Thread.sleep(100);
      }
      // If there was no data to read then just return.
      if (dis.available() == 0) {
        return null;
      }
      // Read a single line of text from the CLI.
      byte[] sText = new byte[20000];
      byte c;
      int nIndex;
      String sep = System.getProperty("line.separator");
      c = (byte)dis.read();
      while (sep.indexOf(c) >= 0)
        c = (byte)dis.read();
       for (nIndex = 0; sep.indexOf(c) < 0; c = (byte)dis.read())
        sText[nIndex++] = c;
      String text = new String(sText, 0, nIndex);
      //if (DEBUG) {
      //  System.out.println("\tFrom CLI ->" + text);
       // log("\tFrom CLI ->" + text);
      //}
      return text;
    } catch (Throwable t) {
      try {
        SWErrorHandler seh = new SWErrorHandler("CLI_ERROR",
            "Server connection appears broken.  Please reconnect.");
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }
  }

	public void delete( String UserID ) {

    // Delete the record
    try {
      sendAndWait("delete -i " + UserID );
    } catch (SafewordException se) {
      //se.printStackTrace();
      System.out.println( UserID + ": Not in database");
    }

  }

	// Log out
  public void logout() {
    try {
      sendAndWait("quit");
    } catch (SafewordException se) {
			System.out.println("Failed to logout");
      se.printStackTrace();
    }
  }


	public static void main( String args[] ) {

		DeleteRecord dr = new DeleteRecord();
		dr.go();

	}
}
