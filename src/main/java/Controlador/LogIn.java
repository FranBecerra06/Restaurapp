package Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class LogIn {

    @FXML
    private Button loginBtn;
    @FXML
    private TextField usuarioTF;
    @FXML
    private PasswordField contraUsuPF;

    public void logInAction(ActionEvent event){
        if(usuarioTF.getText().equals("Admin") && contraUsuPF.getText().equals("1234")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Admin");
            alert.setContentText("Admin ha iniciado sesión");
            Optional<ButtonType> result = alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Admin");
            alert.setContentText("Admin no ha iniciado sesión");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

}
