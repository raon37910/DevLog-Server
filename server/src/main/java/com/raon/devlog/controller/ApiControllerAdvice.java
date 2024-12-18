package com.raon.devlog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.raon.devlog.support.error.DevlogException;
import com.raon.devlog.support.error.ErrorType;
import com.raon.devlog.support.response.ApiResponse;

@RestControllerAdvice
public class ApiControllerAdvice {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(DevlogException.class)
	public ResponseEntity<ApiResponse<?>> handleDevlogException(DevlogException exception) {
		switch (exception.getErrorType().getLogLevel()) {
			case ERROR -> log.error("CoreException : {}", exception.getMessage(), exception);
			case WARN -> log.warn("CoreException : {}", exception.getMessage(), exception);
			default -> log.info("CoreException : {}", exception.getMessage(), exception);
		}

		return new ResponseEntity<>(ApiResponse.error(exception.getErrorType(), exception.getData()),
			exception.getErrorType().getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<?>> handleException(Exception exception) {
		log.error("Exception : {}", exception.getMessage(), exception);
		return new ResponseEntity<>(ApiResponse.error(ErrorType.DEFAULT_ERROR), ErrorType.DEFAULT_ERROR.getStatus());
	}
}
