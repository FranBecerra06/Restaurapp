package DAO;

import java.sql.Statement;

public class AlergenoDAO {
	
	//CRUD DE ALÉRGENOS
	
	//-- INSERTAR ALÉRGENO --
	
	public Alergeno insertarAlergeno(Alergeno a) throws SQLException{
		
		String sql = "INSERT INTO alergeno (id_alergeno, nombre) VALUES(?,?)";
		PreparedStatement pst = conn = PreparedStatement(sql, Statement.RETURN_GENERATED_KEYS)){
			
			pst.setInt(1, a.getid_alergeno());
			pst.setString(2, a.getnombre());
			pst.executeUpdate();
			
			try(Result.Set rs = pst.getGeneratedKeys(){
				if(rs.next()) {
					
					a.setId(rs.getInt(1));
				}
				
				
				
			}
			
			
		}
		return a;
		
		
	}
	
	
	
	
	 
	//-- MODIFICAR ALÉRGENO --
	
	
	
	
	
	
	
	//-- ELIMINAR ALÉRGENO --
	
	
	
	
	
	
	
	//-- LISTAR ALÉRGENOS --
	
	
	
	
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
