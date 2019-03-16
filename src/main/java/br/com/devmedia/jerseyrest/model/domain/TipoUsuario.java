package br.com.devmedia.jerseyrest.model.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoUsuario {
	
	CLIENTE("Cliente"),
	FUNCIONARIO("Funcionario"),
	ADMINISTRADOR("Administrador");
	
	private String valor;
	
	private TipoUsuario(String valor) {
		this.valor = valor;
	}
	
	@JsonValue
	public String getValor() {
		return valor;
	}
	
}
