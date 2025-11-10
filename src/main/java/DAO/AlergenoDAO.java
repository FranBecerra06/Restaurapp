package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.xdevapi.Result;

import ConexionBd.ConexionBD;
import DTO.AlergenoDTO;

public class AlergenoDAO {

	// CRUD DE ALÉRGENOS

	// -- INSERTAR ALÉRGENO --

	public AlergenoDTO insertarAlergeno(AlergenoDTO a) throws SQLException{
		
		String sql = "INSERT INTO alergeno (id_alergeno, nombre) VALUES(?,?)";
		
		try(Connection conn = ConexionBD.getConnection();
		PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

			pst.setInt(1, a.getid_alergeno());
			pst.setString(2, a.getnombre());
			pst.executeUpdate();
			
			try(ResultSet rs = pst.getGeneratedKeys(){
				if(rs.next()) {
					
					a.setId(rs.getInt(1));
				}
				
				
				
			}
			
			
		}
		return a;
		
		
	}

// -- MODIFICAR ALÉRGENO --

	public AlergenoDTO modificarAlergeno (AlergenoDTO a) throws SQLException{
		
		String sql = "UPDATE alergeno SET nombre = ? WHERE id_alergeno = ?";
		try (Connection conn = Conexion
		
		
	}

// -- ELIMINAR ALÉRGENO --

// -- LISTAR ALÉRGENOS --

}