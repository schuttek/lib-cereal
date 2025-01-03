package re.nectar.lib.cereal;

public interface Cerealizable {
    String MIME_TYPE = "application/vnd.cereal";

    void cerealizeTo(ByteArray ba);

    void uncerealizeFrom(ByteArray ba);
}
