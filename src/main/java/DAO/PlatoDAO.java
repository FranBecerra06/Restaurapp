package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ConexionBd.ConexionBD;
import DTO.PlatoDTO;

public class PlatoDAO {

    // -- INSERTAR PLATO --
    public PlatoDTO crearPlato(PlatoDTO p) throws SQLException {
        String sql = "INSERT INTO plato (nombre, descripcion, precio, disponible) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, p.getNombre());
            pst.setString(2, p.getDescripcion());
            pst.setDouble(3, p.getPrecio());
            pst.setBoolean(4, p.isDisponible());

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdPlato(rs.getInt(1));
                }
            }
        }
        return p;
    }

    // -- MODIFICAR PLATO --
    public boolean modificarPlato(PlatoDTO p) throws SQLException {
        String sql = "UPDATE plato SET nombre = ?, descripcion = ?, precio = ?, disponible = ? WHERE id_plato = ?";
        
        try (Connection conn = ConexionBD.getConnection(); 
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, p.getNombre());
            pst.setString(2, p.getDescripcion());
            pst.setDouble(3, p.getPrecio());
            pst.setBoolean(4, p.isDisponible());
            pst.setInt(5, p.getIdPlato());

            int filas = pst.executeUpdate();
            return filas > 0;
        }
    }

    // -- ELIMINAR PLATO --
    public boolean eliminarPlato(int idPlato) throws SQLException {
        String sql = "DELETE FROM plato WHERE id_plato = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idPlato);
            int filas = pst.executeUpdate();
            return filas > 0;
        }
    }

    // -- LISTAR PLATOS --
    public List<PlatoDTO> listarPlatos() throws SQLException {
        List<PlatoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM plato";

        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                PlatoDTO p = new PlatoDTO(rs.getInt("id_plato"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setDisponible(rs.getBoolean("disponible"));

                lista.add(p);
            }
        }
        return lista;
    }
}