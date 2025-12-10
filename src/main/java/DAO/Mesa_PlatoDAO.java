package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ConexionBd.ConexionBD;
import DTO.Mesa_PlatoDTO;

public class Mesa_PlatoDAO {

    // -- INSERTAR MESA-PLATO --
    public Mesa_PlatoDTO crearMesaPlato(Mesa_PlatoDTO mp) throws SQLException {
        String sql = "INSERT INTO mesa_plato (id_mesa, id_plato, cantidad) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, mp.getId_mesa());
            pst.setInt(2, mp.getId_plato());
            pst.setInt(3, mp.getCantidad());

            pst.executeUpdate();

            // Si tu tabla tiene ID autoincremental (normalmente no en tablas intermedias)
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    // No se usa en tu DTO, pero aquí iría mp.setId(rs.getInt(1));
                }
            }
        }
        return mp;
    }

    // -- LISTAR TODOS --
    public List<Mesa_PlatoDTO> listarMesaPlatos() throws SQLException {
        List<Mesa_PlatoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM mesa_plato";

        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Mesa_PlatoDTO mp = new Mesa_PlatoDTO(
                        rs.getInt("id_mesa"),
                        rs.getInt("id_plato"),
                        rs.getInt("cantidad")
                );
                lista.add(mp);
            }
        }
        return lista;
    }

    // -- OBTENER REGISTRO DE UNA MESA Y PLATO --
    public Mesa_PlatoDTO obtenerPorMesaPlato(int idMesa, int idPlato) throws SQLException {
        String sql = "SELECT * FROM mesa_plato WHERE id_mesa = ? AND id_plato = ?";
        Mesa_PlatoDTO mp = null;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMesa);
            pst.setInt(2, idPlato);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    mp = new Mesa_PlatoDTO(
                            rs.getInt("id_mesa"),
                            rs.getInt("id_plato"),
                            rs.getInt("cantidad")
                    );
                }
            }
        }
        return mp;
    }

    // -- OBTENER TODOS LOS PLATOS DE UNA MESA --
    public List<Mesa_PlatoDTO> obtenerPlatoPorMesa(int idMesa) throws SQLException {
        List<Mesa_PlatoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM mesa_plato WHERE id_mesa = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMesa);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Mesa_PlatoDTO mp = new Mesa_PlatoDTO(
                            rs.getInt("id_mesa"),
                            rs.getInt("id_plato"),
                            rs.getInt("cantidad")
                    );
                    lista.add(mp);
                }
            }
        }
        return lista;
    }

    // -- ACTUALIZAR CANTIDAD --
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
    
    // -- ELIMINAR UN REGISTRO --
    public boolean eliminarMesaPlato(int idMesa, int idPlato) throws SQLException {
        String sql = "DELETE FROM mesa_plato WHERE id_mesa = ? AND id_plato = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMesa);
            pst.setInt(2, idPlato);

            return pst.executeUpdate() > 0;
        }
    }

    // -- ELIMINAR TODO LO DE UNA MESA --
    public boolean eliminarPorMesa(int idMesa) throws SQLException {
        String sql = "DELETE FROM mesa_plato WHERE id_mesa = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMesa);
            return pst.executeUpdate() > 0;
        }
    }

}