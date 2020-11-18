package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
	
	private final static String jdbcURL =  "jdbc:mysql://localhost/giunico?useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
	//private final static String jdbcURL =  "jdbc:mysql://SERVER06/giunico?allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private final static String db_user = "root";
	private final static String db_pass = "rootroot";	// root per Giunico
	
	public DBConnector() {
		
	}

	public static Connection getConnection() throws SQLException {
		
		Connection connection = null;
		try  {
			// Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, db_user, db_pass);
        }
		catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
		catch (Exception e) {
            e.printStackTrace();
        }
		
		/*Connection connection = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
		
		connection = DriverManager.getConnection(jdbcURL, db_user, db_pass);*/
		
		return connection;
	}
}
