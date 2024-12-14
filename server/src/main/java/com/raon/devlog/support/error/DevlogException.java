package com.raon.devlog.support.error;

public class DevlogException extends RuntimeException {

	private final ErrorType errorType;

	private final Object data;

	public DevlogException(ErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
		this.data = null;
	}

	public DevlogException(ErrorType errorType, Object data) {
		super(errorType.getMessage());
		this.errorType = errorType;
		this.data = data;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public Object getData() {
		return data;
	}
}
