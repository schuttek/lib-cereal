package re.nectar.lib.cereal;

public class CerealLong implements Cerealizable {
    private long value;

    public CerealLong() {
    }

    public CerealLong(long value) {
        this.value = value;
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        ba.add(value);
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        value = ba.getLong();
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CerealLong{" +
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

        final CerealLong that = (CerealLong) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }
}
