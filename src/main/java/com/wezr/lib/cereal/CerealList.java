package com.wezr.lib.cereal;

import java.util.Collection;
import java.util.LinkedList;

public class CerealList<T extends Cerealizable> extends LinkedList<T> implements Cerealizable {
    private final Class<T> clazz;

    public CerealList(final Class<T> clazz) {
        this.clazz = clazz;
    }

    public CerealList(final Collection<? extends T> c, final Class<T> clazz) {
        super(c);
        this.clazz = clazz;
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        ba.add(size());
        for (T cerealizeable : this) {
            cerealizeable.cerealizeTo(ba);
        }
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        try {
            int size = ba.getInt();
            for (int t = 0; t < size; t++) {
                final T object;
                object = clazz.newInstance();
                object.uncerealizeFrom(ba);
                add(object);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
