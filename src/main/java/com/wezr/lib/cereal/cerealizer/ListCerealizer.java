/*
 * Copyright (c) 2019 WEZR SAS
 */

package com.wezr.lib.cereal.cerealizer;

import com.wezr.lib.cereal.ByteArray;

import java.util.ArrayList;
import java.util.List;

public class ListCerealizer<U> implements Cerealizer<List<U>> {

    private final Cerealizer<U> cerealizer;

    public ListCerealizer(final Cerealizer<U> cerealizer) {
        this.cerealizer = cerealizer;
    }

    @Override
    public void cerealizeTo(final ByteArray ba, final List<U> obj) {
        ba.add(obj.size());
        for (final U o : obj) {
            cerealizer.cerealizeTo(ba, o);
        }
    }

    @Override
    public List<U> uncerealizeFrom(final ByteArray ba) {
        final int length = ba.getInt();
        final List<U> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            list.add(cerealizer.uncerealizeFrom(ba));
        }
        return list;
    }
}
