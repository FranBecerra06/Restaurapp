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

			try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {

					u.setNombre(rs.getString(1));
					u.setApellidos(rs.getString(2));
					u.setEmail(rs.getString(3));
					u.setContrasena(rs.getString(4));
					u.setTelefono(rs.getString(5));
					u.setNombre_usuario(rs.getString(6));
				}

			}

		}
		return u;

	}

// -- MODIFICAR USUARIO --

	public boolean modificarUsuario(UsuarioDTO u) throws SQLException {

		String sql = "UPDATE cliente SET nombre = ?, apellidos = ?, email = ?, contrasena = ?, telefono = ?, nombre_usuario = ? WHERE nombre_usuario = ?";
		try (Connection conn = ConexionBD.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, u.getNombre());
			pst.setString(2, u.getApellidos());
			pst.setString(3, u.getEmail());
			pst.setString(4, u.getContrasena());
			pst.setString(5, u.getTelefono());
			pst.setString(6, u.getNombre_usuario());
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
	
	
	
	
}
