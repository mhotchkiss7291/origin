import java.io.*;

// Error handling utility

public class RCAErrorHandler {

  RCAErrorHandler( String status, String message )
      throws IOException {

    if( status.equals("CLI_ERROR") ) {
      RCANotify n = new RCANotify( message );
      try {
        n.send();
      } catch (IOException e ) {
        e.printStackTrace();
      }
    }

    if( status.equals("SECURITY_EMERGENCY") ) {
      RCANotify n = new RCANotify( message );
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
