package dev.opentv.launcher;

import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;

public class HdmiPortTest {
    @Test public void findsPhysicalPortRegardlessOfInputListOrder() {
        assertEquals(0, HdmiPort.uniqueIndex(Arrays.asList("HDMI 3", "HDMI 1", "HDMI 2"), 3));
    }

    @Test public void recognizesCommonSystemLabelFormats() {
        for (String label : Arrays.asList("HDMI3", " hdmi 3 ", "HDMI_3", "HDMI-03", "HDMI 3 (ARC)", "HDMI 3: ARC")) {
            assertEquals(label, 0, HdmiPort.uniqueIndex(Arrays.asList(label), 3));
        }
    }

    @Test public void doesNotMistakeAnotherPortOrVendorIdentifierForHdmiThree() {
        assertEquals(-1, HdmiPort.uniqueIndex(Arrays.asList("HDMI 13", "HDMI 30", "AV 3", "HW3", "Jio", null), 3));
    }

    @Test public void ambiguousOrMissingPortFallsBackToManualChoice() {
        assertEquals(-1, HdmiPort.uniqueIndex(Arrays.asList("HDMI 3", "HDMI3"), 3));
        assertEquals(-1, HdmiPort.uniqueIndex(Arrays.asList("HDMI 1", "HDMI 2"), 3));
    }
}
