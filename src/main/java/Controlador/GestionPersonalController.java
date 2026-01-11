package Controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import DAO.UsuarioDAO;
import DTO.UsuarioDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
// import javafx.scene.control.ComboBox; // Ya no lo necesitamos
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GestionPersonalController implements Initializable {

    // Campos del formulario
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtUsuario; // nombre_usuario
    @FXML private PasswordField txtContrasena;
    
    // ELIMINADO: @FXML private ComboBox<String> comboRol; -> Ya no existe en la vista

    // Tabla
    @FXML private TableView<String> tablaUsuarios;
    @FXML private TableColumn<String, String> colNombreUsuario;

    private UsuarioDAO usuarioDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuarioDAO = new UsuarioDAO();

        // Configurar Columna (Muestra el String directamente)
        colNombreUsuario.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

        // ELIMINADO: comboRol.getItems().addAll(...) -> Esto causaba el NullPointerException

        // Cargar datos al iniciar
        cargarDatos();
        
        // Listener para cuando seleccionas una fila
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtUsuario.setText(newSelection);
            }
        });
    }

    private void cargarDatos() {
        try {
            List<String> listaNombres = usuarioDAO.listarUsuarioClientes();
            ObservableList<String> datos = FXCollections.observableArrayList(listaNombres);
            tablaUsuarios.setItems(datos);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo cargar la lista: " + e.getMessage());
        }
    }

    // --- BOTÓN AÑADIR ---
    @FXML
    void handleAnadir(ActionEvent event) {
        if (camposVacios()) return;

        // Crear DTO con los datos del formulario
        UsuarioDTO nuevoUser = new UsuarioDTO(
            txtNombre.getText(),
            txtApellidos.getText(),
            txtEmail.getText(),
            txtContrasena.getText(),
            txtTelefono.getText(),
            null, // <--- CAMBIO: Pasamos null porque ya no hay Rol
            txtUsuario.getText()
        );

        try {
            usuarioDAO.crearCliente(nuevoUser);
            mostrarAlerta("Éxito", "Cliente añadido correctamente.");
            limpiarFormulario();
            cargarDatos();
        } catch (SQLException e) {
            mostrarAlerta("Error SQL", "No se pudo añadir: " + e.getMessage());
        }
    }

    // --- BOTÓN EDITAR ---
    @FXML
    void handleEditar(ActionEvent event) {
        String usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un usuario de la tabla para editarlo.");
            return;
        }
        
        if (camposVacios()) return;
        
        UsuarioDTO userEditado = new UsuarioDTO(
            txtNombre.getText(),
            txtApellidos.getText(),
            txtEmail.getText(),
            txtContrasena.getText(),
            txtTelefono.getText(),
            null, // <--- CAMBIO: Pasamos null porque ya no hay Rol
            txtUsuario.getText()
        );

        try {
            boolean exito = usuarioDAO.modificarUsuario(userEditado);
            if (exito) {
                mostrarAlerta("Éxito", "Cliente modificado correctamente.");
                limpiarFormulario();
                cargarDatos();
            } else {
                mostrarAlerta("Error", "No se encontró el usuario o no se pudo modificar.");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error SQL", "Fallo al editar: " + e.getMessage());
        }
    }

    // --- BOTÓN ELIMINAR ---
    @FXML
    void handleEliminar(ActionEvent event) {
        String usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un usuario de la tabla.");
            return;
        }

        try {
            boolean exito = usuarioDAO.eliminarUsuario(usuarioSeleccionado);
            if (exito) {
                mostrarAlerta("Éxito", "Usuario eliminado.");
                limpiarFormulario();
                cargarDatos();
            } else {
                mostrarAlerta("Error", "No se pudo eliminar.");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error SQL", "Fallo al eliminar: " + e.getMessage());
        }
    }

    private boolean camposVacios() {
        if (txtUsuario.getText().isEmpty() || txtContrasena.getText().isEmpty()) {
            mostrarAlerta("Datos incompletos", "El usuario y contraseña son obligatorios.");
            return true;
        }
        return false;
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtApellidos.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtUsuario.clear();
        txtContrasena.clear();
        // ELIMINADO: comboRol.getSelectionModel().clearSelection(); -> Ya no existe
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}