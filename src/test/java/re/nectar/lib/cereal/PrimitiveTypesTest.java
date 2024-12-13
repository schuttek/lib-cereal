package re.nectar.lib.cereal;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimitiveTypesTest {

    private static final int iterations = 10;
    @Test
    void booleanTrueTest() {
        final CerealBoolean cerealBoolean = new CerealBoolean(true);
        final ByteArray cerealize = ByteArray.cerealize(cerealBoolean);
        final CerealBoolean uncerealize = cerealize.uncerealize(CerealBoolean.class);
        assertEquals(cerealBoolean, uncerealize);
    }

    @Test
    void booleanFalseTest() {
        final CerealBoolean cerealBoolean = new CerealBoolean(false);
        final ByteArray cerealize = ByteArray.cerealize(cerealBoolean);
        final CerealBoolean uncerealize = cerealize.uncerealize(CerealBoolean.class);
        assertEquals(cerealBoolean, uncerealize);
    }

    @Test
    void byteTest() {
        for (int t=0;t<iterations;t++) {
            byte value = RandUtils.nextByte();
            final CerealByte cereal = new CerealByte(value);
            final ByteArray cerealized = ByteArray.cerealize(cereal);
            final CerealByte uncerealize = cerealized.uncerealize(CerealByte.class);
            assertEquals(value, cereal.getValue());
            assertEquals(value, uncerealize.getValue());
            assertEquals(cereal, uncerealize);
        }
    }
    @Test
    void bytesTest() {
        for (int t=0;t<iterations;t++) {
            byte[] value = RandUtils.nextByteArray(100);
            final CerealBytes cereal = new CerealBytes(value);
            final ByteArray cerealized = ByteArray.cerealize(cereal);
            final CerealBytes uncerealize = cerealized.uncerealize(CerealBytes.class);
            assertArrayEquals(value, cereal.getValue());
            assertArrayEquals(value, uncerealize.getValue());
            assertEquals(cereal, uncerealize);
        }
    }

    @Test
    void doubleTest() {
        for (int t=0;t<iterations;t++) {
            double value = RandUtils.nextDouble();
            final CerealDouble cereal = new CerealDouble(value);
            final ByteArray cerealized = ByteArray.cerealize(cereal);
            final CerealDouble uncerealize = cerealized.uncerealize(CerealDouble.class);
            assertEquals(value, cereal.getValue());
            assertEquals(value, uncerealize.getValue());
            assertEquals(cereal, uncerealize);
        }
    }

    @Test
    void floatTest() {
        for (int t=0;t<iterations;t++) {
            float value = RandUtils.nextFloat();
            final CerealFloat cereal = new CerealFloat(value);
            final ByteArray cerealized = ByteArray.cerealize(cereal);
            final CerealFloat uncerealize = cerealized.uncerealize(CerealFloat.class);
            assertEquals(value, cereal.getValue());
            assertEquals(value, uncerealize.getValue());
            assertEquals(cereal, uncerealize);
        }
    }

    @Test
    void intTest() {
        for (int t=0;t<iterations;t++) {
            int value = RandUtils.nextInt();
            final CerealInteger cereal = new CerealInteger(value);
            final ByteArray cerealized = ByteArray.cerealize(cereal);
            final CerealInteger uncerealize = cerealized.uncerealize(CerealInteger.class);
            assertEquals(value, cereal.getValue());
            assertEquals(value, uncerealize.getValue());
            assertEquals(cereal, uncerealize);
        }
    }

    @Test
    void longTest() {
        for (int t=0;t<iterations;t++) {
            long value = RandUtils.nextLong();
            final CerealLong cereal = new CerealLong(value);
            final ByteArray cerealized = ByteArray.cerealize(cereal);
            final CerealLong uncerealize = cerealized.uncerealize(CerealLong.class);
            assertEquals(value, cereal.getValue());
            assertEquals(value, uncerealize.getValue());
            assertEquals(cereal, uncerealize);
        }
    }

    @Test
    void shortTest() {
        for (int t=0;t<iterations;t++) {
            short value = RandUtils.nextShort();
            final CerealShort cereal = new CerealShort(value);
            final ByteArray cerealized = ByteArray.cerealize(cereal);
            final CerealShort uncerealize = cerealized.uncerealize(CerealShort.class);
            assertEquals(value, cereal.getValue());
            assertEquals(value, uncerealize.getValue());
            assertEquals(cereal, uncerealize);
        }
    }
    @Test
    void stringTest() {
        for (int t=0;t<iterations;t++) {
            String value = RandUtils.nextUTF8String(60,200);
            final CerealString cereal = new CerealString(value);
            final ByteArray cerealized = ByteArray.cerealize(cereal);
            final CerealString uncerealize = cerealized.uncerealize(CerealString.class);
            assertEquals(value, cereal.getValue());
            assertEquals(value, uncerealize.getValue());
            assertEquals(cereal, uncerealize);
        }
    }
    @Test
    void uuidTest() {
        for (int t=0;t<iterations;t++) {
            UUID value = UUID.randomUUID();
            final CerealUUID cereal = new CerealUUID(value);
            final ByteArray cerealized = ByteArray.cerealize(cereal);
            final CerealUUID uncerealize = cerealized.uncerealize(CerealUUID.class);
            assertEquals(value, cereal.getValue());
            assertEquals(value, uncerealize.getValue());
            assertEquals(cereal, uncerealize);
        }
    }



}
