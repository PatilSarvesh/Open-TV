package dev.opentv.launcher;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Recognizes an explicit port label; never infers a port from list order or vendor hardware IDs. */
final class HdmiPort {
    private static final Pattern LABEL = Pattern.compile(
            "^HDMI[\\s_-]*0*([1-9][0-9]*)(?=$|\\s|\\(|\\[|:|[-–—])", Pattern.CASE_INSENSITIVE);

    private HdmiPort() {}

    static int uniqueIndex(List<String> labels, int port) {
        int found = -1;
        for (int i = 0; i < labels.size(); i++) {
            String label = labels.get(i);
            if (label == null) continue;
            Matcher match = LABEL.matcher(label.trim());
            if (!match.find()) continue;
            try {
                if (Integer.parseInt(match.group(1)) != port) continue;
            } catch (NumberFormatException ignored) {
                continue;
            }
            if (found >= 0) return -1; // Ambiguous firmware labels: let the owner choose.
            found = i;
        }
        return found;
    }
}
