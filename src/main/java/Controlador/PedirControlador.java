package Controlador;

import DAO.CategoriaDAO;
import DAO.PlatoDAO;
import DTO.CategoriaDTO;
import DTO.PlatoDTO;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
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
import java.util.*;

public class PedirControlador {
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
    
    // Contenedores y lógica de tu rama (HEAD)
    @FXML
    private VBox contenedorCards;
    @FXML
    private ScrollPane  scrollPane;
    @FXML
    private ScrollPane  scrollPanePedido;
    @FXML
    private HBox btnsHbox;
    @FXML
    private VBox pedidosContainer;
    @FXML
    private Label totalPedidos;

    private Button botonCategoriaActivo = null;
    private List<Button> listaBotones = new ArrayList<>();
    private List<Button> listaBotonesCategoria = new ArrayList<>();
    private List<CategoriaDTO> listaCategoria;
    private Map<PlatoDTO, Integer> mapaPedidos = new HashMap<>();
    private Map<PlatoDTO, HBox> mapaCards = new HashMap<>();

    private PlatoDAO platoDAO = new PlatoDAO();

    @FXML
    public void initialize(){
        // Estilos para los ScrollPane
        scrollPane.setStyle("""
                -fx-focus-color: transparent;
                -fx-faint-focus-color: transparent;
                -fx-border-width: 0;
                -fx-border-insets: 0;
                -fx-background-insets: 0;
                -fx-background-color: transparent;
                """);
        scrollPanePedido.setStyle("""
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

        for(Button b : listaBotones){
            aplicarAnimacionBoton(b);
        }

        listaCategoria = mostrarBotones();
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
            List<HBox> listaFilas = crearEstructura(listaPlatos);
            List<VBox> cards = crearCard(listaPlatos);
            int filaActual = 1;
            int contCards = 0;
            for(HBox hbox: listaFilas){
                contenedorCards.getChildren().add(hbox);
                for (int i = 0; i<3 && contCards< cards.size(); i++){
                    VBox cardToDraw = cards.get(i+filaActual-1);
                    hbox.getChildren().add(cardToDraw);
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
        for(int i = 0; i<numFilas; i++){
            HBox fila = new HBox();
            fila.setSpacing(25);
            filas.add(fila);
        }
        return filas;
    }


    public HBox crearCardPedido(PlatoDTO plato) {
        HBox hbox = new HBox();
        hbox.setStyle("""
        -fx-background-color: white;
        -fx-background-radius: 12;
        -fx-border-radius: 12;
        -fx-border-color: #e0e0e0;
        -fx-border-width: 1;
        -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 8, 0, 0, 2);
        -fx-min-height: 100px;
        -fx-pref-height: 100px;
        -fx-min-width: 100px;
        -fx-pref-width: 100px;
        -fx-spacing: 15px;
        -fx-padding: 15px;
    """);

        VBox contentBox = new VBox();
        contentBox.setSpacing(8);
        contentBox.setPrefWidth(250);
        HBox.setHgrow(contentBox, Priority.ALWAYS);


        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.setSpacing(10);

        Label platoNombre = new Label(plato.getNombre());
        platoNombre.setStyle("""
        -fx-font-weight: bold;
        -fx-font-size: 16px;
        -fx-text-fill: #2d3748;
    """);
        platoNombre.setMaxWidth(180);
        platoNombre.setWrapText(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label platoPrecio = new Label(String.format("%,.2f€", plato.getPrecio()));
        platoPrecio.setStyle("""
        -fx-font-weight: bold;
        -fx-font-size: 16px;
        -fx-text-fill: #ce6a2e;
        -fx-background-color: #eef2ff;
        -fx-background-radius: 6;
        -fx-padding: 4px 8px;
    """);

        topRow.getChildren().addAll(platoNombre, spacer, platoPrecio);


        HBox bottomRow = new HBox();
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        bottomRow.setSpacing(15);


        HBox counterBox = new HBox();
        counterBox.setSpacing(5);
        counterBox.setAlignment(Pos.CENTER_LEFT);
        counterBox.setStyle("""
        -fx-background-color: #f3f4f6;
        -fx-background-radius: 20;
        -fx-padding: 3px;
    """);

        Button botonMenos = new Button("-");
        botonMenos.setStyle("""
        -fx-background-color: #ffffff;
        -fx-background-radius: 50%;
        -fx-min-width: 28px;
        -fx-min-height: 28px;
        -fx-font-weight: bold;
        -fx-font-size: 14px;
        -fx-text-fill: #4b5563;
        -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);
        -fx-cursor: hand;
    """);

        Label numPedidos = new Label("1");
        numPedidos.setStyle("""
        -fx-font-weight: bold;
        -fx-font-size: 14px;
        -fx-text-fill: #1f2937;
        -fx-min-width: 30px;
        -fx-alignment: center;
    """);

        Button botonMas = new Button("+");
        botonMas.setStyle("""
        -fx-background-color: #ce6a2e;
        -fx-background-radius: 50%;
        -fx-min-width: 28px;
        -fx-min-height: 28px;
        -fx-font-weight: bold;
        -fx-font-size: 14px;
        -fx-text-fill: white;
        -fx-effect: dropshadow(one-pass-box, rgba(79,70,229,0.3), 2, 0, 0, 1);
        -fx-cursor: hand;
    """);

        counterBox.getChildren().addAll(botonMenos, numPedidos, botonMas);


        HBox subtotalBox = new HBox();
        subtotalBox.setSpacing(5);
        subtotalBox.setAlignment(Pos.CENTER_LEFT);

        Label subtotalLabel = new Label("Subtotal:");
        subtotalLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b7280;");

        Label subtotalValue = new Label(String.format("%,.2f€", plato.getPrecio()));
        subtotalValue.setId("subtotalVLabel");
        subtotalValue.setStyle("""
        -fx-font-weight: bold;
        -fx-font-size: 14px;
        -fx-text-fill: #059669;
    """);

        subtotalBox.getChildren().addAll(subtotalLabel, subtotalValue);

        bottomRow.getChildren().addAll(counterBox, subtotalBox);

        // Botón eliminar (X)
        Button eliminarBtn = new Button("✕");
        eliminarBtn.setStyle("""
        -fx-background-color: transparent;
        -fx-text-fill: #9ca3af;
        -fx-font-size: 16px;
        -fx-font-weight: bold;
        -fx-min-width: 30px;
        -fx-min-height: 30px;
        -fx-cursor: hand;
        -fx-padding: 0;
    """);

        // Efecto hover para el botón eliminar
        eliminarBtn.setOnMouseEntered(e -> eliminarBtn.setStyle("""
        -fx-background-color: #fee2e2;
        -fx-text-fill: #dc2626;
        -fx-font-size: 16px;
        -fx-font-weight: bold;
        -fx-min-width: 30px;
        -fx-min-height: 30px;
        -fx-background-radius: 50%;
        -fx-cursor: hand;
        -fx-padding: 0;
    """));

        eliminarBtn.setOnMouseExited(e -> eliminarBtn.setStyle("""
        -fx-background-color: transparent;
        -fx-text-fill: #9ca3af;
        -fx-font-size: 16px;
        -fx-font-weight: bold;
        -fx-min-width: 30px;
        -fx-min-height: 30px;
        -fx-cursor: hand;
        -fx-padding: 0;
    """));

        // Acción para eliminar la card
        eliminarBtn.setOnAction(e -> {
            Parent parent = eliminarBtn.getParent();
            while (parent != null && !(parent instanceof HBox)) {
                parent = parent.getParent();
            }
            if (parent != null && parent.getParent() instanceof Pane) {
                ((Pane) parent.getParent()).getChildren().remove(parent);
            }
        });

        numPedidos.setText("1");


        botonMenos.setOnAction(e -> {

            PlatoDTO platoEnMapa = null;
            for(PlatoDTO p : mapaPedidos.keySet()){
                if(p.getNombre().equals(plato.getNombre())){
                    platoEnMapa = p;
                    break;
                }
            }
            if(platoEnMapa != null){
                int cantidadActual = mapaPedidos.get(platoEnMapa);
                if(cantidadActual>1){
                    int nuevaCantidad = cantidadActual -1;
                    mapaPedidos.put(platoEnMapa,nuevaCantidad);
                    numPedidos.setText(String.valueOf(nuevaCantidad));

                    double subtotal = nuevaCantidad * platoEnMapa.getPrecio();
                    subtotalValue.setText(String.format("%,.2f€", subtotal));
                }else{
                    mapaPedidos.remove(platoEnMapa);
                    mapaCards.remove(platoEnMapa);
                    if (hbox.getParent() instanceof Pane) {
                        ((Pane) hbox.getParent()).getChildren().remove(hbox);
                    }
                }
            }
        });

        botonMas.setOnAction(e -> {
            PlatoDTO platoEnMapa = null;
            for (PlatoDTO p : mapaPedidos.keySet()) {
                if (p.getNombre().equals(plato.getNombre())) {
                    platoEnMapa = p;
                    break;
                }
            }

            if (platoEnMapa != null) {
                int nuevaCantidad = mapaPedidos.get(platoEnMapa) + 1;
                mapaPedidos.put(platoEnMapa, nuevaCantidad);

                numPedidos.setText(String.valueOf(nuevaCantidad));
                double subtotal = nuevaCantidad * platoEnMapa.getPrecio();
                subtotalValue.setText(String.format("%,.2f€", subtotal));
            }
        });

        eliminarBtn.setOnAction(e -> {

            PlatoDTO platoAEliminar = null;
            for (PlatoDTO p : mapaPedidos.keySet()) {
                if (p.getNombre().equals(plato.getNombre())) {
                    platoAEliminar = p;
                    break;
                }
            }

            if (platoAEliminar != null) {
                mapaPedidos.remove(platoAEliminar);
                mapaCards.remove(platoAEliminar);
            }


            if (hbox.getParent() instanceof Pane) {
                ((Pane) hbox.getParent()).getChildren().remove(hbox);
            }


        });

        contentBox.getChildren().addAll(topRow, bottomRow);
        hbox.getChildren().addAll(contentBox, eliminarBtn);

        return hbox;
    }


    private void buscarYActualizarLabels(Node node, int nuevaCantidad, double precio) {
        if (node instanceof Label) {
            Label label = (Label) node;
            String texto = label.getText();

            // Si el label contiene solo números, es el label de cantidad
            if (texto.matches("\\d+")) {
                label.setText(String.valueOf(nuevaCantidad));
            }
            // Si el label contiene "€", es el label de subtotal
            else if (texto.contains("€")) {
                if ("subtotalVLabel".equals(label.getId())) {
                    double subtotal = nuevaCantidad * precio;
                    label.setText(String.format("%,.2f€", subtotal));
                }
            }
        }

        // Buscar recursivamente en los hijos
        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            for (Node child : parent.getChildrenUnmodifiable()) {
                buscarYActualizarLabels(child, nuevaCantidad, precio);
            }
        }
    }

    private void actualizarCardPedido(PlatoDTO plato, int nuevaCantidad) {
        HBox card = mapaCards.get(plato);
        if (card != null) {
            buscarYActualizarLabels(card, nuevaCantidad, plato.getPrecio());
        }
    }

    @FXML
    private void tramitarPedido(ActionEvent event){
        for(PlatoDTO p: mapaPedidos.keySet()){
            System.out.println(p.getNombre() + ", cantidad: "+ mapaPedidos.get(p));
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
            Button cardBtn = new Button("+");
            cardBtn.setStyle("""
                        -fx-background-color: #ce6a2e;
                        -fx-text-fill: white;
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

        if (botonCategoriaActivo != null) {
            botonCategoriaActivo.setStyle(estiloNormal);
        }

        boton.setStyle(estiloActivo);
        botonCategoriaActivo = boton;

        String categoria = boton.getText();
        mostrarPlatos(categoria);
    }

    public void pedirPlato(ActionEvent event){
        System.out.println("Deberia crearse un pedido");
        Node nodoActual = (Button) event.getSource();
        String nombrePlato = "";

        while (nodoActual.getParent()!=null){
            nodoActual = nodoActual.getParent();
            if(nodoActual instanceof VBox){
                nombrePlato = nodoActual.getId();
                break;
            }
        }

        PlatoDTO platoAPedir = null;
        try {
            platoAPedir = platoDAO.obtenerPlatoPorId(platoDAO.obtenerIdPlatoPorNombre(nombrePlato));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean platoPedido = false;
        PlatoDTO platoEnMapa = null;

        for(PlatoDTO p : mapaPedidos.keySet()){
            if (p.getNombre().equals(nombrePlato)) {
                platoPedido = true;
                platoEnMapa = p;
                break;
            }
        }

        if(platoPedido && platoEnMapa!=null){
            int nuevaCantidad = mapaPedidos.get(platoEnMapa)+1;
            mapaPedidos.put(platoEnMapa,nuevaCantidad);
            //Actualizar UI
            actualizarCardPedido(platoEnMapa, nuevaCantidad);
        }else{
            mapaPedidos.put(platoAPedir,1);
            HBox nuevaCard = crearCardPedido(platoAPedir);
            mapaCards.put(platoAPedir, nuevaCard);

            pedidosContainer.getChildren().add(nuevaCard);
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