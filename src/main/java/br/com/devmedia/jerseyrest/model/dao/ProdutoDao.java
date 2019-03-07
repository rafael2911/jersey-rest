package br.com.devmedia.jerseyrest.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.devmedia.jerseyrest.model.domain.Produto;

public class ProdutoDao {
	
	public List<Produto> findAll(){
		
		EntityManager em = JpaUtil.getEntityManager();
		List<Produto> produtos = null;
		
		try {
			produtos = em.createQuery("from Produto p", Produto.class).getResultList();
		}finally {
			em.close();
		}
		
		return produtos;
		
	}
	
	public Produto save(Produto produto) {
		
		EntityManager em = JpaUtil.getEntityManager();
		
		try {
			em.getTransaction().begin();
			em.persist(produto);
			em.getTransaction().commit();
		}finally {
			em.close();
		}
		
		return produto;
		
	}
	
	public Produto update(Produto produto) {
		EntityManager em = JpaUtil.getEntityManager();
		Produto produtoManaged = null;
		
		try {
			em.getTransaction().begin();
			produtoManaged = em.find(Produto.class, produto.getId());
			produtoManaged.setNome(produto.getNome());
			produtoManaged.setQuantidade(produto.getQuantidade());
			em.getTransaction().commit();
		}finally {
			em.close();
		}
		
		return produtoManaged;
	}
	
	public Produto delete(Long id) {
		EntityManager em = JpaUtil.getEntityManager();
		Produto produto = null;
		
		try {
			em.getTransaction().begin();
			produto = em.find(Produto.class, id);
			em.remove(produto);
			em.getTransaction().commit();
		}finally {
			em.close();
		}
		
		return produto;
	}
	
}
