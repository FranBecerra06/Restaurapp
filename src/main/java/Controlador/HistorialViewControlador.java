package Controlador;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import DAO.PedidoDAO;
import DTO.PedidoDTO;
import DTO.VentaDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistorialViewControlador {
	
	@FXML
	private TableView<VentaDTO> tablaVentas;
	
	@FXML
	private DatePicker fechaInicio, fechaFin;
	
	@FXML
	private TableColumn<VentaDTO, Integer> colId;
	
	@FXML
	private TableColumn<VentaDTO, LocalDateTime> colFecha;
	
	@FXML
	private TableColumn<VentaDTO, Double> colTotal;
	
	@FXML
	private TableColumn<VentaDTO, String> colCamarero;
	
	@FXML
	private Label lblVentas;
	
	private PedidoDAO pedidoDAO;
	
	
	@FXML
	public void initialize() {
		
		pedidoDAO = new PedidoDAO();
		
		colId.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
		colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
		colCamarero.setCellValueFactory(new PropertyValueFactory<>("camarero"));
		
		actualizarTabla();
		actualizarTotalGeneral();
		
	}
	
	
	public void actualizarTabla() {
		
		try {
	        PedidoDAO pDAO = new PedidoDAO();
	        List<PedidoDTO> pedidos = pDAO.listarPedidos();
	        ObservableList<VentaDTO> ventas = FXCollections.observableArrayList();

	        for(PedidoDTO p : pedidos) {
	            String nombreCamarero = "Camarero " + p.getIdCamarero(); // o buscar nombre real si tienes tabla usuario
	            ventas.add(new VentaDTO(p.getIdPedido(), p.getFecha(), p.getTotal(), nombreCamarero));
	        }

	        tablaVentas.setItems(ventas);

	    } catch(SQLException e) {
	        e.printStackTrace();
	    }
		
	}
	
	
	public void actualizarTotalGeneral() {
	    try {
	        PedidoDAO pDAO = new PedidoDAO();
	        List<PedidoDTO> pedidos = pDAO.listarPedidos(); // todos los pedidos
	        double suma = 0;
	        for (PedidoDTO p : pedidos) {
	            suma += p.getTotal();
	        }
	        lblVentas.setText(String.format("%.2f €", suma));
	    } catch (SQLException e) {
	        e.printStackTrace();
	        lblVentas.setText("Error");
	    }
	}
	
	
	@FXML
	public void filtrarPorFecha() throws SQLException {
		
		if (fechaInicio.getValue() == null || fechaFin.getValue() == null) {
	        return; // no filtramos hasta que estén las dos
	    }
		
		LocalDate inicio = fechaInicio.getValue();
		LocalDate fin = fechaFin.getValue();
		
		if (fin.isBefore(inicio)) {
	        Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setTitle("Rango incorrecto");
	        alert.setHeaderText("Fecha inválida");
	        alert.setContentText("La fecha de fin no puede ser anterior a la de inicio.");
	        alert.showAndWait();
	        return;
	    }
		
		Date fechaSQLInicio = Date.valueOf(inicio);
	    Date fechaSQLFin = Date.valueOf(fin);
		
	    List<PedidoDTO> filtrados = pedidoDAO.listarPedidosPorFecha(fechaSQLInicio, fechaSQLFin);
	    
	    // Convertimos PedidoDTO → VentaDTO
	    ObservableList<VentaDTO> ventasFiltradas = FXCollections.observableArrayList();
	    
	    for (PedidoDTO p : filtrados) {
	        String nombreCamarero = "Camarero " + p.getIdCamarero();
	        ventasFiltradas.add(new VentaDTO(p.getIdPedido(), p.getFecha(), p.getTotal(), nombreCamarero));
	    }
	    
        // Sumar total real
        double suma = 0;
        for (PedidoDTO p : filtrados) {
            suma += p.getTotal();
        }
        
        lblVentas.setText(String.format("%.2f €", suma));
        
        // Si tienes tabla:
        tablaVentas.setItems(ventasFiltradas);
		
	}
	
}