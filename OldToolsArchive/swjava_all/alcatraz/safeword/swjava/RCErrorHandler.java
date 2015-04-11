import java.io.*;

// Error handling utility

public class RCErrorHandler {

  RCErrorHandler( String status, String message )
      throws IOException {

    if( status.equals("CLI_ERROR") ) {
      RCNotify n = new RCNotify( message );
      try {
        n.send();
      } catch (IOException e ) {
        e.printStackTrace();
      }
    }

    if( status.equals("SECURITY_EMERGENCY") ) {
      RCNotify n = new RCNotify( message );
      try {
        n.send();
        System.out.println("SECURITY_EMERGENCY: " + status);
      } catch (IOException e ) {
        e.printStackTrace();
      }

      // Getting out for now
      System.exit(0);
    }
  }
  
}
