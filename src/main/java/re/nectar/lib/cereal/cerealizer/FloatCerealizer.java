package re.nectar.lib.cereal.cerealizer;

import re.nectar.lib.cereal.ByteArray;

public class FloatCerealizer implements Cerealizer<Float> {
    @Override
    public void cerealizeTo(ByteArray ba, Float i) {
        ba.add(i);
    }

    @Override
    public Float uncerealizeFrom(ByteArray ba) {
        return ba.getFloat();
    }
}
