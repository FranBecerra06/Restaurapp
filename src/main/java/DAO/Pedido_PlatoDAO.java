package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ConexionBd.ConexionBD;
import DTO.Pedido_PlatoDTO;

public class Pedido_PlatoDAO {

    // -- INSERTAR PEDIDO-PLATO --
    public Pedido_PlatoDTO crearPedidoPlato(Pedido_PlatoDTO pp) throws SQLException {
        String sql = "INSERT INTO pedido_plato (id_pedido, id_plato, cantidad) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, pp.getId_pedido());
            pst.setInt(2, pp.getId_plato());
            pst.setInt(3, pp.getCantidad());

            pst.executeUpdate();

            // Si la tabla tiene un ID autoincremental, lo podemos recuperar aquí
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    // No tenemos campo de ID en tu DTO, pero si lo agregas, lo pondrías aquí
                }
            }
        }
        return pp;
    }

    // -- LISTAR TODOS LOS PEDIDOS-PLATO --
    public List<Pedido_PlatoDTO> listarPedidosPlatos() throws SQLException {
        List<Pedido_PlatoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido_plato";

        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Pedido_PlatoDTO pp = new Pedido_PlatoDTO(
                        rs.getInt("id_pedido"),
                        rs.getInt("id_plato"),
                        rs.getInt("cantidad")
                );
                lista.add(pp);
            }
        }
        return lista;
    }

    // -- OBTENER POR ID PEDIDO Y PLATO --
    public Pedido_PlatoDTO obtenerPorPedidoPlato(int idPedido, int idPlato) throws SQLException {
        String sql = "SELECT * FROM pedido_plato WHERE id_pedido = ? AND id_plato = ?";
        Pedido_PlatoDTO pp = null;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idPedido);
            pst.setInt(2, idPlato);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    pp = new Pedido_PlatoDTO(
                            rs.getInt("id_pedido"),
                            rs.getInt("id_plato"),
                            rs.getInt("cantidad")
                    );
                }
            }
        }
        return pp;
    }

    // -- ACTUALIZAR CANTIDAD --
    public boolean actualizarCantidad(Pedido_PlatoDTO pp) throws SQLException {
        String sql = "UPDATE pedido_plato SET cantidad = ? WHERE id_pedido = ? AND id_plato = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, pp.getCantidad());
            pst.setInt(2, pp.getId_pedido());
            pst.setInt(3, pp.getId_plato());

            return pst.executeUpdate() > 0;
        }
    }

    // -- ELIMINAR PEDIDO-PLATO --
    public boolean eliminarPedidoPlato(int idPedido, int idPlato) throws SQLException {
        String sql = "DELETE FROM pedido_plato WHERE id_pedido = ? AND id_plato = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idPedido);
            pst.setInt(2, idPlato);

            return pst.executeUpdate() > 0;
        }
    }
}