package de.cluetec.showcase.exceptions;

public class TodoItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TodoItemNotFoundException() {
		super();
	}

	public TodoItemNotFoundException(String message) {
		super(message);
	}

	public TodoItemNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}