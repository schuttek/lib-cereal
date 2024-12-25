package re.nectar.lib.cereal.cerealizer;

import re.nectar.lib.cereal.ByteArray;

public class LongCerealizer implements Cerealizer<Long> {
    @Override
    public void cerealizeTo(ByteArray ba, Long i) {
        ba.add(i);
    }

    @Override
    public Long uncerealizeFrom(ByteArray ba) {
        return ba.getLong();
    }
}
