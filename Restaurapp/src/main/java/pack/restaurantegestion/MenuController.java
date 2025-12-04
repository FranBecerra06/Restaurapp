package pack.restaurantegestion;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import DAO.PlatoDAO;
import DTO.PlatoDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;

public class MenuController implements Initializable {

    @FXML private ListView<PlatoDTO> listaPlatos;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private CheckBox chkGluten;

    private PlatoDAO platoDAO;
    private ObservableList<PlatoDTO> platosObservables;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        platoDAO = new PlatoDAO();
        platosObservables = FXCollections.observableArrayList();

        listaPlatos.setCellFactory(param -> new ListCell<PlatoDTO>() {
            @Override
            protected void updateItem(PlatoDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText(item.getNombre() + " - " + item.getPrecio() + " €");
                } else {
                    setText(null);
                }
            }
        });

        listaPlatos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtNombrePlato.setText(newVal.getNombre());
                txtDescripcion.setText(newVal.getDescripcion() != null ? newVal.getDescripcion() : "");
                txtPrecio.setText(String.valueOf(newVal.getPrecio()));
            }
        });

        listaPlatos.setItems(platosObservables);
        cargarPlatos();
    }

    private void cargarPlatos() {
        try {
            List<PlatoDTO> lista = platoDAO.listarPlatos();
            platosObservables.setAll(lista);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo cargar el menú: " + e.getMessage());
        }
    }

    // --- ACCIÓN: CREAR NUEVO (Botón Verde) ---
    @FXML
    void handleCrearProducto(ActionEvent event) {
        // Llama directamente a la lógica de crear, ignorando la selección de la tabla
        crearNuevoPlato();
    }

    // --- ACCIÓN: EDITAR (Botón Azul) ---
    @FXML
    void handleGuardarCambios(ActionEvent event) {
        PlatoDTO seleccionado = listaPlatos.getSelectionModel().getSelectedItem();
        
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Para crear uno nuevo usa el botón verde 'Crear Nuevo'.\nPara editar, selecciona un plato de la lista.");
            return;
        }

        try {
            seleccionado.setNombre(txtNombrePlato.getText());
            seleccionado.setDescripcion(txtDescripcion.getText());
            seleccionado.setPrecio(Double.parseDouble(txtPrecio.getText()));
            
            if (platoDAO.modificarPlato(seleccionado)) {
                mostrarAlerta("Éxito", "Plato modificado correctamente.");
                listaPlatos.refresh();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El precio debe ser un número válido.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar: " + e.getMessage());
        }
    }
    
    // --- ACCIÓN: ELIMINAR (Botón Rojo) ---
    @FXML
    void handleEliminarProducto(ActionEvent event) {
        PlatoDTO seleccionado = listaPlatos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un plato para eliminarlo.");
            return;
        }

        try {
            if (platoDAO.eliminarPlato(seleccionado.getIdPlato())) {
                mostrarAlerta("Eliminado", "Plato borrado.");
                cargarPlatos();
                limpiarFormulario();
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo borrar: " + e.getMessage());
        }
    }

    // --- Lógica Interna de Creación ---
    private void crearNuevoPlato() {
        if (txtNombrePlato.getText().isEmpty() || txtPrecio.getText().isEmpty()) {
            mostrarAlerta("Faltan datos", "El nombre y el precio son obligatorios.");
            return;
        }

        try {
            String nombre = txtNombrePlato.getText();
            String desc = txtDescripcion.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            
            PlatoDTO nuevo = new PlatoDTO(0);
            nuevo.setNombre(nombre);
            nuevo.setDescripcion(desc);
            nuevo.setPrecio(precio);
            nuevo.setDisponible(true);
            
            platoDAO.crearPlato(nuevo);
            mostrarAlerta("Éxito", "Nuevo plato creado.");
            limpiarFormulario();
            cargarPlatos();
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El precio debe ser un número (usa punto para decimales).");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo crear: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtNombrePlato.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        listaPlatos.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}