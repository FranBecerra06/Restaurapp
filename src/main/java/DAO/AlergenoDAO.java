package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.xdevapi.Result;

import ConexionBd.ConexionBD;
import DTO.AlergenoDTO;

public class AlergenoDAO {

	// CRUD DE ALÉRGENOS

	// -- INSERTAR ALÉRGENO --

	public AlergenoDTO crearAlergeno(AlergenoDTO a) throws SQLException {

		String sql = "INSERT INTO alergeno (nombre) VALUES(?)";

		try (Connection conn = ConexionBD.getConnection();
				PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			pst.setString(1, a.getNombre());
			pst.executeUpdate();

			try (ResultSet rs = pst.getGeneratedKeys()) {
				if (rs.next()) {

					a.setId(rs.getInt(1));
				}

			}

		}
		return a;

	}

// -- MODIFICAR ALÉRGENO --

	public boolean modificarAlergeno(AlergenoDTO a) throws SQLException {

		String sql = "UPDATE alergeno SET nombre = ? WHERE nombre = ?";
		try (Connection conn = ConexionBD.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setString(1, a.getNombre());
			int filas = pst.executeUpdate();
			return filas > 0;

		}
	}

// -- ELIMINAR ALÉRGENO --

	public boolean eliminarAlergeno(int id) throws SQLException {

		String sql = "DELETE FROM alergeno WHERE nombre = ?";

		try (Connection conn = ConexionBD.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

			pst.setInt(1, id);
			int filas = pst.executeUpdate();
			return filas > 0;

		}
	}

// -- LISTAR ALÉRGENOS --

	public List<AlergenoDTO> listarAlergenos() throws SQLException {

		List<AlergenoDTO> lista = new ArrayList<>();
		String sql = "SELECT nombre FROM alergeno";
		try (Connection conn = ConexionBD.getConnection();

				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {

				lista.add(new AlergenoDTO(rs.getString("nombre")));

			}

		}
		return lista;

	}

}