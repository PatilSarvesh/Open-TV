package dev.opentv.launcher;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

/** On-TV app choices survive theme changes, app updates, and process restarts. */
final class AppPreferences {
    private static final String NAME = "app_preferences";
    private final SharedPreferences preferences;

    AppPreferences(Context context) {
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    List<String> favorites(List<String> defaults) { return read("favorites", defaults); }
    List<String> hidden(List<String> defaults) { return read("hidden", defaults); }

    void save(List<String> favorites, List<String> hidden) {
        preferences.edit()
                .putString("favorites", new JSONArray(AppOrder.unique(favorites)).toString())
                .putString("hidden", new JSONArray(AppOrder.unique(hidden)).toString())
                .apply();
    }

    private List<String> read(String key, List<String> defaults) {
        String json = preferences.getString(key, null);
        if (json == null) return new ArrayList<>(defaults);
        try {
            JSONArray array = new JSONArray(json);
            List<String> packages = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                Object value = array.get(i);
                if (!(value instanceof String)) return new ArrayList<>(defaults);
                packages.add((String) value);
            }
            return AppOrder.unique(packages);
        } catch (JSONException e) {
            return new ArrayList<>(defaults);
        }
    }
}
