package pack.restaurantegestion;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdminController implements Initializable {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label lblTituloSeccion;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnGestionPersonal;

    @FXML
    private Button btnGestionMenu;

    @FXML
    private Button btnGestionMesas;

    @FXML
    private Button btnHistorial;

    @FXML
    private Button btnLogout;

    // Variable para guardar la vista original (las gráficas del inicio)
    private Node dashboardView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Guardamos lo que hay en el centro al arrancar (el Dashboard)
        // Esto es importante para poder volver aquí si pulsamos "Resumen General"
        if (mainBorderPane != null) {
            dashboardView = mainBorderPane.getCenter();
        }
    }

    // --- ACCIONES DE LOS BOTONES ---

    @FXML
    void handleShowDashboard(ActionEvent event) {
        lblTituloSeccion.setText("Resumen General");
        if (dashboardView != null) {
            mainBorderPane.setCenter(dashboardView);
        }
    }

    @FXML
    void handleGestionPersonal(ActionEvent event) {
        lblTituloSeccion.setText("Gestión de Personal");
        cargarVista("GestionPersonal.fxml");
    }

    @FXML
    void handleGestionMenu(ActionEvent event) {
        lblTituloSeccion.setText("Edición de Menú");
        cargarVista("Menu.fxml");
    }

    @FXML
    void handleGestionMesas(ActionEvent event) {
        lblTituloSeccion.setText("Layout de Mesas");
        cargarVista("GestionMesas.fxml");
    }

    @FXML
    void handleHistorialVentas(ActionEvent event) {
        lblTituloSeccion.setText("Historial de Ventas");
        cargarVista("Historial.fxml");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        System.out.println("Cerrando sesión...");
        try {
            // Cargar la vista de Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
            Parent root = loader.load();
            
            // Obtener el escenario (Stage) actual y cambiar la escena
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login - RestaurApp");
            stage.show();
            
        } catch (IOException e) {
            System.err.println("Error al intentar cerrar sesión. Verifica que SignIn.fxml existe.");
            e.printStackTrace();
        }
    }

    // --- MÉTODO AUXILIAR PARA CARGAR VISTAS ---
    
    private void cargarVista(String nombreArchivo) {
        try {
            // Intentamos cargar el archivo FXML
            // Al poner solo el nombre, busca en el mismo paquete que esta clase java
            URL url = getClass().getResource(nombreArchivo);
            
            if (url == null) {
                System.err.println("❌ ERROR: No se encuentra el archivo: " + nombreArchivo);
                lblTituloSeccion.setText("Error: Archivo no encontrado");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent vistaNueva = loader.load();
            
            // Ponemos la nueva vista en el CENTRO del BorderPane
            mainBorderPane.setCenter(vistaNueva);
            
        } catch (IOException e) {
            System.err.println("❌ Error crítico al cargar el FXML: " + nombreArchivo);
            e.printStackTrace();
        }
    }
}