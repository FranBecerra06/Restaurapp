package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import DAO.CategoriaDAO;
import DAO.PedidoDAO;
import DAO.Pedido_PlatoDAO;
import DAO.PlatoDAO;
import DTO.CategoriaDTO;
import DTO.PedidoDTO;
import DTO.Pedido_PlatoDTO;
import DTO.PlatoDTO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
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
    private ImageView imagenLogo, notificacion;
    
    @FXML
    private StackPane paneNotificacion;

    @FXML
    private Label badge;
    
    public static Map<PlatoDTO, Integer> mapa;
    public static int numeroMesa;
    
    @FXML
    public void initialize() throws SQLException {
        
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        
        cargarCategorias();
        
        Image image = new Image(getClass().getResourceAsStream("/Imagenes/logoRestaurapp.png"));
    	imagenLogo.setImage(image);
    	
    	Image imagenNotificacion = new Image(getClass().getResourceAsStream("/Imagenes/notificacion.png"));
    	notificacion.setImage(imagenNotificacion);
    	
    	actualizarNotificacion();
    }
    
    
    public TableView<PlatoDTO> getTablaProductos() {
        return tablaProductos;
    }
    
    
    public void cargarCategorias() {

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        List<CategoriaDTO> categorias = categoriaDAO.obtenerCategorias();

        flowCategorias.getChildren().clear();

        for (CategoriaDTO categoria : categorias) {
        	
        	if(categoria.getNombre().equalsIgnoreCase("otro")) {
        		continue;
        	}

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
                    plato(categoria.getIdCategoria(), categoria.getNombre());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            flowCategorias.getChildren().add(btn);
        }
    }
    
    
    
    public void plato(int idCategoria, String nombreCategoria) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/PlatosView.fxml"));
        ScrollPane view = loader.load();
        
        PlatosViewControlador pvc = loader.getController();
        pvc.setCamareroController(this);
        
        if(!nombreCategoria.equalsIgnoreCase("otro")) {
        	pvc.cargarPlatos(idCategoria);
        }
        
        productoAnchorPane.getChildren().setAll(view);
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
    
    
    @FXML
    public void borrar() {
    	
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
                
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Observaciones");
                dialog.setHeaderText("¿Desea añadir alguna observación al pedido?");
                dialog.setContentText("Observación:");

                Optional<String> result = dialog.showAndWait();  //result toma el valor que ha escrito el camarero
                String observacion = result.orElse("");  //observacion tomara el valor de result o en su defecto ("")
                
                LocalDateTime fecha = LocalDateTime.now();
                
                
                PedidoDTO pDTO = new PedidoDTO(1, null, fecha, total, observacion);
                
                /*pDTO.setIdCamarero(1);
                pDTO.setIdMesa(null);
                pDTO.setTotal(total);
                pDTO.setObservaciones("");
                pDTO.setFecha(LocalDateTime.now());
                pDTO.setObservaciones(observacion);*/
                
                PedidoDAO p = new PedidoDAO();
                
                p.crearPedido(pDTO);
                
                
                int idUltimoPedido = p.obtenerUltimoIdPedido();
                
                System.out.println(idUltimoPedido);
                
                Pedido_PlatoDAO ppDAO = new Pedido_PlatoDAO();
                
                for (PlatoDTO plato : tablaProductos.getItems()) {
                	
                	PlatoDAO pDAO = new PlatoDAO();
                	
                	if(!plato.getNombre().isEmpty()) {
                		
                		int id_plato = pDAO.obtenerIdPlatoPorNombre(plato.getNombre());
                    	
                        Pedido_PlatoDTO ppDTO = new Pedido_PlatoDTO(idUltimoPedido, id_plato, plato.getCantidad());
                        
                        System.out.println(ppDTO.getId_pedido() + "" + ppDTO.getId_plato() + "" + ppDTO.getCantidad());
                        
                        ppDAO.crearPedidoPlato(ppDTO);
                	}
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
            AnchorPane root = loader.load();
            DividirCuentaViewControlador dcvc = loader.getController();
            
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No puedes salir");
            alert.setContentText("Primero debes cobrar o eliminar los productos de la tabla.");
            alert.showAndWait();
            return;
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
    
    
    @FXML
    public void anadirPago(ActionEvent event) throws IOException {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/AnadirPagoView.fxml"));
        Parent root = loader.load();
        
        AnadirPagoViewControlador apvc = loader.getController();
        apvc.obtenerPlato("otro");
        
        Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.showAndWait();
		
		
		PlatoDTO extra = apvc.getProductoCreado();
		
		if(extra != null) {
			tablaProductos.getItems().add(extra);
			tablaProductos.refresh();
			actualizarPrecioTotal();
		}
		
    }
    
    
    @FXML
    public void ajustes(ActionEvent event) throws IOException {
    	
    	if(!tablaProductos.getItems().isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.WARNING);
    		alert.setTitle("Advertencia");
    		alert.setHeaderText("Se perderan todos los productos de la tabla");
    		alert.setContentText("¿Deseas continuar?");

    		Optional<ButtonType> resultado = alert.showAndWait();

    		if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/AdminView.fxml"));
    	         Parent root = loader.load();
    	         
    	         TextInputDialog dialog = new TextInputDialog();
    	         dialog.setTitle("Ajustes");
    	         dialog.setHeaderText("Introduzca la contraseña del administrador");
    	         dialog.setContentText("Contraseña:");

    	         Optional<String> result = dialog.showAndWait();
    	         
    	         if(result.isPresent()) {
    	        	 String contrasena = result.get();
    	        	 if(contrasena.equals("1234")) {
    	            	 Stage stage = (Stage) btnSalir.getScene().getWindow();
    	                 stage.setScene(new Scene(root));
    	                 stage.show();
    	             }else {
    	            	 Alert alerta = new Alert(Alert.AlertType.WARNING);
    	                 alerta.setTitle("Advertencia");
    	                 alerta.setHeaderText("Contraseña incorrecta");
    	                 alerta.setContentText("No se ha podido cambiar a la vista camarero debido a que la contraseña no coincide");
    	                 alerta.showAndWait();
    	                 return;
    	             }
    	         }else {
    	        	 return;
    	         }
    		} else {
    			return;
    		}
    	}else {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/AdminView.fxml"));
	         Parent root = loader.load();
	         
	         TextInputDialog dialog = new TextInputDialog();
	         dialog.setTitle("Ajustes");
	         dialog.setHeaderText("Introduzca la contraseña del administrador");
	         dialog.setContentText("Contraseña:");

	         Optional<String> result = dialog.showAndWait();
	         
	         if(result.isPresent()) {
	        	 String contrasena = result.get();
	        	 if(contrasena.equals("1234")) {
	            	 Stage stage = (Stage) btnSalir.getScene().getWindow();
	                 stage.setScene(new Scene(root));
	                 stage.show();
	             }else {
	            	 Alert alerta = new Alert(Alert.AlertType.WARNING);
	                 alerta.setTitle("Advertencia");
	                 alerta.setHeaderText("Contraseña incorrecta");
	                 alerta.setContentText("No se ha podido cambiar a la vista camarero debido a que la contraseña no coincide");
	                 alerta.showAndWait();
	                 return;
	             }
	         }else {
	        	 return;
	         }
    	}
    	
    }
    
    
    public void actualizarNotificacion() {
        Platform.runLater(() -> {
            if (mapa != null && !mapa.isEmpty()) {
                badge.setVisible(true);
                badge.setText("1");
            } else {
                badge.setVisible(false);
            }
        });
    }
    
    
    @FXML
    public void notificacion(MouseEvent event) throws IOException {
    	
    	if(mapa == null || mapa.isEmpty()) {
    		return;
    	}
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/NotificacionPedidoView.fxml"));
    	Parent root = loader.load();
    	
    	NotificacionPedidoViewControlador npvc = loader.getController();
    	npvc.notificacionPedido(CamareroViewControlador.mapa, CamareroViewControlador.numeroMesa);
        
        Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.showAndWait();
		
		mapa.clear();
		actualizarNotificacion();
    }

}