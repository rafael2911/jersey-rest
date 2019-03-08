package br.com.devmedia.jerseyrest.model.domain;

public class ErrorMessage {
	
	private String message;
	private Integer code;
	
	public ErrorMessage() {
		
	}
	
	public ErrorMessage(String message, Integer code) {
		super();
		this.message = message;
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
}
