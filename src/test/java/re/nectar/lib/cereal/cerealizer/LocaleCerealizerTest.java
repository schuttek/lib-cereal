package re.nectar.lib.cereal.cerealizer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.nectar.lib.cereal.ByteArray;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocaleCerealizerTest {

    @Test
    @DisplayName("Locale is correctly cerealized/uncerealized")
    void object_cerealization() {
        // Setup
        final LocaleCerealizer cerealizer = new LocaleCerealizer();
        final Locale locale = Locale.ENGLISH;
        // Exercise
        final ByteArray ba = new ByteArray();
        cerealizer.cerealizeTo(ba, locale);

        final Locale locale2 = cerealizer.uncerealizeFrom(new ByteArray(ba));
        // Verify
        assertEquals(locale, locale2);
    }
}
