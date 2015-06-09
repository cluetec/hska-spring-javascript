package de.cluetec.showcase.exceptions;

public class TodoAppInvalidArgumentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TodoAppInvalidArgumentException() {
		super();
	}

	public TodoAppInvalidArgumentException(String message) {
		super(message);
	}

	public TodoAppInvalidArgumentException(String message, Throwable cause) {
		super(message, cause);
	}
}