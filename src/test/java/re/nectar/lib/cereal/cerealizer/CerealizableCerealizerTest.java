/*
 * Copyright (c) 2019 WEZR SAS
 */

package re.nectar.lib.cereal.cerealizer;

import re.nectar.lib.cereal.ByteArray;
import re.nectar.lib.cereal.CerealUUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CerealizableCerealizerTest {

    @Test
    @DisplayName("Cerealizable is correctly cerealized/uncerealized")
    void object_cerealization() {
        // Setup
        final CerealUUID obj = new CerealUUID(UUID.randomUUID());

        final CerealizableCerealizer<CerealUUID> cerealizer = new CerealizableCerealizer<>(CerealUUID.class);
        // Exercise
        final ByteArray ba = new ByteArray();
        cerealizer.cerealizeTo(ba, obj);

        final CerealUUID obj2 = cerealizer.uncerealizeFrom(new ByteArray(ba));
        // Verify
        assertEquals(obj, obj2);
    }

}