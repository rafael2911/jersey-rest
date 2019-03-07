package br.com.devmedia.jerseyrest.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {
	
	private static EntityManagerFactory emf;
	
	public static EntityManager getEntityManager() {
		if(emf == null) {
			emf = Persistence.createEntityManagerFactory("jerseyrest");
		}
		
		return emf.createEntityManager();
	}
	
}
