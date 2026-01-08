package Controlador;

import ConexionBd.ConexionBD; 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LogIn {

    private List<String> nombresUsuario = new ArrayList<>();


    @FXML
    private Button loginBtn;
    @FXML
    private TextField usuarioTF;
    @FXML
    private PasswordField contraUsuPF;
    @FXML
    private Label textoAuxiliar;



    @FXML
    private void initialize() {//IMPORTANTE! este metodo ocurre al iniciarse la aplicacion
        loginBtn.disableProperty().bind(//Enlazamos la propiedad isDisabled del boton a la propiedad isEmpty del passwordField
                contraUsuPF.textProperty().isEmpty()
        );
    }

    public void recibirNombre(String nombre){
        usuarioTF.setText(nombre);
    }

    //Metoto principal, ocurre al clicar en iniciar sesion
    public void logInAction(ActionEvent event) {
        getUsuarios();
        boolean busquedaUsuarioExitosa = false;
        String nombreUsuario="";
        boolean casoAdmin = false;

        textoAuxiliar.setText("");

        //Control de caso de Admin
        if (usuarioTF.getText().equals("Admin") ) {
            casoAdmin = true;
            if(contraUsuPF.getText().equals("1234")){
                cambiarAAdmin();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Admin");
                alert.setContentText("Contraseña incorrecta");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }

        //Control de caso de Camarero
        if (usuarioTF.getText().equals("Camarero") ) {
            casoAdmin = true;
            if(contraUsuPF.getText().equals("1234")){
                cambiarACamarero();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Admin");
                alert.setContentText("Contraseña incorrecta");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }

        //Control de caso de usuario existente
        while(!busquedaUsuarioExitosa){
            for(String nombreEnBusqueda : nombresUsuario){
                if(usuarioTF.getText().equals(nombreEnBusqueda)){
                    nombreUsuario = nombreEnBusqueda;
                    busquedaUsuarioExitosa = true;
                }
            }
            break;
        }
        if(busquedaUsuarioExitosa)
            iniciarSesionUsuario(getContrasena(nombreUsuario),nombreUsuario);
        //Control de caso de usuario inexistente
        if(!busquedaUsuarioExitosa)
            if(casoAdmin){
                textoAuxiliar.setText("Contraseña inválida para Adminitrador");
            }else {
                textoAuxiliar.setText("Usuario no reconocido");

            }
    }


    //Este metodo devuelve todos los usuarios almacenados en la base de datos
    public void getUsuarios(){
        try{
            Connection conn = ConexionBD.getConnection();
            System.out.println("Conexión establecida");
            Statement usersRetrieve = conn.createStatement();
            //Query SQL que se hará y se guarda como ResultSet
            String stm1 = "select nombre_usuario from usuario";
            ResultSet rs = usersRetrieve.executeQuery(stm1);
            //Aqui guardo los nombres recuperados en el resultSet en un arraylist nombresUsuario
            while(rs.next()){
                nombresUsuario.add(rs.getString("nombre_usuario"));
            }
        } catch (SQLException e) {
            System.out.println("Problema obteniendo usuarios");
            e.printStackTrace();
        }

    }

    public String getContrasena(String nombre){
        String contra = "";
        try {
            Connection connection = ConexionBD.getConnection();
            Statement statement = connection.createStatement();
            String query = "select contrasena from usuario where nombre_usuario = '"+nombre+"'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                contra = rs.getString("contrasena");
            }
        } catch (SQLException e) {
            System.out.println("Error en getContrasena");;
        }
        return contra;
    }

    public void iniciarSesionUsuario(String contrasena,String nombreUsuario){
        if(contraUsuPF.getText().equals(contrasena)){
            cambiarAClienteLogged();
        }else{
            textoAuxiliar.setText("Contraseña incorrecta");
        }

    }



    @FXML
    public void cambiarAClienteLogged(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/clienteLogged.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) loginBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setWidth(1280);
            currentStage.setHeight(720);
            //currentStage.setResizable(false);
            scene.getStylesheets().add("../resources/EstiloClienteLogged.css/");
            currentStage.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void cambiarACamarero(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/CamareroView.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) loginBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            //currentStage.setWidth(1280);
            //currentStage.setHeight(720);
            //currentStage.setResizable(false);
            //scene.getStylesheets().add("../resources/EstiloClienteLogged.css/");
            currentStage.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void cambiarAAdmin(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/AdminView.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) loginBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            //currentStage.setWidth(1280);
            //currentStage.setHeight(720);
            //currentStage.setResizable(false);
            //scene.getStylesheets().add("../resources/EstiloClienteLogged.css/");
            currentStage.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void cambiarASignIn(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/SignIn.fxml"));
            Parent root = loader.load();
            Stage currentScene = (Stage) loginBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            currentScene.setScene(scene);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}