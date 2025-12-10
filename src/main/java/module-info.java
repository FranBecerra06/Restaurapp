module pack.restaurantegestion {


    opens Controlador to javafx.fxml;
    opens DTO to javafx.base;

    exports pack.restaurantegestion;
    exports Controlador;
	requires javafx.fxml;
	requires javafx.controls;
	requires java.sql;


}