package pack.restaurantegestion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 1. Cargamos tu nuevo archivo AdminView.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pack/restaurantegestion/AdminView.fxml"));
        
        // 2. Creamos la escena. NO ponemos tamaño (320, 240) para que use el tamaño grande de tu diseño (1080x720)
        Scene scene = new Scene(fxmlLoader.load());
        
        // 3. Título y mostrar
        stage.setTitle("Panel de Administración - RestaurApp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}