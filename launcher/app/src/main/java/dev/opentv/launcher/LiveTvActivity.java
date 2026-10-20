package dev.opentv.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.tv.TvContract;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Opens the selected physical HDMI input. New installations ask the user to choose. */
public final class LiveTvActivity extends Activity {
    static final String TILE_ID = "dev.opentv.launcher.live_tv";
    static final String EXTRA_CHOOSE_INPUT = "choose_input";
    // Retain the original preference key so existing manually selected inputs survive updates.
    private static final String INPUT_KEY = "jio_input_id";
    private final List<TvInputInfo> inputs = new ArrayList<>();
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        preferences = getSharedPreferences("live_tv", MODE_PRIVATE);
        TvInputManager manager = (TvInputManager) getSystemService(TV_INPUT_SERVICE);
        try {
            if (manager != null) {
                for (TvInputInfo input : manager.getTvInputList()) {
                    // Keep every exposed HDMI input, even if the box is off or the TV's state is stale.
                    if (input.isPassthroughInput() && input.getType() == TvInputInfo.TYPE_HDMI) inputs.add(input);
                }
            }
        } catch (SecurityException ignored) {
            // Some firmware restricts input discovery; explain the remote fallback below.
        }
        Collections.sort(inputs, (a, b) -> inputLabel(a).compareToIgnoreCase(inputLabel(b)));
        String saved = preferences.getString(INPUT_KEY, null);
        if (state == null && !getIntent().getBooleanExtra(EXTRA_CHOOSE_INPUT, false)) {
            TvInputInfo target = null;
            for (TvInputInfo input : inputs) {
                if (input.getId().equals(saved)) target = input;
            }
            if (saved == null) {
                int preferred = defaultInputIndex();
                if (preferred >= 0) target = inputs.get(preferred);
            }
            if (target != null && openInput(target)) {
                finish();
                return;
            }
        }
        showChooser();
    }

    private int defaultInputIndex() {
        List<String> labels = new ArrayList<>();
        for (TvInputInfo input : inputs) {
            // Use the physical input's system label, not a renamed port or a CEC child device.
            CharSequence label = input.loadLabel(this);
            labels.add(input.getParentId() == null && label != null ? label.toString() : null);
        }
        return HdmiPort.uniqueIndex(labels, getResources().getInteger(R.integer.default_hdmi_port));
    }

    private void showChooser() {
        if (inputs.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.live_tv)
                    .setMessage(R.string.no_hdmi_inputs)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
                    .setOnCancelListener(dialog -> finish())
                    .show();
            return;
        }
        String[] labels = new String[inputs.size()];
        for (int i = 0; i < inputs.size(); i++) {
            String label = inputLabel(inputs.get(i));
            labels[i] = label.isEmpty() ? getString(R.string.hdmi_input_fallback, i + 1) : label;
        }
        // Buttons instead of an item-click auto-dismiss let a failed switch leave the chooser usable.
        int selected = Math.max(0, defaultInputIndex());
        String saved = preferences.getString(INPUT_KEY, null);
        for (int i = 0; i < inputs.size(); i++) if (inputs.get(i).getId().equals(saved)) selected = i;
        final int[] selection = {selected};
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCustomTitle(chooserTitle())
                .setSingleChoiceItems(labels, selected, (choice, which) -> selection[0] = which)
                .setPositiveButton(R.string.open_live_tv, null)
                .setNegativeButton(android.R.string.cancel, (choice, which) -> finish())
                .setOnCancelListener(choice -> finish())
                .create();
        dialog.setOnShowListener(ignored -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                TvInputInfo input = inputs.get(selection[0]);
                if (openInput(input)) {
                    preferences.edit().putString(INPUT_KEY, input.getId()).apply();
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.getListView().requestFocus();
            dialog.getListView().setSelection(selection[0]);
        });
        dialog.show();
    }

    private LinearLayout chooserTitle() {
        int spacing = Math.round(24 * getResources().getDisplayMetrics().density);
        LinearLayout title = new LinearLayout(this);
        title.setOrientation(LinearLayout.VERTICAL);
        title.setPadding(spacing, spacing, spacing, spacing / 2);
        TextView heading = new TextView(this);
        heading.setText(R.string.choose_live_tv_input);
        heading.setTextSize(22);
        heading.setTextColor(0xFFFFFFFF);
        title.addView(heading);
        TextView help = new TextView(this);
        help.setText(R.string.live_tv_input_help);
        help.setTextSize(17);
        help.setTextColor(0xFFC6CFD8);
        help.setPadding(0, spacing / 3, 0, 0);
        title.addView(help);
        return title;
    }

    private String inputLabel(TvInputInfo input) {
        CharSequence supplied = input.loadLabel(this);
        String label = supplied == null ? "" : supplied.toString().trim();
        CharSequence custom = input.loadCustomLabel(this);
        if (custom != null && !custom.toString().trim().isEmpty()
                && !custom.toString().equals(label)) {
            return label.isEmpty() ? custom.toString() : getString(R.string.hdmi_custom_label, label, custom);
        }
        return label;
    }

    private boolean openInput(TvInputInfo input) {
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(TvContract.buildChannelUriForPassthroughInput(input.getId()),
                        "vnd.android.cursor.item/channel")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException | SecurityException e) {
            Toast.makeText(this, R.string.live_tv_input_unavailable, Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
