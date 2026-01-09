package Controlador;

import DTO.PlatoDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AnadirPagoViewControlador {
	
	@FXML
	private TextField txtPrecio;
	
	@FXML
	private Button btnCancelar, btnGuardar;
	
	private PlatoDTO plato;
	
	
	@FXML
	public void guardar(ActionEvent event) {
		
		String precioS = txtPrecio.getText();
		
		double precio = Double.parseDouble(precioS);
		
		plato = new PlatoDTO("", 1, precio);
		
		Stage st = (Stage) txtPrecio.getScene().getWindow();
        st.close();
		
	}
	
	
	@FXML
	public void cancelar(ActionEvent event) {
		
		Stage st = (Stage) txtPrecio.getScene().getWindow();
		st.close();
		
	}
	
	
	public PlatoDTO getProductoCreado() {
		return plato;
	}
	
}