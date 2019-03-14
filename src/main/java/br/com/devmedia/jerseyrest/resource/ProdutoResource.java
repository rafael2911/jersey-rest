package br.com.devmedia.jerseyrest.resource;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.devmedia.jerseyrest.model.domain.Produto;
import br.com.devmedia.jerseyrest.resource.filter.FilterBean;
import br.com.devmedia.jerseyrest.service.ProdutoService;

@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ProdutoResource {
	
	private ProdutoService service = new ProdutoService();
	
	@GET
	public List<Produto> getProdutos(@PathParam("marcaId") Long marcaId, @BeanParam FilterBean filterBean){
		
		if(filterBean.getOffset() >= 0 && filterBean.getLimit() > 0) {
			return service.findByPagination(marcaId, filterBean.getOffset(), filterBean.getLimit());
		}
		
		if(filterBean.getName() != null) {
			return service.findByName(marcaId, filterBean.getName());
		}
		
		return service.findAll(marcaId);
	}
	
	@GET
	@Path("/{produtoId}")
	public Produto getProduto(@PathParam("produtoId") Long produtoId) {
		return service.findById(produtoId);
	}
	
	@POST
	public Response salvar(@PathParam("marcaId") Long marcaId, Produto produto) {
		service.save(marcaId, produto);
		return Response.status(Status.CREATED).entity(produto).build();
	}
	
	@PUT
	@Path("/{produtoId}")
	public void atualizar(@PathParam("marcaId") Long marcaId, @PathParam("produtoId") Long produtoId, Produto produto) {
		produto.setId(produtoId);
		service.update(marcaId, produto);
	}
	
	@DELETE
	@Path("/{produtoId}")
	public void remover(@PathParam("produtoId") Long produtoId) {
		service.delete(produtoId);
	}
	
}
