package Controlador;

import java.io.IOException;

import DTO.ProductoDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class CamareroViewControlador {
	
    @FXML
    private TableView<ProductoDTO> tablaProductos;
    
    @FXML
    private TableColumn<ProductoDTO, String> colProducto;
    
    @FXML
    private TableColumn<ProductoDTO, Integer> colCantidad;
   
    @FXML
    private TableColumn<ProductoDTO, Double> colPrecio;
    
    @FXML
    private TextField precioTotal, entregado, devolver;
    
    @FXML
    private AnchorPane productoAnchorPane;
    
    @FXML
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnComa, btnDelete, btnClear;
    
    @FXML
    public void initialize() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
    }
    
    
    public void agregarProducto(String nombre, double precio) {
        for (ProductoDTO p : tablaProductos.getItems()) {
            if (p.getNombre().equals(nombre)) {
                p.setCantidad(p.getCantidad() + 1);
                tablaProductos.refresh();
                actualizarPrecioTotal();
                return;
            }
        }
        tablaProductos.getItems().add(new ProductoDTO(nombre, 1, precio));
        actualizarPrecioTotal();
    }
    
    
    public void actualizarPrecioTotal() {
        double total = 0;
        for (ProductoDTO p : tablaProductos.getItems()) {
            total += p.getCantidad() * p.getPrecio();
        }
        precioTotal.setText(total +  "");
    }

    // Refresco: carga la vista de refrescos
    @FXML
    public void Refresco() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/RefrescoView.fxml"));
        AnchorPane view = loader.load();

        // Pasar la tabla al controlador de Refresco
        RefrescoViewControlador controller = loader.getController();
        controller.setTablaProductos(tablaProductos);
        controller.setCamareroController(this); // Para actualizar total desde Refresco

        // Mostrar la vista
        productoAnchorPane.getChildren().setAll(view);

        //AnchorPane.setTopAnchor(view, 0.0);
        //AnchorPane.setBottomAnchor(view, 0.0);
        //AnchorPane.setLeftAnchor(view, 0.0);
        //AnchorPane.setRightAnchor(view, 0.0);
    }
    
    
    @FXML
    public void borrar() {
        // Obtener el producto seleccionado
        ProductoDTO seleccionado = tablaProductos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            if (seleccionado.getCantidad() > 1) {
                seleccionado.setCantidad(seleccionado.getCantidad() - 1);
                tablaProductos.refresh();
            } else {
                tablaProductos.getItems().remove(seleccionado);
            }

            // Actualizar el total
            actualizarPrecioTotal();
        }
    }
}