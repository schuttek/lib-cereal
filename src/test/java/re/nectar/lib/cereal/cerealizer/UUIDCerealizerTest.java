/*
 * Copyright (c) 2019 WEZR SAS
 */

package re.nectar.lib.cereal.cerealizer;

import re.nectar.lib.cereal.ByteArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UUIDCerealizerTest {

    @Test
    @DisplayName("UUID is correctly cerealized/uncerealized")
    void object_cerealization() {
        // Setup
        final UUIDCerealizer cerealizer = new UUIDCerealizer();
        final UUID obj = UUID.randomUUID();
        // Exercise
        final ByteArray ba = new ByteArray();
        cerealizer.cerealizeTo(ba, obj);

        final UUID obj2 = cerealizer.uncerealizeFrom(new ByteArray(ba));
        // Verify
        assertEquals(obj, obj2);
    }

}