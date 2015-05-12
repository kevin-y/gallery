package org.keviny.gallery.exception.controller;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

import org.keviny.gallery.common.exception.ErrorCode;
import org.keviny.gallery.common.exception.ErrorCodeException;
import org.keviny.gallery.common.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionController {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionController.class);

	@ExceptionHandler(ErrorCodeException.class)
	public @ResponseBody ResponseEntity<ErrorMessage> handleErrorCodeException(
			ErrorCodeException e) {
		ErrorCode ec = e.getErrorCode();
		LOG.error("ErrorCode: {}, Description: {}", ec.value(), ec.description());
		ErrorMessage err = new ErrorMessage(ec.value(), ec.description());
		return new ResponseEntity<ErrorMessage>(err, HttpStatus.OK);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class) 
	public @ResponseBody ResponseEntity<ErrorMessage> handleFileUploadException(MaxUploadSizeExceededException e) {
		LOG.error(e.getMessage());
		ErrorMessage err = new ErrorMessage(
				HttpStatus.EXPECTATION_FAILED.value(),
				e.getMessage());
		return new ResponseEntity<ErrorMessage>(err, HttpStatus.EXPECTATION_FAILED);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public @ResponseBody ResponseEntity<ErrorMessage> handleRequestMethodException(
			HttpRequestMethodNotSupportedException e) {
		LOG.error(e.getMessage());
		ErrorMessage err = new ErrorMessage(
				HttpStatus.SERVICE_UNAVAILABLE.value(),
				e.getMessage());
		return new ResponseEntity<ErrorMessage>(err, HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@ExceptionHandler(Exception.class)
	public @ResponseBody ResponseEntity<ErrorMessage> handleGenericException(Exception e) {
		LOG.error(e.getMessage());
		e.printStackTrace();
		ErrorMessage err = new ErrorMessage(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		return new ResponseEntity<ErrorMessage>(err, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
