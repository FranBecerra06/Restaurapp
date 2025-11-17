package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ConexionBd.ConexionBD;
import DTO.ClienteDTO;
import DTO.UsuarioDTO;
import Modelos.Usuario;

public class ClienteDAO {
	
		//CRUD ClienteDAO
		
		// -- INSERTAR CLIENTE --

		public ClienteDTO crearCliente(ClienteDTO c) throws SQLException {

			String sql = "INSERT INTO cliente (nombre, apellidos, email, fecha_nacimiento) VALUES(?,?,?,?)";

			try (Connection conn = ConexionBD.getConnection();
					PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

				pst.setString(1, c.getNombre());
				pst.setString(2, c.getApellidos());
				pst.setString(3, c.getEmail());
				pst.setDate(4, java.sql.Date.valueOf(c.getFechaNacimiento()));
				
				pst.executeUpdate();

				try (ResultSet rs = pst.getGeneratedKeys()) {
					if (rs.next()) {

						c.setIdCliente(rs.getInt(1));
					}

				}

			}
			return c;

		}

	// -- MODIFICAR CLIENTE --

		public boolean modificarCliente(ClienteDTO c) throws SQLException {

			String sql = "UPDATE cliente SET nombre = ?, apellidos = ?, email = ?, fecha_nacimiento = ? WHERE id_usuario = ?";
			try (Connection conn = ConexionBD.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

				pst.setString(1, c.getNombre());
				pst.setString(2, c.getApellidos());
				pst.setString(3, c.getEmail());
				pst.setDate(4, java.sql.Date.valueOf(c.getFechaNacimiento()));
				int filas = pst.executeUpdate();
				return filas > 0;

			}
		}

	// -- ELIMINAR CLIENTE --

		public boolean eliminarCliente(String nombreUsuario) throws SQLException {

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

			List<ClienteDTO> lista = new ArrayList<>();
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
		
		// -- LISTAR CLIENTES --
		
		public List<ClienteDTO> listarClientes() throws SQLException {
		    List<ClienteDTO> lista = new ArrayList<>();

		    String sql = "SELECT id_usuario, nombre, apellidos, email, fecha_nacimiento FROM cliente";

		    try (Connection conn = ConexionBD.getConnection();
		         Statement st = conn.createStatement();
		         ResultSet rs = st.executeQuery(sql)) {

		        while (rs.next()) {
		         

		            ClienteDTO cliente = new ClienteDTO(
		                rs.getString("nombre"),
		                rs.getString("apellidos"),
		                rs.getString("email"),
		                rs.getDate("fecha_nacimiento").toLocalDate()
		            );

		            lista.add(cliente);
		        }
		    }

		    return lista;
		}
		

	}