package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import DAO.PedidoDAO;
import DAO.Pedido_PlatoDAO;
import DAO.PlatoDAO;
import DTO.PedidoDTO;
import DTO.Pedido_PlatoDTO;
import DTO.PlatoDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DividirCuentaViewControlador {

	@FXML
	private Button btnMoverDerecha, btnMoverIzquierda, btnVolver, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnComa,
	btnDelete, btnClear;

	@FXML
	private TextField totalPrincipal, totalDividir, totalEntregado, totalDevolver;

	@FXML
	private TableView<PlatoDTO> tablaPrincipal, tablaDividir;

	@FXML
	private TableColumn<PlatoDTO, String> colProductoP, colProductoD;

	@FXML
	private TableColumn<PlatoDTO, Integer> colCantidadP, colCantidadD;

	@FXML
	private TableColumn<PlatoDTO, Double> colPrecioP, colPrecioD;

	private CamareroViewControlador cvc;
	
	private PedidoMesaViewControlador pmvc;

	private boolean cobrar = false;

	private ObservableList<PlatoDTO> listaPrincipalLocal = FXCollections.observableArrayList();


	public void setCamareroController(CamareroViewControlador controller) {

		this.cvc = controller;

	}
	
	public void setPedidoController(PedidoMesaViewControlador controller) {

		this.pmvc = controller;

	}


	public void mostrarCuentaPrincipal(ObservableList<PlatoDTO> productos, String total) {

		listaPrincipalLocal.clear();

		for (PlatoDTO p : productos) {
			PlatoDTO prd = new PlatoDTO(p.getNombre(), p.getCantidad(), p.getPrecio());
			listaPrincipalLocal.add(prd);
		}

		tablaPrincipal.setItems(listaPrincipalLocal);
		totalPrincipal.setText(total);

	}


	public void initialize() {
        colProductoP.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colProductoD.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        colCantidadP.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCantidadD.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        colPrecioP.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPrecioD.setCellValueFactory(new PropertyValueFactory<>("precio"));
    }


	@FXML
    private void numeroPulsado(ActionEvent event) {
        Button boton = (Button) event.getSource();
        String numeroPulsado = boton.getText();
        String actual = totalEntregado.getText();

        switch (numeroPulsado) {
        case "C":
            totalEntregado.clear();
            break;

        case "<":
            if (!actual.isEmpty()) {
                totalEntregado.setText(actual.substring(0, actual.length() - 1));
            }
            break;

        case ".":
            if (!actual.contains(".")) {
                totalEntregado.setText(actual + ".");
            }
            break;

        default:
            // Aquí entran 0-9 u otros números
            totalEntregado.setText(actual + numeroPulsado);
            break;
        }
	}


	@FXML
    public void cobrar(ActionEvent event) throws SQLException, IOException {
        try {
            double total = Double.parseDouble(totalDividir.getText().trim());
            double pago = Double.parseDouble(totalEntregado.getText().trim());

            double cambio = pago - total;

            totalDevolver.setText(String.format(Locale.US, "%.2f", cambio));
            
            
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
            
            for (PlatoDTO plato : tablaDividir.getItems()) {
            	
            	PlatoDAO pDAO = new PlatoDAO();
            	
            	int id_plato = pDAO.obtenerIdPlatoPorNombre(plato.getNombre());
            	
                Pedido_PlatoDTO ppDTO = new Pedido_PlatoDTO(idUltimoPedido, id_plato, plato.getCantidad());
                
                System.out.println(ppDTO.getId_pedido() + "" + ppDTO.getId_plato() + "" + ppDTO.getCantidad());
                
                ppDAO.crearPedidoPlato(ppDTO);
            }
            
            tablaDividir.getItems().clear();
            totalDividir.clear();
            totalEntregado.clear();
            
            
        } catch (NumberFormatException e) {
            totalDevolver.setText("ERROR");
        }
    }

	//---------MOVER A CUENTA DIVIDIR---------------------------------------------------------------------

	@FXML
	public void moverCuentaDividir(ActionEvent event) {
		PlatoDTO seleccionado = tablaPrincipal.getSelectionModel().getSelectedItem();

	    if (seleccionado != null) {
	        // Buscar si ya existe en tablaDividir
	    	PlatoDTO existente = null;
	        for (PlatoDTO p : tablaDividir.getItems()) {
	            if (p.getNombre().equals(seleccionado.getNombre())) {
	                existente = p;
	                break;
	            }
	        }

	        if (seleccionado.getCantidad() > 1) {
	            // Reducir cantidad en tablaPrincipal
	            seleccionado.setCantidad(seleccionado.getCantidad() - 1);
	        } else {
	            // Si es 1, quitarlo de tablaPrincipal
	            tablaPrincipal.getItems().remove(seleccionado);
	        }

	        actualizarPrecioTotalPrincipal();

	        if (existente != null) {
	            // Incrementar cantidad si ya existe en tablaDividir
	            existente.setCantidad(existente.getCantidad() + 1);
	        } else {
	            // Sino, crear una copia con cantidad 1 y añadir
	        	PlatoDTO nuevo = new PlatoDTO(seleccionado.getNombre(), 1, seleccionado.getPrecio());
	            tablaDividir.getItems().add(nuevo);
	        }

	        actualizarPrecioTotalDividir();

	        // Refrescar ambas tablas
	        tablaPrincipal.refresh();
	        tablaDividir.refresh();
	    }
	}


	//-------------------------------------------------------------------------------------------------------


	//------------MOVER A CUENTA PRINCIPAL-------------------------------------------------------------------

	@FXML
	public void moverCuentaPrincipal() {
		PlatoDTO seleccionado = tablaDividir.getSelectionModel().getSelectedItem();

	    if (seleccionado != null) {
	        // Buscar si ya existe en tablaDividir
	    	PlatoDTO existente = null;
	        for (PlatoDTO p : tablaPrincipal.getItems()) {
	            if (p.getNombre().equals(seleccionado.getNombre())) {
	                existente = p;
	                break;
	            }
	        }

	        if (seleccionado.getCantidad() > 1) {
	            // Reducir cantidad en tablaPrincipal
	            seleccionado.setCantidad(seleccionado.getCantidad() - 1);
	        } else {
	            // Si es 1, quitarlo de tablaPrincipal
	            tablaDividir.getItems().remove(seleccionado);
	        }

	        actualizarPrecioTotalDividir();

	        if (existente != null) {
	            // Incrementar cantidad si ya existe en tablaDividir
	            existente.setCantidad(existente.getCantidad() + 1);
	        } else {
	            // Sino, crear una copia con cantidad 1 y añadir
	        	PlatoDTO nuevo = new PlatoDTO(seleccionado.getNombre(), 1, seleccionado.getPrecio());
	            tablaPrincipal.getItems().add(nuevo);
	        }

	        actualizarPrecioTotalPrincipal();

	        // Refrescar ambas tablas
	        tablaPrincipal.refresh();
	        tablaDividir.refresh();
	    }
	}


	//---------------------------------------------------------------------------------------------------


	@FXML
	public void volver(ActionEvent event) {
		
		
		if (!tablaDividir.getItems().isEmpty()) {
	        boolean confirmado = confirmar("Hay platos en la tabla dividir.\nSi vuelves, se descartarán.\n¿Deseas continuar?");
	        if (!confirmado) return;
	    }
		
		
		if(!cobrar) {
			for(PlatoDTO p : tablaDividir.getItems()) {

				PlatoDTO existe = null;

				for(PlatoDTO productoUnico : tablaPrincipal.getItems()) {
					if(productoUnico.getNombre().equals(p.getNombre())) {
						existe = productoUnico;
						break;
					}
				}

				if(existe != null) {
					existe.setCantidad(existe.getCantidad() + p.getCantidad());
				}else {
					PlatoDTO nuevo = new PlatoDTO(p.getNombre(), p.getCantidad(), p.getPrecio());
					tablaPrincipal.getItems().add(nuevo);
				}

			}

			tablaDividir.getItems().clear();

			actualizarPrecioTotalPrincipal();
		    actualizarPrecioTotalDividir();
		}

	    if (cvc != null) {
	        String total = totalPrincipal.getText();
	        cvc.actualizarTabla(tablaPrincipal.getItems(), total);
	    }

	    // Cerrar la ventana
	    Stage st = (Stage) totalPrincipal.getScene().getWindow();
	    st.close();
	}


	public void actualizarPrecioTotalPrincipal() {
        double total = 0;
        for (PlatoDTO p : tablaPrincipal.getItems()) {
            total += p.getCantidad() * p.getPrecio();
        }
        totalPrincipal.setText(String.format(Locale.US, "%.2f", total));
    }


	public void actualizarPrecioTotalDividir() {
        double total = 0;
        for (PlatoDTO p : tablaDividir.getItems()) {
            total += p.getCantidad() * p.getPrecio();
        }
        totalDividir.setText(String.format(Locale.US, "%.2f", total));
    }
	
	
	public boolean confirmar(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        return alerta.showAndWait().filter(bt -> bt == ButtonType.OK).isPresent();
    }
	
}