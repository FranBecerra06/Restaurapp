module pack.restaurantegestion {
    requires javafx.controls;
    requires javafx.fxml;


    opens Controlador to javafx.fxml;

    exports pack.restaurantegestion;
    exports Controlador;


}


