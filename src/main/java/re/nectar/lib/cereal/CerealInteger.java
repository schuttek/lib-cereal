package re.nectar.lib.cereal;

public class CerealInteger implements Cerealizable {
    private int value;

    public CerealInteger() {
    }

    public CerealInteger(int value) {
        this.value = value;
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        ba.add(value);
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        value = ba.getInt();
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CerealInteger{" +
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

        final CerealInteger that = (CerealInteger) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
