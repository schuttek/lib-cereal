package com.wezr.lib.cereal;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadAheadInputStream extends InputStream {
	private final InputStream inputStream;

	private ReadAheadThread readAheadThread = new ReadAheadThread(this);

	private boolean closed = false;

	/**
	 * Read from the inputStream, and buffer up to bufferSize bytes of data.
	 * Continue reading in a separate thread until bufferSize + chunkSize is
	 * exceeded. As soon as data is read from this input stream, try to refill
	 * the buffer.
	 * 
	 * This InputStream allows very large files to be read from a laggy source
	 * (for example a network file system) at optimal speeds, as long as you
	 * increase the buffer and chunkSize enough. This class should only be used
	 * when there is almost too much RAM available.
	 * 
	 * @param inputStream
	 * @param bufferSize
	 * @param chunkSize
	 */
	public ReadAheadInputStream(InputStream inputStream, int bufferSize, int chunkSize) {
		this.inputStream = new DataInputStream(inputStream);
		readAheadThread.start();
	}

	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

	public int read(byte b[], int off, int len) throws IOException {
		// TODO implement me.
		return 0;
	}

	public void close() throws IOException {
		this.closed = true;
		this.inputStream.close();
	}

	boolean isClosed() {
		return closed;
	}

	boolean isBufferSpaceAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	void readNextChunkFromBelow() throws IOException {
		// TODO Auto-generated method stub

	}

	void reportExceptionFromBelow(IOException e) {
		// TODO Auto-generated method stub

	}
}
