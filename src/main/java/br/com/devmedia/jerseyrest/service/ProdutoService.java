package br.com.devmedia.jerseyrest.service;

import java.util.List;

import br.com.devmedia.jerseyrest.model.dao.ProdutoDao;
import br.com.devmedia.jerseyrest.model.domain.Produto;

public class ProdutoService {
	
	private ProdutoDao dao = new ProdutoDao();
	
	public List<Produto> findAll(){
		return this.dao.findAll();
	}
	
	public Produto save(Produto produto) {
		return dao.save(produto);
	}
	
	public Produto update(Produto produto) {
		return dao.update(produto);
	}
	
	public Produto delete(Long id) {
		return dao.delete(id);
	}
	
}
