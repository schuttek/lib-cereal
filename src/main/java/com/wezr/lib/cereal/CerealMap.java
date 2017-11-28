package com.wezr.lib.cereal;

import java.util.HashMap;
import java.util.Map;

public class CerealMap<K extends Cerealizable, V extends Cerealizable> extends HashMap<K, V> implements Cerealizable {

    private final Class<K> keyClass;
    private final Class<V> valueClass;

    public CerealMap(final int initialCapacity, final float loadFactor, final Class<K> keyClass, final Class<V> valueClass) {
        super(initialCapacity, loadFactor);
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    public CerealMap(final int initialCapacity, final Class<K> keyClass, final Class<V> valueClass) {
        super(initialCapacity);
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    public CerealMap(final Class<K> keyClass, final Class<V> valueClass) {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    public CerealMap(final Map<? extends K, ? extends V> m, final Class<K> keyClass, final Class<V> valueClass) {
        super(m);
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        ba.add(size());
        for (Map.Entry<K, V> entry : this.entrySet()) {
            entry.getKey().cerealizeTo(ba);
            entry.getValue().cerealizeTo(ba);
        }
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        try {
            int size = ba.getInt();
            for (int t = 0; t < size; t++) {
                final K key = keyClass.newInstance();
                key.uncerealizeFrom(ba);
                final V value = valueClass.newInstance();
                value.uncerealizeFrom(ba);
                put(key, value);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
