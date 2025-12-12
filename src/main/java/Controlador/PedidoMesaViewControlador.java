package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import DAO.CategoriaDAO;
import DAO.Mesa_PlatoDAO;
import DAO.PedidoDAO;
import DAO.Pedido_PlatoDAO;
import DAO.PlatoDAO;
import DTO.CategoriaDTO;
import DTO.MesaDTO;
import DTO.Mesa_PlatoDTO;
import DTO.PedidoDTO;
import DTO.Pedido_PlatoDTO;
import DTO.PlatoDTO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PedidoMesaViewControlador {
	
	@FXML
    private TableView<PlatoDTO> tablaProductos;
    
    @FXML
    private TableColumn<PlatoDTO, String> colProducto;
    
    @FXML
    private TableColumn<PlatoDTO, Integer> colCantidad;
   
    @FXML
    private TableColumn<PlatoDTO, Double> colPrecio;
    
    @FXML
    private TextField txtNumeroMesa, precioTotal, entregado, devolver;
    
    @FXML
    private AnchorPane productoAnchorPane, mesaAnchorPane;
    
    @FXML
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnComa, btnDelete, btnClear, btnCambiarMesa, btnDividirCuenta, btnSalir;
    
    @FXML
    private GridPane gridCategorias;
    
    private int numeroMesa;
    
    
    
    @FXML
    public void initialize() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        
        cargarCategorias(); 
    }
	
    
    public TableView<PlatoDTO> getTablaProductos() {
        return tablaProductos;
    }
    
    
    public void setMesa(int numMesa) throws SQLException {   //actualizarTabla desde la BD
        this.numeroMesa = numMesa;

        if (txtNumeroMesa != null) {
            txtNumeroMesa.setText("Numero: " + numMesa);
        }

        // Cargar productos de la BD para esta mesa
        Mesa_PlatoDAO mpDAO = new Mesa_PlatoDAO();
        List<Mesa_PlatoDTO> productosBD = mpDAO.obtenerPlatoPorMesa(numMesa);

        tablaProductos.getItems().clear();
        for (Mesa_PlatoDTO mp : productosBD) {
            PlatoDAO pDAO = new PlatoDAO();
            PlatoDTO plato = pDAO.obtenerPlatoPorId(mp.getId_plato());
            plato.setCantidad(mp.getCantidad());
            tablaProductos.getItems().add(plato);
        }

        if(!tablaProductos.getItems().isEmpty()) {
        	actualizarPrecioTotal();
        }
    }
    
    
    public void cargarCategorias() {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        List<CategoriaDTO> categorias = categoriaDAO.obtenerCategorias();

        gridCategorias.getChildren().clear();
        int fila = 0;
        int columna = 0;

        for (CategoriaDTO categoria : categorias) {
            Button btn = new Button(categoria.getNombre());
            btn.setPrefWidth(150);
            btn.setStyle(
            		"-fx-background-color: linear-gradient(to bottom, #FFB84D, #FF9500);" + // gradiente naranja
            	    "-fx-text-fill: white;" + // texto blanco
            	    "-fx-font-size: 18px;" + // tamaño de fuente más grande
            	    "-fx-font-weight: bold;" + // texto en negrita
            	    "-fx-background-radius: 15;" + // bordes redondeados
            	    "-fx-border-width: 2;" +
            	    "-fx-border-radius: 15;"
            	    );
            btn.setOnAction(e -> {
                try {
                    Refresco(categoria.getIdCategoria());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            gridCategorias.add(btn, columna, fila);
            columna++;
            if (columna > 1) { // tu GridPane tiene 2 columnas
                columna = 0;
                fila++;
            }
        }
    }
    
    
    public void agregarProducto(String nombre, double precio) {
        PlatoDAO pDAO = new PlatoDAO();
        int idPlato = pDAO.obtenerIdPlatoPorNombre(nombre); // <--- obtener el id real

        for (PlatoDTO p : tablaProductos.getItems()) {
            if (p.getNombre().equals(nombre)) {
                p.setCantidad(p.getCantidad() + 1);
                tablaProductos.refresh();
                actualizarPrecioTotal();

                // Actualizar BD
                Mesa_PlatoDTO mpDTO = new Mesa_PlatoDTO(numeroMesa, idPlato, p.getCantidad());
                Mesa_PlatoDAO mpDAO = new Mesa_PlatoDAO();
                mpDAO.actualizarMesaPlato(mpDTO); // update
                return;
            }
        }

        // Si no existe en la tabla, agregar
        PlatoDTO nuevo = new PlatoDTO(nombre, 1, precio);
        tablaProductos.getItems().add(nuevo);
        actualizarPrecioTotal();

        // Guardar en BD
        Mesa_PlatoDTO mpDTO = new Mesa_PlatoDTO(numeroMesa, idPlato, 1);
        Mesa_PlatoDAO mpDAO = new Mesa_PlatoDAO();
        try {
            mpDAO.crearMesaPlato(mpDTO);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void actualizarPrecioTotal() {
        double total = 0;
        for (PlatoDTO p : tablaProductos.getItems()) {
            total += p.getCantidad() * p.getPrecio();
        }
        precioTotal.setText(String.format(Locale.US, "%.2f", total));
    }
    
    
    @FXML
    public void Refresco(int idCategoria) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/PlatosView.fxml"));
        ScrollPane view = loader.load();

        // Pasar la tabla al controlador de Refresco
        PlatosViewControlador controller = loader.getController();
        //controller.setTablaProductos(tablaProductos);
        controller.setPedidoController(this); // Para actualizar total desde Refresco
        controller.cargarPlatos(idCategoria);

        // Mostrar la vista
        productoAnchorPane.getChildren().setAll(view);
    }
    
    
    @FXML
    public void borrar() throws SQLException {
        // Obtener el producto seleccionado
    	PlatoDTO seleccionado = tablaProductos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            if (seleccionado.getCantidad() > 1) {
                // Reducir la cantidad en 1
                seleccionado.setCantidad(seleccionado.getCantidad() - 1);
                tablaProductos.refresh(); // Refrescar la tabla para actualizar la cantidad
            } else {
                // Si solo queda 1, eliminar la fila
                tablaProductos.getItems().remove(seleccionado);
                
                Mesa_PlatoDAO mpDAO = new Mesa_PlatoDAO();
                mpDAO.eliminarPorMesa(numeroMesa);
            }

            // Actualizar el total
            actualizarPrecioTotal();
        }
    }
    
    
    @FXML
    public void numeroPulsado(ActionEvent event) {
        Button boton = (Button) event.getSource();
        String numeroPulsado = boton.getText();
        String actual = entregado.getText();
        
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
    }
    
    
    @FXML
    public void cobrar(ActionEvent event) throws SQLException, IOException {
        try {
            double total = Double.parseDouble(precioTotal.getText().trim());
            double pago = Double.parseDouble(entregado.getText().trim());

            double cambio = pago - total;

            devolver.setText(String.format(Locale.US, "%.2f", cambio));
            
            
            //añadir panel: quiere añadir alguna observacion?
            
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Observaciones");
            dialog.setHeaderText("¿Desea añadir alguna observación al pedido?");
            dialog.setContentText("Observación:");

            Optional<String> result = dialog.showAndWait();
            String observacion = result.orElse("");
            
            
            PedidoDTO pDTO = new PedidoDTO();
            
            MesaDTO m = new MesaDTO(numeroMesa);
            
            pDTO.setIdCamarero(1);
            pDTO.setIdMesa(m);
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
            
            Mesa_PlatoDAO mpDAO = new Mesa_PlatoDAO();
            mpDAO.eliminarPorMesa(numeroMesa);
            
            
            
        } catch (NumberFormatException e) {
            devolver.setText("ERROR");
        }
    }
    
    
    @FXML
    public void dividirCuenta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/DividirCuentaView.fxml"));
        AnchorPane root = loader.load(); // Cargar vista
        DividirCuentaViewControlador dcvc = loader.getController();
        
        
        // Crear nueva ventana
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show(); // Mostrar nueva ventana
        
        String total = precioTotal.getText();
        
        dcvc.setPedidoController(this, numeroMesa);
        dcvc.mostrarCuentaPrincipal(tablaProductos.getItems(), total);
        
        
    }
    
    
    public void actualizarTabla(ObservableList<PlatoDTO> productos, String total) {
    	
    	tablaProductos.setItems(productos);
    	
    }
    
    
    @FXML
    public void editar(ActionEvent event) throws IOException {
    	
    	PlatoDTO p = tablaProductos.getSelectionModel().getSelectedItem();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/EditarRefrescoView.fxml"));
    	AnchorPane root = loader.load();
    	
    	EditarProductoTablaViewControlador ervc = loader.getController();
    	
    	ervc.setProductos(p);
    	
    	Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.showAndWait();
		
		tablaProductos.refresh();
    	
		actualizarPrecioTotal();
    	
    }
    
    
    @FXML
    public void salir(ActionEvent event) throws IOException {
        // Cerrar la ventana actual
    	Stage stageActual = (Stage) btnSalir.getScene().getWindow();
        stageActual.close();

        // 2. Cargar la vista de CamareroView
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/CamareroView.fxml"));
        AnchorPane root = loader.load();

        // 3. Crear un nuevo Stage
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}