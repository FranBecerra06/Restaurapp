module pack.restaurantegestion {
    requires javafx.controls;
    requires javafx.fxml;

	requires java.sql;

	requires mysql.connector.j;



    opens pack.restaurantegestion to javafx.fxml;
    exports pack.restaurantegestion;
}