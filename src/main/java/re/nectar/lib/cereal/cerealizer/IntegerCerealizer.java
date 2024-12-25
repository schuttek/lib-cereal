package re.nectar.lib.cereal.cerealizer;

import re.nectar.lib.cereal.ByteArray;

public class IntegerCerealizer implements Cerealizer<Integer> {
    @Override
    public void cerealizeTo(ByteArray ba, Integer i) {
        ba.add(i);
    }

    @Override
    public Integer uncerealizeFrom(ByteArray ba) {
        return ba.getInt();
    }
}
