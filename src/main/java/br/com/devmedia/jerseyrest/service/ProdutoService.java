package br.com.devmedia.jerseyrest.service;

import java.util.List;

import br.com.devmedia.jerseyrest.model.dao.ProdutoDao;
import br.com.devmedia.jerseyrest.model.domain.Produto;

public class ProdutoService {
	
	private ProdutoDao dao = new ProdutoDao();
	
	public List<Produto> findAll(Long marcaId){
		return this.dao.findAll(marcaId);
	}
	
	public Produto findById(Long id) {
		return dao.findById(id);
	}
	
	public List<Produto> findByPagination(Long marcaId, Integer firstResult, Integer maxResults){
		return dao.findByPagination(marcaId, firstResult, maxResults);
	}
	
	public List<Produto> findByName(Long marcaId, String name){
		return dao.findByName(marcaId, name);
	}
	
	public Produto save(Long marcaId, Produto produto) {
		return dao.save(marcaId, produto);
	}
	
	public Produto update(Long marcaId, Produto produto) {
		return dao.update(marcaId, produto);
	}
	
	public Produto delete(Long produtoId) {
		return dao.delete(produtoId);
	}
	
}
