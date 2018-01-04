package com.wezr.lib.cereal;

public class CerealFloat implements Cerealizable {
    private float value;

    public CerealFloat() {
    }

    public CerealFloat(float value) {
        this.value = value;
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        ba.add(value);
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        value = ba.getFloat();
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CerealFloat{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final CerealFloat that = (CerealFloat) o;

        return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return (value != +0.0f ? Float.floatToIntBits(value) : 0);
    }
}
