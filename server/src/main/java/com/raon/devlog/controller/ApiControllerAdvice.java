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
	public ResponseEntity<ApiResponse<?>> handleDevlogException(DevlogException e) {
		switch (e.getErrorType().getLogLevel()) {
			case ERROR -> log.error("CoreException : {}", e.getMessage(), e);
			case WARN -> log.warn("CoreException : {}", e.getMessage(), e);
			default -> log.info("CoreException : {}", e.getMessage(), e);
		}

		return new ResponseEntity<>(ApiResponse.error(e.getErrorType(), e.getData()), e.getErrorType().getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
		log.error("Exception : {}", e.getMessage(), e);
		return new ResponseEntity<>(ApiResponse.error(ErrorType.DEFAULT_ERROR), ErrorType.DEFAULT_ERROR.getStatus());
	}
}
