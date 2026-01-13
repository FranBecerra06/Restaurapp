package Controlador;

import DTO.PlatoDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AnadirPagoViewControlador {
	
	@FXML
	private TextField txtPrecio;
	
	@FXML
	private Text txtError;
	
	@FXML
	private Button btnCancelar, btnGuardar;
	
	private PlatoDTO p;
	
	private String plato;
	
	
	public void obtenerPlato(String nombrePlato) {
		plato = nombrePlato;
	}
	
	
	@FXML
	public void guardar(ActionEvent event) {
		
	    String precioS = txtPrecio.getText();
	    
	    if (precioS == null || precioS.isEmpty()) {
	        txtError.setText("Este campo es obligatorio");
	        txtError.setVisible(true);
	        return;
	    }
	    
	    double precio;
	    
	    try {
	        precio = Double.parseDouble(precioS);
	    } catch (NumberFormatException e) {
	        txtError.setText("Introduce un precio válido");
	        txtError.setVisible(true);
	        return;
	    }
	    
	    if (precio <= 0) {
	        txtError.setText("El precio debe ser mayor a 0");
	        txtError.setVisible(true);
	        return;
	    }
	    
	    p = new PlatoDTO(plato, 1, precio);
	    
	    Stage st = (Stage) txtPrecio.getScene().getWindow();
	    st.close();
	}
	
	
	@FXML
	public void cancelar(ActionEvent event) {
		
		Stage st = (Stage) txtPrecio.getScene().getWindow();
		st.close();
		
	}
	
	
	public PlatoDTO getProductoCreado() {
		return p;
	}
	
}