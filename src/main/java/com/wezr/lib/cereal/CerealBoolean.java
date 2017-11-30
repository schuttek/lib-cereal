package com.wezr.lib.cereal;

public class CerealBoolean implements Cerealizable {
    private boolean value;

    public CerealBoolean() {
    }

    public CerealBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        ba.add(value);
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        value = ba.getBoolean();
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CerealBoolean{" +
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

        final CerealBoolean that = (CerealBoolean) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }
}
