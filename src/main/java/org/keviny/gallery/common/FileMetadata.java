package org.keviny.gallery.common;

import java.util.Date;


public class FileMetadata {

	private String filename;
	private String contentType;
	private Object fid;
	private String md5sum;
	private Date uploadTime;
	private Long length;
	
	public FileMetadata() {
		
	}
	
	public FileMetadata(String filename, String contentType) {
		this.filename = filename;
		this.contentType = contentType;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Object getFid() {
		return fid;
	}

	public void setFid(Object fid) {
		this.fid = fid;
	}

	public String getMd5sum() {
		return md5sum;
	}

	public void setMd5sum(String md5sum) {
		this.md5sum = md5sum;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}
}
