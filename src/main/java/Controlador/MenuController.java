package Controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList; // Importante
import java.util.List;      // Importante
import java.util.ResourceBundle;

import DAO.CategoriaDAO;
import DAO.PlatoDAO;
import DTO.CategoriaDTO;
import DTO.PlatoDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox; // Importante
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MenuController implements Initializable {

    @FXML private ListView<PlatoDTO> listaPlatos;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    
    // --- NUEVOS CHECKBOXES (Deben coincidir con el fx:id en SceneBuilder) ---
    @FXML private CheckBox chkGluten;
    @FXML private CheckBox chkLactosa;
    @FXML private CheckBox chkHuevo;
    @FXML private CheckBox chkFrutosSecos;
    @FXML private CheckBox chkPescado;
    @FXML private CheckBox chkMarisco;
    
    @FXML private ComboBox<CategoriaDTO> comboCategoria;

    private PlatoDAO platoDAO;
    private CategoriaDAO categoriaDAO;

    private ObservableList<PlatoDTO> platosObservables;
    private ObservableList<CategoriaDTO> categoriasObservables;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        platoDAO = new PlatoDAO();
        categoriaDAO = new CategoriaDAO();

        platosObservables = FXCollections.observableArrayList();
        categoriasObservables = FXCollections.observableArrayList();

        // ---- CONFIGURACIÓN DE LISTA DE PLATOS ----
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

        // ---- AL SELECCIONAR UN PLATO ----
        listaPlatos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtNombrePlato.setText(newVal.getNombre());
                txtDescripcion.setText(newVal.getDescripcion() != null ? newVal.getDescripcion() : "");
                txtPrecio.setText(String.valueOf(newVal.getPrecio()));
                
                seleccionarCategoriaEnCombo(newVal.getIdCategoria());
                
                // NOTA: Para que aquí se marquen los checks al seleccionar, 
                // el método 'listarPlatos' del DAO tendría que hacer un JOIN con la tabla de alérgenos.
                // Por ahora, nos centramos en que GUARDEN bien al crear.
            }
        });

        listaPlatos.setItems(platosObservables);

        cargarCategorias();
        cargarPlatos();
    }
    
    private void cargarCategorias() {
        try {
            List<CategoriaDTO> lista = categoriaDAO.listarCategorias();
            categoriasObservables.setAll(lista);

            comboCategoria.setItems(categoriasObservables);

            // Mostrar solo el nombre en el desplegable
            comboCategoria.setCellFactory(param -> new ListCell<CategoriaDTO>() {
                @Override
                protected void updateItem(CategoriaDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNombre());
                }
            });

            comboCategoria.setButtonCell(new ListCell<CategoriaDTO>() {
                @Override
                protected void updateItem(CategoriaDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNombre());
                }
            });

        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar las categorías: " + e.getMessage());
        }
    }
    
    private void seleccionarCategoriaEnCombo(int idCategoria) {
        for (CategoriaDTO c : categoriasObservables) {
            if (c.getIdCategoria() == idCategoria) {
                comboCategoria.getSelectionModel().select(c);
                return;
            }
        }
        comboCategoria.getSelectionModel().clearSelection();
    }
    
    private void cargarPlatos() {
        try {
            List<PlatoDTO> lista = platoDAO.listarPlatos();
            platosObservables.setAll(lista);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo cargar el menú: " + e.getMessage());
        }
    }
    
    
    @FXML
    void handleCrearProducto(ActionEvent event) {
        crearNuevoPlato();
    }

    private void crearNuevoPlato() {
        if (txtNombrePlato.getText().isEmpty() || txtPrecio.getText().isEmpty() || comboCategoria.getValue() == null) {
            mostrarAlerta("Faltan datos", "El nombre, el precio y la categoria son obligatorios.");
            return;
        }

        try {
            // 1. Crear la lista de Alérgenos (Booleanos)
            // EL ORDEN ES CRÍTICO: 0=Gluten, 1=Lactosa, 2=Huevo, 3=FrutosSecos, 4=Pescado, 5=Marisco
            List<Boolean> listaAlergenos = new ArrayList<>();
            listaAlergenos.add(chkGluten.isSelected());      // ID 1
            listaAlergenos.add(chkLactosa.isSelected());     // ID 2
            listaAlergenos.add(chkHuevo.isSelected());       // ID 3
            listaAlergenos.add(chkFrutosSecos.isSelected()); // ID 4
            listaAlergenos.add(chkPescado.isSelected());     // ID 5
            listaAlergenos.add(chkMarisco.isSelected());     // ID 6

            // 2. Crear el objeto PlatoDTO usando el constructor completo
            PlatoDTO nuevo = new PlatoDTO(
                comboCategoria.getValue().getIdCategoria(), // ID Categoría
                txtNombrePlato.getText(),                   // Nombre
                txtDescripcion.getText(),                   // Descripción
                Double.parseDouble(txtPrecio.getText()),    // Precio
                listaAlergenos                              // Lista de alérgenos
            );
            
            // Validar si existe
            boolean existe = false;
            for (PlatoDTO p : listaPlatos.getItems()) {
                if (p.getNombre().equalsIgnoreCase(nuevo.getNombre())) {
                    existe = true;
                    break;
                }
            }
            
            if (existe) {
                mostrarAlerta("Advertencia", "El plato \"" + nuevo.getNombre() + "\" ya existe");
                return;
            }

            // 3. Llamar al DAO (Esto guardará en 'plato' y en 'plato_alergeno')
            platoDAO.crearPlato(nuevo);

            mostrarAlerta("Éxito", "Nuevo plato creado con alérgenos.");
            limpiarFormulario();
            cargarPlatos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El precio debe ser un número válido.");
        } catch (Exception e) {
            e.printStackTrace(); // Ver error en consola
            mostrarAlerta("Error", "No se pudo crear: " + e.getMessage());
        }
    }
    
    
    @FXML
    void handleGuardarCambios(ActionEvent event) {
        PlatoDTO seleccionado = listaPlatos.getSelectionModel().getSelectedItem();
        
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un plato de la lista.");
            return;
        }

        try {
            seleccionado.setNombre(txtNombrePlato.getText());
            seleccionado.setDescripcion(txtDescripcion.getText());
            seleccionado.setPrecio(Double.parseDouble(txtPrecio.getText()));

            if (comboCategoria.getValue() != null) {
                seleccionado.setIdCategoria(comboCategoria.getValue().getIdCategoria());
            }
            
            // NOTA: Actualmente el DAO 'modificarPlato' solo actualiza datos básicos, 
            // no actualiza los alérgenos si los cambias aquí.
            
            if (platoDAO.modificarPlato(seleccionado)) {
                mostrarAlerta("Éxito", "Plato modificado correctamente.");
                listaPlatos.refresh();
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar: " + e.getMessage());
        }
    }
    
    
    @FXML
    void handleEliminarProducto(ActionEvent event) {
        PlatoDTO seleccionado = listaPlatos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un plato para eliminarlo.");
            return;
        }

        try {
            if (platoDAO.eliminarPlato(seleccionado.getIdPlato())) {
                mostrarAlerta("Eliminado", "Plato eliminado.");
                cargarPlatos();
                limpiarFormulario();
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo borrar: " + e.getMessage());
        }
    }
    
    
    private void limpiarFormulario() {
        txtNombrePlato.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        comboCategoria.getSelectionModel().clearSelection();
        listaPlatos.getSelectionModel().clearSelection();
        
        // Limpiar checkboxes
        chkGluten.setSelected(false);
        chkLactosa.setSelected(false);
        chkHuevo.setSelected(false);
        chkFrutosSecos.setSelected(false);
        chkPescado.setSelected(false);
        chkMarisco.setSelected(false);
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}