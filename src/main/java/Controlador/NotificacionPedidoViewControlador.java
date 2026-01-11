package Controlador;

import java.util.Map;

import DTO.PlatoDTO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class NotificacionPedidoViewControlador {
	
	@FXML
	private VBox pedidos;
	
	
	
	public void notificacionPedido(Map<PlatoDTO, Integer> mapaPedidos, int numeroMesa) {

	    pedidos.getChildren().clear();
	    pedidos.setPadding(new Insets(20, 20, 20, 10));
	    pedidos.setSpacing(10);
	    
	    HBox tarjetaMesa = new HBox();
	    tarjetaMesa.setAlignment(Pos.CENTER_LEFT);
	    tarjetaMesa.setPadding(new Insets(12, 14, 12, 14));
	    tarjetaMesa.setMaxWidth(400);
	    tarjetaMesa.setStyle("""
	        -fx-background-color: #F1F5F9;
	        -fx-background-radius: 12;
	        -fx-border-color: #CBD5E1;
	        -fx-border-radius: 12;
	    """);
	    
	    HBox.setMargin(tarjetaMesa, new Insets(10, 0, 0, 20));
	    
	    
	    VBox contenidoMesa = new VBox(8);
	    contenidoMesa.setAlignment(Pos.CENTER_LEFT);
	    
	    
	    Label mesaLabel = new Label("Mesa " + numeroMesa);
	    mesaLabel.setStyle("""
	        -fx-font-weight: bold;
	        -fx-font-size: 18px;
	        -fx-text-fill: FF9500;
	    """);
	    contenidoMesa.getChildren().add(mesaLabel);
	    
	    for (Map.Entry<PlatoDTO, Integer> entry : mapaPedidos.entrySet()) {
	        PlatoDTO plato = entry.getKey();
	        Integer cantidad = entry.getValue();
	        
	        HBox lineaPlato = new HBox(12);
	        lineaPlato.setAlignment(Pos.CENTER_LEFT);
	        
	        Label platoNombre = new Label(plato.getNombre());
	        platoNombre.setStyle("""
	            -fx-font-weight: bold;
	            -fx-font-size: 16px;
	            -fx-text-fill: #1E293B;
	        """);
	        platoNombre.setWrapText(true);
	        platoNombre.setMaxWidth(220);
	        
	        Label cantidadPlato = new Label("x" + cantidad);
	        cantidadPlato.setStyle("""
	            -fx-font-weight: bold;
	            -fx-font-size: 15px;
	            -fx-text-fill: #F97316;
	            -fx-background-color: #FEF3C7;
	            -fx-background-radius: 6;
	            -fx-padding: 4px 10px;
	        """);
	        
	        lineaPlato.getChildren().addAll(platoNombre, cantidadPlato);
	        contenidoMesa.getChildren().add(lineaPlato);
	    }
	    
	    
	    Label listoLabel = new Label("Listo");
	    listoLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #19E000;");
	    listoLabel.setVisible(false);
	    
	    
	    HBox listoWrapper = new HBox(listoLabel);
	    listoWrapper.setAlignment(Pos.BOTTOM_RIGHT);
	    listoWrapper.setMaxWidth(Double.MAX_VALUE);
	    contenidoMesa.getChildren().add(listoWrapper);
	    
	    
	    CheckBox ch = new CheckBox();
	    ch.setStyle("-fx-scale-x: 1.3; -fx-scale-y: 1.3; -fx-mark-color: #FF9500; -fx-faint-focus-color: #FF9500;");
	    
	    
	    ch.setOnAction(e -> {
	        if (ch.isSelected()) {
	            listoLabel.setVisible(true);
	            ch.setDisable(true);
	        }
	    });
	    
	    
	    HBox.setHgrow(contenidoMesa, Priority.ALWAYS);
	    tarjetaMesa.getChildren().addAll(contenidoMesa, ch);
	    pedidos.getChildren().add(tarjetaMesa);
	}
}