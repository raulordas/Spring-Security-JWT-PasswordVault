package com.raul.passwordvault.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "accesos")
public class Acceso {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String clave;
	
	@Column(nullable = false)
	private String valor;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	@JsonInclude(Include.NON_NULL)
	private Usuario usuario;

	public Acceso(int id, String clave, String valor, Usuario usuario) {
		super();
		this.id = id;
		this.clave = clave;
		this.valor = valor;
		this.usuario = usuario;
	}
	
	public Acceso() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Acceso [id=" + id + ", clave=" + clave + ", valor=" + valor + ", usuario=" + usuario + "]";
	}
}
