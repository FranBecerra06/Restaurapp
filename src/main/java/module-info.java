module pack.restaurantegestion {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;


    opens Controlador to javafx.fxml;

    exports pack.restaurantegestion;
    exports Controlador;


}


