package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private Map<String, String> errors = new HashMap<String, String>();
	
	public ValidationException(String mensagem) {
		super(mensagem);
	}
	
	//Retornar o map de erros
	public Map<String, String> getErrors() {
		return this.errors;
	}
	
	//Adicionando erro ao map de erros
	public void addError(String fieldName, String errorMessage) {
		this.errors.put(fieldName, errorMessage);
	}
}
