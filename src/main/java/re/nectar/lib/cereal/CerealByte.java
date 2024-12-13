package re.nectar.lib.cereal;

public class CerealByte implements Cerealizable {
    private byte value;

    public CerealByte() {
    }

    public CerealByte(byte value) {
        this.value = value;
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        ba.add(value);
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        value = ba.getByte();
    }

    public byte getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CerealByte{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final CerealByte that = (CerealByte) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) value;
    }
}
