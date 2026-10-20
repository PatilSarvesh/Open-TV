package dev.opentv.launcher;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/** Package-level preferences, independent of the current installed-app list. */
final class AppOrder {
    private AppOrder() {}

    static List<String> unique(List<String> packages) {
        LinkedHashSet<String> clean = new LinkedHashSet<>();
        for (String pkg : packages) {
            if (pkg != null && !pkg.trim().isEmpty()) clean.add(pkg.trim());
        }
        return new ArrayList<>(clean);
    }

    static List<String> toggle(List<String> packages, String pkg) {
        List<String> updated = unique(packages);
        if (!updated.remove(pkg)) updated.add(pkg);
        return updated;
    }

    static List<String> move(List<String> packages, String pkg, int direction) {
        List<String> updated = unique(packages);
        int from = updated.indexOf(pkg);
        if (from < 0) return updated;
        int to = Math.max(0, Math.min(updated.size() - 1, from + direction));
        updated.remove(from);
        updated.add(to, pkg);
        return updated;
    }
}
