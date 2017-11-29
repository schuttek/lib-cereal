package com.wezr.lib.cereal;

import java.util.Arrays;

public class CerealBytes implements Cerealizable {
    private byte[] bowl;

    public CerealBytes() {
    }

    public CerealBytes(final byte[] bowl) {
        this.bowl = bowl;
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        ba.addByteArray(bowl);
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        this.bowl = ba.getByteArray();
    }

    public static CerealBytes pack(byte[] bowl) {
        return new CerealBytes(bowl);
    }

    public static byte[] unpack(final ByteArray ba) {
        CerealBytes cb = new CerealBytes();
        cb.uncerealizeFrom(ba);
        return cb.bowl;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final CerealBytes that = (CerealBytes) o;

        return Arrays.equals(bowl, that.bowl);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bowl);
    }

    public byte[] getBytes() {
        return bowl;
    }
}
