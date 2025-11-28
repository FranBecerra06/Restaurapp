package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ConexionBd.ConexionBD;
import DTO.ClienteDTO;
import DTO.MesaDTO;
import DTO.ReservaDTO;

public class ReservaDAO {

    // -- INSERTAR RESERVA --
    public ReservaDTO crearReserva(ReservaDTO r) throws SQLException {
        String sql = "INSERT INTO reservas (id_cliente, id_mesa, fecha_hora, numero_comensales, estado) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // FK Cliente
            if (r.getIdCliente() != null) pst.setInt(1, r.getIdCliente().getIdCliente());
            else pst.setNull(1, java.sql.Types.INTEGER);

            // FK Mesa
            if (r.getIdMesa() != null) pst.setInt(2, r.getIdMesa().getIdMesa());
            else pst.setNull(2, java.sql.Types.INTEGER);

            // Fecha (LocalDateTime -> Timestamp)
            if (r.getFechaHora() != null) pst.setTimestamp(3, Timestamp.valueOf(r.getFechaHora()));
            else pst.setNull(3, java.sql.Types.TIMESTAMP);

            pst.setInt(4, r.getNumeroComensales());
            pst.setString(5, r.getEstado());

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    r.setIdReserva(rs.getInt(1));
                }
            }
        }
        return r;
    }

    // -- MODIFICAR RESERVA --
    public boolean modificarReserva(ReservaDTO r) throws SQLException {
        String sql = "UPDATE reservas SET id_cliente = ?, id_mesa = ?, fecha_hora = ?, numero_comensales = ?, estado = ? WHERE id_reserva = ?";
        
        try (Connection conn = ConexionBD.getConnection(); 
             PreparedStatement pst = conn.prepareStatement(sql)) {

            if (r.getIdCliente() != null) pst.setInt(1, r.getIdCliente().getIdCliente());
            else pst.setNull(1, java.sql.Types.INTEGER);

            if (r.getIdMesa() != null) pst.setInt(2, r.getIdMesa().getIdMesa());
            else pst.setNull(2, java.sql.Types.INTEGER);

            if (r.getFechaHora() != null) pst.setTimestamp(3, Timestamp.valueOf(r.getFechaHora()));
            else pst.setNull(3, java.sql.Types.TIMESTAMP);

            pst.setInt(4, r.getNumeroComensales());
            pst.setString(5, r.getEstado());
            pst.setInt(6, r.getIdReserva());

            int filas = pst.executeUpdate();
            return filas > 0;
        }
    }

    // -- ELIMINAR RESERVA --
    public boolean eliminarReserva(int idReserva) throws SQLException {
        String sql = "DELETE FROM reservas WHERE id_reserva = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idReserva);
            int filas = pst.executeUpdate();
            return filas > 0;
        }
    }

    // -- LISTAR RESERVAS --
    public List<ReservaDTO> listarReservas() throws SQLException {
        List<ReservaDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas";

        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                ReservaDTO r = new ReservaDTO(rs.getInt("id_reserva"));
                
                // Mapear Objetos FK
                int idCliente = rs.getInt("id_cliente");
                if (idCliente > 0) {
                    ClienteDTO c = new ClienteDTO(idCliente);
                    c.setIdCliente(idCliente);
                    r.setIdCliente(c);
                }

                int idMesa = rs.getInt("id_mesa");
                if (idMesa > 0) {
                    MesaDTO m = new MesaDTO(idMesa);
                    m.setIdMesa(idMesa);
                    r.setIdMesa(m);
                }

                // Fecha (Timestamp -> LocalDateTime)
                Timestamp ts = rs.getTimestamp("fecha_hora");
                if (ts != null) {
                    r.setFechaHora(ts.toLocalDateTime());
                }

                r.setNumeroComensales(rs.getInt("numero_comensales"));
                r.setEstado(rs.getString("estado"));

                lista.add(r);
            }
        }
        return lista;
    }
}