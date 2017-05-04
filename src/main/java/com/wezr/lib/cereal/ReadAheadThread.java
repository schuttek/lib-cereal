package com.wezr.lib.cereal;

import java.io.IOException;

public class ReadAheadThread extends Thread {
	final ReadAheadInputStream readAheadInputStream;

	public ReadAheadThread(ReadAheadInputStream readAheadInputStream) {
		this.readAheadInputStream = readAheadInputStream;
	}

	@Override
	public void run() {
		while (!readAheadInputStream.isClosed() && readAheadInputStream.isBufferSpaceAvailable()) {
			try {
				readAheadInputStream.readNextChunkFromBelow();
			} catch (IOException e) {
				readAheadInputStream.reportExceptionFromBelow(e);
				break;
			}
		}
	}

}
