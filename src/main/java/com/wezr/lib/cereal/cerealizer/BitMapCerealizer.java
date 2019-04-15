/*
 * Copyright (c) 2019 WEZR SAS
 */

package com.wezr.lib.cereal.cerealizer;

import com.wezr.lib.cereal.BitMap;
import com.wezr.lib.cereal.ByteArray;

public class BitMapCerealizer implements Cerealizer<BitMap> {

    @Override
    public void cerealizeTo(final ByteArray ba, final BitMap obj) {
        obj.cerealizeTo(ba);
    }

    @Override
    public BitMap uncerealizeFrom(final ByteArray ba) {
        final BitMap obj = new BitMap();
        obj.uncerealizeFrom(ba);
        return obj;
    }
}
