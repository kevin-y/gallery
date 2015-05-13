package org.keviny.gallery.common.exception;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public enum ErrorCode {
	RESOURCE_DOES_NOT_EXIST(1000, "Resources does not exist"),
	USER_DOES_NOT_EXIST(2000, "User does not exist"),
	PASSWORD_INCORRECT(2001, "Password incorrect"),
	CONTENT_TYPE_NOT_SUPPORTED(3000, "ContentType not supported"),
	FILE_IS_EMPTY(3001, "File is empty"),
	MAX_UPLOAD_SIZE_EXCEEDED(3002, "Maximum upload size of 10485760 bytes exceeded"),
	FILE_UPLOAD_FAILED(3003, "File upload failed");
	
	
	
	private int value;
	private String description;
	
	ErrorCode(int value, String description) {
		this.value = value;
		this.description = description;
	}
	
	public int value() {
		return this.value;
	}
	
	public String description() {
		return this.description;
	}
}
