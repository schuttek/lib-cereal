/*
 * Copyright (c) 2019 WEZR SAS
 */

package com.wezr.lib.cereal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NullBitMapTest {

    @Test
    @DisplayName("Can serialize and unserialize null bitmap")
    void null_bitmap_test() {
        // Setup
        final NullBitMap nullBitMap = new NullBitMap(10);
        nullBitMap.set(0, true);
        nullBitMap.set(5, true);
        nullBitMap.set(9, true);
        // Exercise
        final ByteArray ba = new ByteArray();
        nullBitMap.cerealizeTo(ba);

        final NullBitMap obj2 = new ByteArray(ba).uncerealize(NullBitMap.class);
        // Verify
        assertEquals(obj2.is(0), true);
        assertEquals(obj2.is(1), false);
        assertEquals(obj2.is(2), false);
        assertEquals(obj2.is(3), false);
        assertEquals(obj2.is(4), false);
        assertEquals(obj2.is(5), true);
        assertEquals(obj2.is(6), false);
        assertEquals(obj2.is(7), false);
        assertEquals(obj2.is(8), false);
        assertEquals(obj2.is(9), true);
    }


    @Test
    @DisplayName("Out of bound index returns like is null was set")
    void null_bitmap_test_where_out_of_bound() {
        // Setup
        final NullBitMap nullBitMap = new NullBitMap(10);
        nullBitMap.set(0, true);
        nullBitMap.set(5, true);
        nullBitMap.set(9, true);
        // Exercise
        final ByteArray ba = new ByteArray();
        nullBitMap.cerealizeTo(ba);

        final NullBitMap obj2 = new ByteArray(ba).uncerealize(NullBitMap.class);
        // Verify
        assertEquals(obj2.is(10), true);
        assertEquals(obj2.is(25), true);
    }
}