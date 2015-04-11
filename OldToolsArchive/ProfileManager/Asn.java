import java.io.*;
import java.lang.*;
import java.util.*;
import com.sun.authtech.scp.*;

public class Asn {

	public void go( long backdays ) {

		// CHECK location of profile directory
		String rundir = "/safedir/snauthd/var/opt/iws4.1/https-auth/profiles";
		File dir = new File( rundir );

    String[] sa = dir.list();
		FileWriter out = null;
    File f = null;
    int i = 0;

		Calendar nowcal = Calendar.getInstance();
		Date nowdate = nowcal.getTime();
		long nowtime = nowdate.getTime();

		long thentime = nowtime - (backdays * 24 * 60 * 60 * 1000);

		String filedata = null;
		String roledata = null;
		int rolestartindex = 0;
    int roleendindex = 0;

		ArrayList outputList = new ArrayList();

		// For every file in the directory
    while( i < sa.length ) {

      f = new File( rundir, sa[i] );

			// If the file is younger than "thentime"
			if( thentime <= f.lastModified() ) {

				filedata = open( f.getAbsolutePath() );

				// If it doesn't contain "role=employee"
				if( filedata.indexOf("role=employee") == -1)  {

					// If it has a role, get its value
					if( (rolestartindex = 
							filedata.indexOf( "role=" )) != -1 ) {

						roleendindex = filedata.indexOf( "\n", rolestartindex );
						roledata = filedata.substring( rolestartindex + 5, roleendindex );

						outputList.add( f.getName() + ":" + roledata );

					}
				}
			}

      i++;
    }

		StringWriter theContent = new StringWriter();
		PrintWriter theContentOut = new PrintWriter(theContent);

		i = 0;

		if( outputList.isEmpty() ) {
			theContentOut.println("No changes");
		} else {

			while( i < outputList.size() ) {

				theContentOut.println( (String)outputList.get( i ) );
				i++;

			}
		}

		Properties config = new Properties();

		try {

			//CHECK
			config.load(
				new FileInputStream(
					"/safedir/snauthd/var/opt/ProfileManager/SendBytes.config"));
		} catch (Exception e ) {
			e.printStackTrace();
		}

		// CHECK Username and password
		String remoteUsername = "profmgr";
		String remotePassword = "profmgr";

		//CHECK destination path
		String remoteFile = 
			"/export/home/profmgr/LoadProfileUpdates/alcatrazData/userrole_" + 
				(new java.sql.Date( System.currentTimeMillis())).toString(); 
;

		SCPClient scpClient = null;

		try {
			scpClient = new SCPClient(config);
		} catch (Exception e) {
			e.printStackTrace();
		}

		scpClient.setUsername(remoteUsername);
		scpClient.setPassword(remotePassword);
		
		try {
			scpClient.put(
				new ByteArrayInputStream(
					theContent.toString().getBytes()),
				remoteFile);
		} catch (Exception e ) {
			e.printStackTrace();
		}

	}


	private String open( String fullpathname ) {

    File f;
    FileReader in = null;
    String filename = fullpathname;
    String DataString = null;
    String passwd = null;

    try {
      f = new File( filename );
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
        e.printStackTrace();
      }
    }

    return DataString;
  }

	public static void main( String args[] ) {

		long days = 0;

		for( int i = 0; i < args.length; i++) {
			days = Integer.parseInt( args[0] );
		}
		
		Asn asn  = new Asn();
		asn.go( days );
	}

}
