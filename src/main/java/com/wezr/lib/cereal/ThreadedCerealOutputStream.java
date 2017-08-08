package com.wezr.lib.cereal;

import org.apache.commons.io.IOUtils;
import org.iq80.snappy.SnappyFramedOutputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

/**
 * This is perhaps a faster version of CerealOutputStream. It cerealizes data in a separate thread,
 * then compresses it an another, Leaving the caller's thread mostly unaffected.
 */

public class ThreadedCerealOutputStream implements AutoCloseable {
    private OutputStream writerOutputStream;
    private PipedInputStream compressorPipedInputStream;
    private PipedOutputStream cerealizerOutputStream;
    private CerealOutputStream cerealOutputStream;
    private OutputStream originalOutputStream;

    private ArrayBlockingQueue<Cerealizable> cerealQueue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_SIZE);
    private Exception exception = null;
    private Thread compresserThread;
    private Thread cerealizerThread;
    private boolean keepRunning = true;

    public enum Compression {
        gzip,
        snappy,
        none;
    }

    private final int bufferSize;
    private final Compression compression;

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024; // 1MB
    private static final int DEFAULT_QUEUE_SIZE = 512;

    public ThreadedCerealOutputStream(OutputStream outputStream) {
        this.bufferSize = DEFAULT_BUFFER_SIZE;
        this.compression = Compression.none;
        this.originalOutputStream = outputStream;
        init();
    }

    public ThreadedCerealOutputStream(OutputStream outputStream, Compression compression) {
        this.bufferSize = DEFAULT_BUFFER_SIZE;
        this.compression = compression;
        this.originalOutputStream = outputStream;
        init();
    }

    public ThreadedCerealOutputStream(OutputStream outputStream, int bufferSize) {
        this.bufferSize = bufferSize;
        this.compression = Compression.none;
        this.originalOutputStream = outputStream;
        init();
    }

    public ThreadedCerealOutputStream(OutputStream outputStream, int bufferSize, Compression compression) {
        this.bufferSize = bufferSize;
        this.compression = compression;
        this.originalOutputStream = outputStream;
        init();
    }

    private void init() {
        try {

            writerOutputStream = new BufferedOutputStream(originalOutputStream, this.bufferSize);

            switch (compression) {
                case gzip:
                    writerOutputStream = new GZIPOutputStream(writerOutputStream);
                    break;
                case snappy:
                    writerOutputStream = new SnappyFramedOutputStream(writerOutputStream);
                    break;
                case none:
                    break;
            }

            cerealizerOutputStream = new PipedOutputStream();
            compressorPipedInputStream = new PipedInputStream(cerealizerOutputStream, bufferSize);

            compresserThread = new Thread() {
                @Override
                public void run() {
                    try {
                        IOUtils.copy(compressorPipedInputStream, writerOutputStream);
                    } catch (IOException e) {
                        exception = e;
                    }
                }
            };

            cerealOutputStream = new CerealOutputStream(cerealizerOutputStream);

            cerealizerThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (keepRunning || !cerealQueue.isEmpty()) {
                            Cerealizable object = cerealQueue.poll(10L, TimeUnit.MILLISECONDS);
                            if (object != null) {
                                cerealOutputStream.write(object);
                            }
                        }
                        cerealOutputStream.close();
                    } catch (Exception e) {
                        exception = e;
                    }
                }
            };

            compresserThread.start();
            cerealizerThread.start();


        } catch (IOException e) {
            exception = e;
        }
    }

    @Override
    public void close() throws Exception {
        if (exception != null) {
            throw exception;
        }
        keepRunning = false;
        compresserThread.join();
        cerealizerThread.join();
        writerOutputStream.close();
        compressorPipedInputStream.close();
        cerealizerOutputStream.close();
        cerealOutputStream.close();
        originalOutputStream.close();

    }

    public void write(Cerealizable object) throws Exception {
        if (exception != null) {
            throw exception;
        } else {
            cerealQueue.put(object);
        }
    }
}
