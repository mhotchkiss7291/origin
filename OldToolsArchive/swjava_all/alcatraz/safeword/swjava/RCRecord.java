import java.io.*;
import java.util.*;

// Main object for extracting/modifying user
// records from the Safeword database

public class RCRecord {

  // Private members
  private String RequestType = null;
  private String ReplyType = null;

  private String UserID = null;
  private String CardID = null;
  private String OldCardNumber = null;
  private String Comment = null;
  private String FirstName = null;
  private String LastName = null;
  private String EMail = null;

  private String ModemPool = null;
  private String HighSpeed = null;
  private String Eccpx = null;
  private String Psc = null;
  private String NsgSun = null;
  private String Th = null;
  private String Icabad = null;
  private String Dialup = null;
  private String Safew = null;
  private String Ite = null;

  // Preserved card replacement values
  private String ServicesAllowed = null;
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

	//Empty constructor
  RCRecord() {
	}

  // Main constructor
  RCRecord( Vector in ) {
    requestV = new Vector();
    requestV = in;
    replyV = new Vector( );
    replyV.setSize( 13 );

    // Get the request type
    RequestType = new String( (String)requestV.elementAt(0));

    // Assign member variables based on RequestType
    if ( RequestType.equals("GETSERVICES") ) {

      UserID = (String)requestV.elementAt(1);

      if(DEBUG)System.out.println("UserID = " + UserID);

    } else if ( RequestType.equals("CHANGESERVICES") ) {

      UserID = (String)requestV.elementAt(1);
      Comment = (String)requestV.elementAt(2);
      ModemPool = (String)requestV.elementAt(3);
      HighSpeed = (String)requestV.elementAt(4);
      Eccpx = (String)requestV.elementAt(5);
      Psc = (String)requestV.elementAt(6);
      NsgSun = (String)requestV.elementAt(7);
      Th = (String)requestV.elementAt(8);
      Icabad = (String)requestV.elementAt(9);
      Dialup = (String)requestV.elementAt(10);
      Safew = (String)requestV.elementAt(11);
      Ite = (String)requestV.elementAt(12);

    } else if ( RequestType.equals("VERIFYACCOUNT") ) {

      UserID = (String)requestV.elementAt(1);
      CardID = (String)requestV.elementAt(2);

    } else if ( RequestType.equals("DELETEACCOUNT") ) {

      UserID = (String)requestV.elementAt(1);

    } else if ( RequestType.equals("RCENABLE") ||
								RequestType.equals("RCREPLACE") ) { 

      UserID = (String)requestV.elementAt(1);
      CardID = ((String)requestV.elementAt(2)).toUpperCase();

      FirstName = (String)requestV.elementAt(3);
      LastName = (String)requestV.elementAt(4);
			//EMail = (String)requestV.elementAt(5);

    } else if ( RequestType.equals("RCROLLBACK")) {

      UserID = (String)requestV.elementAt(1);

    // If valid RequestType isn't present, send alert!
    } else {
      RCNotify n = new RCNotify("Illegal request type: " + RequestType );
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

		if(DEBUG)System.out.println("Connecting to server...");

    // Issue runtime command
    try {

      Runtime process = Runtime.getRuntime();
      String cmd = "/usr/safeword/cli"; //CHECK!

      try {
        mChild = process.exec(cmd);
        Thread.sleep(1000);
      } catch (Throwable t) {
        replyV.setElementAt("process.exec() problem", 0 );
        try {
          RCErrorHandler seh = new RCErrorHandler(
						"CLI_ERROR", "Error while loading the alcatraz CLI " + cmd);
        } catch (IOException e) {
          e.printStackTrace();
        }
        return replyV;
      }

      // Open streams
      dos = new BufferedOutputStream (mChild.getOutputStream());
      dis = new BufferedInputStream(mChild.getInputStream());
    } catch(Throwable e) {
      replyV.setElementAt("Buffered Streams problem", 0);
      try {
        RCErrorHandler seh = new RCErrorHandler("CLI_ERROR", 
            "Error while creating I/O streams to the local CLI.");
      } catch (IOException ee) {
        ee.printStackTrace();
      }
      return replyV;
    }

    // Log in as super user
    try {
      sendAndWait("login");
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Login failed", 0);
      return replyV;
    }
    try {
      sendAndWait("super");
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("password entry failed", 0);
      return replyV;
    }

    // Extract password from protected file
    String pswd = getAdminPassword();
    try {
      sendAndWait( pswd );
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Password confirmation failed", 0);
      return replyV;
    }
    return replyV;

    // logged in!
  }

  // Log out 
  public Vector logout() {
    try {
      sendAndWait("quit");
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Quit failed", 0);
      return replyV;
    }
    return replyV;
  }

  // Exports a user record to local directory, modifies
  // it and imports the modified record
  public Vector setServices() {
    try {
      sendAndWait("lock -i " + UserID );
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Lock record failed", 0);
      return replyV;
    }

    try {
      sendAndWait("get -i " + UserID + " -f olduser.rec");
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Get record failed", 0);
      return replyV;
    }

    if( !setServicesRecordFile() ) {
      replyV.setElementAt("Services Edit of record file failed", 0);
      return replyV;
    }

    try {
      sendAndWait( "delete " + UserID );
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Delete record failed", 0);
      return replyV;
    }

    try {
      sendAndWait("import -f newuser.rec");
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Import record failed", 0);
      return replyV;
    }

    try {
      sendAndWait( "unlock " + UserID );
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Unlock record failed", 0);
      return replyV;
    }

    // Made it!!
    replyV.setElementAt("CHANGESERVICESCONFIRMED", 0);
    return replyV;
  }


  // Exports a user record, extracts the Services Allowed
  // settings for that user
  public Vector getServices() {

    try {
      sendAndWait("get -i " + UserID + " -f olduser.rec");
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("GETRECORDFAILED", 0);
      return replyV;
    }
    replyV = getServicesFromFile();
    return replyV;
  }

  // Opens the exported record file, tokenizes the
  // services allowed fields, and populates the reply
  // Vector with the values
  private Vector getServicesFromFile() {

     String[] RecordData = parseRecord("./olduser.rec");

    // Confirm request type 
    ReplyType = new String("SERVICESRETURNED");
    //replyV.insertElementAt( ReplyType, 0 );
    replyV.setElementAt( ReplyType, 0 );

    // Full Name
    replyV.setElementAt( RecordData[4], 1 );

    // Comment
    replyV.setElementAt( RecordData[9], 2 );

    String ServicesAllowed = RecordData[35];

    String[] ServiceData = new String[24];
    ServiceData = parseServices( ServicesAllowed );

    readHash( ServiceData );

    //Load the reply Vector
    replyV.setElementAt( ModemPool, 3 );
    replyV.setElementAt( HighSpeed, 4 );
    replyV.setElementAt( Eccpx, 5 );
    replyV.setElementAt( Psc, 6 );
    replyV.setElementAt( NsgSun, 7 );
    replyV.setElementAt( Th, 8 );
    replyV.setElementAt( Icabad, 9 );
    replyV.setElementAt( Dialup, 10 );
    replyV.setElementAt( Safew, 11 );
    replyV.setElementAt( Ite, 12 );

    log( "Get services for " + UserID + ": " + replyV );
    //logAndNotify( "Get services for " + UserID + ": " + replyV );
    return replyV;
  }

  // Opens the exported user record file. For 
  // each Services Allowed field, determine the
  // input value, assign key and value strings
  // for each type of field, and then modify the
  // Services Allowed subrecord. Finally, write
  // the modified record out to a new file for
  // importing.
  private boolean setServicesRecordFile() {

		String[] RecordData = parseRecord("./olduser.rec");

    // ModemPool
    String ErpcdKey = null;
    String ErpcdValue = null;
    if( ModemPool.equals("Yes")) {
      ErpcdKey = new String("default=ERPCD");
      ErpcdValue = new String("1");
    } else {
      ErpcdKey = new String("");
      ErpcdValue = new String("");
    }

    // High Speed
    String HighSpeedKey = null;
    String HighSpeedValue = null;
    if( HighSpeed.equals("Yes") ) {
      HighSpeedKey = new String("default");
      HighSpeedValue = new String( 
              "chap=arspass group=ISDN" );
    } else {
      HighSpeedKey = new String("");
      HighSpeedValue = new String("");
    }

    // ECCPX
    String EccpxKey = null;
    String EccpxValue = null;
    if( !Eccpx.equals("No") ) {
      EccpxKey = new String("default=ECCPX");
      EccpxValue = new String( "/bin/csh" );
    } else {
      EccpxKey = new String("");
      EccpxValue = new String("");
    }

    // PSC
    String PscKey = null;
    String PscValue = null;
    if( !Psc.equals("No") ) {
      PscKey = new String("default=PSC");
      PscValue = new String( "/bin/csh" );
    } else {
      PscKey = new String("");
      PscValue = new String("");
    }

    // NSG
    String NsgSunKey = null;
    String NsgSunValue = null;
    if( !NsgSun.equals("No") ) {
      NsgSunKey = new String("default=NSG");
      NsgSunValue = new String( "/bin/csh" );
    } else {
      NsgSunKey = new String("");
      NsgSunValue = new String("");
    }

    // TH
    String ThKey = null;
    String ThValue = null;
    if( !Th.equals("No") ) {
      ThKey = new String("default=TH");
      ThValue = new String( "/bin/csh" );
    } else {
      ThKey = new String("");
      ThValue = new String("");
    }

    // Icabad
    String IcabadKey = null;
    String IcabadValue = null;
    if( !Icabad.equals("No") ) {
      IcabadKey = new String( "default=ICABAD" );
      IcabadValue = new String( "/bin/csh" );
    } else {
      IcabadKey = new String("");
      IcabadValue = new String("");
    }

    // Dialup
    String DialupKey = null;
    String DialupValue = null;
    if( !Dialup.equals("No") ) {
      DialupKey = new String( "default=DIALUP" );
      DialupValue = new String( "1" );
    } else {
      DialupKey = new String("");
      DialupValue = new String("");
    }

    // Safew
    String SafewKey = null;
    String SafewValue = null;
    if( !Safew.equals("No") ) {
      SafewKey = new String( "default=SAFEW" );
      SafewValue = new String( "/bin/csh" );
    } else {
      SafewKey = new String("");
      SafewValue = new String("");
    }

    // Ite
    String IteKey = null;
    String IteValue = null;
    if( !Ite.equals("No") ) {
      IteKey = new String( "default=ITE" );
      IteValue = new String( "/bin/csh" );
    } else {
      IteKey = new String("");
      IteValue = new String("");
    }

    RecordData[ 35 ] = new String(
      "Services Allowed;" +
      "$$" + ErpcdKey + "$$;" +
      "$$" + HighSpeedKey + "$$;" +
      "$$" + EccpxKey + "$$;" +
      "$$" + PscKey + "$$;" +
      "$$" + NsgSunKey + "$$;" +
      "$$" + ThKey + "$$;" +
      "$$" + IcabadKey + "$$;" +
      "$$" + DialupKey + "$$;" +
      "$$" + SafewKey + "$$;" +
      "$$" + IteKey + "$$;" +
      "$$default$$;" +
      "$$$$;" +

      "$$" + ErpcdValue + "$$;" +
      "$$" + HighSpeedValue + "$$;" +
      "$$" + EccpxValue + "$$;" +
      "$$" + PscValue + "$$;" +
      "$$" + NsgSunValue + "$$;" +
      "$$" + ThValue + "$$;" +
      "$$" + IcabadValue + "$$;" +
      "$$" + DialupValue + "$$;" +
      "$$" + SafewValue + "$$;" +
      "$$" + IteValue + "$$;" +
      "$$0$$;" +
      "$$$$;" 
      );

     writeRecord( RecordData, "./newuser.rec");

    log( "Set services for " + UserID );
    //logAndNotify( "Set services for " + UserID );
    return true;
  }

  // Because there is no order to the settings, we have
  // to read and analyize the whole vector. 
  // Value for each key is 12 positions after the key
  private void readHash( String[] v ) {

    // Set all fields to defaults
    ModemPool = new String("No");
    HighSpeed = new String("No");
    Eccpx = new String("No");
    Psc = new String("No");
    NsgSun = new String("No");
    Th = new String("No");
    Icabad = new String("No");
    Dialup = new String("No");
    Safew = new String("No");
    Ite = new String("No");

    int count = 0;
    while( count < 24 ) {

      // ModemPool
      if( v[ count ].equals("default=ERPCD") ) {
        if (v[count + 12].equals("1")) {
          ModemPool = new String("Yes");
        }
      // ECCPX
      } else if (v[count].equals("default=ECCPX")) {
        Eccpx = (String)v[count + 12];
        if( Eccpx.equals("/bin/csh")) {
          Eccpx = new String("Yes");
				}
      // PSC
      } else if (v[count].equals("default=PSC")) {
        Psc = (String)v[ count + 12 ];
        if( Psc.equals("/bin/csh")) {
          Psc = new String("Yes");
        }
      // NSG
      } else if (v[count].equals("default=NSG")) {
        NsgSun = (String)v[ count + 12 ];
        if( NsgSun.equals("/bin/csh") ) {
          NsgSun = new String("Yes");
        }
      // TH
      } else if (v[count].equals("default=TH")) {
        Th = (String)v[ count + 12 ];
        if( Th.equals("/bin/csh") ) {
          Th = new String("Yes");
        }
      // Icabad
      } else if (v[count].equals("default=ICABAD")) {
        Icabad = (String)v[ count + 12 ];
        if( Icabad.equals("/bin/csh") ) {
          Icabad = new String("Yes");
        }
      // Dialup
      } else if (v[count].equals("default=DIALUP")) {
        Dialup = (String)v[ count + 12 ];
        if( Dialup.equals("1") ) {
          Dialup = new String("Yes");
        }
      // Safew
      } else if (v[count].equals("default=SAFEW")) {
        Safew = (String)v[ count + 12 ];
        if( Safew.equals("/bin/csh") ) {
          Safew = new String("Yes");
        }
      // Ite
      } else if (v[count].equals("default=ITE")) {
        Ite = (String)v[ count + 12 ];
        if( Ite.equals("/bin/csh") ) {
          Ite = new String("Yes");
        }

      // HIGH SPEED 
      // The following ones are different in that the value has to be
      // found first and then go backwards to the key 12 positions

      // ISDN
      } else if (v[count].equals(
                          "chap=arspass group=ISDN")) {
        if ( ((String)v[ count - 12 ]).equals("default") ) {
          HighSpeed = new String("Yes");
        }
      }
      count++;
    }
  }

	//*****************New Code**************************************
	//Retrieves all records from Safeword database and
	//writes data to a file.  The file can then be parsed
	//to find records with selected options.
	public void getAllRecords(long now) {

		try {
			sendAndWait(
				"get -all -f /usr/safeword/swjava/RecsAll" + 
				now + ".rec", 450);   
			//check
			} catch (SafewordRCException e) {
				System.out.println("Error while getting all records.");
				e.printStackTrace();
			}

	}
	//*****************************************************************


  public Vector enable() {
    // Don't let anyone else get to the record
    // while your modifying it by locking it
    try {
      sendAndWait("lock -i dg-" + CardID );
    } catch (SafewordRCException se) {

      //Check to see if they already have a user record
      try {
        sendAndWait("get -i " + getUserID() );
      } catch (SafewordRCException sse) {
        sse.printStackTrace();
        log( getUserID() + ": Card not in database");
        //logAndNotify( getUserID() + ": Card not in database");
        replyV.setElementAt(
          "Your card number is not found in the database", 0 );
        return replyV;
      }

      se.printStackTrace();
      //logAndNotify( getUserID() + ": Card already assigned");
      log( getUserID() + ": Card already assigned");
      replyV.setElementAt(
        "Your card was previously assigned to you. Now retrieve your PIN", 0 );
      return replyV;
    }

    // Export the unassigned card record to a local file
    try {
      sendAndWait("get -i dg-" + CardID + " -f newcard.rec");
    } catch (SafewordRCException se) {
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
    } catch (SafewordRCException se) {
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
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Unlock record failed", 0 );
      return replyV;
    }
    try {
      sendAndWait( "delete dg-" + CardID );
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Delete record failed", 0 );
      return replyV;
    }

    // If you get to here, the card's enabled
    log( getUserID() + ": Enabled");
    //logAndNotify( getUserID() + ": Enabled");
    replyV.setElementAt("ENABLECONFIRMED", 0);

    return replyV;
  }

  // Replace a token card with a new one for an existing user
  public Vector replace() {
    // Don't let anyone else get to the record
    // while your modifying it by locking it

    try {
      sendAndWait("lock -i dg-" + CardID );
    } catch (SafewordRCException se) {

      se.printStackTrace();
      log( getUserID() + ": Card number not found or already assigned");
      //logAndNotify( getUserID() + ": Card number not found or already assigned");
      replyV.setElementAt("Card number not found or already assigned", 0 );
      return replyV;
    }
    // Export the unassigned card record to a local file
    try {
      sendAndWait("get -i dg-" + CardID + " -f newcard.rec");
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Get record failed", 0 );
      return replyV;
    }

    // Lock the existing User record
    try {
      sendAndWait("lock -i " + UserID );
    } catch (SafewordRCException se) {
      se.printStackTrace();
      //logAndNotify( getUserID() + ": UserID not found");
      log( getUserID() + ": UserID not found");
      replyV.setElementAt("Your original token card record was not found.", 0 );
      return replyV;
    }

    // Export the user record to a local file
    try {
      sendAndWait("get -i " + UserID + " -f olduser.rec");
    } catch (SafewordRCException se) {
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
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Delete User record failed", 0 );
      return replyV;
    }

    // Import the new file
    try {
      sendAndWait("import -f newuser.rec");
    } catch (SafewordRCException se) {
      se.printStackTrace();
      log( getUserID() + ": Record Import Failed");
      //logAndNotify( getUserID() + ": Record Import Failed");
      replyV.setElementAt("Safeword Import record failed", 0 );
      return replyV;
    }

    // Remove the old unassigned record
    try {
      sendAndWait( "unlock dg-" + CardID );
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Unlock record failed", 0 );
      return replyV;
    }
    try {
      sendAndWait( "delete dg-" + CardID );
    } catch (SafewordRCException se) {
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
     //      int i = 1;
     //      while( i < 39 ) {
     //      System.out.println( "i = " + i + ": \"" + UserData[i] + "\"" );
     //      i++;
     //      }
     //}

     // Set the variables
     Group = UserData[ 5 ];
     OldCardNumber = getOldCardNumber( UserData[ 9 ] );
     ServicesAllowed = UserData[ 35 ];

     return true;
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

  // Main method for user record deletion
  // NOTE: This method is intended to be executed from
  // Socket request made to Listener from a protected
  // interface in NetAdmin or HR only. Because record deletion
  // could be a potential denial of service problem,
  // execution of this method should be very limited.
  // No user facing interfaces should be allowed to call it.
  public Vector delete() {
    // Delete the record
    try {
      sendAndWait("delete -i " + getUserID() );
    } catch (SafewordRCException se) {
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

  //rollback transaction due to processing error
  //in initial transaction
  public Vector rollback() {

    try {

      if((requestV.elementAt(5).equals("RCENABLE")) || 
					(requestV.elementAt(5).equals("RCDELETE"))) {

        //restore token card
        sendAndWait( "import -f newcard.rec" );

      } else if(requestV.elementAt(5).equals("RCREPLACE")) {

        //restore user to pre-transaction value and
        //reset card to pre-transaction value
        sendAndWait( "import -f olduser.rec" );
        sendAndWait( "import -f newcard.rec" );
      } else {
        replyV.setElementAt(
					"Unable to process request type during rollback.", 0);
      }
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword rollback failed", 0);
      return replyV;
    }

    //if you get to here, the Safeword entry was
    //rolled back to its original value
    logAndNotify( getUserID() + ": Rollback RC");
    replyV.setElementAt("ROLLBACKCONFIRMED", 0);
    return replyV;

  }

  public Vector verifyAccount() {

    // Lock the existing User record
    try {
      sendAndWait("lock -i " + UserID );
    } catch (SafewordRCException se) {
      se.printStackTrace();
      log( getUserID() + ": UserID not found");
      //logAndNotify( getUserID() + ": UserID not found");
      replyV.setElementAt("UserID not found", 0);
      return replyV;
    }

    // Export the user record to a local file
    try {
      sendAndWait("get -i " + UserID + " -f olduser.rec");
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Get User record failed", 0 );
      return replyV;
    }

    if ( !getCardID() ) {
      return replyV;
    }

		try {
      sendAndWait( "unlock " + UserID );
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Unlock record failed", 0 );
      return replyV;
    }

    if( !CardID.toUpperCase().equals( OldCardNumber.toUpperCase() )) {
      replyV.setElementAt(
				"The card you entered does not match in the database.", 0 );

    	log( getUserID() + ": Failed Existing Account Verifcation");
    	//logAndNotify( getUserID() + ": Failed Existing Account Verifcation");
      return replyV;
		}

    log( getUserID() + ": Admin retrieved: Account Verified, PIN retrieved");
    //logAndNotify( getUserID() + ": Admin retrieved: Account Verified, PIN retrieved");
    replyV.setElementAt("ACCOUNTVERIFIED", 0);
    replyV.setElementAt("OldCardNumber", 1);
    return replyV;
  }

  public Vector deleteAccount() {

    // Export the assigned card record to a local file.
    //This is done in case a rollback is necessary
    try {
      sendAndWait("get -i " + UserID + " -f newcard.rec");
    } catch (SafewordRCException se) {
      se.printStackTrace();
      replyV.setElementAt("Safeword Get record failed", 0 );
      return replyV;
    }

    // delete the User record
    try {
      sendAndWait("delete " + UserID );
    } catch (SafewordRCException se) {
      se.printStackTrace();
      log( getUserID() + ": Delete unsuccessful; UserID not found");
      //logAndNotify( getUserID() + ": Delete unsuccessful; UserID not found");
      replyV.setElementAt("Delete unsuccessful; UserID not found", 0);
      return replyV;
    }

    //logAndNotify( getUserID() + ": Account Deleted");
    log( getUserID() + ": Account Deleted");
    replyV.setElementAt("DELETEVERIFIED", 0);
    return replyV;
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
					e.getMessage(), 1);
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
        RCErrorHandler seh = new RCErrorHandler("CLI_ERROR", 
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
      if (DEBUG) {
        System.out.println("From CLI ->" + text);
        log("From CLI ->" + text);
      }
      return text;
    } catch (Throwable t) {
      try {
        RCErrorHandler seh = new RCErrorHandler("CLI_ERROR", 
            "Server connection appears broken.  Please reconnect.");
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }
  }

  // Issue command to CLI and wait for reply
  private synchronized Vector sendAndWait(String s, int waitTime) 
            throws SafewordRCException {

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
      throw new SafewordRCException("ERROR Timeout in CLI connection.");

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
        throw new SafewordRCException(outputFromServer);
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
        throws SafewordRCException {
    return sendAndWait(s, DEFAULT_WAIT);
  }

  // Write to the CLI
  private boolean write(String s) {
    if (dos == null) {
      return false;
    }

    if( DEBUG ) {
      System.out.println("To CLI:> "+ s);
      log("To CLI:> "+ s);
    }

    try {
      dos.write((s+"\n").getBytes());
    }
    catch (Throwable t) {
      try {
        RCErrorHandler seh = new RCErrorHandler("CLI_ERROR", 
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

    File f1 = new File( "./olduser.rec" );
    File f2 = new File( "./newuser.rec" );
    File f3 = new File( "./newcard.rec" );

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

  // Strip $$ wrappers from a string
  public String unQuote( String s ) {
    if (s.length() >= 4 &&
        s.startsWith("$$") &&
        s.endsWith("$$"))
      return s.substring(2, s.length()-2);
    else
      return s;
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
    RCNotify n = new RCNotify( in );
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
      RCNotify n = new RCNotify("Can't open log file");
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


  // Debugging method and preserves records 
  // Should be turned off in production
  public void copyRecordFiles(String Recfile) {

    // Issue runtime command
    try {
      Runtime process = Runtime.getRuntime();
      String cmd = "cp ./olduser.rec ./etc/" + Recfile + ".rec\n" ;
      String cmd2 = "cp ./newuser.rec ./etc/" + Recfile + ".rec\n" ;
      try {
        mChild = process.exec(cmd);
        Thread.sleep(1000);
        mChild = process.exec(cmd2);
        Thread.sleep(1000);
      } catch (Throwable t) {
        try {
          RCErrorHandler seh = new RCErrorHandler("Copy Error:",
              "Error while copying record file" + cmd);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch(Throwable e) {
      try {
        RCErrorHandler seh = new RCErrorHandler("Copy Error:",
            "Error while execing copy command");
      } catch (IOException ee) {
        ee.printStackTrace();
      }
    }
  }

	public String[] parseStringRecord( String record ) {

    //Divide the string into tokens
    StringTokenizer sst = new StringTokenizer(record, "\n", false);

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
      replyV.addElement( "Could not open " + inFile );
      replyV.addElement( e.getClass().getName() + ": " +
          e.getMessage() );
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

  private String[] parseServices( String inString ) {

    String[] sa = new String[24];
    int j = 0;
    int count = 0;

    // Trim off header
    int i = inString.indexOf(";");
    inString = inString.substring( i+1 );

    while( count < 24 ) {
      if( ( i = inString.indexOf(";")) >= 0 ) {
				sa[ count ] = unQuote(inString.substring( 0, i ));
	  		inString = inString.substring( i+1 );
			} else {
	   		sa[ count ] = inString.substring( 2 );
			}
			count++;
    }

    return sa;
  }
}
