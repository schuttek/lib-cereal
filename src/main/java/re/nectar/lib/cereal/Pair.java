package re.nectar.lib.cereal;

import java.util.Map;

public class Pair<K, V> implements Map.Entry<K, V> {

    final K key;
    final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V setValue(V v) {
        throw new UnsupportedOperationException();
    }

}
