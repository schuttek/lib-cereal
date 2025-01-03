/*
 * Copyright (c) 2019 WEZR SAS
 */

package re.nectar.lib.cereal.cerealizer;

import re.nectar.lib.cereal.ByteArray;

import java.util.HashMap;
import java.util.Map;

public class MapCerealizer<K, V> implements Cerealizer<Map<K,V>> {

    private final Cerealizer<V> valueCerealizer;
    private final Cerealizer<K> keyCerealizer;

    public MapCerealizer(final Cerealizer<K> keyCerealizer, final Cerealizer<V> valueCerealizer) {
        this.keyCerealizer = keyCerealizer;
        this.valueCerealizer = valueCerealizer;
    }

    @Override
    public void cerealizeTo(ByteArray ba, Map<K, V> map) {
        ba.add(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            keyCerealizer.cerealizeTo(ba, entry.getKey());
            valueCerealizer.cerealizeTo(ba, entry.getValue());
        }
    }

    @Override
    public Map<K, V> uncerealizeFrom(ByteArray ba) {
        int mapSize = ba.getInt();
        Map<K, V> map = new HashMap<>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            K k = keyCerealizer.uncerealizeFrom(ba);
            V v = valueCerealizer.uncerealizeFrom(ba);
            map.put(k, v);
        }
        return map;
    }
}
