package re.nectar.lib.cereal;

import re.nectar.lib.cereal.cerealizer.Cerealizer;

import java.io.IOException;
import java.io.OutputStream;

public class CerealOutputStream extends OutputStream {

    private final OutputStream outputStream;

    public CerealOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(final Cerealizable cerealizable) throws IOException {
        final ByteArray ba = new ByteArray();
        cerealizable.cerealizeTo(ba);
        writeRawObject(ba);
    }


    public <U> void write(final Cerealizer<U> cerealizer, final U obj) throws IOException {
        final ByteArray ba = new ByteArray();
        cerealizer.cerealizeTo(ba, obj);
        writeRawObject(ba);
    }

    private void writeRawObject(final ByteArray ba) throws IOException {
        final byte[] byteLengthBuffer = new byte[4];
        final byte[] cerealizedObjectBuffer = ba.getAllBytes();
        ByteArray.intToBytes(cerealizedObjectBuffer.length, byteLengthBuffer, 0);
        outputStream.write(byteLengthBuffer);
        outputStream.write(cerealizedObjectBuffer);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }

    @Override
    public void write(final int b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        outputStream.write(b, off, len);
    }
}
