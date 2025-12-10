package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ConexionBd.ConexionBD;
import DTO.CategoriaDTO;

public class CategoriaDAO {

    // -- INSERTAR CATEGORÍA --
    public CategoriaDTO crearCategoria(CategoriaDTO c) throws SQLException {
        String sql = "INSERT INTO categoria (nombre) VALUES (?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, c.getNombre());
            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setIdCategoria(rs.getInt(1));
                }
            }
        }
        return c;
    }

    // -- MODIFICAR CATEGORÍA --
    public boolean modificarCategoria(CategoriaDTO c) throws SQLException {
        String sql = "UPDATE categoria SET nombre = ? WHERE id_categoria = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, c.getNombre());
            pst.setInt(2, c.getIdCategoria());

            int filas = pst.executeUpdate();
            return filas > 0;
        }
    }

    // -- ELIMINAR CATEGORÍA --
    public boolean eliminarCategoria(int idCategoria) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idCategoria);
            int filas = pst.executeUpdate();
            return filas > 0;
        }
    }

    // -- LISTAR CATEGORÍAS --
    public List<CategoriaDTO> listarCategorias() throws SQLException {
        List<CategoriaDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria";

        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                CategoriaDTO c = new CategoriaDTO(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")
                );
                lista.add(c);
            }
        }
        return lista;
    }

    // -- OBTENER CATEGORÍA POR NOMBRE --
    public CategoriaDTO obtenerCategoriaPorNombre(String nombre) throws SQLException {
        CategoriaDTO categoria = null;
        String sql = "SELECT * FROM categoria WHERE nombre = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, nombre);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    categoria = new CategoriaDTO(
                            rs.getInt("id_categoria"),
                            rs.getString("nombre")
                    );
                }
            }
        }
        return categoria;
    }
    
    
    public List<CategoriaDTO> obtenerCategorias() {
        List<CategoriaDTO> categorias = new ArrayList<>();

        String sql = "SELECT * FROM categoria";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categorias.add(new CategoriaDTO(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categorias;
    }
}