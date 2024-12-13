/*
 * Copyright (c) 2019 WEZR SAS
 */

package re.nectar.lib.cereal.cerealizer;

import re.nectar.lib.cereal.ByteArray;

public class StringCerealizer implements Cerealizer<String> {

    @Override
    public void cerealizeTo(final ByteArray ba, final String obj) {
        ba.add(obj);
    }

    @Override
    public String uncerealizeFrom(final ByteArray ba) {
        return ba.getString();
    }
}
