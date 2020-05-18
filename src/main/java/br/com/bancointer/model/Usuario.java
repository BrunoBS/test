package br.com.bancointer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "digitos" })
public class Usuario {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull(message = "Informar um Nome Válido!")
	@Column(length = 2048)
	private String nome;

	@NotNull(message = "Informar um E-mail Válido!")
	@Column(length = 2048)
	private String email;

	public Usuario(Long id, @NotNull(message = "Informar um Nome Válido!") String nome,
			@NotNull(message = "Informar um E-mail Válido!") String email) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;

	}

	public Usuario() {
		super();
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {

		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
