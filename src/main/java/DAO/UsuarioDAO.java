package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ConexionBd.ConexionBD;
import DTO.UsuarioDTO;

public class UsuarioDAO {
	
	//CRUD COMPLETO
	
	// -- INSERTAR USUARIO -- 

	public UsuarioDTO crearCliente(UsuarioDTO u) throws SQLException {

		String sql = "INSERT INTO usuario (nombre, apellidos, email, contrasena, telefono, nombre_usuario) VALUES(?,?,?,?,?,?)";

		try (Connection conn = ConexionBD.getConnection();
			 PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			pst.setString(1, u.getNombre());
			pst.setString(2, u.getApellidos());
			pst.setString(3, u.getEmail());
			pst.setString(4, u.getContrasena());
			pst.setString(5, u.getTelefono());
			pst.setString(6, u.getNombre_usuario());

			pst.executeUpdate();

			/*try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {
					u.setIdUsuario(rs.getInt(1));  // SOLO el ID autogenerado
				}
			}*/
		}

		return u;
	}

// -- MODIFICAR USUARIO --

	public boolean modificarUsuario(UsuarioDTO u) throws SQLException {
		// CORRECCIÓN 1: Cambiar "cliente" por "usuario"
		// CORRECCIÓN 2: Añadir el parámetro para el WHERE
		String sql = "UPDATE usuario SET nombre = ?, apellidos = ?, email = ?, contrasena = ?, telefono = ?, nombre_usuario = ? WHERE nombre_usuario = ?";

		try (Connection conn = ConexionBD.getConnection();
			 PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, u.getNombre());
			pst.setString(2, u.getApellidos());
			pst.setString(3, u.getEmail());
			pst.setString(4, u.getContrasena());
			pst.setString(5, u.getTelefono());
			pst.setString(6, u.getNombre_usuario());  // Nuevo nombre_usuario (si cambia)
			pst.setString(7, u.getNombre_usuario());  // Nombre_usuario original para WHERE

			int filas = pst.executeUpdate();
			return filas > 0;
		}
	}

// -- ELIMINAR CLIENTE --

	public boolean eliminarUsuario(String nombreUsuario) throws SQLException {

		String sql = "DELETE FROM usuario WHERE nombre_usuario = ?";

		try (Connection conn = ConexionBD.getConnection(); 
				PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, nombreUsuario);
			int filas = pst.executeUpdate();
			return filas > 0;

		}
	}

// -- LISTAR NOMBRE USUARIOS --

	public List<String> listarUsuarioClientes() throws SQLException {
		String usuario;

		List<String> listaNombres = new ArrayList<>();
		String sql = "SELECT nombre_usuario FROM usuario";
		try (Connection conn = ConexionBD.getConnection();

				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {

				usuario= rs.getString("nombre_usuario");
				listaNombres.add(usuario);

			}

		}
		
		return listaNombres;

	}
	public List<String> listarEmailClientes() throws SQLException {
		String email;

		List<String> emails = new ArrayList<>();
		String sql = "SELECT email FROM usuario";
		try (Connection conn = ConexionBD.getConnection();

			 Statement st = conn.createStatement();
			 ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {

				email= rs.getString("email");
				emails.add(email);

			}

		}

		return emails;

	}
	public UsuarioDTO obtenerUsuarioPorNombreDeUsuario(String nombreUsuario) throws SQLException {
		UsuarioDTO usuario = null;
		String sql = "SELECT * FROM usuario WHERE nombre_usuario = ?";

		try (Connection conn = ConexionBD.getConnection();
			 PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, nombreUsuario);

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					usuario = new UsuarioDTO();
					usuario.setIdUsuario(rs.getInt("id_usuario"));
					usuario.setNombre(rs.getString("nombre"));
					usuario.setApellidos(rs.getString("apellidos"));
					usuario.setEmail(rs.getString("email"));
					usuario.setContrasena(rs.getString("contrasena"));
					usuario.setTelefono(rs.getString("telefono"));
					usuario.setNombre_usuario(rs.getString("nombre_usuario"));
				}
			}
		}

		return usuario;
	}
}
