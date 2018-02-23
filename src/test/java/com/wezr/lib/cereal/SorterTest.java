package com.wezr.lib.cereal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SorterTest {


    public static final int iterations = 5000;
    final ConverterTest converterTest = new ConverterTest();
    final Path inputFile = Paths.get("./src/test/resources/sorter_input.cereal");
    final Path outputFile = Paths.get("./src/test/resources/sorter_output.cereal");

    @AfterEach
    void cleanup() throws IOException {
        Files.delete(inputFile);
        Files.delete(outputFile);
    }

    @Test
    void sortTest() throws IOException, IllegalAccessException, InstantiationException {

        List<Forecast> inputList = new LinkedList<>();

        try (CerealOutputStream cos = new CerealOutputStream(new FileOutputStream(inputFile.toFile()))) {
            for (int t = 0; t < iterations; t++) {
                final Forecast randomForecast = converterTest.getRandomForecast(true);
                inputList.add(randomForecast);
                cos.write(randomForecast);
            }
        }

        CerealFileSorter<Forecast> sorter =
                new CerealFileSorter<>(Forecast.class, 2048,
                                       Paths.get("./src/test/resources/"),
                                       new ForecastComparator());

        sorter.sort(inputFile, outputFile);


        List<Forecast> testList = new LinkedList<>();
        try (CerealInputStream cis = new CerealInputStream(new FileInputStream(outputFile.toFile()))) {
            Optional<Forecast> readOpt;
            do {
                readOpt = cis.read(Forecast.class);
                readOpt.ifPresent(testList::add);
            } while (readOpt.isPresent());
        }
        assertEquals(inputList.size(), testList.size());

        assertTrue(inputList.containsAll(testList));
        assertTrue(testList.containsAll(inputList));

        long lastTimestamp = Long.MIN_VALUE;
        for (Forecast test : testList) {
            assertTrue(lastTimestamp <= test.getTimestamp());
            lastTimestamp = test.getTimestamp();
        }

    }


    public static class ForecastComparator implements Comparator<Forecast> {
        @Override
        public int compare(final Forecast f1, final Forecast f2) {
            return Long.compare(f1.getTimestamp(), f2.getTimestamp());
        }
    }


    @Test
    void largeFileSortTest() throws IOException, IllegalAccessException, InstantiationException {
        int entries = 15 * 15 * 15;

        try (CerealOutputStream cos = new CerealOutputStream(new FileOutputStream(inputFile.toFile()))) {
            for (int t = 0; t < entries; t++) {
                final Forecast randomForecast = converterTest.getRandomForecast(true);
                cos.write(randomForecast);
            }
        }

        CerealFileSorter<Forecast> sorter =
                new CerealFileSorter<>(Forecast.class, 10 * 1024 * 1024,
                                       Paths.get("./src/test/resources/"),
                                       new ForecastComparator());

        sorter.sort(inputFile, outputFile);


        long counter = 0;
        long lastTimestamp = Long.MIN_VALUE;
        try (CerealInputStream cis = new CerealInputStream(new FileInputStream(outputFile.toFile()))) {
            Optional<Forecast> readOpt;
            do {
                readOpt = cis.read(Forecast.class);
                if (readOpt.isPresent()) {
                    counter++;
                    assertTrue(lastTimestamp <= readOpt.get().getTimestamp());
                    lastTimestamp = readOpt.get().getTimestamp();
                }
            } while (readOpt.isPresent());
        }

        assertEquals(entries, counter);
    }

}
