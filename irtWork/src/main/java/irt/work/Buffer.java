package irt.work;

public class Buffer {
	private static byte[] buffer;
	
	public Buffer(int bufferSize){
		buffer = new byte[bufferSize];
	}

	public static byte[] getBuffer() {
		return buffer;
	}

	public void close() {
		buffer = null;
	}
}
