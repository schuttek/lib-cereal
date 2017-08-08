package com.wezr.lib.cereal;

import org.apache.commons.io.IOUtils;
import org.iq80.snappy.SnappyFramedInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;


/**
 * This is perhaps a faster way to read Cereal data. It decompresses data from the original input stream in
 * one thread, decerealizes objects in another, leaving your thread to do the actual data processing.
 * <p>
 * The main caveat is that you can only read files with one type of Cerealizeable object,
 * since decerealization is done ahead of time.
 *
 * @param <T>
 */
public class ThreadedCerealInputStream<T> implements AutoCloseable {
    private InputStream readerInputStream;
    private PipedOutputStream decompressorPipedOutputStream;
    private PipedInputStream decerealizerInputStream;
    private final InputStream originalInputStream;
    private CerealInputStream cerealInputStream;

    private final ArrayBlockingQueue<T> cerealQueue = new ArrayBlockingQueue<T>(DEFAULT_CEREAL_QUEUE_SIZE);
    private boolean eofReached = false;
    private ExecutorService executor;
    private final Class<T> cerealClass;
    private Exception exception = null;
    private Thread decompresserThread;
    private Thread decerealizerThread;

    public enum Compression {
        gzip,
        snappy,
        none;
    }

    private final int bufferSize;
    private final Compression compression;

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024; // 1MB
    private static final int DEFAULT_CEREAL_QUEUE_SIZE = 512;

    public ThreadedCerealInputStream(InputStream inputStream, Class<T> cerealClass) {
        this.bufferSize = DEFAULT_BUFFER_SIZE;
        this.compression = Compression.none;
        this.cerealClass = cerealClass;
        this.originalInputStream = inputStream;
        init();
    }

    public ThreadedCerealInputStream(InputStream inputStream, Class<T> cerealClass, Compression compression) {
        this.bufferSize = DEFAULT_BUFFER_SIZE;
        this.compression = compression;
        this.cerealClass = cerealClass;
        this.originalInputStream = inputStream;
        init();
    }

    public ThreadedCerealInputStream(InputStream inputStream, Class<T> cerealClass, int bufferSize) {
        this.bufferSize = bufferSize;
        this.compression = Compression.none;
        this.cerealClass = cerealClass;
        this.originalInputStream = inputStream;
        init();
    }

    public ThreadedCerealInputStream(InputStream inputStream, Class<T> cerealClass, int bufferSize, Compression compression) {
        this.bufferSize = bufferSize;
        this.compression = compression;
        this.cerealClass = cerealClass;
        this.originalInputStream = inputStream;
        init();
    }

    private void init() {
        try {

            readerInputStream = new BufferedInputStream(originalInputStream, bufferSize);

            switch (compression) {
                case gzip:
                    readerInputStream = new GZIPInputStream(readerInputStream);
                    break;
                case snappy:
                    readerInputStream = new SnappyFramedInputStream(readerInputStream, true);
                    break;
                case none:
                    break;
            }

            decompressorPipedOutputStream = new PipedOutputStream();
            decerealizerInputStream = new PipedInputStream(decompressorPipedOutputStream, bufferSize);

            decompresserThread = new Thread() {
                @Override
                public void run() {
                    try {
                        IOUtils.copy(readerInputStream, decompressorPipedOutputStream);
                        decompressorPipedOutputStream.close();
                    } catch (IOException e) {
                        exception = e;
                    }
                }
            };

            cerealInputStream = new CerealInputStream(decerealizerInputStream);

            decerealizerThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (true) {

                            Optional<T> cerealOptional = cerealInputStream.read((Class<T>) cerealClass);
                            if (cerealOptional.isPresent()) {
                                cerealQueue.put(cerealOptional.get());
                            } else {
                                eofReached = true;
                                break;
                            }
                        }
                    } catch (Exception e) {
                        exception = e;
                    }
                }
            };

            decompresserThread.start();
            decerealizerThread.start();

        } catch (IOException e) {
            exception = e;
        }
    }

    @Override
    public void close() throws Exception {
        if (exception != null) {
            throw exception;
        }

        decerealizerThread.interrupt();
        decompresserThread.interrupt();
        decerealizerThread.join();
        decompresserThread.join();

        readerInputStream.close();
        cerealInputStream.close();
        decompressorPipedOutputStream.close();
        decerealizerInputStream.close();
        originalInputStream.close();
    }

    public Optional<T> read() throws Exception {
        while (true) {
            if (exception != null) {
                throw exception;
            } else if (eofReached && cerealQueue.isEmpty()) {
                return Optional.empty();
            } else {
                T object = cerealQueue.poll(100, TimeUnit.MILLISECONDS);
                if (object != null) {
                    return Optional.of(object);
                }
            }
        }
    }
}
