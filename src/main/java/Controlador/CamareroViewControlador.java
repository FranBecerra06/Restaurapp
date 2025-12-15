package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Pos;

import DAO.CategoriaDAO;
import DAO.PedidoDAO;
import DAO.Pedido_PlatoDAO;
import DAO.PlatoDAO;
import DTO.CategoriaDTO;
import DTO.PedidoDTO;
import DTO.Pedido_PlatoDTO;
import DTO.PlatoDTO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class CamareroViewControlador {
	
    @FXML
    private TableView<PlatoDTO> tablaProductos;
    
    @FXML
    private TableColumn<PlatoDTO, String> colProducto;
    
    @FXML
    private TableColumn<PlatoDTO, Integer> colCantidad;
   
    @FXML
    private TableColumn<PlatoDTO, Double> colPrecio;
    
    @FXML
    private TextField txtUsuario, precioTotal, entregado, devolver;
    
    @FXML
    private AnchorPane productoAnchorPane, mesaAnchorPane;
    
    @FXML
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnComa, btnDelete, btnClear, btnMesas, btnDividirCuenta, btnSalir,
    btnLimpiar;
    
    @FXML
    private FlowPane flowCategorias;
    
    
    @FXML
    public void initialize() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        
        cargarCategorias(); 
    }
    
    
    /*public void mostrarUsuarioCamarero() {
    	
    }*/
    
    
    public TableView<PlatoDTO> getTablaProductos() {
        return tablaProductos;
    }
    
    
    public void cargarCategorias() {

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        List<CategoriaDTO> categorias = categoriaDAO.obtenerCategorias();

        flowCategorias.getChildren().clear();

        for (CategoriaDTO categoria : categorias) {

            Button btn = new Button(categoria.getNombre());

            btn.setPrefWidth(105);
            btn.setMinHeight(45);
            btn.setWrapText(true);
            btn.setTextAlignment(TextAlignment.CENTER); // centra las líneas dentro del texto
            btn.setAlignment(Pos.CENTER);

            btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #FFB84D, #FF9500);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20;"
            );

            btn.setOnAction(e -> {
                try {
                    Refresco(categoria.getIdCategoria());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            flowCategorias.getChildren().add(btn);
        }
    }
    
    
    public void agregarProducto(String nombre, double precio) {
        for (PlatoDTO p : tablaProductos.getItems()) {
            if (p.getNombre().equals(nombre)) {
                p.setCantidad(p.getCantidad() + 1);
                tablaProductos.refresh();
                actualizarPrecioTotal();
                return;
            }
        }
        tablaProductos.getItems().add(new PlatoDTO(nombre, 1, precio));
        actualizarPrecioTotal();
    }
    
    
    public void actualizarPrecioTotal() {
    	
    	if(tablaProductos.getItems().isEmpty()) {
    		precioTotal.clear();
    		entregado.clear();
    		return;
    	}
    	
        double total = 0;
        for (PlatoDTO p : tablaProductos.getItems()) {
            total += p.getCantidad() * p.getPrecio();
        }
        precioTotal.setText(String.format(Locale.US, "%.2f", total));
    }
    
    
    // Refresco: carga la vista de refrescos
    @FXML
    public void Refresco(int idCategoria) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/PlatosView.fxml"));
        ScrollPane view = loader.load();

        // Pasar la tabla al controlador de Refresco
        PlatosViewControlador controller = loader.getController();
        //controller.setTablaProductos(tablaProductos);
        controller.setCamareroController(this); // Para actualizar total desde Refresco
        controller.cargarPlatos(idCategoria);

        // Mostrar la vista
        productoAnchorPane.getChildren().setAll(view);
    }
    
    
    @FXML
    public void borrar() {

        // Obtener el producto seleccionado
        PlatoDTO seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        
        if (seleccionado == null) {
            return;
        }

        // Si la cantidad es mayor que 1, restamos 1
        if (seleccionado.getCantidad() > 1) {
            seleccionado.setCantidad(seleccionado.getCantidad() - 1);
            tablaProductos.refresh();  // Actualizar la tabla
        } 
        // Si la cantidad es 1, eliminamos toda la fila
        else {
            tablaProductos.getItems().remove(seleccionado);
        }

        // Actualizar el precio total
        actualizarPrecioTotal();
    }
    
    
    @FXML
    private void numeroPulsado(ActionEvent event) {
        Button boton = (Button) event.getSource();
        String numeroPulsado = boton.getText();
        String actual = entregado.getText();
        
        if(!precioTotal.getText().isEmpty()) {
        	switch (numeroPulsado) {
            case "C":
                entregado.clear();
                break;

            case "<":
                if (!actual.isEmpty()) {
                    entregado.setText(actual.substring(0, actual.length() - 1));
                }
                break;

            case ".":
                if (!actual.contains(".")) { 
                    entregado.setText(actual + ".");
                }
                break;

            default:  
                // Aquí entran 0-9 u otros números
                entregado.setText(actual + numeroPulsado);
                break;
            }
        }else {
        	return;
        }
    }
    
    
    @FXML
    public void cobrar(ActionEvent event) throws SQLException, IOException {
        try {
            double total = Double.parseDouble(precioTotal.getText().trim());
            double pago = Double.parseDouble(entregado.getText().trim());
            
            double cambio = pago - total;
            
            if(pago < total) {
            	Alert alerta = new Alert(Alert.AlertType.WARNING);
            	alerta.setTitle("Advertencia");
                alerta.setHeaderText("No se ha realizado el cobro");
                alerta.setContentText("La cantidad entregada es menor que el total");
                alerta.showAndWait();
                
                entregado.clear();
                
                return;
            }else {
            	devolver.setText(String.format(Locale.US, "%.2f", cambio));
                
                
                //añadir panel: quiere añadir alguna observacion?
                
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Observaciones");
                dialog.setHeaderText("¿Desea añadir alguna observación al pedido?");
                dialog.setContentText("Observación:");

                Optional<String> result = dialog.showAndWait();
                String observacion = result.orElse("");
                
                
                PedidoDTO pDTO = new PedidoDTO();
                
                pDTO.setIdCamarero(1);
                pDTO.setIdMesa(null);
                pDTO.setTotal(total);
                pDTO.setObservaciones("");
                pDTO.setFecha(LocalDateTime.now());
                pDTO.setObservaciones(observacion);
                
                PedidoDAO p = new PedidoDAO();
                
                p.crearPedido(pDTO);
                
                
                int idUltimoPedido = p.obtenerUltimoIdPedido();
                
                System.out.println(idUltimoPedido);
                
                Pedido_PlatoDAO ppDAO = new Pedido_PlatoDAO();
                
                for (PlatoDTO plato : tablaProductos.getItems()) {
                	
                	PlatoDAO pDAO = new PlatoDAO();
                	
                	int id_plato = pDAO.obtenerIdPlatoPorNombre(plato.getNombre());
                	
                    Pedido_PlatoDTO ppDTO = new Pedido_PlatoDTO(idUltimoPedido, id_plato, plato.getCantidad());
                    
                    System.out.println(ppDTO.getId_pedido() + "" + ppDTO.getId_plato() + "" + ppDTO.getCantidad());
                    
                    ppDAO.crearPedidoPlato(ppDTO);
                }
                
                tablaProductos.getItems().clear();
                precioTotal.clear();
                entregado.clear();
                
            }
            
        } catch (NumberFormatException e) {
            devolver.setText("ERROR");
        }
    }
    
    
    @FXML
    public void dividirCuenta(ActionEvent event) throws IOException {
    	
    	if(!tablaProductos.getItems().isEmpty()) {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/DividirCuentaView.fxml"));
            AnchorPane root = loader.load(); // Cargar vista
            DividirCuentaViewControlador dcvc = loader.getController();
            
            
            // Crear nueva ventana
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show(); // Mostrar nueva ventana
            
            String total = precioTotal.getText();
            
            dcvc.setCamareroController(this);
            dcvc.mostrarCuentaPrincipal(tablaProductos.getItems(), total);
    	}
        
    }
    
    
    public void actualizarTabla(ObservableList<PlatoDTO> productos, String total) {
    	
    	tablaProductos.setItems(productos);
		precioTotal.setText(total);
    	
    }
    
    
    @FXML
    public void editar(ActionEvent event) throws IOException {
    	
    	PlatoDTO seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
    	
        if (seleccionado == null) {
            return;
        }else {
        	PlatoDTO p = tablaProductos.getSelectionModel().getSelectedItem();
        	
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/EditarProductoTablaView.fxml"));
        	AnchorPane root = loader.load();
        	
        	EditarProductoTablaViewControlador eptvc = loader.getController();
        	
        	eptvc.setProductos(p);
        	
        	Stage stage = new Stage();
    		stage.setScene(new Scene(root));
    		stage.showAndWait();
    		
    		tablaProductos.refresh();
        	
    		actualizarPrecioTotal();
        }
    	
    }
    
    
    @FXML
    public void salir(ActionEvent event) throws IOException {
    	
    	if (!tablaProductos.getItems().isEmpty()) {
            // Crear un alert de advertencia
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No puedes salir");
            alert.setContentText("Primero debes cobrar o eliminar los productos de la tabla.");
            alert.showAndWait();
            return; // Salir del método, no cambiamos de ventana
        }
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/Main.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) precioTotal.getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
    @FXML
    public void mesa(ActionEvent event) throws IOException {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/MesaCamareroView.fxml"));
        Parent root = loader.load();

        mesaAnchorPane.getChildren().setAll(root);
    	
    }
    
    
    @FXML
    public void limpiar(ActionEvent event) {
    	devolver.clear();
    }

}