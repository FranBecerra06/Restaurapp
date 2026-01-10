package Controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;

public class MenuController implements Initializable {

    @FXML private ListView<PlatoDTO> listaPlatos;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    
    @FXML private ComboBox<CategoriaDTO> comboCategoria;

    @FXML private CheckBox chkGluten;
    @FXML private CheckBox chkLactosa;
    @FXML private CheckBox chkHuevo;
    @FXML private CheckBox chkFrutosSecos;
    @FXML private CheckBox chkPescado;
    @FXML private CheckBox chkMarisco;

    private PlatoDAO platoDAO;
    private CategoriaDAO categoriaDAO;

    private static final String ALERGENO_GLUTEN = "gluten";
    private static final String ALERGENO_LACTOSA = "lactosa";
    private static final String ALERGENO_HUEVO = "huevo";
    private static final String ALERGENO_FRUTOS_SECOS = "frutos_secos";
    private static final String ALERGENO_PESCADO = "pescado";
    private static final String ALERGENO_MARISCO = "marisco";

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
                    String texto = item.getNombre() + " - " + item.getPrecio() + " €";
                    // Mostrar alérgenos si los tiene
                    if (item.getAlergenos() != null && !item.getAlergenos().isEmpty()) {
                        texto += " [" + String.join(", ", item.getAlergenos()) + "]";
                    }
                    setText(texto);
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

                // Cargar alérgenos del plato seleccionado en los CheckBox
                cargarAlergenosEnCheckBox(newVal);
            } else {
                // Limpiar CheckBox si no hay plato seleccionado
                limpiarCheckBoxAlergenos();
            }
        });

        listaPlatos.setItems(platosObservables);

        cargarCategorias();
        cargarPlatos();
    }
    // Método para cargar los alérgenos del plato en los CheckBox
    private void cargarAlergenosEnCheckBox(PlatoDTO plato) {
        // Limpiar primero todos los CheckBox
        limpiarCheckBoxAlergenos();

        if (plato.getAlergenos() != null) {
            for (String alergeno : plato.getAlergenos()) {
                switch (alergeno.toLowerCase()) {
                    case ALERGENO_GLUTEN:
                        chkGluten.setSelected(true);
                        break;
                    case ALERGENO_LACTOSA:
                        chkLactosa.setSelected(true);
                        break;
                    case ALERGENO_HUEVO:
                        chkHuevo.setSelected(true);
                        break;
                    case ALERGENO_FRUTOS_SECOS:
                        chkFrutosSecos.setSelected(true);
                        break;
                    case ALERGENO_PESCADO:
                        chkPescado.setSelected(true);
                        break;
                    case ALERGENO_MARISCO:
                        chkMarisco.setSelected(true);
                        break;
                }
            }
        }
    }

    // Método para limpiar todos los CheckBox de alérgenos
    private void limpiarCheckBoxAlergenos() {
        chkGluten.setSelected(false);
        chkLactosa.setSelected(false);
        chkHuevo.setSelected(false);
        chkFrutosSecos.setSelected(false);
        chkPescado.setSelected(false);
        chkMarisco.setSelected(false);
    }

    private List<String> obtenerAlergenosSeleccionados() {
        List<String> alergenos = new ArrayList<>();

        if (chkGluten.isSelected()) {
            alergenos.add(ALERGENO_GLUTEN);
        }
        if (chkLactosa.isSelected()) {
            alergenos.add(ALERGENO_LACTOSA);
        }
        if (chkHuevo.isSelected()) {
            alergenos.add(ALERGENO_HUEVO);
        }
        if (chkFrutosSecos.isSelected()) {
            alergenos.add(ALERGENO_FRUTOS_SECOS);
        }
        if (chkPescado.isSelected()) {
            alergenos.add(ALERGENO_PESCADO);
        }
        if (chkMarisco.isSelected()) {
            alergenos.add(ALERGENO_MARISCO);
        }

        return alergenos;
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
            PlatoDTO nuevo = new PlatoDTO(0);
            nuevo.setNombre(txtNombrePlato.getText());
            nuevo.setDescripcion(txtDescripcion.getText());
            nuevo.setPrecio(Double.parseDouble(txtPrecio.getText()));
            nuevo.setIdCategoria(comboCategoria.getValue().getIdCategoria());

            // Añadir lista de alérgenos seleccionados
            List<String> alergenos = obtenerAlergenosSeleccionados();
            nuevo.setAlergenos(alergenos);

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

            platoDAO.crearPlato(nuevo);

            mostrarAlerta("Éxito", "Nuevo plato creado.");
            limpiarFormulario();
            cargarPlatos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El precio debe ser un número válido.");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo crear: " + e.getMessage());
            e.printStackTrace(); // Para depuración
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

            // ➕ Editar categoría
            if (comboCategoria.getValue() != null) {
                seleccionado.setIdCategoria(comboCategoria.getValue().getIdCategoria());
            }

            // ➕ Editar alérgenos
            List<String> alergenos = obtenerAlergenosSeleccionados();
            seleccionado.setAlergenos(alergenos);

            if (platoDAO.modificarPlato(seleccionado)) {
                mostrarAlerta("Éxito", "Plato modificado correctamente.");
                listaPlatos.refresh();
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar: " + e.getMessage());
            e.printStackTrace(); // Para depuración
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
        limpiarCheckBoxAlergenos(); // Limpiar también los CheckBox
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}