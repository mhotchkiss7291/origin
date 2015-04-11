import java.lang.*;
import java.io.*;
import java.util.*;
import com.sun.authtech.scp.*;

public class Ac {

  public void go() {

		Properties AcProps = new Properties();
		try {
			FileInputStream in = new FileInputStream("ac.props");
			AcProps.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String hostname = AcProps.getProperty("hostname");
		String remoteUsername = AcProps.getProperty("username");
		String remotePassword = remoteUsername;
		String remotePath = AcProps.getProperty("remotePath");

		// Do the file operations
		String today =  
			( new java.sql.Date( System.currentTimeMillis())).toString();

		String remoteFile = remotePath + hostname + "." + today;
		String localFile = "./" + hostname + "." + today ;

		File tempfile = new File("./tempaccesslog");
		File timestampfile = new File( localFile );

		tempfile.renameTo( timestampfile );

		Properties ScpProps = new Properties();
		try {
			FileInputStream in = new FileInputStream("scp.props");
			ScpProps.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Begin scp operations
		StringWriter theContent = new StringWriter();
		PrintWriter theContentOut = new PrintWriter(theContent);

		try {
			Thread.sleep( 5000 );
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Read the compressed file into a byte array
		FileToArrayList fal = new FileToArrayList();
		ArrayList al = fal.getArrayList( localFile );

    int i = 0;

		while( i < al.size() ) {

      theContentOut.println( (String)al.get( i ) ) ;
      i++;

    }

		SCPClient scpClient = null;

		try {
			scpClient = new SCPClient(ScpProps);
		} catch (Exception e) {
			e.printStackTrace();
		}

		scpClient.setUsername(remoteUsername);
		scpClient.setPassword(remotePassword);

		boolean PUSH_SUCCESSFUL = true;
		
		try {
			scpClient.put(
				new ByteArrayInputStream(
					theContent.toString().getBytes()), remoteFile);
		} catch (Exception e ) {
			PUSH_SUCCESSFUL = false;
			e.printStackTrace();
		}

		if( PUSH_SUCCESSFUL ) {
			timestampfile.delete();
		} else {
			Notify n = new Notify("Sun.Net logs NOT pushed from alcatraz");
			try {
				n.send();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
  }

  public static void main( String args[] ) {

    Ac ac  = new Ac();
    ac.go();
  }

}
