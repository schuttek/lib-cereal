package com.wezr.lib.cereal;

import java.util.UUID;

public class CerealUUID implements Cerealizable {
    private UUID value;

    public CerealUUID() {
    }

    public CerealUUID(UUID value) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.value = value;
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        ba.add(value);
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        value = ba.getUUID();
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CerealUUID{" +
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

        final CerealUUID that = (CerealUUID) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
