import java.io.*;
import java.util.*;

public class EMARecord {

  // Private members
  private String RequestType = null;
  private String ReplyType = null;
  private String UserID = null;
  private String CardID = null;
  private String FirstName = null;
  private String LastName = null;
  private String EMail = null;

  // Preserved card replacement values
  private String ServicesAllowed = null;
  private String OldCardNumber = null;
  private String Group = null;

  // Containers
  private Vector requestV = null;
  private Vector replyV = null;

  // Used for Safeword CLI commands
  static final String completed = "COMPLETED: ";
  static final int DEFAULT_WAIT = 3;

  // I/O objects
  private Process mChild = null;
  private BufferedInputStream dis = null;
  private BufferedOutputStream dos = null;
  static final boolean DEBUG = false;

  // Main constructor
  EMARecord( Vector in ) {
    requestV = new Vector();
    requestV = in;
    replyV = new Vector();
		replyV.setSize( 2 );
	
		ServicesAllowed = new String();
		OldCardNumber = new String();
		Group = new String();

    // Get the request type
    RequestType = new String( (String)requestV.elementAt(0));

    // Assign member variables based on RequestType
    if( RequestType.equals("ENABLE") ||
					RequestType.equals("REPLACE") || 
					RequestType.equals("VERIFYACCOUNT") ) {

      UserID = (String)requestV.elementAt(1);
      CardID = ((String)requestV.elementAt(2)).toUpperCase();

      FirstName = (String)requestV.elementAt(3);
      LastName = (String)requestV.elementAt(4);
      EMail = (String)requestV.elementAt(5);

    } else if ( RequestType.equals("DELETE") || 
								RequestType.equals("ROLLBACK")) {

      UserID = (String)requestV.elementAt(1);

    // If valid RequestType isn't present, send alert!
    } else {
      EMANotify n = new EMANotify("Illegal request type: " + RequestType );
      try {
        n.send();
      } catch (IOException e ) {
        e.printStackTrace();
      }
    }
  }

  // Open up the command line interface and get ready
  // for conversation
  public Vector connectToServer() {
    // Issue runtime command
    try {
      Runtime process = Runtime.getRuntime();

      String cmd = "/usr/safeword-async/cli"; //CHECK!

      try {
        mChild = process.exec(cmd);
        Thread.sleep(1000);
      } catch (Throwable t) {
        try {
          EMAErrorHandler seh = new EMAErrorHandler("CLI_ERROR", 
              "Error while loading the CLI on ironwood EMA" + cmd);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      // Open streams
      dos = new BufferedOutputStream (mChild.getOutputStream());
      dis = new BufferedInputStream(mChild.getInputStream());
    } catch(Throwable e) {
      try {
        EMAErrorHandler seh = new EMAErrorHandler("CLI_ERROR", 
            "Error while creating I/O streams to the local CLI.");
      } catch (IOException ee) {
        ee.printStackTrace();
      }
    }

    // Log in as super user
    try {
      sendAndWait("login");
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Login failed", 0 );
      return replyV;
    }
    try {
      sendAndWait("super");
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("password entry failed", 0 );
      return replyV;
    }

    // Extract password from protected file
    String pswd = getAdminPassword();
    try {
      sendAndWait( pswd );
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Password confirmation failed", 0 );
      return replyV;
    }
    return replyV;

    // logged in!
  }

  // Main method for enabling a new card
  public Vector enable() {
    // Don't let anyone else get to the record
    // while your modifying it by locking it
    try {
      sendAndWait("lock -i dg-" + CardID );
    } catch (EMASafewordException se) {

      //Check to see if they already have a user record
      try {
        sendAndWait("get -i " + getUserID() );
      } catch (EMASafewordException sse) {
        sse.printStackTrace();
        log( getUserID() + ": Card not in database");
        //logAndNotify( getUserID() + ": Card not in database");
        replyV.setElementAt(
          "Your card number is not found in the database", 0 );
        return replyV;
      }

      se.printStackTrace();
      log( getUserID() + ": Card already assigned");
      //logAndNotify( getUserID() + ": Card already assigned");
      replyV.setElementAt(
        "Your card was previously assigned to you. Now retrieve your PIN", 0 );
      return replyV;
    }

    // Export the unassigned card record to a local file
    try {
      sendAndWait("get -i dg-" + CardID + " -f newcard.rec");
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Get record failed", 0 );
      return replyV;
    }

    // Modify the important values of the record and
    // write it to a new file for import
    if ( !enableRecordFile() ) {
      return replyV;
    }

    // Import the file
    try {
      sendAndWait("import -f newuser.rec");
    } catch (EMASafewordException se) {
      se.printStackTrace();
			//copyRecordFiles( getUserID() );
      log( getUserID() + ": Record import failed; ID may exist");
      //logAndNotify( getUserID() + ": Record import failed; ID may exist");
      replyV.setElementAt(
				"You have different token card already registered to you.", 0 );
      return replyV;
    }

    // Remove the old unassigned record
    try {
      sendAndWait( "unlock dg-" + CardID );
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Unlock record failed", 0 );
      return replyV;
    }
    try {
      sendAndWait( "delete dg-" + CardID );
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Delete record failed", 0 );
      return replyV;
    }

    // If you get to here, the card's enabled
    log( getUserID() + ": Enabled");
    //logAndNotify( getUserID() + ": Enabled");
    replyV.setElementAt("ENABLECONFIRMED", 0 );
    return replyV;
  }

	// Replace a token card with a new one for an existing user
  public Vector replace() {
    // Don't let anyone else get to the record
    // while your modifying it by locking it
    try {
      sendAndWait("lock -i dg-" + CardID );
    } catch (EMASafewordException se) {

      se.printStackTrace();
      log( getUserID() + ": Card number not found or already assigned");
      //logAndNotify( getUserID() + ": Card number not found or already assigned");
      replyV.setElementAt("Card number not found or already assigned", 0 );
      return replyV;
    }
    // Export the unassigned card record to a local file
    try {
      sendAndWait("get -i dg-" + CardID + " -f newcard.rec");
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Get record failed", 0 );
      return replyV;
    }

    // Lock the existing User record
    try {
      sendAndWait("lock -i " + UserID );
    } catch (EMASafewordException se) {
      se.printStackTrace();
      log( getUserID() + ": UserID not found");
      //logAndNotify( getUserID() + ": UserID not found");
      replyV.setElementAt("Your original token card record was not found.", 0 );
      return replyV;
    }

    // Export the user record to a local file
    try {
      sendAndWait("get -i " + UserID + " -f olduser.rec");
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Get User record failed", 0 );
      return replyV;
    }

    // Modify the important values of the record, insert
		// the preserved data of the user into the record, and
    // write it to a new file for import
    if ( !replaceRecordFile() ) {
      replyV.setElementAt("Failed to read existing record properly", 0 );
      return replyV;
    }

    // Delete the existing user's record prior to the 
    // import of the new one. (Record must be unique)
    try {
      sendAndWait("delete " + UserID);
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Delete User record failed", 0 );
      return replyV;
    }

    // Import the new file
    try {
      sendAndWait("import -f newuser.rec");
    } catch (EMASafewordException se) {
      se.printStackTrace();
			//copyRecordFiles( getUserID() );
      log( getUserID() + ": Record Import Failed");
      //logAndNotify( getUserID() + ": Record Import Failed");
      replyV.setElementAt("Safeword Import record failed", 0 );
      return replyV;
    }

    // Remove the old unassigned record
    try {
      sendAndWait( "unlock dg-" + CardID );
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Unlock record failed", 0 );
      return replyV;
    }
    try {
      sendAndWait( "delete dg-" + CardID );
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Delete record failed", 0 );
      return replyV;
    }

    // If you get to here, the card's been replaced
    log( getUserID() + ": Replaced");
    //logAndNotify( getUserID() + ": Replaced");
    replyV.setElementAt("REPLACECONFIRMED", 0 );
    return replyV;
  }

  //rollback transaction due to processing error
  //in initial transaction
  public Vector rollback() {
    try {
      if(requestV.elementAt(5).equals("ENABLE")) {	
				//restore token card 
        sendAndWait( "import -f newcard.rec" );
      } else if (requestV.elementAt(5).equals("REPLACE")) {
				//restore user to pre-transaction value and
        //reset card to pre-transaction value		
				sendAndWait( "import -f olduser.rec" );
   			sendAndWait( "import -f newcard.rec" );		
      } else {
				replyV.setElementAt(
					"Unable to process request type during rollback.", 0);		
      }	
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword rollback failed", 0);
      return replyV;
    }

    //if you get to here, the Safeword entry was 
    //rolled back to its original value 
    logAndNotify( getUserID() + ": Rollback");
    replyV.setElementAt("ROLLBACKCONFIRMED", 0);  
    return replyV;

  } 

  public Vector verifyAccount() {

    // Lock the existing User record
    try {
      sendAndWait("lock -i " + UserID );
    } catch (EMASafewordException se) {
      se.printStackTrace();
      log( getUserID() + ": UserID not found");
      //logAndNotify( getUserID() + ": UserID not found");
      replyV.setElementAt("UserID not found", 0 );
      return replyV;
    }

    // Export the user record to a local file
    try {
      sendAndWait("get -i " + UserID + " -f olduser.rec");
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Get User record failed", 0 );
      return replyV;
    }

    EMANotifyPINUser npu = new EMANotifyPINUser( EMail );
    try {
      npu.send();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if ( !getCardID() ) {
      return replyV;
    }

    try {
      sendAndWait( "unlock " + UserID );
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Unlock record failed", 0 );
      return replyV;
    }

    if( !CardID.equals( OldCardNumber )) {
      replyV.setElementAt(
					"The card you hold does not match in the database.", 0 );
    	log( getUserID() + ": Failed Existing Account Verifcation");
    	//logAndNotify( getUserID() + ": Failed Existing Account Verifcation");
      return replyV;
		}

    log( getUserID() + ": Account Verified, PIN retrieved");
    //logAndNotify( getUserID() + ": Account Verified, PIN retrieved");
    replyV.setElementAt( "ACCOUNTVERIFIED", 0  );
    replyV.setElementAt( OldCardNumber, 1 );
    return replyV;
  }

  // Log out 
  public Vector logout() {
    try {
      sendAndWait("quit");
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Quit failed", 0);
      return replyV;
    }
    return replyV;
  }

  public Vector delete() {
    // Delete the record
    try {
      sendAndWait("delete -i " + getUserID() );
    } catch (EMASafewordException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Delete record failed", 0);
      log( UserID + ": Not in database");
      //logAndNotify( UserID + ": Not in database");
      return replyV;
    }

    // If you made it here, the record is gone
    log( UserID + ": Deleted");
    //logAndNotify( UserID + ": Deleted");
    replyV.setElementAt("DELETECONFIRMED", 0);
    return replyV;
  }

  // Modifies a Safeword record from a local input
  // file and writes it out to a new file
  private boolean enableRecordFile() {

    // Check that FirstName and LastName do not have
    // any $ characters in them
    if( (FirstName.indexOf("$")) != -1 ) {
      return false;
    }
    if( (LastName.indexOf("$")) != -1 ) {
      return false;
    }

    // Check that FirstName and LastName do not have
    // any commas in them
    if( (FirstName.indexOf(",")) != -1 ) {
      return false;
    }
    if( (LastName.indexOf(",")) != -1 ) {
      return false;
    }

    String[] CardRecordData = parseRecord("./newcard.rec");

    if( CardRecordData[1] == null ) {
			return false;
    }

    // Change record values

    // UserID
    CardRecordData[3] = UserID.toUpperCase(); 

    // User Name
    CardRecordData[4] = FirstName + " " + LastName;

    // Comment field
    CardRecordData[9] = "S/N:" + CardID.toUpperCase();

    if( !writeRecord( CardRecordData, "./newuser.rec") ) {
			return false;
    }

    return true;
  }

  // Method for constructing import record while preserving
  // a user's vital data. 
  private boolean replaceRecordFile() {

    // Get existing user data for replacement into new card
    if( !getUserRecordData()) {
      return false;
    }

    // Check that FirstName and LastName do not have
    // any $ characters in them
    if( (FirstName.indexOf("$")) != -1 ) {
      return false;
    }
    if( (LastName.indexOf("$")) != -1 ) {
      return false;
    }

    // Check that FirstName and LastName do not have
    // any commas in them
    if( (FirstName.indexOf(",")) != -1 ) {
      return false;
    }
    if( (LastName.indexOf(",")) != -1 ) {
      return false;
    }

		String[] CardRecordData = parseRecord("./newcard.rec");

		if( CardRecordData[1] == null ) {
			return false;
		}

		// Change record values

		// UserID
		CardRecordData[3] = UserID.toUpperCase(); 

		// User Name
		CardRecordData[4] = FirstName + " " + LastName;

		// Group
		CardRecordData[5] = Group;

		// Comment field
		CardRecordData[9] = 
        "S/N:" + CardID.toUpperCase() + " r:" + OldCardNumber ;

		// Services Allowed
		CardRecordData[35] = ServicesAllowed; 

		if( !writeRecord( CardRecordData, "./newuser.rec") ) {
			return false;
		}

    return true;
  }

	// Extract the user's Group, Comment, and Services Allowed
	// values for insertion in the newly constructed record.
	private boolean getUserRecordData() {


		String[] UserData = parseRecord("./olduser.rec");

		if( UserData[1] == null ) {
			return false;
		} 

		//if(DEBUG) {
	        //	int i = 1;
		//      while( i < 39 ) {
	        //	System.out.println( "i = " + i + ": \"" + UserData[i] + "\"");
	        //	i++;
	        //	}
		//}

		// Set the variables
		Group = UserData[ 5 ];
		OldCardNumber = getOldCardNumber( UserData[ 9 ] );
		ServicesAllowed = UserData[ 35 ];

		return true;
	}

	private boolean getCardID() {

		// Open existing user record file
    File fi = null;
    FileReader in = null;
    String dstring = null;
    try {
      fi = new File("./olduser.rec");
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
      replyV.setElementAt( "Could not open esunU.rec", 0 );
      replyV.setElementAt( e.getClass().getName() + ": " + 
					e.getMessage(), 1 );
      return false;
    }

    // With string loaded, tokenize it
    StringTokenizer sst = 
          new StringTokenizer( dstring, ",", true);

    StringBuffer sb = new StringBuffer();
    String sout = null;
    String s = null;
    int count = 0;

    // Catch and modify important record values
    while( sst.hasMoreTokens() ) {

      s = sst.nextToken();

			// Get card ID out of comment field
      if( count == 16 ) {
				OldCardNumber = getOldCardNumber( unQuote( s ) );

      // Otherwise, skip it
      } else {
        sb.append( s );
      }
			count++;
    }

		return true;
	}

	// Trims $$ from both sides of a string
	private String unQuote( String s ) {
    if (s.length() >= 4 &&
        s.startsWith("$$") &&
        s.endsWith("$$"))
      return s.substring(2, s.length()-2);
    else
      return s;
  }

	// Parse the Comment field and extract the old
	// card number from the "S/N:" prefix
	private String getOldCardNumber( String s ) {

		int start = 0;
		int end = 0;

		if( (start = s.indexOf("N:")) == -1 ) {
			return (new String("Not available"));
		} 

		start = start + 2;

		// If the Comment field has more text after
		// the number, return only the number.
		end = s.indexOf(" ", start);
		if( end == -1 ) {
			return s.substring( start );
		} else {
			return s.substring( start, end );
		}
  }

  // Read response from CLI
  private String read(int waitsec) {
    if (dis == null) {
      return null;
    }
    try {
      dos.flush();  
    } catch(IOException e) {
      try {
        EMAErrorHandler seh = new EMAErrorHandler("CLI_ERROR", 
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
       // System.out.println("\tFrom CLI ->" + text);
        //log("\tFrom CLI ->" + text);
      //}
      return text;
    } catch (Throwable t) {
      try {
        EMAErrorHandler seh = new EMAErrorHandler("CLI_ERROR", 
            "Server connection appears broken.  Please reconnect.");
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }
  }

  // Issue command to CLI and wait for reply
  private synchronized Vector sendAndWait(String s, int waitTime) 
            throws EMASafewordException {
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
      throw new EMASafewordException("ERROR Timeout in CLI connection.");

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
        throw new EMASafewordException(outputFromServer);
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
        throws EMASafewordException {
    return sendAndWait(s, DEFAULT_WAIT);
  }

  // Write to the CLI
  private boolean write(String s) {
    if (dos == null) {
      return false;
    }

    //if( DEBUG ) {
    //  System.out.println("\tTo CLI:> "+ s);
    //  log("\tTo CLI:> "+ s);
    //}

    try {
      dos.write((s+"\n").getBytes());
    }
    catch (Throwable t) {
      try {
        EMAErrorHandler seh = new EMAErrorHandler("CLI_ERROR", 
            "Error writing to CLI ");
      } catch (IOException e) {
        e.printStackTrace();
      }
      return false;
    }
    return true;
  }

  // Unlink any local record files, if they exist
  public void deleteRecFiles() {

    File f1 = new File( "./newcard.rec" );
    File f2 = new File( "./newuser.rec" );
    File f3 = new File( "./olduser.rec" );

    if( f1.exists() ) {
      f1.delete();
    }

    if( f2.exists() ) {
      f2.delete();
    }

    if( f3.exists() ) {
      f3.delete();
    }
  }

  // CardIDs have to start with a alpha character and
  // the rest digits. The string has to be 6 or 7 characters.
  // Check for this.
  public boolean validateCardID() {

    CardID.trim();
    int len = CardID.length();

    // Check length
    if(!(( len >= 6 ) && ( len <= 8 ))) {
      return false;
    }
    char c = CardID.charAt(0);

    // Check for letter at the front 
    if( !Character.isLetter(c) ) {
      return false;
    }
    int i = 2;

    // Check for trailing digits
    while( i < len ) {
      c = CardID.charAt( i );
      if( !Character.isDigit( c ) ) {
        return false;
      }
      i++;
    } 
    return true;
  }

  // UserIDs start with two alpha characters
  // with remaining digits and must be 9 characters
  // or less. Check it.
  public boolean validateUserID() {
    UserID.trim();
    int len = UserID.length();

    // Check length
    if( ( len > 9 ) || ( len < 2 )) {
      return false;
    }

    int i = 0;
    char c;

    // Check for two characters
    while( i < 2 ) {
      c = UserID.charAt(i);
      if( !Character.isLetter( c ) ) {
        return false;
      }
      i++;
    }

    // Check for trailing digits
    while( i < len ) {
      c = UserID.charAt( i );
      if( !Character.isDigit( c ) ) {
        return false;
      }
      i++;
    } 
    return true;
  }

  // Get the password from a local protected file
  private String getAdminPassword() {
    File f;
    FileReader in = null;
    String directory = "./etc";
    String filename = "dat2";
    String DataString = null;
    String passwd = null;

    try {
      f = new File( directory, filename );
      in = new FileReader(f);
      int size = (int) f.length();
      char[] data = new char[size];
      int chars_read = 0;

      while( chars_read < size ) {
        chars_read += in.read(data, chars_read, size - chars_read);
      }

      DataString = new String( data );

    } catch (IOException e) {
      System.out.println( e.getClass().getName() + 
                ": " + e.getMessage());
    } finally {
      try {
        if( in != null ) {
          in.close();
        }
      } catch (IOException e ) {
      }
    }
    return DataString.trim();
  }

  // Log the transaction and send urgent notification
  // to admin
  public void logAndNotify( String in ) {
    log( in );
    EMANotify n = new EMANotify( in );
    try {
      n.send();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Log the transaction for non-urgent activity
  public void log( String in ) {
    RandomAccessFile raf = null;

    // Check that the logfile is open. If not,
    // notify admin
    try {
      raf = new RandomAccessFile("./etc/log", "rw");
    } catch (IOException e) {
      e.printStackTrace();
      EMANotify n = new EMANotify("Can't open log file");
      try {
        n.send();
      } catch (IOException ee) {
        ee.printStackTrace();
      }
    }

    // Append to the bottom of the log file
    try {
      raf.seek( raf.length() );
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Tack on a readable date
    String buf = (new Date()) + ": " + in + "\n";
    byte[] buffer = buf.getBytes();
    try {
      raf.write( buffer );
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      raf.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Accessor for UserID
  public String getUserID() {
    return UserID;
  }

	// In case the import of the record fails for any
	// reason, preserve the before and after record 
	// files for debugging. Files are copied to a
	// secure directory
  public void copyRecordFiles(String Recfile) {

    // Issue runtime command
    try {
      Runtime process = Runtime.getRuntime();
      String cmd = "cp ./newcard.rec ./etc/" + Recfile + ".rec\n" ;
      String cmd2 = "cp ./newuser.rec ./etc/" + Recfile + ".rec2\n" ;
      try {
        mChild = process.exec(cmd);
        Thread.sleep(1000);
        mChild = process.exec(cmd2);
        Thread.sleep(1000);
      } catch (Throwable t) {
        try {
          EMAErrorHandler seh = new EMAErrorHandler("Copy Error:", 
              "Error while copying record file" + cmd);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch(Throwable e) {
      try {
        EMAErrorHandler seh = new EMAErrorHandler("Copy Error:", 
            "Error while execing copy command");
      } catch (IOException ee) {
        ee.printStackTrace();
      }
    }
	}

	private String[] parseRecord( String inFile ) {

    // Open existing user record file
    File fi = null;
    FileReader in = null;
    String dstring = null;
    try {
      fi = new File( inFile );
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
      replyV.setElementAt( "Could not open " + inFile , 0 );
      replyV.setElementAt( e.getClass().getName() + ": " + 
					e.getMessage(), 1 );
      return null;
    }

    // Skip the first two lines and strip newlines
    StringTokenizer sst =
          new StringTokenizer( dstring, "\n", false);
    sst.nextToken();
    sst.nextToken();

    // varible setup
    String sBuffer = null;
    String sFieldBuffer = null;
    String delimiter = null;
    int pFieldEnd = 0;
    int nField = 0;
		String[] sa = new String[39];

		int i = 1;
		while( i < 39 ) {
			sa[i] = null;
			i++;
		}

    // Main buffer
    sBuffer = new String( sst.nextToken() );

    // Parse
    for( nField = 1; nField < 39; nField++ ) {

      switch( nField ) {
        case 3:
        case 4:
        case 5:
        case 9:
        case 28:
          delimiter = "$$,";
          break;
        case 35:
        case 36:
          if( sBuffer.startsWith("S") ) {
            delimiter = "$$,";
          } else {
            delimiter = ",";
          }
          break;
        case 29:
        case 30:
        case 31:
        default:
          delimiter = ",";
      }

      // if $$ delimited and doesn't start with S, step over $$
      if( delimiter.startsWith("$") && (!sBuffer.startsWith("S")))  {
          sBuffer = sBuffer.substring( 2 );
      }

      // Find next delimiter position
      pFieldEnd = sBuffer.indexOf( delimiter );

      // If not at the end, chop off next field and advance token
      if( pFieldEnd != -1 ) {
        sFieldBuffer = sBuffer.substring( 0, pFieldEnd );
        sBuffer = sBuffer.substring( pFieldEnd + delimiter.length() );
      // otherwise just chop
      } else {
        sFieldBuffer = sBuffer;
      }

      sa[ nField ] = sFieldBuffer;

    }
    return sa;
  }

	private boolean writeRecord( String[] sa, String FileName ) {

   // Construct a buffer a bit larger than a 
   // typical record
   StringBuffer sb = new StringBuffer( 750 );

   // First two lines
   sb.append( "00000000\n(33235C14F333)\n" );

   int nField = 0;

    for( nField = 1; nField < 39; nField++ ) {

      switch( nField ) {
        case 3:
        case 4:
        case 5:
        case 9:
        case 28:
					sb.append("$$" + sa[nField] + "$$,"); 
          break;
				case 35:
	  			sb.append( sa[nField] + "$$,"); 
          break;
				case 38:
          sb.append( sa[nField] + "\n" ); 
          break;
				case 6:
	  			sb.append( "00000000," );
	  			break;
        default:
	  			sb.append( sa[ nField ] + "," );
      }
		}

    File fo = null;
    FileWriter out = null;
		String sout = null;

    try {
      fo = new File( FileName );
      out = new FileWriter( fo );
      sout = new String( sb.toString() );
      out.write( sout, 0, sout.length());
      out.flush();
      out.close();
    } catch (IOException e ) {
      System.out.println( e.getClass().getName() + 
              ": " + e.getMessage());
      return false;
    }

    return true;
  }

}
