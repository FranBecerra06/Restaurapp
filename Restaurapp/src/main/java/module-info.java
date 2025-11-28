module pack.restaurantegestion {
    requires javafx.controls;
    requires javafx.fxml;

	requires java.sql;
	requires javafx.graphics;



    opens pack.restaurantegestion to javafx.fxml;
    exports pack.restaurantegestion;
}