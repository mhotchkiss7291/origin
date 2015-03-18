import java.net.*;
import javax.net.ssl.*;
import javax.net.*;
import java.util.*;
import java.security.*;
import java.io.*;
import com.sun.net.ssl.*;

public class Listen {

  // Method that does all the work
  public void go() {

    // I/O variables
    ServerSocket s = null;
    Socket s1 = null;
    InputStream s1in = null;
    ObjectInputStream ois = null;
    OutputStream s1out = null;
    ObjectOutputStream oos = null;
    String requestType = null;

    // Construct containers for data shipment
    Vector request = new Vector();
    request.setSize( 6 );
    Vector reply = new Vector();
		reply.addElement("This is the server's reply");

		ServerSocket ss = null;

		try {
      ServerSocketFactory ssf = getServerSocketFactory();
      ss = ssf.createServerSocket( 5432 );
      ((SSLServerSocket)ss).setNeedClientAuth(true);
    } catch (IOException e) {
      System.out.println("Unable to : " + e.getMessage()); 
			e.printStackTrace();
    }

    // Infinite listening loop begins
    while (true) {

      try {

        // Wait here and listen for a connection
        s1 = ss.accept();

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

        System.out.println("Request is: " + requestType );

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

	private static ServerSocketFactory getServerSocketFactory() {

		SSLServerSocketFactory ssf = null;

		try {

			// set up key manager to do server authentication
			SSLContext ctx;
			KeyManagerFactory kmf;
			KeyStore ks;
			char[] passphrase = "serverpw".toCharArray();

			System.setProperty("javax.net.ssl.trustStore", 
						"/usr/sslserver/serverkeystore");

			ctx = SSLContext.getInstance("TLS");
			kmf = KeyManagerFactory.getInstance("SunX509");
			ks = KeyStore.getInstance("JKS");

			ks.load(new FileInputStream("serverkeystore"), passphrase);
			kmf.init(ks, passphrase);
			ctx.init(kmf.getKeyManagers(), null, null);

			ssf = ctx.getServerSocketFactory();
			return ssf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ServerSocketFactory.getDefault();
  }

  // Return type of request from the client
  public String getRequestType( Vector v ) {
    return( (String)v.elementAt(0) );
  }

  // Entry point
  public static void main( String args[]) {
    Listen sl = new Listen();
    sl.go();
  }
}
