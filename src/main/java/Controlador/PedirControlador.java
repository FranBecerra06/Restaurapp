package Controlador;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import pack.restaurantegestion.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedirControlador {
    @FXML
    private Button pedirButton; // Ya estaba
    @FXML
    private Button menuButton;    // NUEVO
    @FXML
    private Button alergenosButton;  // NUEVO
    @FXML
    private Button reservarButton; // NUEVO
    @FXML
    private Button sNosototrosButton;    // NUEVO
    @FXML
    private Button perfilButton;
    @FXML
    private Button closeButton;

    @FXML
    private ImageView iv;

    private List<Button> listaBotones = new ArrayList<>();

    @FXML
    public void initialize(){
        redondearImagen(iv);
        listaBotones.add(pedirButton);
        listaBotones.add(menuButton);
        listaBotones.add(alergenosButton);
        listaBotones.add(reservarButton);
        listaBotones.add(sNosototrosButton);
        listaBotones.add(perfilButton);
        listaBotones.add(closeButton);

        for(Button b : listaBotones){
            aplicarAnimacionBoton(b);
        }
    }

    public void pedirPlato(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pedido");
        alert.setContentText("Se ha pedido este plato");
        Optional<ButtonType> result = alert.showAndWait();
    }

    public void redondearImagen(ImageView iv){
        Rectangle clip = new Rectangle(
                iv.getFitWidth(), iv.getFitWidth()
        );
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        iv.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = iv.snapshot(parameters, null);

        iv.setClip(null);

        iv.setImage(image);
    }

    private void aplicarAnimacionBoton(Button button) {

        Pane underline = new Pane();
        underline.setStyle("-fx-background-color: #ce6a2e;");
        underline.setPrefHeight(2);
        underline.setPrefWidth(0);

        button.setGraphic(underline);
        button.setContentDisplay(ContentDisplay.BOTTOM);

        Interpolator spline = Interpolator.SPLINE(0.25, 0.8, 0.25, 1);
        Duration dur = Duration.millis(400);

        button.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                button.applyCss();
                button.layout();
                underline.setTranslateY(button.getHeight() - 2);
                underline.setTranslateX(button.getWidth() / 4.0);
            }
        });

        button.setOnMouseEntered(e -> {
            double w = button.getWidth();

            new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(button.textFillProperty(), Color.web("#432323"), spline)
                    ),
                    new KeyFrame(dur,
                            new KeyValue(button.textFillProperty(), Color.BLACK, spline)
                    )
            ).play();

            new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(underline.prefWidthProperty(), 0, spline),
                            new KeyValue(underline.translateXProperty(), w / 2.0, spline)
                    ),
                    new KeyFrame(dur,
                            new KeyValue(underline.prefWidthProperty(), w, spline),
                            new KeyValue(underline.translateXProperty(), 0, spline)
                    )
            ).play();
        });

        button.setOnMouseExited(e -> {
            double w = button.getWidth();

            new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(button.textFillProperty(), Color.BLACK, spline)
                    ),
                    new KeyFrame(dur,
                            new KeyValue(button.textFillProperty(), Color.web("#3d3d3d"), spline)
                    )
            ).play();

            new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(underline.prefWidthProperty(), w, spline),
                            new KeyValue(underline.translateXProperty(), 0, spline)
                    ),
                    new KeyFrame(dur,
                            new KeyValue(underline.prefWidthProperty(), 0, spline),
                            new KeyValue(underline.translateXProperty(), w / 2.0, spline)
                    )
            ).play();
        });
    }
}
