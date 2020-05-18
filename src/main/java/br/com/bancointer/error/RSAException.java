package br.com.bancointer.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RSAException extends RuntimeException {
	public RSAException(String message) {
		super(message);
	}
}