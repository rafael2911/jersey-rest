package br.com.devmedia.jerseyrest.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.devmedia.jerseyrest.exceptions.DaoException;
import br.com.devmedia.jerseyrest.exceptions.ErrorCode;
import br.com.devmedia.jerseyrest.model.domain.Produto;

public class ProdutoDao {
	
	public List<Produto> findAll(){
		
		EntityManager em = JpaUtil.getEntityManager();
		List<Produto> produtos = null;
		
		try {
			produtos = em.createQuery("from Produto p", Produto.class).getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao listar produtos: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return produtos;
		
	}
	
	public Produto findById(Long id) {
		EntityManager em = JpaUtil.getEntityManager();
		Produto produto = null;
		
		if(id <= 0) {
			throw new DaoException("O id precisa ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		try {
			produto = em.find(Produto.class, id);
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar produto por id no BD: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		if(produto == null) {
			throw new DaoException("Produto de id " + id + " não encontrado!", ErrorCode.NOT_FOUND);
		}
		
		return produto;
		
	}
	
	public List<Produto> findByPagination(Integer firstResult, Integer maxResults){
		EntityManager em = JpaUtil.getEntityManager();
		List<Produto> produtos = null;
		
		try {
			produtos = em.createQuery("from Produto p", Produto.class)
					.setFirstResult(firstResult-1)
					.setMaxResults(maxResults)
					.getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar produtos com paginação: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		if(produtos.isEmpty()) {
			throw new DaoException("A paginação não retornou elementos!", ErrorCode.NOT_FOUND);
		}
		
		return produtos;
	}
	
	public List<Produto> findByName(String name){
		EntityManager em = JpaUtil.getEntityManager();
		List<Produto> produtos = null;
		
		try {
			produtos = em.createQuery("select p from Produto p where p.nome like :name", Produto.class)
					.setParameter("name", "%" + name + "%")
					.getResultList();
		}catch (RuntimeException ex) {
			throw new DaoException("Erro ao buscar produto por nome: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		if(produtos.isEmpty()) {
			throw new DaoException("A busca não retornou elementos!", ErrorCode.NOT_FOUND);
		}
		
		return produtos;
	}
	
	public Produto save(Produto produto) {
		
		EntityManager em = JpaUtil.getEntityManager();
		
		if(!produtoIsValid(produto)) {
			throw new DaoException("Produto com dados incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		try {
			em.getTransaction().begin();
			em.persist(produto);
			em.getTransaction().commit();
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao salvar produto no BD: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return produto;
		
	}
	
	public Produto update(Produto produto) {
		EntityManager em = JpaUtil.getEntityManager();
		Produto produtoManaged = null;
		
		if(produto.getId() <= 0) {
			throw new DaoException("O id precisa ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		if(!produtoIsValid(produto)) {
			throw new DaoException("Produto com dados incompletos!", ErrorCode.BAD_REQUEST);
		}
		
		try {
			em.getTransaction().begin();
			produtoManaged = em.find(Produto.class, produto.getId());
			produtoManaged.setNome(produto.getNome());
			produtoManaged.setQuantidade(produto.getQuantidade());
			em.getTransaction().commit();
		}catch (NullPointerException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Produto informado para atualização não existe: " + ex.getMessage(), ErrorCode.NOT_FOUND);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao atualizar produto no banco de dados: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return produtoManaged;
	}
	
	public Produto delete(Long id) {
		EntityManager em = JpaUtil.getEntityManager();
		Produto produto = null;
		
		if(id <= 0 ) {
			throw new DaoException("O id precisa ser maior que zero!", ErrorCode.BAD_REQUEST);
		}
		
		try {
			em.getTransaction().begin();
			produto = em.find(Produto.class, id);
			em.remove(produto);
			em.getTransaction().commit();
		}catch (IllegalArgumentException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Produto informado para exclusão não existe: " + ex.getMessage(), ErrorCode.NOT_FOUND);
		}catch (RuntimeException ex) {
			em.getTransaction().rollback();
			throw new DaoException("Erro ao remover produto do BD: " + ex.getMessage(), ErrorCode.SERVER_ERROR);
		}finally {
			em.close();
		}
		
		return produto;
	}
	
	public boolean produtoIsValid(Produto produto) {
		try {
			if(produto.getNome().isEmpty() || produto.getQuantidade() < 0) {
				return false;
			}
		}catch (NullPointerException ex) {
			throw new DaoException("Produto com dados incompletos", ErrorCode.BAD_REQUEST);
		}
		
		return true;
	}
	
}
