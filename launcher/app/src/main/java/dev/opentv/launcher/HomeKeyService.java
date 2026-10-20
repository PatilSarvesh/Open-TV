package dev.opentv.launcher;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

/**
 * Routes HOME and ALL_APPS to Open TV on firmware that prioritizes its stock launcher.
 * Every other key passes through. No screen content is read and no key events are stored.
 */
public class HomeKeyService extends AccessibilityService {

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode != KeyEvent.KEYCODE_HOME && keyCode != KeyEvent.KEYCODE_ALL_APPS) return false;
        if (event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled()) {
            Intent home = new Intent(this, MainActivity.class)
                    .setAction(Intent.ACTION_MAIN)
                    .addCategory(Intent.CATEGORY_HOME)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            try {
                startActivity(home);
            } catch (ActivityNotFoundException | SecurityException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }
}
