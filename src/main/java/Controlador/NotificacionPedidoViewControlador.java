package Controlador;

import java.util.Map;

import DTO.PlatoDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class NotificacionPedidoViewControlador {
	
	@FXML
	private VBox pedidos;
	
	
	public void notificacionPedido(Map<PlatoDTO, Integer> mapaPedidos, int numeroMesa) {
        pedidos.getChildren().clear(); // Limpiamos por si hay algo previo

        Label mesaLabel = new Label("Mesa: " + numeroMesa);
        mesaLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        pedidos.getChildren().add(mesaLabel);

        for (Map.Entry<PlatoDTO, Integer> entry : mapaPedidos.entrySet()) {
            Label platoLabel = new Label(entry.getKey().getNombre() + " x " + entry.getValue());
            pedidos.getChildren().add(platoLabel);
        }
    }
	
}