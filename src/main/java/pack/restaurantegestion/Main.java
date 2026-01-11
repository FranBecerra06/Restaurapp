package pack.restaurantegestion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 725);
        stage.setTitle("Restaurapp");
        stage.setScene(scene);
        stage.show();




    }

    /*public static void main(String[] args) {

        try (Connection conn = ConexionBD.getConnection()) {
            System.out.println("Conexión establecida");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}