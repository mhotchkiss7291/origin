import java.util.*;
import java.io.*;
import com.sun.authtech.scp.*;

public class PushFiles {

	public boolean push( ArrayList radiuslist ) {

		// SCP the file contents to the servers
		StringWriter theContent = new StringWriter();
    PrintWriter theContentOut = new PrintWriter(theContent);
    int i = 0;
		while( i < radiuslist.size() ) {
			theContentOut.print( (String)radiuslist.get( i ) );
			i++;
		}
    Properties config = new Properties();

		//push to ironwood
    try {
      config.load(
        new FileInputStream(
          "/opt/netscape/server4/docs-BCA/servlet/etc/ironwood.config"));
    } catch (Exception e ) {
      e.printStackTrace();
			return false;
    }
    String remoteUsername = "radiusmgr";
    String remotePassword = "swordfish";
    String remoteFile = "/usr/safeword/SWRADSRV/clients";

    SCPClient scpClient = null;

    try {
      scpClient = new SCPClient(config);
    } catch (Exception e) {
      e.printStackTrace();
			return false;
    }

    scpClient.setUsername(remoteUsername);
    scpClient.setPassword(remotePassword);

		Notify nf = new Notify("Failed to push to ironwood.central");

		try {
      scpClient.put(
        new ByteArrayInputStream(
          theContent.toString().getBytes()),
        remoteFile);
    } catch (Exception e ) {
      e.printStackTrace();
			try {
				nf.send();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			return false;
    }

		// push to pkira.central
    try {
      config.load(
        new FileInputStream(
          "/opt/netscape/server4/docs-BCA/servlet/etc/pkira.central.config"));
    } catch (Exception e ) {
      e.printStackTrace();
			return false;
    }
    remoteFile = "/safeword/safedir/safeword/sync/SWRADSRV/clients";
    scpClient = null;
    try {
      scpClient = new SCPClient(config);
    } catch (Exception e) {
      e.printStackTrace();
			return false;
    }
    scpClient.setUsername(remoteUsername);
    scpClient.setPassword(remotePassword);
		nf = new Notify("Failed to push to pkira.central");
		try {
      scpClient.put(
        new ByteArrayInputStream(
          theContent.toString().getBytes()),
        remoteFile);
    } catch (Exception e ) {
      e.printStackTrace();
			try {
				nf.send();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			return false;
    }

		// push to pkira.singapore
    try {
      config.load(
        new FileInputStream(
          "/opt/netscape/server4/docs-BCA/servlet/etc/pkira.singapore.config"));
    } catch (Exception e ) {
      e.printStackTrace();
			return false;
    }
    remoteFile = "/safeword/safedir/safeword/sync/SWRADSRV/clients";
    scpClient = null;
    try {
      scpClient = new SCPClient(config);
    } catch (Exception e) {
      e.printStackTrace();
			return false;
    }
    scpClient.setUsername(remoteUsername);
    scpClient.setPassword(remotePassword);
		nf = new Notify("Failed to push to pkira.singapore");
		try {
      scpClient.put(
        new ByteArrayInputStream(
          theContent.toString().getBytes()),
        remoteFile);
    } catch (Exception e ) {
      e.printStackTrace();
			try {
				nf.send();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			return false;
    }

		// push to pkira.holland
    try {
      config.load(
        new FileInputStream(
          "/opt/netscape/server4/docs-BCA/servlet/etc/pkira.holland.config"));
    } catch (Exception e ) {
      e.printStackTrace();
			return false;
    }
    remoteFile = "/safeword/safedir/safeword/sync/SWRADSRV/clients";
    scpClient = null;
    try {
      scpClient = new SCPClient(config);
    } catch (Exception e) {
      e.printStackTrace();
			return false;
    }
    scpClient.setUsername(remoteUsername);
    scpClient.setPassword(remotePassword);
		nf = new Notify("Failed to push to pkira.holland");
		try {
      scpClient.put(
        new ByteArrayInputStream(
          theContent.toString().getBytes()),
        remoteFile);
    } catch (Exception e ) {
      e.printStackTrace();
			try {
				nf.send();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			return false;
    }

		return true;
  }
}
