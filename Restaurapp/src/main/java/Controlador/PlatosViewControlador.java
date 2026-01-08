package Controlador;

import java.sql.SQLException;
import java.util.List;

import DAO.PlatoDAO;
import DTO.PlatoDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

public class PlatosViewControlador {

    @FXML
    private GridPane gridRefrescos;

    @FXML
    private TableView<PlatoDTO> tablaProductos;

    private CamareroViewControlador camareroController;
    
    private PedidoMesaViewControlador pedidoController;

    public void setCamareroController(CamareroViewControlador controller) {
        this.camareroController = controller;
        this.tablaProductos = controller.getTablaProductos(); // Asegura que trabajamos sobre la misma tabla
    }
    
    
    public void setPedidoController(PedidoMesaViewControlador controller) {
        this.pedidoController = controller;
        this.tablaProductos = controller.getTablaProductos(); // Asegura que trabajamos sobre la misma tabla
    }
    

    public void cargarPlatos(int idCategoria) throws SQLException {
        PlatoDAO platoDAO = new PlatoDAO();
        List<PlatoDTO> platos = platoDAO.obtenerPlatosPorCategoriaBotones(idCategoria);

        gridRefrescos.getChildren().clear();
        
        

        int col = 0, row = 0;
        
        
        
        for (PlatoDTO p : platos) {
            Button btn = new Button(p.getNombre());
            btn.setPrefWidth(180);
            btn.setStyle(
                "-fx-background-color: #FFCA80; " +
                "-fx-font-size: 16px; " +
                "-fx-background-radius: 15; " +
                "-fx-border-color: #3B3B3B; " +
                "-fx-border-radius: 15;"
            );

            btn.setOnAction(e -> {
                if (camareroController != null) {
                    camareroController.agregarProducto(p.getNombre(), p.getPrecio());
                } else if (pedidoController != null) {
                    pedidoController.agregarProducto(p.getNombre(), p.getPrecio());
                }
            });


            gridRefrescos.add(btn, col, row);

            col++;
            if (col >= 3) { // 3 columnas
                col = 0;
                row++;
            }
        }
    }
}