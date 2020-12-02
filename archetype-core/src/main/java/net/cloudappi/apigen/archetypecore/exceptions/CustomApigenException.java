package net.cloudappi.apigen.archetypecore.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class CustomApigenException extends RuntimeException {

	private transient List<Error> errors = new ArrayList<>();
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

	public CustomApigenException(String key) {
		this.errors.add(new Error(key));
	}

	public CustomApigenException(String key, String element, Object... otherElements) {
		this.errors.add(new Error(key, element, otherElements));
	}

	public CustomApigenException(List<Error> errors) {
		this.errors = errors;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	@Getter
	public static class Error {
		private String key;
		private String element;
		private Object[] otherElements;

		public Error(String key) {
			this.key = key;
		}

		public Error(String key, String element) {
			this.key = key;
			this.element = element;
		}

		public Error(String key, String element, Object[] otherElements) {
			this.key = key;
			this.element = element;
			this.otherElements = otherElements;
		}
	}
}
