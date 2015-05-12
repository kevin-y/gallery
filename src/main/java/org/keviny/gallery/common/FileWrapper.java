package org.keviny.gallery.common;

public class FileWrapper {
	private byte[] data;
	private FileMetadata metadata;

	public FileWrapper() {

	}

	public FileWrapper(byte[] data, FileMetadata metadata) {
		this.data = data;
		this.metadata = metadata;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public FileMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(FileMetadata metadata) {
		this.metadata = metadata;
	}

}
