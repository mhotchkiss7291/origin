import java.sql.*;
import java.util.*;

public class DBConnection {

	private Connection mySQLcon = null;

	public DBConnection( String GroupID ) {

		try {

			try {
				Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}

			//create the "URL"
			String url = "jdbc:mysql://127.0.0.1:3306/" + GroupID ;

			//Use the DriverManager to get a Connection
			mySQLcon = DriverManager.getConnection (url);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return mySQLcon;
	}

	public Vector showTables() {

		Vector tables = new Vector();
		ResultSet rs = null;

		try {

			Statement stmt = mySQLcon.createStatement();
			rs = stmt.executeQuery("SHOW TABLES");

			int i = 1;

      while( rs.next() ) {
        tables.addElement( rs.getString( 1 ) ) ;
				i++;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

		return tables;
	}

	public void closeConnection() {

		if( mySQLcon != null ) {
			try {
				mySQLcon.close();
			} catch( Exception e ) {
				e.printStackTrace();
			}
		}			
	}
}
