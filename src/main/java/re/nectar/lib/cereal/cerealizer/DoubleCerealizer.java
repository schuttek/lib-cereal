package re.nectar.lib.cereal.cerealizer;

import re.nectar.lib.cereal.ByteArray;

public class DoubleCerealizer implements Cerealizer<Double> {
    @Override
    public void cerealizeTo(ByteArray ba, Double i) {
        ba.add(i);
    }

    @Override
    public Double uncerealizeFrom(ByteArray ba) {
        return ba.getDouble();
    }
}
