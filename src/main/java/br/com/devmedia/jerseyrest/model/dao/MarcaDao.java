package br.com.devmedia.jerseyrest.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.devmedia.jerseyrest.exceptions.DaoException;
import br.com.devmedia.jerseyrest.exceptions.ErrorCode;
import br.com.devmedia.jerseyrest.model.domain.Marca;

public class MarcaDao {

	public List<Marca> findAll(){
		
		EntityManager em = JpaUtil.getEntityManager();
		List<Marca> marcas = null;
		
		try {
			marcas = em.createQuery("from Marca m", Marca.class).getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro a listar marcas: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		}finally {
			em.close();
		}
		
		return marcas;
		
	}
	
	public Marca findById(Long id) {
		
		if(id <= 0) {
			throw new DaoException("Id deve ser maior que zero!", ErrorCode.BAD_REQUEST.getCode());
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Marca marca = null;
		
		try {
			marca = em.find(Marca.class, id);
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar marca por id: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		}finally {
			em.close();
		}
		
		if(marca == null) {
			throw new DaoException("Marca de Id " + id + " não encontrada!", ErrorCode.NOT_FOUND.getCode());
		}
		
		return marca;
		
	}
	
	public List<Marca> findByPagination(Integer firstResult, Integer maxResults){
		
		EntityManager em = JpaUtil.getEntityManager();
		List<Marca> marcas = null;
		
		try {
			marcas = em.createQuery("from Marca m", Marca.class)
					.setFirstResult(firstResult)
					.setMaxResults(maxResults)
					.getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar marcas com paginação: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		}finally {
			em.close();
		}
		
		if(marcas.isEmpty()) {
			throw new DaoException("A paginação não retornou registros!", ErrorCode.NOT_FOUND.getCode());
		}
		
		return marcas;
		
	}
	
	public List<Marca> findByName(String name){
		
		EntityManager em = JpaUtil.getEntityManager();
		List<Marca> marcas = null;
		
		try {
			marcas = em.createQuery("from Marcas m where m.nome like :name", Marca.class)
					.setParameter("name", "%" + name + "%")
					.getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar marcas por nome: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		}finally {
			em.close();
		}
		
		if(marcas.isEmpty()) {
			throw new DaoException("A busca por nome não retornou registros!", ErrorCode.NOT_FOUND.getCode());
		}
		
		return marcas;
	}
	
	public Marca save(Marca marca) {
		
		if(!marcaIsValid(marca)) {
			throw new DaoException("Marca com dados incompletos!", ErrorCode.BAD_REQUEST.getCode());
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		
		try {
			em.getTransaction().begin();
			em.persist(marca);
			em.getTransaction().commit();
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao salvar marca: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		}finally {
			em.close();
		}
		
		return marca;
		
	}
	
	public Marca updaet(Marca marca) {
		
		if(!marcaIsValid(marca)) {
			throw new DaoException("Marca com dados incompletos!", ErrorCode.BAD_REQUEST.getCode());
		}
		
		if(marca.getId() <= 0) {
			throw new DaoException("O id precisa ser maior que zero!", ErrorCode.BAD_REQUEST.getCode());
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Marca marcaManaged = null;
		
		try {
			em.getTransaction().begin();
			marcaManaged = em.find(Marca.class, marca.getId());
			marcaManaged.setNome(marca.getNome());
			marcaManaged.setCategoria(marca.getCategoria());
			em.getTransaction().commit();
		}catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Marca informada para atualização não existe: " + ex.getMessage(), ErrorCode.NOT_FOUND.getCode());
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao atualizar marca no Banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		}finally {
			em.close();
		}
		
		return marcaManaged;
		
	}
	
	public Marca delete(Long id) {
		
		if(id <= 0) {
			throw new DaoException("O id precisa ser maior que zero!", ErrorCode.BAD_REQUEST.getCode());
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		Marca marca = null;
		
		try {
			em.getTransaction().begin();
			marca = em.find(Marca.class, id);
			em.remove(marca);
			em.getTransaction().commit();
		}catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Marca informada para exclusão não existe: " + ex.getMessage(), ErrorCode.NOT_FOUND.getCode());
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao excluir marca do banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR.getCode());
		}finally {
			em.close();
		}
		
		return marca;
		
	}
	
	private boolean marcaIsValid(Marca marca) {
		try {
			if(marca.getNome().isEmpty() || marca.getCategoria().isEmpty()) {
				return false;
			}
		}catch (NullPointerException ex) {
			throw new DaoException("Marca com dados incompletos!", ErrorCode.BAD_REQUEST.getCode());
		}
		
		return true;
	}
	
}
