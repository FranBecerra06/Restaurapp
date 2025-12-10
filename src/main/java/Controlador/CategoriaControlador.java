package Controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import DAO.CategoriaDAO;
import DTO.CategoriaDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class CategoriaControlador implements Initializable {
	
	@FXML
	private Button btnCrear, btnEditar, btnEliminar;
	
    @FXML
    private ListView<CategoriaDTO> listaCategorias;

    @FXML
    private TextField txtNombreCategoria;

    private CategoriaDAO categoriaDAO;
    private ObservableList<CategoriaDTO> categorias;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoriaDAO = new CategoriaDAO();
        categorias = FXCollections.observableArrayList();
        listaCategorias.setItems(categorias);

        cargarCategorias();

        // Permite mostrar solo el nombre en el ListView
        listaCategorias.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(CategoriaDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNombre());
                }
            }
        });

        // Cuando seleccionamos una categoría, llenamos el campo de texto
        listaCategorias.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtNombreCategoria.setText(newSel.getNombre());
            }
        });
    }

    // Carga las categorías desde la base de datos
    private void cargarCategorias() {
        try {
            List<CategoriaDTO> lista = categoriaDAO.listarCategorias();
            categorias.setAll(lista);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar las categorías", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void crearCategoria() {
        String nombre = txtNombreCategoria.getText().trim();
        if (nombre.isEmpty()) {
            mostrarAlerta("Advertencia", "El nombre de la categoría no puede estar vacío", Alert.AlertType.WARNING);
            return;
        }
        
        boolean existe = false;
        
        for (CategoriaDTO c : categorias) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                existe = true;
                break;
            }
        }
        
        if (existe) {
            mostrarAlerta("Advertencia", "La categoría \"" + nombre + "\" ya existe", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            CategoriaDTO nueva = new CategoriaDTO(nombre);
            categoriaDAO.crearCategoria(nueva);
            categorias.add(nueva);
            txtNombreCategoria.clear();
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo crear la categoría", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void guardarCambios() {
        CategoriaDTO seleccionada = listaCategorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Selecciona una categoría para editar", Alert.AlertType.WARNING);
            return;
        }

        String nuevoNombre = txtNombreCategoria.getText().trim();
        if (nuevoNombre.isEmpty()) {
            mostrarAlerta("Advertencia", "El nombre de la categoría no puede estar vacío", Alert.AlertType.WARNING);
            return;
        }

        seleccionada.setNombre(nuevoNombre);

        try {
            if (categoriaDAO.modificarCategoria(seleccionada)) {
            	mostrarAlerta("Éxito", "Categoria modificado correctamente.", Alert.AlertType.INFORMATION);
                listaCategorias.refresh();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar la categoría", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo actualizar la categoría", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarCategoria() {
        CategoriaDTO seleccionada = listaCategorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Selecciona una categoría para eliminar", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(
        	    Alert.AlertType.CONFIRMATION,
        	    "¿Seguro que quieres eliminar la categoría \"" + seleccionada.getNombre() + "\"?" +
        	    "\nSe eliminarán también los platos asociados.",
        	    ButtonType.YES,
        	    ButtonType.NO
        	);
        
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            try {
                if (categoriaDAO.eliminarCategoria(seleccionada.getIdCategoria())) {
                    categorias.remove(seleccionada);
                    txtNombreCategoria.clear();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar la categoría", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudo eliminar la categoría", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    // Método auxiliar para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}