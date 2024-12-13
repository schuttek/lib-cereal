/*
 * Copyright (c) 2019 WEZR SAS
 */

package re.nectar.lib.cereal.cerealizer;

import re.nectar.lib.cereal.BitMap;
import re.nectar.lib.cereal.ByteArray;

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
