module pack.restaurantegestion {
    requires javafx.controls;
    requires javafx.fxml;


    opens pack.restaurantegestion to javafx.fxml;
    exports pack.restaurantegestion;
}