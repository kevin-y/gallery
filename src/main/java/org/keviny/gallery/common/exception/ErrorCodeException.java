package org.keviny.gallery.common.exception;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public class ErrorCodeException extends RuntimeException {

	private static final long serialVersionUID = -661321315230390475L;
	
	private ErrorCode errorCode;
	private Object[] params;
	
	public ErrorCodeException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

    public ErrorCodeException(ErrorCode errorCode, Object...params) {
        this.errorCode = errorCode;
        this.params = params;
    }

	public ErrorCode getErrorCode() {
		return this.errorCode;
	}

    public String getMessage() {
        if(params != null && params.length > 0) {
            StringBuilder message = new StringBuilder(errorCode.description());
            for(Object param : params) {
                int i = message.indexOf("%s");
                message.replace(i, i + 2, param.toString());
            }
            return message.toString();
        }
        return errorCode.description();
    }
}
