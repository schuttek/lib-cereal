/*
 * Copyright (c) 2019 WEZR SAS
 */

package re.nectar.lib.cereal;

public class NullBitMap extends BitMap {

    public NullBitMap(final int size) {
        super(size);
        outOfBoundValue = true;
    }

    public NullBitMap(final byte[] map, final int size) {
        super(map, size);
        outOfBoundValue = true;
    }

    public NullBitMap() {
        super();
        outOfBoundValue = true;
    }
}
