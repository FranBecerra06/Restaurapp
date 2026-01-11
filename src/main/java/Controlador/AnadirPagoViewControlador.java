package Controlador;

import DTO.PlatoDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AnadirPagoViewControlador {
	
	@FXML
	private TextField txtProducto, txtCantidad, txtPrecio;
	
	@FXML
	private Button btnCancelar, btnGuardar;
	
	private PlatoDTO plato;
	
	
	@FXML
	public void guardar(ActionEvent event) {
		
		String producto = txtProducto.getText();
		String cantidadS = txtCantidad.getText();
		String precioS = txtPrecio.getText();
		
		int cantidad;
		
		if(cantidadS.isEmpty()) {
			cantidad = 1;
		}else {
			cantidad = Integer.parseInt(cantidadS);
		}
		double precio = Double.parseDouble(precioS);
		
		plato = new PlatoDTO(producto, cantidad, precio);
		
		Stage st = (Stage) txtProducto.getScene().getWindow();
        st.close();
		
	}
	
	
	@FXML
	public void cancelar(ActionEvent event) {
		
		Stage st = (Stage) txtProducto.getScene().getWindow();
		st.close();
		
	}
	
	
	public PlatoDTO getProductoCreado() {
		return plato;
	}
	
}