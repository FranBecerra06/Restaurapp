package Controlador;

import DAO.UsuarioDAO;
import DTO.UsuarioDTO;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SignInControlador {

    UsuarioDAO usuarioDAO = new UsuarioDAO();


    @FXML
    private Button signInBtn;
    @FXML
    private Button signInVolverBtn;
    @FXML
    private TextField signInNombreField;
    @FXML
    private TextField signInApellidosField;
    @FXML
    private TextField signInEmailField;
    @FXML
    private TextField signInTelefonoField;
    @FXML
    private TextField signInUsuarioField;
    @FXML
    private PasswordField signInContraField;
    @FXML
    private Label textoAuxiliar;

    //Con este método no dejaremos pulsar el boton si no tenemos los campos mínimos con algo de texto
    @FXML
    private void initialize() {
        boolean crearUsuOk = false;
        signInBtn.disableProperty().bind(
                Bindings.or(
                        signInUsuarioField.textProperty().isEmpty(),
                        Bindings.or(
                                signInEmailField.textProperty().isEmpty(),
                                Bindings.or(
                                        signInNombreField.textProperty().isEmpty(),
                                        Bindings.or(
                                                signInApellidosField.textProperty().isEmpty(),
                                                Bindings.or(
                                                        signInTelefonoField.textProperty().isEmpty(),
                                                        signInContraField.textProperty().isEmpty()
                                                )
                                        )
                                )
                        )
                )
        );
    }

    @FXML
    public void signInAction(ActionEvent event){
        textoAuxiliar.setText("");
        String nombre, apellidos, email, telefono, usuario, contra;
        nombre = signInNombreField.getText();
        apellidos = signInApellidosField.getText();
        email = signInEmailField.getText();
        telefono = signInTelefonoField.getText();
        usuario = signInUsuarioField.getText();
        contra = signInContraField.getText();

        boolean nombreCorrecto, apellidosCorrecto,emailCorrecto, telefonoCorrecto, usuarioCorrecto,contraCorrecta;

        nombreCorrecto = comprobarNombre(nombre);
        apellidosCorrecto = comprobarApellidos(apellidos);
        emailCorrecto = comprobarEmail(email);
        telefonoCorrecto=comprobarTelefono(telefono);
        usuarioCorrecto = comprobarUsuario(usuario);
        contraCorrecta = comprobarContra(contra);

        if(nombreCorrecto&&apellidosCorrecto&&emailCorrecto&&telefonoCorrecto&&usuarioCorrecto&&contraCorrecta){
            UsuarioDTO dto = new UsuarioDTO(nombre,apellidos,email,contra,telefono,usuario);

            try {
            /*System.out.println("Cliente con nombre "+ dto.getNombre()+" apellido "+dto.getApellidos()+
                    " email "+dto.getEmail()+" contraseña "+dto.getContrasena()+" telefono "+dto.getTelefono()+" nombreUsuario "+
                    dto.getNombre_usuario());*/
                usuarioDAO.crearCliente(dto);
                System.out.println("Usuario creado correctamente");
                cambiarALogIn(dto.getNombre_usuario());
            } catch (SQLException e) {
                System.out.println("Error creando usuario");
            }
        }else{
            System.out.println("Usuario no validado");
        }


    }

    //Vamos a usar una serie de métodos similares para la validación de datos.
    public boolean comprobarUsuario(String nombreComprobando){
        List<String> usuarios = new ArrayList<>();
        boolean res = false;
        try {
            usuarios = usuarioDAO.listarUsuarioClientes();
        } catch (SQLException e) {
            System.out.println("Problema recuperando lista con nombres de usuario");
        }
        if(nombreComprobando.length() < 1 || nombreComprobando.length() > 20){
            textoAuxiliar.setText("El nombre debe tener entre 1 y 20 caracteres");
        } else if(usuarios.contains(nombreComprobando)){
            textoAuxiliar.setText("Nombre de usuario no disponible");
        }else{
            res = true;
        }
        return res;
    }

    public boolean comprobarNombre(String nombreComprobando){
        boolean res = false;
        if(nombreComprobando.length() < 1 || nombreComprobando.length() > 20){
            textoAuxiliar.setText("El nombre debe tener entre 1 y 20 caracteres");
        } else{
            res = true;
        }
        return res;
    }

    public boolean comprobarApellidos(String apellidosComprobando){
        boolean res = false;
        if(apellidosComprobando.length() < 1 || apellidosComprobando.length() > 50){
            textoAuxiliar.setText("Sus apellidos deben ocupar entre 1 y 50 caracteres");
        } else{
            res = true;
        }
        return res;
    }

    public boolean comprobarTelefono(String telefono) {
        String patron = "^[0-9]{9}$";
        boolean res = false;
        if(!telefono.matches(patron)){
            textoAuxiliar.setText("El teléfono debe estar compuesto por 9 dígitos");
        }else{
            res = true;
        }
        return res;
    }

    public boolean comprobarContra(String contra){
        boolean res = false;
        if(contra.length() < 1 || contra.length() > 20){
            textoAuxiliar.setText("Su contraseña debe ocupar entre 1 y 20 caracteres");
        } else{
            res = true;
        }
        return res;
    }

    public boolean comprobarEmail(String emailComprobando){
        List<String> emails = new ArrayList<>();
        boolean res = false;
        try {
            emails = usuarioDAO.listarEmailClientes();
        } catch (SQLException e) {
            System.out.println("Problema recuperando lista con emails de usuario");
        }

        if(emailValido(emailComprobando)){
            if(emails.contains(emailComprobando)){
                textoAuxiliar.setText("Email ya registrado");
            }else{
                res = true;
            }
        }else{
            textoAuxiliar.setText("Formato email incorrecto");
        }
        return res;
    }

    public static boolean emailValido(String email) {
        String patron = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(patron);
    }

    @FXML
    public void cambiarALogIn(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/Main.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) signInVolverBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setWidth(1280);
            currentStage.setHeight(720);
            currentStage.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void cambiarALogIn(String nombreUsuario){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/Main.fxml"));
            Parent root = loader.load();

            //Hacemos referencia al controlador y llamamos a su método recibir nombre que hará que cuando
            //volvamos esté ya precargado el nombre de usuario
            LogIn logIn = loader.getController();
            logIn.recibirNombre(nombreUsuario);

            Stage currentStage = (Stage) signInVolverBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setWidth(1280);
            currentStage.setHeight(720);
            currentStage.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}
