/*
 * Copyright (c) 2019 WEZR SAS
 */

package com.wezr.lib.cereal.cerealizer;

import com.wezr.lib.cereal.BitMap;
import com.wezr.lib.cereal.ByteArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BitMapCerealizerTest {

    @Test
    @DisplayName("Bitmap is correctly cerealized/uncerealized")
    void object_cerealization() {
        // Setup
        final BitMapCerealizer cerealizer = new BitMapCerealizer();
        final BitMap obj = new BitMap(5);
        obj.set(2, true);
        obj.set(4, true);
        // Exercise
        final ByteArray ba = new ByteArray();
        cerealizer.cerealizeTo(ba, obj);

        final BitMap obj2 = cerealizer.uncerealizeFrom(new ByteArray(ba));
        // Verify
        assertEquals(obj, obj2);
    }

}