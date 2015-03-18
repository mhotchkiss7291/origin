import java.net.*;
import java.util.*;
import java.io.*;
import java.security.*;
import javax.net.ssl.*;
import javax.net.*;
import com.sun.net.ssl.*;


public class EMAListener {

  // Method that does all the work
  public void go() {

    // I/O variables
    SSLServerSocket s = null;
    SSLSocket s1 = null;
    InputStream s1in = null;
    ObjectInputStream ois = null;
    OutputStream s1out = null;
    ObjectOutputStream oos = null;
    String requestType = null;

    // Check for special runtime files such as logs and 
		// email addresses
    File f = new File("./etc/log");
    if( !f.exists() ) {
      System.out.println("Log file does not exist");
      System.exit(0);
    }
    File f2 = new File("./etc/dat1");
    if( !f2.exists() ) {
      System.out.println("dat1 file does not exist");
      System.exit(0);
    }
    File f3 = new File("./etc/dat2");
    if( !f3.exists() ) {
      System.out.println("dat2 file does not exist");
      System.exit(0);
    }
    File f4 = new File("./etc/recipients");
    if( !f4.exists() ) {
      System.out.println("recipients file does not exist");
      System.exit(0);
    }

    // Construct containers for data shipment
    Vector request = new Vector();
		request.setSize( 6 );
    Vector reply = new Vector();
		reply.setSize( 1 );

    // Get IP and port data from local file
    Vector HostInfo = new Vector();
    HostInfo = getHostInfo();
    String IP = (String)HostInfo.elementAt(0);
    int port = Integer.parseInt( (String)HostInfo.elementAt(1) );

    //Create SSLSocketFactory
    try {
      SSLServerSocketFactory ssf = getServerSocketFactory();
      s = (SSLServerSocket)ssf.createServerSocket( port );
      ((SSLServerSocket)s).setNeedClientAuth(true);
    } catch (IOException e) {
        System.out.println("Unable to : " + e.getMessage());
        e.printStackTrace();
    }

    // Infinite listening loop begins
    while (true) {

      try {

        // Wait here and listen for a connection
        s1 = (SSLSocket)s.accept();
        String addr = s1.getInetAddress().toString();

        // Setup input
        try {
          s1in = s1.getInputStream();
        } catch (IOException e) {
          System.out.println("Exception in getInputStream");
          e.printStackTrace();
        }
        try {
          ois = new ObjectInputStream (s1in);
        } catch (StreamCorruptedException sce) {
          System.out.println("Corrupted ObjectInputStream");
          sce.printStackTrace();
        } catch (IOException e) {
          System.out.println("IOException ObjectInputStream");
          e.printStackTrace();
        }

        // Read the input Vector
        try {
          request = (Vector)ois.readObject();
        } catch (ClassNotFoundException e) {
          System.out.println("Class not found in readObject");
          e.printStackTrace();
        }

        // Qualify the Safeword transaction type
        requestType = getRequestType( request );

        // For ENABLE transactions
        if( requestType.equals("ENABLE") ) {

          // Construct the record
          EMARecord sr = new EMARecord( request );

          // Check the CardID against convention
          if(  sr.validateCardID() ) {

            // Do the enable transaction
            sr.deleteRecFiles();
            reply = sr.connectToServer();
            reply = sr.enable();
            reply = sr.logout();
            //sr.deleteRecFiles();

          } else {

            reply.setElementAt("Illegal Card Number", 0 );
            // Log if illegal card number was entered
            sr.log( sr.getUserID() + ": Illegal Card Number" );
          }
        }

        // For PIN retrival transactions
        if( requestType.equals("VERIFYACCOUNT") ) {

          // Construct the record
          EMARecord sr = new EMARecord( request );

          // Check the CardID against convention
          if(  sr.validateCardID() ) {

            // Do the enable transaction
            sr.deleteRecFiles();
            reply = sr.connectToServer();
            reply = sr.verifyAccount();
            reply = sr.logout();
       //     sr.deleteRecFiles();

          } else {

            reply.setElementAt("Illegal Card Number", 0 );
            // Log if illegal card number was entered
            sr.log( sr.getUserID() + ": Illegal Card Number" );
          }
        }

        // For DELETE transactions
        if( requestType.equals("DELETE") ) {

          // Construct the record
          EMARecord sr = new EMARecord( request );

          // Check the UserID against convention
          if(  sr.validateUserID() ) {
            reply = sr.connectToServer();
            reply = sr.delete();
            reply = sr.logout();
          } else {
            reply.setElementAt("Illegal UserID", 0 );

            // Log and send notification if user was not
            // deleted. Possible security problem flagged.
            sr.logAndNotify( sr.getUserID() + ": Illegal UserID");
          }
        }

        // For card REPLACEMENT transactions
        if( requestType.equals("REPLACE") ) {

          // Construct the record
          EMARecord sr = new EMARecord( request );

          // Check the CardID against convention
          if(  sr.validateCardID() ) {
            // Do the enable transaction
            sr.deleteRecFiles();
            reply = sr.connectToServer();
            reply = sr.replace();
            reply = sr.logout();
            //sr.deleteRecFiles();

          } else {

            reply.setElementAt("Illegal Card Number", 0 );
            // Log if illegal card number was entered
            sr.log( sr.getUserID() + ": Illegal Card Number" );
          }
        }

				//For ROLLBACK transaction.
				//Only called if transaction on second database fails.
				//First database will be rolled back to pre-transaction state.

        if (requestType.equals("ROLLBACK")) {

					//Construct the record
					EMARecord sr = new EMARecord(request);

					//Do the rollback transaction
					reply = sr.connectToServer();
					reply = sr.delete();    //delete existing record 
					reply = sr.rollback();  //use .rec files to rollback
					reply = sr.logout();

					sr.deleteRecFiles();		
        }

        // Setup for output 
        try {
          s1out = s1.getOutputStream();
        } catch (IOException e) {
          System.out.println("IOException getOutputStream");
          e.printStackTrace();
        }

        oos = new ObjectOutputStream(s1out);

        try {

          // Write the reply from safeword operations
          // back to the client website
          oos.writeObject( reply );

        } catch (IOException e) {
          System.out.println("IO Exception writeObject" );
          e.printStackTrace();
        }

        // Close the connection, but not the server socket
        oos.close();
        s1out.close();
        ois.close();
        s1in.close();
        s1.close();
      } catch (IOException e) { 
        System.out.println("Listen exception.." );
      }
    }
  }


  private static SSLServerSocketFactory getServerSocketFactory() {

    SSLServerSocketFactory ssf = null;

    try {

      // set up key manager to do server authentication
      SSLContext ctx;
      KeyManagerFactory kmf;
      KeyStore ks;
      char[] passphrase = "password".toCharArray(); //CHECK!

      System.setProperty(
				"javax.net.ssl.trustStore", 
				"/usr/safeword/swjava_emp/ks") //CHECK!
;

      ctx = SSLContext.getInstance("TLS");
      kmf = KeyManagerFactory.getInstance("SunX509");
      ks = KeyStore.getInstance("JKS");

      ks.load(new FileInputStream(
				"/usr/safeword/swjava_emp/ks"), //CHECK!
				passphrase);

      kmf.init(ks, passphrase);
      ctx.init(kmf.getKeyManagers(), null, null);

      ssf = ctx.getServerSocketFactory();
      return ssf;
    } catch (Exception e) {
       e.printStackTrace();
    }
    return (SSLServerSocketFactory)ServerSocketFactory.getDefault();
	}

  // Return type of request from the client
  public String getRequestType( Vector v ) {
    return( (String)v.elementAt(0) );
  }

  // Gets IP and hostname from local protected directory
  private Vector getHostInfo() {
    File f;
    FileReader in = null;
    String directory = "./etc";
    String filename = "dat1";
    String DataString = null;
    String IP = null;
    String Port = null;

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
      System.out.println( e.getClass().getName() + ": " + e.getMessage());
    } finally {
      try {
        if( in != null ) {
          in.close();
        }
      } catch (IOException e ) {
      }
    }

    int FirstIndex = DataString.indexOf("\n");
    int SecondIndex = DataString.indexOf("\n", FirstIndex + 1 );

    IP = DataString.substring( 0, FirstIndex );
    Port = DataString.substring( FirstIndex + 1, SecondIndex );

    Vector v = new Vector();
		v.setSize( 2 );

    v.setElementAt( IP, 0 );
    v.setElementAt( Port, 1 );

    return v;
  }

  // Entry point
  public static void main( String args[]) {
    EMAListener sl = new EMAListener();
    sl.go();
  }
}
