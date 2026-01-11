package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import ConexionBd.ConexionBD;
import DTO.PlatoDTO;

public class PlatoDAO {

    public PlatoDTO crearPlato(PlatoDTO p) throws SQLException {
        String sql = "INSERT INTO plato (id_categoria, nombre, descripcion, precio, imgUrl, alergenos) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, p.getIdCategoria());
            pst.setString(2, p.getNombre());
            pst.setString(3, p.getDescripcion());
            pst.setDouble(4, p.getPrecio());
            pst.setString(5, p.getImgUrl());
            pst.setString(6, convertirListaAJson(p.getAlergenos()));

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdPlato(rs.getInt(1));
                }
            }
        }
        return p;
    }

    public boolean modificarPlato(PlatoDTO p) throws SQLException {
        String sql = "UPDATE plato SET id_categoria = ?, nombre = ?, descripcion = ?, precio = ?, imgUrl = ?, alergenos = ? WHERE id_plato = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, p.getIdCategoria());
            pst.setString(2, p.getNombre());
            pst.setString(3, p.getDescripcion());
            pst.setDouble(4, p.getPrecio());
            pst.setString(5, p.getImgUrl());
            pst.setString(6, convertirListaAJson(p.getAlergenos()));
            pst.setInt(7, p.getIdPlato());

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
            return pst.executeUpdate() > 0;
        }
    }

    // -- LISTAR TODOS LOS PLATOS --
    public List<PlatoDTO> listarPlatos() throws SQLException {
        List<PlatoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM plato";

        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                PlatoDTO p = mapearPlatoConAlergenos(rs);
                lista.add(p);
            }
        }
        return lista;
    }

    public List<PlatoDTO> obtenerPlatosPorCategoria(int categoriaId) throws SQLException {
        List<PlatoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM plato WHERE id_categoria = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, categoriaId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    PlatoDTO p = mapearPlatoConAlergenos(rs);
                    lista.add(p);
                }
            }
        }
        return lista;
    }
    
    
    public List<PlatoDTO> obtenerPlatosPorCategoriaBotones(int idCategoria) throws SQLException {
        List<PlatoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM plato WHERE id_categoria = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idCategoria);
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

    public int obtenerIdPlatoPorNombre(String nombre) {

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id_plato;
    }

    public PlatoDTO obtenerPlatoPorNombre(String nombre) {
        PlatoDTO plato = null;
        String sql = "SELECT * FROM plato WHERE nombre = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, nombre);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    plato = mapearPlatoConAlergenos(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plato;
    }


    public PlatoDTO obtenerPlatoPorId(int idPlato) {
        PlatoDTO plato = null;
        String sql = "SELECT * FROM plato WHERE id_plato = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, idPlato);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    plato = mapearPlatoConAlergenos(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plato;
    }


    private List<String> convertirJsonALista(String json) {
        List<String> lista = new ArrayList<>();

        try {
            if (json != null && !json.trim().isEmpty() && !json.equals("[]")) {
                String contenido = json.replace("[", "").replace("]", "").replace("\"", "").trim();
                if (!contenido.isEmpty()) {
                    String[] elementos = contenido.split(",");
                    for (String elemento : elementos) {
                        lista.add(elemento.trim());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al convertir JSON: " + json);
            e.printStackTrace();
        }

        return lista;
    }

    private String convertirListaAJson(List<String> lista) {
        if (lista == null || lista.isEmpty()) {
            return "[]";
        }

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            json.append("\"").append(lista.get(i)).append("\"");
            if (i < lista.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        return json.toString();
    }

    private PlatoDTO mapearPlatoConAlergenos(ResultSet rs) throws SQLException {
        PlatoDTO p = new PlatoDTO(
                rs.getInt("id_plato"),
                rs.getInt("id_categoria"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio"),
                rs.getString("imgUrl")
        );

        String jsonAlergenos = rs.getString("alergenos");
        if (jsonAlergenos != null) {
            List<String> alergenos = convertirJsonALista(jsonAlergenos);
            p.setAlergenos(alergenos);
        }

        return p;
    }

    public List<PlatoDTO> obtenerPlatosConAlergeno(String alergeno) throws SQLException {
        List<PlatoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM plato WHERE JSON_CONTAINS(alergenos, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "\"" + alergeno + "\"");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    PlatoDTO p = mapearPlatoConAlergenos(rs);
                    lista.add(p);
                }
            }
        }
        return lista;
    }
    
}