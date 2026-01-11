module pack.restaurantegestion {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;          // Para acceso a bases de datos
    requires java.prefs;        // Para usar Preferences
    requires java.desktop;      // Para algunas funcionalidades adicionales

    // Si estás usando Gson para JSON
    requires com.google.gson;

    // Abre los paquetes que usan reflexión (como FXML)
    opens pack.restaurantegestion to javafx.fxml;
    opens Controlador to javafx.fxml;
    opens DTO to com.google.gson, javafx.base;
    opens DAO to java.sql;

    // Exporta lo necesario
    exports pack.restaurantegestion;
    exports Controlador;
    exports DTO;
    exports DAO;
}