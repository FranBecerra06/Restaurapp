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
	private TextField txtCantidad, txtPrecio;
	
	@FXML
	private Button btnCancelar, btnGuardar;
	
	@FXML
	private Text textoErrorProducto, textoErrorCantidad, textoErrorPrecio;
	
	private PlatoDTO p;
	
	
	public void setProductos(PlatoDTO p) {
		
		this.p = p;
		
		txtCantidad.setText(p.getCantidad() + "");
		txtPrecio.setText(p.getPrecio() + "");
		
	}
	
	
	@FXML
	public void guardar(ActionEvent event) {

	    boolean valido = true;
	    
	    textoErrorCantidad.setVisible(false);
	    textoErrorPrecio.setVisible(false);
	    
	    
	    if (txtCantidad.getText().isEmpty()) {
	        textoErrorCantidad.setText("Este campo es obligatorio");
	        textoErrorCantidad.setVisible(true);
	        valido = false;
	    }else if (txtCantidad.getText().equals("0")){
	    	textoErrorCantidad.setText("La cantidad no puede ser 0");
	        textoErrorCantidad.setVisible(true);
	        valido = false;
	    }else {
	    	try {
	    		p.setCantidad(Integer.parseInt(txtCantidad.getText()));
	    	}catch (NumberFormatException e){
	    		textoErrorCantidad.setText("Debe ser un número");
	            textoErrorCantidad.setVisible(true);
	            valido = false;
	    	}
	    }
	    
	    
	    
	    if (txtPrecio.getText().isEmpty()) {
	        textoErrorPrecio.setText("Este campo es obligatorio");
	        textoErrorPrecio.setVisible(true);
	        valido = false;
	    }else {
	    	try {
	    		p.setPrecio(Double.parseDouble(txtPrecio.getText()));
	    	}catch (NumberFormatException e) {
	    		textoErrorPrecio.setText("Debe ser un número");
	            textoErrorPrecio.setVisible(true);
	            valido = false;
	    	}
	    }

	    // Si falta algún campo → NO seguir
	    if (!valido) return;
	    
	    Stage st = (Stage) txtCantidad.getScene().getWindow();
        st.close();
	    
	}

	
	
	@FXML
	public void cancelar(ActionEvent event) {
		
		Stage st = (Stage) txtCantidad.getScene().getWindow();
		st.close();
		
	}
	
}