package re.nectar.lib.cereal;

import java.util.Map;

public class CerealPair<K extends Cerealizable, V extends Cerealizable> implements Cerealizable {
    private Class<K> keyClass;
    private Class<V> valueClass;
    private K key;
    private V value;
    public CerealPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public CerealPair(final Class<K> keyClass, final Class<V> valueClass) {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    public Class<K> getKeyClass() {
        return keyClass;
    }

    public void setKeyClass(Class<K> keyClass) {
        this.keyClass = keyClass;
    }

    public Class<V> getValueClass() {
        return valueClass;
    }

    public void setValueClass(Class<V> valueClass) {
        this.valueClass = valueClass;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CerealPair {" + key + " -> " + value + "}";
    }

    @Override
    public void cerealizeTo(ByteArray ba) {
        key.cerealizeTo(ba);
        value.cerealizeTo(ba);
    }

    @Override
    public void uncerealizeFrom(ByteArray ba) {
        key = ba.uncerealize(keyClass);
        value = ba.uncerealize(valueClass);
    }
}
