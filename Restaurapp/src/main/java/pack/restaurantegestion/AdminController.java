package pack.restaurantegestion; 

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AdminController {

    @FXML
    private BorderPane mainBorderPane;

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

    @FXML
    private VBox contentArea;

    @FXML
    private Label lblTituloSeccion;

    @FXML
    public void initialize() {
        // Este método se ejecuta automáticamente al cargar la vista
        System.out.println("Vista de Admin cargada correctamente.");
    }

    @FXML
    void handleShowDashboard(ActionEvent event) {
        lblTituloSeccion.setText("Resumen General");
        System.out.println("Mostrando Dashboard...");
        // Aquí volverías a cargar el panel inicial si lo has cambiado
    }

    @FXML
    void handleGestionPersonal(ActionEvent event) {
        lblTituloSeccion.setText("Gestión de Personal");
        System.out.println("Click en Gestión Personal");
        // Lógica para cargar la vista de personal en el centro
    }

    @FXML
    void handleGestionMenu(ActionEvent event) {
        lblTituloSeccion.setText("Edición de Menú");
        System.out.println("Click en Gestión Menú");
        // Lógica para cargar la vista de menú en el centro
    }

    @FXML
    void handleGestionMesas(ActionEvent event) {
        lblTituloSeccion.setText("Layout de Mesas");
        System.out.println("Click en Gestión Mesas");
        // Lógica para cargar la vista de mesas
    }

    @FXML
    void handleHistorialVentas(ActionEvent event) {
        lblTituloSeccion.setText("Historial de Ventas");
        System.out.println("Click en Historial");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        System.out.println("Cerrando sesión...");
        // Aquí va la lógica para volver a cargar el Login.fxml
        // Por ejemplo: App.setRoot("Login");
    }
}