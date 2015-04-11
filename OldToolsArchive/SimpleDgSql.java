import java.sql.*;
import java.io.*;

public class SimpleDgSql {

    public static void main(String[] args) {

		SimpleDgSql ctf = new SimpleDgSql();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection c = DriverManager.getConnection(
				"jdbc:oracle:thin:@boronprda:1521:gold", 
				"nvgold_reporter", 
				"nvgold_reporter");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery( "select * from system_component_log where system_component_id='15014'" );
			int i = 1;
			while( rs.next() ) {
				System.out.println( "Row " + i + " = " + rs.getString(1) );
				i++;
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
