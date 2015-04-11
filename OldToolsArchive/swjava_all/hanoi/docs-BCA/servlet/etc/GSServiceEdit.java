import java.net.*;
import java.io.*;
import java.util.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import com.sun.net.ssl.*;
import java.security.KeyStore;

public class GSServiceEdit {

  private String RequestType = null;
  private String ReplyType = null;

  // User data
  private String UserID = null;
  private String FirstName = null;
  private String LastName = null;
  private String FullName = null;
  private String Comment = null;

  // Services allowed fields
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

  // Containers for the above variables
  private Vector requestVector = null;
  private Vector replyVector = null;

  // Constructor
  GSServiceEdit() {
    requestVector = new Vector();
    replyVector = new Vector();
  }

  // Returns all services allowed for
  // a user
  public boolean getServices( String uid ) {

    // Set the request type
    RequestType = new String("GETSERVICES");
    requestVector.addElement(RequestType); 

    // Initalize all values to null
    UserID = new String(uid);
    requestVector.addElement(UserID);
    Comment = new String("");
    requestVector.addElement(Comment);
    ModemPool = new String("");
    requestVector.addElement(ModemPool);
    HighSpeed = new String("");
    requestVector.addElement(HighSpeed);
    Eccpx = new String("");
    requestVector.addElement(Eccpx);
    Psc = new String("");
    requestVector.addElement(Psc);
    NsgSun = new String("");
    requestVector.addElement(NsgSun);
    Th = new String("");
    requestVector.addElement(Th);
    Icabad = new String("");
    requestVector.addElement(Icabad);
    Dialup = new String("");
    requestVector.addElement(Dialup);
    Safew = new String("");
    requestVector.addElement(Safew);
    Ite = new String("");
    requestVector.addElement(Ite);

    // Submit request to the Safeword server
    if( callSafewordAlcatraz() != true ) {
      return false;
    }
    if( callSafewordIronwood() != true ) {
      return false;
    }

    // Unpack the replyVector
    ReplyType = (String)replyVector.elementAt(0);

    if( ReplyType.equals("SERVICESRETURNED") ) {

      FullName = new String( (String)replyVector.elementAt(1) );
      Comment = new String( (String)replyVector.elementAt(2) );

      ModemPool = (String)replyVector.elementAt(3);
      HighSpeed = (String)replyVector.elementAt(4);
      Eccpx = (String)replyVector.elementAt(5);
      Psc = (String)replyVector.elementAt(6);
      NsgSun = (String)replyVector.elementAt(7);
      Th = (String)replyVector.elementAt(8);
      Icabad = (String)replyVector.elementAt(9);
      Dialup = (String)replyVector.elementAt(10);
      Safew = (String)replyVector.elementAt(11);
      Ite = (String)replyVector.elementAt(12);
    } 

    return true;
  }

  public boolean setServices() {

		requestVector = new Vector();

    // Populate the Vector with local variables
    // set by mutator methods from GUI
    requestVector.addElement("CHANGESERVICES");
    requestVector.addElement( UserID );
    requestVector.addElement( Comment );
    requestVector.addElement( ModemPool );
    requestVector.addElement( HighSpeed );
    requestVector.addElement( Eccpx );
    requestVector.addElement( Psc );
    requestVector.addElement( NsgSun );
    requestVector.addElement( Th );
    requestVector.addElement( Icabad );
    requestVector.addElement( Dialup );
    requestVector.addElement( Safew );
    requestVector.addElement( Ite );

    // Send the populated Vector to the 
    // Safeword server
    if( callSafewordAlcatraz() != true ) {
      return false;
    }
    if( callSafewordIronwood() != true ) {
      return false;
    }

    // Set the result from the transaction
    ReplyType = (String)replyVector.elementAt(0);
    return true;
  }

  // Utility method for contacting Safeword
  // server - alcatraz TO BE REMOVED SOON!
  private boolean callSafewordAlcatraz() {

    SSLSocket s1 = null;
    InputStream s1In = null;
    ObjectInputStream ois = null;

		// alcatraz
		String host = "129.147.30.70"; //CHECK!

		// Check port
    int port = 5432; //CHECK!

    try {

			SSLSocketFactory factory = null;

			try {
				SSLContext ctx;
				KeyManagerFactory kmf;
				KeyStore ks;

				// Password to the alias
        char[] passphrase = "password".toCharArray(); //CHECK!

				// Set keystore property variables
        System.setProperty(
					"javax.net.ssl.trustStore", 
					"/opt/netscape/server4/docs-BCA/ks"); //CHECK!

				// Set transport layer security and other settings
				ctx = SSLContext.getInstance("TLS");
				kmf = KeyManagerFactory.getInstance("SunX509");
				ks = KeyStore.getInstance("JKS");

				// Submit passphrase
        ks.load(
					new FileInputStream(
						"/opt/netscape/server4/docs-BCA/ks"), //CHECK!
						passphrase);

				kmf.init(ks, passphrase);
				ctx.init(kmf.getKeyManagers(), null, null);

				factory = ctx.getSocketFactory();
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}

			// Construct Socket and handshake with server
			s1 = (SSLSocket)factory.createSocket(host, port);
			s1.startHandshake();

    } catch (UnknownHostException uhe) {
      System.out.println("Can't get socket...");
      uhe.printStackTrace();
      return false;
    } catch (IOException e) {
      System.out.println("IOException in opening socket...");
      e.printStackTrace();
      return false;
    }

    OutputStream s1Out = null;

    // Open up output streams for shipping user data to the
    // server
    try {
      s1Out = s1.getOutputStream();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    ObjectOutputStream oos = null;

    try {
      oos = new ObjectOutputStream(s1Out);
    } catch (IOException e) {
      System.out.println(
          "IOException in constructing ObjectOutputStream...");
      e.printStackTrace();
      return false;
    }

    // Ship out the data
    try {
      oos.writeObject( requestVector );
    } catch (IOException e) {
      System.out.println("IOException when trying to write Object");
      e.printStackTrace();
      return false;
    }

    // Open input streams for replies from the server, 
    // regardless of the outcome
    try {
      s1In = s1.getInputStream();
    } catch (IOException e) {
      System.out.println(
          "IOException when trying to get InputStream");
      e.printStackTrace();
      return false;
    }

    try {
      ois = new ObjectInputStream(s1In);
    } catch (StreamCorruptedException sce) {
      System.out.println("Corrupted ObjectInputStream");
      sce.printStackTrace();
      return false;
    } catch (IOException e) {
      System.out.println(
          "IOException in ObjectInputStream construction");
      e.printStackTrace();
      return false;
    }

    try {
      replyVector = (Vector)ois.readObject();
    } catch ( OptionalDataException ode ) {
      System.out.println(
          "OptionalDataException when readObject of ois");
      ode.printStackTrace();
      return false;
    } catch ( ClassNotFoundException cnf) {
      System.out.println("Class Not Found in readObject");
      cnf.printStackTrace();
      return false;
    } catch (IOException e) {
      System.out.println("IO Exception in readObject");
      e.printStackTrace();
      return false;
    }

    // When done, just close the connection and exit
    try {
      ois.close();
    } catch (IOException e) {
      System.out.println("IOException in closing ois");
      e.printStackTrace();
      return false;
    }

    try {
      s1In.close();
    } catch (IOException e) {
      System.out.println("IOException in closing s1In");
      e.printStackTrace();
      return false;
    }

    try {
      s1.close();
    } catch (IOException e) {
      System.out.println("IOException in closing s1");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  // Utility method for contacting Safeword
  // server - ironwood
  private boolean callSafewordIronwood() {

    SSLSocket s1 = null;
    InputStream s1In = null;
    ObjectInputStream ois = null;

		// ironwood
		String host = "129.147.30.50"; //CHECK!

		// Check port
    int port = 5432; //CHECK!

    try {

			SSLSocketFactory factory = null;

			try {
				SSLContext ctx;
				KeyManagerFactory kmf;
				KeyStore ks;

				// Password to the alias
        char[] passphrase = "password".toCharArray(); //CHECK!

				// Set keystore property variables
        System.setProperty(
					"javax.net.ssl.trustStore", 
					"/opt/netscape/server4/docs-BCA/ks"); //CHECK!

				// Set transport layer security and other settings
				ctx = SSLContext.getInstance("TLS");
				kmf = KeyManagerFactory.getInstance("SunX509");
				ks = KeyStore.getInstance("JKS");

				// Submit passphrase
        ks.load(
					new FileInputStream(
						"/opt/netscape/server4/docs-BCA/ks"), //CHECK!
						passphrase);

				kmf.init(ks, passphrase);
				ctx.init(kmf.getKeyManagers(), null, null);

				factory = ctx.getSocketFactory();
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}

			// Construct Socket and handshake with server
			s1 = (SSLSocket)factory.createSocket(host, port);
			s1.startHandshake();

    } catch (UnknownHostException uhe) {
      System.out.println("Can't get socket...");
      uhe.printStackTrace();
      return false;
    } catch (IOException e) {
      System.out.println("IOException in opening socket...");
      e.printStackTrace();
      return false;
    }

    OutputStream s1Out = null;

    // Open up output streams for shipping user data to the
    // server
    try {
      s1Out = s1.getOutputStream();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    ObjectOutputStream oos = null;

    try {
      oos = new ObjectOutputStream(s1Out);
    } catch (IOException e) {
      System.out.println(
          "IOException in constructing ObjectOutputStream...");
      e.printStackTrace();
      return false;
    }

    // Ship out the data
    try {
      oos.writeObject( requestVector );
    } catch (IOException e) {
      System.out.println("IOException when trying to write Object");
      e.printStackTrace();
      return false;
    }

    // Open input streams for replies from the server, 
    // regardless of the outcome
    try {
      s1In = s1.getInputStream();
    } catch (IOException e) {
      System.out.println(
          "IOException when trying to get InputStream");
      e.printStackTrace();
      return false;
    }

    try {
      ois = new ObjectInputStream(s1In);
    } catch (StreamCorruptedException sce) {
      System.out.println("Corrupted ObjectInputStream");
      sce.printStackTrace();
      return false;
    } catch (IOException e) {
      System.out.println(
          "IOException in ObjectInputStream construction");
      e.printStackTrace();
      return false;
    }

    try {
      replyVector = (Vector)ois.readObject();
    } catch ( OptionalDataException ode ) {
      System.out.println(
          "OptionalDataException when readObject of ois");
      ode.printStackTrace();
      return false;
    } catch ( ClassNotFoundException cnf) {
      System.out.println("Class Not Found in readObject");
      cnf.printStackTrace();
      return false;
    } catch (IOException e) {
      System.out.println("IO Exception in readObject");
      e.printStackTrace();
      return false;
    }

    // When done, just close the connection and exit
    try {
      ois.close();
    } catch (IOException e) {
      System.out.println("IOException in closing ois");
      e.printStackTrace();
      return false;
    }

    try {
      s1In.close();
    } catch (IOException e) {
      System.out.println("IOException in closing s1In");
      e.printStackTrace();
      return false;
    }

    try {
      s1.close();
    } catch (IOException e) {
      System.out.println("IOException in closing s1");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  // Mutator methods

  public void setRequestType( String in ) {
    RequestType = new String( in );
  }

  public void setUserID( String in ) {
    UserID = new String( in );
  }

  public void setComment( String in ) {
    Comment = new String( in );
  }

  public void setModemPool( String in ) {
    ModemPool = new String( in );
  }

  public void setHighSpeed( String in ) {
    HighSpeed = new String( in );
  }

  public void setEccpx( String in ) {
    Eccpx = new String( in );
  }

  public void setPsc( String in ) {
    Psc = new String( in );
  }

  public void setNsgSun( String in ) {
    NsgSun = new String( in );
  }

  public void setTh( String in ) {
    Th = new String( in );
  }

  public void setIcabad( String in ) {
    Icabad = new String( in );
  }

  public void setDialup( String in ) {
    Dialup = new String( in );
  }

  public void setSafew( String in ) {
    Safew = new String( in );
  }

  public void setIte( String in ) {
    Ite = new String( in );
  }


  // Accessor methods

  public String getReplyType() {
    return ReplyType;
  }

  public String getUserID() {
    return UserID;
  }

  public String getFirstName() {
    return FirstName;
  }

  public String getLastName() {
    return LastName;
  }

  public String getFullName() {
    return FullName;
  }

  public String getComment() {
    return Comment;
  }

  public String getModemPool() {
    return ModemPool;
  }

  public String getHighSpeed() {
    return HighSpeed;
  }

  public String getEccpx() {
    return Eccpx;
  }

  public String getPsc() {
    return Psc;
  }

  public String getNsgSun() {
    return NsgSun;
  }

  public String getTh() {
    return Th;
  }

  public String getIcabad() {
    return Icabad;
  }

  public String getDialup() {
    return Dialup;
  }

  public String getSafew() {
    return Safew;
  }

  public String getIte() {
    return Ite;
  }

}
