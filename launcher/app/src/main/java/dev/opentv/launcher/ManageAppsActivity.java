package dev.opentv.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** All actions work with D-pad and OK; hidden apps remain available here. */
public final class ManageAppsActivity extends Activity {
    private final List<App> apps = new ArrayList<>();
    private List<String> favorites;
    private List<String> hidden;
    private AppPreferences preferences;
    private ArrayAdapter<App> adapter;
    private ListView list;

    private static final class App {
        final String pkg;
        final String label;
        App(String pkg, String label) { this.pkg = pkg; this.label = label; }
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Config config = Config.load(this);
        preferences = new AppPreferences(this);
        favorites = new ArrayList<>(config.dockApps);
        hidden = new ArrayList<>(config.hiddenApps);

        LinearLayout screen = new LinearLayout(this);
        screen.setOrientation(LinearLayout.VERTICAL);
        screen.setPadding(dp(64), dp(32), dp(64), dp(24));
        screen.setBackgroundColor(Color.rgb(16, 20, 25));
        screen.addView(text(getString(R.string.manage_apps), 30),
                new LinearLayout.LayoutParams(-1, -2));
        TextView help = text(getString(R.string.manage_apps_help, config.columns), 17);
        help.setTextColor(0xFFBAC6D0);
        help.setPadding(0, dp(12), 0, dp(18));
        screen.addView(help, new LinearLayout.LayoutParams(-1, -2));

        list = new ListView(this);
        list.setDividerHeight(dp(8));
        list.setSelector(R.drawable.settings_choice_bg);
        list.setDrawSelectorOnTop(false);
        list.setPadding(dp(2), dp(2), dp(2), dp(2));
        screen.addView(list, new LinearLayout.LayoutParams(-1, 0, 1));
        TextView empty = text(getString(R.string.no_tv_apps), 20);
        screen.addView(empty, new LinearLayout.LayoutParams(-1, -2));
        list.setEmptyView(empty);
        setContentView(screen);

        Set<String> seen = new HashSet<>();
        Intent query = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        for (ResolveInfo info : getPackageManager().queryIntentActivities(query, 0)) {
            if (info.activityInfo == null) continue;
            String pkg = info.activityInfo.packageName;
            if (pkg.equals(getPackageName()) || !seen.add(pkg)) continue;
            apps.add(new App(pkg, String.valueOf(info.loadLabel(getPackageManager()))));
        }
        apps.add(new App(LiveTvActivity.TILE_ID, getString(R.string.live_tv)));
        Collections.sort(apps, (a, b) -> a.label.compareToIgnoreCase(b.label));
        adapter = new ArrayAdapter<App>(this, android.R.layout.simple_list_item_1, apps) {
            @Override
            public View getView(int position, View recycled, ViewGroup parent) {
                App app = getItem(position);
                TextView row = recycled instanceof TextView ? (TextView) recycled : text("", 20);
                row.setPadding(dp(18), dp(14), dp(18), dp(14));
                row.setMinHeight(dp(72));
                int favorite = favorites.indexOf(app.pkg);
                String status = hidden.contains(app.pkg) ? getString(R.string.hidden_status)
                        : favorite >= 0 ? getString(R.string.favorite_status, favorite + 1)
                        : getString(R.string.all_apps_status);
                row.setText(getString(R.string.managed_app_row, app.label, status));
                row.setContentDescription(getString(R.string.managed_app_description, app.label, status));
                return row;
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> showActions(apps.get(position), position));
        list.requestFocus();
        if (!apps.isEmpty()) list.setSelection(state == null ? 0 : state.getInt("selection", 0));
    }

    private void showActions(App app, int position) {
        List<String> labels = new ArrayList<>();
        List<Runnable> actions = new ArrayList<>();
        boolean isHidden = hidden.contains(app.pkg);
        int favorite = favorites.indexOf(app.pkg);
        if (LiveTvActivity.TILE_ID.equals(app.pkg)) {
            add(labels, actions, R.string.choose_live_tv_input, () -> startActivity(
                    new Intent(this, LiveTvActivity.class)
                            .putExtra(LiveTvActivity.EXTRA_CHOOSE_INPUT, true)));
        }
        add(labels, actions, favorite >= 0 ? R.string.remove_favorite : R.string.add_favorite, () -> {
            favorites = AppOrder.toggle(favorites, app.pkg);
            hidden.remove(app.pkg);
        });
        if (favorite > 0) add(labels, actions, R.string.move_earlier,
                () -> favorites = AppOrder.move(favorites, app.pkg, -1));
        if (favorite >= 0 && favorite < favorites.size() - 1) {
            add(labels, actions, R.string.move_later,
                    () -> favorites = AppOrder.move(favorites, app.pkg, 1));
        }
        add(labels, actions, isHidden ? R.string.show_app : R.string.hide_app, () -> {
            hidden = AppOrder.toggle(hidden, app.pkg);
            if (hidden.contains(app.pkg)) favorites.remove(app.pkg);
        });
        new AlertDialog.Builder(this)
                .setTitle(app.label)
                .setItems(labels.toArray(new String[0]), (dialog, which) -> {
                    actions.get(which).run();
                    preferences.save(favorites, hidden);
                    setResult(RESULT_OK);
                    adapter.notifyDataSetChanged();
                    list.requestFocus();
                    list.setSelection(position);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        state.putInt("selection", Math.max(0, list.getSelectedItemPosition()));
        super.onSaveInstanceState(state);
    }

    private void add(List<String> labels, List<Runnable> actions, int label, Runnable action) {
        labels.add(getString(label));
        actions.add(action);
    }

    private TextView text(String value, int size) {
        TextView text = new TextView(this);
        text.setText(value);
        text.setTextSize(size);
        text.setTextColor(Color.WHITE);
        return text;
    }

    private int dp(int value) { return Math.round(value * getResources().getDisplayMetrics().density); }
}
