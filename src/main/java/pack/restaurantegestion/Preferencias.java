package pack.restaurantegestion;

import java.util.prefs.Preferences;

public class Preferencias {
    private static final Preferences prefs = Preferences.userRoot().node("restaurapp");

    public static void guardarUsuario(String usuario) {
        prefs.put("usuario", usuario);
    }

    public static String obtenerUsuario() {
        return prefs.get("usuario", "");
    }

    public static void limpiarTodo() {
        try {
            prefs.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

