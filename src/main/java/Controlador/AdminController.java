package Controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import DAO.MesaDAO;
import DAO.PedidoDAO;
import DAO.UsuarioDAO;
import DTO.MesaDTO;
import DTO.PedidoDTO;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AdminController implements Initializable {

    @FXML private BorderPane mainBorderPane;
    @FXML private Label lblTituloSeccion;

    @FXML private Button btnDashboard;
    @FXML private Button btnGestionPersonal;
    @FXML private Button btnGestionMenu;
    @FXML private Button btnCategoria;
    @FXML private Button btnGestionMesas;
    @FXML private Button btnHistorial; 
    @FXML private Button btnLogout;

    @FXML private Label lblVentas;
    @FXML private Label lblPersonal;
    @FXML private Label lblMesas;

    private Node dashboardView;
    
    private MesaDAO mesaDAO = new MesaDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (mainBorderPane != null) {
            dashboardView = mainBorderPane.getCenter();
        }
        
        actualizarDashboard();
        
        Timeline reloj = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            if (mainBorderPane.getCenter() == dashboardView) {
                actualizarDashboard();
            }
        }));
        reloj.setCycleCount(Timeline.INDEFINITE);
        reloj.play();
    }

    private void actualizarDashboard() {
        try {
            // A) MESAS LIBRES
            List<MesaDTO> mesas = mesaDAO.listarMesas();
            long libres = mesas.stream()
                               .filter(m -> "Disponible".equals(m.getDisponibilidad()))
                               .count();
            
            if (lblMesas != null) {
                lblMesas.setText(libres + " Libres");
            }

            // B) PERSONAL TOTAL
            List<String> usuarios = usuarioDAO.listarUsuarioClientes();
            if (lblPersonal != null) {
                lblPersonal.setText(String.valueOf(usuarios.size()));
            }

            // C) VENTAS EN DINERO (€)
            List<PedidoDTO> pedidos = pedidoDAO.listarPedidos();
            double totalEstimado = 0;
            
            if (lblVentas != null) {
                for(PedidoDTO p : pedidos) {
                    totalEstimado += p.getTotal();
                }
                lblVentas.setText(String.format("%.2f €", totalEstimado));
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar dashboard: " + e.getMessage());
        }
    }

    // --- ACCIONES DE LOS BOTONES ---

    @FXML
    void handleShowDashboard(ActionEvent event) {
        lblTituloSeccion.setText("Resumen General");
        if (dashboardView != null) {
            mainBorderPane.setCenter(dashboardView);
            actualizarDashboard();
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
    void handleHistorialVentas(ActionEvent event) {
        lblTituloSeccion.setText("Historial de Ventas");
        cargarVista("Historial.fxml");
    }
    
    @FXML
    void handleCategoria(ActionEvent event) {
        lblTituloSeccion.setText("Categoria Producto");
        cargarVista("CategoriaProducto.fxml");
    }
    
    @FXML
    void handleHistorialMesas(ActionEvent event) {
        lblTituloSeccion.setText("Layout de Mesas");
        cargarVista("GestionMesas.fxml");
    }

    // --- CAMBIO IMPORTANTE: Renombrado a 'irACamarero' para coincidir con tu FXML ---
    @FXML
    void irACamarero(ActionEvent event) {
        try {
            // 1. Cerrar la ventana actual (Admin)
            Node source = (Node) event.getSource();
            Stage stageActual = (Stage) source.getScene().getWindow();
            stageActual.close();

            // 2. Cargar la vista de Camarero
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/CamareroView.fxml"));
            Parent root = loader.load();

            // 3. Abrir la nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Vista Camarero");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo abrir la vista de Camarero");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/Main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - RestaurApp");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void cargarVista(String nombreArchivo) {
        try {
            URL url = getClass().getResource("/pack/restaurantegestion/" + nombreArchivo);
            if (url == null) {
                System.err.println("ERROR: No encuentro: " + nombreArchivo);
                return;
            }
            FXMLLoader loader = new FXMLLoader(url);
            Parent vistaNueva = loader.load();
            mainBorderPane.setCenter(vistaNueva);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}