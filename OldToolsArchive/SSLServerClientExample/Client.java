import java.net.*;
import java.io.*;
import java.util.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import com.sun.net.ssl.*;
import java.security.KeyStore;

public class Client {

  public static void main(String[] args) throws Exception {

		// Designate the host and port to connect to
    String host = "buster.central.sun.com";
    int port = 5432;

		// Prepare the cargo to be sent
		Vector requestVector = new Vector();
		requestVector.addElement( "This is my request" );

		// Pepare a container for the reply
		Vector replyVector = new Vector();

		// Open the SSL Connection
    try {

      SSLSocketFactory factory = null;

      try {
        SSLContext ctx;
        KeyManagerFactory kmf;
        KeyStore ks;

				// Password to the alias
        char[] passphrase = "clientpw".toCharArray();

				// Set keystore property variables
				System.setProperty("javax.net.ssl.trustStore", 
            "/usr/sslclient/clientkeystore");

				// Set transport layer security and other settings
        ctx = SSLContext.getInstance("TLS");
        kmf = KeyManagerFactory.getInstance("SunX509");
        ks = KeyStore.getInstance("JKS");

				// Submit passphrase
        ks.load(new FileInputStream("clientkeystore"), passphrase);

        kmf.init(ks, passphrase);
        ctx.init(kmf.getKeyManagers(), null, null);

        factory = ctx.getSocketFactory();
      } catch (Exception e) {
        throw new IOException(e.getMessage());
      }

			// Construct Socket and handshake with server
      SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
      socket.startHandshake();

			// Now that connection is open, set up I/O streams
			InputStream s1In = null;
			ObjectInputStream ois = null;
			OutputStream s1Out = null;

			// Open up output streams
			try {
				s1Out = socket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}

			ObjectOutputStream oos = null;

			try {
				oos = new ObjectOutputStream(s1Out);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Ship out the data
			try {
				oos.writeObject( requestVector );
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Open input streams for replies from the server,
			// regardless of the outcome
			try {
				s1In = socket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				ois = new ObjectInputStream(s1In);
			} catch (StreamCorruptedException sce) {
				sce.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Read the reply
			try {
				replyVector = (Vector)ois.readObject();
			} catch ( OptionalDataException ode ) {
				ode.printStackTrace();
			} catch ( ClassNotFoundException cnf) {
				cnf.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// When done, just close the connection and exit
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				s1In.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// End of inserted code

      socket.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

		// Echo the reply back from the server
		System.out.println("Reply is: " + replyVector);
  }
}
