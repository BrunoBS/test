package br.com.bancointer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.bancointer.repository.ChaveServidorRepository;

@SpringBootApplication
public class InterApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterApplication.class, args);

	}

}
