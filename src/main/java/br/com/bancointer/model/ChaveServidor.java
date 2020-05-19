package br.com.bancointer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

@Entity(name = "ChaveServidor")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "usuario", "id", "privada" })
public class ChaveServidor {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@Column(length = 2048)
	private String privada;

	@NotNull
	@Column(length = 2048)
	private String publica;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrivada() {
		return privada;
	}

	public void setPrivada(String privada) {
		this.privada = privada;
	}

	public String getPublica() {
		return publica;
	}

	public void setPublica(String publica) {
		this.publica = publica;
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
		ChaveServidor other = (ChaveServidor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
