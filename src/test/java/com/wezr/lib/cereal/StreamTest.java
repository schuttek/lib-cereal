package com.wezr.lib.cereal;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class StreamTest {


    public static final String testDataFilename = "./src/test/resources/test_data.cereal.gz";
    public static final String testTempFilename = "./src/test/resources/test_temp.cereal.gz";


    @AfterEach
    public void cleanup() throws IOException {
        Files.delete(new File(testTempFilename).toPath());
    }

    @Test
    public void ThreadedCerealInputStreamTest() throws Exception {

        try (ThreadedCerealInputStream<Forecast> inputStream = new ThreadedCerealInputStream<Forecast>(
                new FileInputStream(testDataFilename), Forecast.class, ThreadedCerealInputStream.Compression.gzip)) {
            try (CerealOutputStream outputStream = new CerealOutputStream(
                    new GZIPOutputStream(new FileOutputStream(testTempFilename)))) {

                Optional<Forecast> fo;
                while (true) {
                    fo = inputStream.read();
                    if (fo.isPresent()) {
                        outputStream.write(fo.get());
                    } else {
                        break;
                    }
                }
            }

        }
        assertArrayEquals(getFileMD5Sum(testDataFilename), getFileMD5Sum(testTempFilename));
    }


    @Test
    public void ThreadedCerealOutputStreamTest() throws Exception {

        try (ThreadedCerealOutputStream outputStream = new ThreadedCerealOutputStream(
                new FileOutputStream(testTempFilename), ThreadedCerealOutputStream.Compression.gzip)) {
            try (CerealInputStream inputStream = new CerealInputStream(
                    new GZIPInputStream(new FileInputStream(testDataFilename)))) {

                Optional<Forecast> fo;
                while (true) {
                    fo = inputStream.read(Forecast.class);
                    if (fo.isPresent()) {
                        outputStream.write(fo.get());
                    } else {
                        break;
                    }
                }
            }

        }
        assertArrayEquals(getFileMD5Sum(testDataFilename), getFileMD5Sum(testTempFilename));
    }


    @Test
    public void snappyTest() throws Exception {
        try (ThreadedCerealInputStream<Forecast> inputStream = new ThreadedCerealInputStream<Forecast>(
                new FileInputStream(testDataFilename), Forecast.class, ThreadedCerealInputStream.Compression.gzip)) {
            try (ThreadedCerealOutputStream outputStream = new ThreadedCerealOutputStream(
                    new FileOutputStream(testTempFilename), ThreadedCerealOutputStream.Compression.snappy)) {
                Optional<Forecast> fo;
                while (true) {
                    fo = inputStream.read();
                    if (fo.isPresent()) {
                        outputStream.write(fo.get());
                    } else {
                        break;
                    }
                }
            }
        }

        try (ThreadedCerealInputStream<Forecast> gzipInputStream = new ThreadedCerealInputStream<Forecast>(
                new FileInputStream(testDataFilename), Forecast.class, ThreadedCerealInputStream.Compression.gzip)) {
            try (ThreadedCerealInputStream<Forecast> snappyInputStream = new ThreadedCerealInputStream<Forecast>(
                    new FileInputStream(testTempFilename), Forecast.class,
                    ThreadedCerealInputStream.Compression.snappy)) {

                Optional<Forecast> gzfo, snfo;
                while (true) {
                    gzfo = gzipInputStream.read();
                    snfo = snappyInputStream.read();
                    if (gzfo.isPresent()) {
                        assertEquals(gzfo.get(), snfo.get());
                    } else {
                        break;
                    }
                }
            }

        }
    }

    @Test
    public void speedTest() throws Exception {

        List<Forecast> forecastList = new LinkedList<>();


        try (ThreadedCerealInputStream<Forecast> gzipInputStream = new ThreadedCerealInputStream<Forecast>(
                new FileInputStream(testDataFilename), Forecast.class, ThreadedCerealInputStream.Compression.gzip)) {
            while (true) {
                Optional<Forecast> fo = gzipInputStream.read();
                if (fo.isPresent()) {
                    forecastList.add(fo.get());
                } else {
                    break;
                }
            }
        }


        long start = System.nanoTime();
        try (ThreadedCerealOutputStream outputStream = new ThreadedCerealOutputStream(
                new FileOutputStream(testTempFilename), ThreadedCerealOutputStream.Compression.gzip)) {
            for (Forecast f : forecastList) {
                outputStream.write(f);
            }
        }
        long duration = System.nanoTime() - start;
        long baseline = duration;
        System.err.println(forecastList.size() + " objects in " + duration + " ns gzip write " + String
                .format("%.2f", 100.0 * ((double) duration) / ((double) baseline)) + "%");
        start = System.nanoTime();
        try (ThreadedCerealInputStream<Forecast> gzipInputStream = new ThreadedCerealInputStream<Forecast>(
                new FileInputStream(testTempFilename), Forecast.class, ThreadedCerealInputStream.Compression.gzip)) {
            for (Forecast f : forecastList) {
                gzipInputStream.read();
            }
        }
        duration = System.nanoTime() - start;
        System.err.println(forecastList.size() + " objects in " + duration + " ns gzip read " + String
                .format("%.2f", 100.0 * ((double) duration) / ((double) baseline)) + "%");

        start = System.nanoTime();
        try (ThreadedCerealOutputStream outputStream = new ThreadedCerealOutputStream(
                new FileOutputStream(testTempFilename), ThreadedCerealOutputStream.Compression.snappy)) {
            for (Forecast f : forecastList) {
                outputStream.write(f);
            }
        }
        duration = System.nanoTime() - start;
        System.err.println(forecastList.size() + " objects in " + duration + " ns snappy write " + String
                .format("%.2f", 100.0 * ((double) duration) / ((double) baseline)) + "%");

        start = System.nanoTime();
        try (ThreadedCerealInputStream<Forecast> gzipInputStream = new ThreadedCerealInputStream<Forecast>(
                new FileInputStream(testTempFilename), Forecast.class, ThreadedCerealInputStream.Compression.snappy)) {
            for (Forecast f : forecastList) {
                gzipInputStream.read();
            }
        }
        duration = System.nanoTime() - start;
        System.err.println(forecastList.size() + " objects in " + duration + " ns snappy read " + String
                .format("%.2f", 100.0 * ((double) duration) / ((double) baseline)) + "%");


        start = System.nanoTime();
        try (ThreadedCerealOutputStream outputStream = new ThreadedCerealOutputStream(
                new FileOutputStream(testTempFilename), ThreadedCerealOutputStream.Compression.none)) {
            for (Forecast f : forecastList) {
                outputStream.write(f);
            }
        }
        duration = System.nanoTime() - start;
        System.err.println(forecastList.size() + " objects in " + duration + " ns none write " + String
                .format("%.2f", 100.0 * ((double) duration) / ((double) baseline)) + "%");

        start = System.nanoTime();
        try (ThreadedCerealInputStream<Forecast> gzipInputStream = new ThreadedCerealInputStream<Forecast>(
                new FileInputStream(testTempFilename), Forecast.class, ThreadedCerealInputStream.Compression.none)) {
            for (Forecast f : forecastList) {
                gzipInputStream.read();
            }
        }
        duration = System.nanoTime() - start;
        System.err.println(forecastList.size() + " objects in " + duration + " ns none read " + String
                .format("%.2f", 100.0 * ((double) duration) / ((double) baseline)) + "%");


        start = System.nanoTime();
        try (CerealOutputStream outputStream = new CerealOutputStream(
                new GZIPOutputStream(new FileOutputStream(testTempFilename)))) {
            for (Forecast f : forecastList) {
                outputStream.write(f);
            }
        }
        duration = System.nanoTime() - start;
        System.err.println(forecastList.size() + " objects in " + duration + " ns classic gzip write " + String
                .format("%.2f", 100.0 * ((double) duration) / ((double) baseline)) + "%");

        start = System.nanoTime();
        try (CerealInputStream inputStream = new CerealInputStream(
                new GZIPInputStream(new FileInputStream(testTempFilename)))) {
            for (Forecast f : forecastList) {
                inputStream.read(Forecast.class);
            }
        }
        duration = System.nanoTime() - start;
        System.err.println(forecastList.size() + " objects in " + duration + " ns classic gzip read " + String
                .format("%.2f", 100.0 * ((double) duration) / ((double) baseline)) + "%");

    }


    private byte[] getFileMD5Sum(String filename) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(Files.readAllBytes(new File(filename).toPath()));
        return messageDigest.digest();
    }


}
