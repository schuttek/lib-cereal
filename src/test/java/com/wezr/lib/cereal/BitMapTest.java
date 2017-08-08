package com.wezr.lib.cereal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BitMapTest {

    @Test
    @DisplayName("When a bit is set, the value persists")
    void setBitsPersist() {
        // Setup
        final BitMap bitMap = new BitMap(10);
        // Exercise
        bitMap.set(0);
        bitMap.set(1);
        bitMap.set(2);
        bitMap.set(3);
        bitMap.set(4);
        bitMap.set(5);
        bitMap.set(6);
        bitMap.set(7);
        bitMap.clear(8);
        bitMap.clear(9);
        // Verify
        assertTrue(bitMap.is(0));
        assertTrue(bitMap.is(1));
        assertTrue(bitMap.is(2));
        assertTrue(bitMap.is(3));
        assertTrue(bitMap.is(4));
        assertTrue(bitMap.is(5));
        assertTrue(bitMap.is(6));
        assertTrue(bitMap.is(7));
        assertFalse(bitMap.is(8));
        assertFalse(bitMap.is(9));
    }


    @Test
    @DisplayName("When a bit is set out of range, an exception is thrown")
    void setBitThrowsException() {
        // Setup
        final BitMap bitMap = new BitMap(5);
        // Exercise
        bitMap.set(0);
        bitMap.set(1);
        bitMap.set(2);
        bitMap.set(3);
        bitMap.set(4);
        // Verify
        assertThrows(IndexOutOfBoundsException.class, () -> {
            bitMap.clear(5);
        });
    }
}