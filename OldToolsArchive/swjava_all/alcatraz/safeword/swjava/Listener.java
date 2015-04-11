import java.net.*;
import javax.net.ssl.*;
import javax.net.*;
import java.util.*;
import java.security.*;
import java.io.*;
import com.sun.net.ssl.*;

public class Listener {

  // Local I/O variables
  //ServerSocket ss = null;
  SSLServerSocket ss = null;
  ObjectOutputStream oos = null;
  ObjectInputStream ois = null;
  String requestType = null;
  Vector requestVector = null;
  Vector replyVector = null;
  boolean DEBUG = false;

  // Method that does the connections setup
  public void go() {

    // I/O variables
    //Socket s1 = null;
    SSLSocket s1 = null;
    InputStream s1in = null;
    OutputStream s1out = null;

    // Check for all necessary files before 
    // starting up
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

    // Construct containers
    requestVector = new Vector();
    replyVector = new Vector();

    // Get IP and port data from local file
    Vector HostInfo = new Vector();
    HostInfo = getHostInfo();
    String IP = (String)HostInfo.elementAt(0);
    int port = Integer.parseInt( (String)HostInfo.elementAt(1) );

    // Open the Secure Socket
    try {
      SSLServerSocketFactory ssf = getServerSocketFactory();
      ss = (SSLServerSocket)ssf.createServerSocket( port );
      ss.setNeedClientAuth(true);
    } catch (IOException e) {
      System.out.println("Unable to : " + e.getMessage());
      e.printStackTrace();
    }

    // Infinite listening loop begins
    while (true) {
      try {

        // Wait here and listen for a connection
        s1 = (SSLSocket)ss.accept();
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
        // Read the Vector
        try {
          requestVector = (Vector)ois.readObject();
        } catch (ClassNotFoundException e) {
          System.out.println("Class not found in readObject");
          e.printStackTrace();
        }
        // Setup for output 
        try {
          s1out = s1.getOutputStream();
        } catch (IOException e) {
          System.out.println("IOException getOutputStream");
          e.printStackTrace();
        }
        oos = new ObjectOutputStream(s1out);
      } catch (IOException e) { 
        System.out.println("Input/Output Stream error" );
      }
      processRequest();
      reply();
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
					"/usr/safeword/swjava_emp/ks"); //CHECK!

      ctx = SSLContext.getInstance("TLS");
      kmf = KeyManagerFactory.getInstance("SunX509");
      ks = KeyStore.getInstance("JKS");

      ks.load(
				new FileInputStream(
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


  // Flow control for various request types
  private void processRequest() {

    requestType = getRequestType();

    if(DEBUG)System.out.println("requestType = " + requestType);

    if( requestType.equals("GETSERVICES") ) {
      RCRecord sr = new RCRecord( requestVector );
      sr.deleteRecFiles();
      replyVector = sr.connectToServer();
      replyVector = sr.getServices(); 
      replyVector = sr.logout();

    } else if( requestType.equals("CHANGESERVICES") ) {
      RCRecord sr = new RCRecord( requestVector );
      sr.deleteRecFiles();
      replyVector = sr.connectToServer();
      replyVector = sr.setServices(); 
      replyVector = sr.logout();

    } else if( requestType.equals("VERIFYACCOUNT") ) {
      RCRecord sr = new RCRecord( requestVector );
      sr.deleteRecFiles();
      replyVector = sr.connectToServer();
      replyVector = sr.verifyAccount(); 
      replyVector = sr.logout();

    } else if( requestType.equals("DELETEACCOUNT") ) {
      RCRecord sr = new RCRecord( requestVector );
      sr.deleteRecFiles();
      replyVector = sr.connectToServer();
      replyVector = sr.deleteAccount(); 
      replyVector = sr.logout();

    } else if( requestType.equals("RCENABLE") ) {
      RCRecord sr = new RCRecord( requestVector );
      sr.deleteRecFiles();
      replyVector = sr.connectToServer();
      replyVector = sr.enable();
      replyVector = sr.logout();

    } else if( requestType.equals("RCREPLACE") ) {
      RCRecord sr = new RCRecord( requestVector );
      sr.deleteRecFiles();
      replyVector = sr.connectToServer();
      replyVector = sr.replace();
      replyVector = sr.logout();

    } else if(requestType.equals("RCROLLBACK")) { 	
      RCRecord sr = new RCRecord( requestVector );
      replyVector = sr.connectToServer();
      String rollbackType = getRollbackType();

      if (!(rollbackType.equals("RCDELETE"))) {

				//delete existing record
        replyVector = sr.delete();

      }

			//use .rec files to rollback
      replyVector = sr.rollback(); 
      replyVector = sr.logout();   
      sr.deleteRecFiles();

//*******************New Code*********************************
    } else if(requestType.equals("RCREPORT")) {

			//subclass of RCRecord
  		SWReport sr = new SWReport( requestVector );     
      replyVector = sr.connectToServer();

      //current time used to create unique file names
      long now = System.currentTimeMillis();

      sr.getAllRecords(now);
      replyVector = sr.logout();

      replyVector = sr.filterRecords(now);
      sr.deleteRecFiles(now);
    }
//************************************************************

  }

  private void reply() {
    try {
      // Write the reply from safeword operations
      // back to the client website
      oos.writeObject( replyVector );
    } catch (IOException e) {
      System.out.println("IO Exception writeObject" );
      e.printStackTrace();
    }
  }

  private void closeConnection( 
      ObjectOutputStream oos, 
      OutputStream sout,
      Socket s ) {

    try {
      oos.close();
      sout.close();
      ss.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Return type of request from the client
  public String getRequestType() {
    return( (String)requestVector.elementAt(0) );
  }

  //Return type of rollback from the client
  public String getRollbackType() {
    return( (String)requestVector.elementAt(5) );
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

    int FirstIndex = DataString.indexOf("\n");
    int SecondIndex = DataString.indexOf("\n", FirstIndex + 1 );

    IP = DataString.substring( 0, FirstIndex );
    Port = DataString.substring( FirstIndex + 1, SecondIndex );

    Vector v = new Vector();

    v.addElement( IP );
    v.addElement( Port );

    return v;
  }

  // Entry point
  public static void main( String args[]) {
    Listener l = new Listener();
    l.go();
  }
}
