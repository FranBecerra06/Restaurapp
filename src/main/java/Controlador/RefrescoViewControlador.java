package Controlador;

import DTO.ProductoDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class RefrescoViewControlador {

    private TableView<ProductoDTO> tablaProductos;
    private CamareroViewControlador camareroController;

    // Botones del RefrescoView
    @FXML private Button btnCocaCola;
    @FXML private Button btnCocaColaZero;
    @FXML private Button btnFantaLimon;
    @FXML private Button btnFantaNaranja;
    @FXML private Button btn7Up;
    @FXML private Button btnNestea;
    @FXML private Button btnAquariusNaranja;
    @FXML private Button btnAquariusLimon;

    // Pasar la tabla desde CamareroView
    public void setTablaProductos(TableView<ProductoDTO> tablaProductos) {
        this.tablaProductos = tablaProductos;
    }

    // Pasar referencia al controlador principal
    public void setCamareroController(CamareroViewControlador controller) {
        this.camareroController = controller;
    }

    @FXML
    public void initialize() {
        // Asignar acciones a los botones
        btnCocaCola.setOnAction(e -> agregarProducto("Coca-Cola", 2.5));
        btnCocaColaZero.setOnAction(e -> agregarProducto("Coca-Cola Zero", 2.5));
        btnFantaLimon.setOnAction(e -> agregarProducto("Fanta Limón", 2.5));
        btnFantaNaranja.setOnAction(e -> agregarProducto("Fanta Naranja", 2.5));
        btn7Up.setOnAction(e -> agregarProducto("7 Up", 2.5));
        btnNestea.setOnAction(e -> agregarProducto("Nestea", 2.5));
        btnAquariusNaranja.setOnAction(e -> agregarProducto("Aquarius Naranja", 2.5));
        btnAquariusLimon.setOnAction(e -> agregarProducto("Aquarius Limón", 2.5));
    }

    // Agrega el producto a la tabla
    public void agregarProducto(String nombre, double precio) {
        if (tablaProductos != null) {
            for (ProductoDTO p : tablaProductos.getItems()) {
                if (p.getNombre().equals(nombre)) {
                    p.setCantidad(p.getCantidad() + 1);
                    tablaProductos.refresh();
                    if (camareroController != null) camareroController.actualizarPrecioTotal();
                    return;
                }
            }
            tablaProductos.getItems().add(new ProductoDTO(nombre, 1, precio));
            if (camareroController != null) camareroController.actualizarPrecioTotal();
        }
    }
}