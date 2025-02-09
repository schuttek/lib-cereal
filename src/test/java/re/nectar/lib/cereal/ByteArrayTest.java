package re.nectar.lib.cereal;


import re.nectar.lib.cereal.cerealizer.StringCerealizer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ByteArrayTest {

    private static final int randIterations = 20;
    Random rand = new Random();

    private boolean loadUnloadDouble(double d) {
        ByteArray ba = new ByteArray();
        ba.add(d);
        return (d == ba.getDouble()) ? true : false;
    }

    @Test
    public void doubleTest() {
        for (int t = 0; t < randIterations; t++) {
            assertTrue(loadUnloadDouble(rand.nextDouble()));
        }
        assertTrue(loadUnloadDouble(1.0d));
        assertTrue(loadUnloadDouble(0.0d));
        assertTrue(loadUnloadDouble(-1.0d));
        assertTrue(loadUnloadDouble(Double.MAX_VALUE));
        assertTrue(loadUnloadDouble(Double.MIN_VALUE));
        assertTrue(loadUnloadDouble(-Double.MIN_VALUE));
        // comparing NaN to NaN SHOULD be false!
        assertFalse(loadUnloadDouble(Double.NaN));
        assertFalse(loadUnloadDouble(-Double.NaN));
        assertTrue(loadUnloadDouble(Double.NEGATIVE_INFINITY));
        assertTrue(loadUnloadDouble(Double.POSITIVE_INFINITY));
    }

    private boolean loadUnloadFloat(float d) {
        ByteArray ba = new ByteArray();
        ba.add(d);
        return (d == ba.getFloat()) ? true : false;
    }

    @Test
    public void floatTest() {
        for (int t = 0; t < randIterations; t++) {
            assertTrue(loadUnloadFloat(rand.nextFloat()));
        }
        assertTrue(loadUnloadFloat(1.0f));
        assertTrue(loadUnloadFloat(0.0f));
        assertTrue(loadUnloadFloat(-1.0f));
        assertTrue(loadUnloadFloat(Float.MAX_VALUE));
        assertTrue(loadUnloadFloat(Float.MIN_VALUE));
        assertTrue(loadUnloadFloat(-Float.MIN_VALUE));
        // comparing NaN to NaN SHOULD be false!
        assertFalse(loadUnloadFloat(Float.NaN));
        assertFalse(loadUnloadFloat(-Float.NaN));
        assertTrue(loadUnloadFloat(Float.NEGATIVE_INFINITY));
        assertTrue(loadUnloadFloat(Float.POSITIVE_INFINITY));
    }

    private boolean loadUnloadLong(long d) {
        ByteArray ba = new ByteArray();
        ba.add(d);
        return (d == ba.getLong()) ? true : false;
    }

    @Test
    public void longTest() {
        for (int t = 0; t < randIterations; t++) {
            assertTrue(loadUnloadLong(rand.nextLong()));
        }
        assertTrue(loadUnloadLong(1));
        assertTrue(loadUnloadLong(0));
        assertTrue(loadUnloadLong(-1));
        assertTrue(loadUnloadLong(Long.MAX_VALUE));
        assertTrue(loadUnloadLong(Long.MIN_VALUE));
    }

    private boolean loadUnloadInt(int d) {
        ByteArray ba = new ByteArray();
        ba.add(d);
        return (d == ba.getInt()) ? true : false;
    }

    @Test
    public void intTest() {
        for (int t = 0; t < randIterations; t++) {
            assertTrue(loadUnloadInt(rand.nextInt()));
        }
        assertTrue(loadUnloadInt(1));
        assertTrue(loadUnloadInt(0));
        assertTrue(loadUnloadInt(-1));
        assertTrue(loadUnloadInt(Integer.MAX_VALUE));
        assertTrue(loadUnloadInt(Integer.MIN_VALUE));
    }

    private boolean loadUnloadShort(short d) {
        ByteArray ba = new ByteArray();
        ba.add(d);
        return d == ba.getShort();
    }

    @Test
    public void shortTest() {
        for (int t = 0; t < randIterations; t++) {
            assertTrue(loadUnloadShort(Integer.valueOf(rand.nextInt()).shortValue()));
        }
        assertTrue(loadUnloadShort((short) 1));
        assertTrue(loadUnloadShort((short) 0));
        assertTrue(loadUnloadShort((short) -1));
        assertTrue(loadUnloadShort(Short.MAX_VALUE));
        assertTrue(loadUnloadShort(Short.MIN_VALUE));
    }

    private boolean loadUnloadByte(byte d) {
        ByteArray ba = new ByteArray();
        ba.add(d);
        return (d == ba.getByte()) ? true : false;
    }

    @Test
    public void byteTest() {
        for (int t = 0; t < randIterations; t++) {
            assertTrue(loadUnloadLong(rand.nextLong()));
        }
        assertTrue(loadUnloadByte((byte) 1));
        assertTrue(loadUnloadByte((byte) 0));
        assertTrue(loadUnloadByte((byte) -1));
        assertTrue(loadUnloadByte(Byte.MAX_VALUE));
        assertTrue(loadUnloadByte(Byte.MIN_VALUE));
    }

    @Test
    public void charEncoderTest() throws CharacterCodingException {
        String s = RandUtils.nextUTF8String(500, 500);
        CharsetEncoder enc = StandardCharsets.UTF_8.newEncoder();
        CharsetDecoder dec = StandardCharsets.UTF_8.newDecoder();
        CharBuffer cb = CharBuffer.wrap(s);
        ByteBuffer bb = enc.encode(CharBuffer.wrap(s));
        bb.rewind();
        byte[] ba = new byte[bb.remaining()];
        bb.get(ba);
        CharBuffer dcb = dec.decode(ByteBuffer.wrap(ba));
        assertEquals(cb, dcb);
        assertEquals(cb.toString(), dcb.toString());
    }

    @Test
    public void badCharEncoderTest() throws CharacterCodingException {


        String s1 = RandUtils.nextUTF8String(5, 5);
        String s2 = RandUtils.nextUTF8String(5, 5);

        String s = s1 + "‥" + s2;
        String testResult = s1 + "‥" + s2;

        CharsetEncoder enc = StandardCharsets.UTF_8.newEncoder();
        enc.onMalformedInput(CodingErrorAction.REPLACE);
        enc.onUnmappableCharacter(CodingErrorAction.REPLACE);
        enc.replaceWith("$".getBytes());

        CharsetDecoder dec = StandardCharsets.UTF_8.newDecoder();
        CharBuffer cb = CharBuffer.wrap(s);
        ByteBuffer bb = enc.encode(CharBuffer.wrap(s));
        bb.rewind();
        byte[] ba = new byte[bb.remaining()];
        bb.get(ba);

        CharBuffer dcb = dec.decode(ByteBuffer.wrap(ba));
        assertEquals(testResult, dcb.toString());
    }

    private void loadUnloadString(String s) {
        ByteArray ba = new ByteArray();
        ba.add(s);
        String testString = ba.getString();
        if (s == null) {
            assertTrue(testString == null);
        } else {
            // Log.trace("original: "+s.length());
            // Log.trace("test: "+testString.length());
            assertEquals(s, testString);
            // if your tests fail on this line, it's because your JVM isn't running with a UTF-8 character set by default.
            // Cereal should only ever be used with UTF-8, and it is not compatible with other character sets.
        }
    }

    @Test
    public void localeTest() {
        assertEquals(Charset.defaultCharset().toString(), StandardCharsets.UTF_8.toString());
        assertEquals(Charset.defaultCharset(), StandardCharsets.UTF_8);
    }

    @Test
    public void stringTest() {
        loadUnloadString(new String());
        loadUnloadString(null);
        for (int t = 0; t < randIterations; t++) {
            loadUnloadString(RandUtils.nextUTF8String(50, 50));
        }
    }


    @Test
    public void binaryStringTest() throws CharacterCodingException {
        int len = 50000;
        StringBuffer sb = new StringBuffer(len);
        while (sb.length() < len) {
            char c = (char) (RandUtils.nextInt());
            sb.append(c);
        }

        CharsetEncoder enc = StandardCharsets.UTF_8.newEncoder();
        enc.onMalformedInput(CodingErrorAction.REPLACE);
        enc.onUnmappableCharacter(CodingErrorAction.REPLACE);
        enc.replaceWith("?".getBytes());
        byte[] ba;
        ByteBuffer bb = enc.encode(CharBuffer.wrap(sb.toString()));
        bb.rewind();
        ba = new byte[bb.remaining()];
        bb.get(ba);

        // don't throw an exception...
    }


    private void loadUnloadByteArray(byte[] bb) {
        ByteArray ba = new ByteArray();
        ba.addByteArray(bb);
        assertArrayEquals(bb, ba.getByteArray());
    }

    @Test
    public void byteArrayTest() {
        loadUnloadByteArray(new byte[0]);
        loadUnloadByteArray(null);
        for (int t = 0; t < randIterations; t++) {
            loadUnloadByteArray(RandUtils.nextByteArray(500));
        }
        for (byte t = Byte.MIN_VALUE; t < Byte.MAX_VALUE; t++) {
            byte[] bq = new byte[1];
            bq[0] = t;
            loadUnloadByteArray(bq);
        }
    }

    @Test
    void cerealizableArrayTest() {
        final ConverterTest converterTest = new ConverterTest();
        Forecast[] array = new Forecast[15];
        for (int t = 0; t < array.length; t++) {
            array[t] = converterTest.getRandomForecast(true);
        }

        ByteArray ba = new ByteArray();
        ba.add(array);
        final Forecast[] testArray = ba.getArray(Forecast.class);
        assertArrayEquals(array, testArray);
    }

    @Test
    void cerealizable2DArrayTest() {
        final ConverterTest converterTest = new ConverterTest();
        Forecast[][] array = new Forecast[5][6];
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                array[x][y] = converterTest.getRandomForecast(true);
            }
        }

        ByteArray ba = new ByteArray();
        ba.add(array);
        final Forecast[][] testArray = ba.get2DArray(Forecast.class);
        assertArrayEquals(array, testArray);
    }

    @Test
    void cerealizable3DArrayTest() {
        final ConverterTest converterTest = new ConverterTest();
        Forecast[][][] array = new Forecast[5][6][7];
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                for (int z = 0; z < array[x][y].length; z++) {
                    array[x][y][z] = converterTest.getRandomForecast(true);
                }
            }
        }

        ByteArray ba = new ByteArray();
        ba.add(array);
        final Forecast[][][] testArray = ba.get3DArray(Forecast.class);
        assertArrayEquals(array, testArray);
    }


    @Test
    public void complexTest() {
        final int runTimes = 50;
        final int elements = 50;

        for (int r = 0; r < runTimes; r++) {
            ByteArray ba = new ByteArray();
            LinkedList<Tuple<Integer, Object>> list = new LinkedList<>();
            for (int e = 0; e < elements; e++) {
                int type = RandUtils.nextInt(10);
                switch (type) {
                    case 0:
                        double dvalue = rand.nextDouble();
                        ba.add(dvalue);
                        list.add(new Tuple<Integer, Object>(type, dvalue));
                        break;
                    case 1:
                        float fvalue = rand.nextFloat();
                        ba.add(fvalue);
                        list.add(new Tuple<Integer, Object>(type, fvalue));
                        break;
                    case 2:
                        long lvalue = rand.nextLong();
                        ba.add(lvalue);
                        list.add(new Tuple<Integer, Object>(type, lvalue));
                        break;
                    case 3:
                        int ivalue = rand.nextInt();
                        ba.add(ivalue);
                        list.add(new Tuple<Integer, Object>(type, ivalue));
                        break;
                    case 4:
                        short svalue = (short) rand.nextInt();
                        ba.add(svalue);
                        list.add(new Tuple<Integer, Object>(type, svalue));
                        break;
                    case 5:
                        byte bvalue = (byte) rand.nextInt();
                        ba.add(bvalue);
                        list.add(new Tuple<Integer, Object>(type, bvalue));
                        break;
                    case 6:
                        boolean ovalue = rand.nextBoolean();
                        ba.add(ovalue);
                        list.add(new Tuple<Integer, Object>(type, ovalue));
                        break;
                    case 7:
                        String tvalue = RandUtils.nextUTF8String(1000, 2000);
                        ba.add(tvalue);
                        list.add(new Tuple<Integer, Object>(type, tvalue));
                        break;
                    case 8:
                        byte[] avalue = RandUtils.nextByteArray(RandUtils.nextInt(1000, 2000));
                        ba.addByteArray(avalue);
                        list.add(new Tuple<Integer, Object>(type, avalue));
                        break;
                    case 9:
                        UUID uvalue = UUIDType5.nameUUIDFromNamespaceAndString(UUIDType5.NAMESPACE_OID,
                                                                               RandUtils
                                                                                       .nextUTF8String(1000,
                                                                                                       2000));
                        ba.add(uvalue);
                        list.add(new Tuple<Integer, Object>(type, uvalue));
                        break;
                }
            }

            assertEquals(list.size(), elements);

            for (int e = 0; e < elements; e++) {
                Tuple<Integer, Object> t = list.get(e);
                int type = t.getLeft();
                switch (type) {
                    case 0:
                        double dvalue = (double) t.getRight();
                        double dactual = ba.getDouble();
                        assertEquals(dvalue, dactual);
                        break;
                    case 1:
                        float fvalue = (float) t.getRight();
                        float factual = ba.getFloat();
                        assertEquals(fvalue, factual);
                        break;
                    case 2:
                        long lvalue = (long) t.getRight();
                        long lactual = ba.getLong();
                        assertEquals(lvalue, lactual);
                        break;
                    case 3:
                        int ivalue = (int) t.getRight();
                        int iactual = ba.getInt();
                        assertEquals(ivalue, iactual);
                        break;
                    case 4:
                        short svalue = (short) t.getRight();
                        short sactual = ba.getShort();
                        assertEquals(svalue, sactual);
                        break;
                    case 5:
                        byte bvalue = (byte) t.getRight();
                        byte bactual = ba.getByte();
                        assertEquals(bvalue, bactual);
                        break;
                    case 6:
                        boolean ovalue = (Boolean) t.getRight();
                        boolean oactual = ba.getBoolean();
                        assertEquals(ovalue, oactual);
                        break;
                    case 7:
                        String tvalue = (String) t.getRight();
                        String tactual = ba.getString();
                        assertEquals(tvalue, tactual);
                        break;
                    case 8:
                        byte[] avalue = (byte[]) t.getRight();
                        byte[] aactual = ba.getByteArray();
                        assertArrayEquals(avalue, aactual);
                        break;
                    case 9:
                        UUID uvalue = (UUID) t.getRight();
                        UUID uactual = ba.getUUID();
                        assertEquals(uvalue, uactual);
                        break;
                }
            }
        }
    }

    @Test
    @DisplayName("Data is correctly encoded/decoded when used with cerealizer")
    void data_with_cerealizer() {
        // Setup
        final ByteArray ba = new ByteArray();
        ba.add(new StringCerealizer(), "Somestring");
        // Exercise
        // Verify
        final ByteArray ba2 = new ByteArray(ba);
        assertEquals("Somestring", ba.get(new StringCerealizer()));
    }


    @Test
    @DisplayName("Data array is correctly encoded/decoded when used with cerealizer")
    void array_with_cerealizer() {
        // Setup
        final ByteArray ba = new ByteArray();
        ba.add(new StringCerealizer(), new String[]{"Ss1", "Ss2", "Ss3"});
        // Exercise
        // Verify
        final ByteArray ba2 = new ByteArray(ba);
        final String[] array = ba2.getArray(new StringCerealizer(), String.class);
        assertArrayEquals(new String[]{"Ss1", "Ss2", "Ss3"}, array);
    }

    @Test
    @DisplayName("ByteArray is empty after reset")
    void reset_array() {
        final ByteArray ba = new ByteArray();
        assertEquals(0, ba.length());
        ba.add(29378);
        ba.add(9237);
        assertEquals(8, ba.length());
        ba.reset();
        assertEquals(0, ba.length());
    }

    @Test
    @DisplayName("Constructor with ByteBuffer")
    void constructor_with_ByteBuffer() {
        byte[] bytes = RandUtils.nextByteArray(50);
        ByteArray ba = new ByteArray(ByteBuffer.wrap(bytes));
        assertArrayEquals(bytes, ba.getAllBytes());
    }

    @Test
    @DisplayName("Constructor with ByteBuffer respects the ByteBuffer position")
    void constructor_with_ByteBuffer_position() {
        byte[] bytes = RandUtils.nextByteArray(50);

        byte[] last30Bytes = new byte[30];
        System.arraycopy(bytes, 20, last30Bytes, 0, 30);

        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.position(20);
        assertEquals(30, bb.remaining());

        ByteArray ba = new ByteArray(bb);
        bb.position(20);

        assertEquals(30, ba.length());
        assertEquals(30, bb.remaining());
        byte[] wyf = new byte[30];
        bb.get(wyf);
        bb.position(20);

        assertArrayEquals(last30Bytes, wyf);
        byte[] allBytes = ba.getAllBytes();

        assertArrayEquals(last30Bytes, allBytes);

    }

    @Test
    @DisplayName("wrap with ByteBuffer")
    void wrap_with_ByteBuffer() {
        byte[] bytes = RandUtils.nextByteArray(50);
        ByteArray ba = ByteArray.wrap(ByteBuffer.wrap(bytes));
        assertArrayEquals(bytes, ba.getAllBytes());
    }

    @Test
    @DisplayName("wrap with byte array")
    void wrap_with_byte_array() {
        byte[] bytes = RandUtils.nextByteArray(50);
        ByteArray ba = ByteArray.wrap(bytes);
        assertArrayEquals(bytes, ba.getAllBytes());
    }

    @Test
    @DisplayName("add integer to front test")
    void add_integer_to_front_test() {
        byte[] bytes = RandUtils.nextByteArray(50);
        ByteArray ba = ByteArray.wrap(bytes);

        ByteArray expected = new ByteArray();
        int randInt = RandUtils.nextInt();
        expected.add(randInt);
        expected.addRawBytes(bytes);

        ba.addToFront(randInt);
        assertArrayEquals(expected.getAllBytes(), ba.getAllBytes());
    }



    @Test
    @DisplayName("add byte array to front test")
    void add_byte_array_to_front_test() {
        byte[] bytes1 = RandUtils.nextByteArray(50);
        byte[] bytes2 = RandUtils.nextByteArray(20);

        ByteArray test = new ByteArray();
        test.addByteArray(bytes2);
        test.addByteArrayToFront(bytes1);

        ByteArray expected = new ByteArray();
        expected.addByteArray(bytes1);
        expected.addByteArray(bytes2);

        assertArrayEquals(expected.getAllBytes(), test.getAllBytes());
    }


    @Test
    @DisplayName("add string to front test")
    void add_string_to_front_test() {
        byte[] bytes1 = RandUtils.nextByteArray(50);
        String s = RandUtils.nextUTF8String(10, 20);

        ByteArray test = new ByteArray();
        test.addByteArray(bytes1);
        test.addToFront(s);

        ByteArray expected = new ByteArray();
        expected.add(s);
        expected.addByteArray(bytes1);

        assertArrayEquals(expected.getAllBytes(), test.getAllBytes());
    }


    @Test
    @DisplayName("byte array is empty after getAllBytes()")
    void ba_empty_after_getAllBytes_test() {
        Forecast randomForecast = new ConverterTest().getRandomForecast(true);
        ByteArray original = ByteArray.cerealize(randomForecast);
        original.getAllBytes();
        assertEquals(0, original.length());
    }

    @Test
    @DisplayName("byte array remains the same after copyAllBytes()")
    void ba_same_after_copyAllBytes_test() {
        Forecast randomForecast = new ConverterTest().getRandomForecast(true);
        ByteArray original = ByteArray.cerealize(randomForecast);
        int originalLength = original.length();
        byte[] copyBytes = original.copyAllBytes();
        assertEquals(originalLength, original.length());
    }

    @Test
    @DisplayName("copy all bytes and get all bytes results are equal")
    void copy_all_bytes_and_get_all_bytes_results_are_equal() {
        Forecast randomForecast = new ConverterTest().getRandomForecast(true);
        ByteArray original = ByteArray.cerealize(randomForecast);
        byte[] copyBytes = original.copyAllBytes();
        byte[] getAllBytes = original.getAllBytes();
        assertArrayEquals(copyBytes, getAllBytes);
    }


    @Test
    @DisplayName("read from input stream")
    void read_from_input_stream() throws IOException {
        Forecast randomForecast = new ConverterTest().getRandomForecast(true);
        ByteArray original = ByteArray.cerealize(randomForecast);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.writeBytes(original.getAllBytes());
        byte[] buffer = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);

        ByteArray byteArray = ByteArray.fromInputStream(bais);
        Forecast test = byteArray.uncerealize(Forecast.class);

        assertEquals(randomForecast, test);


    }

}
