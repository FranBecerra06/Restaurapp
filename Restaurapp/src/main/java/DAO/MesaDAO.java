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

public class MesaDAO {

    // -- INSERTAR MESA --
    public MesaDTO crearMesa(MesaDTO m) throws SQLException {
        String sql = "INSERT INTO mesa (capacidad, disponibilidad) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, m.getCapacidad());
            pst.setString(2, m.getDisponibilidad());

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setIdMesa(rs.getInt(1));
                }
            }
        }
        return m;
    }

    // -- MODIFICAR MESA --
    public boolean modificarMesa(MesaDTO m) throws SQLException {
        String sql = "UPDATE mesa SET capacidad = ?, disponibilidad = ? WHERE id_mesa = ?";
        
        try (Connection conn = ConexionBD.getConnection(); 
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, m.getCapacidad());
            pst.setString(2, m.getDisponibilidad());
            pst.setInt(3, m.getIdMesa());

            int filas = pst.executeUpdate();
            return filas > 0;
        }
    }

    // -- ELIMINAR MESA --
    public boolean eliminarMesa(int idMesa) throws SQLException {
        String sql = "DELETE FROM mesa WHERE id_mesa = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idMesa);
            int filas = pst.executeUpdate();
            return filas > 0;
        }
    }

    // -- LISTAR MESAS --
    public List<MesaDTO> listarMesas() throws SQLException {
        List<MesaDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM mesa";

        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                MesaDTO mesa = new MesaDTO(rs.getInt("id_mesa"));
                mesa.setCapacidad(rs.getInt("capacidad"));
                mesa.setDisponibilidad(rs.getString("disponibilidad"));

                lista.add(mesa);
            }
        }
        return lista;
    }
}