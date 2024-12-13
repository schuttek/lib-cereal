package re.nectar.lib.cereal;

public class CerealShort implements Cerealizable {
    private short value;

    public CerealShort() {
    }

    public CerealShort(short value) {
        this.value = value;
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        ba.add(value);
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        value = ba.getShort();
    }

    public short getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CerealShort{" +
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

        final CerealShort that = (CerealShort) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) value;
    }
}
