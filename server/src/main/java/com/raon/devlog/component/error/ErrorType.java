package com.raon.devlog.component.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum ErrorType {
	VALIDATION_ERROR(HttpStatus.BAD_REQUEST, ErrorCode.E400, "validation error has occurred", LogLevel.DEBUG),
	AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, ErrorCode.E401, "authentication error has occurred", LogLevel.DEBUG),
	AUTHORIZATION_ERROR(HttpStatus.FORBIDDEN, ErrorCode.E403, "authorization error has occurred", LogLevel.DEBUG),
	DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred.",
		LogLevel.ERROR);

	private final HttpStatus status;

	private final ErrorCode code;

	private final String message;

	private final LogLevel logLevel;

	ErrorType(HttpStatus status, ErrorCode code, String message, LogLevel logLevel) {

		this.status = status;
		this.code = code;
		this.message = message;
		this.logLevel = logLevel;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public ErrorCode getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}
}
