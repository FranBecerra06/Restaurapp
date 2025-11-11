module pack.restaurantegestion {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;


    opens pack.restaurantegestion to javafx.fxml;
    exports pack.restaurantegestion;
}