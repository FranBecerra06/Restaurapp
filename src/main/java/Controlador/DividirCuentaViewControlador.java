package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import DAO.Mesa_PlatoDAO;
import DAO.PedidoDAO;
import DAO.Pedido_PlatoDAO;
import DAO.PlatoDAO;
import DTO.MesaDTO;
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
	
	private int numeroMesa;
	

	private ObservableList<PlatoDTO> listaPrincipalLocal = FXCollections.observableArrayList();


	public void setCamareroController(CamareroViewControlador controller) {

		this.cvc = controller;

	}
	
	public void setPedidoController(PedidoMesaViewControlador controller, int numeroMesa) {

		this.pmvc = controller;
		this.numeroMesa = numeroMesa;

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

        if(!totalDividir.getText().isEmpty()) {
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
        }else {
        	return;
        }
	}
	
	
	@FXML
	public void cobrar(ActionEvent event) throws SQLException, IOException {

	    try {
	        // ----------------------- 1. CALCULAR CAMBIO -----------------------
	        double total = Double.parseDouble(totalDividir.getText().trim());
	        double pago = Double.parseDouble(totalEntregado.getText().trim());
	        double cambio = pago - total;
	        
	        if(pago < total) {
	        	Alert alerta = new Alert(Alert.AlertType.WARNING);
            	alerta.setTitle("Advertencia");
                alerta.setHeaderText("No se ha realizado el cobro");
                alerta.setContentText("La cantidad entregada es menor que el total");
                alerta.showAndWait();
                
                totalEntregado.clear();
                
                return;
	        }else {
	        	totalDevolver.setText(String.format(Locale.US, "%.2f", cambio));


		        // ----------------------- 2. OBSERVACIONES -----------------------
		        TextInputDialog dialog = new TextInputDialog();
		        dialog.setTitle("Observaciones");
		        dialog.setHeaderText("¿Desea añadir alguna observación al pedido?");
		        dialog.setContentText("Observación:");
		        String observacion = dialog.showAndWait().orElse("");


		        // ----------------------- 3. CREAR PEDIDO -----------------------
		        
		        LocalDateTime fecha = LocalDateTime.now();
		        
		        PedidoDTO pedidoDTO;
		        PedidoDAO pedidoDAO = new PedidoDAO();

		        if (numeroMesa > 0) {
		        	MesaDTO m = new MesaDTO(numeroMesa);
		        	pedidoDTO = new PedidoDTO(1, m, fecha, total, observacion);
		        } else {

		        	pedidoDTO = new PedidoDTO(1, null, fecha, total, observacion);
		        }
		        
		        
		        pedidoDAO.crearPedido(pedidoDTO);


		        // ----------------------- 4. OBTENER ID -----------------------
		        int idPedido = pedidoDAO.obtenerUltimoIdPedido();


		        // ----------------------- 5. INSERTAR PEDIDO_PLATO -----------------------
		        Pedido_PlatoDAO pedidoPlatoDAO = new Pedido_PlatoDAO();
		        PlatoDAO platoDAO = new PlatoDAO();
		        Mesa_PlatoDAO mesaPlatoDAO = new Mesa_PlatoDAO();

		        for (PlatoDTO plato : tablaDividir.getItems()) {

		            // Obtener ID real del plato
		            int idPlato = platoDAO.obtenerIdPlatoPorNombre(plato.getNombre());

		            // Guardar en pedido_plato
		            Pedido_PlatoDTO ppDTO = new Pedido_PlatoDTO(
		                    idPedido,
		                    idPlato,
		                    plato.getCantidad()
		            );
		            pedidoPlatoDAO.crearPedidoPlato(ppDTO);


		            // ----------------------- 6. RESTAR CANTIDADES SI VIENE DE MESA -----------------------
		            if (numeroMesa > 0) {

		                // Cantidad que se está pagando
		                int cantidadPagada = plato.getCantidad();

		                // Cantidad que hay en BD
		                int cantidadActual = mesaPlatoDAO.obtenerCantidad(numeroMesa, idPlato);

		                int nuevaCantidad = cantidadActual - cantidadPagada;

		                if (nuevaCantidad > 0) {
		                    // Actualizar cantidad restante
		                    mesaPlatoDAO.actualizarCantidad(numeroMesa, idPlato, nuevaCantidad);
		                } else {
		                    // Si llega a 0, eliminar fila
		                    mesaPlatoDAO.eliminarMesaPlato(numeroMesa, idPlato);
		                }
		            }
		        }


		        // ----------------------- 7. LIMPIAR -----------------------
		        tablaDividir.getItems().clear();
		        totalDividir.clear();
		        totalEntregado.clear();
	        }

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
	    
	    try {
	        if (pmvc != null && numeroMesa > 0) {
	            pmvc.setMesa(numeroMesa);  // <--- RECARGA REAL DESDE BD
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
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
		
		if(tablaDividir.getItems().isEmpty()) {
			totalDividir.clear();
			totalEntregado.clear();
		}
		
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
	
	
	@FXML
    public void limpiar(ActionEvent event) {
    	totalDevolver.clear();
    }
	
}