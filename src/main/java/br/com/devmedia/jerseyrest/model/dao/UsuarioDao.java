package br.com.devmedia.jerseyrest.model.dao;

import javax.persistence.EntityManager;

import br.com.devmedia.jerseyrest.model.domain.Usuario;

public class UsuarioDao {
	
	public Usuario obterUsuario(Usuario usuario) {
		
		EntityManager em = JpaUtil.getEntityManager();
		
		return em.createQuery("FROM Usuario u WHERE u.username = :username AND u.password=:password", Usuario.class)
				.setParameter("username", usuario.getUsername())
				.setParameter("password", usuario.getPassword())
				.getSingleResult();
		
	}
	
}
