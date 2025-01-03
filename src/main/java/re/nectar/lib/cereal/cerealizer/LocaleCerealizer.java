package re.nectar.lib.cereal.cerealizer;

import re.nectar.lib.cereal.ByteArray;

import java.util.Locale;

public class LocaleCerealizer implements Cerealizer<Locale> {
    @Override
    public void cerealizeTo(ByteArray ba, Locale locale) {
        ba.add(locale.toLanguageTag());
    }

    @Override
    public Locale uncerealizeFrom(ByteArray ba) {
        return Locale.forLanguageTag(ba.getString());
    }
}
