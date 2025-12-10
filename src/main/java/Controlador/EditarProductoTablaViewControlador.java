package Controlador;

import DTO.PlatoDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EditarProductoTablaViewControlador {
	
	@FXML
	private TextField  txtProducto, txtCantidad, txtPrecio;
	
	@FXML
	private Button btnCancelar, btnGuardar;
	
	@FXML
	private Text textoErrorProducto, textoErrorCantidad, textoErrorPrecio;
	
	private PlatoDTO p;
	
	
	public void setProductos(PlatoDTO p) {
		
		this.p = p;
		
		txtProducto.setText(p.getNombre());
		txtCantidad.setText(p.getCantidad() + "");
		txtPrecio.setText(p.getPrecio() + "");
		
	}
	
	
	@FXML
	public void guardar(ActionEvent event) {
		
		try {
			p.setNombre(txtProducto.getText());
			p.setCantidad(Integer.parseInt(txtCantidad.getText()));
	        p.setPrecio(Double.parseDouble(txtPrecio.getText()));
			
			
			Stage st = (Stage) txtProducto.getScene().getWindow();
			st.close();
		}catch(NumberFormatException e) {
			
			/*Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setContentText("Este campo es obligatorio");
		    alert.showAndWait();*/
		    
			if(txtProducto.getText().isEmpty()) {
				textoErrorProducto.setText("Este campo es obligatorio");
				textoErrorProducto.setVisible(true);
			}
			
			if(txtCantidad.getText().isEmpty()) {
				textoErrorCantidad.setText("Este campo es obligatorio");
				textoErrorCantidad.setVisible(true);
			}
			
			if(txtPrecio.getText().isEmpty()) {
				textoErrorPrecio.setText("Este campo es obligatorio");
				textoErrorPrecio.setVisible(true);
			}
		}
	}
	
	
	@FXML
	public void cancelar(ActionEvent event) {
		
		Stage st = (Stage) txtProducto.getScene().getWindow();
		st.close();
		
	}
	
}