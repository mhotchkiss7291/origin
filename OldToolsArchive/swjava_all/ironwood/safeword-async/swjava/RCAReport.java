//This class is a subclass of SWRecord.  It is able to 
//send and received strings to/from the command line.
//The results are then placed in a vector and sent back
//to the client.
 
import java.io.*;
import java.util.*;

public class RCAReport extends RCARecord {

  // Private members
  private String RequestType = null;
  private String ReplyType = null;

  // Containers
  private Vector requestV = null;
  private Vector replyV = null;

  // I/O objects
  private Process mChild = null;
  private BufferedReader reader = null;
  private BufferedInputStream bis = null;
  private BufferedOutputStream bos = null;
  static final boolean DEBUG = false;    //CHECK

  private long now = 0;
  
  RCAReport(Vector in) {
    requestV = new Vector();
    requestV = in;
    replyV = new Vector( );

    // Get the service type
    RequestType = new String( (String)requestV.elementAt(1));

  }


  // Read response from command line
  private String read(int waitsec) {
    if (bis == null) {
      return null;
    }
    try {
      bos.flush();
    } catch(IOException e) {
       e.printStackTrace();
    }

    try {
      // Wait for data to be available for reading.
      for (int i = 0; bis.available() == 0 && i < 10*waitsec; i++) {
        Thread.sleep(100);
      }

      // If there was no data to read then just return.
      if (bis.available() == 0) {
        return null;
      }

      // Read a single line of text from the command line.
      byte[] sText = new byte[20000];
      byte c;
      int nIndex;
      String sep = System.getProperty("line.separator");
      c = (byte)bis.read();

      while (sep.indexOf(c) >= 0)
        c = (byte)bis.read();
       for (nIndex = 0; sep.indexOf(c) < 0; c = (byte)bis.read())
        sText[nIndex++] = c;
      String text = new String(sText, 0, nIndex);

      if (DEBUG) {
        System.out.println("From CLI ->" + text);
        log("From CLI ->" + text);
      }

      return text;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } catch (InterruptedException ie) {
      ie.printStackTrace();
      return null;
    }  
  }


  // Send string to command line and wait for reply
  private synchronized Vector sendAndWait(String s, int waitTime) 
            throws RCASafewordException {

    String outputFromServer = "";
    Vector d = new Vector();

    // Send the string to the command line.
    if (!write(s))
      return null;

    // Read the responses from the command line and put each returned line
    // in a Vector.
    for(int x=0; (outputFromServer = read(waitTime)) != null; x++) {
      d.setElementAt(outputFromServer, x);
    }
    
    // If we never got anything then just return null.
    if (d.isEmpty()) {
      return null;
    }
    
    // Return the responses.
    return d;
  }


  // Write to the command line
  private boolean write(String s) {
    if (bos == null) {
      return false;
    }

    if( DEBUG ) {
      System.out.println("To command line:> "+ s);
      log("To command line:> "+ s);
    }

    try {
      bos.write((s+"\n").getBytes());
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }


//Method greps through all-records file looking for selected service.
//The record is then parsed to get the user's name.  
  public Vector filterRecords(long time) {

    now = time;

    replyV.setSize(1);

    // Issue runtime command
    Runtime process = Runtime.getRuntime();
    String cmd = "csh"; //CHECK!

    try {
      mChild = process.exec(cmd);
      Thread.sleep(1000);
    } catch (IOException e) {
      e.printStackTrace();
      replyV.setElementAt("Error getting runtime for filtering.", 0);
      return replyV;
    } catch (InterruptedException e) {
      e.printStackTrace();
      replyV.setElementAt("Error getting runtime for filtering.", 0);
      return replyV;
    }

    // Open streams
    bos = new BufferedOutputStream (mChild.getOutputStream());
    bis = new BufferedInputStream(mChild.getInputStream());

    try {
      sendAndWait("grep =" + RequestType   
                    + " /usr/safeword-async/swjava/RecsAll" + now + ".rec" 
                    + " > /usr/safeword-async/swjava/RecsFiltered" + now + ".rec", 3); 		//CHECK
    } catch (RCASafewordException e) {
      System.out.println("Safeword Exception thrown: " + e);
      replyV.setElementAt("Error filtering records from RecsAll file", 0); 
      return replyV;
    }

    try {
      reader = 
				new BufferedReader(
					new FileReader(
						"/usr/safeword-async/swjava/RecsFiltered" + now + ".rec"));
		//CHECK
    } catch (FileNotFoundException fnf) {
       System.out.println(fnf + "\n");
       replyV.setElementAt("Error reading RecsFiltered file", 0);
       return replyV;
    }


    String[] UserData = null;
    try {
      int pos = 1;
      String output = "";

      //fill vector with names if any returned 
      if((output = reader.readLine()) != null) {
        while ((output = reader.readLine()) != null) {

          output.concat("\n");
          UserData = parseStringRecord(output);

          if (UserData[4] != null) {
            replyV.add(pos, UserData[4]);
            pos++;
          }
        }
        reader.close();

      } else {
       replyV.setElementAt("no data", 0);

        String Email = (String)requestV.elementAt(2);
   
	if(Email.length() >= 3){
          RCASendEmail se = null;
          se = new RCASendEmail(Email, replyV, RequestType);

          try{
          se.send();
          } catch (IOException e ) {
          e.printStackTrace();
         }
        }



	return replyV;
      }
    } catch (IOException e) {
       System.out.println("Error reading RecsFiltered file.");
       System.out.println(e+ "\n");
       replyV.setElementAt("Error writing user names to return vector", 0);
       return replyV;
    }

    // Made it!!
    replyV.setElementAt("REPORTCONFIRMED", 0);
    String Email = (String)requestV.elementAt(2);


    if(Email.length() >= 3){
	RCASendEmail se = null;
	se = new RCASendEmail(Email, replyV, RequestType);
	
        try{
          se.send();
	} catch (IOException e ) {
	  e.printStackTrace();
	}
      }

    return replyV;
  } //end of filterRecords


  // Unlink any local record files, if they exist
  public void deleteRecFiles(long time) {

		//CHECK
    File f1 = new File( 
			"/usr/safeword-async/swjava/RecsAll" + now + ".rec" );

		//CHECK
    File f2 = new File( 
			"/usr/safeword-async/swjava/RecsFiltered" + now + ".rec" );

    if( f1.exists() ) {
      f1.delete();
    }

    if( f2.exists() ) {
      f2.delete();
    }
  }

}
