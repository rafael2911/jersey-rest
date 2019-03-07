package br.com.devmedia.jerseyrest.exceptions;

public class DaoException extends RuntimeException {

	private static final long serialVersionUID = 3442508989870928005L;
	
	private Integer code;

	public DaoException(String message, Integer code) {
		super(message);
		this.code = code;
	}
	
	public Integer getCode() {
		return code;
	}
	
}
