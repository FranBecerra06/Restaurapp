package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ConexionBd.ConexionBD;
import DTO.MesaDTO;
import DTO.PedidoDTO;

public class PedidoDAO {

    // Crear pedido completo con camarero, mesa, total y observaciones
    public PedidoDTO crearPedido(PedidoDTO p) throws SQLException {
        String sql = "INSERT INTO pedido (id_camarero, id_mesa, fecha, total, observaciones) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, p.getIdCamarero());

            if (p.getIdMesa() != null) {
                pst.setInt(2, p.getIdMesa().getIdMesa());
            } else {
                pst.setNull(2, java.sql.Types.INTEGER);
            }

            pst.setTimestamp(3, Timestamp.valueOf(p.getFecha())); // Fecha
            pst.setDouble(4, p.getTotal()); // Total
            pst.setString(5, p.getObservaciones()); // Observaciones

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdPedido(rs.getInt(1));
                }
            }
        }
        return p;
    }

    // Listar todos los pedidos
    public List<PedidoDTO> listarPedidos() throws SQLException {
        List<PedidoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                PedidoDTO p = new PedidoDTO();
                p.setIdPedido(rs.getInt("id_pedido"));
                p.setIdCamarero(rs.getInt("id_camarero"));
                p.setTotal(rs.getDouble("total"));
                p.setObservaciones(rs.getString("observaciones"));
                p.setFecha(rs.getTimestamp("fecha").toLocalDateTime());

                int idMesa = rs.getInt("id_mesa");
                if (idMesa > 0) {
                    p.setIdMesa(new MesaDTO(idMesa));
                }

                lista.add(p);
            }
        }
        return lista;
    }

    // Obtener el último ID de pedido
    public int obtenerUltimoIdPedido() throws SQLException {
        String sql = "SELECT id_pedido FROM pedido ORDER BY id_pedido DESC LIMIT 1";

        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("id_pedido");
            } else {
                return -1;
            }
        }
    }
    
    
    public List<PedidoDTO> listarPedidosPorFecha(Date inicio, Date fin) throws SQLException {
        List<PedidoDTO> lista = new ArrayList<>();

        String sql = "SELECT * FROM pedido WHERE fecha BETWEEN ? AND ?";

        Connection conn = ConexionBD.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDate(1, inicio);
        ps.setDate(2, fin);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            PedidoDTO p = new PedidoDTO();
            p.setIdPedido(rs.getInt("id_pedido"));
            
            Timestamp ts = rs.getTimestamp("fecha");
            if (ts != null) {
                p.setFecha(ts.toLocalDateTime());
            } else {
                p.setFecha(null);
            }
            
            p.setTotal(rs.getDouble("total"));
            lista.add(p);
        }

        return lista;
    }

}