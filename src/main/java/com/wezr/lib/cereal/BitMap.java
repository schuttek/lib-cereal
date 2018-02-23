package com.wezr.lib.cereal;

import java.util.Arrays;

public class BitMap implements Cerealizable {

    private byte[] map;
    private int size;

    public BitMap(int size) {
        this.size = size;
        map = new byte[size / 8 + (size % 8 == 0 ? 0 : 1)];
        for (int n = 0; n < map.length; n++) {
            map[n] = 0;
        }
    }

    public BitMap(byte[] map, int size) {
        this.map = new byte[size / 8 + (size % 8 == 0 ? 0 : 1)];
        System.arraycopy(map, 0, this.map, 0, map.length);
        this.size = size;
    }

    public BitMap() {
    }

    public boolean is(int index) {
        return (map[index / 8] & (1 << (index % 8))) != 0;
    }
    public boolean not(int index) {
        return !is(index);
    }

    public void set(int index, boolean value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "This bitmap has a size equals to " + size + " but you're trying to access the bit at index " + index + " which is out of range");
        }
        byte b = map[index / 8];
        byte posBit = (byte) (1 << (index % 8));
        if (value) {
            b |= posBit;
        } else {
            b &= (255 - posBit);
        }
        map[index / 8] = b;
    }

    public void set(int index) {
        set(index, true);
    }

    public void clear(int index) {
        set(index, false);
    }

    public int size() {
        return size;
    }

    public byte[] map() {
        return map;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(map);
        result = prime * result + size;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BitMap other = (BitMap) obj;
        if (!Arrays.equals(map, other.map)) {
            return false;
        }
        if (size != other.size) {
            return false;
        }
        return true;
    }

    @Override
    public void cerealizeTo(ByteArray ba) {
        ba.add(size);
        ba.addByteArray(map);
    }

    @Override
    public void uncerealizeFrom(ByteArray ba) {
        size = ba.getInt();
        map = ba.getByteArray();
    }

}
