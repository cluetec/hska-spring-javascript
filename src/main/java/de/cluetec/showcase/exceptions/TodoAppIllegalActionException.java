package de.cluetec.showcase.exceptions;

public class TodoAppIllegalActionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TodoAppIllegalActionException() {
		super();
	}

	public TodoAppIllegalActionException(String message) {
		super(message);
	}

	public TodoAppIllegalActionException(String message, Throwable cause) {
		super(message, cause);
	}
}