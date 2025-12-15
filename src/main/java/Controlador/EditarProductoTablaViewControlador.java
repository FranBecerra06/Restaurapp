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

	    boolean valido = true;

	    textoErrorProducto.setVisible(false);
	    textoErrorCantidad.setVisible(false);
	    textoErrorPrecio.setVisible(false);

	    if (txtProducto.getText().isEmpty()) {
	        textoErrorProducto.setText("Este campo es obligatorio");
	        textoErrorProducto.setVisible(true);
	        valido = false;
	    }

	    if (txtCantidad.getText().isEmpty()) {
	        textoErrorCantidad.setText("Este campo es obligatorio");
	        textoErrorCantidad.setVisible(true);
	        valido = false;
	    }

	    if (txtPrecio.getText().isEmpty()) {
	        textoErrorPrecio.setText("Este campo es obligatorio");
	        textoErrorPrecio.setVisible(true);
	        valido = false;
	    }

	    // Si falta algún campo → NO seguir
	    if (!valido) return;

	    try {
	        p.setNombre(txtProducto.getText());
	        p.setCantidad(Integer.parseInt(txtCantidad.getText()));
	        p.setPrecio(Double.parseDouble(txtPrecio.getText()));

	        Stage st = (Stage) txtProducto.getScene().getWindow();
	        st.close();

	    } catch (NumberFormatException e) {

	        // valida formato numérico incorrecto
	        textoErrorCantidad.setText("Debe ser un número");
	        textoErrorCantidad.setVisible(true);

	        textoErrorPrecio.setText("Debe ser un número");
	        textoErrorPrecio.setVisible(true);
	    }
	}

	
	
	@FXML
	public void cancelar(ActionEvent event) {
		
		Stage st = (Stage) txtProducto.getScene().getWindow();
		st.close();
		
	}
	
}