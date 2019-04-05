/*
 * Copyright (c) 2019 WEZR SAS
 */

package com.wezr.lib.cereal.cerealizer;

import com.wezr.lib.cereal.ByteArray;
import com.wezr.lib.cereal.Cerealizable;

/**
 * This class can be used as a bridge between {@link Cerealizable} and {@link Cerealizer}
 * Indeed, you can use a Cerealizer everywhere you can use a Cerealizable. Using this class let you also use easily a Cerealizez instead of a Cerealizable
 * @param <T>
 */
public class CerealizableCerealizer<T extends Cerealizable> implements Cerealizer<T> {

    private final Class<T> clazz;

    public CerealizableCerealizer(final Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void cerealizeTo(final ByteArray ba, final T obj) {
        ba.add(obj);
    }

    @Override
    public T uncerealizeFrom(final ByteArray ba) {
        return ba.get(clazz);
    }

}
