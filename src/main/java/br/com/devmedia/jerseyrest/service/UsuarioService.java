package br.com.devmedia.jerseyrest.service;

import javax.persistence.NoResultException;

import br.com.devmedia.jerseyrest.model.dao.UsuarioDao;
import br.com.devmedia.jerseyrest.model.domain.Usuario;

public class UsuarioService {
	
	private final UsuarioDao dao = new UsuarioDao();
	
	public Usuario validarUsuario(Usuario usuario) {
		
		try {
			usuario = dao.obterUsuario(usuario);
		}catch (NoResultException ex) {
			return null;
		}
		
		return usuario;
		
	}
	
}
