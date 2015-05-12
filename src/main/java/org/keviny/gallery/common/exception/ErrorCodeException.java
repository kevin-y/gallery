package org.keviny.gallery.common.exception;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public class ErrorCodeException extends RuntimeException {

	private static final long serialVersionUID = -661321315230390475L;
	
	private ErrorCode errorCode;
	
	
	public ErrorCodeException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
	public ErrorCode getErrorCode() {
		return this.errorCode;
	}

}
