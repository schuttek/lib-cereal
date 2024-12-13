package re.nectar.lib.cereal;

import re.nectar.lib.cereal.cerealizer.Cerealizer;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.UUID;

/**
 * This class essentially works like java.io.ByteBuffer. You can add data to the
 * front and back of the ByteArray, and remove bytes from the front. The main
 * difference is that it's a bit faster on many successive add() operations than
 * ByteBuffer's putType() methods, since new chunks are added as a list, and
 * therefore always O(1).
 * <p>
 * A common use case is to pack a set of mixed raw values (say, an int, a String
 * and a double) into a byte array, then unpack them later.
 * <p>
 * This class never actually changes the contents of byte arrays passed to it,
 * so there's no need to copy arrays before passing them to these methods.
 * <p>
 * You can only ever remove bytes from the front of a ByteArray, by using the
 * get{Type}() methods. ByteArray cannot be rewound. the coalesce method may
 * free memory at the beginning of the ByteArray as it's being read from, so
 * it's safe to use this as a backing to a messaging queue between two threads
 * for example.
 * <p>
 * This class is NOT threadsafe, so make sure you use external synchronization.
 * <p>
 * WARNING: DO NOT CONFUSE THIS WITH StringBuffer! Adding Strings to this object
 * in sequence does NOT concatenate Strings, but stores them as separate
 * strings! byteArray.add("hello, "); byteArray.add("World!") will NOT become
 * "hello, World!".
 *
 * @author kai
 */

public class ByteArray implements Cerealizable {


    private int length = 0;
    private Chunk front = null;
    private Chunk back = null;

    public ByteArray(final byte[] b) {
        addRawBytes(b);
    }

    public ByteArray() {
    }

    /**
     * Copy constructor
     * <p>
     * Makes an exact, deep and independent copy of the given ByteArray.
     *
     * @param ba
     */
    public ByteArray(final ByteArray ba) {
        ba.coalesce();
        final byte[] copyBuffer = new byte[ba.front.length];
        System.arraycopy(ba.front.array, 0, copyBuffer, 0, ba.front.array.length);
        front = back = makeChunk(copyBuffer, 0, copyBuffer.length);
        length = ba.length;
    }

    public ByteArray(final ByteBuffer byteBuffer) {
        final byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        addRawBytes(bytes);
    }

    public static ByteArray wrap(final byte[] bytes) {
        return new ByteArray(bytes);
    }

    public static ByteArray wrap(final ByteBuffer byteBuffer) {
        return new ByteArray(byteBuffer);
    }

    public static short bytesToShort(final byte[] array, final int offset) {
        short n = 0;
        n ^= array[offset] & 0xFF;
        n <<= 8;
        n ^= array[offset + 1] & 0xFF;
        return n;
    }

    public static void shortToBytes(short val, final byte[] byteBuff, final int offset) {
        byteBuff[offset + 1] = (byte) val;
        val >>= 8;
        byteBuff[offset] = (byte) val;
    }

    public static long bytesToLong(final byte[] array, final int offset) {
        long l = 0;
        for (int i = offset; i < offset + 8; i++) {
            l <<= 8;
            l ^= array[i] & 0xFF;
        }
        return l;
    }

    public static void longToBytes(long l, final byte[] bb, final int offset) {
        for (int i = 7 + offset; i > offset; i--) {
            bb[i] = (byte) l;
            l >>>= 8;
        }
        bb[offset] = (byte) l;
    }

    public static int bytesToInt(final byte[] array, final int offset) {
        return array[offset] << 24 | (array[offset + 1] & 0xFF) << 16 | (array[offset + 2] & 0xFF) << 8
                | (array[offset + 3] & 0xFF);
    }

    public static void intToBytes(final int i, final byte[] byteBuff, final int offset) {
        for (int t = 0; t < 4; t++) {
            byteBuff[offset + t] = (byte) (i >> (4 - (t + 1)) * 8);
        }
    }

    /**
     * This implementation uses Double.doubleToLongBits instead of
     * Double.doubleToRawLongBits, which translates the 0x7ff8000000000100L NaN
     * value to the 0x7ff8000000000000L NaN Value.
     * <p>
     * Honestly, I have no idea what that might imply... but Hadoop uses this
     * version, and that's good enough for me.
     *
     * @param d
     * @param bb
     * @param offset
     */
    public static void doubleToBytes(final double d, final byte[] bb, final int offset) {
        longToBytes(Double.doubleToLongBits(d), bb, offset);
    }

    public static double bytesToDouble(final byte[] array, final int offset) {
        return Double.longBitsToDouble(bytesToLong(array, offset));
    }

    public static void floatToBytes(final float f, final byte[] bb, final int offset) {
        intToBytes(Float.floatToIntBits(f), bb, offset);
    }

    public static float bytesToFloat(final byte[] array, final int offset) {
        return Float.intBitsToFloat(bytesToInt(array, offset));
    }

    public static ByteArray cerealize(final Cerealizable cerealizable) {
        final ByteArray ba = new ByteArray();
        cerealizable.cerealizeTo(ba);
        return ba;
    }

    private Chunk makeChunk(final byte[] b, final int fromIdx, final int length) {
        if (fromIdx + length > b.length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        final Chunk tree = new Chunk();
        tree.array = b;
        tree.startIdx = fromIdx;
        tree.length = length;
        return tree;
    }

    public void addToFront(final byte[] b, final int fromIdx, final int length) {
        if (length == 0) {
            return;
        }
        final Chunk chunk = makeChunk(b, fromIdx, length);
        if (front == null) {
            front = chunk;
            back = front;
        } else {
            chunk.next = front;
            front = chunk;
        }
        this.length += chunk.length;
    }

    public void addRawBytes(final byte[] b, final int fromIdx, final int length) {
        if (length == 0) {
            return;
        }
        final Chunk chunk = makeChunk(b, fromIdx, length);
        if (front == null) {
            front = chunk;
            back = chunk;
        } else {
            back.next = chunk;
            back = chunk;
        }
        this.length += chunk.length;
    }

    /**
     * Compacts the internal storage data structures to it's minimal format.
     * <p>
     * Every add() operation can increase the numbers of small data buffers and
     * internal pointers.
     * <p>
     * This method realigns the internal data structure into a single byte
     * array.
     * <p>
     * max runtime is O(this.length)
     */
    public void coalesce() {
        if (front == null || front.next == null) {
            return;
        }
        final byte[] bb = new byte[length];

        int i = 0;
        Chunk cursor = front;
        while (cursor != null) {
            System.arraycopy(cursor.array, cursor.startIdx, bb, i, cursor.length);
            i += cursor.length;
            cursor = cursor.next;
        }

        front = makeChunk(bb, 0, length);
        back = front;
    }

    public int length() {
        return length;
    }

    public byte[] remove(final int numBytes) {
        if (numBytes == 0) {
            return new byte[0];
        }
        if (numBytes > this.length) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to remove " + numBytes + ", while length is " + this.length);
        }
        coalesce();
        final byte[] ret = new byte[numBytes];
        System.arraycopy(front.array, front.startIdx, ret, 0, numBytes);

        if (front.length - numBytes == 0) {
            front = null;
            length = 0;
        } else {
            front.startIdx += numBytes;
            front.length -= numBytes;
            length = front.length;
        }
        back = front;
        return ret;
    }

    public byte getByte() {
        return remove(1)[0];
    }

    public short getShort() {
        return bytesToShort(remove(2), 0);
    }

    public int getInt() {
        return bytesToInt(remove(4), 0);
    }

    public long getLong() {
        return bytesToLong(remove(8), 0);
    }

    public double getDouble() {
        return bytesToDouble(remove(8), 0);
    }

    public float getFloat() {
        return bytesToFloat(remove(4), 0);
    }

    public String getString() {
        final int len = getInt();
        if (len == -1) {
            return null;
        }
        final byte[] sb = remove(len);

        final CharsetDecoder dec = Charset.defaultCharset().newDecoder();
        dec.onMalformedInput(CodingErrorAction.IGNORE);
        try {
            return dec.decode(ByteBuffer.wrap(sb)).toString();
        } catch (final CharacterCodingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getBoolean() {
        final byte[] b = remove(1);
        if (b[0] == 0) {
            return false;
        }
        return true;
    }

    public UUID getUUID() {
        final long most = getLong();
        final long least = getLong();
        return new UUID(most, least);
    }

    public void addByteArray(final byte[] b) {
        if (b == null) {
            add(-1);
        } else if (b.length == 0) {
            add(0);
        } else {
            add(b.length);
            addRawBytes(b);
        }
    }

    public void addByteArrayToFront(final byte[] b) {
        if (b == null) {
            addToFront(-1);
        } else if (b.length == 0) {
            addToFront(0);
        } else {
            addRawBytesToFront(b);
            addToFront(b.length);
        }
    }

    public void addRawBytesToFront(final byte[] b) {
        addToFront(b, 0, b.length);
    }

    public void addRawBytes(final byte[] b) {
        addRawBytes(b, 0, b.length);
    }

    public void addToFront(final int i) {
        final byte[] b = new byte[4];
        intToBytes(i, b, 0);
        addRawBytesToFront(b);
    }

    public void add(final byte b) {
        final byte[] ba = new byte[1];
        ba[0] = b;
        addRawBytes(ba);
    }

    public void addIfNotNull(final Byte value) {
        if (value != null) {
            add(value);
        }
    }

    public void add(final short s) {
        final byte[] b = new byte[2];
        shortToBytes(s, b, 0);
        addRawBytes(b);
    }

    public void addIfNotNull(final Short value) {
        if (value != null) {
            add(value);
        }
    }

    public void add(final int i) {
        final byte[] b = new byte[4];
        intToBytes(i, b, 0);
        addRawBytes(b);
    }

    public void addIfNotNull(final Integer value) {
        if (value != null) {
            add(value);
        }
    }

    public void addToFront(final long l) {
        final byte[] b = new byte[8];
        longToBytes(l, b, 0);
        addRawBytesToFront(b);
    }

    public void addIfNotNull(final Long value) {
        if (value != null) {
            add(value);
        }
    }

    public void add(final long l) {
        final byte[] b = new byte[8];
        longToBytes(l, b, 0);
        addRawBytes(b);
    }

    public void addToFront(final double d) {
        final long l = Double.doubleToRawLongBits(d);
        final byte[] b = new byte[8];
        longToBytes(l, b, 0);
        addRawBytesToFront(b);
    }

    public void add(final double d) {
        final byte[] b = new byte[8];
        doubleToBytes(d, b, 0);
        addRawBytes(b);
    }

    public void addIfNotNull(final Double value) {
        if (value != null) {
            add(value);
        }
    }

    public void addToFront(final float f) {
        final byte[] b = new byte[8];
        floatToBytes(f, b, 0);
        addRawBytesToFront(b);
    }

    public void add(final float f) {
        final byte[] b = new byte[4];
        floatToBytes(f, b, 0);
        addRawBytes(b);
    }

    public void addIfNotNull(final Float value) {
        if (value != null) {
            add(value);
        }
    }

    public void addToFront(final String s) {
        if (s == null) {
            addToFront(0);
        } else {
            final byte[] b = s.getBytes();
            addRawBytesToFront(b);
            addToFront(b.length);
        }
    }

    public void add(final String s) {
        if (s == null) {
            add(-1);
        } else {
            final CharsetEncoder enc = Charset.defaultCharset().newEncoder();
            enc.onMalformedInput(CodingErrorAction.REPLACE);
            enc.onUnmappableCharacter(CodingErrorAction.REPLACE);
            enc.replaceWith("$".getBytes());
            final byte[] ba;
            try {
                final ByteBuffer bb = enc.encode(CharBuffer.wrap(s));
                bb.rewind();
                ba = new byte[bb.remaining()];
                bb.get(ba);
            } catch (final CharacterCodingException e) {
                throw new RuntimeException(e);
            }
            add(ba.length);
            addRawBytes(ba);
        }
    }

    public void addIfNotNull(final String value) {
        if (value != null) {
            add(value);
        }
    }

    public void addToFront(final boolean bool) {
        final byte[] b = new byte[1];
        b[0] = (byte) ((bool) ? 1 : 0);
        addRawBytesToFront(b);
    }

    public void add(final boolean bool) {
        final byte[] b = new byte[1];
        b[0] = (byte) ((bool) ? 1 : 0);
        addRawBytes(b);
    }

    public void addIfNotNull(final Boolean value) {
        if (value != null) {
            add(value);
        }
    }

    public void add(final UUID uuid) {
        add(uuid.getMostSignificantBits());
        add(uuid.getLeastSignificantBits());
    }

    public void addIfNotNull(final UUID uuid) {
        if (uuid != null) {
            add(uuid);
        }
    }

    public <T extends Cerealizable> void add(final T cerealizable) {
        cerealizable.cerealizeTo(this);
    }

    public <T extends Cerealizable> T get(final Class<T> clazz) {
        return uncerealize(clazz);
    }

    /**
     * add a array of Cerealizable objects to this ByteArray
     *
     * @param array must be "complete" - may not contain null values.
     */
    public <T extends Cerealizable> void add(final T[] array) {
        add(array.length);
        for (int t = 0; t < array.length; t++) {
            array[t].cerealizeTo(this);
        }
    }

    /**
     * uncerealize an array of Cerealizables of type clazz from this ByteArray
     * as added by {@link #add(Cerealizable[])}
     */
    @SuppressWarnings("unchecked")
    public <T extends Cerealizable> T[] getArray(final Class<T> clazz) {
        final T[] array = (T[]) Array.newInstance(clazz, getInt());
        for (int i = 0; i < array.length; i++) {
            array[i] = uncerealize(clazz);
        }
        return array;
    }

    /**
     * add a two dimensional array of Cerealizable objects to this ByteArray
     *
     * @param array must be "complete" - may not contain null values. each sub array may be of different lengths.
     */
    public <T extends Cerealizable> void add(final T[][] array) {
        add(array.length);
        for (int i = 0; i < array.length; i++) {
            add(array[i].length);
            for (int j = 0; j < array[i].length; j++) {
                array[i][j].cerealizeTo(this);
            }
        }
    }

    /**
     * uncerealize a two dimensional array of Cerealizables of type clazz from this ByteArray
     * as added by {@link #add(Cerealizable[][])}
     */
    @SuppressWarnings("unchecked")
    public <T extends Cerealizable> T[][] get2DArray(final Class<T> clazz) {
        final T[][] array = (T[][]) Array.newInstance(clazz, getInt(), 0);
        for (int i = 0; i < array.length; i++) {
            array[i] = (T[]) Array.newInstance(clazz, getInt());
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = uncerealize(clazz);
            }
        }
        return array;
    }

    /**
     * add a 3 dimensional array of Cerealizable objects to this ByteArray
     *
     * @param array must be "complete" - may not contain null values. each sub array may be of different lengths.
     */
    public <T extends Cerealizable> void add(final T[][][] array) {
        add(array.length);
        for (int i = 0; i < array.length; i++) {
            add(array[i].length);
            for (int j = 0; j < array[i].length; j++) {
                add(array[i][j].length);
                for (int k = 0; k < array[i][j].length; k++) {
                    array[i][j][k].cerealizeTo(this);
                }
            }
        }
    }

    /**
     * uncerealize a three dimensional array of Cerealizables of type clazz from this ByteArray
     * as added by {@link #add(Cerealizable[][][])}
     */
    @SuppressWarnings("unchecked")
    public <T extends Cerealizable> T[][][] get3DArray(final Class<T> clazz) {
        final T[][][] array = (T[][][]) Array.newInstance(clazz, getInt(), 0, 0);
        for (int i = 0; i < array.length; i++) {
            array[i] = (T[][]) Array.newInstance(clazz, getInt(), 0);
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = (T[]) Array.newInstance(clazz, getInt());
                for (int k = 0; k < array[i][j].length; k++) {
                    array[i][j][k] = uncerealize(clazz);
                }
            }
        }
        return array;
    }


    public <T> void add(final Cerealizer<T> cerealizer, final T obj) {
        cerealizer.cerealizeTo(this, obj);
    }

    public <T> T get(final Cerealizer<T> cerealizer) {
        return cerealizer.uncerealizeFrom(this);
    }

    public <T> void add(final Cerealizer<T> cerealizer, final T[] array) {
        add(array.length);
        for (int t = 0; t < array.length; t++) {
            cerealizer.cerealizeTo(this, array[t]);
        }
    }

    public <T> T[] getArray(final Cerealizer<T> cerealizer, final Class<T> clazz) {
        final int length = getInt();
        final T[] array = (T[]) Array.newInstance(clazz, length);
        for (int i = 0; i < length; i++) {
            array[i] = cerealizer.uncerealizeFrom(this);
        }
        return array;
    }


    public byte[] getByteArray() {
        final int len = getInt();
        if (len == -1) {
            return null;
        }
        if (len == 0) {
            return new byte[0];
        }
        final byte[] sb = remove(len);
        return sb;
    }

    public byte[] getAllBytes() {
        return remove(length());
    }

    public void reset() {
        front = null;
        back = null;
        length = 0;
    }

    public void reset(final byte[] value) {
        reset();
        addRawBytes(value);
    }

    @SuppressWarnings("unchecked")
    public <T extends Cerealizable> T uncerealize(final Class<? extends Cerealizable> clazz) {
        Cerealizable cerealizable = null;
        try {
            cerealizable = clazz.newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        cerealizable.uncerealizeFrom(this);
        return (T) cerealizable;
    }

    public static <T extends Cerealizable> T uncerealize(final byte[] bytes, final Class<? extends Cerealizable> clazz) {
        return new ByteArray(bytes).uncerealize(clazz);
    }

    @Override
    public void cerealizeTo(final ByteArray ba) {
        coalesce();
        ba.addByteArray(front.array);
    }

    @Override
    public void uncerealizeFrom(final ByteArray ba) {
        this.reset(ba.getByteArray());
    }

    private class Chunk {
        byte[] array = null;
        int startIdx;
        int length;
        Chunk next = null;
    }

}
