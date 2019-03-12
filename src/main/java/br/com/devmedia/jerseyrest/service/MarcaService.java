package br.com.devmedia.jerseyrest.service;

import java.util.List;

import br.com.devmedia.jerseyrest.model.dao.MarcaDao;
import br.com.devmedia.jerseyrest.model.domain.Marca;

public class MarcaService {
	
	private MarcaDao dao = new MarcaDao();
	
	public List<Marca> findAll(){
		return dao.findAll();
	}
	
	public Marca findById(Long id) {
		return dao.findById(id);
	}
	
	public List<Marca> findByPagination(Integer firstResult, Integer maxResults){
		return dao.findByPagination(firstResult, maxResults);
	}
	
	public List<Marca> findByName(String name){
		return dao.findByName(name);
	}
	
	public Marca save(Marca marca) {
		return dao.save(marca);
	}
	
	public Marca update(Marca marca) {
		return dao.updaet(marca);
	}
	
	public Marca delete(Long id) {
		return dao.delete(id);
	}
	
}
