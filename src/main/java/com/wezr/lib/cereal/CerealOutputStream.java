package com.wezr.lib.cereal;

import java.io.IOException;
import java.io.OutputStream;

public class CerealOutputStream extends OutputStream {

	private final OutputStream outputStream;

	public CerealOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void write(Cerealizable cerealizable) throws IOException {
		ByteArray ba = new ByteArray();
		cerealizable.cerealizeTo(ba);
		byte[] byteLengthBuffer = new byte[4];
		byte[] cerealizedObjectBuffer = ba.getAllBytes();
		ByteArray.intToBytes(cerealizedObjectBuffer.length, byteLengthBuffer, 0);
		outputStream.write(byteLengthBuffer);
		outputStream.write(cerealizedObjectBuffer);
	}

	@Override
	public void flush() throws IOException {
		outputStream.flush();
	}

	@Override
	public void close() throws IOException {
		outputStream.close();
	}

	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
	}

	@Override
	public void write(byte b[], int off, int len) throws IOException {
		outputStream.write(b, off, len);
	}
}
