/*
 * Copyright (c) 2019 WEZR SAS
 */

package re.nectar.lib.cereal.cerealizer;

import re.nectar.lib.cereal.ByteArray;

import java.util.UUID;

public class UUIDCerealizer implements Cerealizer<UUID> {

    @Override
    public void cerealizeTo(final ByteArray ba, final UUID obj) {
        ba.add(obj);
    }

    @Override
    public UUID uncerealizeFrom(final ByteArray ba) {
        return ba.getUUID();
    }
}
