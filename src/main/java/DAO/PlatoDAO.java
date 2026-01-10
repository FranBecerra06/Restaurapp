package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ConexionBd.ConexionBD;
import DTO.PlatoDTO;

public class PlatoDAO {

    // -- INSERTAR PLATO CON ALÉRGENOS --
    public PlatoDTO crearPlato(PlatoDTO p) throws SQLException {
        String sqlPlato = "INSERT INTO plato (id_categoria, nombre, descripcion, precio) VALUES (?, ?, ?, ?)";
        String sqlAlergeno = "INSERT INTO plato_alergeno (id_plato, id_alergeno) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement pst = null;
        PreparedStatement pstAlergeno = null;

        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar datos del plato
            pst = conn.prepareStatement(sqlPlato, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, p.getIdCategoria());
            pst.setString(2, p.getNombre());
            pst.setString(3, p.getDescripcion());
            pst.setDouble(4, p.getPrecio());
            pst.executeUpdate();

            // 2. Recuperar ID generado
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdPlato(rs.getInt(1));
                }
            }

            // 3. Insertar Alérgenos (Lista de Booleanos)
            // Orden asumido: 0=Gluten(1), 1=Lactosa(2), 2=Huevo(3), 3=FrutosSecos(4), 4=Pescado(5), 5=Marisco(6)
            List<Boolean> lista = p.getListaAlergenos();
            
            if (lista != null && !lista.isEmpty()) {
                pstAlergeno = conn.prepareStatement(sqlAlergeno);
                boolean hayAlergenos = false;

                // Mapeo manual según posición
                if (lista.size() > 0 && lista.get(0)) { pstAlergeno.setInt(1, p.getIdPlato()); pstAlergeno.setInt(2, 1); pstAlergeno.addBatch(); hayAlergenos = true; } // Gluten
                if (lista.size() > 1 && lista.get(1)) { pstAlergeno.setInt(1, p.getIdPlato()); pstAlergeno.setInt(2, 2); pstAlergeno.addBatch(); hayAlergenos = true; } // Lactosa
                if (lista.size() > 2 && lista.get(2)) { pstAlergeno.setInt(1, p.getIdPlato()); pstAlergeno.setInt(2, 3); pstAlergeno.addBatch(); hayAlergenos = true; } // Huevo
                if (lista.size() > 3 && lista.get(3)) { pstAlergeno.setInt(1, p.getIdPlato()); pstAlergeno.setInt(2, 4); pstAlergeno.addBatch(); hayAlergenos = true; } // Frutos Secos
                if (lista.size() > 4 && lista.get(4)) { pstAlergeno.setInt(1, p.getIdPlato()); pstAlergeno.setInt(2, 5); pstAlergeno.addBatch(); hayAlergenos = true; } // Pescado
                if (lista.size() > 5 && lista.get(5)) { pstAlergeno.setInt(1, p.getIdPlato()); pstAlergeno.setInt(2, 6); pstAlergeno.addBatch(); hayAlergenos = true; } // Marisco

                if (hayAlergenos) {
                    pstAlergeno.executeBatch();
                }
            }

            conn.commit(); // Confirmar cambios

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Deshacer cambios si falla
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (pst != null) pst.close();
            if (pstAlergeno != null) pstAlergeno.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
        return p;
    }

    // -- MODIFICAR PLATO --
    public boolean modificarPlato(PlatoDTO p) throws SQLException {
        String sql = "UPDATE plato SET id_categoria = ?, nombre = ?, descripcion = ?, precio = ? WHERE id_plato = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, p.getIdCategoria());
            pst.setString(2, p.getNombre());
            pst.setString(3, p.getDescripcion());
            pst.setDouble(4, p.getPrecio());
            pst.setInt(5, p.getIdPlato());

            int filas = pst.executeUpdate();
            
            // NOTA: Si quisieras actualizar alérgenos al editar, habría que borrar los viejos e insertar los nuevos aquí.
            // Por ahora dejamos solo la edición de datos básicos.
            
            return filas > 0;
        }
    }

    // -- ELIMINAR PLATO --
    public boolean eliminarPlato(int idPlato) throws SQLException {
        // Primero borramos dependencias en tabla intermedia (si no tienes ON DELETE CASCADE en BD)
        String sqlAlergenos = "DELETE FROM plato_alergeno WHERE id_plato = ?";
        String sqlPlato = "DELETE FROM plato WHERE id_plato = ?";

        Connection conn = null;
        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pst1 = conn.prepareStatement(sqlAlergenos)) {
                pst1.setInt(1, idPlato);
                pst1.executeUpdate();
            }

            int filas;
            try (PreparedStatement pst2 = conn.prepareStatement(sqlPlato)) {
                pst2.setInt(1, idPlato);
                filas = pst2.executeUpdate();
            }

            conn.commit();
            return filas > 0;

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    // -- LISTAR TODOS LOS PLATOS --
    public List<PlatoDTO> listarPlatos() throws SQLException {
        List<PlatoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM plato ";

        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                PlatoDTO p = new PlatoDTO(
                        rs.getInt("id_plato"),
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio")
                );
                lista.add(p);
            }
        }
        return lista;
    }

    // -- LISTAR PLATOS POR CATEGORÍA --
    public List<PlatoDTO> obtenerPlatosPorCategoria(int categoriaId) throws SQLException {
        List<PlatoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM plato WHERE id_categoria = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, categoriaId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    PlatoDTO p = new PlatoDTO(
                            rs.getInt("id_plato"),
                            rs.getInt("id_categoria"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio")
                    );
                    lista.add(p);
                }
            }
        }
        return lista;
    }

    public List<PlatoDTO> obtenerPlatosPorCategoriaBotones(int idCategoria) throws SQLException {
        return obtenerPlatosPorCategoria(idCategoria); 
    }

    // -- OBTENER ID POR NOMBRE --
    public int obtenerIdPlatoPorNombre(String nombre) throws SQLException {
        int id_plato = 0;
        String sql = "SELECT id_plato FROM plato WHERE nombre = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, nombre);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    id_plato = rs.getInt("id_plato");
                }
            }
        }
        return id_plato;
    }

    // -- OBTENER PLATO POR NOMBRE --
    public PlatoDTO obtenerPlatoPorNombre(String nombre) throws SQLException {
        PlatoDTO plato = null;
        String sql = "SELECT * FROM plato WHERE nombre = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, nombre);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    plato = new PlatoDTO(
                            rs.getInt("id_plato"),
                            rs.getInt("id_categoria"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio")
                    );
                }
            }
        }
        return plato;
    }

    // -- OBTENER PLATO POR ID --
    public PlatoDTO obtenerPlatoPorId(int idPlato) throws SQLException {
        PlatoDTO plato = null;
        String sql = "SELECT * FROM plato WHERE id_plato = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idPlato);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    plato = new PlatoDTO(
                            rs.getInt("id_plato"),
                            rs.getInt("id_categoria"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio")
                    );
                }
            }
        }
        return plato;
    }
}