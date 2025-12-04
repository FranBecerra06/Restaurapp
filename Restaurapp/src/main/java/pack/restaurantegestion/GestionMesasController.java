package pack.restaurantegestion;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import DAO.MesaDAO;
import DTO.MesaDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class GestionMesasController implements Initializable {

    @FXML private TextField txtCapacidad;
    @FXML private ListView<MesaDTO> listaMesas;

    // Hemos eliminado el ComboBox de aquí

    private MesaDAO mesaDAO;
    private ObservableList<MesaDTO> mesasObservables;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mesaDAO = new MesaDAO();
        mesasObservables = FXCollections.observableArrayList();
        
        // Configuración visual de la lista (Verde/Rojo)
        listaMesas.setCellFactory(param -> new ListCell<MesaDTO>() {
            @Override
            protected void updateItem(MesaDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText("Mesa " + item.getIdMesa() + " - Cap: " + item.getCapacidad() + " (" + item.getDisponibilidad() + ")");
                    
                    if ("No Disponible".equals(item.getDisponibilidad())) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    }
                } else {
                    setText(null);
                    setStyle("");
                }
            }
        });
        
        listaMesas.setItems(mesasObservables);
        cargarMesas();
    }

    private void cargarMesas() {
        try {
            List<MesaDTO> mesasDB = mesaDAO.listarMesas();
            mesasObservables.setAll(mesasDB);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se cargaron las mesas: " + e.getMessage());
        }
    }

    @FXML
    void handleAnadir(ActionEvent event) {
        try {
            int capacidad = Integer.parseInt(txtCapacidad.getText());
            
            // --- CAMBIO: SIEMPRE "Disponible" ---
            // Ya no leemos del combo, lo ponemos fijo
            MesaDTO nuevaMesa = new MesaDTO(capacidad, "Disponible");
            
            mesaDAO.crearMesa(nuevaMesa);
            
            mostrarAlerta("Éxito", "Mesa creada (Disponible).");
            
            txtCapacidad.clear();
            cargarMesas();
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La capacidad debe ser un número.");
        } catch (SQLException e) {
            mostrarAlerta("Error SQL", "No se pudo crear: " + e.getMessage());
        }
    }

    @FXML
    void handleCambiarEstado(ActionEvent event) {
        MesaDTO seleccionada = listaMesas.getSelectionModel().getSelectedItem();
        
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona una mesa de la lista primero.");
            return;
        }

        try {
            String estadoActual = seleccionada.getDisponibilidad();
            // Alternamos entre los dos estados posibles
            String nuevoEstado = "Disponible".equals(estadoActual) ? "No Disponible" : "Disponible";
            
            seleccionada.setDisponibilidad(nuevoEstado);
            
            boolean exito = mesaDAO.modificarMesa(seleccionada);
            
            if (exito) {
                cargarMesas(); 
            } else {
                mostrarAlerta("Error", "No se pudo cambiar el estado en la BD.");
            }
            
        } catch (SQLException e) {
            mostrarAlerta("Error SQL", e.getMessage());
        }
    }

    @FXML
    void handleEliminar(ActionEvent event) {
        MesaDTO seleccionada = listaMesas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona una mesa para eliminar.");
            return;
        }
        try {
            mesaDAO.eliminarMesa(seleccionada.getIdMesa());
            cargarMesas();
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo eliminar: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}