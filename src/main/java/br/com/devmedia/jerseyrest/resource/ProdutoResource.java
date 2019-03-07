package br.com.devmedia.jerseyrest.resource;

import java.util.List;

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
import br.com.devmedia.jerseyrest.service.ProdutoService;

@Path("/produtos")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ProdutoResource {
	
	private ProdutoService service = new ProdutoService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<Produto> getProdutos(){
		return service.findAll();
	}
	
	@POST
	public Response salvar(Produto produto) {
		service.save(produto);
		return Response.status(Status.CREATED).entity(produto).build();
	}
	
	@PUT
	@Path("/{id}")
	public void atualizar(@PathParam("id") Long id, Produto produto) {
		produto.setId(id);
		service.update(produto);
	}
	
	@DELETE
	@Path("/{id}")
	public void remover(@PathParam("id") Long id) {
		service.delete(id);
	}
	
}
