package de.cluetec.showcase.error;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestError {

	@JsonProperty("timestamp")
	private Date timestamp;

	@JsonProperty("status")
	private int statusCode;

	@JsonProperty("error")
	private String error;

	@JsonProperty("message")
	private String errorMessage;

	public RestError(HttpStatus statusCode, String error, String errorMessage) {
		super();
		this.timestamp = new Date(System.currentTimeMillis());
		this.statusCode = statusCode.value();
		this.error = error;
		this.errorMessage = errorMessage;
	}

	public RestError(Date timestamp, HttpStatus statusCode, String error, String errorMessage) {
		super();
		this.timestamp = timestamp;
		this.statusCode = statusCode.value();
		this.error = error;
		this.errorMessage = errorMessage;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
