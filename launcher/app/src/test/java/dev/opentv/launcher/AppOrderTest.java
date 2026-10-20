package dev.opentv.launcher;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class AppOrderTest {
    @Test public void movingFavoritesPreservesOrderOfOtherApps() {
        List<String> initial = Arrays.asList("youtube", "netflix", "prime", "hotstar");
        assertEquals(Arrays.asList("youtube", "prime", "netflix", "hotstar"),
                AppOrder.move(initial, "prime", -1));
        assertEquals(Arrays.asList("youtube", "netflix", "prime", "hotstar"), initial);
    }

    @Test public void moveStopsAtEdgesAndIgnoresMissingPackages() {
        List<String> initial = Arrays.asList("youtube", "netflix");
        assertEquals(initial, AppOrder.move(initial, "youtube", -1));
        assertEquals(initial, AppOrder.move(initial, "netflix", 1));
        assertEquals(initial, AppOrder.move(initial, "missing", 1));
    }

    @Test public void toggleRemovesFavoriteOrAddsItAtEnd() {
        List<String> initial = Arrays.asList("youtube", "netflix");
        assertEquals(Arrays.asList("netflix"), AppOrder.toggle(initial, "youtube"));
        assertEquals(Arrays.asList("youtube", "netflix", "prime"), AppOrder.toggle(initial, "prime"));
    }

    @Test public void repeatedOrInvalidEntriesDoNotCreateDuplicateTiles() {
        assertEquals(Arrays.asList("youtube", "netflix"),
                AppOrder.unique(Arrays.asList("youtube", null, "", " netflix ", "youtube")));
    }
}
