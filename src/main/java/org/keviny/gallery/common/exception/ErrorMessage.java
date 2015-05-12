package org.keviny.gallery.common.exception;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ErrorMessage implements Serializable {
	private static final long serialVersionUID = -2732373573650217114L;
	
	private String message;
	@JsonProperty("error_code")
	private int code;

	public ErrorMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
