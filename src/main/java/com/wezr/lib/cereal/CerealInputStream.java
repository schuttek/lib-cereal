package com.wezr.lib.cereal;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class CerealInputStream extends InputStream {

	private final DataInputStream inputStream;

	public CerealInputStream(InputStream inputStream) {
		this.inputStream = new DataInputStream(inputStream);
	}

	public <T> Optional<T> read(Class<T> cerealClass)
			throws InstantiationException, IllegalAccessException, IOException {
		try {
			byte[] byteLengthBuffer = new byte[4];
			inputStream.readFully(byteLengthBuffer);
			int length = ByteArray.bytesToInt(byteLengthBuffer, 0);
			byte[] cerealizedObjectBuffer = new byte[length];
			inputStream.readFully(cerealizedObjectBuffer);
			ByteArray ba = new ByteArray(cerealizedObjectBuffer);
			T cerealObject = cerealClass.newInstance();
			((Cerealizable) cerealObject).uncerealizeFrom(ba);
			return Optional.of(cerealObject);
		} catch (EOFException e) {
			return Optional.empty();
		}
	}

	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

}
