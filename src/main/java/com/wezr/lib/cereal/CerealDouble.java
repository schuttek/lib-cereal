package com.wezr.lib.cereal;

public class CerealDouble implements Cerealizable {
    private double value;

    public CerealDouble() {
    }

    public CerealDouble(double value) {
        this.value = value;
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        ba.add(value);
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        value = ba.getDouble();
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CerealDouble{" +
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

        final CerealDouble that = (CerealDouble) o;

        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        final long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }
}
