/*
 * Copyright (c) 2019 WEZR SAS
 */

package com.wezr.lib.cereal.cerealizer;

import com.wezr.lib.cereal.ByteArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringCerealizerTest {

    @Test
    @DisplayName("String is correctly cerealized/uncerealized")
    void object_cerealization() {
        // Setup
        final StringCerealizer cerealizer = new StringCerealizer();
        final String obj = "TestYOLO";
        // Exercise
        final ByteArray ba = new ByteArray();
        cerealizer.cerealizeTo(ba, obj);

        final String obj2 = cerealizer.uncerealizeFrom(new ByteArray(ba));
        // Verify
        assertEquals(obj, obj2);
    }

}