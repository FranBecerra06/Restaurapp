package Controlador;

import DAO.CategoriaDAO;
import DAO.PlatoDAO;
import DTO.CategoriaDTO;
import DTO.PlatoDTO;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Text;
import pack.restaurantegestion.Main;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.PublicKey;
import java.sql.SQLException;
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
    private VBox contenedorCards;
    @FXML
    private ScrollPane  scrollPane;
    @FXML
    private HBox btnsHbox;

    private Button botonCategoriaActivo = null;

    @FXML
    private ImageView iv;

    private List<Button> listaBotones = new ArrayList<>();
    private List<Button> listaBotonesCategoria = new ArrayList<>();
    private List<CategoriaDTO> listaCategoria;

    private PlatoDAO platoDAO = new PlatoDAO();


    @FXML
    public void initialize(){
        scrollPane.setFocusTraversable(false);
        //redondearImagen(iv);
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

        listaCategoria=mostrarBotones();

    }

    public void mostrarPlatos(String categoria){
        contenedorCards.getChildren().clear();
        List<PlatoDTO> listaPlatos = null;
        int id=0;
        for(CategoriaDTO c: listaCategoria){
            if(c.getNombre().equals(categoria)){
                id=c.getIdCategoria();
            }
        }
        try {
            listaPlatos = platoDAO.obtenerPlatosPorCategoria(id);
            for(PlatoDTO p: listaPlatos){
                System.out.println("Tenemos "+p.getNombre());
            }
            List<HBox> listaFilas = crearEstructura(listaPlatos);
            List<VBox> cards = crearCard(listaPlatos);
            System.out.println("Mostrar platos ocurre");
            int filaActual = 1;
            int contCards = 0;
            for(HBox hbox: listaFilas){
                contenedorCards.getChildren().add(hbox);
                System.out.println("Se ha añadido un hbox");
                for (int i = 0;i<3 && contCards< cards.size();i++){
                    VBox cardToDraw = cards.get(i+filaActual-1);
                    hbox.getChildren().add(cardToDraw);
                    System.out.println("Se ha añadido "+(i+filaActual)+" elemento");
                    contCards++;
                }
                filaActual+=3;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<HBox> crearEstructura(List<PlatoDTO> platos){
        List<HBox> filas = new ArrayList<>();
        int numFilas = (int) Math.ceil((double) platos.size()/3);
        for(int i = 0;i<numFilas;i++){
            HBox fila = new HBox();
            fila.setSpacing(25);
            filas.add(fila);
            System.out.println("Fila añadida");
        }

        return filas;
    }

    public List<VBox> crearCard(List<PlatoDTO> platos){
        List<VBox> cards = new ArrayList<>();
        for(PlatoDTO p: platos){
            VBox card = new VBox();
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
            Button cardBtn = new Button("+");
            cardBtn.setStyle("""
                        -fx-background-color: white;
                        -fx-font-size: 15px;
                        -fx-font-weight: bold;
                        -fx-pref-width: 35px;
                        -fx-pref-height: 35px;
                        -fx-background-radius: 50%;
                    """);
            cardBtn.setOnAction(this::pedirPlato);
            sp.getChildren().add(cardBtn);
            StackPane.setAlignment(cardBtn, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(cardBtn, new Insets(10));
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
                    -fx-text-fill: #1132d4;
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

    public List<CategoriaDTO> mostrarBotones(){
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        List<CategoriaDTO> listaCategorias = new ArrayList<>();
        try {
            listaCategorias = categoriaDAO.listarCategorias();
            crearBotones(btnsHbox, listaCategorias);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaCategorias;
    }

    public void crearBotones(HBox hbox, List<CategoriaDTO> listaCategorias){
        hbox.setSpacing(15);
        for (CategoriaDTO c: listaCategorias){
            Button btn = new Button(c.getNombre());
            btn.setStyle("""
                -fx-background-radius: 10px;
                -fx-background-color: #e7e9f3;
                -fx-text-fill: black;  
                -fx-font-weight: bold;          
            """);
            btn.setOnAction(this::mostrarCategoria);
            listaBotonesCategoria.add(btn);
            hbox.getChildren().add(btn);
        }
    }

    public void mostrarCategoria(ActionEvent event){
        Button boton = (Button) event.getSource();
        if (botonCategoriaActivo != null) {
            botonCategoriaActivo.setStyle(
                    "-fx-background-color: #e7e9f3;" +
                            "-fx-text-fill: black;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 10px;"
            );
        }
        boton.setStyle(
                "-fx-background-color: #1132d4;"+
                "-fx-text-fill: white;"+
                "-fx-font-weight: bold"+
                "-fx-background-radius: 20px;"
        );
        botonCategoriaActivo = boton;
        String categoria = boton.getText().toString();
        mostrarPlatos(categoria);
    }

    public void pedirPlato(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pedido");
        alert.setContentText("Se ha pedido este plato");
        Optional<ButtonType> result = alert.showAndWait();
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
