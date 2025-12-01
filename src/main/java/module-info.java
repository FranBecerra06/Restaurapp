module pack.restaurantegestion {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;

	//requires java.sql;

	requires mysql.connector.j;



    opens Controlador to javafx.fxml;

    exports pack.restaurantegestion;
    exports Controlador;


}


