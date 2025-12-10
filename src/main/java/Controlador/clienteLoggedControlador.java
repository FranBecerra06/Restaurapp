package Controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class clienteLoggedControlador {

    @FXML
    private Button botonPedir;

    @FXML
    private void initialize() { // Este metodo se ejecuta automáticamente
        botonPedir.setOnMouseEntered(e -> botonPedir.setOpacity(1.0));
        botonPedir.setOnMouseExited(e -> botonPedir.setOpacity(0.7));
    }
}