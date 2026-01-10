package ConexionBd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

	private static final String URL = "jdbc:mysql://localhost:3306/restaurapp";
	private static final String USER = "root";
	private static final String PASS = "Gonzalo-BD/06";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASS);
	}
}