package Controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import DAO.MesaDAO;
import DAO.Mesa_PlatoDAO;
import DTO.MesaDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MesaCamareroViewControlador {
	
	@FXML
    private VBox vboxCol1, vboxCol2, vboxCol3;
	private static int mesaActual = 0;
	
	
	
	public static void resetearMesaActual() {
		mesaActual = 0;
	}
	
	
	public void initialize() throws SQLException {
        generarMesas();
    }
	
	
	public void generarMesas() throws SQLException {
		
		MesaDAO mDAO = new MesaDAO();
		
		List<MesaDTO> mesas = mDAO.listarMesas();
		int contador = 1;
		
		 for (MesaDTO mesa : mesas) {

		    int idMesa = mesa.getIdMesa();

            Image imgMesa = new Image(getClass().getResourceAsStream("/Imagenes/ImagenMesas.png"));

            ImageView imagen = new ImageView(imgMesa);
            imagen.setFitWidth(80);
            imagen.setFitHeight(80);

            Button btnMesa = new Button("Mesa " + idMesa, imagen);
            
            if(mesa.getDisponibilidad().equalsIgnoreCase("No Disponible")) {
            	btnMesa.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
            	btnMesa.setStyle("-fx-background-radius: 15;" + "-fx-font-family: 'Serif';" + "-fx-background-color: grey;");
                btnMesa.setPrefWidth(120);
                btnMesa.setPrefHeight(120);
            }else {
            	btnMesa.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
                btnMesa.setPrefWidth(120);
                btnMesa.setPrefHeight(120);
                
                Mesa_PlatoDAO mpDAO = new Mesa_PlatoDAO();
                
                boolean tieneProductos = mpDAO.obtenerPlatoPorMesa(idMesa).size() > 0;
                
                String estiloBase = "-fx-background-radius: 15; -fx-font-family: 'Serif';";
                
                if(tieneProductos) {
                	btnMesa.setStyle(estiloBase + "-fx-background-color: #E57373;");  //color rojo
                }else {
                	btnMesa.setStyle(estiloBase + "-fx-background-color: #81C784;");  //color verde
                }
                
                btnMesa.setOnAction(e -> {
    				try {
    					seleccionarMesa(idMesa);
    				} catch (Exception e1) {
    					e1.printStackTrace();
    				}
    			});
            }
            
            if (contador == 1) {
                vboxCol1.getChildren().add(btnMesa);
            } else if (contador == 2) {
                vboxCol2.getChildren().add(btnMesa);
            } else {
                vboxCol3.getChildren().add(btnMesa);
                contador = 0;
            }

            contador++;
            
		}
		
	}
	
	
	public void seleccionarMesa(int numMesa) throws IOException, SQLException{
		
		if(numMesa == mesaActual) {
			return;
		}
		
		mesaActual = numMesa;
		
		 FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/PedidoMesaView.fxml"));
	     Parent root = loader.load();

	     // Obtener el controlador de PedidoMesa
	     PedidoMesaViewControlador pmvc = loader.getController();
	     pmvc.setMesa(numMesa);  // Enviar el número de mesa
	     
	     Stage stage = (Stage) vboxCol1.getScene().getWindow();
	     Scene scene = new Scene(root);
	     stage.setScene(scene);
	     stage.show();
    }
	
}