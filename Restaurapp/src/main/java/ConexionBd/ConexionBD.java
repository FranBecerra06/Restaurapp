package ConexionBd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

	private static final String URL = "jdbc:mysql://localhost:3306/restaurapp?useSSL=false&serverTimezone=UTC";
	private static final String USER = "root";
	private static final String PASS = "";

	public static Connection getConnection() throws SQLException {
        try {
            // ESTA LÍNEA ES OBLIGATORIA PARA ARREGLAR ESE ERROR
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return DriverManager.getConnection(URL, USER, PASS);
    }

	public static void main(String[] args) {

		try (Connection conn = ConexionBD.getConnection()) {
			System.out.println("Conexión establecida");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}