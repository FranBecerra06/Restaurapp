package Controlador;

import DAO.PlatoDAO;
import DTO.CategoriaDTO;
import DTO.PlatoDTO;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteAlergenosControlador {

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
    private Button botonGluten; // Ya estaba
    @FXML
    private Button botonLactosa;    // NUEVO
    @FXML
    private Button botonHuevo;  // NUEVO
    @FXML
    private Button botonFrutosSecos; // NUEVO
    @FXML
    private Button botonPescado;    // NUEVO
    @FXML
    private Button botonMarisco;
    @FXML
    private Button closeButton;
    @FXML
    private VBox contenedorCards;
    @FXML
    private ScrollPane scrollPane;

    private List<Button> listaBotones = new ArrayList<>();
    private List<Button> listaFiltros = new ArrayList<>();

    private Button botonFiltroActivo = null;

    PlatoDAO platoDAO = new PlatoDAO();

    @FXML
    public void initialize(){
        scrollPane.setStyle("""
                -fx-focus-color: transparent;
                -fx-faint-focus-color: transparent;
                -fx-border-width: 0;
                -fx-border-insets: 0;
                -fx-background-insets: 0;
                -fx-background-color: transparent;
                """);
        listaBotones.add(pedirButton);
        listaBotones.add(menuButton);
        listaBotones.add(alergenosButton);
        listaBotones.add(reservarButton);
        listaBotones.add(sNosototrosButton);
        listaBotones.add(perfilButton);
        listaBotones.add(closeButton);

        listaFiltros.add(botonGluten);
        listaFiltros.add(botonLactosa);
        listaFiltros.add(botonHuevo);
        listaFiltros.add(botonFrutosSecos);
        listaFiltros.add(botonPescado);
        listaFiltros.add(botonMarisco);

        for(Button b : listaBotones){
            aplicarAnimacionBoton(b);
        }
    }

    public List<HBox> crearEstructura(List<PlatoDTO> platos){
        List<HBox> filas = new ArrayList<>();
        int numFilas = (int) Math.ceil((double) platos.size() / 5);

        for(int i = 0; i < numFilas; i++){
            HBox fila = new HBox();
            fila.setSpacing(15);
            fila.setAlignment(Pos.CENTER);
            filas.add(fila);
        }

        return filas;
    }

    public void mostrarFiltro(ActionEvent event){
        Button boton = (Button) event.getSource();
        String estiloNormal = """
            -fx-background-radius: 10px;
            -fx-background-color: #e7e9f3;
            -fx-text-fill: black;
            -fx-font-weight: bold;
            """;

        String estiloActivo = """
            -fx-background-radius: 20px;
            -fx-background-color: #ce6a2e;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            """;

        if (botonFiltroActivo != null) {
            botonFiltroActivo.setStyle(estiloNormal);
        }

        boton.setStyle(estiloActivo);
        botonFiltroActivo = boton;

        String filtro = boton.getText().toLowerCase();
        filtro = filtro.replace(' ','_');
        System.out.println(filtro);
        mostrarPlatos(filtro);
    }

    public void mostrarPlatos(String alergeno){
        contenedorCards.getChildren().clear();
        List<PlatoDTO> listaPlatos = null;
        try {
            listaPlatos = platoDAO.obtenerPlatosConAlergeno(alergeno);
            List<HBox> listaFilas = crearEstructura(listaPlatos);
            List<VBox> cards = crearCard(listaPlatos);
            int filaActual = 1;
            int contCards = 0;
            for(HBox hbox: listaFilas){
                contenedorCards.getChildren().add(hbox);
                int inicio = (filaActual - 1) * 5;
                for (int i = 0; i < 5 && contCards < cards.size(); i++) {
                    int indiceCard = inicio + i;
                    if (indiceCard < cards.size()) {
                        VBox cardToDraw = cards.get(indiceCard);
                        hbox.getChildren().add(cardToDraw);
                        contCards++;
                    }
                }
                filaActual++;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<VBox> crearCard(List<PlatoDTO> platos){
        List<VBox> cards = new ArrayList<>();
        for(PlatoDTO p: platos){
            VBox card = new VBox();
            card.setId(p.getNombre());
            card.setStyle(
                    "-fx-background-color: white;\n" +
                            "    -fx-background-radius: 10;\n" +
                            "    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 5);\n" +
                            "    -fx-alignment: center;\n" +
                            "    -fx-max-width: 230px;\n" +
                            "    -fx-pref-width: 266px;\n" +
                            "    -fx-pref-height: 290px;\n" +
                            "    -fx-fill-width: false;\n" +
                            "    -fx-padding: 10px 0px 10px 0px;"
            );
            card.setAlignment(Pos.TOP_CENTER);
            StackPane sp = new StackPane();
            sp.setStyle(
                    "-fx-background-radius: 10;\n" +
                            "    -fx-border-radius: 10;\n" +
                            "    -fx-max-width: 150px;\n" +
                            "    -fx-min-width: 200px;\n" +
                            "    -fx-pref-width: 200px;\n" +
                            "    -fx-pref-height: 150px;"
            );
            card.getChildren().add(sp);


            ImageView iv = new ImageView();
            if(p.getImgUrl()!=null){
                Image im = new Image(getClass().getResourceAsStream(p.getImgUrl()));
                iv = new ImageView(im);


            }else{
                Image im = new Image(getClass().getResourceAsStream("/Imagenes/plato_alpha-removebg-preview.png"));
                iv = new ImageView(im);
            }
            iv.setFitHeight(180);
            iv.setPreserveRatio(true);

            Rectangle clip = new Rectangle(200, 180);
            clip.setArcWidth(20);
            clip.setArcHeight(20);

            iv.setClip(clip);
            sp.getChildren().add(iv);
            HBox hb = new HBox();
            hb.setStyle(
                    "    -fx-padding: 10px 0px 10px 0px;"
            );
            hb.setPrefWidth(200);
            hb.setAlignment(Pos.CENTER_LEFT);
            Label nombre = new Label(p.getNombre());
            nombre.setStyle("""
                    -fx-font-weight: bold;
                    -fx-font-size: 18px;                      
            """);
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            Label precio = new Label(String.valueOf(p.getPrecio())+"€");
            precio.setStyle("""
                    -fx-font-weight: bold;
                    -fx-text-fill: #ce6a2e;
                    -fx-font-size: 18px;                      
            """);
            hb.getChildren().addAll(nombre,region,precio);
            card.getChildren().add(hb);
            HBox hb2 = new HBox();
            hb2.setStyle(
                    "    -fx-padding: 10px 0px 10px 0px;"
            );
            hb2.setPrefWidth(200);
            hb2.setAlignment(Pos.CENTER_LEFT);
            Label desc = new Label(p.getDescripcion());
            hb2.getChildren().add(desc);
            card.getChildren().add(hb2);
            cards.add(card);

        }

        return cards;
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

    @FXML
    public void goToLanding(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/Landing.fxml"));
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
}
