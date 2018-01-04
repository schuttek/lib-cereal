package com.wezr.lib.cereal;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public class CerealInputStream extends InputStream {

    private static final int OBJECT_BUFFER_SIZE = 4; // 4 bytes for an int.
    private final DataInputStream inputStream;
    private long position = 0;

    public CerealInputStream(InputStream inputStream) {
        this.inputStream = new DataInputStream(inputStream);
    }


    public CerealInputStream(DataInputStream inputStream) {
        // we're creating a DataInputStream anyway, so if we receive one, no
        // need to rewrap it.
        this.inputStream = inputStream;
    }

    public <T> Optional<T> read(Class<T> cerealClass)
            throws InstantiationException, IllegalAccessException, IOException {
        byte[] byteLengthBuffer = new byte[OBJECT_BUFFER_SIZE];
        try {
            inputStream.readFully(byteLengthBuffer);
            int length = ByteArray.bytesToInt(byteLengthBuffer, 0);
            byte[] cerealizedObjectBuffer = new byte[length];
            inputStream.readFully(cerealizedObjectBuffer);
            position += length + OBJECT_BUFFER_SIZE;
            ByteArray ba = new ByteArray(cerealizedObjectBuffer);
            T cerealObject = cerealClass.newInstance();
            ((Cerealizable) cerealObject).uncerealizeFrom(ba);
            return Optional.of(cerealObject);
        } catch (EOFException e) {
            return Optional.empty();
        }
    }

    public long position() {
        return position;
    }

    @Override
    public long skip(final long n) throws IOException {
        long skipped = super.skip(n);
        position += skipped;
        return skipped;
    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedEncodingException();
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
