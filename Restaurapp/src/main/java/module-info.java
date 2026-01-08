module pack.restaurantegestion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    // CORRECCIÓN AQUÍ: connector con doble 'n'
    requires mysql.connector.j;

    opens pack.restaurantegestion to javafx.fxml;
    exports pack.restaurantegestion;
    
    // Y recuerda mantener esto si tienes controladores en otros paquetes
    opens Controlador to javafx.fxml;
    exports Controlador;
}