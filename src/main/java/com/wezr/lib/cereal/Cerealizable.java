package com.wezr.lib.cereal;

public interface Cerealizable {
    void cerealizeTo(ByteArray ba);

    void uncerealizeFrom(ByteArray ba);
}
