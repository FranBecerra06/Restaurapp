package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ConexionBd.ConexionBD;
import DTO.MesaDTO;
import DTO.PedidoDTO;
import Modelos.Mesa;

public class PedidoDAO {

    // -- INSERTAR PEDIDO --
    public PedidoDTO crearPedido(PedidoDTO p) throws SQLException {
        String sql = "INSERT INTO pedidos (id_mesa, observaciones) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Extraemos el ID del objeto Mesa
            if (p.getIdMesa() != null) {
                pst.setInt(1, p.getIdMesa().getIdMesa());
            } else {
                pst.setNull(1, java.sql.Types.INTEGER);
            }
            
            pst.setString(2, p.getObservaciones());

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdPedido(rs.getInt(1));
                }
            }
        }
        return p;
    }

    // -- MODIFICAR PEDIDO --
    public boolean modificarPedido(PedidoDTO p) throws SQLException {
        String sql = "UPDATE pedidos SET id_mesa = ?, observaciones = ? WHERE id_pedido = ?";
        
        try (Connection conn = ConexionBD.getConnection(); 
             PreparedStatement pst = conn.prepareStatement(sql)) {

            if (p.getIdMesa() != null) {
                pst.setInt(1, p.getIdMesa().getIdMesa());
            } else {
                pst.setNull(1, java.sql.Types.INTEGER);
            }
            
            pst.setString(2, p.getObservaciones());
            pst.setInt(3, p.getIdPedido());

            int filas = pst.executeUpdate();
            return filas > 0;
        }
    }

    // -- ELIMINAR PEDIDO --
    public boolean eliminarPedido(int idPedido) throws SQLException {
        String sql = "DELETE FROM pedidos WHERE id_pedido = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idPedido);
            int filas = pst.executeUpdate();
            return filas > 0;
        }
    }

    // -- LISTAR PEDIDOS --
    public List<PedidoDTO> listarPedidos() throws SQLException {
        List<PedidoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";

        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                PedidoDTO p = new PedidoDTO(rs.getInt("id_pedido"));
                p.setObservaciones(rs.getString("observaciones"));
                
                // Reconstruimos objeto Mesa solo con ID
                int idMesa = rs.getInt("id_mesa");
                if (idMesa > 0) {
                    MesaDTO m = new MesaDTO(idMesa); 
                    m.setIdMesa(idMesa);
                    p.setIdMesa(m);
                }

                lista.add(p);
            }
        }
        return lista;
    }
}