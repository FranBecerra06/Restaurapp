package Controlador;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LandingControlador {
    // Declarar TODOS los botones que quieres animar aquí
    @FXML
    private Button pedirButton; 
    @FXML
    private Button menuButton;    
    @FXML
    private Button alergenosButton;  
    @FXML
    private Button reservarButton; 
    @FXML
    private Button sNosototrosButton;    
    @FXML
    private Button perfilButton;
    @FXML
    private Button closeButton;
    @FXML
    private ImageView imgPrincipal;


    private List<Button> listaBotones = new ArrayList<>();

    public void initialize() {
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

        // Clip de imagen si la imagen existe
        if (imgPrincipal != null) {
            imgPrincipal.layoutBoundsProperty().addListener((obs, oldV, newV) -> {
                clipImgPrincipal();
            });
        }
    }

    private void clipImgPrincipal() {
        double w = imgPrincipal.getBoundsInParent().getWidth();
        double h = imgPrincipal.getBoundsInParent().getHeight();

        Rectangle clip = new Rectangle(w, h);
        clip.setArcWidth(60);   // borde redondeado
        clip.setArcHeight(60);

        imgPrincipal.setClip(clip);
    }

    @FXML
    public void goToPedir(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/Pedir.fxml"));
            Parent root = loader.load();
            Stage currentScene = (Stage) pedirButton.getScene().getWindow();
            Scene scene = new Scene(root);
            currentScene.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void cerrarSesion(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/Main.fxml"));
            Parent root = loader.load();
            Stage currentScene = (Stage) pedirButton.getScene().getWindow();
            Scene scene = new Scene(root);
            currentScene.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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