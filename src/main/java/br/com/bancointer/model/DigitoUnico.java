package br.com.bancointer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

@Entity(name = "DigitoUnico")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "usuario", "id", "parametro", "cache" })
public class DigitoUnico {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private String parametroUm;
	@NotNull
	private String parametroDois;

	private Integer resultado = 0;

	@ManyToOne
	private Usuario usuario;

	@Transient
	private boolean cache = Boolean.FALSE;

	public DigitoUnico() {

	}

	public DigitoUnico(@NotBlank String parametroUm, @NotBlank String parametroDois, @NotBlank Integer resultado) {
		super();
		this.parametroUm = parametroUm;
		this.parametroDois = parametroDois;
		this.resultado = resultado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getParametroUm() {
		return parametroUm;
	}

	public void setParametroUm(String parametroUm) {
		this.parametroUm = parametroUm;
	}

	public String getParametroDois() {
		return parametroDois;
	}

	public void setParametroDois(String parametroDois) {
		this.parametroDois = parametroDois;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getResultado() {
		return resultado;
	}

	public void setResultado(Integer resultado) {
		this.resultado = resultado;
	}

	public boolean isCache() {
		return cache;
	}

	public void setCache(boolean cache) {
		this.cache = cache;
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
		DigitoUnico other = (DigitoUnico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
