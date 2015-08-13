import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestOrderService {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String DB_URL = "jdbc:postgresql://biwsdblgmt01:5434/dgcs";

	// Database credentials
	static final String USER = "sif_rw";
	static final String PASS = "sif_rw";

	static final int ID_START_COUNT = 15000;
	static final int PARAMETER_SIZE_MAX = 15019;
	static final int CONSTRAINT_SIZE_MAX = 15071;

	public static void main(String[] args) {

		TestOrderService tos = new TestOrderService();
		tos.runTests();

	}

	void runTests() {

		Connection conn = null;

		try {
			Class.forName("org.postgresql.Driver");

			// System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			doQueries(conn);

			conn.close();

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	void doQueries(Connection conn) {

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String sql = null;

		iterateDependencies(stmt, sql);

		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void iterateDependencies(Statement stmt, String sql) {

		int parameter_id = ID_START_COUNT;
		int constraint_id = ID_START_COUNT;

		while (parameter_id < PARAMETER_SIZE_MAX) {

			constraint_id = ID_START_COUNT;

			while (constraint_id < CONSTRAINT_SIZE_MAX) {

				sql = "SELECT dependent_on_parameter,dependent_on_constraint FROM order_parameter_dependency WHERE parameter_id="
						+ parameter_id + "AND constraint_id=" + constraint_id;
				ResultSet rs = null;
				try {
					rs = stmt.executeQuery(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				int dop = 0;
				int doc = 0;

				try {
					if (!rs.next()) {
						// System.out.println("No records found");
					} else {
						do {
							dop = rs.getInt("dependent_on_parameter");
							doc = rs.getInt("dependent_on_constraint");
							System.out.println("pid = " + parameter_id
									+ ": cid = " + constraint_id + ": dop = "
									+ dop + ": doc = " + doc);
						} while (rs.next());
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				constraint_id++;
			}
			parameter_id++;
		}
	}
}