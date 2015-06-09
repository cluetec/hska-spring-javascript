package de.cluetec.showcase.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.cluetec.showcase.exceptions.TodoAppIllegalActionException;
import de.cluetec.showcase.exceptions.TodoAppInvalidArgumentException;
import de.cluetec.showcase.exceptions.TodoItemNotFoundException;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler({ TodoItemNotFoundException.class })
	public ResponseEntity<RestError> notFoundErrors(Exception ex) {
		RestError restError = new RestError(HttpStatus.NOT_FOUND, "Item Not Found", ex.getMessage());;
		return new ResponseEntity<RestError>(restError, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ TodoAppInvalidArgumentException.class })
	public ResponseEntity<RestError> badRequestErrors(Exception ex) {
		RestError restError = new RestError(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());;
		return new ResponseEntity<RestError>(restError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ TodoAppIllegalActionException.class })
	public ResponseEntity<RestError> conflictErrors(Exception ex) {
		RestError restError = new RestError(HttpStatus.CONFLICT, "Conflict", ex.getMessage());;
		return new ResponseEntity<RestError>(restError, HttpStatus.CONFLICT);
	}
}
