package com.wezr.lib.cereal;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.function.Predicate;


/**
 * Sort the contents of a Cereal File without loading everything into memory.
 * <p>
 * Input files must be uniform in type (ie, created with a CerealOutputStream where every element has the same type).
 * <p>
 * This utility class works by loading small chunks of the input file in memory, sorting the chunks, then writing them to many small temporary files. Finally it reads the temporary files and repeatedly picks the lowest order element from the head of each input file.
 *
 * @param <T>
 */
public class CerealFileSorter<T extends Cerealizable> {

    private final Class<T> clazz;
    private final long blockSize;
    private final Path workspace;
    private final Comparator<T> comparator;
    private int fileCounter;

    /**
     * @param clazz      the type to read and write from the input/output files.
     * @param blockSize  How much data to read into one chunk. Precisely, it is the minimum number of cerealized bytes to read from the file. Uncerealized objects may take up MUCH more memory that their cerealized versions.
     * @param workspace  A preferrably empty directory in which to write temporary files
     * @param comparator determines the sorting order of the objects
     */
    public CerealFileSorter(final Class<T> clazz, final long blockSize, final Path workspace, final Comparator<T> comparator) {
        this.clazz = clazz;
        this.blockSize = blockSize;
        this.workspace = workspace;
        this.comparator = comparator;
    }

    public void sort(Path input, Path output) throws IOException, InstantiationException, IllegalAccessException {
        sort(input, t -> true, output);
    }

    /**
     * Sort objects from the input file and write them, in order, to output file.
     * <p>
     * Input and output are allowed to be the same file, in which case the input file will be overwritten.
     */
    public void sort(Path input, Predicate<T> filter, Path output) throws IOException, InstantiationException, IllegalAccessException {
        try (FileInputStream fis = new FileInputStream(input.toFile());
             FileOutputStream fos = new FileOutputStream(output.toFile())) {
            sort(fis, filter, fos);
        }
    }

    public void sort(InputStream input, OutputStream output) throws IOException, InstantiationException, IllegalAccessException {
        sort(input, t -> true, output);
    }

    public void sort(InputStream input, Predicate<T> filter, OutputStream output) throws IOException, InstantiationException, IllegalAccessException {
        List<File> tempFiles = new LinkedList<>();
        String randomUUID = UUID.randomUUID().toString();
        fileCounter = 0;

        // read from input into segments, sort segments, and write them to temp files.
        try (CerealInputStream cerealInputStream = new CerealInputStream(input)) {
            List<T> block;
            do {
                block = readBlock(cerealInputStream, filter);

                if (!block.isEmpty()) {
                    block.sort(comparator);

                    File tempFile = buildNextTempFile(randomUUID);
                    tempFiles.add(tempFile);

                    try (CerealOutputStream cerealOutputStream =
                                 new CerealOutputStream(new FileOutputStream(tempFile))) {
                        for (T cereal : block) {
                            cerealOutputStream.write(cereal);
                        }
                    }
                }
            } while (!block.isEmpty());
        }

        // read from all the tempfiles, and output the highest priority element to the outputfile;
        try (CerealOutputStream cerealOutputStream = new CerealOutputStream(output)) {
            List<CerealInputStream> tempCerealInputStreams = openTempFiles(tempFiles);
            try {
                final PriorityQueue<ImmutablePair<T, CerealInputStream>> queue =
                        buildPriorityQueues(tempCerealInputStreams);

                while (true) {
                    final ImmutablePair<T, CerealInputStream> first = queue.poll();
                    if (first == null) {
                        break;
                    }
                    cerealOutputStream.write(first.getKey());

                    final Optional<T> readOpt = first.getValue().read(clazz);
                    readOpt.ifPresent(t -> queue.add(new ImmutablePair<>(t, first.getValue())));
                }
            } finally {
                closeTempFiles(tempCerealInputStreams);
                deleteTempFiles(tempFiles);
            }
        }
    }


    private void deleteTempFiles(final List<File> tempFiles) {
        for (File file : tempFiles) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                // ignore
            }
        }
    }

    private void closeTempFiles(final List<CerealInputStream> tempCerealInputStreams) {
        for (CerealInputStream cis : tempCerealInputStreams) {
            try {
                cis.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    private List<CerealInputStream> openTempFiles(final List<File> tempFiles) throws FileNotFoundException {
        List<CerealInputStream> inputStreams = new LinkedList<>();
        for (File tempFile : tempFiles) {
            inputStreams.add(new CerealInputStream(new FileInputStream(tempFile)));
        }
        return inputStreams;
    }

    private PriorityQueue<ImmutablePair<T, CerealInputStream>> buildPriorityQueues(final List<CerealInputStream> tempFiles) throws IllegalAccessException, IOException, InstantiationException {
        PriorityQueue<ImmutablePair<T, CerealInputStream>> queue = new PriorityQueue<>(
                (o1, o2) -> comparator.compare(o1.getKey(), o2.getKey()));
        for (CerealInputStream cerealInputStream : tempFiles) {
            final Optional<T> read = cerealInputStream.read(clazz);
            read.ifPresent(t -> queue.add(new ImmutablePair<>(t, cerealInputStream)));
        }
        return queue;
    }

    private File buildNextTempFile(final String rand) {
        return workspace.resolve(rand + "_tmp_" + String.valueOf(fileCounter++) + (".cereal")).toFile();
    }

    private List<T> readBlock(final CerealInputStream cerealInputStream, final Predicate<T> filter) throws IllegalAccessException, IOException, InstantiationException {
        List<T> block = new ArrayList<>();
        Optional<T> readOpt;
        long startPosition = cerealInputStream.position();
        long readSize = 0;
        do {
            readOpt = cerealInputStream.read(clazz);
            if (readOpt.isPresent() && filter.test(readOpt.get())) {
                block.add(readOpt.get());
                readSize = cerealInputStream.position() - startPosition;
            }
        } while (readOpt.isPresent() && readSize < blockSize);
        return block;
    }

}
