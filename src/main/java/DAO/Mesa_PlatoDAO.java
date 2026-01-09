package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ConexionBd.ConexionBD;
import DTO.Mesa_PlatoDTO;

public class Mesa_PlatoDAO {
	
    public Mesa_PlatoDTO crearMesaPlato(Mesa_PlatoDTO mp) throws SQLException {
        String sql = "INSERT INTO mesa_plato (id_mesa, id_plato, cantidad) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, mp.getId_mesa());
            pst.setInt(2, mp.getId_plato());
            pst.setInt(3, mp.getCantidad());
            pst.executeUpdate();
        }

        return mp;
    }
    
    
    public List<Mesa_PlatoDTO> obtenerPlatoPorMesa(int idMesa) throws SQLException {
        List<Mesa_PlatoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM mesa_plato WHERE id_mesa = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMesa);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Mesa_PlatoDTO(
                            rs.getInt("id_mesa"),
                            rs.getInt("id_plato"),
                            rs.getInt("cantidad")
                    ));
                }
            }
        }
        return lista;
    }
    
    
    public int obtenerCantidad(int idMesa, int idPlato) throws SQLException {
        String sql = "SELECT cantidad FROM mesa_plato WHERE id_mesa = ? AND id_plato = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMesa);
            pst.setInt(2, idPlato);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("cantidad");
            }
        }
        return 0; // No existe ese plato en la mesa
    }
    
    
    public boolean actualizarCantidad(int idMesa, int idPlato, int nuevaCantidad) throws SQLException {
        String sql = "UPDATE mesa_plato SET cantidad = ? WHERE id_mesa = ? AND id_plato = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, nuevaCantidad);
            pst.setInt(2, idMesa);
            pst.setInt(3, idPlato);

            return pst.executeUpdate() > 0;
        }
    }
    
    
    public boolean actualizarMesaPlato(Mesa_PlatoDTO mp) { 
    	String sql = "UPDATE mesa_plato SET cantidad = ? WHERE id_mesa = ? AND id_plato = ?"; 
    	try (Connection conn = ConexionBD.getConnection(); 
    			PreparedStatement pst = conn.prepareStatement(sql)) { 
    		pst.setInt(1, mp.getCantidad()); 
    		pst.setInt(2, mp.getId_mesa()); 
    		pst.setInt(3, mp.getId_plato()); 
    		return pst.executeUpdate() > 0; 
    	} catch (SQLException e) { 
    		e.printStackTrace(); 
    		return false; 
    	} 
    }
    
    
    public boolean eliminarMesaPlato(int idMesa, int idPlato) throws SQLException {
        String sql = "DELETE FROM mesa_plato WHERE id_mesa = ? AND id_plato = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMesa);
            pst.setInt(2, idPlato);

            return pst.executeUpdate() > 0;
        }
    }
    
    
    public boolean eliminarPorMesa(int idMesa) throws SQLException {
        String sql = "DELETE FROM mesa_plato WHERE id_mesa = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMesa);
            return pst.executeUpdate() > 0;
        }
    }
}