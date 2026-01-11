package pack.restaurantegestion;

import DTO.NotificacionDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class Preferencias {
    private static final Preferences prefs = Preferences.userRoot().node("restaurapp");
    private static final Gson gson = new Gson();

    public static List<NotificacionDTO> notficaciones = new ArrayList<>();

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

