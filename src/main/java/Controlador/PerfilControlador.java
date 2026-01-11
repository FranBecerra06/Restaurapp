package Controlador;

import DAO.UsuarioDAO;
import DTO.UsuarioDTO;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import pack.restaurantegestion.Preferencias;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PerfilControlador {

    @FXML
    private Button pedirButton; // Ya estaba
    @FXML
    private Button alergenosButton;  // NUEVO
    @FXML
    private Button sNosototrosButton;    // NUEVO
    @FXML
    private Button perfilButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button guardarBtn;

    @FXML
    private TextField nombreTF, apellidosTF, correoTF, telefonoTF, usuarioTF;
    @FXML
    private PasswordField contraPF;
    @FXML
    private Label textoAuxiliar;

    private List<Button> listaBotones = new ArrayList<>();

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    UsuarioDTO usuario;

    @FXML
    public void initialize(){
        listaBotones.add(pedirButton);
        listaBotones.add(alergenosButton);
        listaBotones.add(sNosototrosButton);
        listaBotones.add(perfilButton);
        listaBotones.add(closeButton);

        for(Button b : listaBotones){
            aplicarAnimacionBoton(b);
        }

        try {
            usuario = usuarioDAO.obtenerUsuarioPorNombreDeUsuario(Preferencias.obtenerUsuario());
        }catch (SQLException e){
            System.out.println("Problema obteniendo usuario desde preferencias.");
        }

        if(usuario!=null){
            nombreTF.setText(usuario.getNombre());
            apellidosTF.setText(usuario.getApellidos());
            correoTF.setText(usuario.getEmail());
            telefonoTF.setText(usuario.getTelefono());
            usuarioTF.setText(usuario.getNombre_usuario());
            contraPF.setText(usuario.getContrasena());
        }

        guardarBtn.disableProperty().bind(
                Bindings.or(
                        usuarioTF.textProperty().isEmpty(),
                        Bindings.or(
                                correoTF.textProperty().isEmpty(),
                                Bindings.or(
                                        nombreTF.textProperty().isEmpty(),
                                        Bindings.or(
                                                apellidosTF.textProperty().isEmpty(),
                                                Bindings.or(
                                                        telefonoTF.textProperty().isEmpty(),
                                                        contraPF.textProperty().isEmpty()
                                                )
                                        )
                                )
                        )
                )
        );


    }

    @FXML
    public void guardar(ActionEvent event){
        System.out.println("Se pulsa el boton guardar");
        textoAuxiliar.setText("");
        String nombre, apellidos, email, telefono, usuario, contra;
        nombre = nombreTF.getText();
        apellidos = apellidosTF.getText();
        email = correoTF.getText();
        telefono = telefonoTF.getText();
        usuario = usuarioTF.getText();
        contra = contraPF.getText();

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
                usuarioDAO.modificarUsuario(dto);
                System.out.println("Usuario modificado correctamente");
            } catch (SQLException e) {
                System.out.println("Error creando usuario");
            }
        }else{
            System.out.println("Usuario no validado");
        }
    }

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
            res = true;
        }else{
            textoAuxiliar.setText("Formato email incorrecto");
        }
        return res;
    }

    public static boolean emailValido(String email) {
        String patron = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(patron);
    }









    private void aplicarAnimacionBoton(Button button) {

        Pane underline = new Pane();
        underline.setStyle("-fx-background-color: #ce6a2e;");
        underline.setPrefHeight(2);
        underline.setPrefWidth(0);

        button.setGraphic(underline);
        button.setContentDisplay(ContentDisplay.BOTTOM);

        Interpolator spline = Interpolator.SPLINE(0.25, 0.8, 0.25, 1);
        Duration dur = Duration.millis(400);

        button.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                button.applyCss();
                button.layout();
                underline.setTranslateY(button.getHeight() - 2);
                underline.setTranslateX(button.getWidth() / 4.0);
            }
        });

        button.setOnMouseEntered(e -> {
            double w = button.getWidth();

            new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(button.textFillProperty(), Color.web("#432323"), spline)
                    ),
                    new KeyFrame(dur,
                            new KeyValue(button.textFillProperty(), Color.BLACK, spline)
                    )
            ).play();

            new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(underline.prefWidthProperty(), 0, spline),
                            new KeyValue(underline.translateXProperty(), w / 2.0, spline)
                    ),
                    new KeyFrame(dur,
                            new KeyValue(underline.prefWidthProperty(), w, spline),
                            new KeyValue(underline.translateXProperty(), 0, spline)
                    )
            ).play();
        });

        button.setOnMouseExited(e -> {
            double w = button.getWidth();

            new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(button.textFillProperty(), Color.BLACK, spline)
                    ),
                    new KeyFrame(dur,
                            new KeyValue(button.textFillProperty(), Color.web("#3d3d3d"), spline)
                    )
            ).play();

            new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(underline.prefWidthProperty(), w, spline),
                            new KeyValue(underline.translateXProperty(), 0, spline)
                    ),
                    new KeyFrame(dur,
                            new KeyValue(underline.prefWidthProperty(), 0, spline),
                            new KeyValue(underline.translateXProperty(), w / 2.0, spline)
                    )
            ).play();
        });
    }

    @FXML
    public void goToLanding(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/Landing.fxml"));
            Parent root = loader.load();
            Stage currentScene = (Stage) pedirButton.getScene().getWindow();
            Scene scene = new Scene(root);
            currentScene.setScene(scene);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToPedir(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/Pedir.fxml"));
            Parent root = loader.load();
            Stage currentScene = (Stage) pedirButton.getScene().getWindow();
            Scene scene = new Scene(root);
            currentScene.setScene(scene);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToAlergenos(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/ClienteAlergenos.fxml"));
            Parent root = loader.load();
            Stage currentScene = (Stage) pedirButton.getScene().getWindow();
            Scene scene = new Scene(root);
            currentScene.setScene(scene);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToSNosotros(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/SNosotros.fxml"));
            Parent root = loader.load();
            Stage currentScene = (Stage) pedirButton.getScene().getWindow();
            Scene scene = new Scene(root);
            currentScene.setScene(scene);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToPerfil(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/PerfilView.fxml"));
            Parent root = loader.load();
            Stage currentScene = (Stage) pedirButton.getScene().getWindow();
            Scene scene = new Scene(root);
            currentScene.setScene(scene);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void cerrarSesion(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/Main.fxml"));
            Parent root = loader.load();
            Stage currentScene = (Stage) pedirButton.getScene().getWindow();
            Scene scene = new Scene(root);
            currentScene.setScene(scene);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

