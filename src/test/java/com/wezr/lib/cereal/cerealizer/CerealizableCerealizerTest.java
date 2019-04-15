/*
 * Copyright (c) 2019 WEZR SAS
 */

package com.wezr.lib.cereal.cerealizer;

import com.wezr.lib.cereal.ByteArray;
import com.wezr.lib.cereal.CerealUUID;
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