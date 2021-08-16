package com.wezr.lib.cereal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class CerealCollectionTest {

    public static final int iterations = 50;

    @DisplayName("Loading / unloading from a Cereal Map")
    @Test
    void loadUnloadMapTest() {
        final ConverterTest converterTest = new ConverterTest();


        CerealMap<CerealLong, Forecast> timestampMap = new CerealMap<>(CerealLong.class, Forecast.class);
        for (int t = 0; t < iterations; t++) {
            final Forecast randomForecast = converterTest.getRandomForecast(true);
            final CerealLong cerealLong = new CerealLong();
            cerealLong.setValue(randomForecast.getTimestamp());
            timestampMap.put(cerealLong, randomForecast);
        }


        final ByteArray byteArray = new ByteArray();
        timestampMap.cerealizeTo(byteArray);

        CerealMap<CerealLong, Forecast> timestampMap2 = new CerealMap<>(CerealLong.class, Forecast.class);
        timestampMap2.uncerealizeFrom(byteArray);

        assertIterableEquals(timestampMap.values(), timestampMap2.values());
        assertEquals(timestampMap, timestampMap2);


    }

    @DisplayName("Loading / unloading from a Cereal Set")
    @Test
    void loadUnloadSetTest() {
        final ConverterTest converterTest = new ConverterTest();

        CerealSet<Forecast> timestampSet = new CerealSet<>(Forecast.class);
        for (int t = 0; t < iterations; t++) {
            final Forecast randomForecast = converterTest.getRandomForecast(true);
            timestampSet.add(randomForecast);
        }


        final ByteArray byteArray = new ByteArray();
        timestampSet.cerealizeTo(byteArray);

        CerealSet<Forecast> timestampSet2 = new CerealSet<>(Forecast.class);
        timestampSet2.uncerealizeFrom(byteArray);

        assertIterableEquals(timestampSet, timestampSet2);


    }

    @DisplayName("Loading / unloading from a Cereal Set")
    @Test
    void loadUnloadListtTest() {
        final ConverterTest converterTest = new ConverterTest();

        CerealList<Forecast> timestampSet = new CerealList<>(Forecast.class);
        for (int t = 0; t < iterations; t++) {
            final Forecast randomForecast = converterTest.getRandomForecast(true);
            timestampSet.add(randomForecast);
        }


        final ByteArray byteArray = new ByteArray();
        timestampSet.cerealizeTo(byteArray);

        CerealList<Forecast> timestampSet2 = new CerealList<>(Forecast.class);
        timestampSet2.uncerealizeFrom(byteArray);

        assertIterableEquals(timestampSet, timestampSet2);


    }

    static class CerealLong implements Cerealizable {
        private long getValue() {
            return value;
        }

        private void setValue(final long value) {
            this.value = value;
        }

        private long value;

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

        @Override
        public void cerealizeTo(final ByteArray ba) {
            ba.add(value);
        }

        @Override
        public void uncerealizeFrom(final ByteArray ba) {
            value = ba.getLong();
        }
    }
}
